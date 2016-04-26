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
package de.hybris.platform.core.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractMedia first defined at extension core.
 */
@SuppressWarnings("all")
public class AbstractMediaModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AbstractMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMedia.mime</code> attribute defined at extension <code>core</code>. */
	public static final String MIME = "mime";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMedia.size</code> attribute defined at extension <code>core</code>. */
	public static final String SIZE = "size";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMedia.dataPK</code> attribute defined at extension <code>core</code>. */
	public static final String DATAPK = "dataPK";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMedia.location</code> attribute defined at extension <code>core</code>. */
	public static final String LOCATION = "location";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMedia.locationHash</code> attribute defined at extension <code>core</code>. */
	public static final String LOCATIONHASH = "locationHash";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMedia.realFileName</code> attribute defined at extension <code>core</code>. */
	public static final String REALFILENAME = "realFileName";
	
	
	/** <i>Generated variable</i> - Variable of <code>AbstractMedia.mime</code> attribute defined at extension <code>core</code>. */
	private String _mime;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractMedia.size</code> attribute defined at extension <code>core</code>. */
	private Long _size;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractMedia.dataPK</code> attribute defined at extension <code>core</code>. */
	private Long _dataPK;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractMedia.location</code> attribute defined at extension <code>core</code>. */
	private String _location;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractMedia.locationHash</code> attribute defined at extension <code>core</code>. */
	private String _locationHash;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractMedia.realFileName</code> attribute defined at extension <code>core</code>. */
	private String _realFileName;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractMediaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractMediaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractMediaModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMedia.dataPK</code> attribute defined at extension <code>core</code>. 
	 * @return the dataPK - PK of the referenced data file.
	 */
	@Accessor(qualifier = "dataPK", type = Accessor.Type.GETTER)
	public Long getDataPK()
	{
		if (this._dataPK!=null)
		{
			return _dataPK;
		}
		return _dataPK = getPersistenceContext().getValue(DATAPK, _dataPK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMedia.location</code> attribute defined at extension <code>core</code>. 
	 * @return the location - Generated location (by one of Storage Strategies) to media within storage.
	 */
	@Accessor(qualifier = "location", type = Accessor.Type.GETTER)
	public String getLocation()
	{
		if (this._location!=null)
		{
			return _location;
		}
		return _location = getPersistenceContext().getValue(LOCATION, _location);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMedia.locationHash</code> attribute defined at extension <code>core</code>. 
	 * @return the locationHash - Computed hash of folder qualifier and location
	 */
	@Accessor(qualifier = "locationHash", type = Accessor.Type.GETTER)
	public String getLocationHash()
	{
		if (this._locationHash!=null)
		{
			return _locationHash;
		}
		return _locationHash = getPersistenceContext().getValue(LOCATIONHASH, _locationHash);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMedia.mime</code> attribute defined at extension <code>core</code>. 
	 * @return the mime - Mime type of referenced data file.
	 */
	@Accessor(qualifier = "mime", type = Accessor.Type.GETTER)
	public String getMime()
	{
		if (this._mime!=null)
		{
			return _mime;
		}
		return _mime = getPersistenceContext().getValue(MIME, _mime);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMedia.realFileName</code> attribute defined at extension <code>core</code>. 
	 * @return the realFileName
	 * @deprecated use {@link #getRealFileName()} instead
	 */
	@Deprecated
	public String getRealfilename()
	{
		return this.getRealFileName();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMedia.realFileName</code> attribute defined at extension <code>core</code>. 
	 * @return the realFileName
	 */
	@Accessor(qualifier = "realFileName", type = Accessor.Type.GETTER)
	public String getRealFileName()
	{
		if (this._realFileName!=null)
		{
			return _realFileName;
		}
		return _realFileName = getPersistenceContext().getValue(REALFILENAME, _realFileName);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMedia.size</code> attribute defined at extension <code>core</code>. 
	 * @return the size - Size of referenced data file.
	 */
	@Accessor(qualifier = "size", type = Accessor.Type.GETTER)
	public Long getSize()
	{
		if (this._size!=null)
		{
			return _size;
		}
		return _size = getPersistenceContext().getValue(SIZE, _size);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMedia.dataPK</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the dataPK - PK of the referenced data file.
	 */
	@Accessor(qualifier = "dataPK", type = Accessor.Type.SETTER)
	public void setDataPK(final Long value)
	{
		_dataPK = getPersistenceContext().setValue(DATAPK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMedia.location</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the location - Generated location (by one of Storage Strategies) to media within storage.
	 */
	@Accessor(qualifier = "location", type = Accessor.Type.SETTER)
	public void setLocation(final String value)
	{
		_location = getPersistenceContext().setValue(LOCATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMedia.locationHash</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the locationHash - Computed hash of folder qualifier and location
	 */
	@Accessor(qualifier = "locationHash", type = Accessor.Type.SETTER)
	public void setLocationHash(final String value)
	{
		_locationHash = getPersistenceContext().setValue(LOCATIONHASH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMedia.mime</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the mime - Mime type of referenced data file.
	 */
	@Accessor(qualifier = "mime", type = Accessor.Type.SETTER)
	public void setMime(final String value)
	{
		_mime = getPersistenceContext().setValue(MIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMedia.realFileName</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the realFileName
	 * @deprecated use {@link #setRealFileName(java.lang.String)} instead
	 */
	@Deprecated
	public void setRealfilename(final String value)
	{
		this.setRealFileName(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMedia.realFileName</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the realFileName
	 */
	@Accessor(qualifier = "realFileName", type = Accessor.Type.SETTER)
	public void setRealFileName(final String value)
	{
		_realFileName = getPersistenceContext().setValue(REALFILENAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMedia.size</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the size - Size of referenced data file.
	 */
	@Accessor(qualifier = "size", type = Accessor.Type.SETTER)
	public void setSize(final Long value)
	{
		_size = getPersistenceContext().setValue(SIZE, value);
	}
	
}
