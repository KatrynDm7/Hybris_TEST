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
package de.hybris.platform.commons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commons.model.FormatModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type Document first defined at extension commons.
 */
@SuppressWarnings("all")
public class DocumentModel extends MediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Document";
	
	/**<i>Generated relation code constant for relation <code>ItemDocrRelation</code> defining source attribute <code>sourceItem</code> in extension <code>commons</code>.</i>*/
	public final static String _ITEMDOCRRELATION = "ItemDocrRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Document.itemTimestamp</code> attribute defined at extension <code>commons</code>. */
	public static final String ITEMTIMESTAMP = "itemTimestamp";
	
	/** <i>Generated constant</i> - Attribute key of <code>Document.format</code> attribute defined at extension <code>commons</code>. */
	public static final String FORMAT = "format";
	
	/** <i>Generated constant</i> - Attribute key of <code>Document.sourceItem</code> attribute defined at extension <code>commons</code>. */
	public static final String SOURCEITEM = "sourceItem";
	
	
	/** <i>Generated variable</i> - Variable of <code>Document.itemTimestamp</code> attribute defined at extension <code>commons</code>. */
	private Date _itemTimestamp;
	
	/** <i>Generated variable</i> - Variable of <code>Document.format</code> attribute defined at extension <code>commons</code>. */
	private FormatModel _format;
	
	/** <i>Generated variable</i> - Variable of <code>Document.sourceItem</code> attribute defined at extension <code>commons</code>. */
	private ItemModel _sourceItem;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DocumentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DocumentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Document</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _format initial attribute declared by type <code>Document</code> at extension <code>commons</code>
	 */
	@Deprecated
	public DocumentModel(final CatalogVersionModel _catalogVersion, final String _code, final FormatModel _format)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setFormat(_format);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Document</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _format initial attribute declared by type <code>Document</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceItem initial attribute declared by type <code>Document</code> at extension <code>commons</code>
	 */
	@Deprecated
	public DocumentModel(final CatalogVersionModel _catalogVersion, final String _code, final FormatModel _format, final ItemModel _owner, final ItemModel _sourceItem)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setFormat(_format);
		setOwner(_owner);
		setSourceItem(_sourceItem);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Document.format</code> attribute defined at extension <code>commons</code>. 
	 * @return the format - The format of the document
	 */
	@Accessor(qualifier = "format", type = Accessor.Type.GETTER)
	public FormatModel getFormat()
	{
		if (this._format!=null)
		{
			return _format;
		}
		return _format = getPersistenceContext().getValue(FORMAT, _format);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Document.itemTimestamp</code> attribute defined at extension <code>commons</code>. 
	 * @return the itemTimestamp - The modified time of the attached item. If the time of the item is
	 * 					younger than the value of this document it could be outdated.
	 */
	@Accessor(qualifier = "itemTimestamp", type = Accessor.Type.GETTER)
	public Date getItemTimestamp()
	{
		if (this._itemTimestamp!=null)
		{
			return _itemTimestamp;
		}
		return _itemTimestamp = getPersistenceContext().getValue(ITEMTIMESTAMP, _itemTimestamp);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Document.sourceItem</code> attribute defined at extension <code>commons</code>. 
	 * @return the sourceItem
	 */
	@Accessor(qualifier = "sourceItem", type = Accessor.Type.GETTER)
	public ItemModel getSourceItem()
	{
		if (this._sourceItem!=null)
		{
			return _sourceItem;
		}
		return _sourceItem = getPersistenceContext().getValue(SOURCEITEM, _sourceItem);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Document.format</code> attribute defined at extension <code>commons</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the format - The format of the document
	 */
	@Accessor(qualifier = "format", type = Accessor.Type.SETTER)
	public void setFormat(final FormatModel value)
	{
		_format = getPersistenceContext().setValue(FORMAT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Document.itemTimestamp</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the itemTimestamp - The modified time of the attached item. If the time of the item is
	 * 					younger than the value of this document it could be outdated.
	 */
	@Accessor(qualifier = "itemTimestamp", type = Accessor.Type.SETTER)
	public void setItemTimestamp(final Date value)
	{
		_itemTimestamp = getPersistenceContext().setValue(ITEMTIMESTAMP, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Document.sourceItem</code> attribute defined at extension <code>commons</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceItem
	 */
	@Accessor(qualifier = "sourceItem", type = Accessor.Type.SETTER)
	public void setSourceItem(final ItemModel value)
	{
		_sourceItem = getPersistenceContext().setValue(SOURCEITEM, value);
	}
	
}
