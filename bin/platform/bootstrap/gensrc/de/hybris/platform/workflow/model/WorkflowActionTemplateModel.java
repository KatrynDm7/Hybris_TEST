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
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type WorkflowActionTemplate first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowActionTemplateModel extends AbstractWorkflowActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "WorkflowActionTemplate";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionOrderingRelation</code> defining source attribute <code>predecessors</code> in extension <code>workflow</code>.</i>*/
	public final static String _WORKFLOWACTIONORDERINGRELATION = "WorkflowActionOrderingRelation";
	
	/**<i>Generated relation code constant for relation <code>WorkflowTemplateActionTemplateRelation</code> defining source attribute <code>workflow</code> in extension <code>workflow</code>.</i>*/
	public final static String _WORKFLOWTEMPLATEACTIONTEMPLATERELATION = "WorkflowTemplateActionTemplateRelation";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionTemplateLinkTemplateRelation</code> defining source attribute <code>incomingTemplateDecisions</code> in extension <code>workflow</code>.</i>*/
	public final static String _WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION = "WorkflowActionTemplateLinkTemplateRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.incomingLinkTemplates</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGLINKTEMPLATES = "incomingLinkTemplates";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.incomingLinkTemplatesStr</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGLINKTEMPLATESSTR = "incomingLinkTemplatesStr";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.creationType</code> attribute defined at extension <code>workflow</code>. */
	public static final String CREATIONTYPE = "creationType";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.workflow</code> attribute defined at extension <code>workflow</code>. */
	public static final String WORKFLOW = "workflow";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.decisionTemplates</code> attribute defined at extension <code>workflow</code>. */
	public static final String DECISIONTEMPLATES = "decisionTemplates";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowActionTemplate.incomingTemplateDecisions</code> attribute defined at extension <code>workflow</code>. */
	public static final String INCOMINGTEMPLATEDECISIONS = "incomingTemplateDecisions";
	
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowActionTemplate.incomingLinkTemplates</code> attribute defined at extension <code>workflow</code>. */
	private List<LinkModel> _incomingLinkTemplates;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowActionTemplate.incomingLinkTemplatesStr</code> attribute defined at extension <code>workflow</code>. */
	private String _incomingLinkTemplatesStr;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowActionTemplate.creationType</code> attribute defined at extension <code>workflow</code>. */
	private ComposedTypeModel _creationType;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowActionTemplate.workflow</code> attribute defined at extension <code>workflow</code>. */
	private WorkflowTemplateModel _workflow;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowActionTemplate.decisionTemplates</code> attribute defined at extension <code>workflow</code>. */
	private Collection<WorkflowDecisionTemplateModel> _decisionTemplates;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowActionTemplate.incomingTemplateDecisions</code> attribute defined at extension <code>workflow</code>. */
	private Collection<WorkflowDecisionTemplateModel> _incomingTemplateDecisions;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowActionTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowActionTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _actionType initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _principalAssigned initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowActionTemplate</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionTemplateModel(final WorkflowActionType _actionType, final PrincipalModel _principalAssigned, final WorkflowTemplateModel _workflow)
	{
		super();
		setActionType(_actionType);
		setPrincipalAssigned(_principalAssigned);
		setWorkflow(_workflow);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _actionType initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _code initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _principalAssigned initial attribute declared by type <code>AbstractWorkflowAction</code> at extension <code>workflow</code>
	 * @param _workflow initial attribute declared by type <code>WorkflowActionTemplate</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowActionTemplateModel(final WorkflowActionType _actionType, final String _code, final UserModel _owner, final PrincipalModel _principalAssigned, final WorkflowTemplateModel _workflow)
	{
		super();
		setActionType(_actionType);
		setCode(_code);
		setOwner(_owner);
		setPrincipalAssigned(_principalAssigned);
		setWorkflow(_workflow);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.creationType</code> attribute defined at extension <code>workflow</code>. 
	 * @return the creationType
	 */
	@Accessor(qualifier = "creationType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getCreationType()
	{
		if (this._creationType!=null)
		{
			return _creationType;
		}
		return _creationType = getPersistenceContext().getValue(CREATIONTYPE, _creationType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.decisionTemplates</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the decisionTemplates - list of all decision templates of the action template
	 */
	@Accessor(qualifier = "decisionTemplates", type = Accessor.Type.GETTER)
	public Collection<WorkflowDecisionTemplateModel> getDecisionTemplates()
	{
		if (this._decisionTemplates!=null)
		{
			return _decisionTemplates;
		}
		return _decisionTemplates = getPersistenceContext().getValue(DECISIONTEMPLATES, _decisionTemplates);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.incomingLinkTemplates</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the incomingLinkTemplates
	 */
	@Accessor(qualifier = "incomingLinkTemplates", type = Accessor.Type.GETTER)
	public List<LinkModel> getIncomingLinkTemplates()
	{
		if (this._incomingLinkTemplates!=null)
		{
			return _incomingLinkTemplates;
		}
		return _incomingLinkTemplates = getPersistenceContext().getValue(INCOMINGLINKTEMPLATES, _incomingLinkTemplates);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.incomingLinkTemplatesStr</code> attribute defined at extension <code>workflow</code>. 
	 * @return the incomingLinkTemplatesStr
	 */
	@Accessor(qualifier = "incomingLinkTemplatesStr", type = Accessor.Type.GETTER)
	public String getIncomingLinkTemplatesStr()
	{
		if (this._incomingLinkTemplatesStr!=null)
		{
			return _incomingLinkTemplatesStr;
		}
		return _incomingLinkTemplatesStr = getPersistenceContext().getValue(INCOMINGLINKTEMPLATESSTR, _incomingLinkTemplatesStr);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.incomingTemplateDecisions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the incomingTemplateDecisions
	 */
	@Accessor(qualifier = "incomingTemplateDecisions", type = Accessor.Type.GETTER)
	public Collection<WorkflowDecisionTemplateModel> getIncomingTemplateDecisions()
	{
		if (this._incomingTemplateDecisions!=null)
		{
			return _incomingTemplateDecisions;
		}
		return _incomingTemplateDecisions = getPersistenceContext().getValue(INCOMINGTEMPLATEDECISIONS, _incomingTemplateDecisions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowActionTemplate.workflow</code> attribute defined at extension <code>workflow</code>. 
	 * @return the workflow - workflow template to which the action template belongs
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.GETTER)
	public WorkflowTemplateModel getWorkflow()
	{
		if (this._workflow!=null)
		{
			return _workflow;
		}
		return _workflow = getPersistenceContext().getValue(WORKFLOW, _workflow);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionTemplate.creationType</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the creationType
	 */
	@Accessor(qualifier = "creationType", type = Accessor.Type.SETTER)
	public void setCreationType(final ComposedTypeModel value)
	{
		_creationType = getPersistenceContext().setValue(CREATIONTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionTemplate.decisionTemplates</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the decisionTemplates - list of all decision templates of the action template
	 */
	@Accessor(qualifier = "decisionTemplates", type = Accessor.Type.SETTER)
	public void setDecisionTemplates(final Collection<WorkflowDecisionTemplateModel> value)
	{
		_decisionTemplates = getPersistenceContext().setValue(DECISIONTEMPLATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowActionTemplate.incomingTemplateDecisions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the incomingTemplateDecisions
	 */
	@Accessor(qualifier = "incomingTemplateDecisions", type = Accessor.Type.SETTER)
	public void setIncomingTemplateDecisions(final Collection<WorkflowDecisionTemplateModel> value)
	{
		_incomingTemplateDecisions = getPersistenceContext().setValue(INCOMINGTEMPLATEDECISIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>WorkflowActionTemplate.workflow</code> attribute defined at extension <code>workflow</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the workflow - workflow template to which the action template belongs
	 */
	@Accessor(qualifier = "workflow", type = Accessor.Type.SETTER)
	public void setWorkflow(final WorkflowTemplateModel value)
	{
		_workflow = getPersistenceContext().setValue(WORKFLOW, value);
	}
	
}
