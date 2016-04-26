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
package de.hybris.platform.acceleratorservices.process.strategies;

import de.hybris.platform.commons.model.renderer.RendererTemplateModel;

import java.util.Map;


/**
 * 
 */
public interface EmailTemplateTranslationStrategy
{
	Map<String, Object> translateMessagesForTemplate(final RendererTemplateModel renderTemplate, final String languageIso);
}
