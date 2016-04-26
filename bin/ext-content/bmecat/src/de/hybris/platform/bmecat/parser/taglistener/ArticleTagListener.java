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
package de.hybris.platform.bmecat.parser.taglistener;

import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.bootstrap.xml.XMLContentLogger;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.Article;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


/**
 * Parses the &lt;ARTICLE&gt; tag
 * 
 * 
 */
public class ArticleTagListener extends DefaultBMECatTagListener implements XMLContentLogger
{
	private String myXML;

	public ArticleTagListener(final TagListener parent)
	{
		super(parent);
	}

	public void setXML(final String xml)
	{
		myXML = xml;
	}

	public String getXML()
	{
		return myXML;
	}

	@Override
	protected Collection createSubTagListeners()
	{
		// Transaction: T_UPDATE_PRICES
		if (getParents().contains(new TUpdatePricesTagListener().getTagName()))
		{
			return Arrays.asList(new TagListener[]
			{ new StringValueTagListener(this, BMECatConstants.XML.TAG.SUPPLIER_AID), new ArticlePriceDetailsTagListener(this), });
		}
		// Transactions: T_NEW_CATALOG, T_UPDATE_PRODUCTS
		return Arrays.asList(new TagListener[]
		{ new ArticleDetailsTagListener(this), new ArticleOrderDetailsTagListener(this), new ArticlePriceDetailsTagListener(this),
				new MimeInfoTagListener(this), new ArticleReferenceTagListener(this), new ArticleFeaturesTagListener(this),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.SUPPLIER_AID), new AbortTagListener(this) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		final Article article = new Article();

		article.setMode(getAttribute(BMECatConstants.XML.ATTRIBUTE.ARTICLE.MODE));

		//Details (delegated from ArticleDetailstagListener !!!)
		article.setShortDescription((String) getSubTagValue(BMECatConstants.XML.TAG.DESCRIPTION_SHORT));
		article.setLongDescription((String) getSubTagValue(BMECatConstants.XML.TAG.DESCRIPTION_LONG));
		article.setManufacturerTypeDescription((String) getSubTagValue(BMECatConstants.XML.TAG.MANUFACTURER_TYPE_DESCR));
		article.setEan((String) getSubTagValue(BMECatConstants.XML.TAG.EAN));
		article.setSupplierAlternativeAID((String) getSubTagValue(BMECatConstants.XML.TAG.SUPPLIER_ALT_AID));
		article.setManufacturerAID((String) getSubTagValue(BMECatConstants.XML.TAG.MANUFACTURER_AID));
		article.setManufacturerName((String) getSubTagValue(BMECatConstants.XML.TAG.MANUFACTURER_NAME));
		article.setErpGroupBuyer((String) getSubTagValue(BMECatConstants.XML.TAG.ERP_GROUP_BUYER));
		article.setErpGroupSupplier((String) getSubTagValue(BMECatConstants.XML.TAG.ERP_GROUP_SUPPLIER));
		article.setDeliveryTime((Double) getSubTagValue(BMECatConstants.XML.TAG.DELIVERY_TIME));
		article.setKeywords(getSubTagValueCollection(BMECatConstants.XML.TAG.KEYWORD));
		article.setSegment((String) getSubTagValue(BMECatConstants.XML.TAG.SEGMENT));
		article.setOrder((Integer) getSubTagValue(BMECatConstants.XML.TAG.ARTICLE_ORDER));
		article.setRemarks((String) getSubTagValue(BMECatConstants.XML.TAG.REMARKS));
		article
				.setSpecialTreatmentClasses(getSubTagValue(BMECatConstants.XML.TAG.SPECIAL_TREATMENT_CLASS) != null ? (Map) getSubTagValue(BMECatConstants.XML.TAG.SPECIAL_TREATMENT_CLASS)
						: Collections.EMPTY_MAP);

		article
				.setStatus(getSubTagValue(BMECatConstants.XML.TAG.ARTICLE_STATUS) != null ? (Map) getSubTagValue(BMECatConstants.XML.TAG.ARTICLE_STATUS)
						: Collections.EMPTY_MAP);
		article
				.setBuyerIDs(getSubTagValue(BMECatConstants.XML.TAG.BUYER_AID) != null ? (Map) getSubTagValue(BMECatConstants.XML.TAG.BUYER_AID)
						: Collections.EMPTY_MAP);

		//Order details
		article.setNumberContentUnits((Double) getSubTagValue(BMECatConstants.XML.TAG.NO_CU_PER_OU));
		article.setContentUnit((String) getSubTagValue(BMECatConstants.XML.TAG.CONTENT_UNIT));
		article.setOrderUnit((String) getSubTagValue(BMECatConstants.XML.TAG.ORDER_UNIT));
		article.setPriceQuantity((Double) getSubTagValue(BMECatConstants.XML.TAG.PRICE_QUANTITY));
		article.setMinOrderQuantity((Integer) getSubTagValue(BMECatConstants.XML.TAG.QUANTITY_MIN));
		article.setOrderQuantityInterval((Integer) getSubTagValue(BMECatConstants.XML.TAG.QUANTITY_INTERVAL));

		article.setSupplierAID((String) getSubTagValue(BMECatConstants.XML.TAG.SUPPLIER_AID));
		article.setMimeInfos(getSubTagValueCollection(BMECatConstants.XML.TAG.MIME_INFO));
		article.setArticlePriceDetails(getSubTagValueCollection(BMECatConstants.XML.TAG.ARTICLE_PRICE_DETAILS));
		article.setArticleReferences(getSubTagValueCollection(BMECatConstants.XML.TAG.ARTICLE_REFERENCE));
		article.setArticleFeatures(getSubTagValueCollection(BMECatConstants.XML.TAG.ARTICLE_FEATURES));
		article.setUDXValue(getSubTagValue(BMECatConstants.XML.TAG.USER_DEFINED_EXTENSIONS));

		article.setMode(getAttribute(BMECatConstants.XML.ATTRIBUTE.ARTICLE.MODE));

		article.setStartLineNumber(getStartLineNumber());
		article.setEndLineNumber(getEndLineNumber());
		article.setXML(getXML());

		/*
		 * notify processor
		 */
		processor.process(this, article);

		// there is no need for storing this value object by the parent
		return null;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE;
	}
}
