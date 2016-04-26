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
import java.math.BigDecimal;

/**
 * Generated model class for type DecimalMinConstraint first defined at extension validation.
 * <p>
 * Decimal minimal JSR 303 compatible constraint class.
 */
@SuppressWarnings("all")
public class DecimalMinConstraintModel extends AttributeConstraintModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DecimalMinConstraint";
	
	/** <i>Generated constant</i> - Attribute key of <code>DecimalMinConstraint.value</code> attribute defined at extension <code>validation</code>. */
	public static final String VALUE = "value";
	
	
	/** <i>Generated variable</i> - Variable of <code>DecimalMinConstraint.value</code> attribute defined at extension <code>validation</code>. */
	private BigDecimal _value;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DecimalMinConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DecimalMinConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _value initial attribute declared by type <code>DecimalMinConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public DecimalMinConstraintModel(final String _id, final BigDecimal _value)
	{
		super();
		setId(_id);
		setValue(_value);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _value initial attribute declared by type <code>DecimalMinConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public DecimalMinConstraintModel(final String _id, final ItemModel _owner, final BigDecimal _value)
	{
		super();
		setId(_id);
		setOwner(_owner);
		setValue(_value);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DecimalMinConstraint.value</code> attribute defined at extension <code>validation</code>. 
	 * @return the value - Maximal value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public BigDecimal getValue()
	{
		if (this._value!=null)
		{
			return _value;
		}
		return _value = getPersistenceContext().getValue(VALUE, _value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DecimalMinConstraint.value</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the value - Maximal value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final BigDecimal value)
	{
		_value = getPersistenceContext().setValue(VALUE, value);
	}
	
}
