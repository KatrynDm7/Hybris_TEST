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
 */

package de.hybris.platform.emsclient.populators;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.jgroups.util.Util.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.entitlementservices.data.EmsGrantData;

import com.hybris.services.entitlements.condition.ConditionData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/emsclient/test/string-populator-test-spring.xml")
public class EmsGrantDataStringConditionPopulatorTest
{
	@Autowired
	private Populator<EmsGrantData, ConditionData> populator;

	@Test
	public void noString()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();

		populator.populate(source, target);

		assertNull(target.getType());
		assertTrue(target.getProperties() == null || target.getProperties().isEmpty());
	}

	@Test
	public void emptyString()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();

		source.setConditionString("");
		populator.populate(source, target);

		assertNull(target.getType());
		assertTrue(target.getProperties() == null || target.getProperties().isEmpty());
	}

	@Test
	public void string()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();

		final String value = "test";
		source.setConditionString(value);
		populator.populate(source, target);

		assertEquals("string", target.getType());
		assertEquals(value, target.getProperty("string"));
	}

}
