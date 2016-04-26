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
package de.hybris.platform.comments.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cockpit.model.CommentMetadataModel;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type Comment first defined at extension comments.
 */
@SuppressWarnings("all")
public class CommentModel extends AbstractCommentModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.code</code> attribute defined at extension <code>comments</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.priority</code> attribute defined at extension <code>comments</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.relatedItems</code> attribute defined at extension <code>comments</code>. */
	public static final String RELATEDITEMS = "relatedItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.replies</code> attribute defined at extension <code>comments</code>. */
	public static final String REPLIES = "replies";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.assignedTo</code> attribute defined at extension <code>comments</code>. */
	public static final String ASSIGNEDTO = "assignedTo";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.watchers</code> attribute defined at extension <code>comments</code>. */
	public static final String WATCHERS = "watchers";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.component</code> attribute defined at extension <code>comments</code>. */
	public static final String COMPONENT = "component";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.commentType</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENTTYPE = "commentType";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.commentMetadata</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COMMENTMETADATA = "commentMetadata";
	
	
	/** <i>Generated variable</i> - Variable of <code>Comment.code</code> attribute defined at extension <code>comments</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>Comment.priority</code> attribute defined at extension <code>comments</code>. */
	private Integer _priority;
	
	/** <i>Generated variable</i> - Variable of <code>Comment.relatedItems</code> attribute defined at extension <code>comments</code>. */
	private Collection<ItemModel> _relatedItems;
	
	/** <i>Generated variable</i> - Variable of <code>Comment.replies</code> attribute defined at extension <code>comments</code>. */
	private List<ReplyModel> _replies;
	
	/** <i>Generated variable</i> - Variable of <code>Comment.assignedTo</code> attribute defined at extension <code>comments</code>. */
	private Collection<UserModel> _assignedTo;
	
	/** <i>Generated variable</i> - Variable of <code>Comment.watchers</code> attribute defined at extension <code>comments</code>. */
	private Collection<PrincipalModel> _watchers;
	
	/** <i>Generated variable</i> - Variable of <code>Comment.component</code> attribute defined at extension <code>comments</code>. */
	private ComponentModel _component;
	
	/** <i>Generated variable</i> - Variable of <code>Comment.commentType</code> attribute defined at extension <code>comments</code>. */
	private CommentTypeModel _commentType;
	
	/** <i>Generated variable</i> - Variable of <code>Comment.commentMetadata</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<CommentMetadataModel> _commentMetadata;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CommentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CommentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _commentType initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _component initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CommentModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final String _text)
	{
		super();
		setAuthor(_author);
		setCommentType(_commentType);
		setComponent(_component);
		setText(_text);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _commentType initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _component initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CommentModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final ItemModel _owner, final String _text)
	{
		super();
		setAuthor(_author);
		setCommentType(_commentType);
		setComponent(_component);
		setOwner(_owner);
		setText(_text);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.assignedTo</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the assignedTo
	 */
	@Accessor(qualifier = "assignedTo", type = Accessor.Type.GETTER)
	public Collection<UserModel> getAssignedTo()
	{
		if (this._assignedTo!=null)
		{
			return _assignedTo;
		}
		return _assignedTo = getPersistenceContext().getValue(ASSIGNEDTO, _assignedTo);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.code</code> attribute defined at extension <code>comments</code>. 
	 * @return the code - unique identifier of the comment; will be generated if not set
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
	 * <i>Generated method</i> - Getter of the <code>Comment.commentMetadata</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the commentMetadata
	 */
	@Accessor(qualifier = "commentMetadata", type = Accessor.Type.GETTER)
	public Collection<CommentMetadataModel> getCommentMetadata()
	{
		if (this._commentMetadata!=null)
		{
			return _commentMetadata;
		}
		return _commentMetadata = getPersistenceContext().getValue(COMMENTMETADATA, _commentMetadata);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.commentType</code> attribute defined at extension <code>comments</code>. 
	 * @return the commentType
	 */
	@Accessor(qualifier = "commentType", type = Accessor.Type.GETTER)
	public CommentTypeModel getCommentType()
	{
		if (this._commentType!=null)
		{
			return _commentType;
		}
		return _commentType = getPersistenceContext().getValue(COMMENTTYPE, _commentType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.component</code> attribute defined at extension <code>comments</code>. 
	 * @return the component
	 */
	@Accessor(qualifier = "component", type = Accessor.Type.GETTER)
	public ComponentModel getComponent()
	{
		if (this._component!=null)
		{
			return _component;
		}
		return _component = getPersistenceContext().getValue(COMPONENT, _component);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.priority</code> attribute defined at extension <code>comments</code>. 
	 * @return the priority - Priority of a comment
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		if (this._priority!=null)
		{
			return _priority;
		}
		return _priority = getPersistenceContext().getValue(PRIORITY, _priority);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.relatedItems</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the relatedItems - Related items of this comment
	 */
	@Deprecated
	@Accessor(qualifier = "relatedItems", type = Accessor.Type.GETTER)
	public Collection<ItemModel> getRelatedItems()
	{
		if (this._relatedItems!=null)
		{
			return _relatedItems;
		}
		return _relatedItems = getPersistenceContext().getValue(RELATEDITEMS, _relatedItems);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.replies</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the replies
	 */
	@Accessor(qualifier = "replies", type = Accessor.Type.GETTER)
	public List<ReplyModel> getReplies()
	{
		if (this._replies!=null)
		{
			return _replies;
		}
		return _replies = getPersistenceContext().getValue(REPLIES, _replies);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.watchers</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the watchers
	 */
	@Accessor(qualifier = "watchers", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getWatchers()
	{
		if (this._watchers!=null)
		{
			return _watchers;
		}
		return _watchers = getPersistenceContext().getValue(WATCHERS, _watchers);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.assignedTo</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the assignedTo
	 */
	@Accessor(qualifier = "assignedTo", type = Accessor.Type.SETTER)
	public void setAssignedTo(final Collection<UserModel> value)
	{
		_assignedTo = getPersistenceContext().setValue(ASSIGNEDTO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.code</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the code - unique identifier of the comment; will be generated if not set
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.commentMetadata</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the commentMetadata
	 */
	@Accessor(qualifier = "commentMetadata", type = Accessor.Type.SETTER)
	public void setCommentMetadata(final Collection<CommentMetadataModel> value)
	{
		_commentMetadata = getPersistenceContext().setValue(COMMENTMETADATA, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Comment.commentType</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the commentType
	 */
	@Accessor(qualifier = "commentType", type = Accessor.Type.SETTER)
	public void setCommentType(final CommentTypeModel value)
	{
		_commentType = getPersistenceContext().setValue(COMMENTTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Comment.component</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the component
	 */
	@Accessor(qualifier = "component", type = Accessor.Type.SETTER)
	public void setComponent(final ComponentModel value)
	{
		_component = getPersistenceContext().setValue(COMPONENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.priority</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the priority - Priority of a comment
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		_priority = getPersistenceContext().setValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.relatedItems</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the relatedItems - Related items of this comment
	 */
	@Deprecated
	@Accessor(qualifier = "relatedItems", type = Accessor.Type.SETTER)
	public void setRelatedItems(final Collection<ItemModel> value)
	{
		_relatedItems = getPersistenceContext().setValue(RELATEDITEMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.replies</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the replies
	 */
	@Accessor(qualifier = "replies", type = Accessor.Type.SETTER)
	public void setReplies(final List<ReplyModel> value)
	{
		_replies = getPersistenceContext().setValue(REPLIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.watchers</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the watchers
	 */
	@Accessor(qualifier = "watchers", type = Accessor.Type.SETTER)
	public void setWatchers(final Collection<PrincipalModel> value)
	{
		_watchers = getPersistenceContext().setValue(WATCHERS, value);
	}
	
}
