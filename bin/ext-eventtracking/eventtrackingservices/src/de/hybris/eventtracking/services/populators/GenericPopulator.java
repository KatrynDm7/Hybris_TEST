/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.eventtracking.services.populators;

import de.hybris.platform.converters.Populator;


/**
 * @author stevo.slavic
 *
 */
public interface GenericPopulator<SOURCE, TARGET> extends Populator<SOURCE, TARGET>
{
	boolean supports(Class<?> clazz);
}
