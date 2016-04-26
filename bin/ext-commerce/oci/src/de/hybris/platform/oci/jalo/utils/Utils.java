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
package de.hybris.platform.oci.jalo.utils;

import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.oci.constants.OciConstants;
import de.hybris.platform.oci.jalo.exception.OciException;
import de.hybris.platform.oci.jalo.interfaces.SAPProduct;
import de.hybris.platform.oci.jalo.interfaces.SAPProductList;
import de.hybris.platform.util.Base64;
import de.hybris.platform.util.PriceValue;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;


/**
 * provide some methods for generating xml and html code
 * 
 * 
 * 
 */
public class Utils
{

	/**
	 * Generates a html page (contains the formular with the xml data (encoded as Base64) or html data) which submit
	 * itself per JavaScript to the HOOK_URL
	 * 
	 * @param data
	 *           a SAPProduct or SAPProductList as plain xml or html code in OCI conform tags
	 * @param outboundSection
	 *           contains the sended Parameter from the SRM Server
	 * @param autoSubmit
	 *           if set to <code>false</code> the formular will <b>not</b> be send automatically to the HOOK_URL (no
	 *           JavaScript will be written to the output).
	 * @param useHtml
	 * @return a html document which contains a form with Base64 encoded xml data and JavaScript which submit the form on
	 *         document load
	 * @throws OciException
	 *            if something oci related failiure is happening
	 */
	public static String generateHTML(final String data, final OutboundSection outboundSection, final boolean autoSubmit,
			final boolean useHtml) throws OciException
	{
		final StringBuilder htmltemp = new StringBuilder("<HTML>\n<BODY>\n");
		htmltemp.append("<form action=\"").append(outboundSection.getField(outboundSection.getHookURLFieldName()))
				.append("\" method=\"post\" name=\"OrderForm\" accept-charset=\"UTF-8\"");
		htmltemp.append(outboundSection.getField(OutboundSection.Fields.TARGET) != null
				&& outboundSection.getField(OutboundSection.Fields.TARGET).length() > 0 ? " target=\""
				+ encodeHTML(outboundSection.getField(OutboundSection.Fields.TARGET)) + "\"" : "");
		htmltemp.append(">\n");

		if (!useHtml)
		{
			htmltemp.append("<input type=\"hidden\" name=\"~xmlDocument\" value=\"");
			htmltemp.append(Base64.encodeBytes(data.getBytes(), Base64.DONT_BREAK_LINES)).append("\">\n");
			htmltemp.append("<input type=\"hidden\" name=\"~xml_type\" value=\"ESAPO\">");
		}
		else
		{
			htmltemp.append("<input type=\"hidden\" id=\"").append(OutboundSection.Fields.OK_CODE).append("\" ");

			htmltemp.append("name=\"").append(OutboundSection.Fields.OK_CODE).append("\" ");
			htmltemp.append("value=\"").append(encodeHTML(outboundSection.getField(OutboundSection.Fields.OK_CODE)))
					.append("\"/>\n");

			htmltemp.append("<input type=\"hidden\" id=\"").append(OutboundSection.Fields.CALLER).append("\" ");
			htmltemp.append("name=\"").append(OutboundSection.Fields.CALLER).append("\" ");
			htmltemp.append("value=\"").append(encodeHTML(outboundSection.getField(OutboundSection.Fields.CALLER))).append("\"/>\n");

			htmltemp.append("<input type=\"hidden\" id=\"").append(OutboundSection.Fields.TARGET).append("\" ");
			htmltemp.append("name=\"").append(OutboundSection.Fields.TARGET).append("\" ");
			htmltemp.append("value=\"").append(encodeHTML(outboundSection.getField(OutboundSection.Fields.TARGET))).append("\"/>\n");

			htmltemp.append(data);
		}

		htmltemp.append("<input type=\"submit\" value=\"Submit\" id=submit1 name=submit1>\n</form>\n");
		if (autoSubmit)
		{
			htmltemp.append("<script language=\"JavaScript1.2\">\ndocument.OrderForm.submit()\n</script>\n");
		}
		htmltemp.append("</BODY>\n</HTML>");
		return htmltemp.toString();
	}

