package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Test;


/**
 * JUnit test suite for {@link PointOfServiceTypeEnum}
 */

@UnitTest
public class PointOfServiceTypeEnumTest
{
	private final String AGENCY_STRING = "AGENCY";

	@Test
	public void testAgencyExists()
	{
		final PointOfServiceTypeEnum agency = PointOfServiceTypeEnum.valueOf(AGENCY_STRING);
		Assert.assertNotNull(agency);
	}
}
