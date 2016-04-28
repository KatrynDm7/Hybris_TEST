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
package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cockpit.model.template.CockpitItemTemplateModel;
import de.hybris.platform.commons.model.FormatModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import java.util.Collection;
import java.util.Set;

/**
 * Generated model class for type ComposedType first defined at extension core.
 */
@SuppressWarnings("all")
public class ComposedTypeModel extends TypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ComposedType";
	
	/**<i>Generated relation code constant for relation <code>Format2ComTypRel</code> defining source attribute <code>formats</code> in extension <code>commons</code>.</i>*/
	public final static String _FORMAT2COMTYPREL = "Format2ComTypRel";
	
	/**<i>Generated relation code constant for relation <code>ConstraintCompositeTypeRelation</code> defining source attribute <code>constraints</code> in extension <code>validation</code>.</i>*/
	public final static String _CONSTRAINTCOMPOSITETYPERELATION = "ConstraintCompositeTypeRelation";
	
	/**<i>Generated relation code constant for relation <code>CockpitItemTemplate2ComposedTypeRelation</code> defining source attribute <code>cockpitItemTemplates</code> in extension <code>cockpit</code>.</i>*/
	public final static String _COCKPITITEMTEMPLATE2COMPOSEDTYPERELATION = "CockpitItemTemplate2ComposedTypeRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.abstract</code> attribute defined at extension <code>core</code>. */
	public static final String ABSTRACT = "abstract";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.declaredattributedescriptors</code> attribute defined at extension <code>core</code>. */
	public static final String DECLAREDATTRIBUTEDESCRIPTORS = "declaredattributedescriptors";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.dumpPropertyTable</code> attribute defined at extension <code>core</code>. */
	public static final String DUMPPROPERTYTABLE = "dumpPropertyTable";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.inheritedattributedescriptors</code> attribute defined at extension <code>core</code>. */
	public static final String INHERITEDATTRIBUTEDESCRIPTORS = "inheritedattributedescriptors";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.jaloclass</code> attribute defined at extension <code>core</code>. */
	public static final String JALOCLASS = "jaloclass";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.jndiName</code> attribute defined at extension <code>core</code>. */
	public static final String JNDINAME = "jndiName";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.singleton</code> attribute defined at extension <code>core</code>. */
	public static final String SINGLETON = "singleton";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.jaloonly</code> attribute defined at extension <code>core</code>. */
	public static final String JALOONLY = "jaloonly";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.dynamic</code> attribute defined at extension <code>core</code>. */
	public static final String DYNAMIC = "dynamic";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.subtypes</code> attribute defined at extension <code>core</code>. */
	public static final String SUBTYPES = "subtypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.superType</code> attribute defined at extension <code>core</code>. */
	public static final String SUPERTYPE = "superType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.table</code> attribute defined at extension <code>core</code>. */
	public static final String TABLE = "table";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.allSuperTypes</code> attribute defined at extension <code>core</code>. */
	public static final String ALLSUPERTYPES = "allSuperTypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.allSubTypes</code> attribute defined at extension <code>core</code>. */
	public static final String ALLSUBTYPES = "allSubTypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.legacyPersistence</code> attribute defined at extension <code>core</code>. */
	public static final String LEGACYPERSISTENCE = "legacyPersistence";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.formats</code> attribute defined at extension <code>commons</code>. */
	public static final String FORMATS = "formats";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.systemType</code> attribute defined at extension <code>impex</code>. */
	public static final String SYSTEMTYPE = "systemType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.constraints</code> attribute defined at extension <code>validation</code>. */
	public static final String CONSTRAINTS = "constraints";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.catalogItemType</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGITEMTYPE = "catalogItemType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.catalogVersionAttribute</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSIONATTRIBUTE = "catalogVersionAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.uniqueKeyAttributes</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNIQUEKEYATTRIBUTES = "uniqueKeyAttributes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.hmcIcon</code> attribute defined at extension <code>hmc</code>. */
	public static final String HMCICON = "hmcIcon";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.logHMCChanges</code> attribute defined at extension <code>hmc</code>. */
	public static final String LOGHMCCHANGES = "logHMCChanges";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.cockpitItemTemplates</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITITEMTEMPLATES = "cockpitItemTemplates";
	
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.abstract</code> attribute defined at extension <code>core</code>. */
	private Boolean _abstract;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.declaredattributedescriptors</code> attribute defined at extension <code>core</code>. */
	private Collection<AttributeDescriptorModel> _declaredattributedescriptors;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.dumpPropertyTable</code> attribute defined at extension <code>core</code>. */
	private String _dumpPropertyTable;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.inheritedattributedescriptors</code> attribute defined at extension <code>core</code>. */
	private Collection<AttributeDescriptorModel> _inheritedattributedescriptors;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.jaloclass</code> attribute defined at extension <code>core</code>. */
	private Class _jaloclass;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.jndiName</code> attribute defined at extension <code>core</code>. */
	private String _jndiName;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.singleton</code> attribute defined at extension <code>core</code>. */
	private Boolean _singleton;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.jaloonly</code> attribute defined at extension <code>core</code>. */
	private Boolean _jaloonly;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.dynamic</code> attribute defined at extension <code>core</code>. */
	private Boolean _dynamic;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.subtypes</code> attribute defined at extension <code>core</code>. */
	private Collection<ComposedTypeModel> _subtypes;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.superType</code> attribute defined at extension <code>core</code>. */
	private ComposedTypeModel _superType;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.table</code> attribute defined at extension <code>core</code>. */
	private String _table;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.allSuperTypes</code> attribute defined at extension <code>core</code>. */
	private Collection<ComposedTypeModel> _allSuperTypes;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.allSubTypes</code> attribute defined at extension <code>core</code>. */
	private Collection<ComposedTypeModel> _allSubTypes;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.legacyPersistence</code> attribute defined at extension <code>core</code>. */
	private Boolean _legacyPersistence;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.formats</code> attribute defined at extension <code>commons</code>. */
	private Collection<FormatModel> _formats;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.systemType</code> attribute defined at extension <code>impex</code>. */
	private Boolean _systemType;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.constraints</code> attribute defined at extension <code>validation</code>. */
	private Set<AbstractConstraintModel> _constraints;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.catalogItemType</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _catalogItemType;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.catalogVersionAttribute</code> attribute defined at extension <code>catalog</code>. */
	private AttributeDescriptorModel _catalogVersionAttribute;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.uniqueKeyAttributes</code> attribute defined at extension <code>catalog</code>. */
	private Collection<AttributeDescriptorModel> _uniqueKeyAttributes;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.hmcIcon</code> attribute defined at extension <code>hmc</code>. */
	private MediaModel _hmcIcon;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.logHMCChanges</code> attribute defined at extension <code>hmc</code>. */
	private Boolean _logHMCChanges;
	
	/** <i>Generated variable</i> - Variable of <code>ComposedType.cockpitItemTemplates</code> attribute defined at extension <code>cockpit</code>. */
	private Set<CockpitItemTemplateModel> _cockpitItemTemplates;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ComposedTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ComposedTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 */
	@Deprecated
	public ComposedTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final Boolean _singleton, final ComposedTypeModel _superType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setSingleton(_singleton);
		setSuperType(_superType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 */
	@Deprecated
	public ComposedTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final ItemModel _owner, final Boolean _singleton, final ComposedTypeModel _superType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setOwner(_owner);
		setSingleton(_singleton);
		setSuperType(_superType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.abstract</code> attribute defined at extension <code>core</code>. 
	 * @return the abstract
	 */
	@Accessor(qualifier = "abstract", type = Accessor.Type.GETTER)
	public Boolean getAbstract()
	{
		if (this._abstract!=null)
		{
			return _abstract;
		}
		return _abstract = getPersistenceContext().getValue(ABSTRACT, _abstract);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.allSubTypes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSubTypes
	 */
	@Accessor(qualifier = "allSubTypes", type = Accessor.Type.GETTER)
	public Collection<ComposedTypeModel> getAllSubTypes()
	{
		if (this._allSubTypes!=null)
		{
			return _allSubTypes;
		}
		return _allSubTypes = getPersistenceContext().getValue(ALLSUBTYPES, _allSubTypes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.allSuperTypes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSuperTypes
	 */
	@Accessor(qualifier = "allSuperTypes", type = Accessor.Type.GETTER)
	public Collection<ComposedTypeModel> getAllSuperTypes()
	{
		if (this._allSuperTypes!=null)
		{
			return _allSuperTypes;
		}
		return _allSuperTypes = getPersistenceContext().getValue(ALLSUPERTYPES, _allSuperTypes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.catalogItemType</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogItemType
	 */
	@Accessor(qualifier = "catalogItemType", type = Accessor.Type.GETTER)
	public Boolean getCatalogItemType()
	{
		if (this._catalogItemType!=null)
		{
			return _catalogItemType;
		}
		return _catalogItemType = getPersistenceContext().getValue(CATALOGITEMTYPE, _catalogItemType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.catalogVersionAttribute</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersionAttribute
	 */
	@Accessor(qualifier = "catalogVersionAttribute", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getCatalogVersionAttribute()
	{
		if (this._catalogVersionAttribute!=null)
		{
			return _catalogVersionAttribute;
		}
		return _catalogVersionAttribute = getPersistenceContext().getValue(CATALOGVERSIONATTRIBUTE, _catalogVersionAttribute);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.cockpitItemTemplates</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cockpitItemTemplates
	 */
	@Accessor(qualifier = "cockpitItemTemplates", type = Accessor.Type.GETTER)
	public Set<CockpitItemTemplateModel> getCockpitItemTemplates()
	{
		if (this._cockpitItemTemplates!=null)
		{
			return _cockpitItemTemplates;
		}
		return _cockpitItemTemplates = getPersistenceContext().getValue(COCKPITITEMTEMPLATES, _cockpitItemTemplates);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.constraints</code> attribute defined at extension <code>validation</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the constraints
	 */
	@Accessor(qualifier = "constraints", type = Accessor.Type.GETTER)
	public Set<AbstractConstraintModel> getConstraints()
	{
		if (this._constraints!=null)
		{
			return _constraints;
		}
		return _constraints = getPersistenceContext().getValue(CONSTRAINTS, _constraints);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.declaredattributedescriptors</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the declaredattributedescriptors
	 */
	@Accessor(qualifier = "declaredattributedescriptors", type = Accessor.Type.GETTER)
	public Collection<AttributeDescriptorModel> getDeclaredattributedescriptors()
	{
		if (this._declaredattributedescriptors!=null)
		{
			return _declaredattributedescriptors;
		}
		return _declaredattributedescriptors = getPersistenceContext().getValue(DECLAREDATTRIBUTEDESCRIPTORS, _declaredattributedescriptors);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.dumpPropertyTable</code> attribute defined at extension <code>core</code>. 
	 * @return the dumpPropertyTable
	 */
	@Accessor(qualifier = "dumpPropertyTable", type = Accessor.Type.GETTER)
	public String getDumpPropertyTable()
	{
		if (this._dumpPropertyTable!=null)
		{
			return _dumpPropertyTable;
		}
		return _dumpPropertyTable = getPersistenceContext().getValue(DUMPPROPERTYTABLE, _dumpPropertyTable);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.dynamic</code> attribute defined at extension <code>core</code>. 
	 * @return the dynamic
	 */
	@Accessor(qualifier = "dynamic", type = Accessor.Type.GETTER)
	public Boolean getDynamic()
	{
		if (this._dynamic!=null)
		{
			return _dynamic;
		}
		return _dynamic = getPersistenceContext().getValue(DYNAMIC, _dynamic);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.formats</code> attribute defined at extension <code>commons</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the formats
	 */
	@Accessor(qualifier = "formats", type = Accessor.Type.GETTER)
	public Collection<FormatModel> getFormats()
	{
		if (this._formats!=null)
		{
			return _formats;
		}
		return _formats = getPersistenceContext().getValue(FORMATS, _formats);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.hmcIcon</code> attribute defined at extension <code>hmc</code>. 
	 * @return the hmcIcon - HMCIcon
	 */
	@Accessor(qualifier = "hmcIcon", type = Accessor.Type.GETTER)
	public MediaModel getHmcIcon()
	{
		if (this._hmcIcon!=null)
		{
			return _hmcIcon;
		}
		return _hmcIcon = getPersistenceContext().getValue(HMCICON, _hmcIcon);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.inheritedattributedescriptors</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the inheritedattributedescriptors
	 */
	@Accessor(qualifier = "inheritedattributedescriptors", type = Accessor.Type.GETTER)
	public Collection<AttributeDescriptorModel> getInheritedattributedescriptors()
	{
		if (this._inheritedattributedescriptors!=null)
		{
			return _inheritedattributedescriptors;
		}
		return _inheritedattributedescriptors = getPersistenceContext().getValue(INHERITEDATTRIBUTEDESCRIPTORS, _inheritedattributedescriptors);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.jaloclass</code> attribute defined at extension <code>core</code>. 
	 * @return the jaloclass
	 */
	@Accessor(qualifier = "jaloclass", type = Accessor.Type.GETTER)
	public Class getJaloclass()
	{
		if (this._jaloclass!=null)
		{
			return _jaloclass;
		}
		return _jaloclass = getPersistenceContext().getValue(JALOCLASS, _jaloclass);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.jaloonly</code> attribute defined at extension <code>core</code>. 
	 * @return the jaloonly
	 */
	@Accessor(qualifier = "jaloonly", type = Accessor.Type.GETTER)
	public Boolean getJaloonly()
	{
		if (this._jaloonly!=null)
		{
			return _jaloonly;
		}
		return _jaloonly = getPersistenceContext().getValue(JALOONLY, _jaloonly);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.jndiName</code> attribute defined at extension <code>core</code>. 
	 * @return the jndiName
	 */
	@Accessor(qualifier = "jndiName", type = Accessor.Type.GETTER)
	public String getJndiName()
	{
		if (this._jndiName!=null)
		{
			return _jndiName;
		}
		return _jndiName = getPersistenceContext().getValue(JNDINAME, _jndiName);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.legacyPersistence</code> attribute defined at extension <code>core</code>. 
	 * @return the legacyPersistence
	 */
	@Accessor(qualifier = "legacyPersistence", type = Accessor.Type.GETTER)
	public Boolean getLegacyPersistence()
	{
		if (this._legacyPersistence!=null)
		{
			return _legacyPersistence;
		}
		return _legacyPersistence = getPersistenceContext().getValue(LEGACYPERSISTENCE, _legacyPersistence);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.logHMCChanges</code> attribute defined at extension <code>hmc</code>. 
	 * @return the logHMCChanges - whether or not log changes to items of this type via hmc
	 */
	@Accessor(qualifier = "logHMCChanges", type = Accessor.Type.GETTER)
	public Boolean getLogHMCChanges()
	{
		if (this._logHMCChanges!=null)
		{
			return _logHMCChanges;
		}
		return _logHMCChanges = getPersistenceContext().getValue(LOGHMCCHANGES, _logHMCChanges);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.singleton</code> attribute defined at extension <code>core</code>. 
	 * @return the singleton
	 */
	@Accessor(qualifier = "singleton", type = Accessor.Type.GETTER)
	public Boolean getSingleton()
	{
		if (this._singleton!=null)
		{
			return _singleton;
		}
		return _singleton = getPersistenceContext().getValue(SINGLETON, _singleton);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.subtypes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the subtypes
	 */
	@Accessor(qualifier = "subtypes", type = Accessor.Type.GETTER)
	public Collection<ComposedTypeModel> getSubtypes()
	{
		if (this._subtypes!=null)
		{
			return _subtypes;
		}
		return _subtypes = getPersistenceContext().getValue(SUBTYPES, _subtypes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.superType</code> attribute defined at extension <code>core</code>. 
	 * @return the superType
	 */
	@Accessor(qualifier = "superType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getSuperType()
	{
		if (this._superType!=null)
		{
			return _superType;
		}
		return _superType = getPersistenceContext().getValue(SUPERTYPE, _superType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.systemType</code> attribute defined at extension <code>impex</code>. 
	 * @return the systemType
	 */
	@Accessor(qualifier = "systemType", type = Accessor.Type.GETTER)
	public Boolean getSystemType()
	{
		if (this._systemType!=null)
		{
			return _systemType;
		}
		return _systemType = getPersistenceContext().getValue(SYSTEMTYPE, _systemType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.table</code> attribute defined at extension <code>core</code>. 
	 * @return the table
	 */
	@Accessor(qualifier = "table", type = Accessor.Type.GETTER)
	public String getTable()
	{
		if (this._table!=null)
		{
			return _table;
		}
		return _table = getPersistenceContext().getValue(TABLE, _table);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.uniqueKeyAttributes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the uniqueKeyAttributes
	 */
	@Accessor(qualifier = "uniqueKeyAttributes", type = Accessor.Type.GETTER)
	public Collection<AttributeDescriptorModel> getUniqueKeyAttributes()
	{
		if (this._uniqueKeyAttributes!=null)
		{
			return _uniqueKeyAttributes;
		}
		return _uniqueKeyAttributes = getPersistenceContext().getValue(UNIQUEKEYATTRIBUTES, _uniqueKeyAttributes);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.catalogItemType</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogItemType
	 */
	@Accessor(qualifier = "catalogItemType", type = Accessor.Type.SETTER)
	public void setCatalogItemType(final Boolean value)
	{
		_catalogItemType = getPersistenceContext().setValue(CATALOGITEMTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.catalogVersionAttribute</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersionAttribute
	 */
	@Accessor(qualifier = "catalogVersionAttribute", type = Accessor.Type.SETTER)
	public void setCatalogVersionAttribute(final AttributeDescriptorModel value)
	{
		_catalogVersionAttribute = getPersistenceContext().setValue(CATALOGVERSIONATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.cockpitItemTemplates</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitItemTemplates
	 */
	@Accessor(qualifier = "cockpitItemTemplates", type = Accessor.Type.SETTER)
	public void setCockpitItemTemplates(final Set<CockpitItemTemplateModel> value)
	{
		_cockpitItemTemplates = getPersistenceContext().setValue(COCKPITITEMTEMPLATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.constraints</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the constraints
	 */
	@Accessor(qualifier = "constraints", type = Accessor.Type.SETTER)
	public void setConstraints(final Set<AbstractConstraintModel> value)
	{
		_constraints = getPersistenceContext().setValue(CONSTRAINTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.declaredattributedescriptors</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the declaredattributedescriptors
	 */
	@Accessor(qualifier = "declaredattributedescriptors", type = Accessor.Type.SETTER)
	public void setDeclaredattributedescriptors(final Collection<AttributeDescriptorModel> value)
	{
		_declaredattributedescriptors = getPersistenceContext().setValue(DECLAREDATTRIBUTEDESCRIPTORS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.formats</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the formats
	 */
	@Accessor(qualifier = "formats", type = Accessor.Type.SETTER)
	public void setFormats(final Collection<FormatModel> value)
	{
		_formats = getPersistenceContext().setValue(FORMATS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.hmcIcon</code> attribute defined at extension <code>hmc</code>. 
	 *  
	 * @param value the hmcIcon - HMCIcon
	 */
	@Accessor(qualifier = "hmcIcon", type = Accessor.Type.SETTER)
	public void setHmcIcon(final MediaModel value)
	{
		_hmcIcon = getPersistenceContext().setValue(HMCICON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.jaloclass</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the jaloclass
	 */
	@Accessor(qualifier = "jaloclass", type = Accessor.Type.SETTER)
	public void setJaloclass(final Class value)
	{
		_jaloclass = getPersistenceContext().setValue(JALOCLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.jaloonly</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the jaloonly
	 */
	@Accessor(qualifier = "jaloonly", type = Accessor.Type.SETTER)
	public void setJaloonly(final Boolean value)
	{
		_jaloonly = getPersistenceContext().setValue(JALOONLY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.legacyPersistence</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the legacyPersistence
	 */
	@Accessor(qualifier = "legacyPersistence", type = Accessor.Type.SETTER)
	public void setLegacyPersistence(final Boolean value)
	{
		_legacyPersistence = getPersistenceContext().setValue(LEGACYPERSISTENCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.logHMCChanges</code> attribute defined at extension <code>hmc</code>. 
	 *  
	 * @param value the logHMCChanges - whether or not log changes to items of this type via hmc
	 */
	@Accessor(qualifier = "logHMCChanges", type = Accessor.Type.SETTER)
	public void setLogHMCChanges(final Boolean value)
	{
		_logHMCChanges = getPersistenceContext().setValue(LOGHMCCHANGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.singleton</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the singleton
	 */
	@Accessor(qualifier = "singleton", type = Accessor.Type.SETTER)
	public void setSingleton(final Boolean value)
	{
		_singleton = getPersistenceContext().setValue(SINGLETON, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ComposedType.superType</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the superType
	 */
	@Accessor(qualifier = "superType", type = Accessor.Type.SETTER)
	public void setSuperType(final ComposedTypeModel value)
	{
		_superType = getPersistenceContext().setValue(SUPERTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.systemType</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the systemType
	 */
	@Accessor(qualifier = "systemType", type = Accessor.Type.SETTER)
	public void setSystemType(final Boolean value)
	{
		_systemType = getPersistenceContext().setValue(SYSTEMTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.uniqueKeyAttributes</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the uniqueKeyAttributes
	 */
	@Accessor(qualifier = "uniqueKeyAttributes", type = Accessor.Type.SETTER)
	public void setUniqueKeyAttributes(final Collection<AttributeDescriptorModel> value)
	{
		_uniqueKeyAttributes = getPersistenceContext().setValue(UNIQUEKEYATTRIBUTES, value);
	}
	
}
