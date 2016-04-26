package de.hybris.platform.entitlementservices.communication;

import com.hybris.services.entitlements.api.*;
import com.hybris.services.entitlements.api.status.Status;
import com.hybris.services.entitlements.condition.ConditionData;
import com.hybris.services.entitlements.condition.CriterionData;
import de.hybris.bootstrap.annotations.IntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test communication to EMS API.
 *
 * <p>
 *     As only stub implementation of EMS is being checked,
 *     there are only generic asserts. We do not expect functional code from the stub.
 *     The goal of the tests is to check that API itself works in general,
 *     without any missing classes.
 * </p>
 *
 * @see com.hybris.services.entitlements.api.impl.StubEntitlementFacade
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/entitlement-api-spring.xml")
public class EmsStubTest
{
	@Autowired
	private EntitlementFacade ems;

	@Test
	public void shouldGetGrants()
	{
		final List<GrantData> grants = ems.getGrants("user", null, null, null);
		assertNotNull(grants);
	}

	@Test
	public void shouldGetSingleGrant()
	{
		final GrantData grant = ems.getGrant("grantId");
		assertNotNull(grant);
	}

	@Test
	public void shouldGrantEntitlement()
	{
		final GrantData grant = new GrantData();
		grant.setUserId("user");
		grant.setGrantSource("source");
		grant.setGrantSourceId("sourceId");
		final GrantData created = ems.createGrant(grant);
		assertNotNull(created);
		assertEquals(grant.getUserId(), created.getUserId());
		assertEquals(grant.getGrantSource(), created.getGrantSource());
		assertEquals(grant.getGrantSourceId(), created.getGrantSourceId());
	}

	@Test
	public void shouldExecuteSingleGrant()
	{
		final ExecuteResult result = ems.execute(
				Actions.CHECK, "user", "entitlementType",
				Collections.<CriterionData>emptyList(), true);
		assertNotNull(result);
	}

	@Test
	public void shouldExecuteMultipleGrants()
	{
		final ExecuteManyResult result = ems.execute(
				Actions.CHECK, "user",
				Collections.<ExecuteManyData>emptyList(), true);
		assertNotNull(result);
	}

	@Test
	public void shouldRevokeSingleGrant()
	{
		ems.revokeGrant("grantId");
	}

	@Test
	public void shouldRevokeMultipleGrants()
	{
		ems.revokeGrants("user", null, null, null);
	}

	@Test
	public void shouldUpdateConditions()
	{
		final ConditionData condition = new ConditionData();
		condition.setType("string");
		condition.setProperty("name", "value");
		final GrantData grant = ems.updateConditions("grantId", Collections.singletonList(condition));
		assertNotNull(grant);
	}

	@Test
	public void shouldDeleteConditions()
	{
		final GrantData grant = ems.deleteConditions("grantId", "metered");
		assertNotNull(grant);
	}

	@Test
	public void shouldUpdateStatus()
	{
		final GrantData grant = ems.updateGrantStatus("grantId", Status.SUSPENDED);
		assertNotNull(grant);
	}

	@Test
	public void shouldCreateProperties()
	{
		final GrantData grant = ems.createGrantProperty("grantId", "propertyName", "propertyValue");
		assertNotNull(grant);
	}

	@Test
	public void shouldUpdateProperties()
	{
		final GrantData grant = ems.updateGrantProperty("grantId", "propertyName", "newValue");
		assertNotNull(grant);
	}

	@Test
	public void shouldDeleteGrantProperties()
	{
		final GrantData grant = ems.deleteGrantProperty("grantId", "propertyName");
		assertNotNull(grant);
	}

	@Test
	public void shouldAddToGrantProperty()
	{
		final GrantData grant = ems.addGrantProperty("grantId", "propertyName", 1);
		assertNotNull(grant);
	}
}
