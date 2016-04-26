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
package de.hybris.platform.financialacceleratorstorefrontatddtests.converters;

import de.hybris.platform.commercefacades.xstream.alias.AttributeAliasMapping;
import de.hybris.platform.commercefacades.xstream.alias.FieldAliasMapping;
import de.hybris.platform.commercefacades.xstream.alias.TypeAliasMapping;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.thoughtworks.xstream.XStream;


public class XmlConverters implements ApplicationContextAware
{
	private static final Logger LOG = Logger.getLogger(XmlConverters.class);

	private ApplicationContext ctx;
	private XStream xstream = null;

	public String getXmlFromObject(final Object object)
	{
		final StringBuffer xml = new StringBuffer();

		if (object instanceof Collection)
		{
			for (final Object dto : (Collection) object)
			{
				xml.append(getXstream().toXML(dto));
			}
		}
		else
		{
			xml.append(getXstream().toXML(object));
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(xml);
		}

		return xml.toString();
	}

	public XStream getXstream()
	{
		if (xstream == null)
		{
			xstream = new XStream();

			final Map<String, TypeAliasMapping> allTypeAliases = BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx,
					TypeAliasMapping.class);

			for (final TypeAliasMapping alias : allTypeAliases.values())
			{

				if (!(alias instanceof AttributeAliasMapping) && !(alias instanceof FieldAliasMapping))
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug("registering type alias " + alias.getAlias() + " , " + alias.getAliasedClass());
					}
					xstream.alias(alias.getAlias(), alias.getAliasedClass());
				}
			}

			final Map<String, AttributeAliasMapping> allAttributes = BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx,
					AttributeAliasMapping.class);

			for (final String attribute : allAttributes.keySet())
			{
				xstream.useAttributeFor(allAttributes.get(attribute).getAlias(), allAttributes.get(attribute).getAliasedClass());
			}
		}

		return xstream;
	}

	@Override
	public void setApplicationContext(final ApplicationContext ctx) throws BeansException
	{
		this.ctx = ctx;
	}
}
