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
package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type ClassificationAttributeUnit first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ClassificationAttributeUnitModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ClassificationAttributeUnit";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.systemVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYSTEMVERSION = "systemVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.code</code> attribute defined at extension <code>catalog</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.name</code> attribute defined at extension <code>catalog</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.externalID</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXTERNALID = "externalID";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.symbol</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYMBOL = "symbol";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.unitType</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNITTYPE = "unitType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.conversionFactor</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONVERSIONFACTOR = "conversionFactor";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.convertibleUnits</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONVERTIBLEUNITS = "convertibleUnits";
	
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttributeUnit.systemVersion</code> attribute defined at extension <code>catalog</code>. */
	private ClassificationSystemVersionModel _systemVersion;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttributeUnit.code</code> attribute defined at extension <code>catalog</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttributeUnit.externalID</code> attribute defined at extension <code>catalog</code>. */
	private String _externalID;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttributeUnit.symbol</code> attribute defined at extension <code>catalog</code>. */
	private String _symbol;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttributeUnit.unitType</code> attribute defined at extension <code>catalog</code>. */
	private String _unitType;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttributeUnit.conversionFactor</code> attribute defined at extension <code>catalog</code>. */
	private Double _conversionFactor;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationAttributeUnit.convertibleUnits</code> attribute defined at extension <code>catalog</code>. */
	private Set<ClassificationAttributeUnitModel> _convertibleUnits;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ClassificationAttributeUnitModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ClassificationAttributeUnitModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 * @param _symbol initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 * @param _systemVersion initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationAttributeUnitModel(final String _code, final String _symbol, final ClassificationSystemVersionModel _systemVersion)
	{
		super();
		setCode(_code);
		setSymbol(_symbol);
		setSystemVersion(_systemVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _symbol initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 * @param _systemVersion initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationAttributeUnitModel(final String _code, final ItemModel _owner, final String _symbol, final ClassificationSystemVersionModel _systemVersion)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setSymbol(_symbol);
		setSystemVersion(_systemVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.code</code> attribute defined at extension <code>catalog</code>. 
	 * @return the code - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		if (this._code!=null)
		{
			return _code;
		}
		return _code = getPersistenceContext().getValue(CODE, _code);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.conversionFactor</code> attribute defined at extension <code>catalog</code>. 
	 * @return the conversionFactor - Conversion factor
	 */
	@Accessor(qualifier = "conversionFactor", type = Accessor.Type.GETTER)
	public Double getConversionFactor()
	{
		if (this._conversionFactor!=null)
		{
			return _conversionFactor;
		}
		return _conversionFactor = getPersistenceContext().getValue(CONVERSIONFACTOR, _conversionFactor);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.convertibleUnits</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the convertibleUnits - Convertible units
	 */
	@Accessor(qualifier = "convertibleUnits", type = Accessor.Type.GETTER)
	public Set<ClassificationAttributeUnitModel> getConvertibleUnits()
	{
		if (this._convertibleUnits!=null)
		{
			return _convertibleUnits;
		}
		return _convertibleUnits = getPersistenceContext().getValue(CONVERTIBLEUNITS, _convertibleUnits);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.externalID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
	public String getExternalID()
	{
		if (this._externalID!=null)
		{
			return _externalID;
		}
		return _externalID = getPersistenceContext().getValue(EXTERNALID, _externalID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.name</code> attribute defined at extension <code>catalog</code>. 
	 * @return the name - optional localized name of this class
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.name</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the name - optional localized name of this class
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.symbol</code> attribute defined at extension <code>catalog</code>. 
	 * @return the symbol - Symbol
	 */
	@Accessor(qualifier = "symbol", type = Accessor.Type.GETTER)
	public String getSymbol()
	{
		if (this._symbol!=null)
		{
			return _symbol;
		}
		return _symbol = getPersistenceContext().getValue(SYMBOL, _symbol);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.systemVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.GETTER)
	public ClassificationSystemVersionModel getSystemVersion()
	{
		if (this._systemVersion!=null)
		{
			return _systemVersion;
		}
		return _systemVersion = getPersistenceContext().getValue(SYSTEMVERSION, _systemVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.unitType</code> attribute defined at extension <code>catalog</code>. 
	 * @return the unitType - Unit type
	 */
	@Accessor(qualifier = "unitType", type = Accessor.Type.GETTER)
	public String getUnitType()
	{
		if (this._unitType!=null)
		{
			return _unitType;
		}
		return _unitType = getPersistenceContext().getValue(UNITTYPE, _unitType);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.code</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the code - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.conversionFactor</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the conversionFactor - Conversion factor
	 */
	@Accessor(qualifier = "conversionFactor", type = Accessor.Type.SETTER)
	public void setConversionFactor(final Double value)
	{
		_conversionFactor = getPersistenceContext().setValue(CONVERSIONFACTOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.externalID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
	public void setExternalID(final String value)
	{
		_externalID = getPersistenceContext().setValue(EXTERNALID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - optional localized name of this class
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - optional localized name of this class
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.symbol</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the symbol - Symbol
	 */
	@Accessor(qualifier = "symbol", type = Accessor.Type.SETTER)
	public void setSymbol(final String value)
	{
		_symbol = getPersistenceContext().setValue(SYMBOL, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ClassificationAttributeUnit.systemVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.SETTER)
	public void setSystemVersion(final ClassificationSystemVersionModel value)
	{
		_systemVersion = getPersistenceContext().setValue(SYSTEMVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.unitType</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the unitType - Unit type
	 */
	@Accessor(qualifier = "unitType", type = Accessor.Type.SETTER)
	public void setUnitType(final String value)
	{
		_unitType = getPersistenceContext().setValue(UNITTYPE, value);
	}
	
}
