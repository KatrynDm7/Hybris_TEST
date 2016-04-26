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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CommentUserSetting first defined at extension comments.
 */
@SuppressWarnings("all")
public class CommentUserSettingModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CommentUserSetting";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.read</code> attribute defined at extension <code>comments</code>. */
	public static final String READ = "read";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.ignored</code> attribute defined at extension <code>comments</code>. */
	public static final String IGNORED = "ignored";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.priority</code> attribute defined at extension <code>comments</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.comment</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.user</code> attribute defined at extension <code>comments</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.workStatus</code> attribute defined at extension <code>cockpit</code>. */
	public static final String WORKSTATUS = "workStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.hidden</code> attribute defined at extension <code>cockpit</code>. */
	public static final String HIDDEN = "hidden";
	
	
	/** <i>Generated variable</i> - Variable of <code>CommentUserSetting.read</code> attribute defined at extension <code>comments</code>. */
	private Boolean _read;
	
	/** <i>Generated variable</i> - Variable of <code>CommentUserSetting.ignored</code> attribute defined at extension <code>comments</code>. */
	private Boolean _ignored;
	
	/** <i>Generated variable</i> - Variable of <code>CommentUserSetting.priority</code> attribute defined at extension <code>comments</code>. */
	private Integer _priority;
	
	/** <i>Generated variable</i> - Variable of <code>CommentUserSetting.comment</code> attribute defined at extension <code>comments</code>. */
	private AbstractCommentModel _comment;
	
	/** <i>Generated variable</i> - Variable of <code>CommentUserSetting.user</code> attribute defined at extension <code>comments</code>. */
	private UserModel _user;
	
	/** <i>Generated variable</i> - Variable of <code>CommentUserSetting.workStatus</code> attribute defined at extension <code>cockpit</code>. */
	private Boolean _workStatus;
	
	/** <i>Generated variable</i> - Variable of <code>CommentUserSetting.hidden</code> attribute defined at extension <code>cockpit</code>. */
	private Boolean _hidden;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CommentUserSettingModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CommentUserSettingModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>CommentUserSetting</code> at extension <code>comments</code>
	 * @param _user initial attribute declared by type <code>CommentUserSetting</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CommentUserSettingModel(final AbstractCommentModel _comment, final UserModel _user)
	{
		super();
		setComment(_comment);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>CommentUserSetting</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>CommentUserSetting</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CommentUserSettingModel(final AbstractCommentModel _comment, final ItemModel _owner, final UserModel _user)
	{
		super();
		setComment(_comment);
		setOwner(_owner);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.comment</code> attribute defined at extension <code>comments</code>. 
	 * @return the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public AbstractCommentModel getComment()
	{
		if (this._comment!=null)
		{
			return _comment;
		}
		return _comment = getPersistenceContext().getValue(COMMENT, _comment);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.hidden</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the hidden
	 */
	@Accessor(qualifier = "hidden", type = Accessor.Type.GETTER)
	public Boolean getHidden()
	{
		if (this._hidden!=null)
		{
			return _hidden;
		}
		return _hidden = getPersistenceContext().getValue(HIDDEN, _hidden);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.ignored</code> attribute defined at extension <code>comments</code>. 
	 * @return the ignored
	 */
	@Accessor(qualifier = "ignored", type = Accessor.Type.GETTER)
	public Boolean getIgnored()
	{
		if (this._ignored!=null)
		{
			return _ignored;
		}
		return _ignored = getPersistenceContext().getValue(IGNORED, _ignored);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.priority</code> attribute defined at extension <code>comments</code>. 
	 * @return the priority
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
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.read</code> attribute defined at extension <code>comments</code>. 
	 * @return the read
	 */
	@Accessor(qualifier = "read", type = Accessor.Type.GETTER)
	public Boolean getRead()
	{
		if (this._read!=null)
		{
			return _read;
		}
		return _read = getPersistenceContext().getValue(READ, _read);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.user</code> attribute defined at extension <code>comments</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		if (this._user!=null)
		{
			return _user;
		}
		return _user = getPersistenceContext().getValue(USER, _user);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.workStatus</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the workStatus
	 */
	@Accessor(qualifier = "workStatus", type = Accessor.Type.GETTER)
	public Boolean getWorkStatus()
	{
		if (this._workStatus!=null)
		{
			return _workStatus;
		}
		return _workStatus = getPersistenceContext().getValue(WORKSTATUS, _workStatus);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CommentUserSetting.comment</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final AbstractCommentModel value)
	{
		_comment = getPersistenceContext().setValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentUserSetting.hidden</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the hidden
	 */
	@Accessor(qualifier = "hidden", type = Accessor.Type.SETTER)
	public void setHidden(final Boolean value)
	{
		_hidden = getPersistenceContext().setValue(HIDDEN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentUserSetting.ignored</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the ignored
	 */
	@Accessor(qualifier = "ignored", type = Accessor.Type.SETTER)
	public void setIgnored(final Boolean value)
	{
		_ignored = getPersistenceContext().setValue(IGNORED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentUserSetting.priority</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		_priority = getPersistenceContext().setValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentUserSetting.read</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the read
	 */
	@Accessor(qualifier = "read", type = Accessor.Type.SETTER)
	public void setRead(final Boolean value)
	{
		_read = getPersistenceContext().setValue(READ, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CommentUserSetting.user</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		_user = getPersistenceContext().setValue(USER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentUserSetting.workStatus</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the workStatus
	 */
	@Accessor(qualifier = "workStatus", type = Accessor.Type.SETTER)
	public void setWorkStatus(final Boolean value)
	{
		_workStatus = getPersistenceContext().setValue(WORKSTATUS, value);
	}
	
}
