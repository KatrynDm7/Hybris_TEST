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
import de.hybris.platform.catalog.jalo.Keyword;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;ArticleDetails&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class ArticleDetailsTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public ArticleDetailsTagWriter(final XMLTagWriter parent)
	{
		super(parent, true);
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.DESCRIPTION_SHORT, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.DESCRIPTION_LONG));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.EAN));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.SUPPLIER_ALT_AID));

		final Map buyerAidTagWriter = new LinkedMap();
		buyerAidTagWriter.put("unspecified", new SimpleTagWriter(this, BMECatConstants.XML.TAG.BUYER_AID));
		buyerAidTagWriter.put("duns", new SimpleTypedTagWriter(this, BMECatConstants.XML.TAG.BUYER_AID, "duns"));
		buyerAidTagWriter.put("iln", new SimpleTypedTagWriter(this, BMECatConstants.XML.TAG.BUYER_AID, "iln"));
		buyerAidTagWriter
				.put("buyer_specific", new SimpleTypedTagWriter(this, BMECatConstants.XML.TAG.BUYER_AID, "buyer_specific"));
		buyerAidTagWriter.put("supplier_specific", new SimpleTypedTagWriter(this, BMECatConstants.XML.TAG.BUYER_AID,
				"supplier_specific"));
		addSubTagWriter(BMECatConstants.XML.TAG.BUYER_AID, buyerAidTagWriter);

		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.MANUFACTURER_AID));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.MANUFACTURER_NAME));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.ERP_GROUP_BUYER));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.ERP_GROUP_SUPPLIER));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.DELIVERY_TIME));
		addSubTagWriter(new SpecialTreatmentClassTagWriter(this));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.KEYWORD));

		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.REMARKS));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.SEGMENT));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.ARTICLE_ORDER));

		final Map articleStatusTagWriter = new LinkedMap();

		final EnumerationType articleStatusType = EnumerationManager.getInstance().getEnumerationType(
				GeneratedCatalogConstants.TC.ARTICLESTATUS);
		for (final Iterator it = articleStatusType.getValues().iterator(); it.hasNext();)
		{
			final String statusEnumCode = ((EnumerationValue) it.next()).getCode();
			articleStatusTagWriter.put(statusEnumCode, new SimpleTypedTagWriter(this, BMECatConstants.XML.TAG.ARTICLE_STATUS,
					statusEnumCode));
		}
		addSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_STATUS, articleStatusTagWriter);

	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_DETAILS;
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final Product product = (Product) object;
		final String name = product.getName();
		getSubTagWriter(BMECatConstants.XML.TAG.DESCRIPTION_SHORT).write(xmlOut, name != null ? name : product.getCode());
		getSubTagWriter(BMECatConstants.XML.TAG.DESCRIPTION_LONG).write(xmlOut, product.getDescription());
		getSubTagWriter(BMECatConstants.XML.TAG.EAN).write(xmlOut, getCatalogManager().getEan(product));
		getSubTagWriter(BMECatConstants.XML.TAG.SUPPLIER_ALT_AID).write(xmlOut,
				getCatalogManager().getSupplierAlternativeAID(product));

		final Map buyerIds = getCatalogManager().getAllBuyerIDS(product);
		if (buyerIds != null)
		{
			for (final Iterator it = buyerIds.entrySet().iterator(); it.hasNext();)
			{
				final Map.Entry entry = (Map.Entry) it.next();
				final String type = ((EnumerationValue) entry.getKey()).getCode();
				final String value = (String) entry.getValue();
				getSubTagWriter(BMECatConstants.XML.TAG.BUYER_AID, type).write(xmlOut, value);
			}
		}

		getSubTagWriter(BMECatConstants.XML.TAG.MANUFACTURER_AID).write(xmlOut, getCatalogManager().getManufacturerAID(product));
		getSubTagWriter(BMECatConstants.XML.TAG.MANUFACTURER_NAME).write(xmlOut, getCatalogManager().getManufacturerName(product));
		getSubTagWriter(BMECatConstants.XML.TAG.ERP_GROUP_BUYER).write(xmlOut, getCatalogManager().getErpGroupBuyer(product));
		getSubTagWriter(BMECatConstants.XML.TAG.ERP_GROUP_SUPPLIER).write(xmlOut, getCatalogManager().getErpGroupSupplier(product));
		getSubTagWriter(BMECatConstants.XML.TAG.DELIVERY_TIME).write(xmlOut, getCatalogManager().getDeliveryTime(product));

		final Map specialTreatmentClassesMap = getCatalogManager().getAllSpecialTreatmentClasses(product);
		if (specialTreatmentClassesMap != null)
		{
			for (final Iterator it = specialTreatmentClassesMap.entrySet().iterator(); it.hasNext();)
			{
				final Map.Entry entry = (Map.Entry) it.next();
				getSubTagWriter(BMECatConstants.XML.TAG.SPECIAL_TREATMENT_CLASS).write(xmlOut, entry);
			}
		}

		final Collection keywords = getCatalogManager().getKeywords(product);
		if (keywords != null && !keywords.isEmpty())
		{
			for (final Iterator keywordIt = keywords.iterator(); keywordIt.hasNext();)
			{
				final Keyword keyword = (Keyword) keywordIt.next();
				getSubTagWriter(BMECatConstants.XML.TAG.KEYWORD).write(xmlOut, keyword.getKeyword());
			}
		}

		getSubTagWriter(BMECatConstants.XML.TAG.REMARKS).write(xmlOut, getCatalogManager().getRemarks(product));
		getSubTagWriter(BMECatConstants.XML.TAG.SEGMENT).write(xmlOut, getCatalogManager().getSegment(product));
		getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_ORDER).write(xmlOut, getCatalogManager().getOrder(product));

		final Map articleStatusMap = getCatalogManager().getArticleStatus(product);
		if (articleStatusMap != null)
		{
			for (final Iterator it = articleStatusMap.entrySet().iterator(); it.hasNext();)
			{
				final Map.Entry entry = (Map.Entry) it.next();
				final EnumerationValue typeEnum = (EnumerationValue) entry.getKey();
				getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_STATUS, typeEnum.getCode()).write(xmlOut, entry.getValue());
			}
		}
	}

	//	private BMECatManager getBMECatManager()
	//	{
	//		return (BMECatManager)JaloSession.getCurrentSession().getExtensionManager().getExtension( GeneratedBMECatConstants.EXTENSIONNAME );
	//	}	

	private CatalogManager getCatalogManager()
	{
		return (CatalogManager) JaloSession.getCurrentSession().getExtensionManager().getExtension(
				GeneratedCatalogConstants.EXTENSIONNAME);
	}

}
