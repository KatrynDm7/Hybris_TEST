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
package de.hybris.platform.converters;

import java.util.List;


/**
 * Interface for a list of populators.
 * 
 * @param <SOURCE>
 *           the type of the source object
 * @param <TARGET>
 *           the type of the destination object
 */
public interface PopulatorList<SOURCE, TARGET>
{
	/**
	 * Get the list of populators.
	 * 
	 * @return the populators.
	 */
	List<Populator<SOURCE, TARGET>> getPopulators();

	/**
	 * Set the list of populators.
	 * 
	 * @param populators
	 *           the populators
	 */
	void setPopulators(List<Populator<SOURCE, TARGET>> populators);
}
