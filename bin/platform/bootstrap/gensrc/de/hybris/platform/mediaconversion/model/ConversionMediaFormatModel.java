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
package de.hybris.platform.mediaconversion.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type ConversionMediaFormat first defined at extension mediaconversion.
 */
@SuppressWarnings("all")
public class ConversionMediaFormatModel extends MediaFormatModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ConversionMediaFormat";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionMediaFormat.mimeType</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String MIMETYPE = "mimeType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionMediaFormat.conversion</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String CONVERSION = "conversion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionMediaFormat.conversionStrategy</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String CONVERSIONSTRATEGY = "conversionStrategy";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionMediaFormat.inputFormat</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String INPUTFORMAT = "inputFormat";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionMediaFormat.mediaAddOns</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String MEDIAADDONS = "mediaAddOns";
	
	
	/** <i>Generated variable</i> - Variable of <code>ConversionMediaFormat.mimeType</code> attribute defined at extension <code>mediaconversion</code>. */
	private String _mimeType;
	
	/** <i>Generated variable</i> - Variable of <code>ConversionMediaFormat.conversion</code> attribute defined at extension <code>mediaconversion</code>. */
	private String _conversion;
	
	/** <i>Generated variable</i> - Variable of <code>ConversionMediaFormat.conversionStrategy</code> attribute defined at extension <code>mediaconversion</code>. */
	private String _conversionStrategy;
	
	/** <i>Generated variable</i> - Variable of <code>ConversionMediaFormat.inputFormat</code> attribute defined at extension <code>mediaconversion</code>. */
	private ConversionMediaFormatModel _inputFormat;
	
	/** <i>Generated variable</i> - Variable of <code>ConversionMediaFormat.mediaAddOns</code> attribute defined at extension <code>mediaconversion</code>. */
	private List<MediaModel> _mediaAddOns;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ConversionMediaFormatModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ConversionMediaFormatModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _qualifier initial attribute declared by type <code>MediaFormat</code> at extension <code>core</code>
	 */
	@Deprecated
	public ConversionMediaFormatModel(final String _qualifier)
	{
		super();
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>MediaFormat</code> at extension <code>core</code>
	 */
	@Deprecated
	public ConversionMediaFormatModel(final ItemModel _owner, final String _qualifier)
	{
		super();
		setOwner(_owner);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionMediaFormat.conversion</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the conversion - Mime type of this format.
	 */
	@Accessor(qualifier = "conversion", type = Accessor.Type.GETTER)
	public String getConversion()
	{
		if (this._conversion!=null)
		{
			return _conversion;
		}
		return _conversion = getPersistenceContext().getValue(CONVERSION, _conversion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionMediaFormat.conversionStrategy</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the conversionStrategy - Bean name of the conversion strategy to use.
	 */
	@Accessor(qualifier = "conversionStrategy", type = Accessor.Type.GETTER)
	public String getConversionStrategy()
	{
		if (this._conversionStrategy!=null)
		{
			return _conversionStrategy;
		}
		return _conversionStrategy = getPersistenceContext().getValue(CONVERSIONSTRATEGY, _conversionStrategy);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionMediaFormat.inputFormat</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the inputFormat - Media format to take as input for the conversion (optional).
	 */
	@Accessor(qualifier = "inputFormat", type = Accessor.Type.GETTER)
	public ConversionMediaFormatModel getInputFormat()
	{
		if (this._inputFormat!=null)
		{
			return _inputFormat;
		}
		return _inputFormat = getPersistenceContext().getValue(INPUTFORMAT, _inputFormat);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionMediaFormat.mediaAddOns</code> attribute defined at extension <code>mediaconversion</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the mediaAddOns - Additional media to be used in the conversion (optional).
	 */
	@Accessor(qualifier = "mediaAddOns", type = Accessor.Type.GETTER)
	public List<MediaModel> getMediaAddOns()
	{
		if (this._mediaAddOns!=null)
		{
			return _mediaAddOns;
		}
		return _mediaAddOns = getPersistenceContext().getValue(MEDIAADDONS, _mediaAddOns);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionMediaFormat.mimeType</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the mimeType - Mime type of this format.
	 */
	@Accessor(qualifier = "mimeType", type = Accessor.Type.GETTER)
	public String getMimeType()
	{
		if (this._mimeType!=null)
		{
			return _mimeType;
		}
		return _mimeType = getPersistenceContext().getValue(MIMETYPE, _mimeType);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConversionMediaFormat.conversion</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the conversion - Mime type of this format.
	 */
	@Accessor(qualifier = "conversion", type = Accessor.Type.SETTER)
	public void setConversion(final String value)
	{
		_conversion = getPersistenceContext().setValue(CONVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConversionMediaFormat.conversionStrategy</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the conversionStrategy - Bean name of the conversion strategy to use.
	 */
	@Accessor(qualifier = "conversionStrategy", type = Accessor.Type.SETTER)
	public void setConversionStrategy(final String value)
	{
		_conversionStrategy = getPersistenceContext().setValue(CONVERSIONSTRATEGY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConversionMediaFormat.inputFormat</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the inputFormat - Media format to take as input for the conversion (optional).
	 */
	@Accessor(qualifier = "inputFormat", type = Accessor.Type.SETTER)
	public void setInputFormat(final ConversionMediaFormatModel value)
	{
		_inputFormat = getPersistenceContext().setValue(INPUTFORMAT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConversionMediaFormat.mediaAddOns</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the mediaAddOns - Additional media to be used in the conversion (optional).
	 */
	@Accessor(qualifier = "mediaAddOns", type = Accessor.Type.SETTER)
	public void setMediaAddOns(final List<MediaModel> value)
	{
		_mediaAddOns = getPersistenceContext().setValue(MEDIAADDONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConversionMediaFormat.mimeType</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the mimeType - Mime type of this format.
	 */
	@Accessor(qualifier = "mimeType", type = Accessor.Type.SETTER)
	public void setMimeType(final String value)
	{
		_mimeType = getPersistenceContext().setValue(MIMETYPE, value);
	}
	
}
