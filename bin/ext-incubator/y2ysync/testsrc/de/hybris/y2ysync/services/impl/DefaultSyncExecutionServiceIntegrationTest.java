package de.hybris.y2ysync.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.services.SyncExecutionService;

import javax.annotation.Resource;

import org.junit.Test;

import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;


@IntegrationTest
public class DefaultSyncExecutionServiceIntegrationTest extends ServicelayerBaseTest
{
	@Resource(name = "syncExecutionService")
	private SyncExecutionService service;
	@Resource
	private ModelService modelService;

	@Test
    public void shouldCreateSyncJobForDataHub() throws Exception
    {
    	// given
        final String syncJobCode = "testJob";
        final Y2YStreamConfigurationContainerModel containter = createStreamConfigurationContainer();
    	
    	// when
        final Y2YSyncJobModel job = service.createSyncJobForDataHub(syncJobCode, containter);

        // then
        assertThat(job).isNotNull();
        assertThat(modelService.isNew(job)).isTrue();
        assertThat(job.getSyncType()).isEqualTo(Y2YSyncType.DATAHUB);
        assertThat(job.getDataHubUrl()).isNull();
        assertThat(job.getStreamConfigurationContainer()).isEqualTo(containter);
    }

	@Test
    public void shouldCreateSyncJobForDataHubWithCustomURL() throws Exception
    {
    	// given
        final String syncJobCode = "testJob";
        final String customURL = "http://foo.bar.baz";
        final Y2YStreamConfigurationContainerModel containter = createStreamConfigurationContainer();

    	// when
        final Y2YSyncJobModel job = service.createSyncJobForDataHub(syncJobCode, containter, customURL);

        // then
        assertThat(job).isNotNull();
        assertThat(modelService.isNew(job)).isTrue();
        assertThat(job.getSyncType()).isEqualTo(Y2YSyncType.DATAHUB);
        assertThat(job.getDataHubUrl()).isEqualTo(customURL);
        assertThat(job.getStreamConfigurationContainer()).isEqualTo(containter);
    }

    @Test
    public void shouldCreateSyncJobForZip() throws Exception
    {
        // given
        final String syncJobCode = "testJob";
        final Y2YStreamConfigurationContainerModel containter = createStreamConfigurationContainer();

        // when
        final Y2YSyncJobModel job = service.createSyncJobForZip(syncJobCode, containter);

        // then
        assertThat(job).isNotNull();
        assertThat(modelService.isNew(job)).isTrue();
        assertThat(job.getSyncType()).isEqualTo(Y2YSyncType.ZIP);
        assertThat(job.getDataHubUrl()).isNull();
        assertThat(job.getStreamConfigurationContainer()).isEqualTo(containter);
    }

    @Test
    public void shouldReturnAllSyncJobsPresentInTheSystem() throws Exception
    {
    	// given
        final Y2YStreamConfigurationContainerModel containter = createStreamConfigurationContainer();
        final Y2YSyncJobModel job1 = service.createSyncJobForZip("testJob1", containter);
        final Y2YSyncJobModel job2 = service.createSyncJobForDataHub("testJob2", containter);
        modelService.saveAll(job1, job2);

    	// when
        final Collection<Y2YSyncJobModel> syncJobs = service.getAllSyncJobs();

        // then
        assertThat(syncJobs).isNotNull().hasSize(2);
        assertThat(syncJobs).containsOnly(job1, job2);
    }

	private Y2YStreamConfigurationContainerModel createStreamConfigurationContainer()
	{
		final Y2YStreamConfigurationContainerModel container = modelService.create(Y2YStreamConfigurationContainerModel.class);
		container.setId("testContainer");
		modelService.save(container);

		return container;
	}
}
