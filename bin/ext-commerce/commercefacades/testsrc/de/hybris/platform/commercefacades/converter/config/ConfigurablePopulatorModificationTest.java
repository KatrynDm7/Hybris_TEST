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
package de.hybris.platform.commercefacades.converter.config;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.converter.impl.DefaultConfigurablePopulator;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for the {@link ConfigurablePopulatorModification} type.
 */
@UnitTest
public class ConfigurablePopulatorModificationTest
{
	@Test
	public void testInitialize()
	{
		final DefaultConfigurablePopulator modifiablePopulator = new DefaultConfigurablePopulator();

		final ConfigurablePopulatorModification mod1 = new ConfigurablePopulatorModification();
		mod1.setTarget(modifiablePopulator);
		mod1.setKeyType("de.hybris.platform.commercefacades.converter.config.ConfigurablePopulatorModificationTest$OptionEnum");
		mod1.setKey("OPTION_1");

		final ConfigurablePopulatorModification mod2 = new ConfigurablePopulatorModification();
		mod2.setTarget(modifiablePopulator);
		mod2.setKeyType("de.hybris.platform.commercefacades.converter.config.ConfigurablePopulatorModificationTest$OptionEnum");
		mod2.setKey("OPTION_2");

		mod1.initialize();
		mod2.initialize();

		Assert.assertEquals(OptionEnum.OPTION_1, mod1.getResolvedKey());
		Assert.assertEquals(OptionEnum.OPTION_2, mod2.getResolvedKey());
		Assert.assertTrue(modifiablePopulator.getModifications().contains(mod1));
		Assert.assertTrue(modifiablePopulator.getModifications().contains(mod2));
	}

	private enum OptionEnum
	{
		OPTION_1, OPTION_2;
	}
}
