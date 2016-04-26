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
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;


/**
 * Object which holds the value of a parsed &lt;VARIANTS&gt; tag
 * 
 * 
 * 
 */
public class Variants extends AbstractValueObject
{

	public static final String VARIANT = "Variant";
	/** TODO change VORDER to store Integer */
	public static final String VORDER = "VOrder";

	private final Map values;

	public Variants()
	{
		super();
		values = new CaseInsensitiveMap();
	}

	/**
	 * BMECat: VARIANTS.VARIANT
	 * 
	 * @param variant
	 *           the variant to add
	 */
	public void addVariant(final Variant variant)
	{
		List temp = (List) values.get(VARIANT);
		if (temp == null)
		{
			temp = new ArrayList();
		}
		temp.add(variant);
		values.put(VARIANT, temp);
	}

	/**
	 * BMECat: VARIANTS.VARIANT
	 * 
	 * @return Returns the features.
	 */
	public Collection<Variant> getVariants()
	{
		return (Collection) values.get(VARIANT);
	}

	/**
	 * BMECat: VARIANTS.VARIANT
	 * 
	 * @param variants
	 *           the variants to set.
	 */
	public void setVariants(final Collection<Variant> variants)
	{
		values.put(VARIANT, variants);
	}


	/**
	 * BMECat: VARIANTS.VORDER
	 * 
	 * @param vOrder
	 *           the VOrder to set.
	 */
	public void setVorder(final Integer vOrder)
	{
		values.put(VORDER, vOrder);
	}

	/**
	 * BMECat: VARIANTS.VORDER
	 * 
	 * @return Returns the vorder.
	 */
	public Integer getVorder()
	{
		return (Integer) values.get(VORDER);
	}
}
