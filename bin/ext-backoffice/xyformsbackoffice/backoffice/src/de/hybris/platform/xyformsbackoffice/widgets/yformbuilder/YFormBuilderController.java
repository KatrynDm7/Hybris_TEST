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
package de.hybris.platform.xyformsbackoffice.widgets.yformbuilder;

import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.form.YFormService;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.net.MalformedURLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.URIEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Window;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;


/**
 * Displays Form Builder inside containing widget
 */
public class YFormBuilderController extends DefaultWidgetController
{
	private static final Logger LOG = Logger.getLogger(YFormBuilderController.class);

	protected static final String ORBEON_APPLICATION_ID = "orbeon";
	protected static final String ORBEON_FORM_BUILDER_ID = "builder";

	protected static final String ORBEON_BUILDER_EDIT_ADDRESS = "/orbeon/fr/orbeon/builder/edit/";

	protected static final String YFORM_DEFINITION_SOCKET_IN = "yformDefinition";
	protected static final String YFORM_DEFINITION_SOCKET_OUT = "yformDefinition";

	private Div div;

	@Resource(name = "yformService")
	private YFormService yformService;

	@Override
	public void initialize(final Component component)
	{
		super.initialize(component);

		div = (Div) component;
	}

	/**
	 * Gets the current widget's Window.
	 * 
	 * @param component
	 */
	protected Window getWindow(final Component component)
	{
		// TODO: improve this... there should be a nicer way to look for the containing Widget Window
		Component c = component;
		while (c.getParent() != null)
		{
			if (c instanceof Window)
			{
				return (Window) c;
			}
			c = c.getParent();
		}
		return null;
	}

	/**
	 * Displays the Window that contains the Form Builder.
	 * 
	 * @param yformDefinition
	 * @throws MalformedURLException
	 */
	@SocketEvent(socketId = YFORM_DEFINITION_SOCKET_IN)
	public void show(final YFormDefinitionModel yformDefinition) throws MalformedURLException
	{
		div.setWidth("100%");
		div.setHeight("100%");
		div.setStyle("position: relative");

		final Window window = this.getWindow(div);
		if (window != null)
		{
			window.setWidth(getWidgetSettings().getString("width"));
			window.setHeight(getWidgetSettings().getString("height"));
			window.setBorder("normal");
			window.setSizable(getWidgetSettings().getBoolean("sizable"));
			window.setClosable(getWidgetSettings().getBoolean("closable"));
			window.setDraggable(getWidgetSettings().getString("draggable"));
			window.setMaximizable(true);
		}
		else
		{
			LOG.error("There is no window associated to the current widget.");
			return;
		}

		window.addEventListener(Events.ON_CLOSE, new EventListener<Event>()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				// we show always the latest formDefinition
				try
				{
					final YFormDefinitionModel yform = yformService.getYFormDefinition(yformDefinition.getApplicationId(),
							yformDefinition.getFormId());
					sendOutput(YFORM_DEFINITION_SOCKET_OUT, yform);
				}
				catch (final YFormServiceException e)
				{
					NotificationUtils.notifyUser(e.getLocalizedMessage(), NotificationEvent.Type.FAILURE,
							NotificationEvent.Behavior.TIMED);
					LOG.error(e, e);
				}
			}
		});

		final Iframe iframe = new Iframe();
		iframe.setWidth("100%");
		iframe.setHeight("100%");
		iframe.setScrolling("no");
		iframe.setStyle("overflow: hidden");

		iframe.setSrc(this.getFormBuilderURL(yformDefinition));

		div.appendChild(iframe);

		iframe.addEventListener("onURIChange", new EventListener<URIEvent>()
		{
			@Override
			public void onEvent(final URIEvent arg0) throws Exception
			{
				// User is not allowed to change provided URI, for security reasons.
				Messagebox.show(getLabel("change.url.not.allowed"), getLabel("title.error"), new Messagebox.Button[]
				{ Messagebox.Button.OK }, Messagebox.ERROR, new EventListener<ClickEvent>()
				{
					@Override
					public void onEvent(final ClickEvent arg0) throws Exception
					{
						window.onClose();
					}
				});
			}
		});
	}

	/**
	 * Creates the URL to call Orbeon Form Builder.
	 * 
	 * @param yformDefinition
	 */
	protected String getFormBuilderURL(final YFormDefinitionModel yformDefinition)
	{
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		String url = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		url = url.substring(0, url.indexOf(contextPath)); // it shouldn't contain the "/"

		// this is to prevent a previous filter that could modify the current contextPath.
		contextPath = contextPath.indexOf("/", 1) > 0 ? contextPath.substring(0, contextPath.indexOf("/", 1)) : contextPath;
		url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;

		url = url + contextPath + ORBEON_BUILDER_EDIT_ADDRESS + yformDefinition.getDocumentId();
		LOG.debug(url);
		return url;
	}
}
