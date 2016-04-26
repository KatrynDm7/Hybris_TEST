/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.commons.renderer;

import de.hybris.platform.commons.model.renderer.RendererTemplateModel;

import java.io.Writer;


/**
 * Renders template using context informations
 */
public interface Renderer
{
	/**
	 * Renders template. Rendered content is stored into output.
	 * 
	 * @param template
	 *           to render
	 * 
	 * @param context
	 *           context (usually map of properties)
	 * 
	 * @param output
	 *           rendered content
	 * 
	 * @throws de.hybris.platform.commons.renderer.exceptions.RendererException
	 *            if any problem during rendering occurrs
	 */
	public void render(RendererTemplateModel template, Object context, Writer output);
}
