/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 28.04.2016 16:51:49                         ---
 * ----------------------------------------------------------------
 *  
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
package de.hybris.platform.commons.model.translator;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.model.translator.JaloTranslatorConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type JaloVelocityRenderer first defined at extension commons.
 */
@SuppressWarnings("all")
public class JaloVelocityRendererModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "JaloVelocityRenderer";
	
	/**<i>Generated relation code constant for relation <code>TranslatorConfig2Renderers</code> defining source attribute <code>translatorConfiguration</code> in extension <code>commons</code>.</i>*/
	public final static String _TRANSLATORCONFIG2RENDERERS = "TranslatorConfig2Renderers";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloVelocityRenderer.name</code> attribute defined at extension <code>commons</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloVelocityRenderer.template</code> attribute defined at extension <code>commons</code>. */
	public static final String TEMPLATE = "template";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloVelocityRenderer.translatorConfiguration</code> attribute defined at extension <code>commons</code>. */
	public static final String TRANSLATORCONFIGURATION = "translatorConfiguration";
	
	
	/** <i>Generated variable</i> - Variable of <code>JaloVelocityRenderer.name</code> attribute defined at extension <code>commons</code>. */
	private String _name;
	
	/** <i>Generated variable</i> - Variable of <code>JaloVelocityRenderer.template</code> attribute defined at extension <code>commons</code>. */
	private String _template;
	
	/** <i>Generated variable</i> - Variable of <code>JaloVelocityRenderer.translatorConfiguration</code> attribute defined at extension <code>commons</code>. */
	private JaloTranslatorConfigurationModel _translatorConfiguration;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public JaloVelocityRendererModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public JaloVelocityRendererModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>JaloVelocityRenderer</code> at extension <code>commons</code>
	 */
	@Deprecated
	public JaloVelocityRendererModel(final String _name)
	{
		super();
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>JaloVelocityRenderer</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public JaloVelocityRendererModel(final String _name, final ItemModel _owner)
	{
		super();
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloVelocityRenderer.name</code> attribute defined at extension <code>commons</code>. 
	 * @return the name - renderer name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		if (this._name!=null)
		{
			return _name;
		}
		return _name = getPersistenceContext().getValue(NAME, _name);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloVelocityRenderer.template</code> attribute defined at extension <code>commons</code>. 
	 * @return the template - velocity template
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.GETTER)
	public String getTemplate()
	{
		if (this._template!=null)
		{
			return _template;
		}
		return _template = getPersistenceContext().getValue(TEMPLATE, _template);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloVelocityRenderer.translatorConfiguration</code> attribute defined at extension <code>commons</code>. 
	 * @return the translatorConfiguration
	 */
	@Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.GETTER)
	public JaloTranslatorConfigurationModel getTranslatorConfiguration()
	{
		if (this._translatorConfiguration!=null)
		{
			return _translatorConfiguration;
		}
		return _translatorConfiguration = getPersistenceContext().getValue(TRANSLATORCONFIGURATION, _translatorConfiguration);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloVelocityRenderer.name</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the name - renderer name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		_name = getPersistenceContext().setValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloVelocityRenderer.template</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the template - velocity template
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.SETTER)
	public void setTemplate(final String value)
	{
		_template = getPersistenceContext().setValue(TEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloVelocityRenderer.translatorConfiguration</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the translatorConfiguration
	 */
	@Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.SETTER)
	public void setTranslatorConfiguration(final JaloTranslatorConfigurationModel value)
	{
		_translatorConfiguration = getPersistenceContext().setValue(TRANSLATORCONFIGURATION, value);
	}
	
}
