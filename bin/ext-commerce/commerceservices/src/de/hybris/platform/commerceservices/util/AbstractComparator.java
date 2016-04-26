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
package de.hybris.platform.commerceservices.util;

import java.util.Comparator;
import java.util.Date;


/**
 * Base class for building comparators.
 */
public abstract class AbstractComparator<T> implements Comparator<T>
{
	protected static final int BEFORE = -1;
	protected static final int EQUAL = 0;
	protected static final int AFTER = 1;

	@Override
	public int compare(final T instance1, final T instance2)
	{
		// Check if the 2 references are equal (this also checks for both nulls)
		if (instanceEquals(instance1, instance2))
		{
			return EQUAL;
		}

		// Check for one or other of the instances being null - nulls are ordered first
		if (instance1 == null)
		{
			return BEFORE;
		}
		if (instance2 == null)
		{
			return AFTER;
		}

		return compareInstances(instance1, instance2);
	}

	/**
	 * Implement method to perform the comparison. The instances passed are different and neither is null.
	 * 
	 * @param instance1
	 *           first instance
	 * @param instance2
	 *           second instance
	 * @return return one of {@link #BEFORE}, {@link #EQUAL}, or {@link #AFTER}.
	 */
	protected abstract int compareInstances(final T instance1, final T instance2);

	protected boolean instanceEquals(final Object object1, final Object object2)
	{
		return object1 == object2;//NOPMD
	}

	//
	// Helper methods for comparing common value types
	//

	//
	// Helper methods
	//

	protected int compareValues(final int int1, final int int2)
	{
		return (int1 < int2 ? BEFORE : (int1 == int2 ? EQUAL : AFTER));
	}

	protected int compareValues(final long long1, final long long2)
	{
		return (long1 < long2 ? BEFORE : (long1 == long2 ? EQUAL : AFTER));
	}

	protected int compareValues(final double double1, final double double2)
	{
		return (double1 < double2 ? BEFORE : (double1 == double2 ? EQUAL : AFTER));
	}

	protected int compareValues(final Date date1, final Date date2)
	{
		// Check if the 2 references are equal (this also checks for both nulls)
		if (instanceEquals(date1, date2))
		{
			return EQUAL;
		}

		// Check for one or other of the instances being null - nulls are ordered first
		if (date1 == null)
		{
			return BEFORE;
		}
		if (date2 == null)
		{
			return AFTER;
		}

		return date1.compareTo(date2);
	}

	protected int compareValues(final String string1, final String string2, final boolean caseSensitive)
	{
		// Check if the 2 references are equal (this also checks for both nulls)
		if (instanceEquals(string1, string2))
		{
			return EQUAL;
		}

		// Check for one or other of the instances being null - nulls are ordered first
		if (string1 == null)
		{
			return BEFORE;
		}
		if (string2 == null)
		{
			return AFTER;
		}

		return caseSensitive ? string1.compareTo(string2) : string1.compareToIgnoreCase(string2);
	}
}
