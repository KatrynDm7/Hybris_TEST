/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.servicelayer.web;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.web.XSSFilter.XSSFilterConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


@UnitTest
public class XSSFilterTest
{
	private XSSFilter strippingFilter;
	private XSSFilter rejectingFilter;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private MockFilterChain filterChain;

	@Before
	public void testSetup() throws Exception
	{
		strippingFilter = new XSSFilter();
		strippingFilter.initFromConfig(createMockFilterConfig(true, createDefaultRulePatternDefinitions(), XSSMatchAction.STRIP));

		rejectingFilter = new XSSFilter();
		rejectingFilter.initFromConfig(createMockFilterConfig(true, createDefaultRulePatternDefinitions(), XSSMatchAction.REJECT));

		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		filterChain = new MockFilterChain();
	}


	protected Map<String, String> createDefaultRulePatternDefinitions()
	{
		final Map<String, String> patterns = new LinkedHashMap<>();
		patterns.put("xss.filter.rule.script_fragments", "(?i)<script>(.*?)</script>");
		patterns.put("xss.filter.rule.src", "(?ims)[\\s\r\n]{1}src[\\s\r\n]*=[\r\n\\s]*'(.*?)'");
		patterns.put("xss.filter.rule.lonely_script_tags", "(?i)</script>");
		patterns.put("xss.filter.rule.lonely_script_tags2", "(?ims)<script(.*?)>");
		patterns.put("xss.filter.rule.eval", "(?ims)eval\\((.*?)\\)");
		patterns.put("xss.filter.rule.expression", "(?ims)expression\\((.*?)\\)");
		patterns.put("xss.filter.rule.javascript", "(?i)javascript:");
		patterns.put("xss.filter.rule.vbscript", "(?i)vbscript:");
		patterns.put("xss.filter.rule.onload", "(?ims)onload(.*?)=");
		return patterns;
	}

	@Test
	public void testLegalParamsForStrippingFilter() throws IOException, ServletException
	{
		testLegalParams(strippingFilter);
	}

	@Test
	public void testLegalParamsForRejectingFilter() throws IOException, ServletException
	{
		testLegalParams(rejectingFilter);
	}

	protected void testLegalParams(final XSSFilter filter) throws IOException, ServletException
	{
		final String[] legalValue1 = new String[]
		{ "hello world! How about src script eval-uation ?", "I've can see your \nexpression onload that bloody vbscript !",
				"\n\r\tasldkl sad asjdnalsd" };

		final String[] legalValue2 = new String[]
		{ "single value as well" };
		final Map<String, String[]> expectedParameterMap = new HashMap<String, String[]>();
		expectedParameterMap.put("legal1", legalValue1);
		expectedParameterMap.put("legal2", legalValue2);

		request.setParameters(expectedParameterMap);

		filter.doFilter(request, response, filterChain);

		final HttpServletRequest wrappedRequest = (HttpServletRequest) filterChain.getRequest();

		assertSame(wrappedRequest.getParameterValues("legal1"), legalValue1);
		assertSame(wrappedRequest.getParameterValues("legal2"), legalValue2);
		assertSame(wrappedRequest.getParameter("legal2"), legalValue2[0]);
		assertEquals(expectedParameterMap, wrappedRequest.getParameterMap());
	}

	@Test
	public void testIllegalParamsForStrippingFilter() throws IOException, ServletException
	{
		final String[][] illegalValues1 =
		{
				{ ">>><script>whatever <p/> in here is not legal (how about newline?) </script><<<",
						">>> src\n \r=\n 'http://google.de'<<<", ">>></ScRiPt><<<" },
				{ ">>><<<", ">>><<<", ">>><<<" } };
		final String[][] illegalValues2 =
		{
		{ ">>><ScRiPtwhat ever\n may come ou\r way><<<" },
		{ ">>><<<" } };

		final Map<String, String[]> illegalParameterMap = new HashMap<String, String[]>();
		illegalParameterMap.put("illegal1", illegalValues1[0]);
		illegalParameterMap.put("illegal2", illegalValues2[0]);

		final Map<String, String[]> expectedParameterMap = new HashMap<String, String[]>();
		expectedParameterMap.put("illegal1", illegalValues1[1]);
		expectedParameterMap.put("illegal2", illegalValues2[1]);

		request.setParameters(illegalParameterMap);

		strippingFilter.doFilter(request, response, filterChain);

		final HttpServletRequest wrappedRequest = (HttpServletRequest) filterChain.getRequest();
		assertArrayEquals(illegalValues1[1], wrappedRequest.getParameterValues("illegal1"));
		assertArrayEquals(illegalValues2[1], wrappedRequest.getParameterValues("illegal2"));
		assertEquals(illegalValues1[1][0], wrappedRequest.getParameter("illegal1"));
		assertArrayEquals(illegalValues1[1], wrappedRequest.getParameterMap().get("illegal1"));
		assertArrayEquals(illegalValues2[1], wrappedRequest.getParameterMap().get("illegal2"));
	}

