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
package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.TypeConstraintModel;

/**
 * Generated model class for type XorNullReferenceConstraint first defined at extension validation.
 * <p>
 * Custom constraint for presenting type scoped constraint.
 */
@SuppressWarnings("all")
public class XorNullReferenceConstraintModel extends TypeConstraintModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "XorNullReferenceConstraint";
	
	/** <i>Generated constant</i> - Attribute key of <code>XorNullReferenceConstraint.firstFieldName</code> attribute defined at extension <code>validation</code>. */
	public static final String FIRSTFIELDNAME = "firstFieldName";
	
	/** <i>Generated constant</i> - Attribute key of <code>XorNullReferenceConstraint.secondFieldName</code> attribute defined at extension <code>validation</code>. */
	public static final String SECONDFIELDNAME = "secondFieldName";
	
	
	/** <i>Generated variable</i> - Variable of <code>XorNullReferenceConstraint.firstFieldName</code> attribute defined at extension <code>validation</code>. */
	private String _firstFieldName;
	
	/** <i>Generated variable</i> - Variable of <code>XorNullReferenceConstraint.secondFieldName</code> attribute defined at extension <code>validation</code>. */
	private String _secondFieldName;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public XorNullReferenceConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public XorNullReferenceConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _firstFieldName initial attribute declared by type <code>XorNullReferenceConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _secondFieldName initial attribute declared by type <code>XorNullReferenceConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public XorNullReferenceConstraintModel(final String _firstFieldName, final String _id, final String _secondFieldName)
	{
		super();
		setFirstFieldName(_firstFieldName);
		setId(_id);
		setSecondFieldName(_secondFieldName);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _firstFieldName initial attribute declared by type <code>XorNullReferenceConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _secondFieldName initial attribute declared by type <code>XorNullReferenceConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public XorNullReferenceConstraintModel(final String _firstFieldName, final String _id, final ItemModel _owner, final String _secondFieldName)
	{
		super();
		setFirstFieldName(_firstFieldName);
		setId(_id);
		setOwner(_owner);
		setSecondFieldName(_secondFieldName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>XorNullReferenceConstraint.firstFieldName</code> attribute defined at extension <code>validation</code>. 
	 * @return the firstFieldName - First field name for XOR null reference logic
	 */
	@Accessor(qualifier = "firstFieldName", type = Accessor.Type.GETTER)
	public String getFirstFieldName()
	{
		if (this._firstFieldName!=null)
		{
			return _firstFieldName;
		}
		return _firstFieldName = getPersistenceContext().getValue(FIRSTFIELDNAME, _firstFieldName);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>XorNullReferenceConstraint.secondFieldName</code> attribute defined at extension <code>validation</code>. 
	 * @return the secondFieldName - Second field name for XOR null reference logic
	 */
	@Accessor(qualifier = "secondFieldName", type = Accessor.Type.GETTER)
	public String getSecondFieldName()
	{
		if (this._secondFieldName!=null)
		{
			return _secondFieldName;
		}
		return _secondFieldName = getPersistenceContext().getValue(SECONDFIELDNAME, _secondFieldName);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>XorNullReferenceConstraint.firstFieldName</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the firstFieldName - First field name for XOR null reference logic
	 */
	@Accessor(qualifier = "firstFieldName", type = Accessor.Type.SETTER)
	public void setFirstFieldName(final String value)
	{
		_firstFieldName = getPersistenceContext().setValue(FIRSTFIELDNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>XorNullReferenceConstraint.secondFieldName</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the secondFieldName - Second field name for XOR null reference logic
	 */
	@Accessor(qualifier = "secondFieldName", type = Accessor.Type.SETTER)
	public void setSecondFieldName(final String value)
	{
		_secondFieldName = getPersistenceContext().setValue(SECONDFIELDNAME, value);
	}
	
}
