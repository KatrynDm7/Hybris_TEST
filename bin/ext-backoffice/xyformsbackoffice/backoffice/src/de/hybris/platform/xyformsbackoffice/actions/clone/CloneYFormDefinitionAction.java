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
package de.hybris.platform.xyformsbackoffice.actions.clone;

import static de.hybris.platform.xyformsbackoffice.constants.XyformsbackofficeConstants.BUILDER;
import static de.hybris.platform.xyformsbackoffice.constants.XyformsbackofficeConstants.ORBEON;

import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.form.YFormService;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;


/**
 * Action that creates a new copy of an already existing YFormDefinition and pass it to the normal createAction for
 * completing the process.
 */
public class CloneYFormDefinitionAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, String>
{
	public static final Logger LOG = Logger.getLogger(CloneYFormDefinitionAction.class);
	public static final String TYPE_CODE_KEY = "TYPE_CODE";
	public static final String TEMPLATE_KEY = "template";
	public static final String CONTEXT_SOCKET_OUT = "context";

	@Resource(name = "yformService")
	private YFormService yformService;

	@Override
	public ActionResult<String> perform(final ActionContext<Object> ctx)
	{
		if (!(ctx.getData() instanceof YFormDefinitionModel))
		{
			LOG.error("No YFormDefinitionModel object was provided!");
			return new ActionResult<String>(ActionResult.ERROR);
		}
		YFormDefinitionModel template = (YFormDefinitionModel) ctx.getData();

		// We read the same object from Database, so the information is fresh
		try
		{
			template = yformService.getYFormDefinition(template.getApplicationId(), template.getFormId(), template.getVersion());
		}
		catch (final YFormServiceException e)
		{
			NotificationUtils.notifyUser(e.getLocalizedMessage(), NotificationEvent.Type.FAILURE, NotificationEvent.Behavior.TIMED);
			LOG.error(e.getMessage(), e);
			return new ActionResult<String>(ActionResult.ERROR);
		}

		final YFormDefinitionModel yformDefinition = new YFormDefinitionModel();

		// the metadata will be rewritten afterwards as part of the process.
		final String content = template.getContent();
		yformDefinition.setContent(content);

		final String applicationId = template.getApplicationId();
		final String formId = template.getFormId();
		final String title = StringUtils.isEmpty(template.getTitle()) ? ctx.getLabel("untitled.form") : template.getTitle();
		final String description = StringUtils.isEmpty(template.getDescription()) ? "" : template.getDescription();

		final String copyOfApplicationId = ctx.getLabel("copy.of.for.applicationId", new Object[]
		{ applicationId });
		final String copyOfFormId = ctx.getLabel("copy.of.for.formId", new Object[]
		{ formId });
		final String copyOfTitle = ctx.getLabel("copy.of.for.title", new Object[]
		{ title });
		final String copyOfDescription = ctx.getLabel("copy.of.for.description", new Object[]
		{ description });

		yformDefinition.setApplicationId(copyOfApplicationId);
		yformDefinition.setFormId(copyOfFormId);
		yformDefinition.setDescription(copyOfDescription);
		yformDefinition.setTitle(copyOfTitle);

		// we send this object to the normal createAction
		final Map<String, Object> context = new HashMap<String, Object>();

		context.put(TYPE_CODE_KEY, YFormDefinitionModel._TYPECODE);
		context.put(TEMPLATE_KEY, yformDefinition);

		sendOutput(CONTEXT_SOCKET_OUT, context);
		return new ActionResult<String>(ActionResult.SUCCESS);
	}

	@Override
	public boolean canPerform(final ActionContext<Object> ctx)
	{
		if (!(ctx.getData() instanceof YFormDefinitionModel))
		{
			LOG.error("No YFormDefinitionModel object was provided!");
			return false;
		}
		final YFormDefinitionModel yformDefinition = (YFormDefinitionModel) ctx.getData();
		boolean allowed = !StringUtils.isEmpty(yformDefinition.getContent());
		if (!allowed)
		{
			return false;
		}
		// the form builder cannot be cloned, since it is only a placeholder
		allowed = !(ORBEON.equals(yformDefinition.getApplicationId()) && BUILDER.equals(yformDefinition.getFormId()));
		return allowed;
	}

	@Override
	public boolean needsConfirmation(final ActionContext<Object> ctx)
	{
		return false;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<Object> ctx)
	{
		throw new UnsupportedOperationException();
	}
}