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
package de.hybris.platform.commercefacades.converter;

import de.hybris.platform.commercefacades.converter.config.ConfigurablePopulatorModification;


/**
 * Interface for modifiable configurable populators.
 */
public interface ModifiableConfigurablePopulator<SOURCE, TARGET, OPTION> extends ConfigurablePopulator<SOURCE, TARGET, OPTION>
{
	void addModification(ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> modification);

	void applyModification(ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> modification);
}
