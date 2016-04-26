/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 18.04.2016 18:26:54                         ---
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
 * Generated model class for type RenderersProperty first defined at extension commons.
 */
@SuppressWarnings("all")
public class RenderersPropertyModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "RenderersProperty";
	
	/**<i>Generated relation code constant for relation <code>TranslatorConfig2RenderProperties</code> defining source attribute <code>translatorConfiguration</code> in extension <code>commons</code>.</i>*/
	public final static String _TRANSLATORCONFIG2RENDERPROPERTIES = "TranslatorConfig2RenderProperties";
	
	/** <i>Generated constant</i> - Attribute key of <code>RenderersProperty.key</code> attribute defined at extension <code>commons</code>. */
	public static final String KEY = "key";
	
	/** <i>Generated constant</i> - Attribute key of <code>RenderersProperty.value</code> attribute defined at extension <code>commons</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>RenderersProperty.translatorConfiguration</code> attribute defined at extension <code>commons</code>. */
	public static final String TRANSLATORCONFIGURATION = "translatorConfiguration";
	
	
	/** <i>Generated variable</i> - Variable of <code>RenderersProperty.key</code> attribute defined at extension <code>commons</code>. */
	private String _key;
	
	/** <i>Generated variable</i> - Variable of <code>RenderersProperty.value</code> attribute defined at extension <code>commons</code>. */
	private String _value;
	
	/** <i>Generated variable</i> - Variable of <code>RenderersProperty.translatorConfiguration</code> attribute defined at extension <code>commons</code>. */
	private JaloTranslatorConfigurationModel _translatorConfiguration;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RenderersPropertyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RenderersPropertyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RenderersPropertyModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RenderersProperty.key</code> attribute defined at extension <code>commons</code>. 
	 * @return the key - key
	 */
	@Accessor(qualifier = "key", type = Accessor.Type.GETTER)
	public String getKey()
	{
		if (this._key!=null)
		{
			return _key;
		}
		return _key = getPersistenceContext().getValue(KEY, _key);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RenderersProperty.translatorConfiguration</code> attribute defined at extension <code>commons</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>RenderersProperty.value</code> attribute defined at extension <code>commons</code>. 
	 * @return the value - value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public String getValue()
	{
		if (this._value!=null)
		{
			return _value;
		}
		return _value = getPersistenceContext().getValue(VALUE, _value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RenderersProperty.key</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the key - key
	 */
	@Accessor(qualifier = "key", type = Accessor.Type.SETTER)
	public void setKey(final String value)
	{
		_key = getPersistenceContext().setValue(KEY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RenderersProperty.translatorConfiguration</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the translatorConfiguration
	 */
	@Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.SETTER)
	public void setTranslatorConfiguration(final JaloTranslatorConfigurationModel value)
	{
		_translatorConfiguration = getPersistenceContext().setValue(TRANSLATORCONFIGURATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RenderersProperty.value</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the value - value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final String value)
	{
		_value = getPersistenceContext().setValue(VALUE, value);
	}
	
}
