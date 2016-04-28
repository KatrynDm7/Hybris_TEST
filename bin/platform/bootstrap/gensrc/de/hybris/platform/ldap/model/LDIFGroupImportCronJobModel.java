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
package de.hybris.platform.ldap.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.MediaProcessCronJobModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.ldap.model.ConfigurationMediaModel;
import de.hybris.platform.ldap.model.LDIFMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type LDIFGroupImportCronJob first defined at extension ldap.
 */
@SuppressWarnings("all")
public class LDIFGroupImportCronJobModel extends MediaProcessCronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "LDIFGroupImportCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.searchbase</code> attribute defined at extension <code>ldap</code>. */
	public static final String SEARCHBASE = "searchbase";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.ldapquery</code> attribute defined at extension <code>ldap</code>. */
	public static final String LDAPQUERY = "ldapquery";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.resultfilter</code> attribute defined at extension <code>ldap</code>. */
	public static final String RESULTFILTER = "resultfilter";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.userSearchFieldQualifier</code> attribute defined at extension <code>ldap</code>. */
	public static final String USERSEARCHFIELDQUALIFIER = "userSearchFieldQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.userRootDN</code> attribute defined at extension <code>ldap</code>. */
	public static final String USERROOTDN = "userRootDN";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.userResultfilter</code> attribute defined at extension <code>ldap</code>. */
	public static final String USERRESULTFILTER = "userResultfilter";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.importUsers</code> attribute defined at extension <code>ldap</code>. */
	public static final String IMPORTUSERS = "importUsers";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.codeExecution</code> attribute defined at extension <code>ldap</code>. */
	public static final String CODEEXECUTION = "codeExecution";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.ldifFile</code> attribute defined at extension <code>ldap</code>. */
	public static final String LDIFFILE = "ldifFile";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.configFile</code> attribute defined at extension <code>ldap</code>. */
	public static final String CONFIGFILE = "configFile";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.destMedia</code> attribute defined at extension <code>ldap</code>. */
	public static final String DESTMEDIA = "destMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFGroupImportCronJob.dumpMedia</code> attribute defined at extension <code>ldap</code>. */
	public static final String DUMPMEDIA = "dumpMedia";
	
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.searchbase</code> attribute defined at extension <code>ldap</code>. */
	private String _searchbase;
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.ldapquery</code> attribute defined at extension <code>ldap</code>. */
	private String _ldapquery;
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.resultfilter</code> attribute defined at extension <code>ldap</code>. */
	private String _resultfilter;
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.userSearchFieldQualifier</code> attribute defined at extension <code>ldap</code>. */
	private String _userSearchFieldQualifier;
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.userRootDN</code> attribute defined at extension <code>ldap</code>. */
	private String _userRootDN;
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.userResultfilter</code> attribute defined at extension <code>ldap</code>. */
	private String _userResultfilter;
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.importUsers</code> attribute defined at extension <code>ldap</code>. */
	private Boolean _importUsers;
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.codeExecution</code> attribute defined at extension <code>ldap</code>. */
	private Boolean _codeExecution;
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.ldifFile</code> attribute defined at extension <code>ldap</code>. */
	private LDIFMediaModel _ldifFile;
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.configFile</code> attribute defined at extension <code>ldap</code>. */
	private ConfigurationMediaModel _configFile;
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.destMedia</code> attribute defined at extension <code>ldap</code>. */
	private ImpExMediaModel _destMedia;
	
	/** <i>Generated variable</i> - Variable of <code>LDIFGroupImportCronJob.dumpMedia</code> attribute defined at extension <code>ldap</code>. */
	private ImpExMediaModel _dumpMedia;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public LDIFGroupImportCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public LDIFGroupImportCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _codeExecution initial attribute declared by type <code>LDIFGroupImportCronJob</code> at extension <code>ldap</code>
	 * @param _importUsers initial attribute declared by type <code>LDIFGroupImportCronJob</code> at extension <code>ldap</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public LDIFGroupImportCronJobModel(final Boolean _codeExecution, final Boolean _importUsers, final JobModel _job)
	{
		super();
		setCodeExecution(_codeExecution);
		setImportUsers(_importUsers);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _codeExecution initial attribute declared by type <code>LDIFGroupImportCronJob</code> at extension <code>ldap</code>
	 * @param _importUsers initial attribute declared by type <code>LDIFGroupImportCronJob</code> at extension <code>ldap</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public LDIFGroupImportCronJobModel(final Boolean _codeExecution, final Boolean _importUsers, final JobModel _job, final ItemModel _owner)
	{
		super();
		setCodeExecution(_codeExecution);
		setImportUsers(_importUsers);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.codeExecution</code> attribute defined at extension <code>ldap</code>. 
	 * @return the codeExecution
	 */
	@Accessor(qualifier = "codeExecution", type = Accessor.Type.GETTER)
	public Boolean getCodeExecution()
	{
		if (this._codeExecution!=null)
		{
			return _codeExecution;
		}
		return _codeExecution = getPersistenceContext().getValue(CODEEXECUTION, _codeExecution);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.configFile</code> attribute defined at extension <code>ldap</code>. 
	 * @return the configFile
	 */
	@Accessor(qualifier = "configFile", type = Accessor.Type.GETTER)
	public ConfigurationMediaModel getConfigFile()
	{
		if (this._configFile!=null)
		{
			return _configFile;
		}
		return _configFile = getPersistenceContext().getValue(CONFIGFILE, _configFile);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.destMedia</code> attribute defined at extension <code>ldap</code>. 
	 * @return the destMedia
	 */
	@Accessor(qualifier = "destMedia", type = Accessor.Type.GETTER)
	public ImpExMediaModel getDestMedia()
	{
		if (this._destMedia!=null)
		{
			return _destMedia;
		}
		return _destMedia = getPersistenceContext().getValue(DESTMEDIA, _destMedia);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.dumpMedia</code> attribute defined at extension <code>ldap</code>. 
	 * @return the dumpMedia
	 */
	@Accessor(qualifier = "dumpMedia", type = Accessor.Type.GETTER)
	public ImpExMediaModel getDumpMedia()
	{
		if (this._dumpMedia!=null)
		{
			return _dumpMedia;
		}
		return _dumpMedia = getPersistenceContext().getValue(DUMPMEDIA, _dumpMedia);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.importUsers</code> attribute defined at extension <code>ldap</code>. 
	 * @return the importUsers
	 */
	@Accessor(qualifier = "importUsers", type = Accessor.Type.GETTER)
	public Boolean getImportUsers()
	{
		if (this._importUsers!=null)
		{
			return _importUsers;
		}
		return _importUsers = getPersistenceContext().getValue(IMPORTUSERS, _importUsers);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.ldapquery</code> attribute defined at extension <code>ldap</code>. 
	 * @return the ldapquery
	 */
	@Accessor(qualifier = "ldapquery", type = Accessor.Type.GETTER)
	public String getLdapquery()
	{
		if (this._ldapquery!=null)
		{
			return _ldapquery;
		}
		return _ldapquery = getPersistenceContext().getValue(LDAPQUERY, _ldapquery);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.ldifFile</code> attribute defined at extension <code>ldap</code>. 
	 * @return the ldifFile
	 */
	@Accessor(qualifier = "ldifFile", type = Accessor.Type.GETTER)
	public LDIFMediaModel getLdifFile()
	{
		if (this._ldifFile!=null)
		{
			return _ldifFile;
		}
		return _ldifFile = getPersistenceContext().getValue(LDIFFILE, _ldifFile);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.resultfilter</code> attribute defined at extension <code>ldap</code>. 
	 * @return the resultfilter
	 */
	@Accessor(qualifier = "resultfilter", type = Accessor.Type.GETTER)
	public String getResultfilter()
	{
		if (this._resultfilter!=null)
		{
			return _resultfilter;
		}
		return _resultfilter = getPersistenceContext().getValue(RESULTFILTER, _resultfilter);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.searchbase</code> attribute defined at extension <code>ldap</code>. 
	 * @return the searchbase
	 */
	@Accessor(qualifier = "searchbase", type = Accessor.Type.GETTER)
	public String getSearchbase()
	{
		if (this._searchbase!=null)
		{
			return _searchbase;
		}
		return _searchbase = getPersistenceContext().getValue(SEARCHBASE, _searchbase);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.userResultfilter</code> attribute defined at extension <code>ldap</code>. 
	 * @return the userResultfilter
	 */
	@Accessor(qualifier = "userResultfilter", type = Accessor.Type.GETTER)
	public String getUserResultfilter()
	{
		if (this._userResultfilter!=null)
		{
			return _userResultfilter;
		}
		return _userResultfilter = getPersistenceContext().getValue(USERRESULTFILTER, _userResultfilter);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.userRootDN</code> attribute defined at extension <code>ldap</code>. 
	 * @return the userRootDN
	 */
	@Accessor(qualifier = "userRootDN", type = Accessor.Type.GETTER)
	public String getUserRootDN()
	{
		if (this._userRootDN!=null)
		{
			return _userRootDN;
		}
		return _userRootDN = getPersistenceContext().getValue(USERROOTDN, _userRootDN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFGroupImportCronJob.userSearchFieldQualifier</code> attribute defined at extension <code>ldap</code>. 
	 * @return the userSearchFieldQualifier
	 */
	@Accessor(qualifier = "userSearchFieldQualifier", type = Accessor.Type.GETTER)
	public String getUserSearchFieldQualifier()
	{
		if (this._userSearchFieldQualifier!=null)
		{
			return _userSearchFieldQualifier;
		}
		return _userSearchFieldQualifier = getPersistenceContext().getValue(USERSEARCHFIELDQUALIFIER, _userSearchFieldQualifier);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.codeExecution</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the codeExecution
	 */
	@Accessor(qualifier = "codeExecution", type = Accessor.Type.SETTER)
	public void setCodeExecution(final Boolean value)
	{
		_codeExecution = getPersistenceContext().setValue(CODEEXECUTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.configFile</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the configFile
	 */
	@Accessor(qualifier = "configFile", type = Accessor.Type.SETTER)
	public void setConfigFile(final ConfigurationMediaModel value)
	{
		_configFile = getPersistenceContext().setValue(CONFIGFILE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.destMedia</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the destMedia
	 */
	@Accessor(qualifier = "destMedia", type = Accessor.Type.SETTER)
	public void setDestMedia(final ImpExMediaModel value)
	{
		_destMedia = getPersistenceContext().setValue(DESTMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.dumpMedia</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the dumpMedia
	 */
	@Accessor(qualifier = "dumpMedia", type = Accessor.Type.SETTER)
	public void setDumpMedia(final ImpExMediaModel value)
	{
		_dumpMedia = getPersistenceContext().setValue(DUMPMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.importUsers</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the importUsers
	 */
	@Accessor(qualifier = "importUsers", type = Accessor.Type.SETTER)
	public void setImportUsers(final Boolean value)
	{
		_importUsers = getPersistenceContext().setValue(IMPORTUSERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.ldapquery</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the ldapquery
	 */
	@Accessor(qualifier = "ldapquery", type = Accessor.Type.SETTER)
	public void setLdapquery(final String value)
	{
		_ldapquery = getPersistenceContext().setValue(LDAPQUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.ldifFile</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the ldifFile
	 */
	@Accessor(qualifier = "ldifFile", type = Accessor.Type.SETTER)
	public void setLdifFile(final LDIFMediaModel value)
	{
		_ldifFile = getPersistenceContext().setValue(LDIFFILE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.resultfilter</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the resultfilter
	 */
	@Accessor(qualifier = "resultfilter", type = Accessor.Type.SETTER)
	public void setResultfilter(final String value)
	{
		_resultfilter = getPersistenceContext().setValue(RESULTFILTER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.searchbase</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the searchbase
	 */
	@Accessor(qualifier = "searchbase", type = Accessor.Type.SETTER)
	public void setSearchbase(final String value)
	{
		_searchbase = getPersistenceContext().setValue(SEARCHBASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.userResultfilter</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the userResultfilter
	 */
	@Accessor(qualifier = "userResultfilter", type = Accessor.Type.SETTER)
	public void setUserResultfilter(final String value)
	{
		_userResultfilter = getPersistenceContext().setValue(USERRESULTFILTER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.userRootDN</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the userRootDN
	 */
	@Accessor(qualifier = "userRootDN", type = Accessor.Type.SETTER)
	public void setUserRootDN(final String value)
	{
		_userRootDN = getPersistenceContext().setValue(USERROOTDN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFGroupImportCronJob.userSearchFieldQualifier</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the userSearchFieldQualifier
	 */
	@Accessor(qualifier = "userSearchFieldQualifier", type = Accessor.Type.SETTER)
	public void setUserSearchFieldQualifier(final String value)
	{
		_userSearchFieldQualifier = getPersistenceContext().setValue(USERSEARCHFIELDQUALIFIER, value);
	}
	
}
