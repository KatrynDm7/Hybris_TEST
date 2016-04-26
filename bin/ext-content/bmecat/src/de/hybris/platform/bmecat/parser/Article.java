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
package de.hybris.platform.bmecat.parser;

import de.hybris.bootstrap.xml.AbstractValueObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;


/**
 * Object which holds the value of a parsed &lt;ARTICLE&gt; tag
 * 
 * 
 */
public class Article extends AbstractValueObject
{
	public static final String MODE = "mode";
	public static final String SUPPLIER_AID = "SupplierAID";
	public static final String SUPPLIER_ALT_AID = "SupplierAlternativeAID";
	public static final String BUYER_IDS = "BuyerIDS";
	public static final String DELIVERY_TIME = "DeliveryTime";
	public static final String EAN = "EAN";
	public static final String ERP_GROUP_BUYER = "ErpGroupBuyer";
	public static final String ERP_GROUP_SUPPLIER = "ErpGroupSupplier";
	public static final String KEYWORDS = "Keywords";
	public static final String ORDER = "Order";
	public static final String REMARKS = "Remarks";
	public static final String SEGMENT = "Segment";
	public static final String SHORT_DESCRIPTION = "ShortDescription";
	public static final String LONG_DESCRIPTION = "LongDescription";
	public static final String SPECIAL_TREATMENT_CLASSES = "SpecialTreatmentClasses";
	public static final String STATUS = "ArticleStatus";
	public static final String MEDIAS = "Medias";
	public static final String MANUFACTURER_TYPE_DESCRIPTION = "ManufacturerTypeDescription";
	public static final String MANUFACTURER_NAME = "ManufacturerName";
	public static final String MANUFACTURER_AID = "ManufacturerAID";
	public static final String ARTICLE_PRICE_DETAILS = "ArticlePriceDetails";
	public static final String ARTICLE_REFERENCES = "ArticleReferences";
	public static final String ARTICLE_FEATURES = "ArticleFeatures";
	public static final String USER_DEFINED_EXTENSIONS = "UserDefinedExtensions";

	// order details
	public static final String ORDER_UNIT = "OrderUnit";
	public static final String CONTENT_UNIT = "ContentUnit";
	public static final String NUMBER_CONTENT_UNITS = "NumberContentUnits";
	public static final String PRICE_QUANTITY = "PriceQuantity";
	public static final String MIN_ORDER_QUANTITY = "MinOrderQuantity";
	public static final String ORDER_QUANTITY_INTERVAL = "OrderQuantityInterval";

	// modes
	public static final String MODE_NEW = "new";
	public static final String MODE_UPDATE = "update";
	public static final String MODE_DELETE = "delete";

	private final Map values;

	public Article()
	{
		super();
		values = new CaseInsensitiveMap();
	}

	/**
	 * BMECat: ARTICLE mode="..."
	 * 
	 * @return Returns the article mode
	 */
	public String getMode()
	{
		return (String) values.get(MODE);
	}

