package de.hybris.y2ysync.task.runner.internal;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.jalo.Y2YStreamConfigurationContainer;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import de.hybris.y2ysync.services.SyncExecutionService;
import org.apache.commons.io.IOUtils;
import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ImportZipCreatorTest extends ServicelayerTransactionalBaseTest
{
	private final static String SYNC_EXECUTION_ID = "testExecutionId";

	@Resource
	private ImportZipCreator importZipCreator;
	@Resource
	private ModelService modelService;
	@Resource
	private MediaService mediaService;
	@Resource
	private TypeService typeService;
    private Y2YSyncCronJobModel cronJob;

    @Before
	public void setUp() throws Exception
	{
        cronJob = createExportCronJob();

		createSyncMedia("Product", ";productCode1;some description");
		createSyncMedia("Product", ";productCode2;other description");
		createSyncMedia("Title", ";titleCode1;some description");
	}

    private Y2YSyncCronJobModel createExportCronJob()
    {
        final Y2YStreamConfigurationContainerModel container = modelService.create(Y2YStreamConfigurationContainerModel.class);
        container.setId("testContainer");
        modelService.save(container);

        final Y2YSyncJobModel syncJob = modelService.create(Y2YSyncJobModel.class);
        syncJob.setCode("testJob");
        syncJob.setSyncType(Y2YSyncType.ZIP);
        syncJob.setStreamConfigurationContainer(container);
        modelService.save(syncJob);

        final Y2YSyncCronJobModel cronJob = modelService.create(Y2YSyncCronJobModel.class);
        cronJob.setCode(SYNC_EXECUTION_ID);
        cronJob.setJob(syncJob);
        modelService.save(cronJob);

        return cronJob;
    }

	private void createSyncMedia(final String type, final String impexLine)
	{
		final SyncImpExMediaModel media = modelService.create(SyncImpExMediaModel.class);
		media.setCode("test-code-" + UUID.randomUUID());
		media.setSyncType(typeService.getComposedTypeForCode(type));
		media.setImpexHeader("INSERT_UPDATE " + type + ";code[unique=true];description;");
        media.setExportCronJob(cronJob);
		modelService.save(media);
		mediaService.setStreamForMedia(media, getStreamForString(impexLine));
	}


	private InputStream getStreamForString(final String line)
	{
		return new ByteArrayInputStream(line.getBytes());
	}

	@Test
	public void shouldGenerateMainZipMedia() throws Exception
	{
		// when
		final ImportPackage importPackage = importZipCreator.createImportMedias(SYNC_EXECUTION_ID);
		final MediaModel finalZip = importPackage.getMediaData();

		// then
		assertThat(modelService.isNew(finalZip)).isFalse();
		assertThat(finalZip.getCode()).isEqualTo("data-" + SYNC_EXECUTION_ID);
		assertThat(mediaService.hasData(finalZip)).isTrue();

		ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia(finalZip)).hasNumEntries(4);
		ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia(finalZip))
				.containsEntryWithText("Product-339d26e5fab4676cfc6e25c6a211b6b9-0.csv",
                        ";productCode1;some description");
		ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia(finalZip)).containsEntryWithText("Product-339d26e5fab4676cfc6e25c6a211b6b9-1.csv",
				";productCode2;other description");
		ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia(finalZip)).containsEntryWithText("Title-d509069a33d3922e3455d2e5403885ed-0.csv",
				";titleCode1;some description");
		ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia(finalZip)).containsEntryWithText("importscript.impex",
				"INSERT_UPDATE Title;code[unique=true];description;");
		ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia(finalZip)).containsEntryWithText("importscript.impex",
				"INSERT_UPDATE Product;code[unique=true];description;");
		ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia(finalZip)).containsEntryWithText("importscript.impex",
				"\"#% impex.includeExternalDataMedia(\"\"Title-d509069a33d3922e3455d2e5403885ed-0.csv\"\", \"\"UTF-8\"\", ';', 1, -1);\"");
		ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia(finalZip)).containsEntryWithText("importscript.impex",
				"\"#% impex.includeExternalDataMedia(\"\"Product-339d26e5fab4676cfc6e25c6a211b6b9-0.csv\"\", \"\"UTF-8\"\", ';', 1, -1);\"");
		ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia(finalZip)).containsEntryWithText("importscript.impex",
				"\"#% impex.includeExternalDataMedia(\"\"Product-339d26e5fab4676cfc6e25c6a211b6b9-1.csv\"\", \"\"UTF-8\"\", ';', 1, -1);\"");
	}

	private ZipInputStream getZipInputStreamFromMedia(final MediaModel mediaModel)
	{
		return new ZipInputStream(mediaService.getStreamFromMedia(mediaModel));
	}


	private static class ZipInputStreamAssert extends GenericAssert<ZipInputStreamAssert, ZipInputStream>
	{

		protected ZipInputStreamAssert(final ZipInputStream actual)
		{
			super(ZipInputStreamAssert.class, actual);
		}

		public static ZipInputStreamAssert assertThat(final ZipInputStream actual)
		{
			return new ZipInputStreamAssert(actual);
		}

		public ZipInputStreamAssert hasNumEntries(final int num) throws IOException
		{
			try
			{
				int count = 0;
				while (actual.getNextEntry() != null)
				{
					count++;
				}

				Assertions.assertThat(count).isEqualTo(num);
				return this;
			}
			finally
			{
				IOUtils.closeQuietly(actual);
			}
		}

		public ZipInputStreamAssert containsEntryWithText(final String entryName, final String text) throws IOException
		{
			try
			{
				ZipEntry entry;
				while ((entry = actual.getNextEntry()) != null)
				{
					if (entry.getName().equals(entryName))
					{
						final StringWriter output = new StringWriter();
						IOUtils.copy(actual, output, "UTF-8");
						Assertions.assertThat(output.toString()).containsIgnoringCase(text);
						return this;
					}
				}


				fail("Zip stream does not contain entry named: " + entryName);
			}
			finally
			{
				IOUtils.closeQuietly(actual);
			}

			return null;
		}
	}
}
