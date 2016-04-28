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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncAttributeDescriptorConfigModel;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated model class for type SyncItemJob first defined at extension catalog.
 */
@SuppressWarnings("all")
public class SyncItemJobModel extends JobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SyncItemJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEVERSION = "sourceVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETVERSION = "targetVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.exclusiveMode</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXCLUSIVEMODE = "exclusiveMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.syncPrincipalsOnly</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCPRINCIPALSONLY = "syncPrincipalsOnly";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.createNewItems</code> attribute defined at extension <code>catalog</code>. */
	public static final String CREATENEWITEMS = "createNewItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.removeMissingItems</code> attribute defined at extension <code>catalog</code>. */
	public static final String REMOVEMISSINGITEMS = "removeMissingItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.executions</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXECUTIONS = "executions";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.exportAttributeDescriptors</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXPORTATTRIBUTEDESCRIPTORS = "exportAttributeDescriptors";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.syncAttributeConfigurations</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCATTRIBUTECONFIGURATIONS = "syncAttributeConfigurations";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.effectiveSyncLanguages</code> attribute defined at extension <code>catalog</code>. */
	public static final String EFFECTIVESYNCLANGUAGES = "effectiveSyncLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.rootTypes</code> attribute defined at extension <code>catalog</code>. */
	public static final String ROOTTYPES = "rootTypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.syncLanguages</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCLANGUAGES = "syncLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.syncPrincipals</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCPRINCIPALS = "syncPrincipals";
	
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _sourceVersion;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _targetVersion;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.exclusiveMode</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _exclusiveMode;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.syncPrincipalsOnly</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _syncPrincipalsOnly;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.createNewItems</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _createNewItems;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.removeMissingItems</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _removeMissingItems;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.executions</code> attribute defined at extension <code>catalog</code>. */
	private Collection<SyncItemCronJobModel> _executions;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.exportAttributeDescriptors</code> attribute defined at extension <code>catalog</code>. */
	private Map<AttributeDescriptorModel,Boolean> _exportAttributeDescriptors;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.syncAttributeConfigurations</code> attribute defined at extension <code>catalog</code>. */
	private Collection<SyncAttributeDescriptorConfigModel> _syncAttributeConfigurations;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.effectiveSyncLanguages</code> attribute defined at extension <code>catalog</code>. */
	private Collection<LanguageModel> _effectiveSyncLanguages;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.rootTypes</code> attribute defined at extension <code>catalog</code>. */
	private List<ComposedTypeModel> _rootTypes;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.syncLanguages</code> attribute defined at extension <code>catalog</code>. */
	private Set<LanguageModel> _syncLanguages;
	
	/** <i>Generated variable</i> - Variable of <code>SyncItemJob.syncPrincipals</code> attribute defined at extension <code>catalog</code>. */
	private List<PrincipalModel> _syncPrincipals;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SyncItemJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SyncItemJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public SyncItemJobModel(final String _code, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCode(_code);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public SyncItemJobModel(final String _code, final Integer _nodeID, final ItemModel _owner, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.createNewItems</code> attribute defined at extension <code>catalog</code>. 
	 * @return the createNewItems
	 */
	@Accessor(qualifier = "createNewItems", type = Accessor.Type.GETTER)
	public Boolean getCreateNewItems()
	{
		if (this._createNewItems!=null)
		{
			return _createNewItems;
		}
		return _createNewItems = getPersistenceContext().getValue(CREATENEWITEMS, _createNewItems);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.effectiveSyncLanguages</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the effectiveSyncLanguages
	 */
	@Accessor(qualifier = "effectiveSyncLanguages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getEffectiveSyncLanguages()
	{
		if (this._effectiveSyncLanguages!=null)
		{
			return _effectiveSyncLanguages;
		}
		return _effectiveSyncLanguages = getPersistenceContext().getValue(EFFECTIVESYNCLANGUAGES, _effectiveSyncLanguages);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.exclusiveMode</code> attribute defined at extension <code>catalog</code>. 
	 * @return the exclusiveMode
	 */
	@Accessor(qualifier = "exclusiveMode", type = Accessor.Type.GETTER)
	public Boolean getExclusiveMode()
	{
		if (this._exclusiveMode!=null)
		{
			return _exclusiveMode;
		}
		return _exclusiveMode = getPersistenceContext().getValue(EXCLUSIVEMODE, _exclusiveMode);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.executions</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the executions
	 */
	@Accessor(qualifier = "executions", type = Accessor.Type.GETTER)
	public Collection<SyncItemCronJobModel> getExecutions()
	{
		if (this._executions!=null)
		{
			return _executions;
		}
		return _executions = getPersistenceContext().getValue(EXECUTIONS, _executions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.exportAttributeDescriptors</code> attribute defined at extension <code>catalog</code>. 
	 * @return the exportAttributeDescriptors
	 */
	@Accessor(qualifier = "exportAttributeDescriptors", type = Accessor.Type.GETTER)
	public Map<AttributeDescriptorModel,Boolean> getExportAttributeDescriptors()
	{
		if (this._exportAttributeDescriptors!=null)
		{
			return _exportAttributeDescriptors;
		}
		return _exportAttributeDescriptors = getPersistenceContext().getValue(EXPORTATTRIBUTEDESCRIPTORS, _exportAttributeDescriptors);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.removeMissingItems</code> attribute defined at extension <code>catalog</code>. 
	 * @return the removeMissingItems
	 */
	@Accessor(qualifier = "removeMissingItems", type = Accessor.Type.GETTER)
	public Boolean getRemoveMissingItems()
	{
		if (this._removeMissingItems!=null)
		{
			return _removeMissingItems;
		}
		return _removeMissingItems = getPersistenceContext().getValue(REMOVEMISSINGITEMS, _removeMissingItems);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.rootTypes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the rootTypes
	 */
	@Accessor(qualifier = "rootTypes", type = Accessor.Type.GETTER)
	public List<ComposedTypeModel> getRootTypes()
	{
		if (this._rootTypes!=null)
		{
			return _rootTypes;
		}
		return _rootTypes = getPersistenceContext().getValue(ROOTTYPES, _rootTypes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getSourceVersion()
	{
		if (this._sourceVersion!=null)
		{
			return _sourceVersion;
		}
		return _sourceVersion = getPersistenceContext().getValue(SOURCEVERSION, _sourceVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.syncAttributeConfigurations</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the syncAttributeConfigurations
	 */
	@Accessor(qualifier = "syncAttributeConfigurations", type = Accessor.Type.GETTER)
	public Collection<SyncAttributeDescriptorConfigModel> getSyncAttributeConfigurations()
	{
		if (this._syncAttributeConfigurations!=null)
		{
			return _syncAttributeConfigurations;
		}
		return _syncAttributeConfigurations = getPersistenceContext().getValue(SYNCATTRIBUTECONFIGURATIONS, _syncAttributeConfigurations);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.syncLanguages</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the syncLanguages
	 */
	@Accessor(qualifier = "syncLanguages", type = Accessor.Type.GETTER)
	public Set<LanguageModel> getSyncLanguages()
	{
		if (this._syncLanguages!=null)
		{
			return _syncLanguages;
		}
		return _syncLanguages = getPersistenceContext().getValue(SYNCLANGUAGES, _syncLanguages);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.syncPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the syncPrincipals
	 */
	@Accessor(qualifier = "syncPrincipals", type = Accessor.Type.GETTER)
	public List<PrincipalModel> getSyncPrincipals()
	{
		if (this._syncPrincipals!=null)
		{
			return _syncPrincipals;
		}
		return _syncPrincipals = getPersistenceContext().getValue(SYNCPRINCIPALS, _syncPrincipals);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.syncPrincipalsOnly</code> attribute defined at extension <code>catalog</code>. 
	 * @return the syncPrincipalsOnly
	 */
	@Accessor(qualifier = "syncPrincipalsOnly", type = Accessor.Type.GETTER)
	public Boolean getSyncPrincipalsOnly()
	{
		if (this._syncPrincipalsOnly!=null)
		{
			return _syncPrincipalsOnly;
		}
		return _syncPrincipalsOnly = getPersistenceContext().getValue(SYNCPRINCIPALSONLY, _syncPrincipalsOnly);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.targetVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getTargetVersion()
	{
		if (this._targetVersion!=null)
		{
			return _targetVersion;
		}
		return _targetVersion = getPersistenceContext().getValue(TARGETVERSION, _targetVersion);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.createNewItems</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the createNewItems
	 */
	@Accessor(qualifier = "createNewItems", type = Accessor.Type.SETTER)
	public void setCreateNewItems(final Boolean value)
	{
		_createNewItems = getPersistenceContext().setValue(CREATENEWITEMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SyncItemJob.exclusiveMode</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the exclusiveMode
	 */
	@Accessor(qualifier = "exclusiveMode", type = Accessor.Type.SETTER)
	public void setExclusiveMode(final Boolean value)
	{
		_exclusiveMode = getPersistenceContext().setValue(EXCLUSIVEMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.exportAttributeDescriptors</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the exportAttributeDescriptors
	 */
	@Accessor(qualifier = "exportAttributeDescriptors", type = Accessor.Type.SETTER)
	public void setExportAttributeDescriptors(final Map<AttributeDescriptorModel,Boolean> value)
	{
		_exportAttributeDescriptors = getPersistenceContext().setValue(EXPORTATTRIBUTEDESCRIPTORS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.removeMissingItems</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the removeMissingItems
	 */
	@Accessor(qualifier = "removeMissingItems", type = Accessor.Type.SETTER)
	public void setRemoveMissingItems(final Boolean value)
	{
		_removeMissingItems = getPersistenceContext().setValue(REMOVEMISSINGITEMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.rootTypes</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the rootTypes
	 */
	@Accessor(qualifier = "rootTypes", type = Accessor.Type.SETTER)
	public void setRootTypes(final List<ComposedTypeModel> value)
	{
		_rootTypes = getPersistenceContext().setValue(ROOTTYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SyncItemJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
	public void setSourceVersion(final CatalogVersionModel value)
	{
		_sourceVersion = getPersistenceContext().setValue(SOURCEVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.syncAttributeConfigurations</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the syncAttributeConfigurations
	 */
	@Accessor(qualifier = "syncAttributeConfigurations", type = Accessor.Type.SETTER)
	public void setSyncAttributeConfigurations(final Collection<SyncAttributeDescriptorConfigModel> value)
	{
		_syncAttributeConfigurations = getPersistenceContext().setValue(SYNCATTRIBUTECONFIGURATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.syncLanguages</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the syncLanguages
	 */
	@Accessor(qualifier = "syncLanguages", type = Accessor.Type.SETTER)
	public void setSyncLanguages(final Set<LanguageModel> value)
	{
		_syncLanguages = getPersistenceContext().setValue(SYNCLANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.syncPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the syncPrincipals
	 */
	@Accessor(qualifier = "syncPrincipals", type = Accessor.Type.SETTER)
	public void setSyncPrincipals(final List<PrincipalModel> value)
	{
		_syncPrincipals = getPersistenceContext().setValue(SYNCPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.syncPrincipalsOnly</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the syncPrincipalsOnly
	 */
	@Accessor(qualifier = "syncPrincipalsOnly", type = Accessor.Type.SETTER)
	public void setSyncPrincipalsOnly(final Boolean value)
	{
		_syncPrincipalsOnly = getPersistenceContext().setValue(SYNCPRINCIPALSONLY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SyncItemJob.targetVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
	public void setTargetVersion(final CatalogVersionModel value)
	{
		_targetVersion = getPersistenceContext().setValue(TARGETVERSION, value);
	}
	
}
