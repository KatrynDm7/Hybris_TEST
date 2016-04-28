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
package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CockpitUIComponentConfiguration first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitUIComponentConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CockpitUIComponentConfiguration";
	
	/**<i>Generated relation code constant for relation <code>Principal2CockpitUIComponentConfigurationRelation</code> defining source attribute <code>principal</code> in extension <code>cockpit</code>.</i>*/
	public final static String _PRINCIPAL2COCKPITUICOMPONENTCONFIGURATIONRELATION = "Principal2CockpitUIComponentConfigurationRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentConfiguration.factoryBean</code> attribute defined at extension <code>cockpit</code>. */
	public static final String FACTORYBEAN = "factoryBean";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentConfiguration.code</code> attribute defined at extension <code>cockpit</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentConfiguration.objectTemplateCode</code> attribute defined at extension <code>cockpit</code>. */
	public static final String OBJECTTEMPLATECODE = "objectTemplateCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentConfiguration.media</code> attribute defined at extension <code>cockpit</code>. */
	public static final String MEDIA = "media";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentConfiguration.principal</code> attribute defined at extension <code>cockpit</code>. */
	public static final String PRINCIPAL = "principal";
	
	
	/** <i>Generated variable</i> - Variable of <code>CockpitUIComponentConfiguration.factoryBean</code> attribute defined at extension <code>cockpit</code>. */
	private String _factoryBean;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitUIComponentConfiguration.code</code> attribute defined at extension <code>cockpit</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitUIComponentConfiguration.objectTemplateCode</code> attribute defined at extension <code>cockpit</code>. */
	private String _objectTemplateCode;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitUIComponentConfiguration.media</code> attribute defined at extension <code>cockpit</code>. */
	private MediaModel _media;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitUIComponentConfiguration.principal</code> attribute defined at extension <code>cockpit</code>. */
	private PrincipalModel _principal;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitUIComponentConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitUIComponentConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _factoryBean initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _media initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _objectTemplateCode initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitUIComponentConfigurationModel(final String _code, final String _factoryBean, final MediaModel _media, final String _objectTemplateCode)
	{
		super();
		setCode(_code);
		setFactoryBean(_factoryBean);
		setMedia(_media);
		setObjectTemplateCode(_objectTemplateCode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _factoryBean initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _media initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _objectTemplateCode initial attribute declared by type <code>CockpitUIComponentConfiguration</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CockpitUIComponentConfigurationModel(final String _code, final String _factoryBean, final MediaModel _media, final String _objectTemplateCode, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setFactoryBean(_factoryBean);
		setMedia(_media);
		setObjectTemplateCode(_objectTemplateCode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentConfiguration.code</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the code - Code of configuration
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
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentConfiguration.factoryBean</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the factoryBean - ID of Spring bean that implements de.hybris.platform.cockpit.services.config.UIComponentConfigurationFactory
	 */
	@Accessor(qualifier = "factoryBean", type = Accessor.Type.GETTER)
	public String getFactoryBean()
	{
		if (this._factoryBean!=null)
		{
			return _factoryBean;
		}
		return _factoryBean = getPersistenceContext().getValue(FACTORYBEAN, _factoryBean);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentConfiguration.media</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the media
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.GETTER)
	public MediaModel getMedia()
	{
		if (this._media!=null)
		{
			return _media;
		}
		return _media = getPersistenceContext().getValue(MEDIA, _media);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentConfiguration.objectTemplateCode</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the objectTemplateCode - Code of Object Template this UIConfiguration is associated with
	 */
	@Accessor(qualifier = "objectTemplateCode", type = Accessor.Type.GETTER)
	public String getObjectTemplateCode()
	{
		if (this._objectTemplateCode!=null)
		{
			return _objectTemplateCode;
		}
		return _objectTemplateCode = getPersistenceContext().getValue(OBJECTTEMPLATECODE, _objectTemplateCode);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentConfiguration.principal</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the principal
	 */
	@Accessor(qualifier = "principal", type = Accessor.Type.GETTER)
	public PrincipalModel getPrincipal()
	{
		if (this._principal!=null)
		{
			return _principal;
		}
		return _principal = getPersistenceContext().getValue(PRINCIPAL, _principal);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentConfiguration.code</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the code - Code of configuration
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentConfiguration.factoryBean</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the factoryBean - ID of Spring bean that implements de.hybris.platform.cockpit.services.config.UIComponentConfigurationFactory
	 */
	@Accessor(qualifier = "factoryBean", type = Accessor.Type.SETTER)
	public void setFactoryBean(final String value)
	{
		_factoryBean = getPersistenceContext().setValue(FACTORYBEAN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentConfiguration.media</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the media
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.SETTER)
	public void setMedia(final MediaModel value)
	{
		_media = getPersistenceContext().setValue(MEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentConfiguration.objectTemplateCode</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the objectTemplateCode - Code of Object Template this UIConfiguration is associated with
	 */
	@Accessor(qualifier = "objectTemplateCode", type = Accessor.Type.SETTER)
	public void setObjectTemplateCode(final String value)
	{
		_objectTemplateCode = getPersistenceContext().setValue(OBJECTTEMPLATECODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentConfiguration.principal</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the principal
	 */
	@Accessor(qualifier = "principal", type = Accessor.Type.SETTER)
	public void setPrincipal(final PrincipalModel value)
	{
		_principal = getPersistenceContext().setValue(PRINCIPAL, value);
	}
	
}