	/**
	 * Transform the given SAPProduct into OCI conform xml data<br>
	 * 
	 * 
	 * @param sapproduct
	 *           the product to transform into
	 * @param outboundSection
	 *           get the OCI Version from this
	 * @return plaintext xml data in OCI version format (2.x, 3.x or 4.x)
	 * @throws OciException
	 *            if a problem occures
	 */
	public static String generateXMLData(final SAPProduct sapproduct, final OutboundSection outboundSection) throws OciException
	{
		if (outboundSection.getField(OciConstants.OCI_VERSION).startsWith("2."))
		{
			checkProductFor20Conform(sapproduct);
		}
		else if (outboundSection.getField(OciConstants.OCI_VERSION).startsWith("3."))
		{
			checkProductFor30Conform(sapproduct);
		}
		else if (outboundSection.getField(OciConstants.OCI_VERSION).startsWith("4."))
		{
			checkProductFor40Conform(sapproduct);
		}
		else
		{
			throw new OciException("Found OCI Version " + outboundSection.getField(OciConstants.OCI_VERSION)
					+ ". Supporting only 2.x 3.x or 4.x. Aborting.", OciException.OCI_FIELD_MISSING_OR_NO_DATA);
		}

		final StringBuilder xmltemp = new StringBuilder(
				"<?xml version =\"1.0\" encoding=\"utf-8\"?>\n\n<BusinessDocument>\n\t<Catalog>\n");
		xmltemp.append(generateXMLCodeForProduct(sapproduct));
		xmltemp.append("\t</Catalog>\n");
		xmltemp.append("</BusinessDocument>\n");
		return xmltemp.toString();
	}

	/**
	 * Transform the given SAPProductList into OCI specific xml format.
	 * 
	 * @param sapproductlist
	 *           the product list
	 * @param outboundSection
	 *           needed for detecting OCI version
	 * @return plaintext xml data
	 * @throws OciException
	 *            by problems
	 */
	public static String generateXMLData(final SAPProductList sapproductlist, final OutboundSection outboundSection)
			throws OciException
	{
		if (outboundSection.getField(OciConstants.OCI_VERSION).startsWith("2."))
		{
			checkCartFor20Conform(sapproductlist);
		}
		else if (outboundSection.getField(OciConstants.OCI_VERSION).startsWith("3."))
		{
			checkCartFor30Conform(sapproductlist);
		}
		else if (outboundSection.getField(OciConstants.OCI_VERSION).startsWith("4."))
		{
			checkCartFor40Conform(sapproductlist);
		}
		else
		{
			throw new OciException("Found OCI Version " + outboundSection.getField(OciConstants.OCI_VERSION)
					+ ". Supporting only 2.x 3.x or 4.x. Aborting.", OciException.OCI_FIELD_MISSING_OR_NO_DATA);
		}

		final StringBuilder xmltemp = new StringBuilder(
				"<?xml version =\"1.0\" encoding=\"utf-8\"?>\n\n<BusinessDocument>\n\t<Catalog>\n");
		for (int index = 0; index < sapproductlist.size(); index++)
		{
			xmltemp.append(generateXMLCodeForProduct(sapproductlist.getProduct(index)));
		}
		xmltemp.append("\t</Catalog>\n");
		xmltemp.append("</BusinessDocument>\n");
		return xmltemp.toString();
	}

	/**
	 * returns <code>true</code> if a String is null or empty. Spaces are trimed out.
	 * 
	 * @param string
	 *           the String
	 * @return false if String is not null or contains some characters except ' ' (blank)
	 */
	public static boolean isEmpty(final String string)
	{
		if (string == null)
		{
			return true;
		}
		else
		{
			return (string.trim().length() == 0); //NOPMD
		}
	}

	//	------------ data as html form part ----------------------------

	/**
	 * Transform the given SAPProductList into OCI specific html format.
	 * 
	 * @param sapproductlist
	 *           the list with products
	 * @param outboundSection
	 *           needed for detecting OCI version
	 * @return plaintext html data
	 * @throws OciException
	 *            by problems
	 */
	public static String generateHtmlData(final SAPProductList sapproductlist, final OutboundSection outboundSection)
			throws OciException
	{
		final StringBuilder htmltemp = new StringBuilder();

		for (int index = 0; index < sapproductlist.size(); index++)
		{
			htmltemp.append(generateHtmlData(sapproductlist.getProduct(index), index + 1, outboundSection));
		}
		return htmltemp.toString();
	}

