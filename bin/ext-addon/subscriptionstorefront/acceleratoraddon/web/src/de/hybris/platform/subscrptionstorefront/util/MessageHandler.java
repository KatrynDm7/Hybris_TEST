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
package de.hybris.platform.subscrptionstorefront.util;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;

/**
 *  Utility class checking if there is a message in threadScopedMessages and supplying model with corresponding messages. 
 */
public class MessageHandler {
	@Autowired
	public ApplicationContext applicationContext;
	
	private static final Logger LOG = Logger.getLogger(MessageHandler.class);
	
	public void supplementModelWithMessages(@Nonnull final Model model) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> threadScopedMessages = (Map<String, String>)applicationContext.getBean("threadScopedMessages");
			String msg = threadScopedMessages.get("ServiceRequestExceptionMessage");
			if (msg != null && msg.contains("The account id is invalid")) {
				GlobalMessages.addErrorMessage(model,  "The account does not exist in SBG. Please create a new account.");
				threadScopedMessages.remove("ServiceRequestExceptionMessage");
			}
		} catch (Exception ex) {
			LOG.info("No bean with name sessionScopedMessages - no messages from it.");
		}
	}
}
