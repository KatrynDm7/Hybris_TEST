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
package de.hybris.platform.processengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type BusinessProcessParameter first defined at extension processing.
 */
@SuppressWarnings("all")
public class BusinessProcessParameterModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "BusinessProcessParameter";
	
	/**<i>Generated relation code constant for relation <code>Process2ProcessParameterRelation</code> defining source attribute <code>process</code> in extension <code>processing</code>.</i>*/
	public final static String _PROCESS2PROCESSPARAMETERRELATION = "Process2ProcessParameterRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcessParameter.name</code> attribute defined at extension <code>processing</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcessParameter.value</code> attribute defined at extension <code>processing</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcessParameter.process</code> attribute defined at extension <code>processing</code>. */
	public static final String PROCESS = "process";
	
	
	/** <i>Generated variable</i> - Variable of <code>BusinessProcessParameter.name</code> attribute defined at extension <code>processing</code>. */
	private String _name;
	
	/** <i>Generated variable</i> - Variable of <code>BusinessProcessParameter.value</code> attribute defined at extension <code>processing</code>. */
	private Object _value;
	
	/** <i>Generated variable</i> - Variable of <code>BusinessProcessParameter.process</code> attribute defined at extension <code>processing</code>. */
	private BusinessProcessModel _process;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BusinessProcessParameterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BusinessProcessParameterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>BusinessProcessParameter</code> at extension <code>processing</code>
	 * @param _process initial attribute declared by type <code>BusinessProcessParameter</code> at extension <code>processing</code>
	 * @param _value initial attribute declared by type <code>BusinessProcessParameter</code> at extension <code>processing</code>
	 */
	@Deprecated
	public BusinessProcessParameterModel(final String _name, final BusinessProcessModel _process, final Object _value)
	{
		super();
		setName(_name);
		setProcess(_process);
		setValue(_value);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>BusinessProcessParameter</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _process initial attribute declared by type <code>BusinessProcessParameter</code> at extension <code>processing</code>
	 * @param _value initial attribute declared by type <code>BusinessProcessParameter</code> at extension <code>processing</code>
	 */
	@Deprecated
	public BusinessProcessParameterModel(final String _name, final ItemModel _owner, final BusinessProcessModel _process, final Object _value)
	{
		super();
		setName(_name);
		setOwner(_owner);
		setProcess(_process);
		setValue(_value);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcessParameter.name</code> attribute defined at extension <code>processing</code>. 
	 * @return the name - Unique identifier of this process context parameter
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		if (this._name!=null)
		{
			return _name;
		}
		return _name = getPersistenceContext().getValue(NAME, _name);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcessParameter.process</code> attribute defined at extension <code>processing</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>BusinessProcessParameter.value</code> attribute defined at extension <code>processing</code>. 
	 * @return the value - Value of this context parameter.
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public Object getValue()
	{
		if (this._value!=null)
		{
			return _value;
		}
		return _value = getPersistenceContext().getValue(VALUE, _value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BusinessProcessParameter.name</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the name - Unique identifier of this process context parameter
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		_name = getPersistenceContext().setValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BusinessProcessParameter.process</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the process
	 */
	@Accessor(qualifier = "process", type = Accessor.Type.SETTER)
	public void setProcess(final BusinessProcessModel value)
	{
		_process = getPersistenceContext().setValue(PROCESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BusinessProcessParameter.value</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the value - Value of this context parameter.
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final Object value)
	{
		_value = getPersistenceContext().setValue(VALUE, value);
	}
	
}
