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
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type ProductReference first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ProductReferenceModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ProductReference";
	
	/**<i>Generated relation code constant for relation <code>ProductReferenceRelation</code> defining source attribute <code>source</code> in extension <code>catalog</code>.</i>*/
	public final static String _PRODUCTREFERENCERELATION = "ProductReferenceRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductReference.qualifier</code> attribute defined at extension <code>catalog</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductReference.target</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGET = "target";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductReference.quantity</code> attribute defined at extension <code>catalog</code>. */
	public static final String QUANTITY = "quantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductReference.referenceType</code> attribute defined at extension <code>catalog</code>. */
	public static final String REFERENCETYPE = "referenceType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductReference.icon</code> attribute defined at extension <code>catalog</code>. */
	public static final String ICON = "icon";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductReference.description</code> attribute defined at extension <code>catalog</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductReference.preselected</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRESELECTED = "preselected";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductReference.active</code> attribute defined at extension <code>catalog</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductReference.source</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCE = "source";
	
	
	/** <i>Generated variable</i> - Variable of <code>ProductReference.qualifier</code> attribute defined at extension <code>catalog</code>. */
	private String _qualifier;
	
	/** <i>Generated variable</i> - Variable of <code>ProductReference.target</code> attribute defined at extension <code>catalog</code>. */
	private ProductModel _target;
	
	/** <i>Generated variable</i> - Variable of <code>ProductReference.quantity</code> attribute defined at extension <code>catalog</code>. */
	private Integer _quantity;
	
	/** <i>Generated variable</i> - Variable of <code>ProductReference.referenceType</code> attribute defined at extension <code>catalog</code>. */
	private ProductReferenceTypeEnum _referenceType;
	
	/** <i>Generated variable</i> - Variable of <code>ProductReference.icon</code> attribute defined at extension <code>catalog</code>. */
	private MediaModel _icon;
	
	/** <i>Generated variable</i> - Variable of <code>ProductReference.preselected</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _preselected;
	
	/** <i>Generated variable</i> - Variable of <code>ProductReference.active</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _active;
	
	/** <i>Generated variable</i> - Variable of <code>ProductReference.source</code> attribute defined at extension <code>catalog</code>. */
	private ProductModel _source;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductReferenceModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductReferenceModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _active initial attribute declared by type <code>ProductReference</code> at extension <code>catalog</code>
	 * @param _preselected initial attribute declared by type <code>ProductReference</code> at extension <code>catalog</code>
	 * @param _source initial attribute declared by type <code>ProductReference</code> at extension <code>catalog</code>
	 * @param _target initial attribute declared by type <code>ProductReference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ProductReferenceModel(final Boolean _active, final Boolean _preselected, final ProductModel _source, final ProductModel _target)
	{
		super();
		setActive(_active);
		setPreselected(_preselected);
		setSource(_source);
		setTarget(_target);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _active initial attribute declared by type <code>ProductReference</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _preselected initial attribute declared by type <code>ProductReference</code> at extension <code>catalog</code>
	 * @param _source initial attribute declared by type <code>ProductReference</code> at extension <code>catalog</code>
	 * @param _target initial attribute declared by type <code>ProductReference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ProductReferenceModel(final Boolean _active, final ItemModel _owner, final Boolean _preselected, final ProductModel _source, final ProductModel _target)
	{
		super();
		setActive(_active);
		setOwner(_owner);
		setPreselected(_preselected);
		setSource(_source);
		setTarget(_target);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductReference.active</code> attribute defined at extension <code>catalog</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		if (this._active!=null)
		{
			return _active;
		}
		return _active = getPersistenceContext().getValue(ACTIVE, _active);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductReference.description</code> attribute defined at extension <code>catalog</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductReference.description</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductReference.icon</code> attribute defined at extension <code>catalog</code>. 
	 * @return the icon
	 */
	@Accessor(qualifier = "icon", type = Accessor.Type.GETTER)
	public MediaModel getIcon()
	{
		if (this._icon!=null)
		{
			return _icon;
		}
		return _icon = getPersistenceContext().getValue(ICON, _icon);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductReference.preselected</code> attribute defined at extension <code>catalog</code>. 
	 * @return the preselected
	 */
	@Accessor(qualifier = "preselected", type = Accessor.Type.GETTER)
	public Boolean getPreselected()
	{
		if (this._preselected!=null)
		{
			return _preselected;
		}
		return _preselected = getPersistenceContext().getValue(PRESELECTED, _preselected);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductReference.qualifier</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ProductReference.quantity</code> attribute defined at extension <code>catalog</code>. 
	 * @return the quantity - Quantity
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
	public Integer getQuantity()
	{
		if (this._quantity!=null)
		{
			return _quantity;
		}
		return _quantity = getPersistenceContext().getValue(QUANTITY, _quantity);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductReference.referenceType</code> attribute defined at extension <code>catalog</code>. 
	 * @return the referenceType
	 */
	@Accessor(qualifier = "referenceType", type = Accessor.Type.GETTER)
	public ProductReferenceTypeEnum getReferenceType()
	{
		if (this._referenceType!=null)
		{
			return _referenceType;
		}
		return _referenceType = getPersistenceContext().getValue(REFERENCETYPE, _referenceType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductReference.source</code> attribute defined at extension <code>catalog</code>. 
	 * @return the source
	 */
	@Accessor(qualifier = "source", type = Accessor.Type.GETTER)
	public ProductModel getSource()
	{
		if (this._source!=null)
		{
			return _source;
		}
		return _source = getPersistenceContext().getValue(SOURCE, _source);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductReference.target</code> attribute defined at extension <code>catalog</code>. 
	 * @return the target - Target
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.GETTER)
	public ProductModel getTarget()
	{
		if (this._target!=null)
		{
			return _target;
		}
		return _target = getPersistenceContext().getValue(TARGET, _target);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductReference.active</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		_active = getPersistenceContext().setValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductReference.description</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductReference.description</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductReference.icon</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the icon
	 */
	@Accessor(qualifier = "icon", type = Accessor.Type.SETTER)
	public void setIcon(final MediaModel value)
	{
		_icon = getPersistenceContext().setValue(ICON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductReference.preselected</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the preselected
	 */
	@Accessor(qualifier = "preselected", type = Accessor.Type.SETTER)
	public void setPreselected(final Boolean value)
	{
		_preselected = getPersistenceContext().setValue(PRESELECTED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductReference.qualifier</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the qualifier - Qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		_qualifier = getPersistenceContext().setValue(QUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductReference.quantity</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the quantity - Quantity
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
	public void setQuantity(final Integer value)
	{
		_quantity = getPersistenceContext().setValue(QUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductReference.referenceType</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the referenceType
	 */
	@Accessor(qualifier = "referenceType", type = Accessor.Type.SETTER)
	public void setReferenceType(final ProductReferenceTypeEnum value)
	{
		_referenceType = getPersistenceContext().setValue(REFERENCETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductReference.source</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the source
	 */
	@Accessor(qualifier = "source", type = Accessor.Type.SETTER)
	public void setSource(final ProductModel value)
	{
		_source = getPersistenceContext().setValue(SOURCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductReference.target</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the target - Target
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.SETTER)
	public void setTarget(final ProductModel value)
	{
		_target = getPersistenceContext().setValue(TARGET, value);
	}
	
}
