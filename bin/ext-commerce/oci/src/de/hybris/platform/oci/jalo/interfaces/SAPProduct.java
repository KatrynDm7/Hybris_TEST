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
package de.hybris.platform.oci.jalo.interfaces;

/**
 * Wrap a catalog product into SAP conform OCI format.
 * 
 * 
 * 
 */
public interface SAPProduct
{
	//	------------ get Methoden fuer SRM Felder ------------------------------------------
	/**
	 * for SAP Field: <b><code>NEW_ITEM-DESCRIPTION</code></b><br>
	 * max. 40 Chars
	 */
	public String getItemDescription();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-MATNR</code></b><br>
	 * max. 40 Chars
	 */
	public String getItemMatNr();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-QUANTITY</code></b><br>
	 * max. 15 Chars<br>
	 * 11 digits before the decimal point, 3 after it. Do not use commas for thousands.
	 */
	public double getItemQuantity();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-UNIT</code></b><br>
	 * max. 3 Chars<br>
	 * Must be maintained as ISO code in the SRM Server
	 */
	public String getItemUnit();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-PRICE</code></b><br>
	 * max. 15 Chars<br>
	 * 11 digits before the decimal point, 3 after it. Do not use commas for thousands.
	 */
	public double getItemPrice();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-CURRENCY</code></b><br>
	 * max. 5 Chars<br>
	 * Must be maintained as ISO code in the SRM Server
	 */
	public String getItemCurrency();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-PRICEUNIT</code></b><br>
	 * max. 5Chars<br>
	 * In whole numbers<br>
	 * default return value, if empty, should be "1"
	 */
	public int getItemPriceUnit();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-LEADTIME</code></b><br>
	 * max. 5 Chars<br>
	 * In whole numbers<br>
	 * default return value, set to '-1' if empty
	 */
	public int getItemLeadTime();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-LONGTEXT_n:132[]</code></b><br>
	 * unlimited Chars <br>
	 */
	public String getItemLongtext();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-VENDOR</code></b><br>
	 * max. 10 Chars
	 */
	public String getItemVendor();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-MANUFACTCODE</code></b><br>
	 * max. 10Chars
	 */
	public String getItemManufactCode();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-VENDORMAT</code></b><br>
	 * max. 40 Chars
	 */
	public String getItemVendorMat();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-MANUFACTMAT</code></b><br>
	 * max. 40 Chars
	 */
	public String getItemManufactMat();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-MATGROUP</code></b><br>
	 * max. 10 Chars
	 */
	public String getItemMatGroup();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-SERVICE</code></b><br>
	 * max. 1 Chars<br>
	 * false if product; true if service
	 */
	public boolean getItemService(); // NOPMD

	/**
	 * for SAP Field: <b><code>NEW_ITEM-CONTRACT</code></b><br>
	 * max. 10 Chars
	 */
	public String getItemContract();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-CONTRACT_ITEM</code></b><br>
	 * max. 5 Chars
	 */
	public String getItemContractItem();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-EXT_QUOTE_ID</code></b><br>
	 * max. 35 Chars
	 */
	public String getItemExtQuoteId();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-EXT_QUOTE_ITEM</code></b><br>
	 * max. 10 Chars
	 */
	public String getItemExtQuoteItem();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-EXT_PRODUCT_ID</code></b><br>
	 * max. 40 Chars
	 */
	public String getItemExtProductId();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-ATTACHMENT</code></b><br>
	 * max. 255 Chars<br>
	 * return type is an accessable URL
	 */
	public String getItemAttachment();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-ATTACHMENT_TITLE</code></b><br>
	 * max. 255 Chars
	 */
	public String getItemAttachmentTitle();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-ATTACHMENT_PURPOSE</code></b><br>
	 * max. 1 Chars<br>
	 * C corresponds here to configuration.
	 */
	public char getItemAttachmentPurpose();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-EXT_CATEGORY_ID</code></b><br>
	 * max. 60 Chars
	 */
	public String getItemExtCategoryId();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-EXT_CATEGORY</code></b><br>
	 * max. 40 Chars
	 */
	public String getItemExtCategory();

	/**
	 * for SAP Field: <b><code>NEW_ITEM-SLD_SYS_NAME</code></b><br>
	 * max. 60 Chars
	 */
	public String getItemSLDSysName();

	/**
	 * For implementing custom fields which are not in SAP OCI specification, use this method and method
	 * {@link #getCustomParameterNames()}.<br>
	 * If you using the SAP Fields <b><code>NEW_ITEM-CUST_FIELD1</code></b> to <b><code>~5</code></b> or <b>
	 * <code>NEW_ITEMEXT_SCHEMA_TYPE</code></b> (currently not mapped in SAP server), they should be included in this
	 * method.
	 * 
	 * @return Stringarray with all custom field names
	 */
	public String[] getCustomParameterNames();

	/**
	 * This method returns the value from the given custom field name.
	 * 
	 * @param parameterName
	 *           name of the custom field
	 * @return value value of the custom field
	 */
	public String getCustomParameterValue(String parameterName);
}
