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
package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type WorkflowAction first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowActionModel extends AbstractWorkflowActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "WorkflowAction";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionOrderingRelation</code> defining source attribute <code>predecessors</code> in extension <code>workflow</code>.</i>*/
	public final static String _WORKFLOWACTIONORDERINGRELATION = "WorkflowActionOrderingRelation";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionRelation</code> defining source attribute <code>workflow</code> in extension <code>workflow</code>.</i>*/
	public final static String _WORKFLOWACTIONRELATION = "WorkflowActionRelation";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionLinkRelation</code> defining source attribute <code>incomingDecisions</code> in extension <code>workflow</code>.</i>*/
	public final static String _WORKFLOWACTIONLINKRELATION = "WorkflowActionLinkRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.incomingLinks</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGLINKS = "incomingLinks";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.incomingLinksStr</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGLINKSSTR = "incomingLinksStr";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.selectedDecision</code> attribute defined at extension <code>workflow</code>. */
	public static final String SELECTEDDECISION = "selectedDecision";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.firstActivated</code> attribute defined at extension <code>workflow</code>. */
	public static final String FIRSTACTIVATED = "firstActivated";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.activated</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIVATED = "activated";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.comment</code> attribute defined at extension <code>workflow</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.status</code> attribute defined at extension <code>workflow</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.template</code> attribute defined at extension <code>workflow</code>. */
	public static final String TEMPLATE = "template";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.attachmentItems</code> attribute defined at extension <code>workflow</code>. */
	public static final String ATTACHMENTITEMS = "attachmentItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.workflow</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOW = "workflow";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.decisions</code> attribute defined at extension <code>workflow</code>. */
	public static final String DECISIONS = "decisions";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.incomingDecisions</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGDECISIONS = "incomingDecisions";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowAction.attachments</code> attribute defined at extension <code>workflow</code>. */
	public static final String ATTACHMENTS = "attachments";
	
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.incomingLinks</code> attribute defined at extension <code>workflow</code>. */
	private List<LinkModel> _incomingLinks;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.incomingLinksStr</code> attribute defined at extension <code>workflow</code>. */
	private String _incomingLinksStr;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.selectedDecision</code> attribute defined at extension <code>workflow</code>. */
	private WorkflowDecisionModel _selectedDecision;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.firstActivated</code> attribute defined at extension <code>workflow</code>. */
	private Date _firstActivated;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.activated</code> attribute defined at extension <code>workflow</code>. */
	private Date _activated;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.comment</code> attribute defined at extension <code>workflow</code>. */
	private String _comment;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.status</code> attribute defined at extension <code>workflow</code>. */
	private WorkflowActionStatus _status;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.template</code> attribute defined at extension <code>workflow</code>. */
	private WorkflowActionTemplateModel _template;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.attachmentItems</code> attribute defined at extension <code>workflow</code>. */
	private List<ItemModel> _attachmentItems;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.workflow</code> attribute defined at extension <code>workflow</code>. */
	private WorkflowModel _workflow;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.decisions</code> attribute defined at extension <code>workflow</code>. */
	private Collection<WorkflowDecisionModel> _decisions;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.incomingDecisions</code> attribute defined at extension <code>workflow</code>. */
	private Collection<WorkflowDecisionModel> _incomingDecisions;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowAction.attachments</code> attribute defined at extension <code>workflow</code>. */
	private List<WorkflowItemAttachmentModel> _attachments;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _actionType initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _principalAssigned initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _template initial attribute declared by type <code>WorkflowAction</code> at extension <code>workflow</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowAction</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionModel(final WorkflowActionType _actionType, final PrincipalModel _principalAssigned, final WorkflowActionTemplateModel _template, final WorkflowModel _workflow)
	{
		super();
		setActionType(_actionType);
		setPrincipalAssigned(_principalAssigned);
		setTemplate(_template);
		setWorkflow(_workflow);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _actionType initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _code initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _principalAssigned initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _template initial attribute declared by type <code>WorkflowAction</code> at extension <code>workflow</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowAction</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionModel(final WorkflowActionType _actionType, final String _code, final UserModel _owner, final PrincipalModel _principalAssigned, final WorkflowActionTemplateModel _template, final WorkflowModel _workflow)
	{
		super();
		setActionType(_actionType);
		setCode(_code);
		setOwner(_owner);
		setPrincipalAssigned(_principalAssigned);
		setTemplate(_template);
		setWorkflow(_workflow);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.activated</code> attribute defined at extension <code>workflow</code>. 
	 * @return the activated - date of last activation
	 */
	@Accessor(qualifier = "activated", type = Accessor.Type.GETTER)
	public Date getActivated()
	{
		if (this._activated!=null)
		{
			return _activated;
		}
		return _activated = getPersistenceContext().getValue(ACTIVATED, _activated);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.attachmentItems</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the attachmentItems
	 */
	@Accessor(qualifier = "attachmentItems", type = Accessor.Type.GETTER)
	public List<ItemModel> getAttachmentItems()
	{
		if (this._attachmentItems!=null)
		{
			return _attachmentItems;
		}
		return _attachmentItems = getPersistenceContext().getValue(ATTACHMENTITEMS, _attachmentItems);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.attachments</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the attachments - part of the WorkflowActionItemAttachmentRelation; associates a set of attachments set to the related workflow of this action
	 */
	@Accessor(qualifier = "attachments", type = Accessor.Type.GETTER)
	public List<WorkflowItemAttachmentModel> getAttachments()
	{
		if (this._attachments!=null)
		{
			return _attachments;
		}
		return _attachments = getPersistenceContext().getValue(ATTACHMENTS, _attachments);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.comment</code> attribute defined at extension <code>workflow</code>. 
	 * @return the comment - comment of the assigned principal on the status of the action
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
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.decisions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the decisions - set of all possible decisions of this action
	 */
	@Accessor(qualifier = "decisions", type = Accessor.Type.GETTER)
	public Collection<WorkflowDecisionModel> getDecisions()
	{
		if (this._decisions!=null)
		{
			return _decisions;
		}
		return _decisions = getPersistenceContext().getValue(DECISIONS, _decisions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.firstActivated</code> attribute defined at extension <code>workflow</code>. 
	 * @return the firstActivated - date of first activation of the action (in case of a rejected action an action can be activated twice for example)
	 */
	@Accessor(qualifier = "firstActivated", type = Accessor.Type.GETTER)
	public Date getFirstActivated()
	{
		if (this._firstActivated!=null)
		{
			return _firstActivated;
		}
		return _firstActivated = getPersistenceContext().getValue(FIRSTACTIVATED, _firstActivated);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.incomingDecisions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the incomingDecisions
	 */
	@Accessor(qualifier = "incomingDecisions", type = Accessor.Type.GETTER)
	public Collection<WorkflowDecisionModel> getIncomingDecisions()
	{
		if (this._incomingDecisions!=null)
		{
			return _incomingDecisions;
		}
		return _incomingDecisions = getPersistenceContext().getValue(INCOMINGDECISIONS, _incomingDecisions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.incomingLinks</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the incomingLinks
	 */
	@Accessor(qualifier = "incomingLinks", type = Accessor.Type.GETTER)
	public List<LinkModel> getIncomingLinks()
	{
		if (this._incomingLinks!=null)
		{
			return _incomingLinks;
		}
		return _incomingLinks = getPersistenceContext().getValue(INCOMINGLINKS, _incomingLinks);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.incomingLinksStr</code> attribute defined at extension <code>workflow</code>. 
	 * @return the incomingLinksStr
	 */
	@Accessor(qualifier = "incomingLinksStr", type = Accessor.Type.GETTER)
	public String getIncomingLinksStr()
	{
		if (this._incomingLinksStr!=null)
		{
			return _incomingLinksStr;
		}
		return _incomingLinksStr = getPersistenceContext().getValue(INCOMINGLINKSSTR, _incomingLinksStr);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.selectedDecision</code> attribute defined at extension <code>workflow</code>. 
	 * @return the selectedDecision - the decision chosen when the action is processed
	 */
	@Accessor(qualifier = "selectedDecision", type = Accessor.Type.GETTER)
	public WorkflowDecisionModel getSelectedDecision()
	{
		if (this._selectedDecision!=null)
		{
			return _selectedDecision;
		}
		return _selectedDecision = getPersistenceContext().getValue(SELECTEDDECISION, _selectedDecision);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.status</code> attribute defined at extension <code>workflow</code>. 
	 * @return the status - the status of the action (pending, active, completed)
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public WorkflowActionStatus getStatus()
	{
		if (this._status!=null)
		{
			return _status;
		}
		return _status = getPersistenceContext().getValue(STATUS, _status);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.template</code> attribute defined at extension <code>workflow</code>. 
	 * @return the template - the action template this action was created by; template defines the perform method
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.GETTER)
	public WorkflowActionTemplateModel getTemplate()
	{
		if (this._template!=null)
		{
			return _template;
		}
		return _template = getPersistenceContext().getValue(TEMPLATE, _template);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowAction.workflow</code> attribute defined at extension <code>workflow</code>. 
	 * @return the workflow - workflow to which the action belongs
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
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.activated</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the activated - date of last activation
	 */
	@Accessor(qualifier = "activated", type = Accessor.Type.SETTER)
	public void setActivated(final Date value)
	{
		_activated = getPersistenceContext().setValue(ACTIVATED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.attachments</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the attachments - part of the WorkflowActionItemAttachmentRelation; associates a set of attachments set to the related workflow of this action
	 */
	@Accessor(qualifier = "attachments", type = Accessor.Type.SETTER)
	public void setAttachments(final List<WorkflowItemAttachmentModel> value)
	{
		_attachments = getPersistenceContext().setValue(ATTACHMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.comment</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the comment - comment of the assigned principal on the status of the action
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final String value)
	{
		_comment = getPersistenceContext().setValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.decisions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the decisions - set of all possible decisions of this action
	 */
	@Accessor(qualifier = "decisions", type = Accessor.Type.SETTER)
	public void setDecisions(final Collection<WorkflowDecisionModel> value)
	{
		_decisions = getPersistenceContext().setValue(DECISIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.firstActivated</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the firstActivated - date of first activation of the action (in case of a rejected action an action can be activated twice for example)
	 */
	@Accessor(qualifier = "firstActivated", type = Accessor.Type.SETTER)
	public void setFirstActivated(final Date value)
	{
		_firstActivated = getPersistenceContext().setValue(FIRSTACTIVATED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.incomingDecisions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the incomingDecisions
	 */
	@Accessor(qualifier = "incomingDecisions", type = Accessor.Type.SETTER)
	public void setIncomingDecisions(final Collection<WorkflowDecisionModel> value)
	{
		_incomingDecisions = getPersistenceContext().setValue(INCOMINGDECISIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.selectedDecision</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the selectedDecision - the decision chosen when the action is processed
	 */
	@Accessor(qualifier = "selectedDecision", type = Accessor.Type.SETTER)
	public void setSelectedDecision(final WorkflowDecisionModel value)
	{
		_selectedDecision = getPersistenceContext().setValue(SELECTEDDECISION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowAction.status</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the status - the status of the action (pending, active, completed)
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final WorkflowActionStatus value)
	{
		_status = getPersistenceContext().setValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>WorkflowAction.template</code> attribute defined at extension <code>workflow</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the template - the action template this action was created by; template defines the perform method
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.SETTER)
	public void setTemplate(final WorkflowActionTemplateModel value)
	{
		_template = getPersistenceContext().setValue(TEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>WorkflowAction.workflow</code> attribute defined at extension <code>workflow</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the workflow - workflow to which the action belongs
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.SETTER)
	public void setWorkflow(final WorkflowModel value)
	{
		_workflow = getPersistenceContext().setValue(WORKFLOW, value);
	}
	
}
