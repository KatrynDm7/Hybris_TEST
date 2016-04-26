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
package de.hybris.platform.subscriptionfacades.converters;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.xstream.alias.AttributeAliasMapping;
import de.hybris.platform.commercefacades.xstream.alias.FieldAliasMapping;
import de.hybris.platform.commercefacades.xstream.alias.TypeAliasMapping;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.thoughtworks.xstream.XStream;


/**
 * {@link XStream} helper for subscriptions.
 */
public class SubscriptionXStreamAliasConverter implements ApplicationContextAware
{
	private static final Logger LOG = Logger.getLogger(SubscriptionXStreamAliasConverter.class);

	private ApplicationContext ctx;
	private XStream xstream = null;

	/**
	 * Convert product dto to xml string.
	 *
	 * @param productData DTO. can be null (empty XML will be returned)
	 * @return xml representation of the object
	 */
	@Nonnull
	public String getXStreamXmlFromSubscriptionProductData(@Nullable final ProductData productData)
	{
		final String xml = getXstream().toXML(productData);
		LOG.debug(xml);
		return xml;
	}

	/**
	 * Convert xml string to instance of {@link ProductData}.
	 *
	 * @param xml xml string
	 * @return unmarshalled object of null if xml is empty
	 *
	 * @throws com.thoughtworks.xstream.converters.ConversionException if class is unavailable
	 * @throws java.lang.ClassCastException is the XML contains another type of object
	 */
	@Nullable
	public ProductData getSubscriptionProductDataFromXml(@Nullable final String xml)
	{
		return StringUtils.isNotEmpty(xml) ? (ProductData) getXstream().fromXML(xml) : null;

	}

	@Nonnull
	public XStream getXstream()
	{
		if (xstream == null)
		{
			xstream = new XStream();
			BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, TypeAliasMapping.class).values().stream()
					.filter(alias -> !(alias instanceof AttributeAliasMapping) && !(alias instanceof FieldAliasMapping))
					.forEach(alias -> {
							if (LOG.isDebugEnabled())
							{
								LOG.debug("registering type alias " + alias.getAlias() + " , " + alias.getAliasedClass());
							}
							xstream.alias(alias.getAlias(), alias.getAliasedClass());
						});

			BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, AttributeAliasMapping.class) .values()
					.forEach(attribute -> xstream.useAttributeFor(attribute.getAlias(), attribute.getAliasedClass()));
		}

		return xstream;
	}

	@Override
	public void setApplicationContext(final ApplicationContext context) throws BeansException
	{
		ctx = context;
	}
}
