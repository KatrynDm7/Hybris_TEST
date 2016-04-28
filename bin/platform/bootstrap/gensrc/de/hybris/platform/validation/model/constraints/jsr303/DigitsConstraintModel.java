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
package de.hybris.platform.validation.model.constraints.jsr303;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;

/**
 * Generated model class for type DigitsConstraint first defined at extension validation.
 * <p>
 * Digits JSR 303 compatible constraint class.
 */
@SuppressWarnings("all")
public class DigitsConstraintModel extends AttributeConstraintModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DigitsConstraint";
	
	/** <i>Generated constant</i> - Attribute key of <code>DigitsConstraint.integer</code> attribute defined at extension <code>validation</code>. */
	public static final String INTEGER = "integer";
	
	/** <i>Generated constant</i> - Attribute key of <code>DigitsConstraint.fraction</code> attribute defined at extension <code>validation</code>. */
	public static final String FRACTION = "fraction";
	
	
	/** <i>Generated variable</i> - Variable of <code>DigitsConstraint.integer</code> attribute defined at extension <code>validation</code>. */
	private Integer _integer;
	
	/** <i>Generated variable</i> - Variable of <code>DigitsConstraint.fraction</code> attribute defined at extension <code>validation</code>. */
	private Integer _fraction;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DigitsConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DigitsConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _fraction initial attribute declared by type <code>DigitsConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _integer initial attribute declared by type <code>DigitsConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public DigitsConstraintModel(final Integer _fraction, final String _id, final Integer _integer)
	{
		super();
		setFraction(_fraction);
		setId(_id);
		setInteger(_integer);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _fraction initial attribute declared by type <code>DigitsConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _integer initial attribute declared by type <code>DigitsConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DigitsConstraintModel(final Integer _fraction, final String _id, final Integer _integer, final ItemModel _owner)
	{
		super();
		setFraction(_fraction);
		setId(_id);
		setInteger(_integer);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DigitsConstraint.fraction</code> attribute defined at extension <code>validation</code>. 
	 * @return the fraction - Fraction part of the number
	 */
	@Accessor(qualifier = "fraction", type = Accessor.Type.GETTER)
	public Integer getFraction()
	{
		if (this._fraction!=null)
		{
			return _fraction;
		}
		return _fraction = getPersistenceContext().getValue(FRACTION, _fraction);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DigitsConstraint.integer</code> attribute defined at extension <code>validation</code>. 
	 * @return the integer - Integer part of the number
	 */
	@Accessor(qualifier = "integer", type = Accessor.Type.GETTER)
	public Integer getInteger()
	{
		if (this._integer!=null)
		{
			return _integer;
		}
		return _integer = getPersistenceContext().getValue(INTEGER, _integer);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DigitsConstraint.fraction</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the fraction - Fraction part of the number
	 */
	@Accessor(qualifier = "fraction", type = Accessor.Type.SETTER)
	public void setFraction(final Integer value)
	{
		_fraction = getPersistenceContext().setValue(FRACTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DigitsConstraint.integer</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the integer - Integer part of the number
	 */
	@Accessor(qualifier = "integer", type = Accessor.Type.SETTER)
	public void setInteger(final Integer value)
	{
		_integer = getPersistenceContext().setValue(INTEGER, value);
	}
	
}
