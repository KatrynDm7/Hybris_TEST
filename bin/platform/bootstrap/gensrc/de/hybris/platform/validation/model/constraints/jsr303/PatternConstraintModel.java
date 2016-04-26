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
import de.hybris.platform.validation.enums.RegexpFlag;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import java.util.Set;

/**
 * Generated model class for type PatternConstraint first defined at extension validation.
 * <p>
 * Pattern JSR 303 compatible constraint class.
 */
@SuppressWarnings("all")
public class PatternConstraintModel extends AttributeConstraintModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "PatternConstraint";
	
	/** <i>Generated constant</i> - Attribute key of <code>PatternConstraint.regexp</code> attribute defined at extension <code>validation</code>. */
	public static final String REGEXP = "regexp";
	
	/** <i>Generated constant</i> - Attribute key of <code>PatternConstraint.flags</code> attribute defined at extension <code>validation</code>. */
	public static final String FLAGS = "flags";
	
	
	/** <i>Generated variable</i> - Variable of <code>PatternConstraint.regexp</code> attribute defined at extension <code>validation</code>. */
	private String _regexp;
	
	/** <i>Generated variable</i> - Variable of <code>PatternConstraint.flags</code> attribute defined at extension <code>validation</code>. */
	private Set<RegexpFlag> _flags;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PatternConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PatternConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _flags initial attribute declared by type <code>PatternConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _regexp initial attribute declared by type <code>PatternConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public PatternConstraintModel(final Set<RegexpFlag> _flags, final String _id, final String _regexp)
	{
		super();
		setFlags(_flags);
		setId(_id);
		setRegexp(_regexp);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _flags initial attribute declared by type <code>PatternConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _regexp initial attribute declared by type <code>PatternConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public PatternConstraintModel(final Set<RegexpFlag> _flags, final String _id, final ItemModel _owner, final String _regexp)
	{
		super();
		setFlags(_flags);
		setId(_id);
		setOwner(_owner);
		setRegexp(_regexp);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PatternConstraint.flags</code> attribute defined at extension <code>validation</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the flags - Regular expression for constraint
	 */
	@Accessor(qualifier = "flags", type = Accessor.Type.GETTER)
	public Set<RegexpFlag> getFlags()
	{
		if (this._flags!=null)
		{
			return _flags;
		}
		return _flags = getPersistenceContext().getValue(FLAGS, _flags);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PatternConstraint.regexp</code> attribute defined at extension <code>validation</code>. 
	 * @return the regexp - Regular expression for constraint
	 */
	@Accessor(qualifier = "regexp", type = Accessor.Type.GETTER)
	public String getRegexp()
	{
		if (this._regexp!=null)
		{
			return _regexp;
		}
		return _regexp = getPersistenceContext().getValue(REGEXP, _regexp);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PatternConstraint.flags</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the flags - Regular expression for constraint
	 */
	@Accessor(qualifier = "flags", type = Accessor.Type.SETTER)
	public void setFlags(final Set<RegexpFlag> value)
	{
		_flags = getPersistenceContext().setValue(FLAGS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PatternConstraint.regexp</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the regexp - Regular expression for constraint
	 */
	@Accessor(qualifier = "regexp", type = Accessor.Type.SETTER)
	public void setRegexp(final String value)
	{
		_regexp = getPersistenceContext().setValue(REGEXP, value);
	}
	
}
