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
package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.enums.ValidatorLanguage;
import de.hybris.platform.validation.model.constraints.TypeConstraintModel;

/**
 * Generated model class for type DynamicConstraint first defined at extension validation.
 * <p>
 * Dynamic constraint definition.
 */
@SuppressWarnings("all")
public class DynamicConstraintModel extends TypeConstraintModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DynamicConstraint";
	
	/** <i>Generated constant</i> - Attribute key of <code>DynamicConstraint.language</code> attribute defined at extension <code>validation</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>DynamicConstraint.expression</code> attribute defined at extension <code>validation</code>. */
	public static final String EXPRESSION = "expression";
	
	
	/** <i>Generated variable</i> - Variable of <code>DynamicConstraint.language</code> attribute defined at extension <code>validation</code>. */
	private ValidatorLanguage _language;
	
	/** <i>Generated variable</i> - Variable of <code>DynamicConstraint.expression</code> attribute defined at extension <code>validation</code>. */
	private String _expression;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DynamicConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DynamicConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _expression initial attribute declared by type <code>DynamicConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _language initial attribute declared by type <code>DynamicConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public DynamicConstraintModel(final String _expression, final String _id, final ValidatorLanguage _language)
	{
		super();
		setExpression(_expression);
		setId(_id);
		setLanguage(_language);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _expression initial attribute declared by type <code>DynamicConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _language initial attribute declared by type <code>DynamicConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DynamicConstraintModel(final String _expression, final String _id, final ValidatorLanguage _language, final ItemModel _owner)
	{
		super();
		setExpression(_expression);
		setId(_id);
		setLanguage(_language);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DynamicConstraint.expression</code> attribute defined at extension <code>validation</code>. 
	 * @return the expression - Expression to evaluate in assigned language type
	 */
	@Accessor(qualifier = "expression", type = Accessor.Type.GETTER)
	public String getExpression()
	{
		if (this._expression!=null)
		{
			return _expression;
		}
		return _expression = getPersistenceContext().getValue(EXPRESSION, _expression);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DynamicConstraint.language</code> attribute defined at extension <code>validation</code>. 
	 * @return the language - Type of the dynamic script language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public ValidatorLanguage getLanguage()
	{
		if (this._language!=null)
		{
			return _language;
		}
		return _language = getPersistenceContext().getValue(LANGUAGE, _language);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DynamicConstraint.expression</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the expression - Expression to evaluate in assigned language type
	 */
	@Accessor(qualifier = "expression", type = Accessor.Type.SETTER)
	public void setExpression(final String value)
	{
		_expression = getPersistenceContext().setValue(EXPRESSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DynamicConstraint.language</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the language - Type of the dynamic script language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final ValidatorLanguage value)
	{
		_language = getPersistenceContext().setValue(LANGUAGE, value);
	}
	
}
