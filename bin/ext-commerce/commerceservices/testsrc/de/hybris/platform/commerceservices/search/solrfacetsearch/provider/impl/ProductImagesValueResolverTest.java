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


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolverTest;
import de.hybris.platform.variants.model.VariantProductModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
public class ProductImagesValueResolverTest extends AbstractValueResolverTest
{

	protected static final String INDEXED_PROPERTY_1_NAME = "img-65Wx65H";
	protected static final String INDEXED_PROPERTY_2_NAME = "img-30Wx30H";
	protected static final String INDEXED_PROPERTY_3_NAME = "other-65Wx65H";

	private static final String mediaModelUrl1 = "url1";
	private static final String mediaModelUrl2 = "url2";

	private ProductImagesValueResolver valueResolver;

	private IndexedProperty indexedProperty1;
	private IndexedProperty indexedProperty2;
	private IndexedProperty indexedProperty3;

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private ProductModel product;

	@Mock
	private VariantProductModel variantProductModel;

	@Mock
	private MediaContainerModel mediaContainerModel;

	@Mock
	private MediaModel mediaModel;

	@Mock
	private MediaModel mediaModel2;

	@Mock
	private MediaFormatModel mediaFormatModel;

	@Mock
	private MediaFormatModel mediaFormatModel2;

	@Before
	public void setUp()
	{
		indexedProperty1 = new IndexedProperty();
		indexedProperty1.setName(INDEXED_PROPERTY_1_NAME);
		indexedProperty1.setValueProviderParameters(new HashMap<String, String>());

		indexedProperty2 = new IndexedProperty();
		indexedProperty2.setName(INDEXED_PROPERTY_2_NAME);
		indexedProperty2.setValueProviderParameters(new HashMap<String, String>());

		indexedProperty3 = new IndexedProperty();
		indexedProperty3.setName(INDEXED_PROPERTY_3_NAME);
		indexedProperty3.setValueProviderParameters(new HashMap<String, String>());

		when(Boolean.valueOf(getQualifierProvider().canApply(any(IndexedProperty.class)))).thenReturn(Boolean.FALSE);
		when(mediaModel.getURL()).thenReturn(mediaModelUrl1);
		when(mediaModel2.getURL()).thenReturn(mediaModelUrl2);
		when(variantProductModel.getBaseProduct()).thenReturn(product);


		valueResolver = new ProductImagesValueResolver();
		valueResolver.setSessionService(getSessionService());
		valueResolver.setQualifierProvider(getQualifierProvider());
	}

	@Test
	public void resolveProductWithNoMediaGallery() throws FieldValueProviderException
	{
		// given
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty1);
		final List<MediaContainerModel> galleryList = Collections.emptyList();

