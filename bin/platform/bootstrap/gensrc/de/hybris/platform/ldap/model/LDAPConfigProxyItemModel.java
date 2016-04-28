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
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ldap.enums.JNDIAuthenticationEnum;
import de.hybris.platform.ldap.enums.LDAPVersionEnum;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type LDAPConfigProxyItem first defined at extension ldap.
 */
@SuppressWarnings("all")
public class LDAPConfigProxyItemModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "LDAPConfigProxyItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.minimumFailbackTime</code> attribute defined at extension <code>ldap</code>. */
	public static final String MINIMUMFAILBACKTIME = "minimumFailbackTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.localAccountsOnly</code> attribute defined at extension <code>ldap</code>. */
	public static final String LOCALACCOUNTSONLY = "localAccountsOnly";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.poolInitsize</code> attribute defined at extension <code>ldap</code>. */
	public static final String POOLINITSIZE = "poolInitsize";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.poolPrefsize</code> attribute defined at extension <code>ldap</code>. */
	public static final String POOLPREFSIZE = "poolPrefsize";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.poolMaxsize</code> attribute defined at extension <code>ldap</code>. */
	public static final String POOLMAXSIZE = "poolMaxsize";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.poolTimeout</code> attribute defined at extension <code>ldap</code>. */
	public static final String POOLTIMEOUT = "poolTimeout";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.poolEnabled</code> attribute defined at extension <code>ldap</code>. */
	public static final String POOLENABLED = "poolEnabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.loginField</code> attribute defined at extension <code>ldap</code>. */
	public static final String LOGINFIELD = "loginField";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiFactory</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDIFACTORY = "jndiFactory";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.socketFactory</code> attribute defined at extension <code>ldap</code>. */
	public static final String SOCKETFACTORY = "socketFactory";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.cacerts</code> attribute defined at extension <code>ldap</code>. */
	public static final String CACERTS = "cacerts";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.clientcerts</code> attribute defined at extension <code>ldap</code>. */
	public static final String CLIENTCERTS = "clientcerts";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.caKeystorePwd</code> attribute defined at extension <code>ldap</code>. */
	public static final String CAKEYSTOREPWD = "caKeystorePwd";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.clientKeystorePwd</code> attribute defined at extension <code>ldap</code>. */
	public static final String CLIENTKEYSTOREPWD = "clientKeystorePwd";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.caKeystoreType</code> attribute defined at extension <code>ldap</code>. */
	public static final String CAKEYSTORETYPE = "caKeystoreType";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.clientKeystoreType</code> attribute defined at extension <code>ldap</code>. */
	public static final String CLIENTKEYSTORETYPE = "clientKeystoreType";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiVersion</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDIVERSION = "jndiVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiAuthentication</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDIAUTHENTICATION = "jndiAuthentication";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiPrincipals</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDIPRINCIPALS = "jndiPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiCredentials</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDICREDENTIALS = "jndiCredentials";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.securityProtocol</code> attribute defined at extension <code>ldap</code>. */
	public static final String SECURITYPROTOCOL = "securityProtocol";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.serverUrl</code> attribute defined at extension <code>ldap</code>. */
	public static final String SERVERURL = "serverUrl";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.serverRootDN</code> attribute defined at extension <code>ldap</code>. */
	public static final String SERVERROOTDN = "serverRootDN";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiConnectTimeout</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDICONNECTTIMEOUT = "jndiConnectTimeout";
	
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.minimumFailbackTime</code> attribute defined at extension <code>ldap</code>. */
	private Integer _minimumFailbackTime;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.localAccountsOnly</code> attribute defined at extension <code>ldap</code>. */
	private Collection<PrincipalModel> _localAccountsOnly;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.poolInitsize</code> attribute defined at extension <code>ldap</code>. */
	private Integer _poolInitsize;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.poolPrefsize</code> attribute defined at extension <code>ldap</code>. */
	private Integer _poolPrefsize;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.poolMaxsize</code> attribute defined at extension <code>ldap</code>. */
	private Integer _poolMaxsize;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.poolTimeout</code> attribute defined at extension <code>ldap</code>. */
	private Integer _poolTimeout;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.poolEnabled</code> attribute defined at extension <code>ldap</code>. */
	private Boolean _poolEnabled;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.loginField</code> attribute defined at extension <code>ldap</code>. */
	private String _loginField;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.jndiFactory</code> attribute defined at extension <code>ldap</code>. */
	private String _jndiFactory;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.socketFactory</code> attribute defined at extension <code>ldap</code>. */
	private String _socketFactory;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.cacerts</code> attribute defined at extension <code>ldap</code>. */
	private String _cacerts;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.clientcerts</code> attribute defined at extension <code>ldap</code>. */
	private String _clientcerts;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.caKeystorePwd</code> attribute defined at extension <code>ldap</code>. */
	private String _caKeystorePwd;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.clientKeystorePwd</code> attribute defined at extension <code>ldap</code>. */
	private String _clientKeystorePwd;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.caKeystoreType</code> attribute defined at extension <code>ldap</code>. */
	private String _caKeystoreType;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.clientKeystoreType</code> attribute defined at extension <code>ldap</code>. */
	private String _clientKeystoreType;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.jndiVersion</code> attribute defined at extension <code>ldap</code>. */
	private LDAPVersionEnum _jndiVersion;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.jndiAuthentication</code> attribute defined at extension <code>ldap</code>. */
	private JNDIAuthenticationEnum _jndiAuthentication;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.jndiPrincipals</code> attribute defined at extension <code>ldap</code>. */
	private String _jndiPrincipals;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.jndiCredentials</code> attribute defined at extension <code>ldap</code>. */
	private String _jndiCredentials;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.securityProtocol</code> attribute defined at extension <code>ldap</code>. */
	private String _securityProtocol;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.serverUrl</code> attribute defined at extension <code>ldap</code>. */
	private Collection<String> _serverUrl;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.serverRootDN</code> attribute defined at extension <code>ldap</code>. */
	private String _serverRootDN;
	
	/** <i>Generated variable</i> - Variable of <code>LDAPConfigProxyItem.jndiConnectTimeout</code> attribute defined at extension <code>ldap</code>. */
	private Integer _jndiConnectTimeout;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public LDAPConfigProxyItemModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public LDAPConfigProxyItemModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _loginField initial attribute declared by type <code>LDAPConfigProxyItem</code> at extension <code>ldap</code>
	 * @param _serverUrl initial attribute declared by type <code>LDAPConfigProxyItem</code> at extension <code>ldap</code>
	 */
	@Deprecated
	public LDAPConfigProxyItemModel(final String _loginField, final Collection<String> _serverUrl)
	{
		super();
		setLoginField(_loginField);
		setServerUrl(_serverUrl);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _loginField initial attribute declared by type <code>LDAPConfigProxyItem</code> at extension <code>ldap</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _serverUrl initial attribute declared by type <code>LDAPConfigProxyItem</code> at extension <code>ldap</code>
	 */
	@Deprecated
	public LDAPConfigProxyItemModel(final String _loginField, final ItemModel _owner, final Collection<String> _serverUrl)
	{
		super();
		setLoginField(_loginField);
		setOwner(_owner);
		setServerUrl(_serverUrl);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.cacerts</code> attribute defined at extension <code>ldap</code>. 
	 * @return the cacerts
	 */
	@Accessor(qualifier = "cacerts", type = Accessor.Type.GETTER)
	public String getCacerts()
	{
		if (this._cacerts!=null)
		{
			return _cacerts;
		}
		return _cacerts = getPersistenceContext().getValue(CACERTS, _cacerts);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.caKeystorePwd</code> attribute defined at extension <code>ldap</code>. 
	 * @return the caKeystorePwd
	 */
	@Accessor(qualifier = "caKeystorePwd", type = Accessor.Type.GETTER)
	public String getCaKeystorePwd()
	{
		if (this._caKeystorePwd!=null)
		{
			return _caKeystorePwd;
		}
		return _caKeystorePwd = getPersistenceContext().getValue(CAKEYSTOREPWD, _caKeystorePwd);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.caKeystoreType</code> attribute defined at extension <code>ldap</code>. 
	 * @return the caKeystoreType
	 */
	@Accessor(qualifier = "caKeystoreType", type = Accessor.Type.GETTER)
	public String getCaKeystoreType()
	{
		if (this._caKeystoreType!=null)
		{
			return _caKeystoreType;
		}
		return _caKeystoreType = getPersistenceContext().getValue(CAKEYSTORETYPE, _caKeystoreType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.clientcerts</code> attribute defined at extension <code>ldap</code>. 
	 * @return the clientcerts
	 */
	@Accessor(qualifier = "clientcerts", type = Accessor.Type.GETTER)
	public String getClientcerts()
	{
		if (this._clientcerts!=null)
		{
			return _clientcerts;
		}
		return _clientcerts = getPersistenceContext().getValue(CLIENTCERTS, _clientcerts);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.clientKeystorePwd</code> attribute defined at extension <code>ldap</code>. 
	 * @return the clientKeystorePwd
	 */
	@Accessor(qualifier = "clientKeystorePwd", type = Accessor.Type.GETTER)
	public String getClientKeystorePwd()
	{
		if (this._clientKeystorePwd!=null)
		{
			return _clientKeystorePwd;
		}
		return _clientKeystorePwd = getPersistenceContext().getValue(CLIENTKEYSTOREPWD, _clientKeystorePwd);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.clientKeystoreType</code> attribute defined at extension <code>ldap</code>. 
	 * @return the clientKeystoreType
	 */
	@Accessor(qualifier = "clientKeystoreType", type = Accessor.Type.GETTER)
	public String getClientKeystoreType()
	{
		if (this._clientKeystoreType!=null)
		{
			return _clientKeystoreType;
		}
		return _clientKeystoreType = getPersistenceContext().getValue(CLIENTKEYSTORETYPE, _clientKeystoreType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiAuthentication</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiAuthentication
	 */
	@Accessor(qualifier = "jndiAuthentication", type = Accessor.Type.GETTER)
	public JNDIAuthenticationEnum getJndiAuthentication()
	{
		if (this._jndiAuthentication!=null)
		{
			return _jndiAuthentication;
		}
		return _jndiAuthentication = getPersistenceContext().getValue(JNDIAUTHENTICATION, _jndiAuthentication);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiConnectTimeout</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiConnectTimeout
	 */
	@Accessor(qualifier = "jndiConnectTimeout", type = Accessor.Type.GETTER)
	public Integer getJndiConnectTimeout()
	{
		if (this._jndiConnectTimeout!=null)
		{
			return _jndiConnectTimeout;
		}
		return _jndiConnectTimeout = getPersistenceContext().getValue(JNDICONNECTTIMEOUT, _jndiConnectTimeout);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiCredentials</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiCredentials
	 */
	@Accessor(qualifier = "jndiCredentials", type = Accessor.Type.GETTER)
	public String getJndiCredentials()
	{
		if (this._jndiCredentials!=null)
		{
			return _jndiCredentials;
		}
		return _jndiCredentials = getPersistenceContext().getValue(JNDICREDENTIALS, _jndiCredentials);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiFactory</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiFactory
	 */
	@Accessor(qualifier = "jndiFactory", type = Accessor.Type.GETTER)
	public String getJndiFactory()
	{
		if (this._jndiFactory!=null)
		{
			return _jndiFactory;
		}
		return _jndiFactory = getPersistenceContext().getValue(JNDIFACTORY, _jndiFactory);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiPrincipals</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiPrincipals
	 */
	@Accessor(qualifier = "jndiPrincipals", type = Accessor.Type.GETTER)
	public String getJndiPrincipals()
	{
		if (this._jndiPrincipals!=null)
		{
			return _jndiPrincipals;
		}
		return _jndiPrincipals = getPersistenceContext().getValue(JNDIPRINCIPALS, _jndiPrincipals);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiVersion</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiVersion
	 */
	@Accessor(qualifier = "jndiVersion", type = Accessor.Type.GETTER)
	public LDAPVersionEnum getJndiVersion()
	{
		if (this._jndiVersion!=null)
		{
			return _jndiVersion;
		}
		return _jndiVersion = getPersistenceContext().getValue(JNDIVERSION, _jndiVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.localAccountsOnly</code> attribute defined at extension <code>ldap</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the localAccountsOnly
	 */
	@Accessor(qualifier = "localAccountsOnly", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getLocalAccountsOnly()
	{
		if (this._localAccountsOnly!=null)
		{
			return _localAccountsOnly;
		}
		return _localAccountsOnly = getPersistenceContext().getValue(LOCALACCOUNTSONLY, _localAccountsOnly);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.loginField</code> attribute defined at extension <code>ldap</code>. 
	 * @return the loginField
	 */
	@Accessor(qualifier = "loginField", type = Accessor.Type.GETTER)
	public String getLoginField()
	{
		if (this._loginField!=null)
		{
			return _loginField;
		}
		return _loginField = getPersistenceContext().getValue(LOGINFIELD, _loginField);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.minimumFailbackTime</code> attribute defined at extension <code>ldap</code>. 
	 * @return the minimumFailbackTime
	 */
	@Accessor(qualifier = "minimumFailbackTime", type = Accessor.Type.GETTER)
	public Integer getMinimumFailbackTime()
	{
		if (this._minimumFailbackTime!=null)
		{
			return _minimumFailbackTime;
		}
		return _minimumFailbackTime = getPersistenceContext().getValue(MINIMUMFAILBACKTIME, _minimumFailbackTime);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.poolEnabled</code> attribute defined at extension <code>ldap</code>. 
	 * @return the poolEnabled
	 */
	@Accessor(qualifier = "poolEnabled", type = Accessor.Type.GETTER)
	public Boolean getPoolEnabled()
	{
		if (this._poolEnabled!=null)
		{
			return _poolEnabled;
		}
		return _poolEnabled = getPersistenceContext().getValue(POOLENABLED, _poolEnabled);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.poolInitsize</code> attribute defined at extension <code>ldap</code>. 
	 * @return the poolInitsize
	 */
	@Accessor(qualifier = "poolInitsize", type = Accessor.Type.GETTER)
	public Integer getPoolInitsize()
	{
		if (this._poolInitsize!=null)
		{
			return _poolInitsize;
		}
		return _poolInitsize = getPersistenceContext().getValue(POOLINITSIZE, _poolInitsize);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.poolMaxsize</code> attribute defined at extension <code>ldap</code>. 
	 * @return the poolMaxsize
	 */
	@Accessor(qualifier = "poolMaxsize", type = Accessor.Type.GETTER)
	public Integer getPoolMaxsize()
	{
		if (this._poolMaxsize!=null)
		{
			return _poolMaxsize;
		}
		return _poolMaxsize = getPersistenceContext().getValue(POOLMAXSIZE, _poolMaxsize);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.poolPrefsize</code> attribute defined at extension <code>ldap</code>. 
	 * @return the poolPrefsize
	 */
	@Accessor(qualifier = "poolPrefsize", type = Accessor.Type.GETTER)
	public Integer getPoolPrefsize()
	{
		if (this._poolPrefsize!=null)
		{
			return _poolPrefsize;
		}
		return _poolPrefsize = getPersistenceContext().getValue(POOLPREFSIZE, _poolPrefsize);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.poolTimeout</code> attribute defined at extension <code>ldap</code>. 
	 * @return the poolTimeout
	 */
	@Accessor(qualifier = "poolTimeout", type = Accessor.Type.GETTER)
	public Integer getPoolTimeout()
	{
		if (this._poolTimeout!=null)
		{
			return _poolTimeout;
		}
		return _poolTimeout = getPersistenceContext().getValue(POOLTIMEOUT, _poolTimeout);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.securityProtocol</code> attribute defined at extension <code>ldap</code>. 
	 * @return the securityProtocol
	 */
	@Accessor(qualifier = "securityProtocol", type = Accessor.Type.GETTER)
	public String getSecurityProtocol()
	{
		if (this._securityProtocol!=null)
		{
			return _securityProtocol;
		}
		return _securityProtocol = getPersistenceContext().getValue(SECURITYPROTOCOL, _securityProtocol);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.serverRootDN</code> attribute defined at extension <code>ldap</code>. 
	 * @return the serverRootDN
	 */
	@Accessor(qualifier = "serverRootDN", type = Accessor.Type.GETTER)
	public String getServerRootDN()
	{
		if (this._serverRootDN!=null)
		{
			return _serverRootDN;
		}
		return _serverRootDN = getPersistenceContext().getValue(SERVERROOTDN, _serverRootDN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.serverUrl</code> attribute defined at extension <code>ldap</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the serverUrl
	 */
	@Accessor(qualifier = "serverUrl", type = Accessor.Type.GETTER)
	public Collection<String> getServerUrl()
	{
		if (this._serverUrl!=null)
		{
			return _serverUrl;
		}
		return _serverUrl = getPersistenceContext().getValue(SERVERURL, _serverUrl);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.socketFactory</code> attribute defined at extension <code>ldap</code>. 
	 * @return the socketFactory
	 */
	@Accessor(qualifier = "socketFactory", type = Accessor.Type.GETTER)
	public String getSocketFactory()
	{
		if (this._socketFactory!=null)
		{
			return _socketFactory;
		}
		return _socketFactory = getPersistenceContext().getValue(SOCKETFACTORY, _socketFactory);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.cacerts</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the cacerts
	 */
	@Accessor(qualifier = "cacerts", type = Accessor.Type.SETTER)
	public void setCacerts(final String value)
	{
		_cacerts = getPersistenceContext().setValue(CACERTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.caKeystorePwd</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the caKeystorePwd
	 */
	@Accessor(qualifier = "caKeystorePwd", type = Accessor.Type.SETTER)
	public void setCaKeystorePwd(final String value)
	{
		_caKeystorePwd = getPersistenceContext().setValue(CAKEYSTOREPWD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.caKeystoreType</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the caKeystoreType
	 */
	@Accessor(qualifier = "caKeystoreType", type = Accessor.Type.SETTER)
	public void setCaKeystoreType(final String value)
	{
		_caKeystoreType = getPersistenceContext().setValue(CAKEYSTORETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.clientcerts</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the clientcerts
	 */
	@Accessor(qualifier = "clientcerts", type = Accessor.Type.SETTER)
	public void setClientcerts(final String value)
	{
		_clientcerts = getPersistenceContext().setValue(CLIENTCERTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.clientKeystorePwd</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the clientKeystorePwd
	 */
	@Accessor(qualifier = "clientKeystorePwd", type = Accessor.Type.SETTER)
	public void setClientKeystorePwd(final String value)
	{
		_clientKeystorePwd = getPersistenceContext().setValue(CLIENTKEYSTOREPWD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.clientKeystoreType</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the clientKeystoreType
	 */
	@Accessor(qualifier = "clientKeystoreType", type = Accessor.Type.SETTER)
	public void setClientKeystoreType(final String value)
	{
		_clientKeystoreType = getPersistenceContext().setValue(CLIENTKEYSTORETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiAuthentication</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiAuthentication
	 */
	@Accessor(qualifier = "jndiAuthentication", type = Accessor.Type.SETTER)
	public void setJndiAuthentication(final JNDIAuthenticationEnum value)
	{
		_jndiAuthentication = getPersistenceContext().setValue(JNDIAUTHENTICATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiConnectTimeout</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiConnectTimeout
	 */
	@Accessor(qualifier = "jndiConnectTimeout", type = Accessor.Type.SETTER)
	public void setJndiConnectTimeout(final Integer value)
	{
		_jndiConnectTimeout = getPersistenceContext().setValue(JNDICONNECTTIMEOUT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiCredentials</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiCredentials
	 */
	@Accessor(qualifier = "jndiCredentials", type = Accessor.Type.SETTER)
	public void setJndiCredentials(final String value)
	{
		_jndiCredentials = getPersistenceContext().setValue(JNDICREDENTIALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiFactory</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiFactory
	 */
	@Accessor(qualifier = "jndiFactory", type = Accessor.Type.SETTER)
	public void setJndiFactory(final String value)
	{
		_jndiFactory = getPersistenceContext().setValue(JNDIFACTORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiPrincipals</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiPrincipals
	 */
	@Accessor(qualifier = "jndiPrincipals", type = Accessor.Type.SETTER)
	public void setJndiPrincipals(final String value)
	{
		_jndiPrincipals = getPersistenceContext().setValue(JNDIPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiVersion</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiVersion
	 */
	@Accessor(qualifier = "jndiVersion", type = Accessor.Type.SETTER)
	public void setJndiVersion(final LDAPVersionEnum value)
	{
		_jndiVersion = getPersistenceContext().setValue(JNDIVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.localAccountsOnly</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the localAccountsOnly
	 */
	@Accessor(qualifier = "localAccountsOnly", type = Accessor.Type.SETTER)
	public void setLocalAccountsOnly(final Collection<PrincipalModel> value)
	{
		_localAccountsOnly = getPersistenceContext().setValue(LOCALACCOUNTSONLY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.loginField</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the loginField
	 */
	@Accessor(qualifier = "loginField", type = Accessor.Type.SETTER)
	public void setLoginField(final String value)
	{
		_loginField = getPersistenceContext().setValue(LOGINFIELD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.minimumFailbackTime</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the minimumFailbackTime
	 */
	@Accessor(qualifier = "minimumFailbackTime", type = Accessor.Type.SETTER)
	public void setMinimumFailbackTime(final Integer value)
	{
		_minimumFailbackTime = getPersistenceContext().setValue(MINIMUMFAILBACKTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.poolEnabled</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the poolEnabled
	 */
	@Accessor(qualifier = "poolEnabled", type = Accessor.Type.SETTER)
	public void setPoolEnabled(final Boolean value)
	{
		_poolEnabled = getPersistenceContext().setValue(POOLENABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.poolInitsize</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the poolInitsize
	 */
	@Accessor(qualifier = "poolInitsize", type = Accessor.Type.SETTER)
	public void setPoolInitsize(final Integer value)
	{
		_poolInitsize = getPersistenceContext().setValue(POOLINITSIZE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.poolMaxsize</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the poolMaxsize
	 */
	@Accessor(qualifier = "poolMaxsize", type = Accessor.Type.SETTER)
	public void setPoolMaxsize(final Integer value)
	{
		_poolMaxsize = getPersistenceContext().setValue(POOLMAXSIZE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.poolPrefsize</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the poolPrefsize
	 */
	@Accessor(qualifier = "poolPrefsize", type = Accessor.Type.SETTER)
	public void setPoolPrefsize(final Integer value)
	{
		_poolPrefsize = getPersistenceContext().setValue(POOLPREFSIZE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.poolTimeout</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the poolTimeout
	 */
	@Accessor(qualifier = "poolTimeout", type = Accessor.Type.SETTER)
	public void setPoolTimeout(final Integer value)
	{
		_poolTimeout = getPersistenceContext().setValue(POOLTIMEOUT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.securityProtocol</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the securityProtocol
	 */
	@Accessor(qualifier = "securityProtocol", type = Accessor.Type.SETTER)
	public void setSecurityProtocol(final String value)
	{
		_securityProtocol = getPersistenceContext().setValue(SECURITYPROTOCOL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.serverRootDN</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the serverRootDN
	 */
	@Accessor(qualifier = "serverRootDN", type = Accessor.Type.SETTER)
	public void setServerRootDN(final String value)
	{
		_serverRootDN = getPersistenceContext().setValue(SERVERROOTDN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.serverUrl</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the serverUrl
	 */
	@Accessor(qualifier = "serverUrl", type = Accessor.Type.SETTER)
	public void setServerUrl(final Collection<String> value)
	{
		_serverUrl = getPersistenceContext().setValue(SERVERURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.socketFactory</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the socketFactory
	 */
	@Accessor(qualifier = "socketFactory", type = Accessor.Type.SETTER)
	public void setSocketFactory(final String value)
	{
		_socketFactory = getPersistenceContext().setValue(SOCKETFACTORY, value);
	}
	
}