	@Test
	public void testIllegalParamsForRejectingFilter() throws IOException, ServletException
	{
		final String[][] illegalValues1 =
		{
				{ ">>><script>whatever <p/> in here is not legal (how about newline?) </script><<<",
						">>> src\n \r=\n 'http://google.de'<<<", ">>></ScRiPt><<<" },
				{ ">>><<<", ">>><<<", ">>><<<" } };
		final String[][] illegalValues2 =
		{
		{ ">>><ScRiPtwhat ever\n may come ou\r way><<<" },
		{ ">>><<<" } };

		final Map<String, String[]> illegalParameterMap = new HashMap<String, String[]>();
		illegalParameterMap.put("illegal1", illegalValues1[0]);
		illegalParameterMap.put("illegal2", illegalValues2[0]);

		final Map<String, String[]> expectedParameterMap = new HashMap<String, String[]>();
		expectedParameterMap.put("illegal1", illegalValues1[1]);
		expectedParameterMap.put("illegal2", illegalValues2[1]);

		request.setParameters(illegalParameterMap);

		rejectingFilter.doFilter(request, response, filterChain);

		assertRejected();
	}


	void assertRejected()
	{
		assertNull(filterChain.getRequest());
		assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
		assertTrue(response.isCommitted());
	}

	@Test
	public void testLegalHeadersForStrippingFilter() throws IOException, ServletException
	{
		testLegalHeaders(strippingFilter);
	}

	@Test
	public void testLegalHeadersForRejectingFilter() throws IOException, ServletException
	{
		testLegalHeaders(rejectingFilter);
	}

	void testLegalHeaders(final XSSFilter filter) throws IOException, ServletException
	{
		final String[] legalValues1 = new String[]
		{ "hello world! How about src script eval-uation ?", "I can see your \nexpression onload that bloody vbscript !",
				"\n\r\tasldkl sad asjdnalsd" };

		final String[] legalValues2 = new String[]
		{ "single value as well" };
		final Map<String, String[]> expectedParameterMap = new HashMap<String, String[]>();
		expectedParameterMap.put("legal1", legalValues1);
		expectedParameterMap.put("legal2", legalValues2);

		request.addHeader("legal1", legalValues1);
		request.addHeader("legal2", legalValues2);

		filter.doFilter(request, response, filterChain);

		final HttpServletRequest wrappedRequest = (HttpServletRequest) filterChain.getRequest();

		assertEquals(Arrays.asList("legal1", "legal2"), Collections.list(wrappedRequest.getHeaderNames()));

		assertSame(legalValues1[0], wrappedRequest.getHeader("legal1"));
		assertSame(legalValues1[0], wrappedRequest.getHeader("Legal1"));
		assertEquals(Arrays.asList(legalValues1), Collections.list(wrappedRequest.getHeaders("legal1")));
		assertSame(legalValues2[0], wrappedRequest.getHeader("legal2"));
		assertSame(legalValues2[0], wrappedRequest.getHeader("Legal2"));
		assertEquals(Arrays.asList(legalValues2), Collections.list(wrappedRequest.getHeaders("legal2")));
	}

	@Test
	public void testIllegalHeadersForStripingFilter() throws IOException, ServletException
	{
		final String[][] illegalValues1 =
		{
				{ ">>><script>whatever <p/> in here is not legal (how about newline?) </script><<<",
						">>> src\n \r=\n 'http://google.de'<<<", ">>></ScRiPt><<<" },
				{ ">>><<<", ">>><<<", ">>><<<" } };
		final String[][] illegalValues2 =
		{
		{ ">>><ScRiPtwhat ever\n may come ou\r way><<<" },
		{ ">>><<<" } };

		request.addHeader("illegal1", illegalValues1[0]);
		request.addHeader("illegal2", illegalValues2[0]);

		strippingFilter.doFilter(request, response, filterChain);

		final HttpServletRequest wrappedRequest = (HttpServletRequest) filterChain.getRequest();

		assertEquals(Arrays.asList("illegal1", "illegal2"), Collections.list(wrappedRequest.getHeaderNames()));

		assertEquals(illegalValues1[1][0], wrappedRequest.getHeader("illegal1"));
		assertEquals(Arrays.asList(illegalValues1[1]), Collections.list(wrappedRequest.getHeaders("illegal1")));
		assertEquals(illegalValues2[1][0], wrappedRequest.getHeader("illegal2"));
		assertEquals(Arrays.asList(illegalValues2[1]), Collections.list(wrappedRequest.getHeaders("illegal2")));
	}

