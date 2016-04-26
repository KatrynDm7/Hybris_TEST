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
package de.hybris.platform.integration.cis.aspect;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.hybris.cis.api.subscription.model.CisSubscriptionProfileResult;
import com.hybris.cis.client.rest.subscription.SubscriptionClient;
import com.hybris.cis.client.rest.subscription.mock.SubscriptionMockClientImpl;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseFactory;


/**
 * Check if calls of
 * de.hybris.platform.integration.cis.subscription.service.impl.DefaultCisSubscriptionService.getCustomerProfile throws
 * com.hybris.cis.api.exception.ServiceRequestException. If so, store the exception message in sessionScopedMessages map by 'ServiceRequestExceptionMessage' key.
 *
 */
@Aspect
public class HandleServiceRequestExceptionAspect
{
	@Resource(name = "threadScopedMessages")
	private Map<String, String> threadScopedMessages; 
	
	private static final Logger LOG = Logger.getLogger(HandleServiceRequestExceptionAspect.class);
	
	@Around("execution(* com.hybris.cis.client.rest.subscription.mock.SubscriptionMockClientImpl.getProfile(..))")
	public Object aroundSubscriptionMockClientImpl_getProfile(final ProceedingJoinPoint joinPoint) throws Throwable
	{
		return processJoinPoint(joinPoint);
	}
	
	private Object processJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
		try
		{
			getThreadScopedMessages().remove("ServiceRequestExceptionMessage");
			return joinPoint.proceed();
		}
		catch (final com.hybris.cis.api.exception.ServiceRequestException ex)
		{
			getThreadScopedMessages().put("ServiceRequestExceptionMessage", ex.getMessage());
			LOG.info("ServiceRequestException was interrupted by de.hybris.platform.integration.cis.aspect.HandleServiceRequestExceptionAspect", ex);
		    return RestResponseFactory.<CisSubscriptionProfileResult>newStubInstance();
		}
	}

	public Map<String, String> getThreadScopedMessages() {
		return threadScopedMessages;
	}

	public void setThreadScopedMessages(Map<String, String> threadScopedMessages) {
		this.threadScopedMessages = threadScopedMessages;
	}
}