		when(product.getGalleryImages()).thenReturn(galleryList);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any());
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any(), any(String.class));
	}

	@Test
	public void resolveProductWithNoMediaInContainer() throws FieldValueProviderException
	{
		// given
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty1);
		final List<MediaContainerModel> galleryList = Collections.singletonList(mediaContainerModel);
		final List<MediaModel> medias = Collections.emptyList();

		when(product.getGalleryImages()).thenReturn(galleryList);
		when(mediaContainerModel.getMedias()).thenReturn(medias);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any());
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any(), any(String.class));
	}

	@Test
	public void resolveProductWithNoMatchingMediaFormat() throws FieldValueProviderException
	{
		// given
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty1);
		final List<MediaContainerModel> galleryList = Collections.singletonList(mediaContainerModel);
		final List<MediaModel> medias = Collections.singletonList(mediaModel);

		indexedProperty1.getValueProviderParameters().put(ProductImagesValueResolver.MEDIA_FORMAT_PARAM, "30Wx30H");

		when(product.getGalleryImages()).thenReturn(galleryList);
		when(mediaContainerModel.getMedias()).thenReturn(medias);
		when(mediaFormatModel.getQualifier()).thenReturn("65Wx65H");
		when(mediaModel.getMediaFormat()).thenReturn(mediaFormatModel);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any());
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any(), any(String.class));
	}

	@Test
	public void resolveProductWithNoMatchingMediaFormatNoOptional() throws FieldValueProviderException
	{
		// given
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty1);
		final List<MediaContainerModel> galleryList = Collections.singletonList(mediaContainerModel);
		final List<MediaModel> medias = Collections.singletonList(mediaModel);

		indexedProperty1.getValueProviderParameters().put(ProductImagesValueResolver.MEDIA_FORMAT_PARAM, "30Wx30H");
		indexedProperty1.getValueProviderParameters().put(ProductImagesValueResolver.OPTIONAL_PARAM, "false");


		when(product.getGalleryImages()).thenReturn(galleryList);
		when(mediaContainerModel.getMedias()).thenReturn(medias);
		when(mediaFormatModel.getQualifier()).thenReturn("65Wx65H");
		when(mediaModel.getMediaFormat()).thenReturn(mediaFormatModel);

		//expect
		expectedException.expect(FieldValueProviderException.class);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);
	}

	@Test
	public void resolveProductWithUnknownMediaFormat() throws FieldValueProviderException
	{
		// given
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty1);
		indexedProperty1.setName("whatever");

		//expect
		expectedException.expect(FieldValueProviderException.class);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);
	}

	@Test
	public void resolveProductWithMedias() throws FieldValueProviderException
	{
		// given
		final Collection<IndexedProperty> indexedProperties = new ArrayList<>();
		indexedProperties.add(indexedProperty1);
		indexedProperties.add(indexedProperty2);
		final List<MediaContainerModel> galleryList = new ArrayList<>();
		galleryList.add(mediaContainerModel);

		final List<MediaModel> medias = new ArrayList<>();
		medias.add(mediaModel);
		medias.add(mediaModel2);

		indexedProperty1.getValueProviderParameters().put(ProductImagesValueResolver.MEDIA_FORMAT_PARAM, "30Wx30H");
		indexedProperty2.getValueProviderParameters().put(ProductImagesValueResolver.MEDIA_FORMAT_PARAM, "65Wx65H");

		when(product.getGalleryImages()).thenReturn(galleryList);
		when(mediaContainerModel.getMedias()).thenReturn(medias);
		when(mediaFormatModel.getQualifier()).thenReturn("30Wx30H");
		when(mediaFormatModel2.getQualifier()).thenReturn("65Wx65H");
		when(mediaModel.getMediaFormat()).thenReturn(mediaFormatModel);
		when(mediaModel2.getMediaFormat()).thenReturn(mediaFormatModel2);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument(), Mockito.times(1)).addField(indexedProperty1, mediaModelUrl1, null);
		verify(getInputDocument(), Mockito.times(1)).addField(indexedProperty2, mediaModelUrl2, null);
	}

	@Test
	public void resolveProductWithMediasFormatFromIndexProperty() throws FieldValueProviderException
	{
		// given
		final Collection<IndexedProperty> indexedProperties = new ArrayList<>();
		indexedProperties.add(indexedProperty1);
		indexedProperties.add(indexedProperty2);
		final List<MediaContainerModel> galleryList = new ArrayList<>();
		galleryList.add(mediaContainerModel);

		final List<MediaModel> medias = new ArrayList<>();
		medias.add(mediaModel);
		medias.add(mediaModel2);

		indexedProperty1.setName("img-65Wx65H");
		indexedProperty2.setName("img-30Wx30H");

		when(product.getGalleryImages()).thenReturn(galleryList);
		when(mediaContainerModel.getMedias()).thenReturn(medias);
		when(mediaFormatModel.getQualifier()).thenReturn("65Wx65H");
		when(mediaFormatModel2.getQualifier()).thenReturn("30Wx30H");
		when(mediaModel.getMediaFormat()).thenReturn(mediaFormatModel);
		when(mediaModel2.getMediaFormat()).thenReturn(mediaFormatModel2);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument(), Mockito.times(1)).addField(indexedProperty1, mediaModelUrl1, null);
		verify(getInputDocument(), Mockito.times(1)).addField(indexedProperty2, mediaModelUrl2, null);
	}

	@Test
	public void resolveProductWithMediasForVariantProduct() throws FieldValueProviderException
	{
		// given
		final Collection<IndexedProperty> indexedProperties = new ArrayList<>();
		indexedProperties.add(indexedProperty1);
		indexedProperties.add(indexedProperty2);
		final List<MediaContainerModel> galleryList = new ArrayList<>();
		galleryList.add(mediaContainerModel);

		final List<MediaModel> medias = new ArrayList<>();
		medias.add(mediaModel);
		medias.add(mediaModel2);

		indexedProperty1.getValueProviderParameters().put(ProductImagesValueResolver.MEDIA_FORMAT_PARAM, "30Wx30H");
		indexedProperty2.getValueProviderParameters().put(ProductImagesValueResolver.MEDIA_FORMAT_PARAM, "65Wx65H");

		when(product.getGalleryImages()).thenReturn(galleryList);
		when(mediaContainerModel.getMedias()).thenReturn(medias);
		when(mediaFormatModel.getQualifier()).thenReturn("30Wx30H");
		when(mediaFormatModel2.getQualifier()).thenReturn("65Wx65H");
		when(mediaModel.getMediaFormat()).thenReturn(mediaFormatModel);
		when(mediaModel2.getMediaFormat()).thenReturn(mediaFormatModel2);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, variantProductModel);

		// then
		verify(getInputDocument(), Mockito.times(1)).addField(indexedProperty1, mediaModelUrl1, null);
		verify(getInputDocument(), Mockito.times(1)).addField(indexedProperty2, mediaModelUrl2, null);
	}

	@Test
	public void resolveProductWithTwoIndexedPropertyForOneMediaFromat() throws FieldValueProviderException
	{
		// given
		final Collection<IndexedProperty> indexedProperties = new ArrayList<>();
		indexedProperties.add(indexedProperty1);
		indexedProperties.add(indexedProperty2);
		indexedProperties.add(indexedProperty3);
		final List<MediaContainerModel> galleryList = new ArrayList<>();
		galleryList.add(mediaContainerModel);

		final List<MediaModel> medias = new ArrayList<>();
		medias.add(mediaModel);
		medias.add(mediaModel2);

		indexedProperty1.getValueProviderParameters().put(ProductImagesValueResolver.MEDIA_FORMAT_PARAM, "30Wx30H");
		indexedProperty2.getValueProviderParameters().put(ProductImagesValueResolver.MEDIA_FORMAT_PARAM, "65Wx65H");
		indexedProperty3.getValueProviderParameters().put(ProductImagesValueResolver.MEDIA_FORMAT_PARAM, "65Wx65H");

		when(product.getGalleryImages()).thenReturn(galleryList);
		when(mediaContainerModel.getMedias()).thenReturn(medias);
		when(mediaFormatModel.getQualifier()).thenReturn("30Wx30H");
		when(mediaFormatModel2.getQualifier()).thenReturn("65Wx65H");
		when(mediaModel.getMediaFormat()).thenReturn(mediaFormatModel);
		when(mediaModel2.getMediaFormat()).thenReturn(mediaFormatModel2);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument(), Mockito.times(1)).addField(indexedProperty1, mediaModelUrl1, null);
		verify(getInputDocument(), Mockito.times(1)).addField(indexedProperty2, mediaModelUrl2, null);
		verify(getInputDocument(), Mockito.times(1)).addField(indexedProperty3, mediaModelUrl2, null);
	}
}
