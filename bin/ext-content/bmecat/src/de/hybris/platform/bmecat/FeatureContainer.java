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
package de.hybris.platform.bmecat;

import java.io.Serializable;


/**
 * Default (and simple) implementation of a container product feature - persistance through serialization
 * 
 * 
 */
public class FeatureContainer implements Serializable
{
	private String qualifier;

	/**
	 * @return Returns the qualifier.
	 */
	public String getQualifier()
	{
		return qualifier;
	}

	/**
	 * @param qualifier
	 *           The qualifier to set.
	 */
	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}

	private String[] values;
	private String unit;
	private Integer order;
	private String valueDetails;
	private String description;

	public FeatureContainer(final String qualifier, final String[] values, final String unit, final Integer order,
			final String valueDetails, final String description)
	{
		this.qualifier = qualifier;
		this.values = values;
		this.unit = unit;
		this.order = order;
		this.valueDetails = valueDetails;
		this.description = description;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *           The description to set.
	 */
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * @return Returns the order.
	 */
	public Integer getOrder()
	{
		return order;
	}

	/**
	 * @param order
	 *           The order to set.
	 */
	public void setOrder(final Integer order)
	{
		this.order = order;
	}

	/**
	 * @return Returns the unit.
	 */
	public String getUnit()
	{
		return unit;
	}

	/**
	 * @param unit
	 *           The unit to set.
	 */
	public void setUnit(final String unit)
	{
		this.unit = unit;
	}

	/**
	 * @return Returns the valueDetails.
	 */
	public String getValueDetails()
	{
		return valueDetails;
	}

	/**
	 * @param valueDetails
	 *           The valueDetails to set.
	 */
	public void setValueDetails(final String valueDetails)
	{
		this.valueDetails = valueDetails;
	}

	/**
	 * @return Returns the values.
	 */
	public String[] getValues()
	{
		return values;
	}

	/**
	 * @param values
	 *           The values to set.
	 */
	public void setValues(final String[] values)
	{
		this.values = values;
	}

	@Override
	public String toString()
	{
		final StringBuilder buffer1 = new StringBuilder();
		buffer1.append(getQualifier()).append(";");
		for (int i = 0; i < values.length; i++)
		{
			if (i > 0)
			{
				buffer1.append(',');
			}
			buffer1.append(getValues()[i]);
		}
		buffer1.append(";");
		if (getValueDetails() != null && getValueDetails().length() > 0)
		{
			buffer1.append(getValueDetails());
		}
		buffer1.append(";");
		if (getUnit() != null && getUnit().length() > 0)
		{
			buffer1.append(getUnit());
		}
		buffer1.append(";");
		buffer1.append(getOrder() == null ? "0" : getOrder().toString());
		buffer1.append(";");
		if (getDescription() != null && getDescription().length() > 0)
		{
			buffer1.append(getDescription());
		}
		buffer1.append(";");
		return buffer1.toString();
	}


	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (!(obj instanceof FeatureContainer))
		{
			return false;
		}
		final FeatureContainer other = (FeatureContainer) obj;

		if (!getQualifier().equals(other.getQualifier()))
		{
			return false;
		}
		// compare values
		if (other.getValues().length != getValues().length)
		{
			return false;
		}
		for (int i = 0; i < getValues().length; i++)
		{
			if (!compareObjectHandlingNull(other.getValues()[i], getValues()[i]))
			{
				return false;
			}
		}
		return (compareObjectHandlingNull(other.getUnit(), getUnit()) && compareObjectHandlingNull(other.getOrder(), getOrder())
				&& compareObjectHandlingNull(other.getDescription(), getDescription()) && compareObjectHandlingNull(
					other.getValueDetails(), getValueDetails()));
	}

	private static boolean compareObjectHandlingNull(final Object object1, final Object object2)
	{
		// both are null or equal
		if (object1 == object2)
		{
			return true;
		}
		// only o1 is null (avoid NullpointerException)
		if (object1 == null)
		{
			return false;
		}
		return object1.equals(object2);
	}
}
