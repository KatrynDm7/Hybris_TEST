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
package de.hybris.platform.servicelayer.internal.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ScriptingJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class ScriptingJobModel extends ServicelayerJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ScriptingJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ScriptingJob.scriptURI</code> attribute defined at extension <code>processing</code>. */
	public static final String SCRIPTURI = "scriptURI";
	
	
	/** <i>Generated variable</i> - Variable of <code>ScriptingJob.scriptURI</code> attribute defined at extension <code>processing</code>. */
	private String _scriptURI;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ScriptingJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ScriptingJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _scriptURI initial attribute declared by type <code>ScriptingJob</code> at extension <code>processing</code>
	 * @param _springId initial attribute declared by type <code>ScriptingJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ScriptingJobModel(final String _code, final String _scriptURI, final String _springId)
	{
		super();
		setCode(_code);
		setScriptURI(_scriptURI);
		setSpringId(_springId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _scriptURI initial attribute declared by type <code>ScriptingJob</code> at extension <code>processing</code>
	 * @param _springId initial attribute declared by type <code>ScriptingJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ScriptingJobModel(final String _code, final Integer _nodeID, final ItemModel _owner, final String _scriptURI, final String _springId)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
		setScriptURI(_scriptURI);
		setSpringId(_springId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ScriptingJob.scriptURI</code> attribute defined at extension <code>processing</code>. 
	 * @return the scriptURI
	 */
	@Accessor(qualifier = "scriptURI", type = Accessor.Type.GETTER)
	public String getScriptURI()
	{
		if (this._scriptURI!=null)
		{
			return _scriptURI;
		}
		return _scriptURI = getPersistenceContext().getValue(SCRIPTURI, _scriptURI);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ScriptingJob.scriptURI</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the scriptURI
	 */
	@Accessor(qualifier = "scriptURI", type = Accessor.Type.SETTER)
	public void setScriptURI(final String value)
	{
		_scriptURI = getPersistenceContext().setValue(SCRIPTURI, value);
	}
	
}