	public void setMode(final String mode)
	{
		values.put(MODE, mode);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.BUYER_AID The Map contains as key the buyer type and as value the name<br>
	 * example:<br>
	 * &lt;BUYER_AID type="xxx"&gt;yyy&lt;/BUYER_AID&gt; -&gt; {xxx=yyy}
	 * 
	 * @return Returns the buyerIDs.
	 */
	public Map<String, String> getBuyerIDs()
	{
		return (Map) values.get(BUYER_IDS);
	}

	/**
	 * The buyerIDs to set. BMECat: ARTICLE.ARTICLE_DETAILS.BUYER_AID
	 * 
	 * @param buyerIDs
	 */
	public void setBuyerIDs(final Map<String, String> buyerIDs)
	{
		values.put(BUYER_IDS, buyerIDs);
	}

	/**
	 * The contentUnit to set. BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.CONTENT_UNIT
	 * 
	 * @return Returns the contentUnit.
	 */
	public String getContentUnit()
	{
		return (String) values.get(CONTENT_UNIT);
	}

	/**
	 * The contentUnit to set. BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.CONTENT_UNIT
	 * 
	 * @param contentUnit
	 */
	public void setContentUnit(final String contentUnit)
	{
		values.put(CONTENT_UNIT, contentUnit);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.DELIVERY_TIME
	 * 
	 * @return Returns the deliveryTime.
	 */
	public Double getDeliveryTime()
	{
		return (Double) values.get(DELIVERY_TIME);
	}

	/**
	 * The deliveryTime to set. BMECat: ARTICLE.ARTICLE_DETAILS.DELIVERY_TIME
	 * 
	 * @param deliveryTime
	 */
	public void setDeliveryTime(final Double deliveryTime)
	{
		values.put(DELIVERY_TIME, deliveryTime);
	}

	/**
	 * @return Returns the ean. BMECat: ARTICLE.ARTICLE_DETAILS.EAN
	 */
	public String getEan()
	{
		return (String) values.get(EAN);
	}

	/**
	 * The ean to set. BMECat: ARTICLE.ARTICLE_DETAILS.EAN
	 * 
	 * @param ean
	 */
	public void setEan(final String ean)
	{
		values.put(EAN, ean);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.ERP_GROUP_BUYER
	 * 
	 * @return Returns the erpGroupBuyer.
	 */
	public String getErpGroupBuyer()
	{
		return (String) values.get(ERP_GROUP_BUYER);
	}

	/**
	 * The erpGroupBuyer to set. BMECat: ARTICLE.ARTICLE_DETAILS.ERP_GROUP_BUYER
	 * 
	 * @param erpGroupBuyer
	 */
	public void setErpGroupBuyer(final String erpGroupBuyer)
	{
		values.put(ERP_GROUP_BUYER, erpGroupBuyer);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.ERP_GROUP_SUPPLIER
	 * 
	 * @return Returns the erpGroupSupplier.
	 */
	public String getErpGroupSupplier()
	{
		return (String) values.get(ERP_GROUP_SUPPLIER);
	}

	/**
	 * The erpGroupSupplier to set. BMECat: ARTICLE.ARTICLE_DETAILS.ERP_GROUP_SUPPLIER
	 * 
	 * @param erpGroupSupplier
	 */
	public void setErpGroupSupplier(final String erpGroupSupplier)
	{
		values.put(ERP_GROUP_SUPPLIER, erpGroupSupplier);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.KEYWORD
	 * 
	 * @return Returns the keywords.
	 */
	public Collection<String> getKeywords()
	{
		return (Collection<String>) values.get(KEYWORDS);
	}

	/**
	 * The keywords to set. BMECat: ARTICLE.ARTICLE_DETAILS.KEYWORD
	 * 
	 * @param keywords
	 */
	public void setKeywords(final Collection<String> keywords)
	{
		values.put(KEYWORDS, keywords);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.QUANTITY_MIN
	 * 
	 * @return Returns the minQuanity.
	 */
	public Integer getMinOrderQuantity()
	{
		return (Integer) values.get(MIN_ORDER_QUANTITY);
	}

	/**
	 * The minQuanity to set. BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.QUANTITY_MIN
	 * 
	 * @param minQuanity
	 */
	public void setMinOrderQuantity(final Integer minQuanity)
	{
		values.put(MIN_ORDER_QUANTITY, minQuanity);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.PRICE_QUANTITY
	 * 
	 * @return Returns the PriceQuantity
	 */
	public Double getPriceQuantity()
	{
		return (Double) values.get(PRICE_QUANTITY);
	}

	/**
	 * The PriceQuantity to set. BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.PRICE_QUANTITY
	 * 
	 * @param quantity
	 */
	public void setPriceQuantity(final Double quantity)
	{
		values.put(PRICE_QUANTITY, quantity);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.NO_CU_PER_OU
	 * 
	 * @return Returns the numberContentUnits.
	 */
	public Double getNumberContentUnits()
	{
		return (Double) values.get(NUMBER_CONTENT_UNITS);
	}

	/**
	 * The numberContentUnits to set. BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.NO_CU_PER_OU
	 * 
	 * @param numberContentUnits
	 */
	public void setNumberContentUnits(final Double numberContentUnits)
	{
		values.put(NUMBER_CONTENT_UNITS, numberContentUnits);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.ARTICLE_ORDER
	 * 
	 * @return Returns the order.
	 */
	public Integer getOrder()
	{
		return (Integer) values.get(ORDER);
	}

	/**
	 * The order to set. BMECat: ARTICLE.ARTICLE_DETAILS.ARTICLE_ORDER
	 * 
	 * @param order
	 */
	public void setOrder(final Integer order)
	{
		values.put(ORDER, order);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.QUANTITY_INTERVAL
	 * 
	 * @return Returns the quantityInterval.
	 */
	public Integer getOrderQuantityInterval()
	{
		return (Integer) values.get(ORDER_QUANTITY_INTERVAL);
	}

	/**
	 * The quantityInterval to set. BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.QUANTITY_INTERVAL
	 * 
	 * @param quantityInterval
	 */
	public void setOrderQuantityInterval(final Integer quantityInterval)
	{
		values.put(ORDER_QUANTITY_INTERVAL, quantityInterval);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.REMARKS
	 * 
	 * @return Returns the remarks.
	 */
	public String getRemarks()
	{
		return (String) values.get(REMARKS);
	}

	/**
	 * The remarks to set. BMECat: ARTICLE.ARTICLE_DETAILS.REMARKS
	 * 
	 * @param remarks
	 */
	public void setRemarks(final String remarks)
	{
		values.put(REMARKS, remarks);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.SEGMENT
	 * 
	 * @return Returns the segment.
	 */
	public String getSegment()
	{
		return (String) values.get(SEGMENT);
	}

	/**
	 * The segment to set. BMECat: ARTICLE.ARTICLE_DETAILS.SEGMENT
	 * 
	 * @param segment
	 */
	public void setSegment(final String segment)
	{
		values.put(SEGMENT, segment);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.DESCRIPTION_SHORT
	 * 
	 * @return Returns the shortDescription.
	 */
	public String getShortDescription()
	{
		return (String) values.get(SHORT_DESCRIPTION);
	}

	/**
	 * The shortDescription to set. BMECat: ARTICLE.ARTICLE_DETAILS.DESCRIPTION_SHORT
	 * 
	 * @param shortDescription
	 */
	public void setShortDescription(final String shortDescription)
	{
		values.put(SHORT_DESCRIPTION, shortDescription);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.SPECIAL_TREATMENT_CLASS<br>
	 * 
	 * The Map contains as key the buyer type and as value the name<br>
	 * example:<br>
	 * &lt;ARTICLE_DETAILS.SPECIAL_TREATMENT_CLASS type="xxx"&gt;yyy&lt;/ARTICLE_DETAILS.SPECIAL_TREATMENT_CLASS&gt;
	 * -&gt; {xxx=yyy}
	 * 
	 * @return Returns the specialTreatmentClasses.
	 */
	public Map<String, String> getSpecialTreatmentClasses()
	{
		return (Map) values.get(SPECIAL_TREATMENT_CLASSES);
	}

	/**
	 * The specialTreatmentClasses to set. <br>
	 * BMECat: ARTICLE.ARTICLE_DETAILS.SPECIAL_TREATMENT_CLASS<br>
	 * 
	 * The Map contains as key the buyer type and as value the name<br>
	 * example:<br>
	 * &lt;ARTICLE_DETAILS.SPECIAL_TREATMENT_CLASS type="xxx"&gt;yyy&lt;/ARTICLE_DETAILS.SPECIAL_TREATMENT_CLASS&gt;
	 * -&gt; {xxx=yyy}
	 * 
	 * @param specialTreatmentClasses
	 */
	public void setSpecialTreatmentClasses(final Map<String, String> specialTreatmentClasses)
	{
		values.put(SPECIAL_TREATMENT_CLASSES, specialTreatmentClasses);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.ARTICLE_STATUS The Map contains as key the buyer type and as value the name<br>
	 * example:<br>
	 * &lt;ARTICLE_STATUS type="xxx"&gt;yyy&lt;/ARTICLE_STATUS&gt; -&gt; {xxx=yyy}
	 * 
	 * @return Returns the status.
	 */
	public Map<String, String> getStatus()
	{
		return (Map) values.get(STATUS) != null ? (Map) values.get(STATUS) : Collections.EMPTY_MAP;
	}

	/**
	 * The status to set. BMECat: ARTICLE.ARTICLE_DETAILS.ARTICLE_STATUS
	 * 
	 * @param status
	 */
	public void setStatus(final Map<String, String> status)
	{
		values.put(STATUS, status);
	}

	/**
	 * BMECat: ARTICLE.MIME_INFO
	 * 
	 * @return Returns the mimeInfos
	 */
	public Collection<Mime> getMedias()
	{
		return (Collection) values.get(MEDIAS);
	}

	/**
	 * @param medias
	 *           The mimeInfos to set see {@link Mime}. BMECat: ARTICLE.MIME_INFO
	 */
	public void setMimeInfos(final Collection<Mime> medias)
	{
		values.put(MEDIAS, medias);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.LONG_DESCRIPTION
	 * 
	 * @return Returns the longDescription.
	 */
	public String getLongDescription()
	{
		return (String) values.get(LONG_DESCRIPTION);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.LONG_DESCRIPTION
	 * 
	 * @param longDescription
	 *           The longDescription to set.
	 */
	public void setLongDescription(final String longDescription)
	{
		values.put(LONG_DESCRIPTION, longDescription);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.MANUFACTURER_AID
	 * 
	 * @return Returns the manufacturerAID.
	 */
	public String getManufacturerAID()
	{
		return (String) values.get(MANUFACTURER_AID);
	}

	/**
	 * @param manufacturerAID
	 *           The manufacturerAID to set.
	 */
	public void setManufacturerAID(final String manufacturerAID)
	{
		values.put(MANUFACTURER_AID, manufacturerAID);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.MANUFACTURER_NAME
	 * 
	 * @return Returns the manufacturerName.
	 */
	public String getManufacturerName()
	{
		return (String) values.get(MANUFACTURER_NAME);
	}

	/**
	 * @param manufacturerName
	 *           The manufacturerName to set.
	 */
	public void setManufacturerName(final String manufacturerName)
	{
		values.put(MANUFACTURER_NAME, manufacturerName);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.MANUFACTURER_TYPE_DESCR
	 * 
	 * @return Returns the manufacturerTypeDescription.
	 */
	public String getManufacturerTypeDescription()
	{
		return (String) values.get(MANUFACTURER_TYPE_DESCRIPTION);
	}

	/**
	 * @param manufacturerTypeDescription
	 *           The manufacturerTypeDescription to set.
	 */
	public void setManufacturerTypeDescription(final String manufacturerTypeDescription)
	{
		values.put(MANUFACTURER_TYPE_DESCRIPTION, manufacturerTypeDescription);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.SUPPLIER_ALT_AID
	 * 
	 * @return Returns the supplierAlternativeAID.
	 */
	public String getSupplierAlternativeAID()
	{
		return (String) values.get(SUPPLIER_ALT_AID);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_DETAILS.SUPPLIER_ALT_AID
	 * 
	 * @param supplierAlternativeAID
	 *           The supplierAlternativeAID to set.
	 */
	public void setSupplierAlternativeAID(final String supplierAlternativeAID)
	{
		values.put(SUPPLIER_ALT_AID, supplierAlternativeAID);
	}

	/**
	 * BMECat: ARTICLE.SUPPLIER_AID
	 * 
	 * @return Returns the supplierAID.
	 */
	public String getSupplierAID()
	{
		return (String) values.get(SUPPLIER_AID);
	}

	/**
	 * @param supplierAID
	 *           The supplierAID to set.
	 */
	public void setSupplierAID(final String supplierAID)
	{
		values.put(SUPPLIER_AID, supplierAID);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.ORDER_UNIT
	 * 
	 * @return Returns the orderUnit.
	 */
	public String getOrderUnit()
	{
		return (String) values.get(ORDER_UNIT);
	}

	/**
	 * The pricequantity to set. BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.PRICE_QUANTITY
	 * 
	 * @param priceQuantity
	 *           the price quantity to set.
	 */
	public void setPriceQuantity(final Integer priceQuantity)
	{
		values.put(PRICE_QUANTITY, priceQuantity);
	}

	/**
	 * The orderunit to set. BMECat: ARTICLE.ARTICLE_ORDER_DETAILS.ORDER_UNIT
	 * 
	 * @param orderUnit
	 *           the orderUnit to set.
	 */
	public void setOrderUnit(final String orderUnit)
	{
		values.put(ORDER_UNIT, orderUnit);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS
	 * 
	 * @return Returns a collection of {@link ArticlePriceDetails}.
	 */
	public Collection<ArticlePriceDetails> getArticlePriceDetails()
	{
		return (Collection) values.get(ARTICLE_PRICE_DETAILS);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS
	 * 
	 * @param articlePriceDetails
	 *           The articlePriceDetails to set.
	 */
	public void setArticlePriceDetails(final Collection<ArticlePriceDetails> articlePriceDetails)
	{
		values.put(ARTICLE_PRICE_DETAILS, articlePriceDetails);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_REFERENCES
	 * 
	 * @param reference
	 */
	public void addArticleReference(final ArticleReference reference)
	{
		List temp = (List) values.get(ARTICLE_REFERENCES);
		if (temp == null)
		{
			temp = new ArrayList();
		}
		temp.add(reference);
		values.put(ARTICLE_REFERENCES, temp);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_REFERENCES
	 * 
	 * @return Returns the articleReferences.
	 */
	public Collection<ArticleReference> getArticleReferences()
	{
		return (Collection) values.get(ARTICLE_REFERENCES);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_REFERENCES
	 * 
	 * @param articleReferences
	 *           The articleReferences to set.
	 */
	public void setArticleReferences(final Collection<ArticleReference> articleReferences)
	{
		values.put(ARTICLE_REFERENCES, articleReferences);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_FEATURES (Context T_NEW_CATALOG)
	 * 
	 * @param articleFeatures
	 */
	public void addArticleFeatures(final ArticleFeatures articleFeatures)
	{
		List temp = (List) values.get(ARTICLE_FEATURES);
		if (temp == null)
		{
			temp = new ArrayList();
		}
		temp.add(articleFeatures);
		values.put(ARTICLE_FEATURES, temp);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_FEATURES (Context T_NEW_CATALOG and T_UPDATE_PRODUCTS)
	 * 
	 * @return Returns the articleFeatures.
	 */
	public Collection<ArticleFeatures> getArticleFeatures()
	{
		return (Collection) values.get(ARTICLE_FEATURES);
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_FEATURES (Context T_NEW_CATALOG and T_UPDATE_PRODUCTS)
	 * 
	 * @param articleFeatures
	 *           The articleFeatures to set.
	 */
	public void setArticleFeatures(final Collection<ArticleFeatures> articleFeatures)
	{
		values.put(ARTICLE_FEATURES, articleFeatures);
	}

	/**
	 * BMECat: ARTICLE.USER_DEFINED_EXTENSIONS (Context T_NEW_CATALOG / T_UPDATE_PRODUCTS / T_UPDATE_PRICES)
	 * 
	 * @return Returns the object representing the user defined extensions.
	 */
	public Object getUDXValue()
	{
		return values.get(USER_DEFINED_EXTENSIONS);
	}

	/**
	 * BMECat: ARTICLE.USER_DEFINED_EXTENSIONS (Context T_NEW_CATALOG / T_UPDATE_PRODUCTS / T_UPDATE_PRICES)
	 * 
	 * @param value
	 *           The object representing the user defined extensions.
	 */
	public void setUDXValue(final Object value)
	{
		values.put(USER_DEFINED_EXTENSIONS, value);
	}

	@Override
	public String toString()
	{
		final StringBuilder output = new StringBuilder();
		output.append("Article[ ");
		for (final Iterator it = values.keySet().iterator(); it.hasNext();)
		{
			final String key = (String) it.next();
			output.append(key + "=" + getValue(key) + ", ");
		}
		output.append("]");
		return output.toString();
	}

	public Object getValue(final String qualifier)
	{
		return values.get(qualifier);
	}

	public Map getValues()
	{
		return Collections.unmodifiableMap(values);
	}
}