	/**
	 * Transform a given SAPProduct into OCI specific html format.
	 * 
	 * @param sapproduct
	 *           the product
	 * @param outboundSection
	 *           needed for detecting OCI version
	 * @return plaintext html data
	 * @throws OciException
	 *            by problems
	 */
	public static String generateHtmlData(final SAPProduct sapproduct, final OutboundSection outboundSection) throws OciException
	{
		return generateHtmlData(sapproduct, 1, outboundSection);
	}

	//	------------ private methods ----------------------------------

	private static String generateHtmlData(final SAPProduct sapproduct, final int index, final OutboundSection outboundSection)
			throws OciException
	{
		if (outboundSection.getField(OciConstants.OCI_VERSION).startsWith("2."))
		{
			checkProductFor20Conform(sapproduct);
		}
		else if (outboundSection.getField(OciConstants.OCI_VERSION).startsWith("3."))
		{
			checkProductFor30Conform(sapproduct);
		}
		else if (outboundSection.getField(OciConstants.OCI_VERSION).startsWith("4."))
		{
			checkProductFor40Conform(sapproduct);
		}
		else
		{
			throw new OciException("Found OCI Version " + outboundSection.getField(OciConstants.OCI_VERSION)
					+ ". Supporting only 2.x 3.x or 4.x. Aborting.", OciException.OCI_FIELD_MISSING_OR_NO_DATA);
		}

		final StringBuilder temp = new StringBuilder("<input type=\"hidden\" name=\"NEW_ITEM-DESCRIPTION[").append(index).append(
				"]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemDescription())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-MATNR[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemMatNr())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-MATGROUP[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemMatGroup())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-QUANTITY[").append(index).append("]\" value=\"");
		temp.append(doubleToString(sapproduct.getItemQuantity())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-UNIT[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemUnit())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-PRICE[").append(index).append("]\" value=\"");
		temp.append(doubleToString(sapproduct.getItemPrice())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-PRICEUNIT[").append(index).append("]\" value=\"");
		temp.append(doubleToString(sapproduct.getItemPriceUnit())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-CURRENCY[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemCurrency())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-LEADTIME[").append(index).append("]\" value=\"");

		final String leadTime = sapproduct.getItemLeadTime() == -1 ? "" : String.valueOf(sapproduct.getItemLeadTime());
		temp.append(leadTime).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-VENDOR[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemVendor())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-VENDORMAT[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemVendorMat())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-MANUFACTCODE[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemManufactCode())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-MANUFACTMAT[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemManufactMat())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-CONTRACT[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemContract())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-CONTRACT_ITEM[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemContractItem())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-SERVICE[").append(index).append("]\" value=\"");
		temp.append((sapproduct.getItemService() ? "Service" : "") + "\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-EXT_QUOTE_ID[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemExtQuoteId())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-EXT_QUOTE_ITEM[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemExtQuoteItem())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-EXT_PRODUCT_ID[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemExtProductId())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-LONGTEXT_").append(index).append(":132[]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemLongtext())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-ATTACHMENT[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemAttachment())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-ATTACHMENT_TITLE[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemAttachmentTitle())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-ATTACHMENT_PURPOSE[").append(index).append("]\" value=\"");
		temp.append(sapproduct.getItemAttachmentPurpose() + "\">\n");

