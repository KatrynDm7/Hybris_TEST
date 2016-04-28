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
package de.hybris.platform.impex.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ImpExMedia first defined at extension impex.
 */
@SuppressWarnings("all")
public class ImpExMediaModel extends MediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ImpExMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExMedia.fieldSeparator</code> attribute defined at extension <code>impex</code>. */
	public static final String FIELDSEPARATOR = "fieldSeparator";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExMedia.quoteCharacter</code> attribute defined at extension <code>impex</code>. */
	public static final String QUOTECHARACTER = "quoteCharacter";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExMedia.commentCharacter</code> attribute defined at extension <code>impex</code>. */
	public static final String COMMENTCHARACTER = "commentCharacter";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExMedia.encoding</code> attribute defined at extension <code>impex</code>. */
	public static final String ENCODING = "encoding";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExMedia.linesToSkip</code> attribute defined at extension <code>impex</code>. */
	public static final String LINESTOSKIP = "linesToSkip";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExMedia.removeOnSuccess</code> attribute defined at extension <code>impex</code>. */
	public static final String REMOVEONSUCCESS = "removeOnSuccess";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExMedia.zipentry</code> attribute defined at extension <code>impex</code>. */
	public static final String ZIPENTRY = "zipentry";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExMedia.preview</code> attribute defined at extension <code>impex</code>. */
	public static final String PREVIEW = "preview";
	
	
	/** <i>Generated variable</i> - Variable of <code>ImpExMedia.fieldSeparator</code> attribute defined at extension <code>impex</code>. */
	private Character _fieldSeparator;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExMedia.quoteCharacter</code> attribute defined at extension <code>impex</code>. */
	private Character _quoteCharacter;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExMedia.commentCharacter</code> attribute defined at extension <code>impex</code>. */
	private Character _commentCharacter;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExMedia.encoding</code> attribute defined at extension <code>impex</code>. */
	private EncodingEnum _encoding;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExMedia.linesToSkip</code> attribute defined at extension <code>impex</code>. */
	private Integer _linesToSkip;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExMedia.removeOnSuccess</code> attribute defined at extension <code>impex</code>. */
	private Boolean _removeOnSuccess;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExMedia.zipentry</code> attribute defined at extension <code>impex</code>. */
	private String _zipentry;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExMedia.preview</code> attribute defined at extension <code>impex</code>. */
	private String _preview;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ImpExMediaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ImpExMediaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>ImpExMedia</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _linesToSkip initial attribute declared by type <code>ImpExMedia</code> at extension <code>impex</code>
	 * @param _removeOnSuccess initial attribute declared by type <code>ImpExMedia</code> at extension <code>impex</code>
	 */
	@Deprecated
	public ImpExMediaModel(final CatalogVersionModel _catalogVersion, final String _code, final int _linesToSkip, final boolean _removeOnSuccess)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setLinesToSkip(_linesToSkip);
		setRemoveOnSuccess(_removeOnSuccess);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>ImpExMedia</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _linesToSkip initial attribute declared by type <code>ImpExMedia</code> at extension <code>impex</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _removeOnSuccess initial attribute declared by type <code>ImpExMedia</code> at extension <code>impex</code>
	 */
	@Deprecated
	public ImpExMediaModel(final CatalogVersionModel _catalogVersion, final String _code, final int _linesToSkip, final ItemModel _owner, final boolean _removeOnSuccess)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setLinesToSkip(_linesToSkip);
		setOwner(_owner);
		setRemoveOnSuccess(_removeOnSuccess);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExMedia.commentCharacter</code> attribute defined at extension <code>impex</code>. 
	 * @return the commentCharacter - Character used for indicating a comment
	 */
	@Accessor(qualifier = "commentCharacter", type = Accessor.Type.GETTER)
	public Character getCommentCharacter()
	{
		if (this._commentCharacter!=null)
		{
			return _commentCharacter;
		}
		return _commentCharacter = getPersistenceContext().getValue(COMMENTCHARACTER, _commentCharacter);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExMedia.encoding</code> attribute defined at extension <code>impex</code>. 
	 * @return the encoding - Used encoding for data stored within media
	 */
	@Accessor(qualifier = "encoding", type = Accessor.Type.GETTER)
	public EncodingEnum getEncoding()
	{
		if (this._encoding!=null)
		{
			return _encoding;
		}
		return _encoding = getPersistenceContext().getValue(ENCODING, _encoding);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExMedia.fieldSeparator</code> attribute defined at extension <code>impex</code>. 
	 * @return the fieldSeparator - Character used for separating columns in CSV-lines
	 */
	@Accessor(qualifier = "fieldSeparator", type = Accessor.Type.GETTER)
	public Character getFieldSeparator()
	{
		if (this._fieldSeparator!=null)
		{
			return _fieldSeparator;
		}
		return _fieldSeparator = getPersistenceContext().getValue(FIELDSEPARATOR, _fieldSeparator);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExMedia.linesToSkip</code> attribute defined at extension <code>impex</code>. 
	 * @return the linesToSkip - Amount of lines ImpEx has to skip while processing data
	 */
	@Accessor(qualifier = "linesToSkip", type = Accessor.Type.GETTER)
	public int getLinesToSkip()
	{
		return toPrimitive( _linesToSkip = getPersistenceContext().getValue(LINESTOSKIP, _linesToSkip));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExMedia.preview</code> attribute defined at extension <code>impex</code>. 
	 * @return the preview - Preview of content
	 */
	@Accessor(qualifier = "preview", type = Accessor.Type.GETTER)
	public String getPreview()
	{
		if (this._preview!=null)
		{
			return _preview;
		}
		return _preview = getPersistenceContext().getValue(PREVIEW, _preview);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExMedia.quoteCharacter</code> attribute defined at extension <code>impex</code>. 
	 * @return the quoteCharacter - Character used for escaping columns in CSV-lines
	 */
	@Accessor(qualifier = "quoteCharacter", type = Accessor.Type.GETTER)
	public Character getQuoteCharacter()
	{
		if (this._quoteCharacter!=null)
		{
			return _quoteCharacter;
		}
		return _quoteCharacter = getPersistenceContext().getValue(QUOTECHARACTER, _quoteCharacter);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExMedia.zipentry</code> attribute defined at extension <code>impex</code>. 
	 * @return the zipentry
	 */
	@Accessor(qualifier = "zipentry", type = Accessor.Type.GETTER)
	public String getZipentry()
	{
		if (this._zipentry!=null)
		{
			return _zipentry;
		}
		return _zipentry = getPersistenceContext().getValue(ZIPENTRY, _zipentry);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExMedia.removeOnSuccess</code> attribute defined at extension <code>impex</code>. 
	 * @return the removeOnSuccess - Should the media be removed after processing?
	 */
	@Accessor(qualifier = "removeOnSuccess", type = Accessor.Type.GETTER)
	public boolean isRemoveOnSuccess()
	{
		return toPrimitive( _removeOnSuccess = getPersistenceContext().getValue(REMOVEONSUCCESS, _removeOnSuccess));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExMedia.commentCharacter</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the commentCharacter - Character used for indicating a comment
	 */
	@Accessor(qualifier = "commentCharacter", type = Accessor.Type.SETTER)
	public void setCommentCharacter(final Character value)
	{
		_commentCharacter = getPersistenceContext().setValue(COMMENTCHARACTER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExMedia.encoding</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the encoding - Used encoding for data stored within media
	 */
	@Accessor(qualifier = "encoding", type = Accessor.Type.SETTER)
	public void setEncoding(final EncodingEnum value)
	{
		_encoding = getPersistenceContext().setValue(ENCODING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExMedia.fieldSeparator</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the fieldSeparator - Character used for separating columns in CSV-lines
	 */
	@Accessor(qualifier = "fieldSeparator", type = Accessor.Type.SETTER)
	public void setFieldSeparator(final Character value)
	{
		_fieldSeparator = getPersistenceContext().setValue(FIELDSEPARATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExMedia.linesToSkip</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the linesToSkip - Amount of lines ImpEx has to skip while processing data
	 */
	@Accessor(qualifier = "linesToSkip", type = Accessor.Type.SETTER)
	public void setLinesToSkip(final int value)
	{
		_linesToSkip = getPersistenceContext().setValue(LINESTOSKIP, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExMedia.quoteCharacter</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the quoteCharacter - Character used for escaping columns in CSV-lines
	 */
	@Accessor(qualifier = "quoteCharacter", type = Accessor.Type.SETTER)
	public void setQuoteCharacter(final Character value)
	{
		_quoteCharacter = getPersistenceContext().setValue(QUOTECHARACTER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExMedia.removeOnSuccess</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the removeOnSuccess - Should the media be removed after processing?
	 */
	@Accessor(qualifier = "removeOnSuccess", type = Accessor.Type.SETTER)
	public void setRemoveOnSuccess(final boolean value)
	{
		_removeOnSuccess = getPersistenceContext().setValue(REMOVEONSUCCESS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExMedia.zipentry</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the zipentry
	 */
	@Accessor(qualifier = "zipentry", type = Accessor.Type.SETTER)
	public void setZipentry(final String value)
	{
		_zipentry = getPersistenceContext().setValue(ZIPENTRY, value);
	}
	
}
