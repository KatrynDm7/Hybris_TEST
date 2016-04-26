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
package de.hybris.platform.core.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type Item first defined at extension core.
 */
@SuppressWarnings("all")
public class ItemModel extends AbstractItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Item";
	
	/**<i>Generated relation code constant for relation <code>CommentItemRelation</code> defining source attribute <code>comments</code> in extension <code>comments</code>.</i>*/
	public final static String _COMMENTITEMRELATION = "CommentItemRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.creationtime</code> attribute defined at extension <code>core</code>. */
	public static final String CREATIONTIME = "creationtime";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.modifiedtime</code> attribute defined at extension <code>core</code>. */
	public static final String MODIFIEDTIME = "modifiedtime";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.itemtype</code> attribute defined at extension <code>core</code>. */
	public static final String ITEMTYPE = "itemtype";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.owner</code> attribute defined at extension <code>core</code>. */
	public static final String OWNER = "owner";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.pk</code> attribute defined at extension <code>core</code>. */
	public static final String PK = "pk";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.comments</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENTS = "comments";
	
	
	/** <i>Generated variable</i> - Variable of <code>Item.creationtime</code> attribute defined at extension <code>core</code>. */
	private Date _creationtime;
	
	/** <i>Generated variable</i> - Variable of <code>Item.modifiedtime</code> attribute defined at extension <code>core</code>. */
	private Date _modifiedtime;
	
	/** <i>Generated variable</i> - Variable of <code>Item.owner</code> attribute defined at extension <code>core</code>. */
	private ItemModel _owner;
	
	/** <i>Generated variable</i> - Variable of <code>Item.comments</code> attribute defined at extension <code>comments</code>. */
	private List<CommentModel> _comments;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ItemModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ItemModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ItemModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.comments</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the comments
	 */
	@Accessor(qualifier = "comments", type = Accessor.Type.GETTER)
	public List<CommentModel> getComments()
	{
		if (this._comments!=null)
		{
			return _comments;
		}
		return _comments = getPersistenceContext().getValue(COMMENTS, _comments);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.creationtime</code> attribute defined at extension <code>core</code>. 
	 * @return the creationtime
	 */
	@Accessor(qualifier = "creationtime", type = Accessor.Type.GETTER)
	public Date getCreationtime()
	{
		if (this._creationtime!=null)
		{
			return _creationtime;
		}
		return _creationtime = getPersistenceContext().getValue(CREATIONTIME, _creationtime);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.modifiedtime</code> attribute defined at extension <code>core</code>. 
	 * @return the modifiedtime
	 */
	@Accessor(qualifier = "modifiedtime", type = Accessor.Type.GETTER)
	public Date getModifiedtime()
	{
		if (this._modifiedtime!=null)
		{
			return _modifiedtime;
		}
		return _modifiedtime = getPersistenceContext().getValue(MODIFIEDTIME, _modifiedtime);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.owner</code> attribute defined at extension <code>core</code>. 
	 * @return the owner
	 */
	@Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
	public ItemModel getOwner()
	{
		if (this._owner!=null)
		{
			return _owner;
		}
		return _owner = getPersistenceContext().getValue(OWNER, _owner);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Item.comments</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the comments
	 */
	@Accessor(qualifier = "comments", type = Accessor.Type.SETTER)
	public void setComments(final List<CommentModel> value)
	{
		_comments = getPersistenceContext().setValue(COMMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Item.creationtime</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the creationtime
	 */
	@Accessor(qualifier = "creationtime", type = Accessor.Type.SETTER)
	public void setCreationtime(final Date value)
	{
		_creationtime = getPersistenceContext().setValue(CREATIONTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Item.modifiedtime</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the modifiedtime
	 */
	@Accessor(qualifier = "modifiedtime", type = Accessor.Type.SETTER)
	public void setModifiedtime(final Date value)
	{
		_modifiedtime = getPersistenceContext().setValue(MODIFIEDTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Item.owner</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the owner
	 */
	@Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
	public void setOwner(final ItemModel value)
	{
		_owner = getPersistenceContext().setValue(OWNER, value);
	}
	
}
