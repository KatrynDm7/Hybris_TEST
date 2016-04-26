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
package de.hybris.platform.sap.sapcoreconfigurationbackoffice.actions;

import de.hybris.platform.sap.core.configuration.datahub.DataHubInitialLoadHandler;
import de.hybris.platform.sap.core.configuration.datahub.DataHubTransferLog;
import de.hybris.platform.sap.core.configuration.model.SAPAdministrationModel;
import de.hybris.platform.util.localization.Localization;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;


/**
 *
 */
public class SendToDataHubAction implements CockpitAction<SAPAdministrationModel, String>
{

	/**
	 * CRLF.
	 */
	public static final String CRLF = System.getProperty("line.separator");
	private static final String ITEM_CODE_TYPE = "type.";

	private static final String ITEM_CODE_NAME = ".name";
	private static final Logger LOG = Logger.getLogger(SendToDataHubAction.class.getName());

	@Resource
	private DataHubInitialLoadHandler sapCoreDataHubInitialLoadHandler;

	@Override
	public ActionResult<String> perform(final ActionContext<SAPAdministrationModel> ctx)
	{

		LOG.info("Execute initial upload of configuration data to data hub");
		final Collection<DataHubTransferLog> transferLogs = sapCoreDataHubInitialLoadHandler.executeInitialLoad();
		final ActionResult<String> result = getActionResult(transferLogs, ctx);
		LOG.info(result.getData());
		if (result.getResultCode().equals(ActionResult.SUCCESS))
		{
			Messagebox.show(ctx.getLabel("ActionResult.DataHubTransfer.success"));
		}
		else
		{
			Messagebox.show(ctx.getLabel("ActionResult.DataHubTransfer.failed"));
		}

		return result;

	}




	/**
	 * Returns the action result based on the passed transfer logs.
	 *
	 * @param transferLogs
	 *           transfer log collection
	 * @return {@link ActionResult}
	 */
	private ActionResult getActionResult(final Collection<DataHubTransferLog> transferLogs,
			final ActionContext<SAPAdministrationModel> ctx)
	{

		final StringBuffer actionMessageBuffer = new StringBuffer();
		final ActionResult<String> result = new ActionResult<String>(ActionResult.SUCCESS);

		for (final DataHubTransferLog transferLog : transferLogs)
		{
			final String itemCode = transferLog.getItemCode();
			final boolean isItemLog = (itemCode != null && !itemCode.isEmpty());
			if (isItemLog)
			{
				actionMessageBuffer.append(getNameForItemCode(transferLog.getItemCode())).append(":").append(CRLF);
			}
			if (transferLog.transferFailed())
			{

				result.setResultCode(ActionResult.ERROR);

			}
			final Collection<String> logCollection = transferLog.getLog();
			for (final String logEntry : logCollection)
			{
				if (isItemLog)
				{
					actionMessageBuffer.append(" - ");
				}
				actionMessageBuffer.append(logEntry).append(CRLF);
			}
		}
		actionMessageBuffer.insert(0, CRLF);
		if (result.getResultCode() == ActionResult.ERROR)
		{
			actionMessageBuffer.insert(0, ctx.getLabel("ActionResult.DataHubTransfer.failed"));
		}
		else
		{
			actionMessageBuffer.insert(0, ctx.getLabel("ActionResult.DataHubTransfer.success"));
		}
		result.setData(actionMessageBuffer.toString());
		return result;
	}

	/**
	 * Returns the name for the itemCode if available otherwise the itemCode.
	 *
	 * @param itemCode
	 *           item code
	 * @return name for the itemCode or the itemCode
	 */
	private String getNameForItemCode(final String itemCode)
	{
		final StringBuffer resourceKeyBuffer = new StringBuffer();
		resourceKeyBuffer.append(ITEM_CODE_TYPE).append(itemCode).append(ITEM_CODE_NAME);
		if (Localization.getLocalizedString(resourceKeyBuffer.toString()).startsWith(ITEM_CODE_TYPE))
		{
			final StringBuffer notFoundBuffer = new StringBuffer();
			notFoundBuffer.append("[").append(itemCode).append("]");
			return notFoundBuffer.toString();
		}
		return Localization.getLocalizedString(resourceKeyBuffer.toString());
	}




	@Override
	public boolean canPerform(final ActionContext<SAPAdministrationModel> ctx)
	{
		return true;
	}

	@Override
	public boolean needsConfirmation(final ActionContext<SAPAdministrationModel> ctx)
	{
		return false;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<SAPAdministrationModel> ctx)
	{
		return ctx.getLabel("confirmation.message");
	}

	/**
	 * @return the sapCoreDataHubInitialLoadHandler
	 */
	public DataHubInitialLoadHandler getSapCoreDataHubInitialLoadHandler()
	{
		return sapCoreDataHubInitialLoadHandler;
	}

	/**
	 * @param sapCoreDataHubInitialLoadHandler
	 *           the sapCoreDataHubInitialLoadHandler to set
	 */
	public void setSapCoreDataHubInitialLoadHandler(final DataHubInitialLoadHandler sapCoreDataHubInitialLoadHandler)
	{
		this.sapCoreDataHubInitialLoadHandler = sapCoreDataHubInitialLoadHandler;
	}





}
