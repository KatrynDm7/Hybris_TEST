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
package de.hybris.platform.yacceleratorstorefront.filters;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorstorefrontcommons.history.BrowseHistory;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.CookieGenerator;


@UnitTest
public class StorefrontFilterTest
{
	private static final String REQUESTEDURL = "http://requestedurl.hybris.de";

	private StorefrontFilter filter;

	@Mock
	private BrowseHistory browseHistory;

	@Mock
	private CookieGenerator cookieGenerator;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private HttpSession session;

	@Mock
	private FilterChain filterChain;

	@Mock
	private StoreSessionFacade storeSessionFacade;

	@Mock
	private Enumeration<Locale> locales;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration configuration;


	@Before
	public void initFilter()
	{
		MockitoAnnotations.initMocks(this);
		filter = new StorefrontFilter();
		filter.setBrowseHistory(browseHistory);
		filter.setCookieGenerator(cookieGenerator);
		filter.setStoreSessionFacade(storeSessionFacade);

		Mockito.when(request.getSession()).thenReturn(session);
		Mockito.when(request.getLocales()).thenReturn(locales);
		final StringBuffer requestUrlSb = new StringBuffer();
		requestUrlSb.append(REQUESTEDURL);
		Mockito.when(request.getRequestURL()).thenReturn(requestUrlSb);
		Mockito.when(request.getRequestURI()).thenReturn(requestUrlSb.toString());


	}

	@Test
	public void shouldStoreOriginalRefererOnGET() throws IOException, ServletException
	{
		Mockito.when(request.getMethod()).thenReturn(HttpMethod.GET.toString());
		filter.doFilterInternal(request, response, filterChain);
		Mockito.verify(session).setAttribute(StorefrontFilter.ORIGINAL_REFERER, REQUESTEDURL);
	}

	@Test
	public void shouldNotStoreOriginalRefererOnPOST() throws IOException, ServletException
	{
		Mockito.when(request.getMethod()).thenReturn(HttpMethod.POST.toString());
		filter.doFilterInternal(request, response, filterChain);
		Mockito.verify(session, Mockito.never()).setAttribute(StorefrontFilter.ORIGINAL_REFERER, REQUESTEDURL);
	}

	@Test
	public void shouldNotStoreOriginalRefererOnAjax() throws IOException, ServletException
	{
		Mockito.when(request.getMethod()).thenReturn(HttpMethod.GET.toString());
		Mockito.when(request.getHeader(StorefrontFilter.AJAX_REQUEST_HEADER_NAME)).thenReturn("1");
		filter.doFilterInternal(request, response, filterChain);
		Mockito.verify(session, Mockito.never()).setAttribute(StorefrontFilter.ORIGINAL_REFERER, REQUESTEDURL);
	}


}
