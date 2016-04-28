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
import de.hybris.platform.commons.model.translator.JaloVelocityRendererModel;
import de.hybris.platform.commons.model.translator.ParserPropertyModel;
import de.hybris.platform.commons.model.translator.RenderersPropertyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type JaloTranslatorConfiguration first defined at extension commons.
 */
@SuppressWarnings("all")
public class JaloTranslatorConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "JaloTranslatorConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloTranslatorConfiguration.code</code> attribute defined at extension <code>commons</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloTranslatorConfiguration.renderers</code> attribute defined at extension <code>commons</code>. */
	public static final String RENDERERS = "renderers";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloTranslatorConfiguration.renderersProperties</code> attribute defined at extension <code>commons</code>. */
	public static final String RENDERERSPROPERTIES = "renderersProperties";
	
	/** <i>Generated constant</i> - Attribute key of <code>JaloTranslatorConfiguration.parserProperties</code> attribute defined at extension <code>commons</code>. */
	public static final String PARSERPROPERTIES = "parserProperties";
	
	
	/** <i>Generated variable</i> - Variable of <code>JaloTranslatorConfiguration.code</code> attribute defined at extension <code>commons</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>JaloTranslatorConfiguration.renderers</code> attribute defined at extension <code>commons</code>. */
	private List<JaloVelocityRendererModel> _renderers;
	
	/** <i>Generated variable</i> - Variable of <code>JaloTranslatorConfiguration.renderersProperties</code> attribute defined at extension <code>commons</code>. */
	private List<RenderersPropertyModel> _renderersProperties;
	
	/** <i>Generated variable</i> - Variable of <code>JaloTranslatorConfiguration.parserProperties</code> attribute defined at extension <code>commons</code>. */
	private List<ParserPropertyModel> _parserProperties;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public JaloTranslatorConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public JaloTranslatorConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>JaloTranslatorConfiguration</code> at extension <code>commons</code>
	 */
	@Deprecated
	public JaloTranslatorConfigurationModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>JaloTranslatorConfiguration</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public JaloTranslatorConfigurationModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloTranslatorConfiguration.code</code> attribute defined at extension <code>commons</code>. 
	 * @return the code - unique item identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		if (this._code!=null)
		{
			return _code;
		}
		return _code = getPersistenceContext().getValue(CODE, _code);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloTranslatorConfiguration.parserProperties</code> attribute defined at extension <code>commons</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the parserProperties
	 */
	@Accessor(qualifier = "parserProperties", type = Accessor.Type.GETTER)
	public List<ParserPropertyModel> getParserProperties()
	{
		if (this._parserProperties!=null)
		{
			return _parserProperties;
		}
		return _parserProperties = getPersistenceContext().getValue(PARSERPROPERTIES, _parserProperties);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloTranslatorConfiguration.renderers</code> attribute defined at extension <code>commons</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the renderers
	 */
	@Accessor(qualifier = "renderers", type = Accessor.Type.GETTER)
	public List<JaloVelocityRendererModel> getRenderers()
	{
		if (this._renderers!=null)
		{
			return _renderers;
		}
		return _renderers = getPersistenceContext().getValue(RENDERERS, _renderers);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JaloTranslatorConfiguration.renderersProperties</code> attribute defined at extension <code>commons</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the renderersProperties
	 */
	@Accessor(qualifier = "renderersProperties", type = Accessor.Type.GETTER)
	public List<RenderersPropertyModel> getRenderersProperties()
	{
		if (this._renderersProperties!=null)
		{
			return _renderersProperties;
		}
		return _renderersProperties = getPersistenceContext().getValue(RENDERERSPROPERTIES, _renderersProperties);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloTranslatorConfiguration.code</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the code - unique item identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloTranslatorConfiguration.parserProperties</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the parserProperties
	 */
	@Accessor(qualifier = "parserProperties", type = Accessor.Type.SETTER)
	public void setParserProperties(final List<ParserPropertyModel> value)
	{
		_parserProperties = getPersistenceContext().setValue(PARSERPROPERTIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloTranslatorConfiguration.renderers</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the renderers
	 */
	@Accessor(qualifier = "renderers", type = Accessor.Type.SETTER)
	public void setRenderers(final List<JaloVelocityRendererModel> value)
	{
		_renderers = getPersistenceContext().setValue(RENDERERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JaloTranslatorConfiguration.renderersProperties</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the renderersProperties
	 */
	@Accessor(qualifier = "renderersProperties", type = Accessor.Type.SETTER)
	public void setRenderersProperties(final List<RenderersPropertyModel> value)
	{
		_renderersProperties = getPersistenceContext().setValue(RENDERERSPROPERTIES, value);
	}
	
}
