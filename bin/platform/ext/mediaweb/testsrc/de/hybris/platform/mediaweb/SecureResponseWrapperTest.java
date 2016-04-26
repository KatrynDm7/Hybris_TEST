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
package de.hybris.platform.mediaweb;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SecureResponseWrapperTest
{
	@Mock
	private HttpServletResponse response;
	private SecureResponseWrapper secureResponseWrapper;

	@Before
	public void createSecureResponseWrapper() throws Exception
	{
		secureResponseWrapper = new SecureResponseWrapper(response);
	}

	/**
	 * Test method for {@link de.hybris.platform.mediaweb.SecureResponseWrapper#sendRedirect(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void shouldProcessNormalRedirectWhenLocationDoesNotContainCRLF() throws IOException
	{
		// given
		final String location = "Foo Bar";

		try
		{
			// when
			secureResponseWrapper.sendRedirect(location);

			// then OK
		}
		catch (final MalformedURLException e)
		{
			fail("Should NOT throw MalformedURLException");
		}
	}

	/**
	 * Test method for {@link de.hybris.platform.mediaweb.SecureResponseWrapper#sendRedirect(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void shouldThrowMalformedURLExceptionWhenRedirectLocationContainsCR() throws IOException
	{
		// given
		final String location = "Foo\rBar";

		try
		{
			// when
			secureResponseWrapper.sendRedirect(location);
			fail("Should throw MalformedURLException");
		}
		catch (final MalformedURLException e)
		{
			// then
			assertThat(e).hasMessage("CR or LF detected in redirect URL: possible http response splitting attack");
		}
	}

	/**
	 * Test method for {@link de.hybris.platform.mediaweb.SecureResponseWrapper#sendRedirect(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void shouldThrowMalformedURLExceptionWhenRedirectLocationContainsLF() throws IOException
	{
		// given
		final String location = "Foo\nBar";

		try
		{
			// when
			secureResponseWrapper.sendRedirect(location);
			fail("Should throw MalformedURLException");
		}
		catch (final MalformedURLException e)
		{
			// then
			assertThat(e).hasMessage("CR or LF detected in redirect URL: possible http response splitting attack");
		}
	}

	/**
	 * Test method for {@link de.hybris.platform.mediaweb.SecureResponseWrapper#sendRedirect(java.lang.String)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void shouldThrowMalformedURLExceptionWhenRedirectLocationContainsCRLF() throws IOException
	{
		// given
		final String location = "Foo\r\nBar";

		try
		{
			// when
			secureResponseWrapper.sendRedirect(location);
			fail("Should throw MalformedURLException");
		}
		catch (final MalformedURLException e)
		{
			// then
			assertThat(e).hasMessage("CR or LF detected in redirect URL: possible http response splitting attack");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.mediaweb.SecureResponseWrapper#setHeader(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldSetNewHeaderIfHeaderValueDoesNotContainCRLF()
	{
		// given
		final String value = "Foo Bar";
		final String name = "Foo";

		try
		{
			// when
			secureResponseWrapper.setHeader(name, value);

			// then OK
		}
		catch (final IllegalArgumentException e)
		{
			fail("Should NOT throw IllegalArgumentException");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.mediaweb.SecureResponseWrapper#setHeader(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenHeaderArgumentContaisCR()
	{
		// given
		final String value = "Foo\rBar";
		final String name = "Foo";

		try
		{
			// when
			secureResponseWrapper.setHeader(name, value);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("Header value must not contain CR or LF characters");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.mediaweb.SecureResponseWrapper#setHeader(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenHeaderArgumentContaisLF()
	{
		// given
		final String value = "Foo\nBar";
		final String name = "Foo";

		try
		{
			// when
			secureResponseWrapper.setHeader(name, value);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("Header value must not contain CR or LF characters");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.mediaweb.SecureResponseWrapper#setHeader(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenHeaderArgumentContaisCRLF()
	{
		// given
		final String value = "Foo\r\nBar";
		final String name = "Foo";

		try
		{
			// when
			secureResponseWrapper.setHeader(name, value);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("Header value must not contain CR or LF characters");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.mediaweb.SecureResponseWrapper#addHeader(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldAddNewHeaderIfHeaderValueDoesNotContainCRLF()
	{
		// given
		final String value = "Foo Bar";
		final String name = "Foo";

		try
		{
			// when
			secureResponseWrapper.addHeader(name, value);

			// then OK
		}
		catch (final IllegalArgumentException e)
		{
			fail("Should NOT throw IllegalArgumentException");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.mediaweb.SecureResponseWrapper#addHeader(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenHeaderArgumentToAddContaisCR()
	{
		// given
		final String value = "Foo\rBar";
		final String name = "Foo";

		try
		{
			// when
			secureResponseWrapper.addHeader(name, value);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("Header value must not contain CR or LF characters");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.mediaweb.SecureResponseWrapper#addHeader(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenHeaderArgumentToAddContaisLF()
	{
		// given
		final String value = "Foo\nBar";
		final String name = "Foo";

		try
		{
			// when
			secureResponseWrapper.addHeader(name, value);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("Header value must not contain CR or LF characters");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.mediaweb.SecureResponseWrapper#addHeader(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldThrowIllegalArgumentExceptionWhenHeaderArgumentToAddContaisCRLF()
	{
		// given
		final String value = "Foo\r\nBar";
		final String name = "Foo";

		try
		{
			// when
			secureResponseWrapper.addHeader(name, value);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("Header value must not contain CR or LF characters");
		}
	}

}
