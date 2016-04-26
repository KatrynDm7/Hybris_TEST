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
package de.hybris.platform.task;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.task.TaskConditionModel;
import java.util.Date;
import java.util.Set;

/**
 * Generated model class for type Task first defined at extension processing.
 */
@SuppressWarnings("all")
public class TaskModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Task";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.runnerBean</code> attribute defined at extension <code>processing</code>. */
	public static final String RUNNERBEAN = "runnerBean";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.executionDate</code> attribute defined at extension <code>processing</code>. */
	public static final String EXECUTIONDATE = "executionDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.executionTimeMillis</code> attribute defined at extension <code>processing</code>. */
	public static final String EXECUTIONTIMEMILLIS = "executionTimeMillis";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.failed</code> attribute defined at extension <code>processing</code>. */
	public static final String FAILED = "failed";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.expirationDate</code> attribute defined at extension <code>processing</code>. */
	public static final String EXPIRATIONDATE = "expirationDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.expirationTimeMillis</code> attribute defined at extension <code>processing</code>. */
	public static final String EXPIRATIONTIMEMILLIS = "expirationTimeMillis";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.context</code> attribute defined at extension <code>processing</code>. */
	public static final String CONTEXT = "context";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.contextItem</code> attribute defined at extension <code>processing</code>. */
	public static final String CONTEXTITEM = "contextItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.nodeId</code> attribute defined at extension <code>processing</code>. */
	public static final String NODEID = "nodeId";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.nodeGroup</code> attribute defined at extension <code>processing</code>. */
	public static final String NODEGROUP = "nodeGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.retry</code> attribute defined at extension <code>processing</code>. */
	public static final String RETRY = "retry";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.runningOnClusterNode</code> attribute defined at extension <code>processing</code>. */
	public static final String RUNNINGONCLUSTERNODE = "runningOnClusterNode";
	
	/** <i>Generated constant</i> - Attribute key of <code>Task.conditions</code> attribute defined at extension <code>processing</code>. */
	public static final String CONDITIONS = "conditions";
	
	
	/** <i>Generated variable</i> - Variable of <code>Task.runnerBean</code> attribute defined at extension <code>processing</code>. */
	private String _runnerBean;
	
	/** <i>Generated variable</i> - Variable of <code>Task.executionDate</code> attribute defined at extension <code>processing</code>. */
	private Date _executionDate;
	
	/** <i>Generated variable</i> - Variable of <code>Task.executionTimeMillis</code> attribute defined at extension <code>processing</code>. */
	private Long _executionTimeMillis;
	
	/** <i>Generated variable</i> - Variable of <code>Task.failed</code> attribute defined at extension <code>processing</code>. */
	private Boolean _failed;
	
	/** <i>Generated variable</i> - Variable of <code>Task.expirationDate</code> attribute defined at extension <code>processing</code>. */
	private Date _expirationDate;
	
	/** <i>Generated variable</i> - Variable of <code>Task.expirationTimeMillis</code> attribute defined at extension <code>processing</code>. */
	private Long _expirationTimeMillis;
	
	/** <i>Generated variable</i> - Variable of <code>Task.context</code> attribute defined at extension <code>processing</code>. */
	private Object _context;
	
	/** <i>Generated variable</i> - Variable of <code>Task.contextItem</code> attribute defined at extension <code>processing</code>. */
	private ItemModel _contextItem;
	
	/** <i>Generated variable</i> - Variable of <code>Task.nodeId</code> attribute defined at extension <code>processing</code>. */
	private Integer _nodeId;
	
	/** <i>Generated variable</i> - Variable of <code>Task.nodeGroup</code> attribute defined at extension <code>processing</code>. */
	private String _nodeGroup;
	
	/** <i>Generated variable</i> - Variable of <code>Task.retry</code> attribute defined at extension <code>processing</code>. */
	private Integer _retry;
	
	/** <i>Generated variable</i> - Variable of <code>Task.runningOnClusterNode</code> attribute defined at extension <code>processing</code>. */
	private Integer _runningOnClusterNode;
	
	/** <i>Generated variable</i> - Variable of <code>Task.conditions</code> attribute defined at extension <code>processing</code>. */
	private Set<TaskConditionModel> _conditions;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TaskModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TaskModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _runnerBean initial attribute declared by type <code>Task</code> at extension <code>processing</code>
	 */
	@Deprecated
	public TaskModel(final String _runnerBean)
	{
		super();
		setRunnerBean(_runnerBean);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _runnerBean initial attribute declared by type <code>Task</code> at extension <code>processing</code>
	 */
	@Deprecated
	public TaskModel(final ItemModel _owner, final String _runnerBean)
	{
		super();
		setOwner(_owner);
		setRunnerBean(_runnerBean);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.conditions</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the conditions
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.GETTER)
	public Set<TaskConditionModel> getConditions()
	{
		if (this._conditions!=null)
		{
			return _conditions;
		}
		return _conditions = getPersistenceContext().getValue(CONDITIONS, _conditions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.context</code> attribute defined at extension <code>processing</code>. 
	 * @return the context
	 */
	@Accessor(qualifier = "context", type = Accessor.Type.GETTER)
	public Object getContext()
	{
		if (this._context!=null)
		{
			return _context;
		}
		return _context = getPersistenceContext().getValue(CONTEXT, _context);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.contextItem</code> attribute defined at extension <code>processing</code>. 
	 * @return the contextItem
	 */
	@Accessor(qualifier = "contextItem", type = Accessor.Type.GETTER)
	public ItemModel getContextItem()
	{
		if (this._contextItem!=null)
		{
			return _contextItem;
		}
		return _contextItem = getPersistenceContext().getValue(CONTEXTITEM, _contextItem);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.executionDate</code> dynamic attribute defined at extension <code>processing</code>. 
	 * @return the executionDate - Date after this task is to be executed
	 */
	@Accessor(qualifier = "executionDate", type = Accessor.Type.GETTER)
	public Date getExecutionDate()
	{
		return getPersistenceContext().getDynamicValue(this,EXECUTIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.executionTimeMillis</code> attribute defined at extension <code>processing</code>. 
	 * @return the executionTimeMillis - Internal representation to overcome database limitations!
	 */
	@Accessor(qualifier = "executionTimeMillis", type = Accessor.Type.GETTER)
	public Long getExecutionTimeMillis()
	{
		if (this._executionTimeMillis!=null)
		{
			return _executionTimeMillis;
		}
		return _executionTimeMillis = getPersistenceContext().getValue(EXECUTIONTIMEMILLIS, _executionTimeMillis);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.expirationDate</code> dynamic attribute defined at extension <code>processing</code>. 
	 * @return the expirationDate
	 */
	@Accessor(qualifier = "expirationDate", type = Accessor.Type.GETTER)
	public Date getExpirationDate()
	{
		return getPersistenceContext().getDynamicValue(this,EXPIRATIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.expirationTimeMillis</code> attribute defined at extension <code>processing</code>. 
	 * @return the expirationTimeMillis - Date when this task is to be executed
	 */
	@Accessor(qualifier = "expirationTimeMillis", type = Accessor.Type.GETTER)
	public Long getExpirationTimeMillis()
	{
		if (this._expirationTimeMillis!=null)
		{
			return _expirationTimeMillis;
		}
		return _expirationTimeMillis = getPersistenceContext().getValue(EXPIRATIONTIMEMILLIS, _expirationTimeMillis);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.failed</code> attribute defined at extension <code>processing</code>. 
	 * @return the failed
	 */
	@Accessor(qualifier = "failed", type = Accessor.Type.GETTER)
	public Boolean getFailed()
	{
		if (this._failed!=null)
		{
			return _failed;
		}
		return _failed = getPersistenceContext().getValue(FAILED, _failed);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.nodeGroup</code> attribute defined at extension <code>processing</code>. 
	 * @return the nodeGroup
	 */
	@Accessor(qualifier = "nodeGroup", type = Accessor.Type.GETTER)
	public String getNodeGroup()
	{
		if (this._nodeGroup!=null)
		{
			return _nodeGroup;
		}
		return _nodeGroup = getPersistenceContext().getValue(NODEGROUP, _nodeGroup);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.nodeId</code> attribute defined at extension <code>processing</code>. 
	 * @return the nodeId
	 */
	@Accessor(qualifier = "nodeId", type = Accessor.Type.GETTER)
	public Integer getNodeId()
	{
		if (this._nodeId!=null)
		{
			return _nodeId;
		}
		return _nodeId = getPersistenceContext().getValue(NODEID, _nodeId);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.retry</code> attribute defined at extension <code>processing</code>. 
	 * @return the retry
	 */
	@Accessor(qualifier = "retry", type = Accessor.Type.GETTER)
	public Integer getRetry()
	{
		if (this._retry!=null)
		{
			return _retry;
		}
		return _retry = getPersistenceContext().getValue(RETRY, _retry);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.runnerBean</code> attribute defined at extension <code>processing</code>. 
	 * @return the runnerBean
	 */
	@Accessor(qualifier = "runnerBean", type = Accessor.Type.GETTER)
	public String getRunnerBean()
	{
		if (this._runnerBean!=null)
		{
			return _runnerBean;
		}
		return _runnerBean = getPersistenceContext().getValue(RUNNERBEAN, _runnerBean);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Task.runningOnClusterNode</code> attribute defined at extension <code>processing</code>. 
	 * @return the runningOnClusterNode
	 */
	@Accessor(qualifier = "runningOnClusterNode", type = Accessor.Type.GETTER)
	public Integer getRunningOnClusterNode()
	{
		if (this._runningOnClusterNode!=null)
		{
			return _runningOnClusterNode;
		}
		return _runningOnClusterNode = getPersistenceContext().getValue(RUNNINGONCLUSTERNODE, _runningOnClusterNode);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.conditions</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the conditions
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.SETTER)
	public void setConditions(final Set<TaskConditionModel> value)
	{
		_conditions = getPersistenceContext().setValue(CONDITIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.context</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the context
	 */
	@Accessor(qualifier = "context", type = Accessor.Type.SETTER)
	public void setContext(final Object value)
	{
		_context = getPersistenceContext().setValue(CONTEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.contextItem</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the contextItem
	 */
	@Accessor(qualifier = "contextItem", type = Accessor.Type.SETTER)
	public void setContextItem(final ItemModel value)
	{
		_contextItem = getPersistenceContext().setValue(CONTEXTITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.executionDate</code> dynamic attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the executionDate - Date after this task is to be executed
	 */
	@Accessor(qualifier = "executionDate", type = Accessor.Type.SETTER)
	public void setExecutionDate(final Date value)
	{
		getPersistenceContext().setDynamicValue(this,EXECUTIONDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.executionTimeMillis</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the executionTimeMillis - Internal representation to overcome database limitations!
	 */
	@Accessor(qualifier = "executionTimeMillis", type = Accessor.Type.SETTER)
	public void setExecutionTimeMillis(final Long value)
	{
		_executionTimeMillis = getPersistenceContext().setValue(EXECUTIONTIMEMILLIS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.expirationDate</code> dynamic attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the expirationDate
	 */
	@Accessor(qualifier = "expirationDate", type = Accessor.Type.SETTER)
	public void setExpirationDate(final Date value)
	{
		getPersistenceContext().setDynamicValue(this,EXPIRATIONDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.expirationTimeMillis</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the expirationTimeMillis - Date when this task is to be executed
	 */
	@Accessor(qualifier = "expirationTimeMillis", type = Accessor.Type.SETTER)
	public void setExpirationTimeMillis(final Long value)
	{
		_expirationTimeMillis = getPersistenceContext().setValue(EXPIRATIONTIMEMILLIS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.failed</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the failed
	 */
	@Accessor(qualifier = "failed", type = Accessor.Type.SETTER)
	public void setFailed(final Boolean value)
	{
		_failed = getPersistenceContext().setValue(FAILED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.nodeGroup</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the nodeGroup
	 */
	@Accessor(qualifier = "nodeGroup", type = Accessor.Type.SETTER)
	public void setNodeGroup(final String value)
	{
		_nodeGroup = getPersistenceContext().setValue(NODEGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.nodeId</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the nodeId
	 */
	@Accessor(qualifier = "nodeId", type = Accessor.Type.SETTER)
	public void setNodeId(final Integer value)
	{
		_nodeId = getPersistenceContext().setValue(NODEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.retry</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the retry
	 */
	@Accessor(qualifier = "retry", type = Accessor.Type.SETTER)
	public void setRetry(final Integer value)
	{
		_retry = getPersistenceContext().setValue(RETRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.runnerBean</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the runnerBean
	 */
	@Accessor(qualifier = "runnerBean", type = Accessor.Type.SETTER)
	public void setRunnerBean(final String value)
	{
		_runnerBean = getPersistenceContext().setValue(RUNNERBEAN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Task.runningOnClusterNode</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the runningOnClusterNode
	 */
	@Accessor(qualifier = "runningOnClusterNode", type = Accessor.Type.SETTER)
	public void setRunningOnClusterNode(final Integer value)
	{
		_runningOnClusterNode = getPersistenceContext().setValue(RUNNINGONCLUSTERNODE, value);
	}
	
}
