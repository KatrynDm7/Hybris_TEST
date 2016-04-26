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
package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;

/**
 * Generated model class for type WorkflowActionComment first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowActionCommentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "WorkflowActionComment";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionCommentRelation</code> defining source attribute <code>workflowAction</code> in extension <code>workflow</code>.</i>*/
	public final static String _WORKFLOWACTIONCOMMENTRELATION = "WorkflowActionCommentRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionComment.comment</code> attribute defined at extension <code>workflow</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionComment.user</code> attribute defined at extension <code>workflow</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionComment.workflowAction</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOWACTION = "workflowAction";
	
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowActionComment.comment</code> attribute defined at extension <code>workflow</code>. */
	private String _comment;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowActionComment.user</code> attribute defined at extension <code>workflow</code>. */
	private UserModel _user;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowActionComment.workflowAction</code> attribute defined at extension <code>workflow</code>. */
	private AbstractWorkflowActionModel _workflowAction;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowActionCommentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowActionCommentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>WorkflowActionComment</code> at extension <code>workflow</code>
	 * @param _workflowAction initial attribute declared by type <code>WorkflowActionComment</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionCommentModel(final String _comment, final AbstractWorkflowActionModel _workflowAction)
	{
		super();
		setComment(_comment);
		setWorkflowAction(_workflowAction);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>WorkflowActionComment</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _workflowAction initial attribute declared by type <code>WorkflowActionComment</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionCommentModel(final String _comment, final ItemModel _owner, final AbstractWorkflowActionModel _workflowAction)
	{
		super();
		setComment(_comment);
		setOwner(_owner);
		setWorkflowAction(_workflowAction);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionComment.comment</code> attribute defined at extension <code>workflow</code>. 
	 * @return the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public String getComment()
	{
		if (this._comment!=null)
		{
			return _comment;
		}
		return _comment = getPersistenceContext().getValue(COMMENT, _comment);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionComment.user</code> attribute defined at extension <code>workflow</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionComment.workflowAction</code> attribute defined at extension <code>workflow</code>. 
	 * @return the workflowAction
	 */
	@Accessor(qualifier = "workflowAction", type = Accessor.Type.GETTER)
	public AbstractWorkflowActionModel getWorkflowAction()
	{
		if (this._workflowAction!=null)
		{
			return _workflowAction;
		}
		return _workflowAction = getPersistenceContext().getValue(WORKFLOWACTION, _workflowAction);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionComment.comment</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final String value)
	{
		_comment = getPersistenceContext().setValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionComment.user</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		_user = getPersistenceContext().setValue(USER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionComment.workflowAction</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the workflowAction
	 */
	@Accessor(qualifier = "workflowAction", type = Accessor.Type.SETTER)
	public void setWorkflowAction(final AbstractWorkflowActionModel value)
	{
		_workflowAction = getPersistenceContext().setValue(WORKFLOWACTION, value);
	}
	
}
