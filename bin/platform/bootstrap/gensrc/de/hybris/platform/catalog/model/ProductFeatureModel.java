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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ProductFeature first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ProductFeatureModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ProductFeature";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.product</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRODUCT = "product";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.qualifier</code> attribute defined at extension <code>catalog</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.classificationAttributeAssignment</code> attribute defined at extension <code>catalog</code>. */
	public static final String CLASSIFICATIONATTRIBUTEASSIGNMENT = "classificationAttributeAssignment";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.language</code> attribute defined at extension <code>catalog</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.valuePosition</code> attribute defined at extension <code>catalog</code>. */
	public static final String VALUEPOSITION = "valuePosition";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.featurePosition</code> attribute defined at extension <code>catalog</code>. */
	public static final String FEATUREPOSITION = "featurePosition";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.value</code> attribute defined at extension <code>catalog</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.unit</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNIT = "unit";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.valueDetails</code> attribute defined at extension <code>catalog</code>. */
	public static final String VALUEDETAILS = "valueDetails";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.description</code> attribute defined at extension <code>catalog</code>. */
	public static final String DESCRIPTION = "description";
	
	
	/** <i>Generated variable</i> - Variable of <code>ProductFeature.product</code> attribute defined at extension <code>catalog</code>. */
	private ProductModel _product;
	
	/** <i>Generated variable</i> - Variable of <code>ProductFeature.qualifier</code> attribute defined at extension <code>catalog</code>. */
	private String _qualifier;
	
	/** <i>Generated variable</i> - Variable of <code>ProductFeature.classificationAttributeAssignment</code> attribute defined at extension <code>catalog</code>. */
	private ClassAttributeAssignmentModel _classificationAttributeAssignment;
	
	/** <i>Generated variable</i> - Variable of <code>ProductFeature.language</code> attribute defined at extension <code>catalog</code>. */
	private LanguageModel _language;
	
	/** <i>Generated variable</i> - Variable of <code>ProductFeature.valuePosition</code> attribute defined at extension <code>catalog</code>. */
	private Integer _valuePosition;
	
	/** <i>Generated variable</i> - Variable of <code>ProductFeature.featurePosition</code> attribute defined at extension <code>catalog</code>. */
	private Integer _featurePosition;
	
	/** <i>Generated variable</i> - Variable of <code>ProductFeature.value</code> attribute defined at extension <code>catalog</code>. */
	private Object _value;
	
	/** <i>Generated variable</i> - Variable of <code>ProductFeature.unit</code> attribute defined at extension <code>catalog</code>. */
	private ClassificationAttributeUnitModel _unit;
	
	/** <i>Generated variable</i> - Variable of <code>ProductFeature.valueDetails</code> attribute defined at extension <code>catalog</code>. */
	private String _valueDetails;
	
	/** <i>Generated variable</i> - Variable of <code>ProductFeature.description</code> attribute defined at extension <code>catalog</code>. */
	private String _description;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductFeatureModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductFeatureModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _product initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _qualifier initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _value initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ProductFeatureModel(final ProductModel _product, final String _qualifier, final Object _value)
	{
		super();
		setProduct(_product);
		setQualifier(_qualifier);
		setValue(_value);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _classificationAttributeAssignment initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _language initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _product initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _qualifier initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _value initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ProductFeatureModel(final ClassAttributeAssignmentModel _classificationAttributeAssignment, final LanguageModel _language, final ItemModel _owner, final ProductModel _product, final String _qualifier, final Object _value)
	{
		super();
		setClassificationAttributeAssignment(_classificationAttributeAssignment);
		setLanguage(_language);
		setOwner(_owner);
		setProduct(_product);
		setQualifier(_qualifier);
		setValue(_value);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.classificationAttributeAssignment</code> attribute defined at extension <code>catalog</code>. 
	 * @return the classificationAttributeAssignment - Classification attribute assignment which this value belongs to
	 */
	@Accessor(qualifier = "classificationAttributeAssignment", type = Accessor.Type.GETTER)
	public ClassAttributeAssignmentModel getClassificationAttributeAssignment()
	{
		if (this._classificationAttributeAssignment!=null)
		{
			return _classificationAttributeAssignment;
		}
		return _classificationAttributeAssignment = getPersistenceContext().getValue(CLASSIFICATIONATTRIBUTEASSIGNMENT, _classificationAttributeAssignment);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.description</code> attribute defined at extension <code>catalog</code>. 
	 * @return the description - description text
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
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.featurePosition</code> attribute defined at extension <code>catalog</code>. 
	 * @return the featurePosition - position of the feature which this value belongs to
	 */
	@Accessor(qualifier = "featurePosition", type = Accessor.Type.GETTER)
	public Integer getFeaturePosition()
	{
		if (this._featurePosition!=null)
		{
			return _featurePosition;
		}
		return _featurePosition = getPersistenceContext().getValue(FEATUREPOSITION, _featurePosition);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.language</code> attribute defined at extension <code>catalog</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		if (this._language!=null)
		{
			return _language;
		}
		return _language = getPersistenceContext().getValue(LANGUAGE, _language);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.product</code> attribute defined at extension <code>catalog</code>. 
	 * @return the product - Product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		if (this._product!=null)
		{
			return _product;
		}
		return _product = getPersistenceContext().getValue(PRODUCT, _product);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.qualifier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the qualifier - Qualifier
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
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.unit</code> attribute defined at extension <code>catalog</code>. 
	 * @return the unit - Classification attribute unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
	public ClassificationAttributeUnitModel getUnit()
	{
		if (this._unit!=null)
		{
			return _unit;
		}
		return _unit = getPersistenceContext().getValue(UNIT, _unit);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.value</code> attribute defined at extension <code>catalog</code>. 
	 * @return the value - the actual value of this feature
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
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.valueDetails</code> attribute defined at extension <code>catalog</code>. 
	 * @return the valueDetails - value details text
	 */
	@Accessor(qualifier = "valueDetails", type = Accessor.Type.GETTER)
	public String getValueDetails()
	{
		if (this._valueDetails!=null)
		{
			return _valueDetails;
		}
		return _valueDetails = getPersistenceContext().getValue(VALUEDETAILS, _valueDetails);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.valuePosition</code> attribute defined at extension <code>catalog</code>. 
	 * @return the valuePosition - position mark for multi value features
	 */
	@Accessor(qualifier = "valuePosition", type = Accessor.Type.GETTER)
	public Integer getValuePosition()
	{
		if (this._valuePosition!=null)
		{
			return _valuePosition;
		}
		return _valuePosition = getPersistenceContext().getValue(VALUEPOSITION, _valuePosition);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductFeature.classificationAttributeAssignment</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the classificationAttributeAssignment - Classification attribute assignment which this value belongs to
	 */
	@Accessor(qualifier = "classificationAttributeAssignment", type = Accessor.Type.SETTER)
	public void setClassificationAttributeAssignment(final ClassAttributeAssignmentModel value)
	{
		_classificationAttributeAssignment = getPersistenceContext().setValue(CLASSIFICATIONATTRIBUTEASSIGNMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.description</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the description - description text
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		_description = getPersistenceContext().setValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.featurePosition</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the featurePosition - position of the feature which this value belongs to
	 */
	@Accessor(qualifier = "featurePosition", type = Accessor.Type.SETTER)
	public void setFeaturePosition(final Integer value)
	{
		_featurePosition = getPersistenceContext().setValue(FEATUREPOSITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductFeature.language</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		_language = getPersistenceContext().setValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductFeature.product</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the product - Product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		_product = getPersistenceContext().setValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductFeature.qualifier</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the qualifier - Qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		_qualifier = getPersistenceContext().setValue(QUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.unit</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the unit - Classification attribute unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
	public void setUnit(final ClassificationAttributeUnitModel value)
	{
		_unit = getPersistenceContext().setValue(UNIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.value</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the value - the actual value of this feature
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final Object value)
	{
		_value = getPersistenceContext().setValue(VALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.valueDetails</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the valueDetails - value details text
	 */
	@Accessor(qualifier = "valueDetails", type = Accessor.Type.SETTER)
	public void setValueDetails(final String value)
	{
		_valueDetails = getPersistenceContext().setValue(VALUEDETAILS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.valuePosition</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the valuePosition - position mark for multi value features
	 */
	@Accessor(qualifier = "valuePosition", type = Accessor.Type.SETTER)
	public void setValuePosition(final Integer value)
	{
		_valuePosition = getPersistenceContext().setValue(VALUEPOSITION, value);
	}
	
}
