package de.hybris.platform.entitlementservices.api;

import com.hybris.services.entitlements.api.GrantData;
import com.hybris.services.entitlements.condition.ConditionData;
import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests to ensure EMS API objects work correctly within the module.
 */
@UnitTest
public class GrantDataTest {
	@Test
	public void shouldCreateGrantData() {
		final GrantData dto = new GrantData();
		assertNotNull(dto);
	}

	@Test
	public void shouldOperateWithProperties() {
		final GrantData dto = new GrantData();
		dto.setProperty("test", "value");
		assertEquals("value", dto.getProperty("test"));
		assertEquals(1, dto.getProperties().size());
	}

	@Test
	public void shouldOperateWithConditions() {
		final GrantData grant = new GrantData();
		final ConditionData condition1 = new ConditionData();
		condition1.setType("string");
		condition1.setProperty("string", "value");
		final ConditionData condition2 = new ConditionData();
		condition2.setType("path");
		condition2.setProperty("file", "/a/b/c");
		grant.setCondition(condition1, condition2);
		assertEquals(2, grant.getConditions().size());
	}
}
