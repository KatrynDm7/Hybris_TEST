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

import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.oci.jalo.utils.Utils;

import org.apache.log4j.Logger;



/**
 * Example implementation of interface SAPProduct. The class DefaultSAPProduct contains a single product from the
 * catalog. <br>
 * The two constructors are added to the interface to better implement the catalog data to SAP format.<br>
 * See <i>oci/doc/resouces/samplesrc/DefaultSAPProduct.java</i> for the source code.
 * 
 * <br/>
 * <br/>
 * <b>This implementation is intended for demonstration purposes only. If it does not contains functionality you need,
 * you will have to implement that functionality by yourself. We do not recommend using this implementation for
 * productive systems.</b>
 * 
 */
public class DefaultSAPProduct implements SAPProduct
{
	private final AbstractOrderEntry cartEntry;
	private final Product product;
	private static final Logger LOG = Logger.getLogger(DefaultSAPProduct.class);

	/**
	 * Stores the given cart entry as <code>SAPProduct</code>.
	 * 
	 * @param cartEntry
	 *           an order entry
	 */
	public DefaultSAPProduct(final AbstractOrderEntry cartEntry)
	{
		this.cartEntry = cartEntry;
		this.product = cartEntry.getProduct();
	}

	/**
	 * Stores the given catalog product as <code>SAPProduct</code>.
	 * 
	 * @param product
	 *           a catalog product
	 */
	public DefaultSAPProduct(final Product product)
	{
		this.cartEntry = null;
		this.product = product;
	}

	/**
	 * returns product.getName
	 */
	public String getItemDescription()
	{
		return this.product.getName();
	}

	/**
	 * returns product.getCode
	 */
	public String getItemMatNr()
	{
		return this.product.getCode();
	}

	/**
	 * returns order.getQuantity (or 1.0)
	 */
	public double getItemQuantity()
	{
		return this.cartEntry != null ? this.cartEntry.getQuantityAsPrimitive() : 1.0;
	}

	/**
	 * returns product.getUnit.getCode
	 */
	public String getItemUnit()
	{
		return this.product.getUnit().getCode();
	}

	/**
	 * returns {@link Utils#getProductPrice(Product, double)} for the given product and quantity
	 */
	public double getItemPrice()
	{
		final double result = (cartEntry != null) ? cartEntry.getBasePriceAsPrimitive() : Utils.getProductPrice(this.product,
				getItemQuantity());
		if (LOG.isDebugEnabled())
		{
			final String type = (cartEntry != null) ? "CartEntry" : "Product";
			LOG.debug("ItemPrice for SAPProduct (" + type + ") " + result);
		}
		return result;
	}

	/**
	 * returns order.getCurrency.getIsoCode or the default session currency
	 */
	public String getItemCurrency()
	{
		return this.cartEntry != null ? this.cartEntry.getOrder().getCurrency().getIsoCode() : JaloSession.getCurrentSession()
				.getSessionContext().getCurrency().getIsoCode();
	}

	/**
	 * returns 1
	 */
	public int getItemPriceUnit()
	{
		return 1;
	}

	// delivery time attribute qualifier (in catalog) 
	public final static String DELIVERY_TIME = "deliveryTime";

	/**
	 * returns product.deliveryTime or -1 if no value is set or error occurs
	 */
	public int getItemLeadTime()
	{
		try
		{
			final AttributeDescriptor attribute = this.product.getComposedType().getAttributeDescriptor(DELIVERY_TIME);
			final Double deliveryTime = (Double) this.product.getAttribute(attribute.getQualifier());
			if (deliveryTime != null)
			{
				return deliveryTime.intValue();
			}
		}
		catch (final JaloItemNotFoundException e)
		{
			// delivery time attribute does not exist
		}
		catch (final JaloSecurityException e)
		{
			throw new JaloSystemException(e);
		}

		return -1;
	}

	/**
	 * returns product.getDescription
	 */
	public String getItemLongtext()
	{
		return this.product.getDescription();
	}

	/**
	 * returns ""
	 */
	public String getItemVendor()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemManufactCode()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemVendorMat()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemManufactMat()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemMatGroup()
	{
		return "";
	}

	/**
	 * returns false
	 */
	public boolean getItemService() // NOPMD
	{
		return false;
	}

	/**
	 * returns ""
	 */
	public String getItemContract()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemContractItem()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemExtQuoteId()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemExtQuoteItem()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemExtProductId()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemAttachment()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemAttachmentTitle()
	{
		return "";
	}

	/**
	 * returns 'C'
	 */
	public char getItemAttachmentPurpose()
	{
		return 'C';
	}

	/**
	 * returns ""
	 */
	public String getItemExtCategoryId()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemExtCategory()
	{
		return "";
	}

	/**
	 * returns ""
	 */
	public String getItemSLDSysName()
	{
		return "";
	}

	/**
	 * returns empty String array
	 */
	public String[] getCustomParameterNames()
	{
		return new String[] {};
	}

	/**
	 * returns ""
	 */
	public String getCustomParameterValue(final String parameterName)
	{
		return "";
	}
}
