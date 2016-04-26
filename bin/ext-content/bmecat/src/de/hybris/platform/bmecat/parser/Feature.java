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


/**
 * Object which holds the value of a parsed &lt;FEATURE&gt; tag
 * 
 * 
 * 
 */
public class Feature extends AbstractValueObject
{
	private String fname;
	private String funit;
	private String[] fvalues;
	private String fdescr;
	private Integer forder;
	private String valueDetails;
	private Variants variants;

	/**
	 * @return Returns the fname.
	 */
	public String getFname()
	{
		return fname;
	}

	/**
	 * @param fname
	 *           The fname to set.
	 */
	public void setFname(final String fname)
	{
		this.fname = fname;
	}

	/**
	 * @return Returns the funit.
	 */
	public String getFunit()
	{
		return funit;
	}

	/**
	 * @param funit
	 *           The funit to set.
	 */
	public void setFunit(final String funit)
	{
		this.funit = funit;
	}

	/**
	 * @return Returns the fvalues.
	 */
	public String[] getFvalues()
	{
		return fvalues;
	}

	/**
	 * @param fvalues
	 *           The fvalues to set.
	 */
	public void setFvalues(final String[] fvalues)
	{
		this.fvalues = fvalues;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return fdescr;
	}

	/**
	 * @param fdescr
	 *           The description to set.
	 */
	public void setDescription(final String fdescr)
	{
		this.fdescr = fdescr;
	}

	/**
	 * @return Returns the value details.
	 */
	public String getValueDetails()
	{
		return valueDetails;
	}

	/**
	 * @param valueDetails
	 *           The value details to set.
	 */
	public void setValueDetails(final String valueDetails)
	{
		this.valueDetails = valueDetails;
	}

	/**
	 * @return Returns the forder.
	 */
	public Integer getForder()
	{
		return forder;
	}

	/**
	 * @param forder
	 *           The forder to set.
	 */
	public void setForder(final Integer forder)
	{
		this.forder = forder;
	}

	/**
	 * @return Returns the variants.
	 */
	public Variants getVariants()
	{
		return variants;
	}

	/**
	 * @param variants
	 *           The variants to set.
	 */
	public void setVariants(final Variants variants)
	{
		this.variants = variants;
	}

	@Override
	public String toString()
	{
		final StringBuilder buf = new StringBuilder();
		buf.append("Feature[").append(fname);
		buf.append("; ");
		buf.append(funit);
		buf.append("; ");
		buf.append(dumpFValues(fvalues));
		buf.append("; ");
		buf.append(fdescr);
		buf.append("; ");
		buf.append(forder);
		buf.append("; ");
		buf.append(valueDetails);
		buf.append("; ");
		buf.append(variants);
		buf.append("]");

		return buf.toString();
	}

	private String dumpFValues(final String[] vals)
	{
		final StringBuilder dump = new StringBuilder();
		dump.append("{");
		if (vals != null)
		{
			for (int i = 0; i < vals.length; i++)
			{
				dump.append(vals[i]);
				if (i < vals.length - 1)
				{
					dump.append("; ");
				}
			}
		}
		dump.append("}");
		return dump.toString();
	}
}
