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
package de.hybris.platform.core.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractDynamicContent first defined at extension core.
 */
@SuppressWarnings("all")
public class AbstractDynamicContentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AbstractDynamicContent";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractDynamicContent.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractDynamicContent.active</code> attribute defined at extension <code>core</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractDynamicContent.checksum</code> attribute defined at extension <code>core</code>. */
	public static final String CHECKSUM = "checksum";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractDynamicContent.content</code> attribute defined at extension <code>core</code>. */
	public static final String CONTENT = "content";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractDynamicContent.version</code> attribute defined at extension <code>core</code>. */
	public static final String VERSION = "version";
	
	
	/** <i>Generated variable</i> - Variable of <code>AbstractDynamicContent.code</code> attribute defined at extension <code>core</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractDynamicContent.active</code> attribute defined at extension <code>core</code>. */
	private Boolean _active;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractDynamicContent.checksum</code> attribute defined at extension <code>core</code>. */
	private String _checksum;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractDynamicContent.content</code> attribute defined at extension <code>core</code>. */
	private String _content;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractDynamicContent.version</code> attribute defined at extension <code>core</code>. */
	private Long _version;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractDynamicContentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractDynamicContentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _content initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractDynamicContentModel(final String _code, final String _content)
	{
		super();
		setCode(_code);
		setContent(_content);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _content initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractDynamicContentModel(final String _code, final String _content, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setContent(_content);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDynamicContent.active</code> attribute defined at extension <code>core</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		if (this._active!=null)
		{
			return _active;
		}
		return _active = getPersistenceContext().getValue(ACTIVE, _active);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDynamicContent.checksum</code> attribute defined at extension <code>core</code>. 
	 * @return the checksum
	 */
	@Accessor(qualifier = "checksum", type = Accessor.Type.GETTER)
	public String getChecksum()
	{
		if (this._checksum!=null)
		{
			return _checksum;
		}
		return _checksum = getPersistenceContext().getValue(CHECKSUM, _checksum);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDynamicContent.code</code> attribute defined at extension <code>core</code>. 
	 * @return the code
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
	 * <i>Generated method</i> - Getter of the <code>AbstractDynamicContent.content</code> attribute defined at extension <code>core</code>. 
	 * @return the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public String getContent()
	{
		if (this._content!=null)
		{
			return _content;
		}
		return _content = getPersistenceContext().getValue(CONTENT, _content);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDynamicContent.version</code> attribute defined at extension <code>core</code>. 
	 * @return the version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public Long getVersion()
	{
		if (this._version!=null)
		{
			return _version;
		}
		return _version = getPersistenceContext().getValue(VERSION, _version);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractDynamicContent.active</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		_active = getPersistenceContext().setValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractDynamicContent.checksum</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the checksum
	 */
	@Accessor(qualifier = "checksum", type = Accessor.Type.SETTER)
	public void setChecksum(final String value)
	{
		_checksum = getPersistenceContext().setValue(CHECKSUM, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractDynamicContent.code</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractDynamicContent.content</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final String value)
	{
		_content = getPersistenceContext().setValue(CONTENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractDynamicContent.version</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final Long value)
	{
		_version = getPersistenceContext().setValue(VERSION, value);
	}
	
}
