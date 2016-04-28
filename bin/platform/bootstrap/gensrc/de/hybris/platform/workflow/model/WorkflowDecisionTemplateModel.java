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
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;

/**
 * Generated model class for type WorkflowDecisionTemplate first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowDecisionTemplateModel extends AbstractWorkflowDecisionModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "WorkflowDecisionTemplate";
	
	/**<i>Generated relation code constant for relation <code>WorkflowActionTemplateDecisionsTemplateRelation</code> defining source attribute <code>actionTemplate</code> in extension <code>workflow</code>.</i>*/
	public final static String _WORKFLOWACTIONTEMPLATEDECISIONSTEMPLATERELATION = "WorkflowActionTemplateDecisionsTemplateRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowDecisionTemplate.parentWorkflowTemplate</code> attribute defined at extension <code>workflow</code>. */
	public static final String PARENTWORKFLOWTEMPLATE = "parentWorkflowTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowDecisionTemplate.actionTemplate</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIONTEMPLATE = "actionTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowDecisionTemplate.toTemplateActions</code> attribute defined at extension <code>workflow</code>. */
	public static final String TOTEMPLATEACTIONS = "toTemplateActions";
	
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowDecisionTemplate.parentWorkflowTemplate</code> attribute defined at extension <code>workflow</code>. */
	private WorkflowTemplateModel _parentWorkflowTemplate;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowDecisionTemplate.actionTemplate</code> attribute defined at extension <code>workflow</code>. */
	private WorkflowActionTemplateModel _actionTemplate;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowDecisionTemplate.toTemplateActions</code> attribute defined at extension <code>workflow</code>. */
	private Collection<WorkflowActionTemplateModel> _toTemplateActions;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowDecisionTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowDecisionTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractWorkflowDecision</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public WorkflowDecisionTemplateModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowDecisionTemplate.actionTemplate</code> attribute defined at extension <code>workflow</code>. 
	 * @return the actionTemplate - reference to the action template this decision belongs to
	 */
	@Accessor(qualifier = "actionTemplate", type = Accessor.Type.GETTER)
	public WorkflowActionTemplateModel getActionTemplate()
	{
		if (this._actionTemplate!=null)
		{
			return _actionTemplate;
		}
		return _actionTemplate = getPersistenceContext().getValue(ACTIONTEMPLATE, _actionTemplate);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowDecisionTemplate.parentWorkflowTemplate</code> attribute defined at extension <code>workflow</code>. 
	 * @return the parentWorkflowTemplate
	 */
	@Accessor(qualifier = "parentWorkflowTemplate", type = Accessor.Type.GETTER)
	public WorkflowTemplateModel getParentWorkflowTemplate()
	{
		if (this._parentWorkflowTemplate!=null)
		{
			return _parentWorkflowTemplate;
		}
		return _parentWorkflowTemplate = getPersistenceContext().getValue(PARENTWORKFLOWTEMPLATE, _parentWorkflowTemplate);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowDecisionTemplate.toTemplateActions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the toTemplateActions - list of actions that will be activated then the decision gets chosen
	 */
	@Accessor(qualifier = "toTemplateActions", type = Accessor.Type.GETTER)
	public Collection<WorkflowActionTemplateModel> getToTemplateActions()
	{
		if (this._toTemplateActions!=null)
		{
			return _toTemplateActions;
		}
		return _toTemplateActions = getPersistenceContext().getValue(TOTEMPLATEACTIONS, _toTemplateActions);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowDecisionTemplate.actionTemplate</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the actionTemplate - reference to the action template this decision belongs to
	 */
	@Accessor(qualifier = "actionTemplate", type = Accessor.Type.SETTER)
	public void setActionTemplate(final WorkflowActionTemplateModel value)
	{
		_actionTemplate = getPersistenceContext().setValue(ACTIONTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowDecisionTemplate.toTemplateActions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the toTemplateActions - list of actions that will be activated then the decision gets chosen
	 */
	@Accessor(qualifier = "toTemplateActions", type = Accessor.Type.SETTER)
	public void setToTemplateActions(final Collection<WorkflowActionTemplateModel> value)
	{
		_toTemplateActions = getPersistenceContext().setValue(TOTEMPLATEACTIONS, value);
	}
	
}
