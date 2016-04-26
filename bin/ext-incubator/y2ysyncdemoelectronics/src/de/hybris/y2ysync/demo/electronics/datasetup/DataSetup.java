/*
 *
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.y2ysync.demo.electronics.datasetup;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.demo.electronics.constants.Y2ysyncdemoelectronicsConstants;

import org.springframework.beans.factory.annotation.Required;


@SystemSetup(extension = Y2ysyncdemoelectronicsConstants.EXTENSIONNAME)
public class DataSetup
{
	private static final String SYNC_SCRIPT = "import de.hybris.platform.servicelayer.search.FlexibleSearchQuery\n" +
            "import de.hybris.y2ysync.services.SyncExecutionService.ExecutionMode\n" +
            "\n" +
            "def productsJob = findJob '%s'\n" +
            "def classificationJob = findJob '%s'\n" +
            "def mediasJob = findJob '%s'\n" +
            "\n" +
            "syncExecutionService.startSync(productsJob, ExecutionMode.SYNC)\n" +
            "syncExecutionService.startSync(classificationJob, ExecutionMode.SYNC)\n" +
            "syncExecutionService.startSync(mediasJob, ExecutionMode.SYNC)\n" +
            "\n" +
            "\n" +
            "\n" +
            "def findJob(code) {\n" +
            "\tdef fQuery = new FlexibleSearchQuery('SELECT {PK} FROM {Y2YSyncJob} WHERE {code}=?code')\n" +
            "\tfQuery.addQueryParameter('code', code)\n" +
            "\n" +
            "\tflexibleSearchService.searchUnique(fQuery)\n" +
            "}";
	private static final String REMOVE_VERSION_MARKERS_SCRIPT = "import de.hybris.platform.servicelayer.search.FlexibleSearchQuery\n"
			+ "\n"
			+ "def fQuery = new FlexibleSearchQuery('SELECT {PK} FROM {ItemVersionMarker}')\n"
			+ "def result = flexibleSearchService.search(fQuery)\n"
			+ "\n"
			+ "result.getResult().forEach {\n"
			+ "modelService.remove(it) \n" + "}";

	private ModelService modelService;

	@SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
	public void setup()
	{
		createScript(
				"syncToDataHub",
				getSyncScriptContent("y2ySyncToDataHubProductsJob", "y2ySyncToDataHubClassificationSystemJob",
						"y2ySyncMediasToDataHubJob"));
		createScript(
				"syncToZip",
				getSyncScriptContent("y2ySyncToZipProductsJob", "y2ySyncToZipClassificationSystemJob",
						"y2ySyncMediasToZipJob"));
		createScript("removeVersionMarkers", REMOVE_VERSION_MARKERS_SCRIPT);
	}

	private String getSyncScriptContent(final String productsCronJobCode, final String classificationCronJobCode,
			final String mediasCronJobCode)
	{
		return String.format(SYNC_SCRIPT, productsCronJobCode, classificationCronJobCode, mediasCronJobCode);
	}

	private void createScript(final String code, final String content)
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setScriptType(ScriptType.GROOVY);
		script.setContent(content);
		script.setCode(code);
		modelService.save(script);
	}


	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
