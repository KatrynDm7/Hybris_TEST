/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;
import de.hybris.platform.variants.model.VariantProductModel;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Resolver for product images urls. It selects the first gallery image that supports the requested media format. By
 * default, if parameter mediaFormat is not specified, it tries to retrieve the media format from the indexed property
 * name. By default it finds character "-" and treats following character sequence as a media format. If character "-"
 * is not present, an exception of type {@link FieldValueProviderException} is thrown. The parsing method can be
 * overridden.
 *
 * <h4>Supported parameters:</h4>
 *
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Table showing supported parameters.">
 * <tr bgcolor="#ccccff">
 * <th>Parameter
 * <th>Default value
 * <th>Description
 * <tr valign=top>
 * <td>optional
 * <td>true
 * <td>If false, indicates that the resolved media url should not be null and not an empty string. If these conditions
 * are not met, an exception of type {@link FieldValueProviderException} is thrown.
 * <tr valign=top bgcolor="#eeeeff">
 * <td>mediaFormat
 * <td>
 * <td>If specified, this is the qualifier of the media format.
 * </table>
 * </blockquote>
 */
public class ProductImagesValueResolver extends AbstractValueResolver<ProductModel, Map<String, MediaModel>, Object>
{
	public static final String OPTIONAL_PARAM = "optional";
	public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;

	public static final String MEDIA_FORMAT_PARAM = "mediaFormat";
	public static final String MEDIA_FORMAT_PARAM_DEFAULT_VALUE = null;

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext,
			final IndexedProperty indexedProperty, final ProductModel model,
			final ValueResolverContext<Map<String, MediaModel>, Object> resolverContext) throws FieldValueProviderException
	{
		boolean hasValue = false;

		final String indexedPropertyName = indexedProperty.getName();
		final Map<String, MediaModel> medias = resolverContext.getData();
		final MediaModel media = medias.get(indexedPropertyName);

		if (media != null)
		{
			final String mediaURL = media.getURL();
			if (StringUtils.isNotBlank(mediaURL))
			{
				document.addField(indexedProperty, mediaURL, resolverContext.getFieldQualifier());
				hasValue = true;
			}
		}

		if (!hasValue)
		{
			final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM,
					OPTIONAL_PARAM_DEFAULT_VALUE);
			if (!isOptional)
			{
				throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
			}
		}
	}

	@Override
	protected Map<String, MediaModel> loadData(final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final ProductModel product) throws FieldValueProviderException
	{
		final Map<String, MediaModel> medias = new HashMap<>();
		final HashMultimap<String, String> indexedPropertyMediaFormats = HashMultimap.create();

		for (final IndexedProperty property : indexedProperties)
		{
			final String mediaFormat = getMediaFormat(property);
			indexedPropertyMediaFormats.put(mediaFormat, property.getName());
		}

		final int mediasToFind = indexedProperties.size();
		collectMedias(product, indexedPropertyMediaFormats, medias, mediasToFind);
		return medias;
	}

	protected void collectMedias(final ProductModel product, final Multimap<String, String> indexedPropertyMediaFormats,
			final Map<String, MediaModel> medias, int mediasToFind)
	{
		if (product == null)
		{
			return;
		}

		final List<MediaContainerModel> galleryImages = product.getGalleryImages();

		// Search each media container in the gallery for an image of the right format
		for (final MediaContainerModel container : galleryImages)
		{
			for (final MediaModel media : container.getMedias())
			{
				final MediaFormatModel mediaFormat = media.getMediaFormat();
				if (mediaFormat != null)
				{
					final Collection<String> indexedPropertyNames = indexedPropertyMediaFormats.get(mediaFormat.getQualifier());

					for (final String indexedPropertyName : indexedPropertyNames)
					{
						if (StringUtils.isNotBlank(indexedPropertyName) && !medias.containsKey(indexedPropertyName))
						{
							medias.put(indexedPropertyName, media);
							mediasToFind--;

							if (mediasToFind == 0)
							{
								return;
							}
						}
					}
				}
			}
		}

		if (product instanceof VariantProductModel)
		{
			// Look in the base product
			collectMedias(((VariantProductModel) product).getBaseProduct(), indexedPropertyMediaFormats, medias, mediasToFind);
		}
	}

	protected String getMediaFormat(final IndexedProperty indexedProperty) throws FieldValueProviderException
	{
		String mediaFormat = ValueProviderParameterUtils.getString(indexedProperty, MEDIA_FORMAT_PARAM,
				MEDIA_FORMAT_PARAM_DEFAULT_VALUE);

		if (StringUtils.isNotBlank(mediaFormat))
		{
			return mediaFormat;
		}
		else
		{
			final String indexedPropertyName = indexedProperty.getName();
			mediaFormat = parseIndexedPropertyName(indexedPropertyName);

			if (StringUtils.isBlank(mediaFormat))
			{
				throw new FieldValueProviderException("Cannot recognize media format for indexed property " + indexedPropertyName);
			}
		}

		return mediaFormat;
	}

	protected String parseIndexedPropertyName(final String indexedPropertyName)
	{
		final int splitCharIndex = indexedPropertyName.indexOf('-');

		if (splitCharIndex == -1)
		{
			return null;
		}

		return indexedPropertyName.substring(splitCharIndex + 1, indexedPropertyName.length());
	}
}