		//htmltemp.append("<input type=\"hidden\" name=\"NEW_ITEM-EXT_SCHEMA_TYPE[").append(index).append("]\" value=\"" );
		//temp.append(sapproduct.getItemex + "\"> \n"); removed, schema_type is not mapped, if want to use -> custom fields

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-EXT_CATEGORY_ID[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemExtCategoryId())).append("\">\n");

		temp.append("<input type=\"hidden\" name=\"NEW_ITEM-EXT_CATEGORY[").append(index).append("]\" value=\"");
		temp.append(encodeHTML(sapproduct.getItemExtCategory())).append("\">\n");

		if (sapproduct.getCustomParameterNames() != null)
		{
			for (int index2 = 0; index2 < sapproduct.getCustomParameterNames().length; index2++)
			{
				final String key = sapproduct.getCustomParameterNames()[index2];
				final String value = sapproduct.getCustomParameterValue(sapproduct.getCustomParameterNames()[index2]);
				temp.append("<input type=\"hidden\" name=\"").append(key).append("[").append(index).append("]").append("\" value=\"")
						.append(encodeHTML(value)).append("\">\n");
			}
		}
		temp.append("\n");
		return temp.toString();
	}

	private static String encodeHTML(final String string)
	{
		if (string == null)
		{
			return "";
		}

		final int length = string.length();
		final StringBuilder stringBuilder = new StringBuilder(length * 2);
		for (int i = 0; i < length; i++)
		{
			final char character = string.charAt(i);
			switch (character)
			{
				case '\"':
				{
					stringBuilder.append("&quot;");
				}
					break;
				case '\'':
				{
					stringBuilder.append("&#39;");
				}
					break;
				case '&':
				{
					stringBuilder.append("&amp;");
				}
					break;
				case '<':
				{
					stringBuilder.append("&lt;");
				}
					break;
				case '>':
				{
					stringBuilder.append("&gt;");
				}
					break;
				default:
				{
					stringBuilder.append(character);
				}
			}
		}

		return stringBuilder.toString();
	}


	/**
	 * wrapps product data in xml as &lt;![CDATA[...]]&gt;
	 */
	private static String wrapInXML(final String string)
	{
		final String start = "<![CDATA[";
		final String end = "]]>";
		if (!isEmpty(string))
		{
			return start + string.trim() + end;
		}
		else
		{
			if (string != null)
			{
				return string.trim();
			}
			else
			{
				return "";
			}
		}
	}

	/**
	 * Generate XML Code for the given Product
	 * 
	 * @param sapproduct
	 *           - the product
	 * @return - the xml code
	 */
	private static String generateXMLCodeForProduct(final SAPProduct sapproduct)
	{
		final StringBuilder xmltemp = new StringBuilder();
		if (sapproduct.getItemService())
		{
			xmltemp.append("\t\t<Product ProductType = \"Service\">\n");
		}
		else
		{
			xmltemp.append("\t\t<Product ProductType = \"Product\">\n");
		}
		//		-----------------------------------------------------------
		final String leadtime = sapproduct.getItemLeadTime() == -1 ? "" : String.valueOf(sapproduct.getItemLeadTime());
		xmltemp.append("\t\t\t<ProductID Code = \"Buyer\">").append(wrapInXML(sapproduct.getItemMatNr())).append("</ProductID>\n");
		xmltemp.append("\t\t\t<CatalogKey>").append(wrapInXML(sapproduct.getItemExtProductId())).append("</CatalogKey>\n");
		xmltemp.append("\t\t\t<ParentCategoryID>").append(wrapInXML(sapproduct.getItemMatGroup())).append("</ParentCategoryID>\n");
		xmltemp.append("\t\t\t<Description>").append(wrapInXML(sapproduct.getItemDescription())).append("</Description>\n");
		xmltemp.append("\t\t\t<Attachment>\n");
		xmltemp.append("\t\t\t\t<URL>").append(wrapInXML(sapproduct.getItemAttachment())).append("</URL>\n");
		xmltemp.append("\t\t\t\t<Description>").append(wrapInXML(sapproduct.getItemAttachmentTitle())).append("</Description>\n");
		xmltemp.append("\t\t\t\t<Purpose>").append(sapproduct.getItemAttachmentPurpose()).append("</Purpose>\n");
		xmltemp.append("\t\t\t</Attachment>\n");
		xmltemp
				.append("\t\t\t<ShoppingBasketItem RefVendorDescription = \"0\" RefManufacturerDescription = \"1\"> <!-- Reference to VendorDescription ID and ManufacturerDescription ID below -->\n");
		xmltemp.append("\t\t\t\t<Quantity UoM = \"").append(sapproduct.getItemUnit()).append("\">")
				.append(doubleToString(sapproduct.getItemQuantity())).append("</Quantity>\n");
		xmltemp.append("\t\t\t\t<NetPrice>\n");
		xmltemp.append("\t\t\t\t\t<Price Currency = \"").append(sapproduct.getItemCurrency()).append("\">")
				.append(doubleToString(sapproduct.getItemPrice())).append("</Price>\n");
		xmltemp.append("\t\t\t\t\t<PriceUnit>").append(sapproduct.getItemPriceUnit()).append("</PriceUnit>\n");
		xmltemp.append("\t\t\t\t</NetPrice>\n");
		xmltemp.append("\t\t\t\t<LeadTime>").append(leadtime).append("</LeadTime>\n");
		xmltemp.append("\t\t\t\t<Quote>\n");
		xmltemp.append("\t\t\t\t\t<QuoteID>").append(wrapInXML(sapproduct.getItemExtQuoteId())).append("</QuoteID>\n");
		xmltemp.append("\t\t\t\t\t<QuoteItemID>").append(wrapInXML(sapproduct.getItemExtQuoteItem())).append("</QuoteItemID>\n");
		xmltemp.append("\t\t\t\t</Quote>\n");
		xmltemp.append("\t\t\t\t<ItemText>").append(wrapInXML(sapproduct.getItemLongtext())).append("</ItemText>\n");
		xmltemp.append("\t\t\t</ShoppingBasketItem>\n");
		xmltemp.append("\t\t\t<ManufacturerDescription ID = \"1\">\n");
		xmltemp.append("\t\t\t\t<PartnerProductID Code = \"Other\">").append(wrapInXML(sapproduct.getItemManufactMat()))
				.append("</PartnerProductID>\n");
		xmltemp.append("\t\t\t\t<PartnerID Code = \"Other\">").append(wrapInXML(sapproduct.getItemManufactCode()))
				.append("</PartnerID>\n");
		xmltemp.append("\t\t\t</ManufacturerDescription>\n");
		xmltemp.append("\t\t\t<VendorDescription ID = \"0\">\n");
		xmltemp.append("\t\t\t\t<PartnerProductID Code = \"Other\">").append(wrapInXML(sapproduct.getItemVendorMat()))
				.append("</PartnerProductID>\n");
		xmltemp.append("\t\t\t\t<PartnerID Code = \"Other\">").append(wrapInXML(sapproduct.getItemVendor()))
				.append("</PartnerID>\n");
		xmltemp.append("\t\t\t\t<BuyerContract>\n");
		xmltemp.append("\t\t\t\t\t<ContractID>").append(wrapInXML(sapproduct.getItemContract())).append("</ContractID>\n");
		xmltemp.append("\t\t\t\t\t<ContractItemID>").append(wrapInXML(sapproduct.getItemContractItem()))
				.append("</ContractItemID>\n");
		xmltemp.append("\t\t\t\t</BuyerContract>\n");
		xmltemp.append("\t\t\t</VendorDescription>\n");
		if (sapproduct.getCustomParameterNames() != null)
		{
			for (int index2 = 0; index2 < sapproduct.getCustomParameterNames().length; index2++)
			{
				xmltemp.append("\t\t\t<" + sapproduct.getCustomParameterNames()[index2] + ">")
						.append(wrapInXML(sapproduct.getCustomParameterValue(sapproduct.getCustomParameterNames()[index2])))
						.append("</" + sapproduct.getCustomParameterNames()[index2] + ">\n");
			}
		}
		xmltemp.append("\t\t</Product>\n");
		return xmltemp.toString();
	}

	//------------------------------ checks for conformity -------------------------------
	/**
	 * check if implementation of SAPProductList for OCI4.0 has all required fields
	 * 
	 * @param sapproductlist
	 *           - an Implementation of Interface SAPProductList
	 * @throws OciException
	 *            - returns an Exception if a required field is missing
	 */
	private static void checkCartFor40Conform(final SAPProductList sapproductlist) throws OciException
	{
		for (int index = 0; index < sapproductlist.size(); index++)
		{
			checkProductFor40Conform(sapproductlist.getProduct(index));
		}
	}

	/**
	 * check if implementation of SAPProductList for OCI3.0 has all required fields OCI2.0 and OCI3.0 have the same
	 * mandatory fields
	 * 
	 * @param sapproductlist
	 *           - an Implementation of Interface SAPProductList
	 * @throws OciException
	 *            - returns an Exception if a required field is missing
	 */
	private static void checkCartFor30Conform(final SAPProductList sapproductlist) throws OciException
	{
		for (int index = 0; index < sapproductlist.size(); index++)
		{
			checkProductFor30Conform(sapproductlist.getProduct(index));
		}
	}

	/**
	 * check if implementation of SAPProductList for OCI2.0 has all required fields OCI2.0 and OCI3.0 have the same
	 * mandatory fields
	 * 
	 * @param sapproductlist
	 *           - an Implementation of Interface SAPProductList
	 * @throws OciException
	 *            - returns an Exception if a required field is missing
	 */
	private static void checkCartFor20Conform(final SAPProductList sapproductlist) throws OciException
	{
		for (int index = 0; index < sapproductlist.size(); index++)
		{
			checkProductFor20Conform(sapproductlist.getProduct(index));
		}
	}

	/**
	 * check if implementation of SAPProduct for OCI4.0 has all required fields
	 * 
	 * @param sapproduct
	 *           - an Implementation of Interface SAPProduct
	 * @throws OciException
	 *            - returns an Exception if a required field is missing
	 */
	private static void checkProductFor40Conform(final SAPProduct sapproduct) throws OciException
	{
		//---------------------------------------------------------------------------------------------------------------------------------	
		if (isEmpty(sapproduct.getItemDescription()) && isEmpty(sapproduct.getItemMatNr()))
		{
			throw new OciException(
					"Either NEW_ITEM-DESCRIPTION[n] or NEW_ITEM_MATNR[n] must be filled. Only one of the two should be filled. "
							+ "Both fields are null or empty.", OciException.OCI_FIELD_MISSING_OR_NO_DATA);
		}
		//----------------------------------------------------------------------------------------------------------------------------------			
		if (isEmpty(sapproduct.getItemMatNr()))
		{
			if (isEmpty(sapproduct.getItemUnit()))
			{
				throw new OciException("NEW_ITEM-UNIT[n] is required and must been filled if NEW-ITEM-MATNR[n] is empty or null.",
						OciException.OCI_FIELD_MISSING_OR_NO_DATA);
			}
		}
		//--------------------------------------------------------------------------------------------------------------------------------------------
		if (isEmpty(sapproduct.getItemCurrency()))
		{
			throw new OciException("NEW_ITEM-CURRENCY[n] is required. (because NEW_ITEM-PRICE[n] != null)",
					OciException.OCI_FIELD_MISSING_OR_NO_DATA);
		}
		//--------------------------------------------------------------------------------------------------------------------------------------------
		if (!isEmpty(sapproduct.getItemExtQuoteItem()))
		{
			if (isEmpty(sapproduct.getItemExtQuoteId()))
			{
				throw new OciException("NEW_ITEM-EXT_QUOTE_ITEM[n] is filled, so NEW_ITEM-EXT_QUOTE_ITEM_ID[n] is required. ",
						OciException.OCI_FIELD_MISSING_OR_NO_DATA);
			}
		}
		//--------------------------------------------------------------------------------------------------------------------------------------------
		if (!isEmpty(sapproduct.getItemContractItem()))
		{
			if (isEmpty(sapproduct.getItemContract()))
			{
				throw new OciException("NEW_ITEM-CONTRACT_ITEM[n] is filled, so NEW_ITEM-CONTRACT[n] is required. ",
						OciException.OCI_FIELD_MISSING_OR_NO_DATA);
			}
		}
		//			--------------------------------------------------------------------------------------------------------------------------------------------
	}

	/**
	 * check if implementation of SAPProduct for OCI3.0 has all required fields OCI2.0 and OCI3.0 have the same mandatory
	 * fields
	 * 
	 * @param sapproduct
	 *           - an Implementation of Interface SAPProduct
	 * @throws OciException
	 *            - returns an Exception if a required field is missing
	 */
	private static void checkProductFor30Conform(final SAPProduct sapproduct) throws OciException
	{
		//---------------------------------------------------------------------------------------------------------------------------------	
		if (isEmpty(sapproduct.getItemDescription()) && isEmpty(sapproduct.getItemMatNr()))
		{
			throw new OciException(
					"Either NEW_ITEM-DESCRIPTION[n] or NEW_ITEM_MATNR[n] must be filled. One of the two should at least be filled. "
							+ "Both fields are null or empty.", OciException.OCI_FIELD_MISSING_OR_NO_DATA);
		}
		//----------------------------------------------------------------------------------------------------------------------------------
		if (isEmpty(sapproduct.getItemMatNr()))
		{
			if (isEmpty(sapproduct.getItemUnit()))
			{
				throw new OciException("NEW_ITEM-UNIT[n] is required if NEW-ITEM-MATNR[n] has not been filled. ",
						OciException.OCI_FIELD_MISSING_OR_NO_DATA);
			}
		}
		//-----------------------------------------------------------------------------------------------------------------------------------
		if (isEmpty(sapproduct.getItemCurrency()))
		{
			throw new OciException("NEW_ITEM-PRICE[n] is filled (it is not null), so NEW_ITEM-CURRENCY[n] is required. ",
					OciException.OCI_FIELD_MISSING_OR_NO_DATA);
		}
	}

	/**
	 * check if implementation of SAPProduct for OCI2.0 has all required fields OCI2.0 and OCI3.0 have the same mandatory
	 * fields
	 * 
	 * @param sapproduct
	 *           - an Implementation of Interface SAPProduct
	 * @throws OciException
	 *            - returns an Exception if a required field is missing
	 */
	private static void checkProductFor20Conform(final SAPProduct sapproduct) throws OciException
	{
		checkProductFor30Conform(sapproduct);
	}

	/**
	 * Returns price of the given product. If more prices are available for this product, the most relevant one will be
	 * returned. If error occurs by price calculation or no price for the product is available, <code>0.0</code> will be
	 * returned.
	 * 
	 * @param product
	 * @param quantity
	 * 
	 * @return the price
	 */
	public static double getProductPrice(final Product product, final double quantity)
	{
		try
		{
			final List priceInfos = product.getPriceInformations(false);
			if (priceInfos != null && !priceInfos.isEmpty())
			{
				final PriceInformation priceInformation = (PriceInformation) priceInfos.get(0);
				final PriceValue priceValue = priceInformation.getPriceValue();
				Currency curr = null;
				double value;
				try
				{
					curr = JaloSession.getCurrentSession().getC2LManager().getCurrencyByIsoCode(priceValue.getCurrencyIso());
				}
				catch (final JaloItemNotFoundException e)
				{
					// DOCTODO Document reason, why this block is empty
				}

				if (curr == null)
				{
					curr = JaloSession.getCurrentSession().getSessionContext().getCurrency();
					value = curr.round(priceValue.getValue());
				}
				else
				{
					value = curr.convertAndRound(JaloSession.getCurrentSession().getSessionContext().getCurrency(),
							priceValue.getValue());
				}

				return value * quantity;
			}
		}
		catch (final JaloPriceFactoryException e)
		{
			// DOCTODO Document reason, why this block is empty
		}

		return 0.0;
	}

	/**
	 * transform a double value in the format "##########0.000"<br>
	 * SAP assumes double value in this format
	 * 
	 * @param number
	 *           the double value to transform
	 * @return a string in the format
	 */
	public static String doubleToString(final double number)
	{
		return getNumberFormat().format(CoreAlgorithms.round(number, 3));
	}

	private static DecimalFormat decimalFormat = null;

	private static DecimalFormat getNumberFormat()
	{
		if (decimalFormat == null)
		{
			decimalFormat = new DecimalFormat();
			final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);
			symbols.setDecimalSeparator('.');
			decimalFormat.setDecimalFormatSymbols(symbols);
			decimalFormat.applyPattern("##########0.000");
			decimalFormat.setDecimalSeparatorAlwaysShown(true);
		}

		return decimalFormat;
	}
}
