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
package com.sap.wec.adtreco.btg;

import java.io.IOException;
import java.net.URISyntaxException;

import de.hybris.platform.btg.condition.operand.types.StringSet;
import de.hybris.platform.btg.condition.operand.valueproviders.CollectionOperandValueProvider;
import de.hybris.platform.btg.enums.BTGConditionEvaluationScope;
import de.hybris.platform.cockpit.model.editor.impl.DefaultSAPInitiativeUIEditor;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sap.wec.adtreco.model.BTGSAPInitiativeOperandModel;
import com.sap.wec.adtreco.bo.ADTUserIdProvider;
import com.sap.wec.adtreco.bo.CookieHelper;
import com.sap.wec.adtreco.bo.impl.SAPInitiative;
import com.sap.wec.adtreco.bo.intf.SAPInitiativeReader;


/**
 *
 */
public class SAPInitiativeValueProvider implements CollectionOperandValueProvider<BTGSAPInitiativeOperandModel>
{
	private static final Logger LOG = Logger.getLogger(DefaultSAPInitiativeUIEditor.class); // NOPMD
	private static final String INITIATIVES_PREFIX = "Initiatives_";
	protected SAPInitiativeReader sapInitiativeReader;
	protected ADTUserIdProvider userIdProvider;
	protected SessionService sessionService;

	@Override
	public Object getValue(final BTGSAPInitiativeOperandModel operand, final UserModel user,
			final BTGConditionEvaluationScope scope)
	{
		List<String> result = new ArrayList<String>();
	
		if (user != null)
		{
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			final String cookieId = CookieHelper.getPiwikId(request);
			final String userId = userIdProvider.getUserId(user);	
			
			if(cookieId != null)
			{
				if(userId == null)
				{
					//Only a Piwik ID is available, this is an anonymous user
					if (sessionService.getAttribute(INITIATIVES_PREFIX + cookieId) != null)
					{
						result = sessionService.getAttribute(INITIATIVES_PREFIX + cookieId);
					}				
					else
					{
						result = this.getValueForUser(cookieId, true);
					}
				}
				else
				{
					//User is logged on and has a Piwik ID
					if (sessionService.getAttribute(INITIATIVES_PREFIX + cookieId + userId) != null)
					{
						result = sessionService.getAttribute(INITIATIVES_PREFIX + cookieId + userId);
					}				
					else
					{
						String[] userIds = new String[2];
						userIds[0] = cookieId;
						userIds[1] = userId;
						result = this.getValueForMultiUsers(userIds);

					}									
				}
			}
			else
			{
				//No Piwik ID is provided in the browser
				if (userId != null)
				{
					//User is logged on and has no Piwik ID
					if (sessionService.getAttribute(INITIATIVES_PREFIX + userId) != null)
					{
						result = sessionService.getAttribute(INITIATIVES_PREFIX + userId);
					}
					else
					{
						result = getValueForUser(userId, false);
					}
				}				
			}
		}

		return new StringSet(result);
	}
	
	private List<String> getValueForUser(String userId, boolean isAnon){
		
		List<String> result = new ArrayList<String>();
		List<SAPInitiative> initiativesForBP = new ArrayList<SAPInitiative>();
		
		try
		{	
			initiativesForBP = sapInitiativeReader.searchInitiativesForBP(userId, isAnon);
			if (initiativesForBP.size() > 0)
			{
				sessionService.setAttribute(INITIATIVES_PREFIX + userId, result);
				for (final SAPInitiative initiative : initiativesForBP)
				{
					result.add(initiative.getId());
				}
			}
			else
			{
				//If the logged on user doesn't belong to any campaigns, a "null" value is placed in cache
				//to avoid making the check against the backend again within the session
				sessionService.setAttribute(INITIATIVES_PREFIX + userId, new ArrayList<String>());
			}
		}
		catch (final URISyntaxException ex)
		{
			LOG.error("Connection to backend system failed due to wrong URI syntax", ex);
		}
		catch (final ODataException ex)
		{
			LOG.error("HTTP Destination is not configured correctly", ex);
		}
		catch (final IOException ex)
		{
			LOG.error("Connection to backend system failed", ex);
		}
		catch (final RuntimeException ex)
		{
			LOG.error("Runtime Error in the backend", ex);
		}
		
		return result;
	}
	
	private List<String> getValueForMultiUsers(String[] users){
		
		List<String> result = new ArrayList<String>();
		List<SAPInitiative> initiativesForMultiBP = new ArrayList<SAPInitiative>();
		
		try
		{					
			initiativesForMultiBP = sapInitiativeReader.searchInitiativesForMultiBP(users);
			if (initiativesForMultiBP.size() > 0)
			{								
				for (final SAPInitiative initiative : initiativesForMultiBP)
				{
					result.add(initiative.getId());
				}
				sessionService.setAttribute(INITIATIVES_PREFIX + users[0] + users[1], result);
			}
			else
			{
				//If the logged on user doesn't belong to any campaigns, a "null" value is placed in cache
				//to avoid making the check against the backend again within the session
				sessionService.setAttribute(INITIATIVES_PREFIX + users[0] + users[1], new ArrayList<String>());
			}
		}
		catch (final URISyntaxException ex)
		{
			LOG.error("Connection to backend system failed due to wrong URI syntax", ex);
		}
		catch (final ODataException ex)
		{
			LOG.error("HTTP Destination is not configured correctly", ex);
		}
		catch (final IOException ex)
		{
			LOG.error("Connection to backend system failed", ex);
		}
		catch (final RuntimeException ex)
		{
			LOG.error("Runtime Error in the backend", ex);
		}
		
		return result;
		
	}

	@Override
	public Class getValueType(final BTGSAPInitiativeOperandModel operand)
	{
		return SAPInitiativeSet.class;
	}

	@Override
	public Class getAtomicValueType(final BTGSAPInitiativeOperandModel operand)
	{
		return String.class;
	}

	/**
	 * @return the sapInitiativeReader
	 */
	public SAPInitiativeReader getSapInitiativeReader()
	{
		return sapInitiativeReader;
	}

	/**
	 * @param sapInitiativeReader
	 *           the sapInitiativeReader to set
	 */
	public void setSapInitiativeReader(final SAPInitiativeReader sapInitiativeReader)
	{
		this.sapInitiativeReader = sapInitiativeReader;
	}

	/**
	 * @return the userIdProvider
	 */
	public ADTUserIdProvider getUserIdProvider()
	{
		return userIdProvider;
	}

	/**
	 * @param userIdProvider
	 *           the userIdProvider to set
	 */
	public void setUserIdProvider(final ADTUserIdProvider userIdProvider)
	{
		this.userIdProvider = userIdProvider;
	}

	public SessionService getsessionService()
	{
		return sessionService;
	}

	public void setsessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
