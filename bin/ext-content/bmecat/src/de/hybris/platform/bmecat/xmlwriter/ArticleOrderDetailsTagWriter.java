/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.bmecat.xmlwriter;

import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;

import java.io.IOException;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;ArticleOrderDetails&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class ArticleOrderDetailsTagWriter extends XMLTagWriter
{

	/**
	 * @param parent
	 */
	public ArticleOrderDetailsTagWriter(final XMLTagWriter parent)
	{
		super(parent, true);
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.ORDER_UNIT, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.CONTENT_UNIT));
		addSubTagWriter(new NumberTagWriter(this, BMECatConstants.XML.TAG.NO_CU_PER_OU));
		addSubTagWriter(new NumberTagWriter(this, BMECatConstants.XML.TAG.PRICE_QUANTITY));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.QUANTITY_MIN));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.QUANTITY_INTERVAL));

	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_ORDER_DETAILS;
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final Product product = (Product) object;
		if (product.getUnit() == null)
		{
			throw new JaloInvalidParameterException("product " + product + " got no unit - cannot export", 0);
		}
		getSubTagWriter(BMECatConstants.XML.TAG.ORDER_UNIT).write(xmlOut, product.getUnit().getCode());

		final Unit contentUnit = getCatalogManager().getContentUnit(product);
		if (contentUnit != null)
		{
			getSubTagWriter(BMECatConstants.XML.TAG.CONTENT_UNIT).write(xmlOut, contentUnit.getCode());
		}

		getSubTagWriter(BMECatConstants.XML.TAG.NO_CU_PER_OU).write(xmlOut, getCatalogManager().getNumberContentUnits(product));
		getSubTagWriter(BMECatConstants.XML.TAG.PRICE_QUANTITY).write(xmlOut, getCatalogManager().getPriceQuantity(product));
		getSubTagWriter(BMECatConstants.XML.TAG.QUANTITY_MIN).write(xmlOut, getCatalogManager().getMinOrderQuantity(product));
		getSubTagWriter(BMECatConstants.XML.TAG.QUANTITY_INTERVAL).write(xmlOut,
				getCatalogManager().getOrderQuantityInterval(product));


	}

	private CatalogManager getCatalogManager()
	{
		return (CatalogManager) JaloSession.getCurrentSession().getExtensionManager().getExtension(
				GeneratedCatalogConstants.EXTENSIONNAME);
	}

}
