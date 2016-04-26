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

import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;

import java.util.ArrayList;
import java.util.List;


/**
 * Example implementation of the interface SAPProductList.<br>
 * See <i>oci/doc/resouces/samplesrc/DefaultSAPProductList.java</i> for the source code.
 * 
 * <br/>
 * <br/>
 * <b>This implementation is intended for demonstration purposes only. If it does not contains functionality you need,
 * you will have to implement that functionality by yourself. We do not recommend using this implementation for
 * productive systems.</b>
 */
public class DefaultSAPProductList implements SAPProductList
{
	private final AbstractOrder cart;
	private final List entries;

	/**
	 * Stores a given catalog list as <code>SAPProductList</code>.
	 * 
	 * @param productlist
	 *           the catalog product list
	 */
	public DefaultSAPProductList(final List productlist)
	{
		this.cart = null;
		this.entries = new ArrayList(productlist.size());
		for (int index = 0; index < productlist.size(); index++)
		{
			final SAPProduct product = new DefaultSAPProduct((Product) productlist.get(index));
			this.entries.add(product);
		}
	}

	/**
	 * Stores a given catalog cart as <code>SAPProductList</code>.
	 * 
	 * @param cart
	 *           the catalog cart
	 */
	public DefaultSAPProductList(final AbstractOrder cart)
	{
		this.cart = cart;

		// calculate the cart if needed
		if (!this.cart.isCalculatedAsPrimitive())
		{
			try
			{
				this.cart.calculate();
			}
			catch (final JaloPriceFactoryException e)
			{
				throw new JaloSystemException(e);
			}
		}

		final List cartEntries = this.cart.getAllEntries();
		this.entries = new ArrayList(cartEntries.size());
		for (int index = 0; index < cartEntries.size(); index++)
		{
			final SAPProduct product = new DefaultSAPProduct((AbstractOrderEntry) cartEntries.get(index));
			this.entries.add(product);
		}
	}

	/**
	 * Returns the <code>SAPProduct</code> with the given index from the <code>SAPProductList</code>.
	 */
	public SAPProduct getProduct(final int index)
	{
		return (SAPProduct) this.entries.get(index);
	}

	/**
	 * Returns the size of the <code>SAPProductList</code>.
	 */
	public int size()
	{
		return this.entries.size();
	}
}
