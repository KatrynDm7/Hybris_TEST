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
package de.hybris.platform.core.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContextModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MediaFormatMapping first defined at extension core.
 */
@SuppressWarnings("all")
public class MediaFormatMappingModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "MediaFormatMapping";
	
	/**<i>Generated relation code constant for relation <code>MediaContext2MediaFormatMappingRel</code> defining source attribute <code>mediaContext</code> in extension <code>core</code>.</i>*/
	public final static String _MEDIACONTEXT2MEDIAFORMATMAPPINGREL = "MediaContext2MediaFormatMappingRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaFormatMapping.source</code> attribute defined at extension <code>core</code>. */
	public static final String SOURCE = "source";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaFormatMapping.target</code> attribute defined at extension <code>core</code>. */
	public static final String TARGET = "target";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaFormatMapping.mediaContext</code> attribute defined at extension <code>core</code>. */
	public static final String MEDIACONTEXT = "mediaContext";
	
	
	/** <i>Generated variable</i> - Variable of <code>MediaFormatMapping.source</code> attribute defined at extension <code>core</code>. */
	private MediaFormatModel _source;
	
	/** <i>Generated variable</i> - Variable of <code>MediaFormatMapping.target</code> attribute defined at extension <code>core</code>. */
	private MediaFormatModel _target;
	
	/** <i>Generated variable</i> - Variable of <code>MediaFormatMapping.mediaContext</code> attribute defined at extension <code>core</code>. */
	private MediaContextModel _mediaContext;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MediaFormatMappingModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MediaFormatMappingModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _mediaContext initial attribute declared by type <code>MediaFormatMapping</code> at extension <code>core</code>
	 * @param _source initial attribute declared by type <code>MediaFormatMapping</code> at extension <code>core</code>
	 * @param _target initial attribute declared by type <code>MediaFormatMapping</code> at extension <code>core</code>
	 */
	@Deprecated
	public MediaFormatMappingModel(final MediaContextModel _mediaContext, final MediaFormatModel _source, final MediaFormatModel _target)
	{
		super();
		setMediaContext(_mediaContext);
		setSource(_source);
		setTarget(_target);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _mediaContext initial attribute declared by type <code>MediaFormatMapping</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _source initial attribute declared by type <code>MediaFormatMapping</code> at extension <code>core</code>
	 * @param _target initial attribute declared by type <code>MediaFormatMapping</code> at extension <code>core</code>
	 */
	@Deprecated
	public MediaFormatMappingModel(final MediaContextModel _mediaContext, final ItemModel _owner, final MediaFormatModel _source, final MediaFormatModel _target)
	{
		super();
		setMediaContext(_mediaContext);
		setOwner(_owner);
		setSource(_source);
		setTarget(_target);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaFormatMapping.mediaContext</code> attribute defined at extension <code>core</code>. 
	 * @return the mediaContext
	 */
	@Accessor(qualifier = "mediaContext", type = Accessor.Type.GETTER)
	public MediaContextModel getMediaContext()
	{
		if (this._mediaContext!=null)
		{
			return _mediaContext;
		}
		return _mediaContext = getPersistenceContext().getValue(MEDIACONTEXT, _mediaContext);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaFormatMapping.source</code> attribute defined at extension <code>core</code>. 
	 * @return the source - Source format
	 */
	@Accessor(qualifier = "source", type = Accessor.Type.GETTER)
	public MediaFormatModel getSource()
	{
		if (this._source!=null)
		{
			return _source;
		}
		return _source = getPersistenceContext().getValue(SOURCE, _source);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaFormatMapping.target</code> attribute defined at extension <code>core</code>. 
	 * @return the target - Target format
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.GETTER)
	public MediaFormatModel getTarget()
	{
		if (this._target!=null)
		{
			return _target;
		}
		return _target = getPersistenceContext().getValue(TARGET, _target);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaFormatMapping.mediaContext</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the mediaContext
	 */
	@Accessor(qualifier = "mediaContext", type = Accessor.Type.SETTER)
	public void setMediaContext(final MediaContextModel value)
	{
		_mediaContext = getPersistenceContext().setValue(MEDIACONTEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MediaFormatMapping.source</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the source - Source format
	 */
	@Accessor(qualifier = "source", type = Accessor.Type.SETTER)
	public void setSource(final MediaFormatModel value)
	{
		_source = getPersistenceContext().setValue(SOURCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MediaFormatMapping.target</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the target - Target format
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.SETTER)
	public void setTarget(final MediaFormatModel value)
	{
		_target = getPersistenceContext().setValue(TARGET, value);
	}
	
}