	@Test
	public void testIllegalHeadersForRejectingFilter() throws IOException, ServletException
	{
		final String[][] illegalValues1 =
		{
				{ ">>><script>whatever <p/> in here is not legal (how about newline?) </script><<<",
						">>> src\n \r=\n 'http://google.de'<<<", ">>></ScRiPt><<<" },
				{ ">>><<<", ">>><<<", ">>><<<" } };
		final String[][] illegalValues2 =
		{
		{ ">>><ScRiPtwhat ever\n may come ou\r way><<<" },
		{ ">>><<<" } };

		request.addHeader("illegal1", illegalValues1[0]);
		request.addHeader("illegal2", illegalValues2[0]);

		rejectingFilter.doFilter(request, response, filterChain);

		assertRejected();
	}

	@Test
	public void testCachingForStrippingFilter() throws IOException, ServletException
	{
		final String[][] illegalValues =
		{
				{ ">>><script>whatever <p/> in here is not legal (how about newline?) </script><<<",
						">>> src\n \r=\n 'http://google.de'<<<", ">>></ScRiPt><<<" },
				{ ">>><<<", ">>><<<", ">>><<<" } };
		final String[] legalValues = new String[]
		{ "hello world! How about src script eval-uation ?", "I can see your \nexpression onload that bloody vbscript !",
				"\n\r\tasldkl sad asjdnalsd" };

		request.addParameter("illegal", illegalValues[0]);
		request.addParameter("legal", legalValues);
		request.addHeader("legal", legalValues);


		strippingFilter.doFilter(request, response, filterChain);

		final HttpServletRequest wrappedRequest = (HttpServletRequest) filterChain.getRequest();

		assertSame(legalValues, wrappedRequest.getParameterValues("legal"));
		assertSame(legalValues[0], wrappedRequest.getHeader("legal"));
		final String[] wrappedIllegal = wrappedRequest.getParameterValues("illegal");
		assertArrayEquals(illegalValues[1], wrappedIllegal);
		for (int i = 0; i < 10; i++)
		{
			assertSame(wrappedIllegal, wrappedRequest.getParameterValues("illegal"));
			assertSame(legalValues[0], wrappedRequest.getHeader("legal"));
		}
	}

	@Test
	public void testCachingForrejectingFilter() throws IOException, ServletException
	{
		final String[] legalValues = new String[]
		{ "hello world! How about src script eval-uation ?", "I can see your \nexpression onload that bloody vbscript !",
				"\n\r\tasldkl sad asjdnalsd" };

		request.addParameter("legal", legalValues);

		request.addHeader("legal", legalValues);

		strippingFilter.doFilter(request, response, filterChain);

		final HttpServletRequest wrappedRequest = (HttpServletRequest) filterChain.getRequest();

		assertSame(legalValues, wrappedRequest.getParameterValues("legal"));
		assertSame(legalValues[0], wrappedRequest.getHeader("legal"));
		for (int i = 0; i < 10; i++)
		{
			assertSame(legalValues, wrappedRequest.getParameterValues("legal"));
			assertSame(legalValues[0], wrappedRequest.getHeader("legal"));
		}
	}

	XSSFilterConfig createMockFilterConfig(final boolean enabled, final Map<String, String> patternDefinitions,
			final XSSMatchAction action)
	{
		return new XSSFilterConfig()
		{

			@Override
			public void unregisterConfigChangeListener()
			{
				//
			}

			@Override
			public void registerForConfigChanges(final XSSFilter toUpdateOnConfigChange)
			{
				//
			}

			@Override
			public boolean isEnabled()
			{
				return enabled;
			}

			@Override
			public XSSMatchAction getActionOnMatch()
			{
				return action;
			}

			@Override
			public Map<String, String> getPatternDefinitions()
			{
				return patternDefinitions;
			}
		};
	}
}
