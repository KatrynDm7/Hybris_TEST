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
package de.hybris.platform.processengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type ProcessTaskLog first defined at extension processing.
 */
@SuppressWarnings("all")
public class ProcessTaskLogModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ProcessTaskLog";
	
	/**<i>Generated relation code constant for relation <code>Process2TaskLogRelation</code> defining source attribute <code>process</code> in extension <code>processing</code>.</i>*/
	public final static String _PROCESS2TASKLOGRELATION = "Process2TaskLogRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.returnCode</code> attribute defined at extension <code>processing</code>. */
	public static final String RETURNCODE = "returnCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.startDate</code> attribute defined at extension <code>processing</code>. */
	public static final String STARTDATE = "startDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.endDate</code> attribute defined at extension <code>processing</code>. */
	public static final String ENDDATE = "endDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.actionId</code> attribute defined at extension <code>processing</code>. */
	public static final String ACTIONID = "actionId";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.clusterId</code> attribute defined at extension <code>processing</code>. */
	public static final String CLUSTERID = "clusterId";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.logMessages</code> attribute defined at extension <code>processing</code>. */
	public static final String LOGMESSAGES = "logMessages";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProcessTaskLog.process</code> attribute defined at extension <code>processing</code>. */
	public static final String PROCESS = "process";
	
	
	/** <i>Generated variable</i> - Variable of <code>ProcessTaskLog.returnCode</code> attribute defined at extension <code>processing</code>. */
	private String _returnCode;
	
	/** <i>Generated variable</i> - Variable of <code>ProcessTaskLog.startDate</code> attribute defined at extension <code>processing</code>. */
	private Date _startDate;
	
	/** <i>Generated variable</i> - Variable of <code>ProcessTaskLog.endDate</code> attribute defined at extension <code>processing</code>. */
	private Date _endDate;
	
	/** <i>Generated variable</i> - Variable of <code>ProcessTaskLog.actionId</code> attribute defined at extension <code>processing</code>. */
	private String _actionId;
	
	/** <i>Generated variable</i> - Variable of <code>ProcessTaskLog.clusterId</code> attribute defined at extension <code>processing</code>. */
	private Integer _clusterId;
	
	/** <i>Generated variable</i> - Variable of <code>ProcessTaskLog.logMessages</code> attribute defined at extension <code>processing</code>. */
	private String _logMessages;
	
	/** <i>Generated variable</i> - Variable of <code>ProcessTaskLog.process</code> attribute defined at extension <code>processing</code>. */
	private BusinessProcessModel _process;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProcessTaskLogModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProcessTaskLogModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _actionId initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 * @param _clusterId initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 * @param _process initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ProcessTaskLogModel(final String _actionId, final Integer _clusterId, final BusinessProcessModel _process)
	{
		super();
		setActionId(_actionId);
		setClusterId(_clusterId);
		setProcess(_process);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _actionId initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 * @param _clusterId initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _process initial attribute declared by type <code>ProcessTaskLog</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ProcessTaskLogModel(final String _actionId, final Integer _clusterId, final ItemModel _owner, final BusinessProcessModel _process)
	{
		super();
		setActionId(_actionId);
		setClusterId(_clusterId);
		setOwner(_owner);
		setProcess(_process);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.actionId</code> attribute defined at extension <code>processing</code>. 
	 * @return the actionId - ID of the action performed
	 */
	@Accessor(qualifier = "actionId", type = Accessor.Type.GETTER)
	public String getActionId()
	{
		if (this._actionId!=null)
		{
			return _actionId;
		}
		return _actionId = getPersistenceContext().getValue(ACTIONID, _actionId);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.clusterId</code> attribute defined at extension <code>processing</code>. 
	 * @return the clusterId - ID of the cluster where action performed
	 */
	@Accessor(qualifier = "clusterId", type = Accessor.Type.GETTER)
	public Integer getClusterId()
	{
		if (this._clusterId!=null)
		{
			return _clusterId;
		}
		return _clusterId = getPersistenceContext().getValue(CLUSTERID, _clusterId);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.endDate</code> attribute defined at extension <code>processing</code>. 
	 * @return the endDate - Date when task was ended
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
	public Date getEndDate()
	{
		if (this._endDate!=null)
		{
			return _endDate;
		}
		return _endDate = getPersistenceContext().getValue(ENDDATE, _endDate);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.logMessages</code> attribute defined at extension <code>processing</code>. 
	 * @return the logMessages - Messages given during the process.
	 */
	@Accessor(qualifier = "logMessages", type = Accessor.Type.GETTER)
	public String getLogMessages()
	{
		if (this._logMessages!=null)
		{
			return _logMessages;
		}
		return _logMessages = getPersistenceContext().getValue(LOGMESSAGES, _logMessages);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.process</code> attribute defined at extension <code>processing</code>. 
	 * @return the process
	 */
	@Accessor(qualifier = "process", type = Accessor.Type.GETTER)
	public BusinessProcessModel getProcess()
	{
		if (this._process!=null)
		{
			return _process;
		}
		return _process = getPersistenceContext().getValue(PROCESS, _process);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.returnCode</code> attribute defined at extension <code>processing</code>. 
	 * @return the returnCode - Return code of the task.
	 */
	@Accessor(qualifier = "returnCode", type = Accessor.Type.GETTER)
	public String getReturnCode()
	{
		if (this._returnCode!=null)
		{
			return _returnCode;
		}
		return _returnCode = getPersistenceContext().getValue(RETURNCODE, _returnCode);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProcessTaskLog.startDate</code> attribute defined at extension <code>processing</code>. 
	 * @return the startDate - Date when task was started
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
	public Date getStartDate()
	{
		if (this._startDate!=null)
		{
			return _startDate;
		}
		return _startDate = getPersistenceContext().getValue(STARTDATE, _startDate);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.actionId</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the actionId - ID of the action performed
	 */
	@Accessor(qualifier = "actionId", type = Accessor.Type.SETTER)
	public void setActionId(final String value)
	{
		_actionId = getPersistenceContext().setValue(ACTIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.clusterId</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the clusterId - ID of the cluster where action performed
	 */
	@Accessor(qualifier = "clusterId", type = Accessor.Type.SETTER)
	public void setClusterId(final Integer value)
	{
		_clusterId = getPersistenceContext().setValue(CLUSTERID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.endDate</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the endDate - Date when task was ended
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
	public void setEndDate(final Date value)
	{
		_endDate = getPersistenceContext().setValue(ENDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.logMessages</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the logMessages - Messages given during the process.
	 */
	@Accessor(qualifier = "logMessages", type = Accessor.Type.SETTER)
	public void setLogMessages(final String value)
	{
		_logMessages = getPersistenceContext().setValue(LOGMESSAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.process</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the process
	 */
	@Accessor(qualifier = "process", type = Accessor.Type.SETTER)
	public void setProcess(final BusinessProcessModel value)
	{
		_process = getPersistenceContext().setValue(PROCESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.returnCode</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the returnCode - Return code of the task.
	 */
	@Accessor(qualifier = "returnCode", type = Accessor.Type.SETTER)
	public void setReturnCode(final String value)
	{
		_returnCode = getPersistenceContext().setValue(RETURNCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProcessTaskLog.startDate</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the startDate - Date when task was started
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
	public void setStartDate(final Date value)
	{
		_startDate = getPersistenceContext().setValue(STARTDATE, value);
	}
	
}
