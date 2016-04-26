/*
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
 *
 */
package de.hybris.platform.assistedservicefacades.constants;



/**
 * Global class for all Assistedservicefacades constants. You can add global constants for your extension into this
 * class.
 */
public final class AssistedservicefacadesConstants extends GeneratedAssistedservicefacadesConstants
{
	public static final String EXTENSIONNAME = "assistedservicefacades";

	/* Default constants */

	// Key to session parameters map for AsmSession object
	public static final String ASM_SESSION_PARAMETER = "ASM";

	// Default parent group id for all AS agents
	public static final String AS_AGENT_GROUP_UID = "asagentgroup";

	// Default group id for all AS sales manager agents
	public static final String AS_MANAGER_AGENT_GROUP_UID = "asagentsalesmanagergroup";


	/* Config properties */

	// Session timeout for AS agent. Session will be killed after timeout expired
	public static final String ASM_AGENT_SESSION_TIMEOUT = "assistedservicefacades.agentsession.timeout";

	// Timer time in secs to be displayed in AS widget before agent will be logged out automatically
	public static final String ASM_AGENT_SESSION_TIMER = "assistedservicefacades.agentsession.timer";

	// Whether display or not create customer option in ASM widget
	public static final String CREATE_DISABLED_PROPERTY = "assistedservicefacades.createDisabled";

	// Whether or not provide an ability to login via https login form
	public static final String AS_ENABLE_FORM_LOGIN = "assistedservicestorefront.https-form-login";

	// Number of wrong passwords attempt before AS agent will be blocked for login
	public static final String AS_BAD_ATTEMPTS_BEFORE_DISABLE = "assistedservicefacades.bad-login-attempts";

	// SSO cookie name with token
	public static final String SSO_COOKIE_NAME = "sso.cookie.name";

	private AssistedservicefacadesConstants()
	{
		//empty to avoid instantiating this constant class
	}
}