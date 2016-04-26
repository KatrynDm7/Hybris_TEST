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
 */
package de.hybris.platform.xyformsbackoffice.actions.archive;

import de.hybris.platform.xyformsservices.enums.YFormDefinitionStatusEnum;
import de.hybris.platform.xyformsservices.form.YFormService;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.BackofficeSpringUtil;



/**
 * Actions that disables a yForm definition
 */
public class ArchiveYFormDefinitionAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, String>
{
	private static final Logger LOG = Logger.getLogger(ArchiveYFormDefinitionAction.class);
	public static final String YFORM_DEFINITION_SOCKET_OUT = "yformDefinition";

	@Resource(name = "yformService")
	private YFormService yformService;

	private PermissionFacade permissionFacade;


	@Override
	public ActionResult<String> perform(final ActionContext<Object> ctx)
	{
		try
		{
			final YFormDefinitionModel yformDefinition = (YFormDefinitionModel) ctx.getData();
			final String applicationId = yformDefinition.getApplicationId();
			final String formId = yformDefinition.getFormId();
			if (YFormDefinitionStatusEnum.DISABLED.equals(yformDefinition.getStatus()))
			{
				yformService.setFormDefinitionStatus(applicationId, formId, YFormDefinitionStatusEnum.ENABLED);

				NotificationUtils.notifyUser(ctx.getLabel("enable.success"), NotificationEvent.Type.SUCCESS,
						NotificationEvent.Behavior.TIMED);
			}
			else
			{
				yformService.setFormDefinitionStatus(applicationId, formId, YFormDefinitionStatusEnum.DISABLED);

				NotificationUtils.notifyUser(ctx.getLabel("disable.success"), NotificationEvent.Type.SUCCESS,
						NotificationEvent.Behavior.TIMED);
			}

			sendOutput(YFORM_DEFINITION_SOCKET_OUT, yformDefinition);

			return new ActionResult<String>(ActionResult.SUCCESS);
		}
		catch (final Exception e)
		{
			NotificationUtils.notifyUser(e.getLocalizedMessage(), NotificationEvent.Type.FAILURE, NotificationEvent.Behavior.TIMED);
			LOG.error(e.getMessage(), e);
			return new ActionResult<String>(ActionResult.ERROR);
		}
	}

	@Override
	public boolean canPerform(final ActionContext<Object> ctx)
	{

		boolean allowed = false;

		final PermissionFacade permissionFacade = getPermissionFacade();
		if (permissionFacade != null)
		{
			if (ctx.getData() instanceof YFormDefinitionModel)
			{
				allowed = permissionFacade.canChangeInstance(ctx.getData());
			}
		}

		return allowed;
	}

	@Override
	public boolean needsConfirmation(final ActionContext<Object> ctx)
	{
		return true;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<Object> ctx)
	{
		final YFormDefinitionModel yformDefinition = (YFormDefinitionModel) ctx.getData();
		if (YFormDefinitionStatusEnum.DISABLED.equals(yformDefinition.getStatus()))
		{
			return ctx.getLabel("enable.confirm");
		}
		else
		{
			return ctx.getLabel("disable.confirm");
		}
	}


	protected PermissionFacade getPermissionFacade()
	{
		if (permissionFacade == null)
		{
			permissionFacade = (PermissionFacade) BackofficeSpringUtil.getBean("permissionFacade", PermissionFacade.class);
		}
		return permissionFacade;
	}

	public void setPermissionFacade(final PermissionFacade permissionFacade)
	{
		this.permissionFacade = permissionFacade;
	}
}