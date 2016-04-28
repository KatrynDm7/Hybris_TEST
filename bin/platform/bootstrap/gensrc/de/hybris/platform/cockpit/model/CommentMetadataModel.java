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
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CommentMetadata first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CommentMetadataModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CommentMetadata";
	
	/**<i>Generated relation code constant for relation <code>CommentToCommentMetadata</code> defining source attribute <code>comment</code> in extension <code>cockpit</code>.</i>*/
	public final static String _COMMENTTOCOMMENTMETADATA = "CommentToCommentMetadata";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentMetadata.x</code> attribute defined at extension <code>cockpit</code>. */
	public static final String X = "x";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentMetadata.y</code> attribute defined at extension <code>cockpit</code>. */
	public static final String Y = "y";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentMetadata.pageIndex</code> attribute defined at extension <code>cockpit</code>. */
	public static final String PAGEINDEX = "pageIndex";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentMetadata.item</code> attribute defined at extension <code>cockpit</code>. */
	public static final String ITEM = "item";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentMetadata.comment</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COMMENT = "comment";
	
	
	/** <i>Generated variable</i> - Variable of <code>CommentMetadata.x</code> attribute defined at extension <code>cockpit</code>. */
	private Integer _x;
	
	/** <i>Generated variable</i> - Variable of <code>CommentMetadata.y</code> attribute defined at extension <code>cockpit</code>. */
	private Integer _y;
	
	/** <i>Generated variable</i> - Variable of <code>CommentMetadata.pageIndex</code> attribute defined at extension <code>cockpit</code>. */
	private Integer _pageIndex;
	
	/** <i>Generated variable</i> - Variable of <code>CommentMetadata.item</code> attribute defined at extension <code>cockpit</code>. */
	private ItemModel _item;
	
	/** <i>Generated variable</i> - Variable of <code>CommentMetadata.comment</code> attribute defined at extension <code>cockpit</code>. */
	private CommentModel _comment;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CommentMetadataModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CommentMetadataModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>CommentMetadata</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CommentMetadataModel(final CommentModel _comment)
	{
		super();
		setComment(_comment);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>CommentMetadata</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CommentMetadataModel(final CommentModel _comment, final ItemModel _owner)
	{
		super();
		setComment(_comment);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentMetadata.comment</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public CommentModel getComment()
	{
		if (this._comment!=null)
		{
			return _comment;
		}
		return _comment = getPersistenceContext().getValue(COMMENT, _comment);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentMetadata.item</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the item
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.GETTER)
	public ItemModel getItem()
	{
		if (this._item!=null)
		{
			return _item;
		}
		return _item = getPersistenceContext().getValue(ITEM, _item);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentMetadata.pageIndex</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the pageIndex
	 */
	@Accessor(qualifier = "pageIndex", type = Accessor.Type.GETTER)
	public Integer getPageIndex()
	{
		if (this._pageIndex!=null)
		{
			return _pageIndex;
		}
		return _pageIndex = getPersistenceContext().getValue(PAGEINDEX, _pageIndex);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentMetadata.x</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the x
	 */
	@Accessor(qualifier = "x", type = Accessor.Type.GETTER)
	public Integer getX()
	{
		if (this._x!=null)
		{
			return _x;
		}
		return _x = getPersistenceContext().getValue(X, _x);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentMetadata.y</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the y
	 */
	@Accessor(qualifier = "y", type = Accessor.Type.GETTER)
	public Integer getY()
	{
		if (this._y!=null)
		{
			return _y;
		}
		return _y = getPersistenceContext().getValue(Y, _y);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CommentMetadata.comment</code> attribute defined at extension <code>cockpit</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final CommentModel value)
	{
		_comment = getPersistenceContext().setValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentMetadata.item</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the item
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.SETTER)
	public void setItem(final ItemModel value)
	{
		_item = getPersistenceContext().setValue(ITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentMetadata.pageIndex</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the pageIndex
	 */
	@Accessor(qualifier = "pageIndex", type = Accessor.Type.SETTER)
	public void setPageIndex(final Integer value)
	{
		_pageIndex = getPersistenceContext().setValue(PAGEINDEX, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentMetadata.x</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the x
	 */
	@Accessor(qualifier = "x", type = Accessor.Type.SETTER)
	public void setX(final Integer value)
	{
		_x = getPersistenceContext().setValue(X, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentMetadata.y</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the y
	 */
	@Accessor(qualifier = "y", type = Accessor.Type.SETTER)
	public void setY(final Integer value)
	{
		_y = getPersistenceContext().setValue(Y, value);
	}
	
}
