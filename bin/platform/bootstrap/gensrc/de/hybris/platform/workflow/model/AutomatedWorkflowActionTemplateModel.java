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
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

/**
 * Generated model class for type AutomatedWorkflowActionTemplate first defined at extension workflow.
 */
@SuppressWarnings("all")
public class AutomatedWorkflowActionTemplateModel extends WorkflowActionTemplateModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AutomatedWorkflowActionTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AutomatedWorkflowActionTemplate.jobClass</code> attribute defined at extension <code>workflow</code>. */
	public static final String JOBCLASS = "jobClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>AutomatedWorkflowActionTemplate.jobHandler</code> attribute defined at extension <code>workflow</code>. */
	public static final String JOBHANDLER = "jobHandler";
	
	
	/** <i>Generated variable</i> - Variable of <code>AutomatedWorkflowActionTemplate.jobClass</code> attribute defined at extension <code>workflow</code>. */
	private Class _jobClass;
	
	/** <i>Generated variable</i> - Variable of <code>AutomatedWorkflowActionTemplate.jobHandler</code> attribute defined at extension <code>workflow</code>. */
	private String _jobHandler;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AutomatedWorkflowActionTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AutomatedWorkflowActionTemplateModel(final ItemModelContext ctx)
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
	public AutomatedWorkflowActionTemplateModel(final WorkflowActionType _actionType, final PrincipalModel _principalAssigned, final WorkflowTemplateModel _workflow)
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
	public AutomatedWorkflowActionTemplateModel(final WorkflowActionType _actionType, final String _code, final UserModel _owner, final PrincipalModel _principalAssigned, final WorkflowTemplateModel _workflow)
	{
		super();
		setActionType(_actionType);
		setCode(_code);
		setOwner(_owner);
		setPrincipalAssigned(_principalAssigned);
		setWorkflow(_workflow);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AutomatedWorkflowActionTemplate.jobClass</code> attribute defined at extension <code>workflow</code>. 
	 * @return the jobClass - class of the automated action which must implement AutomatedWorkflowTemplateJob
	 */
	@Deprecated
	@Accessor(qualifier = "jobClass", type = Accessor.Type.GETTER)
	public Class getJobClass()
	{
		if (this._jobClass!=null)
		{
			return _jobClass;
		}
		return _jobClass = getPersistenceContext().getValue(JOBCLASS, _jobClass);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AutomatedWorkflowActionTemplate.jobHandler</code> attribute defined at extension <code>workflow</code>. 
	 * @return the jobHandler - Spring bean ID of automated action
	 */
	@Accessor(qualifier = "jobHandler", type = Accessor.Type.GETTER)
	public String getJobHandler()
	{
		if (this._jobHandler!=null)
		{
			return _jobHandler;
		}
		return _jobHandler = getPersistenceContext().getValue(JOBHANDLER, _jobHandler);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AutomatedWorkflowActionTemplate.jobClass</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the jobClass - class of the automated action which must implement AutomatedWorkflowTemplateJob
	 */
	@Deprecated
	@Accessor(qualifier = "jobClass", type = Accessor.Type.SETTER)
	public void setJobClass(final Class value)
	{
		_jobClass = getPersistenceContext().setValue(JOBCLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AutomatedWorkflowActionTemplate.jobHandler</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the jobHandler - Spring bean ID of automated action
	 */
	@Accessor(qualifier = "jobHandler", type = Accessor.Type.SETTER)
	public void setJobHandler(final String value)
	{
		_jobHandler = getPersistenceContext().setValue(JOBHANDLER, value);
	}
	
}
