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
package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.StepModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Map;

/**
 * Generated model class for type ChangeDescriptor first defined at extension processing.
 */
@SuppressWarnings("all")
public class ChangeDescriptorModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ChangeDescriptor";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.cronJob</code> attribute defined at extension <code>processing</code>. */
	public static final String CRONJOB = "cronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.step</code> attribute defined at extension <code>processing</code>. */
	public static final String STEP = "step";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.changedItem</code> attribute defined at extension <code>processing</code>. */
	public static final String CHANGEDITEM = "changedItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.sequenceNumber</code> attribute defined at extension <code>processing</code>. */
	public static final String SEQUENCENUMBER = "sequenceNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.saveTimestamp</code> attribute defined at extension <code>processing</code>. */
	public static final String SAVETIMESTAMP = "saveTimestamp";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.previousItemState</code> attribute defined at extension <code>processing</code>. */
	public static final String PREVIOUSITEMSTATE = "previousItemState";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.changeType</code> attribute defined at extension <code>processing</code>. */
	public static final String CHANGETYPE = "changeType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.description</code> attribute defined at extension <code>processing</code>. */
	public static final String DESCRIPTION = "description";
	
	
	/** <i>Generated variable</i> - Variable of <code>ChangeDescriptor.cronJob</code> attribute defined at extension <code>processing</code>. */
	private CronJobModel _cronJob;
	
	/** <i>Generated variable</i> - Variable of <code>ChangeDescriptor.step</code> attribute defined at extension <code>processing</code>. */
	private StepModel _step;
	
	/** <i>Generated variable</i> - Variable of <code>ChangeDescriptor.changedItem</code> attribute defined at extension <code>processing</code>. */
	private ItemModel _changedItem;
	
	/** <i>Generated variable</i> - Variable of <code>ChangeDescriptor.sequenceNumber</code> attribute defined at extension <code>processing</code>. */
	private Integer _sequenceNumber;
	
	/** <i>Generated variable</i> - Variable of <code>ChangeDescriptor.saveTimestamp</code> attribute defined at extension <code>processing</code>. */
	private Date _saveTimestamp;
	
	/** <i>Generated variable</i> - Variable of <code>ChangeDescriptor.previousItemState</code> attribute defined at extension <code>processing</code>. */
	private Map _previousItemState;
	
	/** <i>Generated variable</i> - Variable of <code>ChangeDescriptor.changeType</code> attribute defined at extension <code>processing</code>. */
	private String _changeType;
	
	/** <i>Generated variable</i> - Variable of <code>ChangeDescriptor.description</code> attribute defined at extension <code>processing</code>. */
	private String _description;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ChangeDescriptorModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ChangeDescriptorModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _changeType initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _cronJob initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _sequenceNumber initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _step initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ChangeDescriptorModel(final String _changeType, final CronJobModel _cronJob, final Integer _sequenceNumber, final StepModel _step)
	{
		super();
		setChangeType(_changeType);
		setCronJob(_cronJob);
		setSequenceNumber(_sequenceNumber);
		setStep(_step);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _changeType initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _cronJob initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sequenceNumber initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _step initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ChangeDescriptorModel(final String _changeType, final CronJobModel _cronJob, final ItemModel _owner, final Integer _sequenceNumber, final StepModel _step)
	{
		super();
		setChangeType(_changeType);
		setCronJob(_cronJob);
		setOwner(_owner);
		setSequenceNumber(_sequenceNumber);
		setStep(_step);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.changedItem</code> attribute defined at extension <code>processing</code>. 
	 * @return the changedItem
	 */
	@Accessor(qualifier = "changedItem", type = Accessor.Type.GETTER)
	public ItemModel getChangedItem()
	{
		if (this._changedItem!=null)
		{
			return _changedItem;
		}
		return _changedItem = getPersistenceContext().getValue(CHANGEDITEM, _changedItem);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.changeType</code> attribute defined at extension <code>processing</code>. 
	 * @return the changeType
	 */
	@Accessor(qualifier = "changeType", type = Accessor.Type.GETTER)
	public String getChangeType()
	{
		if (this._changeType!=null)
		{
			return _changeType;
		}
		return _changeType = getPersistenceContext().getValue(CHANGETYPE, _changeType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.cronJob</code> attribute defined at extension <code>processing</code>. 
	 * @return the cronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
	public CronJobModel getCronJob()
	{
		if (this._cronJob!=null)
		{
			return _cronJob;
		}
		return _cronJob = getPersistenceContext().getValue(CRONJOB, _cronJob);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.description</code> attribute defined at extension <code>processing</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		if (this._description!=null)
		{
			return _description;
		}
		return _description = getPersistenceContext().getValue(DESCRIPTION, _description);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.previousItemState</code> attribute defined at extension <code>processing</code>. 
	 * @return the previousItemState
	 */
	@Accessor(qualifier = "previousItemState", type = Accessor.Type.GETTER)
	public Map getPreviousItemState()
	{
		if (this._previousItemState!=null)
		{
			return _previousItemState;
		}
		return _previousItemState = getPersistenceContext().getValue(PREVIOUSITEMSTATE, _previousItemState);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.saveTimestamp</code> attribute defined at extension <code>processing</code>. 
	 * @return the saveTimestamp
	 */
	@Accessor(qualifier = "saveTimestamp", type = Accessor.Type.GETTER)
	public Date getSaveTimestamp()
	{
		if (this._saveTimestamp!=null)
		{
			return _saveTimestamp;
		}
		return _saveTimestamp = getPersistenceContext().getValue(SAVETIMESTAMP, _saveTimestamp);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.sequenceNumber</code> attribute defined at extension <code>processing</code>. 
	 * @return the sequenceNumber
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.GETTER)
	public Integer getSequenceNumber()
	{
		if (this._sequenceNumber!=null)
		{
			return _sequenceNumber;
		}
		return _sequenceNumber = getPersistenceContext().getValue(SEQUENCENUMBER, _sequenceNumber);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.step</code> attribute defined at extension <code>processing</code>. 
	 * @return the step
	 */
	@Accessor(qualifier = "step", type = Accessor.Type.GETTER)
	public StepModel getStep()
	{
		if (this._step!=null)
		{
			return _step;
		}
		return _step = getPersistenceContext().getValue(STEP, _step);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDescriptor.changedItem</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the changedItem
	 */
	@Accessor(qualifier = "changedItem", type = Accessor.Type.SETTER)
	public void setChangedItem(final ItemModel value)
	{
		_changedItem = getPersistenceContext().setValue(CHANGEDITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDescriptor.changeType</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the changeType
	 */
	@Accessor(qualifier = "changeType", type = Accessor.Type.SETTER)
	public void setChangeType(final String value)
	{
		_changeType = getPersistenceContext().setValue(CHANGETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ChangeDescriptor.cronJob</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the cronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
	public void setCronJob(final CronJobModel value)
	{
		_cronJob = getPersistenceContext().setValue(CRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDescriptor.description</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		_description = getPersistenceContext().setValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDescriptor.previousItemState</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the previousItemState
	 */
	@Accessor(qualifier = "previousItemState", type = Accessor.Type.SETTER)
	public void setPreviousItemState(final Map value)
	{
		_previousItemState = getPersistenceContext().setValue(PREVIOUSITEMSTATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDescriptor.saveTimestamp</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the saveTimestamp
	 */
	@Accessor(qualifier = "saveTimestamp", type = Accessor.Type.SETTER)
	public void setSaveTimestamp(final Date value)
	{
		_saveTimestamp = getPersistenceContext().setValue(SAVETIMESTAMP, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ChangeDescriptor.sequenceNumber</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sequenceNumber
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.SETTER)
	public void setSequenceNumber(final Integer value)
	{
		_sequenceNumber = getPersistenceContext().setValue(SEQUENCENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ChangeDescriptor.step</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the step
	 */
	@Accessor(qualifier = "step", type = Accessor.Type.SETTER)
	public void setStep(final StepModel value)
	{
		_step = getPersistenceContext().setValue(STEP, value);
	}
	
}
