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
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import java.util.Set;

/**
 * Generated model class for type AttributeConstraint first defined at extension validation.
 * <p>
 * Attribute constraint definition.
 */
@SuppressWarnings("all")
public class AttributeConstraintModel extends AbstractConstraintModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AttributeConstraint";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeConstraint.qualifier</code> attribute defined at extension <code>validation</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeConstraint.languages</code> attribute defined at extension <code>validation</code>. */
	public static final String LANGUAGES = "languages";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeConstraint.descriptor</code> attribute defined at extension <code>validation</code>. */
	public static final String DESCRIPTOR = "descriptor";
	
	
	/** <i>Generated variable</i> - Variable of <code>AttributeConstraint.qualifier</code> attribute defined at extension <code>validation</code>. */
	private String _qualifier;
	
	/** <i>Generated variable</i> - Variable of <code>AttributeConstraint.languages</code> attribute defined at extension <code>validation</code>. */
	private Set<LanguageModel> _languages;
	
	/** <i>Generated variable</i> - Variable of <code>AttributeConstraint.descriptor</code> attribute defined at extension <code>validation</code>. */
	private AttributeDescriptorModel _descriptor;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AttributeConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AttributeConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public AttributeConstraintModel(final String _id)
	{
		super();
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AttributeConstraintModel(final String _id, final ItemModel _owner)
	{
		super();
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeConstraint.descriptor</code> attribute defined at extension <code>validation</code>. 
	 * @return the descriptor
	 */
	@Accessor(qualifier = "descriptor", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getDescriptor()
	{
		if (this._descriptor!=null)
		{
			return _descriptor;
		}
		return _descriptor = getPersistenceContext().getValue(DESCRIPTOR, _descriptor);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeConstraint.languages</code> attribute defined at extension <code>validation</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the languages - Languages for which constraint will be enforced
	 */
	@Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
	public Set<LanguageModel> getLanguages()
	{
		if (this._languages!=null)
		{
			return _languages;
		}
		return _languages = getPersistenceContext().getValue(LANGUAGES, _languages);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeConstraint.qualifier</code> attribute defined at extension <code>validation</code>. 
	 * @return the qualifier - Qualifier name for attribute descriptor
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		if (this._qualifier!=null)
		{
			return _qualifier;
		}
		return _qualifier = getPersistenceContext().getValue(QUALIFIER, _qualifier);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeConstraint.descriptor</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the descriptor
	 */
	@Accessor(qualifier = "descriptor", type = Accessor.Type.SETTER)
	public void setDescriptor(final AttributeDescriptorModel value)
	{
		_descriptor = getPersistenceContext().setValue(DESCRIPTOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeConstraint.languages</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the languages - Languages for which constraint will be enforced
	 */
	@Accessor(qualifier = "languages", type = Accessor.Type.SETTER)
	public void setLanguages(final Set<LanguageModel> value)
	{
		_languages = getPersistenceContext().setValue(LANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeConstraint.qualifier</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the qualifier - Qualifier name for attribute descriptor
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		_qualifier = getPersistenceContext().setValue(QUALIFIER, value);
	}
	
}
