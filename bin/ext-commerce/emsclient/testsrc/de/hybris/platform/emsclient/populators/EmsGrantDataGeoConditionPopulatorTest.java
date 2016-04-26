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
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.entitlementservices.data.EmsGrantData;

import com.hybris.services.entitlements.condition.ConditionData;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/emsclient/test/geo-populator-test-spring.xml")
public class EmsGrantDataGeoConditionPopulatorTest
{
	@Autowired
	private Populator<EmsGrantData, ConditionData> populator;

	@Test
	public void emptyConditions()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();

		source.setConditionGeo(Collections.<String>emptyList());

		populator.populate(source,target);

		assertNull(target.getType());
		assertTrue(target.getProperties() == null || target.getProperties().isEmpty());
	}

	@Test
	public void noConditions()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();

		source.setConditionGeo(Collections.<String>emptyList());

		populator.populate(source,target);

		assertNull(target.getType());
		assertTrue(target.getProperties() == null || target.getProperties().isEmpty());
	}

	@Test
	public void singleLocation()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();
		final String location = "Deutschland/Bayern/München";

		source.setConditionGeo(Arrays.asList(location));

		populator.populate(source,target);

		assertEquals("geo", target.getType());
		assertEquals(1, target.getProperties().size());
		assertEquals(location, target.getProperty("geoPath"));
	}

	@Test
	public void multiLocations()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();
		final String location1 = "Deutschland/Bayern/München";
		final String location2 = "Россия/Омская область/Тара";

		source.setConditionGeo(Arrays.asList(location1, location2));

		populator.populate(source,target);

		assertEquals("geo", target.getType());
		assertEquals(1, target.getProperties().size());
		assertEquals(location1 + ',' + location2, target.getProperty("geoPath"));
	}

	/*
	 * Errors of this kind are to be filtered by ProductEntitlementValidateInterceptor and server side of EMS
	 */
	@Test
	public void trailingSlash()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();
		final String location = "Deutschland/";

		source.setConditionGeo(Arrays.asList(location));

		populator.populate(source,target);

		assertEquals("geo", target.getType());
		assertEquals(1, target.getProperties().size());
		assertEquals(location, target.getProperty("geoPath"));
	}

	/*
	 * Errors of this kind are to be filtered by ProductEntitlementValidateInterceptor and server side of EMS
	 */
	@Test
	public void tooManyLevels()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();
		final String location = "Россия/Омская область/Тарский район/Тара";

		source.setConditionGeo(Arrays.asList(location));

		populator.populate(source,target);

		assertEquals("geo", target.getType());
		assertEquals(1, target.getProperties().size());
		assertEquals(location, target.getProperty("geoPath"));
	}

	/*
	 * Errors of this kind are to be filtered by ProductEntitlementValidateInterceptor and server side of EMS
	 */
	@Test
	public void missingRegion()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();
		final String location = "England//Liverpool";

		source.setConditionGeo(Arrays.asList(location));

		populator.populate(source,target);

		assertEquals("geo", target.getType());
		assertEquals(1, target.getProperties().size());
		assertEquals(location, target.getProperty("geoPath"));
	}

	/*
	 * Errors of this kind are to be filtered by ProductEntitlementValidateInterceptor and server side of EMS
	 */
	@Test
	public void leadingSlash()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();
		final String location = "/NY";

		source.setConditionGeo(Arrays.asList(location));

		populator.populate(source,target);

		assertEquals("geo", target.getType());
		assertEquals(1, target.getProperties().size());
		assertEquals(location, target.getProperty("geoPath"));
	}

	/*
	 * Errors of this kind are to be filtered by ProductEntitlementValidateInterceptor and server side of EMS
	 */
	@Test
	public void commasInPathItem()
	{
		final EmsGrantData source = new EmsGrantData();
		final ConditionData target = new ConditionData();
		final String location = "Germany/Bayern/Munich,England/Merseyside/Liverpool";

		source.setConditionGeo(Arrays.asList(location));

		populator.populate(source,target);

		assertEquals("geo", target.getType());
		assertEquals(1, target.getProperties().size());
		assertEquals(location, target.getProperty("geoPath"));
	}

}
