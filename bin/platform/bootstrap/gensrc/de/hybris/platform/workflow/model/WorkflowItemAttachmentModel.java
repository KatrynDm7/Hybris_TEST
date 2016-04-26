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
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type WorkflowItemAttachment first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowItemAttachmentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "WorkflowItemAttachment";
	
	/**<i>Generated relation code constant for relation <code>WorkflowItemAttachmentRelation</code> defining source attribute <code>workflow</code> in extension <code>workflow</code>.</i>*/
	public final static String _WORKFLOWITEMATTACHMENTRELATION = "WorkflowItemAttachmentRelation";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionItemAttachmentRelation</code> defining source attribute <code>actions</code> in extension <code>workflow</code>.</i>*/
	public final static String _WORKFLOWACTIONITEMATTACHMENTRELATION = "WorkflowActionItemAttachmentRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.code</code> attribute defined at extension <code>workflow</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.name</code> attribute defined at extension <code>workflow</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.comment</code> attribute defined at extension <code>workflow</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.item</code> attribute defined at extension <code>workflow</code>. */
	public static final String ITEM = "item";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.typeOfItem</code> attribute defined at extension <code>workflow</code>. */
	public static final String TYPEOFITEM = "typeOfItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.actionStr</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIONSTR = "actionStr";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.workflow</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOW = "workflow";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowItemAttachment.actions</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIONS = "actions";
	
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowItemAttachment.code</code> attribute defined at extension <code>workflow</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowItemAttachment.comment</code> attribute defined at extension <code>workflow</code>. */
	private String _comment;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowItemAttachment.item</code> attribute defined at extension <code>workflow</code>. */
	private ItemModel _item;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowItemAttachment.typeOfItem</code> attribute defined at extension <code>workflow</code>. */
	private ComposedTypeModel _typeOfItem;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowItemAttachment.actionStr</code> attribute defined at extension <code>workflow</code>. */
	private String _actionStr;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowItemAttachment.workflow</code> attribute defined at extension <code>workflow</code>. */
	private WorkflowModel _workflow;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowItemAttachment.actions</code> attribute defined at extension <code>workflow</code>. */
	private Collection<WorkflowActionModel> _actions;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowItemAttachmentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowItemAttachmentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _item initial attribute declared by type <code>WorkflowItemAttachment</code> at extension <code>workflow</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowItemAttachment</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowItemAttachmentModel(final ItemModel _item, final WorkflowModel _workflow)
	{
		super();
		setItem(_item);
		setWorkflow(_workflow);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>WorkflowItemAttachment</code> at extension <code>workflow</code>
	 * @param _item initial attribute declared by type <code>WorkflowItemAttachment</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowItemAttachment</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowItemAttachmentModel(final String _code, final ItemModel _item, final ItemModel _owner, final WorkflowModel _workflow)
	{
		super();
		setCode(_code);
		setItem(_item);
		setOwner(_owner);
		setWorkflow(_workflow);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.actions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the actions - part of WorkflowActionItemAttachmentRelation; references specific actions of referenced workflow for which attachment is relevant for processing
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
	public Collection<WorkflowActionModel> getActions()
	{
		if (this._actions!=null)
		{
			return _actions;
		}
		return _actions = getPersistenceContext().getValue(ACTIONS, _actions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.actionStr</code> attribute defined at extension <code>workflow</code>. 
	 * @return the actionStr
	 */
	@Accessor(qualifier = "actionStr", type = Accessor.Type.GETTER)
	public String getActionStr()
	{
		if (this._actionStr!=null)
		{
			return _actionStr;
		}
		return _actionStr = getPersistenceContext().getValue(ACTIONSTR, _actionStr);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.code</code> attribute defined at extension <code>workflow</code>. 
	 * @return the code - identifier of this attachment; will be generated if not set
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
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.comment</code> attribute defined at extension <code>workflow</code>. 
	 * @return the comment - a comment containing some notes either by creator or processor
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
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.item</code> attribute defined at extension <code>workflow</code>. 
	 * @return the item - the item this attachment references
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
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.name</code> attribute defined at extension <code>workflow</code>. 
	 * @return the name - name of the attachment
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.name</code> attribute defined at extension <code>workflow</code>. 
	 * @param loc the value localization key 
	 * @return the name - name of the attachment
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.typeOfItem</code> attribute defined at extension <code>workflow</code>. 
	 * @return the typeOfItem
	 */
	@Accessor(qualifier = "typeOfItem", type = Accessor.Type.GETTER)
	public ComposedTypeModel getTypeOfItem()
	{
		if (this._typeOfItem!=null)
		{
			return _typeOfItem;
		}
		return _typeOfItem = getPersistenceContext().getValue(TYPEOFITEM, _typeOfItem);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowItemAttachment.workflow</code> attribute defined at extension <code>workflow</code>. 
	 * @return the workflow - 1-part of WorkflowItemAttachmentRelation; references the related workflow this attachment belongs to
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.GETTER)
	public WorkflowModel getWorkflow()
	{
		if (this._workflow!=null)
		{
			return _workflow;
		}
		return _workflow = getPersistenceContext().getValue(WORKFLOW, _workflow);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowItemAttachment.actions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the actions - part of WorkflowActionItemAttachmentRelation; references specific actions of referenced workflow for which attachment is relevant for processing
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
	public void setActions(final Collection<WorkflowActionModel> value)
	{
		_actions = getPersistenceContext().setValue(ACTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>WorkflowItemAttachment.code</code> attribute defined at extension <code>workflow</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - identifier of this attachment; will be generated if not set
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowItemAttachment.comment</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the comment - a comment containing some notes either by creator or processor
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final String value)
	{
		_comment = getPersistenceContext().setValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowItemAttachment.item</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the item - the item this attachment references
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.SETTER)
	public void setItem(final ItemModel value)
	{
		_item = getPersistenceContext().setValue(ITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowItemAttachment.name</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the name - name of the attachment
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowItemAttachment.name</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the name - name of the attachment
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>WorkflowItemAttachment.workflow</code> attribute defined at extension <code>workflow</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the workflow - 1-part of WorkflowItemAttachmentRelation; references the related workflow this attachment belongs to
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.SETTER)
	public void setWorkflow(final WorkflowModel value)
	{
		_workflow = getPersistenceContext().setValue(WORKFLOW, value);
	}
	
}
