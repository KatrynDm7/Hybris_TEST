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
 *
 *
 */
package de.hybris.platform.c4ccustomer.datasetup;

import de.hybris.platform.c4ccustomer.constants.C4ccustomerConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;


/**
 * Essential data creator.
 */
@SystemSetup(extension = C4ccustomerConstants.EXTENSIONNAME)
public class DataSetup extends AbstractSystemSetup
{
	private static final Logger LOG = Logger.getLogger(DataSetup.class);
	private ModelService modelService;

	/**
	 * Create essential data.
	 */
	@SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
	public void createEssentialData()
	{
		createScript("c4cSync", "c4ccustomer/essentialdata/Synchronize.groovy");
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context the context providing selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		importImpexFile(context, "/c4ccustomer/import/projectdata-c4cdata-streams.impex");
	}

	protected void createScript(final String modelId, final String scriptResource)
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setScriptType(ScriptType.GROOVY);
		script.setContent(readFromResource(scriptResource));
		script.setCode(modelId);
		modelService.save(script);
	}

	protected String readFromResource(final String filename)
	{
		try (final InputStream stream = getClass().getClassLoader().getResourceAsStream(filename))
		{
			if (stream == null)
			{
				throw new IllegalStateException("Resource " + filename + " does not exist");
			}
			return IOUtils.toString(stream);
		}
		catch (final IOException e)
		{
			LOG.error("Failed to create essential data: error reading resource " + filename, e);
			throw new IllegalStateException("Failed to create essential data: error reading resource " + filename, e);
		}
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		return Collections.emptyList();
	}
}
