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
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type WorkflowTemplate first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowTemplateModel extends JobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "WorkflowTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowTemplate.name</code> attribute defined at extension <code>workflow</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowTemplate.description</code> attribute defined at extension <code>workflow</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowTemplate.activationScript</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIVATIONSCRIPT = "activationScript";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowTemplate.actions</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIONS = "actions";
	
	/** <i>Generated constant</i> - Attribute key of <code>WorkflowTemplate.visibleForPrincipals</code> attribute defined at extension <code>workflow</code>. */
	public static final String VISIBLEFORPRINCIPALS = "visibleForPrincipals";
	
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowTemplate.activationScript</code> attribute defined at extension <code>workflow</code>. */
	private String _activationScript;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowTemplate.actions</code> attribute defined at extension <code>workflow</code>. */
	private List<WorkflowActionTemplateModel> _actions;
	
	/** <i>Generated variable</i> - Variable of <code>WorkflowTemplate.visibleForPrincipals</code> attribute defined at extension <code>workflow</code>. */
	private Collection<PrincipalModel> _visibleForPrincipals;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>WorkflowTemplate</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowTemplateModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>WorkflowTemplate</code> at extension <code>workflow</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>WorkflowTemplate</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowTemplateModel(final String _code, final Integer _nodeID, final UserModel _owner)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowTemplate.actions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the actions - n-part of the WorkflowTemplateActionTemplateRelation; set of action templates from which a action will be created and set to the workflow created by the workflow template each
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
	public List<WorkflowActionTemplateModel> getActions()
	{
		if (this._actions!=null)
		{
			return _actions;
		}
		return _actions = getPersistenceContext().getValue(ACTIONS, _actions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowTemplate.activationScript</code> attribute defined at extension <code>workflow</code>. 
	 * @return the activationScript - Java code used to automatically trigger a Workflow
	 */
	@Accessor(qualifier = "activationScript", type = Accessor.Type.GETTER)
	public String getActivationScript()
	{
		if (this._activationScript!=null)
		{
			return _activationScript;
		}
		return _activationScript = getPersistenceContext().getValue(ACTIVATIONSCRIPT, _activationScript);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowTemplate.description</code> attribute defined at extension <code>workflow</code>. 
	 * @return the description - description of the workflows created by the template; will be copied to a created workflow initially
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowTemplate.description</code> attribute defined at extension <code>workflow</code>. 
	 * @param loc the value localization key 
	 * @return the description - description of the workflows created by the template; will be copied to a created workflow initially
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowTemplate.name</code> attribute defined at extension <code>workflow</code>. 
	 * @return the name - name of the workflows created by the template; will be copied to a created workflow initially
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowTemplate.name</code> attribute defined at extension <code>workflow</code>. 
	 * @param loc the value localization key 
	 * @return the name - name of the workflows created by the template; will be copied to a created workflow initially
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.owner</code> attribute defined at extension <code>core</code> and redeclared at extension <code>workflow</code>. 
	 * @return the owner - responsible user for this template
	 */
	@Override
	@Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
	public UserModel getOwner()
	{
		return (UserModel) super.getOwner();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WorkflowTemplate.visibleForPrincipals</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the visibleForPrincipals
	 */
	@Accessor(qualifier = "visibleForPrincipals", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getVisibleForPrincipals()
	{
		if (this._visibleForPrincipals!=null)
		{
			return _visibleForPrincipals;
		}
		return _visibleForPrincipals = getPersistenceContext().getValue(VISIBLEFORPRINCIPALS, _visibleForPrincipals);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowTemplate.actions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the actions - n-part of the WorkflowTemplateActionTemplateRelation; set of action templates from which a action will be created and set to the workflow created by the workflow template each
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
	public void setActions(final List<WorkflowActionTemplateModel> value)
	{
		_actions = getPersistenceContext().setValue(ACTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowTemplate.activationScript</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the activationScript - Java code used to automatically trigger a Workflow
	 */
	@Accessor(qualifier = "activationScript", type = Accessor.Type.SETTER)
	public void setActivationScript(final String value)
	{
		_activationScript = getPersistenceContext().setValue(ACTIVATIONSCRIPT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowTemplate.description</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the description - description of the workflows created by the template; will be copied to a created workflow initially
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowTemplate.description</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the description - description of the workflows created by the template; will be copied to a created workflow initially
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowTemplate.name</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the name - name of the workflows created by the template; will be copied to a created workflow initially
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowTemplate.name</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the name - name of the workflows created by the template; will be copied to a created workflow initially
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Item.owner</code> attribute defined at extension <code>core</code> and redeclared at extension <code>workflow</code>. Will only accept values of type {@link de.hybris.platform.core.model.user.UserModel}. 
	 *  
	 * @param value the owner - responsible user for this template
	 */
	@Override
	@Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
	public void setOwner(final ItemModel value)
	{
		super.setOwner(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WorkflowTemplate.visibleForPrincipals</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the visibleForPrincipals
	 */
	@Accessor(qualifier = "visibleForPrincipals", type = Accessor.Type.SETTER)
	public void setVisibleForPrincipals(final Collection<PrincipalModel> value)
	{
		_visibleForPrincipals = getPersistenceContext().setValue(VISIBLEFORPRINCIPALS, value);
	}
	
}
