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
package de.hybris.platform.xyformsbackoffice.widgets.createyformdefinitioncounterpart;

import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.form.YFormService;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;


/**
 * Creates a yForm Definition counterpart
 */
public class CreateYFormDefinitionCounterpartController extends DefaultWidgetController
{
	private static final Logger LOG = Logger.getLogger(CreateYFormDefinitionCounterpartController.class);
	private static final String YFORM_DEFINITION_SOCKET_IN = "yformDefinition";
	private static final String WIZARD_RESULT_SOCKET_IN = "wizardResult";
	private static final String WIZARD_RESULT_PROPERTY = "newYFormDefinition";
	private static final String YFORM_DEFINITION_SOCKET_OUT = "yformDefinition";

	@WireVariable(value = "yFormFacade")
	private transient YFormFacade yformFacade;

	@WireVariable(value = "yformService")
	private transient YFormService yformService;

	/**
	 * Creates a yForm Data counterpart for the given yForm Definition.
	 * 
	 * @param object
	 */
	@SocketEvent(socketId = YFORM_DEFINITION_SOCKET_IN)
	public void execute(final Object object)
	{
		if (!(object instanceof YFormDefinitionModel))
		{
			LOG.error("Object is not a YFormDefinition");
			return;
		}
		final YFormDefinitionModel yformDefinition = (YFormDefinitionModel) object;

		final String applicationId = yformDefinition.getApplicationId();
		final String formId = yformDefinition.getFormId();
		final int version = yformDefinition.getVersion();

		LOG.debug("Creating/updating form counterpart...[" + applicationId + "][" + formId + "][" + version + "]");

		try
		{
			yformFacade.recreateYFormDefinitionCounterpart(applicationId, formId, version);

			sendOutput(YFORM_DEFINITION_SOCKET_OUT, yformDefinition);
		}
		catch (final YFormServiceException e)
		{
			NotificationUtils.notifyUser(e.getLocalizedMessage(), NotificationEvent.Type.FAILURE, NotificationEvent.Behavior.TIMED);
			LOG.error(e, e);
		}
	}

	/**
	 * Used when connecting connectionFlow widget to this one. When a new yForm Definition is created its yForm Data
	 * counterpart must also created.
	 * 
	 * @param wizardResult
	 */
	@SocketEvent(socketId = WIZARD_RESULT_SOCKET_IN)
	public void executeAfterWizard(final Object wizardResult)
	{
		if (!(wizardResult instanceof Map))
		{
			LOG.error("Object is not a Map");
			return;
		}

		final Map<String, Object> map = (Map<String, Object>) wizardResult;
		final YFormDefinitionModel yformDefinition = (YFormDefinitionModel) map.get(WIZARD_RESULT_PROPERTY);

		// if the given property is not part of the wizardResult, we are not interested
		if (yformDefinition != null)
		{
			execute(yformDefinition);
		}
	}
}
