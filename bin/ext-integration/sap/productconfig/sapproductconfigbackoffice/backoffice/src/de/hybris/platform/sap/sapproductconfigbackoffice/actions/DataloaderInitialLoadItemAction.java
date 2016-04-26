/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.sapproductconfigbackoffice.actions;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.sap.core.configuration.jalo.SAPConfiguration;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.core.configuration.model.SAPRFCDestinationModel;
import de.hybris.platform.sap.sapproductconfigbackoffice.dataloader.configuration.DataloaderConfiguration;
import de.hybris.platform.sap.sapproductconfigbackoffice.dataloader.configuration.DataloaderSource;
import de.hybris.platform.sap.sapproductconfigbackoffice.dataloader.configuration.InitialDownloadConfiguration;

import java.io.File;

import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.sap.custdev.projects.fbs.slc.dataloader.standalone.DataloaderFacadeImpl;
import com.sap.custdev.projects.fbs.slc.dataloader.standalone.Status;


public class DataloaderInitialLoadItemAction implements CockpitAction<SAPConfigurationModel, String>
{



	private static final Logger LOG = Logger.getLogger(DataloaderInitialLoadItemAction.class);

	@Override
	public ActionResult<String> perform(final ActionContext<SAPConfigurationModel> ctx)
	{
		final ActionResult<String> result = new ActionResult<String>(ActionResult.SUCCESS);


		final SAPConfigurationModel configuration = ctx.getData();

		if (configuration == null)
		{
			result.setResultCode(ActionResult.ERROR);
			Messagebox.show(ctx.getLabel("text.sapproductconfig_configuration_not_set"));
			return result;

		}

		final DataloaderConfiguration dataloaderConfiguration = new DataloaderConfiguration();
		final DataloaderSource dataloaderSource = getSAPSource(configuration);
		if (dataloaderSource == null)
		{
			result.setResultCode(ActionResult.ERROR);
			Messagebox.show(ctx.getLabel("text.sapproductconfig_sapsource_description"));
			return result;
		}

		dataloaderConfiguration.setSource(dataloaderSource);

		final InitialDownloadConfiguration initialDownloadConfiguration = getFilterFiles(configuration);
		dataloaderConfiguration.setInitialDownloadConfiguration(initialDownloadConfiguration);




		if (LOG.isDebugEnabled())
		{
			LOG.debug("Running initial data load [SAP_SYSTEM='" + dataloaderSource.getEccSetting().getMessageServer()
					+ "';SAP_SID='" + dataloaderSource.getEccSetting().getSid() + "';SAP_RFC_DESTINATION='"
					+ dataloaderSource.getRfcDestination() + "']");
		}

		return performDataload(dataloaderConfiguration, ctx);
	}


	private ActionResult<String> performDataload(final DataloaderConfiguration dataloaderConfiguration,
			final ActionContext<SAPConfigurationModel> ctx)
	{
		final DataloaderFacadeImpl facadeImpl = new DataloaderFacadeImpl();

		Status status = facadeImpl.createTables(null);

		final ActionResult<String> result = new ActionResult<String>(ActionResult.SUCCESS);

		if (!status.isOK())
		{
			result.setResultCode(ActionResult.ERROR);
			Messagebox.show(status.getMessage());

			return result;

		}


		status = facadeImpl.initialDownload(dataloaderConfiguration,
				new com.sap.custdev.projects.fbs.slc.dataloader.standalone.IProgressListener()
				{

					@Override
					public void progressMessage(final String msg)
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug(msg);
						}
					}
				});

		if (!status.isOK())
		{
			result.setResultCode(ActionResult.ERROR);
			Messagebox.show(status.getMessage());

			return result;

		}

		Messagebox.show(ctx.getLabel("text.sapproductconfig_initial_load_successful"));

		return result;

	}

	private DataloaderSource getSAPSource(final SAPConfigurationModel configuration)
	{

		final SAPRFCDestinationModel sapServer = configuration.getSapproductconfig_sapServer();


		final String rfcDestination = configuration.getSapproductconfig_sapRFCDestination();

		DataloaderSource dataloaderSource = null;

		boolean isConnectionTypeAsPrimitive = false;

		if (sapServer != null)
		{
			if (sapServer.getConnectionType() != null)
			{
				isConnectionTypeAsPrimitive = sapServer.getConnectionType();
			}

			if (isConnectionTypeAsPrimitive)
			{
				dataloaderSource = new DataloaderSource(sapServer.getInstance(), sapServer.getTargetHost(), sapServer.getUserid(),
						sapServer.getPassword(), sapServer.getClient(), rfcDestination);
			}
			else
			{
				dataloaderSource = new DataloaderSource(sapServer.getSid(), sapServer.getMessageServer(), sapServer.getGroup(),
						sapServer.getUserid(), sapServer.getPassword(), sapServer.getClient(), rfcDestination);
			}
		}
		return dataloaderSource;
	}

	private InitialDownloadConfiguration getFilterFiles(final SAPConfigurationModel configuration)
	{


		MediaModel filterFile = configuration.getSapproductconfig_filterKnowledgeBase();

		final String kbFilterFile = getAbsolutFilePathForMedia(filterFile);

		filterFile = configuration.getSapproductconfig_filterMaterial();

		final String materialsFilterFile = getAbsolutFilePathForMedia(filterFile);

		filterFile = configuration.getSapproductconfig_filterCondition();

		final String conditionsFilterFile = getAbsolutFilePathForMedia(filterFile);

		final InitialDownloadConfiguration initialDownloadConfiguration = new InitialDownloadConfiguration(kbFilterFile,
				materialsFilterFile, conditionsFilterFile);

		return initialDownloadConfiguration;
	}

	private String getAbsolutFilePathForMedia(final MediaModel filterFile)
	{

		String filterFileAbsolutPath = null;

		if (filterFile != null)
		{

			final boolean isAlive = !filterFile.getItemModelContext().isRemoved() && filterFile.getItemModelContext().isUpToDate();

			if (filterFile.getSize() != 0 && isAlive)
			{
				final File file = MediaManager.getInstance().getMediaAsFile(filterFile.getFolder().getQualifier(),
						filterFile.getLocation());

				filterFileAbsolutPath = file.getAbsolutePath();

			}
		}

		return filterFileAbsolutPath;
	}


	private String getAbsolutFilePathForMedia(final SAPConfiguration configuration, final String property)
	{


		final Media filterFile = (Media) configuration.getProperty(property);

		String filterFileAbsolutPath = null;
		if (filterFile != null && filterFile.hasData() && filterFile.isAlive())
		{
			filterFileAbsolutPath = filterFile.getFile().getAbsoluteFile().toString();
		}

		return filterFileAbsolutPath;
	}

	@SuppressWarnings("deprecation")
	private String getEnumerationValue(final SAPConfiguration configuration, final String property) throws JaloBusinessException
	{
		return (String) ((EnumerationValue) configuration.getProperty(property)).getAttribute("code");
	}


	@Override
	public boolean canPerform(final ActionContext<SAPConfigurationModel> arg0)
	{
		return true;
	}


	@Override
	public String getConfirmationMessage(final ActionContext<SAPConfigurationModel> ctx)
	{
		return ctx.getLabel("text.sapproductconfig_initial_load_successful");
	}


	@Override
	public boolean needsConfirmation(final ActionContext<SAPConfigurationModel> arg0)
	{


		return false;
	}




}
