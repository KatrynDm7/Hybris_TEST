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
package com.sap.wec.adtreco.btg;

import java.util.Collection;
import java.util.HashSet;


/**
 *
 */
public class SAPInitiativeSet extends HashSet<String>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public SAPInitiativeSet()
	{
		super();
	}

	/**
	 * @param col
	 */
	public SAPInitiativeSet(final Collection<? extends String> col)
	{
		super(col);
	}

	/**
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public SAPInitiativeSet(final int initialCapacity, final float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}

	/**
	 * @param initialCapacity
	 */
	public SAPInitiativeSet(final int initialCapacity)
	{
		super(initialCapacity);
	}

}
