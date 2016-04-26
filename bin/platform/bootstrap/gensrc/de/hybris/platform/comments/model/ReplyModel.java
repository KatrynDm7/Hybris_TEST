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
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type Reply first defined at extension comments.
 */
@SuppressWarnings("all")
public class ReplyModel extends AbstractCommentModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Reply";
	
	/**<i>Generated relation code constant for relation <code>ReplyParentRelation</code> defining source attribute <code>replies</code> in extension <code>comments</code>.</i>*/
	public final static String _REPLYPARENTRELATION = "ReplyParentRelation";
	
	/**<i>Generated relation code constant for relation <code>CommentReplyRelation</code> defining source attribute <code>comment</code> in extension <code>comments</code>.</i>*/
	public final static String _COMMENTREPLYRELATION = "CommentReplyRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Reply.replies</code> attribute defined at extension <code>comments</code>. */
	public static final String REPLIES = "replies";
	
	/** <i>Generated constant</i> - Attribute key of <code>Reply.parent</code> attribute defined at extension <code>comments</code>. */
	public static final String PARENT = "parent";
	
	/** <i>Generated constant</i> - Attribute key of <code>Reply.comment</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENT = "comment";
	
	
	/** <i>Generated variable</i> - Variable of <code>Reply.replies</code> attribute defined at extension <code>comments</code>. */
	private List<ReplyModel> _replies;
	
	/** <i>Generated variable</i> - Variable of <code>Reply.parent</code> attribute defined at extension <code>comments</code>. */
	private ReplyModel _parent;
	
	/** <i>Generated variable</i> - Variable of <code>Reply.comment</code> attribute defined at extension <code>comments</code>. */
	private CommentModel _comment;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ReplyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ReplyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _comment initial attribute declared by type <code>Reply</code> at extension <code>comments</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public ReplyModel(final UserModel _author, final CommentModel _comment, final String _text)
	{
		super();
		setAuthor(_author);
		setComment(_comment);
		setText(_text);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _comment initial attribute declared by type <code>Reply</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _parent initial attribute declared by type <code>Reply</code> at extension <code>comments</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public ReplyModel(final UserModel _author, final CommentModel _comment, final ItemModel _owner, final ReplyModel _parent, final String _text)
	{
		super();
		setAuthor(_author);
		setComment(_comment);
		setOwner(_owner);
		setParent(_parent);
		setText(_text);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Reply.comment</code> attribute defined at extension <code>comments</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Reply.parent</code> attribute defined at extension <code>comments</code>. 
	 * @return the parent
	 */
	@Accessor(qualifier = "parent", type = Accessor.Type.GETTER)
	public ReplyModel getParent()
	{
		if (this._parent!=null)
		{
			return _parent;
		}
		return _parent = getPersistenceContext().getValue(PARENT, _parent);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Reply.replies</code> attribute defined at extension <code>comments</code>. 
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
	 * <i>Generated method</i> - Initial setter of <code>Reply.comment</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final CommentModel value)
	{
		_comment = getPersistenceContext().setValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Reply.parent</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the parent
	 */
	@Accessor(qualifier = "parent", type = Accessor.Type.SETTER)
	public void setParent(final ReplyModel value)
	{
		_parent = getPersistenceContext().setValue(PARENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Reply.replies</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the replies
	 */
	@Accessor(qualifier = "replies", type = Accessor.Type.SETTER)
	public void setReplies(final List<ReplyModel> value)
	{
		_replies = getPersistenceContext().setValue(REPLIES, value);
	}
	
}
