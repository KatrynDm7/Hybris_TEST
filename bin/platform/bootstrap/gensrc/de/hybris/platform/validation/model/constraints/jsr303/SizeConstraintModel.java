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
package de.hybris.platform.validation.model.constraints.jsr303;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;

/**
 * Generated model class for type SizeConstraint first defined at extension validation.
 * <p>
 * Size range JSR 303 compatible constraint class.
 */
@SuppressWarnings("all")
public class SizeConstraintModel extends AttributeConstraintModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SizeConstraint";
	
	/** <i>Generated constant</i> - Attribute key of <code>SizeConstraint.min</code> attribute defined at extension <code>validation</code>. */
	public static final String MIN = "min";
	
	/** <i>Generated constant</i> - Attribute key of <code>SizeConstraint.max</code> attribute defined at extension <code>validation</code>. */
	public static final String MAX = "max";
	
	
	/** <i>Generated variable</i> - Variable of <code>SizeConstraint.min</code> attribute defined at extension <code>validation</code>. */
	private Long _min;
	
	/** <i>Generated variable</i> - Variable of <code>SizeConstraint.max</code> attribute defined at extension <code>validation</code>. */
	private Long _max;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SizeConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SizeConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _max initial attribute declared by type <code>SizeConstraint</code> at extension <code>validation</code>
	 * @param _min initial attribute declared by type <code>SizeConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public SizeConstraintModel(final String _id, final Long _max, final Long _min)
	{
		super();
		setId(_id);
		setMax(_max);
		setMin(_min);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _max initial attribute declared by type <code>SizeConstraint</code> at extension <code>validation</code>
	 * @param _min initial attribute declared by type <code>SizeConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SizeConstraintModel(final String _id, final Long _max, final Long _min, final ItemModel _owner)
	{
		super();
		setId(_id);
		setMax(_max);
		setMin(_min);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SizeConstraint.max</code> attribute defined at extension <code>validation</code>. 
	 * @return the max - Overflow value
	 */
	@Accessor(qualifier = "max", type = Accessor.Type.GETTER)
	public Long getMax()
	{
		if (this._max!=null)
		{
			return _max;
		}
		return _max = getPersistenceContext().getValue(MAX, _max);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SizeConstraint.min</code> attribute defined at extension <code>validation</code>. 
	 * @return the min - Underflow value
	 */
	@Accessor(qualifier = "min", type = Accessor.Type.GETTER)
	public Long getMin()
	{
		if (this._min!=null)
		{
			return _min;
		}
		return _min = getPersistenceContext().getValue(MIN, _min);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SizeConstraint.max</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the max - Overflow value
	 */
	@Accessor(qualifier = "max", type = Accessor.Type.SETTER)
	public void setMax(final Long value)
	{
		_max = getPersistenceContext().setValue(MAX, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SizeConstraint.min</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the min - Underflow value
	 */
	@Accessor(qualifier = "min", type = Accessor.Type.SETTER)
	public void setMin(final Long value)
	{
		_min = getPersistenceContext().setValue(MIN, value);
	}
	
}
