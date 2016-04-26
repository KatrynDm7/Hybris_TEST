package de.hybris.platform.webservices;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.client.ClientResponse;

import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.dto.CronJobDTO;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.internal.model.ScriptingJobModel;
import de.hybris.platform.test.RunnerCreator;
import de.hybris.platform.test.TestThreadsHolder;

public class ConcurrentCommandsTest extends AbstractWebServicesTest {
	private static final int NUMBER_OF_THREADS = 16;
	private static final int NUMBER_OF_REQUESTS_PER_THREAD = 100;
	private String[] cronJobIds;

	@Before
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		cronJobIds = createTestCronJobs();
	}

	@Test
	public void shouldBeAbleToExecuteMultipleCommandRequestsConcurrently()
	{
		final StartCronJobCreator startCronJobCreator = new StartCronJobCreator(cronJobIds, NUMBER_OF_REQUESTS_PER_THREAD);
		final TestThreadsHolder<StartCronJob> testThreads = new TestThreadsHolder<>(NUMBER_OF_THREADS, startCronJobCreator);
		
		testThreads.startAll();
		
		assertThat(testThreads.waitForAll(5, TimeUnit.MINUTES)).isTrue();
		
		for (final StartCronJob scj : testThreads.getRunners())
		{
			assertThat(scj.getSucceededRequests()).isEqualTo(NUMBER_OF_REQUESTS_PER_THREAD);
		}
		
		for (int i = 0; i < NUMBER_OF_THREADS; i++)
		{
			final String query = "select {PK} from {" + TitleModel._TYPECODE +"} where {" + TitleModel.NAME + "} = ?id";
			final ImmutableMap<String, Object> params = ImmutableMap.<String, Object>of("id", cronJobIds[i]);
			
			final List<TitleModel> result = flexibleSearchService.<TitleModel>search(query, params).getResult();
			
			assertThat(result).isNotNull().hasSize(NUMBER_OF_REQUESTS_PER_THREAD);
		}
	}
	
	private String[] createTestCronJobs()
	{
		final String[] ids = new String[NUMBER_OF_THREADS];
		for (int i = 0; i < NUMBER_OF_THREADS; i++)
		{
			final String id = UUID.randomUUID().toString();
			final ScriptModel script = createScript(id);
			final JobModel job = createJob(script);
			final CronJobModel cronJob = createCronJob(job);
			ids[i] = cronJob.getCode();
		}
		modelService.saveAll();
		return ids;
	}

	private ScriptModel createScript(final String id)
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		
		final String content = "title = modelService.create(de.hybris.platform.core.model.user.TitleModel.class)\n" +
				"title.setCode(UUID.randomUUID().toString())\n" +
				"title.setName(\"" +id + "\")\n" +
				"modelService.saveAll()\n" +
				"\"OK\"";
		
		script.setScriptType(ScriptType.GROOVY);
		script.setCode(id);
		script.setContent(content);

		return script;
	}
	
	private JobModel createJob(final ScriptModel script)
	{
		final ScriptingJobModel job = modelService.create(ScriptingJobModel.class);
		
		job.setScriptURI("model://" + script.getCode());
		job.setCode(script.getCode());
		
		return job;
	}
	
	private CronJobModel createCronJob(final JobModel job)
	{
		final CronJobModel cronJob = modelService.create(CronJobModel.class);
		
		cronJob.setCode(job.getCode());
		cronJob.setSingleExecutable(Boolean.FALSE);
		cronJob.setJob(job);
		
		return cronJob;
	}
	
	private class StartCronJobCreator implements RunnerCreator<StartCronJob>
	{
		private final String[] cronJobIds;
		private final int numberOfRequestsPerThread;
		
		public StartCronJobCreator(final String[] cronJobIds, final int numberOfRequestsPerThread)
		{
			this.cronJobIds = cronJobIds;
			this.numberOfRequestsPerThread = numberOfRequestsPerThread;
		}

		@Override
		public StartCronJob newRunner(final int threadNumber)
		{
			return new StartCronJob(cronJobIds[threadNumber], numberOfRequestsPerThread);
		}
	}
	
	private class StartCronJob implements Runnable
	{
		private final String cronJobId;
		private final int numberOfRequests;
		private volatile int succeededRequests;
		
		public StartCronJob(final String cronJobId, final int numberOfRequests)
		{
			this.cronJobId = cronJobId;
			this.numberOfRequests = numberOfRequests;
		}

		@Override
		public void run()
		{
			succeededRequests = 0;
			for (int i = 0; i < numberOfRequests; i++)
			{
				final CronJobDTO cronJobDTO = new CronJobDTO();
				cronJobDTO.setCode(cronJobId);

				final ClientResponse result = webResource.
						path("cronjobs/" + cronJobId).
						queryParam("cmd", "StartCronJobCommand").
						queryParam("synchronous", "true").
						cookie(tenantCookie).
						header(HEADER_AUTH_KEY, HEADER_AUTH_VALUE_BASIC_ADMIN).
						accept(MediaType.APPLICATION_XML).
						entity(cronJobDTO).put(ClientResponse.class);

				assertThat(result.getClientResponseStatus().getFamily()).isEqualTo(Family.SUCCESSFUL);

				succeededRequests++;
			}
		}
		
		public int getSucceededRequests()
		{
			return succeededRequests;
		}
	}
}
