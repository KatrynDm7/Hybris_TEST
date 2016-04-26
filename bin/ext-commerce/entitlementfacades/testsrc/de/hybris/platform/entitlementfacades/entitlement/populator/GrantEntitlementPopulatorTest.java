package de.hybris.platform.entitlementfacades.entitlement.populator;

import com.hybris.services.entitlements.api.GrantData;
import com.hybris.services.entitlements.condition.ConditionData;
import com.hybris.services.entitlements.conversion.DateTimeConverter;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.entitlementfacades.data.EntitlementData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link GrantEntitlementPopulator}.
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/entitlementfacades/test/entitlementfacades-grant-test-spring.xml")
public class GrantEntitlementPopulatorTest {
	public static final String START_TIME = "2014-01-01T13:14:15Z";
	public static final String END_TIME = "2015-01-01T13:14:15Z";
	public static final String GRANT_TIME = "2013-12-31T07:08:09Z";

	@Autowired
	private GrantEntitlementPopulator<GrantData, EntitlementData> populator;

	@Autowired
	private DateTimeConverter dateTimeConverter;

	@Test
	public void testMeteredTimeframeConditions() throws ParseException {
		// Given a grant dto with {metered, timeframe} conditions.
		final ConditionData metered = new ConditionData();
		metered.setType("metered");
		metered.setProperty("maxQuantity", "10");
		final ConditionData timeframe = new ConditionData();
		timeframe.setType("timeframe");
		timeframe.setProperty("startTime", START_TIME);
		timeframe.setProperty("endTime", END_TIME);
		final GrantData grantData = new GrantData();
		grantData.setGrantSource("grant source");
		grantData.setGrantSourceId("grant source id");
		grantData.setGrantTime(GRANT_TIME);
		grantData.addCondition(metered);
		grantData.addCondition(timeframe);

		// When the populator converts the dto
		final EntitlementData entitlementData = new EntitlementData();
		entitlementData.setName("name");
		entitlementData.setDescription("description");
		populator.populate(grantData, entitlementData);

		// Then time bounds should be correct
		assertEquals(dateTimeConverter.convertStringToDate(START_TIME), entitlementData.getStartTime());
		assertEquals(dateTimeConverter.convertStringToDate(END_TIME), entitlementData.getEndTime());
		assertEquals(dateTimeConverter.convertStringToDate(GRANT_TIME), entitlementData.getGrantTime());
	}

	@Test
	public void testTimeframeMeteredConditions() throws ParseException {
		// Given a grant dto with {metered, timeframe} conditions.
		final ConditionData metered = new ConditionData();
		metered.setType("metered");
		metered.setProperty("maxQuantity", "10");
		final ConditionData timeframe = new ConditionData();
		timeframe.setType("timeframe");
		timeframe.setProperty("startTime", START_TIME);
		timeframe.setProperty("endTime", END_TIME);
		final GrantData grantData = new GrantData();
		grantData.setGrantSource("grant source");
		grantData.setGrantSourceId("grant source id");
		grantData.setGrantTime(GRANT_TIME);
		grantData.addCondition(timeframe);
		grantData.addCondition(metered);

		// When the populator converts the dto
		final EntitlementData entitlementData = new EntitlementData();
		entitlementData.setName("name");
		entitlementData.setDescription("description");
		populator.populate(grantData, entitlementData);

		// Then time bounds should be correct
		assertEquals(dateTimeConverter.convertStringToDate(START_TIME), entitlementData.getStartTime());
		assertEquals(dateTimeConverter.convertStringToDate(END_TIME), entitlementData.getEndTime());
		assertEquals(dateTimeConverter.convertStringToDate(GRANT_TIME), entitlementData.getGrantTime());
	}
}
