package de.hybris.platform.healthcheck.internal;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.healthcheck.HealthCheck;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "/test/HealthCheckTest-context.xml" })
@IntegrationTest
public class DefaultHealthCheckRegistryIntegrationTest
{
	@Resource(name = "healthCheckRegistry")
	private HealthCheckRegistry registry;
	@Resource
	private HealthCheck licenseHealthCheck;


	@Test
	public void shouldReturnOnlyRegisteredHealthCheckers() throws Exception
	{
		// when
		final Iterable<HealthCheck> checkers = registry.getHealthChecks();

		// then
		assertThat(checkers).containsOnly(licenseHealthCheck);
	}
}
