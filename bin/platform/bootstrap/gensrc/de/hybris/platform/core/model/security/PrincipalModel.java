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
package de.hybris.platform.core.model.security;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.CockpitUIComponentAccessRightModel;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.mcc.model.AbstractLinkEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type Principal first defined at extension core.
 */
@SuppressWarnings("all")
public class PrincipalModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Principal";
	
	/**<i>Generated relation code constant for relation <code>Category2PrincipalRelation</code> defining source attribute <code>accessibleCategories</code> in extension <code>catalog</code>.</i>*/
	public final static String _CATEGORY2PRINCIPALRELATION = "Category2PrincipalRelation";
	
	/**<i>Generated relation code constant for relation <code>SyncItemJob2Principal</code> defining source attribute <code>syncJobs</code> in extension <code>catalog</code>.</i>*/
	public final static String _SYNCITEMJOB2PRINCIPAL = "SyncItemJob2Principal";
	
	/**<i>Generated relation code constant for relation <code>WorkflowTemplate2PrincipalRelation</code> defining source attribute <code>visibleTemplates</code> in extension <code>workflow</code>.</i>*/
	public final static String _WORKFLOWTEMPLATE2PRINCIPALRELATION = "WorkflowTemplate2PrincipalRelation";
	
	/**<i>Generated relation code constant for relation <code>CommentWatcherRelation</code> defining source attribute <code>watchedComments</code> in extension <code>comments</code>.</i>*/
	public final static String _COMMENTWATCHERRELATION = "CommentWatcherRelation";
	
	/**<i>Generated relation code constant for relation <code>Principal2CockpitUIComponentReadAccessRelation</code> defining source attribute <code>readableCockpitUIComponents</code> in extension <code>cockpit</code>.</i>*/
	public final static String _PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION = "Principal2CockpitUIComponentReadAccessRelation";
	
	/**<i>Generated relation code constant for relation <code>Principal2CockpitUIComponentWriteAccessRelation</code> defining source attribute <code>writableCockpitUIComponents</code> in extension <code>cockpit</code>.</i>*/
	public final static String _PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION = "Principal2CockpitUIComponentWriteAccessRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.description</code> attribute defined at extension <code>core</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.displayName</code> attribute defined at extension <code>core</code>. */
	public static final String DISPLAYNAME = "displayName";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.uid</code> attribute defined at extension <code>core</code>. */
	public static final String UID = "uid";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.allSearchRestrictions</code> attribute defined at extension <code>core</code>. */
	public static final String ALLSEARCHRESTRICTIONS = "allSearchRestrictions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.allGroups</code> attribute defined at extension <code>core</code>. */
	public static final String ALLGROUPS = "allGroups";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.groups</code> attribute defined at extension <code>core</code>. */
	public static final String GROUPS = "groups";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.searchRestrictions</code> attribute defined at extension <code>core</code>. */
	public static final String SEARCHRESTRICTIONS = "searchRestrictions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.accessibleCategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String ACCESSIBLECATEGORIES = "accessibleCategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.writableCatalogVersions</code> attribute defined at extension <code>catalog</code>. */
	public static final String WRITABLECATALOGVERSIONS = "writableCatalogVersions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.readableCatalogVersions</code> attribute defined at extension <code>catalog</code>. */
	public static final String READABLECATALOGVERSIONS = "readableCatalogVersions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.syncJobs</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCJOBS = "syncJobs";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.visibleTemplates</code> attribute defined at extension <code>workflow</code>. */
	public static final String VISIBLETEMPLATES = "visibleTemplates";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.profilePicture</code> attribute defined at extension <code>comments</code>. */
	public static final String PROFILEPICTURE = "profilePicture";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.watchedComments</code> attribute defined at extension <code>comments</code>. */
	public static final String WATCHEDCOMMENTS = "watchedComments";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.ldapsearchbase</code> attribute defined at extension <code>ldap</code>. */
	public static final String LDAPSEARCHBASE = "ldapsearchbase";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.DN</code> attribute defined at extension <code>ldap</code>. */
	public static final String DN = "DN";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.CN</code> attribute defined at extension <code>ldap</code>. */
	public static final String CN = "CN";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.readCollections</code> attribute defined at extension <code>cockpit</code>. */
	public static final String READCOLLECTIONS = "readCollections";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.writeCollections</code> attribute defined at extension <code>cockpit</code>. */
	public static final String WRITECOLLECTIONS = "writeCollections";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.cockpitUIConfigurations</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITUICONFIGURATIONS = "cockpitUIConfigurations";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.readableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. */
	public static final String READABLECOCKPITUICOMPONENTS = "readableCockpitUIComponents";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.writableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. */
	public static final String WRITABLECOCKPITUICOMPONENTS = "writableCockpitUIComponents";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.readSavedQueries</code> attribute defined at extension <code>cockpit</code>. */
	public static final String READSAVEDQUERIES = "readSavedQueries";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.readAbstractLinkEntry</code> attribute defined at extension <code>mcc</code>. */
	public static final String READABSTRACTLINKENTRY = "readAbstractLinkEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.backOfficeLoginDisabled</code> attribute defined at extension <code>backoffice</code>. */
	public static final String BACKOFFICELOGINDISABLED = "backOfficeLoginDisabled";
	
	
	/** <i>Generated variable</i> - Variable of <code>Principal.description</code> attribute defined at extension <code>core</code>. */
	private String _description;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.name</code> attribute defined at extension <code>core</code>. */
	private String _name;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.uid</code> attribute defined at extension <code>core</code>. */
	private String _uid;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.allSearchRestrictions</code> attribute defined at extension <code>core</code>. */
	private Collection<SearchRestrictionModel> _allSearchRestrictions;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.allGroups</code> attribute defined at extension <code>core</code>. */
	private Set<PrincipalGroupModel> _allGroups;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.groups</code> attribute defined at extension <code>core</code>. */
	private Set<PrincipalGroupModel> _groups;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.searchRestrictions</code> attribute defined at extension <code>core</code>. */
	private Collection<SearchRestrictionModel> _searchRestrictions;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.accessibleCategories</code> attribute defined at extension <code>catalog</code>. */
	private Collection<CategoryModel> _accessibleCategories;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.writableCatalogVersions</code> attribute defined at extension <code>catalog</code>. */
	private List<CatalogVersionModel> _writableCatalogVersions;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.readableCatalogVersions</code> attribute defined at extension <code>catalog</code>. */
	private List<CatalogVersionModel> _readableCatalogVersions;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.syncJobs</code> attribute defined at extension <code>catalog</code>. */
	private Collection<SyncItemJobModel> _syncJobs;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.visibleTemplates</code> attribute defined at extension <code>workflow</code>. */
	private Collection<WorkflowTemplateModel> _visibleTemplates;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.profilePicture</code> attribute defined at extension <code>comments</code>. */
	private MediaModel _profilePicture;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.watchedComments</code> attribute defined at extension <code>comments</code>. */
	private List<CommentModel> _watchedComments;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.ldapsearchbase</code> attribute defined at extension <code>ldap</code>. */
	private String _ldapsearchbase;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.DN</code> attribute defined at extension <code>ldap</code>. */
	private String _DN;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.CN</code> attribute defined at extension <code>ldap</code>. */
	private String _CN;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.readCollections</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<CockpitObjectAbstractCollectionModel> _readCollections;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.writeCollections</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<CockpitObjectAbstractCollectionModel> _writeCollections;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.cockpitUIConfigurations</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<CockpitUIComponentConfigurationModel> _cockpitUIConfigurations;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.readableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<CockpitUIComponentAccessRightModel> _readableCockpitUIComponents;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.writableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<CockpitUIComponentAccessRightModel> _writableCockpitUIComponents;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.readSavedQueries</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<CockpitSavedQueryModel> _readSavedQueries;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.readAbstractLinkEntry</code> attribute defined at extension <code>mcc</code>. */
	private List<AbstractLinkEntryModel> _readAbstractLinkEntry;
	
	/** <i>Generated variable</i> - Variable of <code>Principal.backOfficeLoginDisabled</code> attribute defined at extension <code>backoffice</code>. */
	private Boolean _backOfficeLoginDisabled;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PrincipalModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PrincipalModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public PrincipalModel(final String _uid)
	{
		super();
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public PrincipalModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.accessibleCategories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the accessibleCategories - catalog categories which are accessible for this principal
	 */
	@Accessor(qualifier = "accessibleCategories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getAccessibleCategories()
	{
		if (this._accessibleCategories!=null)
		{
			return _accessibleCategories;
		}
		return _accessibleCategories = getPersistenceContext().getValue(ACCESSIBLECATEGORIES, _accessibleCategories);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.allGroups</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allGroups
	 * @deprecated use {@link #getAllGroups()} instead
	 */
	@Deprecated
	public Set<PrincipalGroupModel> getAllgroups()
	{
		return this.getAllGroups();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.allGroups</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allGroups
	 */
	@Accessor(qualifier = "allGroups", type = Accessor.Type.GETTER)
	public Set<PrincipalGroupModel> getAllGroups()
	{
		if (this._allGroups!=null)
		{
			return _allGroups;
		}
		return _allGroups = getPersistenceContext().getValue(ALLGROUPS, _allGroups);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.allSearchRestrictions</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSearchRestrictions
	 * @deprecated use {@link #getAllSearchRestrictions()} instead
	 */
	@Deprecated
	public Collection<SearchRestrictionModel> getAllsearchrestrictions()
	{
		return this.getAllSearchRestrictions();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.allSearchRestrictions</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSearchRestrictions
	 */
	@Accessor(qualifier = "allSearchRestrictions", type = Accessor.Type.GETTER)
	public Collection<SearchRestrictionModel> getAllSearchRestrictions()
	{
		if (this._allSearchRestrictions!=null)
		{
			return _allSearchRestrictions;
		}
		return _allSearchRestrictions = getPersistenceContext().getValue(ALLSEARCHRESTRICTIONS, _allSearchRestrictions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.backOfficeLoginDisabled</code> attribute defined at extension <code>backoffice</code>. 
	 * @return the backOfficeLoginDisabled
	 */
	@Accessor(qualifier = "backOfficeLoginDisabled", type = Accessor.Type.GETTER)
	public Boolean getBackOfficeLoginDisabled()
	{
		if (this._backOfficeLoginDisabled!=null)
		{
			return _backOfficeLoginDisabled;
		}
		return _backOfficeLoginDisabled = getPersistenceContext().getValue(BACKOFFICELOGINDISABLED, _backOfficeLoginDisabled);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.CN</code> attribute defined at extension <code>ldap</code>. 
	 * @return the CN
	 */
	@Accessor(qualifier = "CN", type = Accessor.Type.GETTER)
	public String getCN()
	{
		if (this._CN!=null)
		{
			return _CN;
		}
		return _CN = getPersistenceContext().getValue(CN, _CN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.cockpitUIConfigurations</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cockpitUIConfigurations
	 */
	@Accessor(qualifier = "cockpitUIConfigurations", type = Accessor.Type.GETTER)
	public Collection<CockpitUIComponentConfigurationModel> getCockpitUIConfigurations()
	{
		if (this._cockpitUIConfigurations!=null)
		{
			return _cockpitUIConfigurations;
		}
		return _cockpitUIConfigurations = getPersistenceContext().getValue(COCKPITUICONFIGURATIONS, _cockpitUIConfigurations);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.description</code> attribute defined at extension <code>core</code>. 
	 * @return the description
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
	 * <i>Generated method</i> - Getter of the <code>Principal.displayName</code> attribute defined at extension <code>core</code>. 
	 * @return the displayName
	 */
	@Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
	public String getDisplayName()
	{
		return getDisplayName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.displayName</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the displayName
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
	public String getDisplayName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DISPLAYNAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.DN</code> attribute defined at extension <code>ldap</code>. 
	 * @return the DN
	 */
	@Accessor(qualifier = "DN", type = Accessor.Type.GETTER)
	public String getDN()
	{
		if (this._DN!=null)
		{
			return _DN;
		}
		return _DN = getPersistenceContext().getValue(DN, _DN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.groups</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the groups
	 */
	@Accessor(qualifier = "groups", type = Accessor.Type.GETTER)
	public Set<PrincipalGroupModel> getGroups()
	{
		if (this._groups!=null)
		{
			return _groups;
		}
		return _groups = getPersistenceContext().getValue(GROUPS, _groups);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.ldapsearchbase</code> attribute defined at extension <code>ldap</code>. 
	 * @return the ldapsearchbase
	 */
	@Accessor(qualifier = "ldapsearchbase", type = Accessor.Type.GETTER)
	public String getLdapsearchbase()
	{
		if (this._ldapsearchbase!=null)
		{
			return _ldapsearchbase;
		}
		return _ldapsearchbase = getPersistenceContext().getValue(LDAPSEARCHBASE, _ldapsearchbase);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		if (this._name!=null)
		{
			return _name;
		}
		return _name = getPersistenceContext().getValue(NAME, _name);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.profilePicture</code> attribute defined at extension <code>comments</code>. 
	 * @return the profilePicture
	 */
	@Accessor(qualifier = "profilePicture", type = Accessor.Type.GETTER)
	public MediaModel getProfilePicture()
	{
		if (this._profilePicture!=null)
		{
			return _profilePicture;
		}
		return _profilePicture = getPersistenceContext().getValue(PROFILEPICTURE, _profilePicture);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.readableCatalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readableCatalogVersions
	 */
	@Accessor(qualifier = "readableCatalogVersions", type = Accessor.Type.GETTER)
	public List<CatalogVersionModel> getReadableCatalogVersions()
	{
		if (this._readableCatalogVersions!=null)
		{
			return _readableCatalogVersions;
		}
		return _readableCatalogVersions = getPersistenceContext().getValue(READABLECATALOGVERSIONS, _readableCatalogVersions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.readableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readableCockpitUIComponents
	 */
	@Accessor(qualifier = "readableCockpitUIComponents", type = Accessor.Type.GETTER)
	public Collection<CockpitUIComponentAccessRightModel> getReadableCockpitUIComponents()
	{
		if (this._readableCockpitUIComponents!=null)
		{
			return _readableCockpitUIComponents;
		}
		return _readableCockpitUIComponents = getPersistenceContext().getValue(READABLECOCKPITUICOMPONENTS, _readableCockpitUIComponents);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.readAbstractLinkEntry</code> attribute defined at extension <code>mcc</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readAbstractLinkEntry
	 */
	@Accessor(qualifier = "readAbstractLinkEntry", type = Accessor.Type.GETTER)
	public List<AbstractLinkEntryModel> getReadAbstractLinkEntry()
	{
		if (this._readAbstractLinkEntry!=null)
		{
			return _readAbstractLinkEntry;
		}
		return _readAbstractLinkEntry = getPersistenceContext().getValue(READABSTRACTLINKENTRY, _readAbstractLinkEntry);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.readCollections</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readCollections
	 */
	@Accessor(qualifier = "readCollections", type = Accessor.Type.GETTER)
	public Collection<CockpitObjectAbstractCollectionModel> getReadCollections()
	{
		if (this._readCollections!=null)
		{
			return _readCollections;
		}
		return _readCollections = getPersistenceContext().getValue(READCOLLECTIONS, _readCollections);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.readSavedQueries</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readSavedQueries
	 */
	@Accessor(qualifier = "readSavedQueries", type = Accessor.Type.GETTER)
	public Collection<CockpitSavedQueryModel> getReadSavedQueries()
	{
		if (this._readSavedQueries!=null)
		{
			return _readSavedQueries;
		}
		return _readSavedQueries = getPersistenceContext().getValue(READSAVEDQUERIES, _readSavedQueries);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.searchRestrictions</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the searchRestrictions
	 * @deprecated use {@link #getSearchRestrictions()} instead
	 */
	@Deprecated
	public Collection<SearchRestrictionModel> getSearchrestrictions()
	{
		return this.getSearchRestrictions();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.searchRestrictions</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the searchRestrictions
	 */
	@Accessor(qualifier = "searchRestrictions", type = Accessor.Type.GETTER)
	public Collection<SearchRestrictionModel> getSearchRestrictions()
	{
		if (this._searchRestrictions!=null)
		{
			return _searchRestrictions;
		}
		return _searchRestrictions = getPersistenceContext().getValue(SEARCHRESTRICTIONS, _searchRestrictions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.syncJobs</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the syncJobs
	 */
	@Accessor(qualifier = "syncJobs", type = Accessor.Type.GETTER)
	public Collection<SyncItemJobModel> getSyncJobs()
	{
		if (this._syncJobs!=null)
		{
			return _syncJobs;
		}
		return _syncJobs = getPersistenceContext().getValue(SYNCJOBS, _syncJobs);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.uid</code> attribute defined at extension <code>core</code>. 
	 * @return the uid
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
	public String getUid()
	{
		if (this._uid!=null)
		{
			return _uid;
		}
		return _uid = getPersistenceContext().getValue(UID, _uid);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.visibleTemplates</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the visibleTemplates
	 */
	@Accessor(qualifier = "visibleTemplates", type = Accessor.Type.GETTER)
	public Collection<WorkflowTemplateModel> getVisibleTemplates()
	{
		if (this._visibleTemplates!=null)
		{
			return _visibleTemplates;
		}
		return _visibleTemplates = getPersistenceContext().getValue(VISIBLETEMPLATES, _visibleTemplates);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.watchedComments</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the watchedComments
	 */
	@Accessor(qualifier = "watchedComments", type = Accessor.Type.GETTER)
	public List<CommentModel> getWatchedComments()
	{
		if (this._watchedComments!=null)
		{
			return _watchedComments;
		}
		return _watchedComments = getPersistenceContext().getValue(WATCHEDCOMMENTS, _watchedComments);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.writableCatalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writableCatalogVersions
	 */
	@Accessor(qualifier = "writableCatalogVersions", type = Accessor.Type.GETTER)
	public List<CatalogVersionModel> getWritableCatalogVersions()
	{
		if (this._writableCatalogVersions!=null)
		{
			return _writableCatalogVersions;
		}
		return _writableCatalogVersions = getPersistenceContext().getValue(WRITABLECATALOGVERSIONS, _writableCatalogVersions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.writableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writableCockpitUIComponents
	 */
	@Accessor(qualifier = "writableCockpitUIComponents", type = Accessor.Type.GETTER)
	public Collection<CockpitUIComponentAccessRightModel> getWritableCockpitUIComponents()
	{
		if (this._writableCockpitUIComponents!=null)
		{
			return _writableCockpitUIComponents;
		}
		return _writableCockpitUIComponents = getPersistenceContext().getValue(WRITABLECOCKPITUICOMPONENTS, _writableCockpitUIComponents);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.writeCollections</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writeCollections
	 */
	@Accessor(qualifier = "writeCollections", type = Accessor.Type.GETTER)
	public Collection<CockpitObjectAbstractCollectionModel> getWriteCollections()
	{
		if (this._writeCollections!=null)
		{
			return _writeCollections;
		}
		return _writeCollections = getPersistenceContext().getValue(WRITECOLLECTIONS, _writeCollections);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.backOfficeLoginDisabled</code> attribute defined at extension <code>backoffice</code>. 
	 *  
	 * @param value the backOfficeLoginDisabled
	 */
	@Accessor(qualifier = "backOfficeLoginDisabled", type = Accessor.Type.SETTER)
	public void setBackOfficeLoginDisabled(final Boolean value)
	{
		_backOfficeLoginDisabled = getPersistenceContext().setValue(BACKOFFICELOGINDISABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.CN</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the CN
	 */
	@Accessor(qualifier = "CN", type = Accessor.Type.SETTER)
	public void setCN(final String value)
	{
		_CN = getPersistenceContext().setValue(CN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.cockpitUIConfigurations</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitUIConfigurations
	 */
	@Accessor(qualifier = "cockpitUIConfigurations", type = Accessor.Type.SETTER)
	public void setCockpitUIConfigurations(final Collection<CockpitUIComponentConfigurationModel> value)
	{
		_cockpitUIConfigurations = getPersistenceContext().setValue(COCKPITUICONFIGURATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		_description = getPersistenceContext().setValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.DN</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the DN
	 */
	@Accessor(qualifier = "DN", type = Accessor.Type.SETTER)
	public void setDN(final String value)
	{
		_DN = getPersistenceContext().setValue(DN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.groups</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the groups
	 */
	@Accessor(qualifier = "groups", type = Accessor.Type.SETTER)
	public void setGroups(final Set<PrincipalGroupModel> value)
	{
		_groups = getPersistenceContext().setValue(GROUPS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.ldapsearchbase</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the ldapsearchbase
	 */
	@Accessor(qualifier = "ldapsearchbase", type = Accessor.Type.SETTER)
	public void setLdapsearchbase(final String value)
	{
		_ldapsearchbase = getPersistenceContext().setValue(LDAPSEARCHBASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		_name = getPersistenceContext().setValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.profilePicture</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the profilePicture
	 */
	@Accessor(qualifier = "profilePicture", type = Accessor.Type.SETTER)
	public void setProfilePicture(final MediaModel value)
	{
		_profilePicture = getPersistenceContext().setValue(PROFILEPICTURE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.readableCatalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the readableCatalogVersions
	 */
	@Accessor(qualifier = "readableCatalogVersions", type = Accessor.Type.SETTER)
	public void setReadableCatalogVersions(final List<CatalogVersionModel> value)
	{
		_readableCatalogVersions = getPersistenceContext().setValue(READABLECATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.readableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the readableCockpitUIComponents
	 */
	@Accessor(qualifier = "readableCockpitUIComponents", type = Accessor.Type.SETTER)
	public void setReadableCockpitUIComponents(final Collection<CockpitUIComponentAccessRightModel> value)
	{
		_readableCockpitUIComponents = getPersistenceContext().setValue(READABLECOCKPITUICOMPONENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.readAbstractLinkEntry</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the readAbstractLinkEntry
	 */
	@Accessor(qualifier = "readAbstractLinkEntry", type = Accessor.Type.SETTER)
	public void setReadAbstractLinkEntry(final List<AbstractLinkEntryModel> value)
	{
		_readAbstractLinkEntry = getPersistenceContext().setValue(READABSTRACTLINKENTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.readCollections</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the readCollections
	 */
	@Accessor(qualifier = "readCollections", type = Accessor.Type.SETTER)
	public void setReadCollections(final Collection<CockpitObjectAbstractCollectionModel> value)
	{
		_readCollections = getPersistenceContext().setValue(READCOLLECTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.readSavedQueries</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the readSavedQueries
	 */
	@Accessor(qualifier = "readSavedQueries", type = Accessor.Type.SETTER)
	public void setReadSavedQueries(final Collection<CockpitSavedQueryModel> value)
	{
		_readSavedQueries = getPersistenceContext().setValue(READSAVEDQUERIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.syncJobs</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the syncJobs
	 */
	@Accessor(qualifier = "syncJobs", type = Accessor.Type.SETTER)
	public void setSyncJobs(final Collection<SyncItemJobModel> value)
	{
		_syncJobs = getPersistenceContext().setValue(SYNCJOBS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.uid</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the uid
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
	public void setUid(final String value)
	{
		_uid = getPersistenceContext().setValue(UID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.visibleTemplates</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the visibleTemplates
	 */
	@Accessor(qualifier = "visibleTemplates", type = Accessor.Type.SETTER)
	public void setVisibleTemplates(final Collection<WorkflowTemplateModel> value)
	{
		_visibleTemplates = getPersistenceContext().setValue(VISIBLETEMPLATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.watchedComments</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the watchedComments
	 */
	@Accessor(qualifier = "watchedComments", type = Accessor.Type.SETTER)
	public void setWatchedComments(final List<CommentModel> value)
	{
		_watchedComments = getPersistenceContext().setValue(WATCHEDCOMMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.writableCatalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the writableCatalogVersions
	 */
	@Accessor(qualifier = "writableCatalogVersions", type = Accessor.Type.SETTER)
	public void setWritableCatalogVersions(final List<CatalogVersionModel> value)
	{
		_writableCatalogVersions = getPersistenceContext().setValue(WRITABLECATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.writableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the writableCockpitUIComponents
	 */
	@Accessor(qualifier = "writableCockpitUIComponents", type = Accessor.Type.SETTER)
	public void setWritableCockpitUIComponents(final Collection<CockpitUIComponentAccessRightModel> value)
	{
		_writableCockpitUIComponents = getPersistenceContext().setValue(WRITABLECOCKPITUICOMPONENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.writeCollections</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the writeCollections
	 */
	@Accessor(qualifier = "writeCollections", type = Accessor.Type.SETTER)
	public void setWriteCollections(final Collection<CockpitObjectAbstractCollectionModel> value)
	{
		_writeCollections = getPersistenceContext().setValue(WRITECOLLECTIONS, value);
	}
	
}
