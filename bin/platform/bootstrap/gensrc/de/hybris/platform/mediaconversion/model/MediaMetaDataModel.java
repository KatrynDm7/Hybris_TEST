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
package de.hybris.platform.mediaconversion.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MediaMetaData first defined at extension mediaconversion.
 */
@SuppressWarnings("all")
public class MediaMetaDataModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "MediaMetaData";
	
	/**<i>Generated relation code constant for relation <code>MediaToMediaMetaDataRel</code> defining source attribute <code>media</code> in extension <code>mediaconversion</code>.</i>*/
	public final static String _MEDIATOMEDIAMETADATAREL = "MediaToMediaMetaDataRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaMetaData.code</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaMetaData.value</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaMetaData.groupName</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String GROUPNAME = "groupName";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaMetaData.media</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String MEDIA = "media";
	
	
	/** <i>Generated variable</i> - Variable of <code>MediaMetaData.code</code> attribute defined at extension <code>mediaconversion</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>MediaMetaData.value</code> attribute defined at extension <code>mediaconversion</code>. */
	private String _value;
	
	/** <i>Generated variable</i> - Variable of <code>MediaMetaData.groupName</code> attribute defined at extension <code>mediaconversion</code>. */
	private String _groupName;
	
	/** <i>Generated variable</i> - Variable of <code>MediaMetaData.media</code> attribute defined at extension <code>mediaconversion</code>. */
	private MediaModel _media;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MediaMetaDataModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MediaMetaDataModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>MediaMetaData</code> at extension <code>mediaconversion</code>
	 * @param _groupName initial attribute declared by type <code>MediaMetaData</code> at extension <code>mediaconversion</code>
	 * @param _media initial attribute declared by type <code>MediaMetaData</code> at extension <code>mediaconversion</code>
	 */
	@Deprecated
	public MediaMetaDataModel(final String _code, final String _groupName, final MediaModel _media)
	{
		super();
		setCode(_code);
		setGroupName(_groupName);
		setMedia(_media);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>MediaMetaData</code> at extension <code>mediaconversion</code>
	 * @param _groupName initial attribute declared by type <code>MediaMetaData</code> at extension <code>mediaconversion</code>
	 * @param _media initial attribute declared by type <code>MediaMetaData</code> at extension <code>mediaconversion</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _value initial attribute declared by type <code>MediaMetaData</code> at extension <code>mediaconversion</code>
	 */
	@Deprecated
	public MediaMetaDataModel(final String _code, final String _groupName, final MediaModel _media, final ItemModel _owner, final String _value)
	{
		super();
		setCode(_code);
		setGroupName(_groupName);
		setMedia(_media);
		setOwner(_owner);
		setValue(_value);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaMetaData.code</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the code - Identifier of this meta data entry. 
	 * 		              Note: this identifier is not unique, it is only unique in combination with the metaDataProvider.
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
	 * <i>Generated method</i> - Getter of the <code>MediaMetaData.groupName</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the groupName - A short display name of the provider handling this meta data.
	 */
	@Accessor(qualifier = "groupName", type = Accessor.Type.GETTER)
	public String getGroupName()
	{
		if (this._groupName!=null)
		{
			return _groupName;
		}
		return _groupName = getPersistenceContext().getValue(GROUPNAME, _groupName);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaMetaData.media</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the media - The media the meta data refers to.
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
	 * <i>Generated method</i> - Getter of the <code>MediaMetaData.value</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the value - Actual value of the meta data.
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
	 * <i>Generated method</i> - Initial setter of <code>MediaMetaData.code</code> attribute defined at extension <code>mediaconversion</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - Identifier of this meta data entry. 
	 * 		              Note: this identifier is not unique, it is only unique in combination with the metaDataProvider.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MediaMetaData.groupName</code> attribute defined at extension <code>mediaconversion</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the groupName - A short display name of the provider handling this meta data.
	 */
	@Accessor(qualifier = "groupName", type = Accessor.Type.SETTER)
	public void setGroupName(final String value)
	{
		_groupName = getPersistenceContext().setValue(GROUPNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MediaMetaData.media</code> attribute defined at extension <code>mediaconversion</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the media - The media the meta data refers to.
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.SETTER)
	public void setMedia(final MediaModel value)
	{
		_media = getPersistenceContext().setValue(MEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MediaMetaData.value</code> attribute defined at extension <code>mediaconversion</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the value - Actual value of the meta data.
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final String value)
	{
		_value = getPersistenceContext().setValue(VALUE, value);
	}
	
}
