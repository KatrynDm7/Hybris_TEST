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
package de.hybris.platform.commercewebservicescommons.oauth2.token.impl;

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commercewebservicescommons.model.OAuthAccessTokenModel;
import de.hybris.platform.commercewebservicescommons.model.OAuthRefreshTokenModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultOAuthTokenServiceTest extends ServicelayerTransactionalTest
{
	protected static String ACCESS_TOKEN_ID = "1";
	protected static String ACCESS_TOKEN_OBJECT = "accessTokenObject";
	protected static String REFRESH_TOKEN_ID = "2";
	protected static String REFRESH_TOKEN_OBJECT = "refreshTokenObject";
	protected static String AUTHENTICATION_ID = "userAuthId";
	protected static String AUTHENTICATION_OBJECT = "userAuthObject";
	protected static String CLIENT_ID = "clientId";
	protected static String USER_NAME = "userName";


	protected static String ACCESS_TOKEN_ID_2 = "3";
	protected static String ACCESS_TOKEN_OBJECT_2 = "accessTokenObject2";
	protected static String REFRESH_TOKEN_ID_2 = "4";
	protected static String REFRESH_TOKEN_OBJECT_2 = "refreshTokenObject2";
	protected static String AUTHENTICATION_ID_2 = "userAuthId2";
	protected static String AUTHENTICATION_OBJECT_2 = "userAuthObject2";
	protected static String CLIENT_ID_2 = "clientId2";
	protected static String USER_NAME_2 = "userName2";

	protected static String NOT_EXISTING_ID = "notExistingId";

	@Resource
	protected DefaultOAuthTokenService oauthTokenService;

	@Before
	public void setUp()
	{
		final OAuthAccessTokenModel accessToken = new OAuthAccessTokenModel();
		accessToken.setTokenId(ACCESS_TOKEN_ID);
		accessToken.setToken(ACCESS_TOKEN_OBJECT);
		accessToken.setAuthenticationId(AUTHENTICATION_ID);
		accessToken.setAuthentication(AUTHENTICATION_OBJECT);
		accessToken.setClientId(CLIENT_ID);
		accessToken.setUserName(USER_NAME);
		final OAuthRefreshTokenModel refreshToken = new OAuthRefreshTokenModel();
		refreshToken.setTokenId(REFRESH_TOKEN_ID);
		refreshToken.setToken(REFRESH_TOKEN_OBJECT);
		refreshToken.setAuthentication(AUTHENTICATION_OBJECT);
		accessToken.setRefreshToken(refreshToken);

		oauthTokenService.saveAccessToken(accessToken);
	}

	@Test
	public void getAccessTokenTest()
	{
		final OAuthAccessTokenModel accessToken = oauthTokenService.getAccessToken(ACCESS_TOKEN_ID);
		Assert.assertEquals(ACCESS_TOKEN_ID, accessToken.getTokenId());
		Assert.assertEquals(ACCESS_TOKEN_OBJECT, accessToken.getToken());
		Assert.assertEquals(AUTHENTICATION_ID, accessToken.getAuthenticationId());
		Assert.assertEquals(AUTHENTICATION_OBJECT, accessToken.getAuthentication());
		Assert.assertEquals(CLIENT_ID, accessToken.getClientId());
		Assert.assertEquals(USER_NAME, accessToken.getUserName());
		Assert.assertNotNull(accessToken.getRefreshToken());
		Assert.assertEquals(REFRESH_TOKEN_ID, accessToken.getRefreshToken().getTokenId());
		Assert.assertEquals(REFRESH_TOKEN_OBJECT, accessToken.getRefreshToken().getToken());
		Assert.assertEquals(AUTHENTICATION_OBJECT, accessToken.getRefreshToken().getAuthentication());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getAccessTokenWithNullParameterTest()
	{
		oauthTokenService.getAccessToken(null);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void getAccessTokenWithIncorrectParameterTest()
	{
		oauthTokenService.getAccessToken(NOT_EXISTING_ID);
	}

	@Test
	public void getRefreshTokenTest()
	{
		final OAuthRefreshTokenModel refreshToken = oauthTokenService.getRefreshToken(REFRESH_TOKEN_ID);
		Assert.assertEquals(REFRESH_TOKEN_ID, refreshToken.getTokenId());
		Assert.assertEquals(REFRESH_TOKEN_OBJECT, refreshToken.getToken());
		Assert.assertEquals(AUTHENTICATION_OBJECT, refreshToken.getAuthentication());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRefreshTokenWithNullParameterTest()
	{
		oauthTokenService.getRefreshToken(null);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void getRefreshTokenWithIncorrectParameterTest()
	{
		oauthTokenService.getRefreshToken(NOT_EXISTING_ID);
	}

	@Test
	public void getAccessTokenForAuthenticationTest()
	{
		final OAuthAccessTokenModel accessToken = oauthTokenService.getAccessTokenForAuthentication(AUTHENTICATION_ID);
		Assert.assertEquals(ACCESS_TOKEN_ID, accessToken.getTokenId());
		Assert.assertEquals(AUTHENTICATION_ID, accessToken.getAuthenticationId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getAccessTokenForAuthenticationWithNullParamTest()
	{
		oauthTokenService.getAccessTokenForAuthentication(null);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void getAccessTokenForAuthenticationWithIncorrectParamTest()
	{
		oauthTokenService.getAccessTokenForAuthentication(NOT_EXISTING_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getAccessTokenForClientWithNullParamTest()
	{
		oauthTokenService.getAccessTokensForClient(null);
	}

	@Test
	public void getAccessTokenForClientTest()
	{
		final List<OAuthAccessTokenModel> resultList = oauthTokenService.getAccessTokensForClient(CLIENT_ID);
		Assert.assertNotNull(resultList);
		Assert.assertEquals(1, resultList.size());
		final OAuthAccessTokenModel accessToken = resultList.get(0);
		Assert.assertEquals(ACCESS_TOKEN_ID, accessToken.getTokenId());
		Assert.assertEquals(CLIENT_ID, accessToken.getClientId());
	}

	@Test
	public void getAccessTokenForUserTest()
	{
		final List<OAuthAccessTokenModel> resultList = oauthTokenService.getAccessTokensForUser(USER_NAME);
		Assert.assertNotNull(resultList);
		Assert.assertEquals(1, resultList.size());
		final OAuthAccessTokenModel accessToken = resultList.get(0);
		Assert.assertEquals(ACCESS_TOKEN_ID, accessToken.getTokenId());
		Assert.assertEquals(USER_NAME, accessToken.getUserName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getAccessTokenForUserWithNullParamTest()
	{
		oauthTokenService.getAccessTokensForUser(null);
	}

	@Test
	public void getAccessTokenForClientAndUserTest()
	{
		final List<OAuthAccessTokenModel> resultList = oauthTokenService.getAccessTokensForClientAndUser(CLIENT_ID, USER_NAME);
		Assert.assertNotNull(resultList);
		Assert.assertEquals(1, resultList.size());
		final OAuthAccessTokenModel accessToken = resultList.get(0);
		Assert.assertEquals(ACCESS_TOKEN_ID, accessToken.getTokenId());
		Assert.assertEquals(USER_NAME, accessToken.getUserName());
		Assert.assertEquals(CLIENT_ID, accessToken.getClientId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getAccessTokenForNullClientAndUserTest()
	{
		oauthTokenService.getAccessTokensForClientAndUser(null, USER_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getAccessTokenForClientAndNullUserTest()
	{
		oauthTokenService.getAccessTokensForClientAndUser(CLIENT_ID, null);
	}

	@Test
	public void saveAccessTokenModelTest()
	{
		OAuthAccessTokenModel accessToken = new OAuthAccessTokenModel();
		accessToken.setTokenId(ACCESS_TOKEN_ID_2);
		accessToken.setToken(ACCESS_TOKEN_OBJECT_2);
		accessToken.setAuthenticationId(AUTHENTICATION_ID_2);
		accessToken.setAuthentication(AUTHENTICATION_OBJECT_2);
		accessToken.setClientId(CLIENT_ID_2);
		accessToken.setUserName(USER_NAME_2);
		final OAuthRefreshTokenModel refreshToken = new OAuthRefreshTokenModel();
		refreshToken.setTokenId(REFRESH_TOKEN_ID_2);
		refreshToken.setToken(REFRESH_TOKEN_OBJECT_2);
		refreshToken.setAuthentication(AUTHENTICATION_OBJECT_2);
		accessToken.setRefreshToken(refreshToken);

		oauthTokenService.saveAccessToken(accessToken);

		accessToken = oauthTokenService.getAccessToken(ACCESS_TOKEN_ID_2);
		Assert.assertEquals(ACCESS_TOKEN_ID_2, accessToken.getTokenId());
		Assert.assertEquals(ACCESS_TOKEN_OBJECT_2, accessToken.getToken());
		Assert.assertEquals(AUTHENTICATION_ID_2, accessToken.getAuthenticationId());
		Assert.assertEquals(AUTHENTICATION_OBJECT_2, accessToken.getAuthentication());
		Assert.assertEquals(CLIENT_ID_2, accessToken.getClientId());
		Assert.assertEquals(USER_NAME_2, accessToken.getUserName());
		Assert.assertNotNull(accessToken.getRefreshToken());
		Assert.assertEquals(REFRESH_TOKEN_ID_2, accessToken.getRefreshToken().getTokenId());
		Assert.assertEquals(REFRESH_TOKEN_OBJECT_2, accessToken.getRefreshToken().getToken());
		Assert.assertEquals(AUTHENTICATION_OBJECT_2, accessToken.getRefreshToken().getAuthentication());
	}

	@Test
	public void saveDuplicatedAccessTokenModelTest()
	{
		final OAuthAccessTokenModel accessToken = new OAuthAccessTokenModel();
		accessToken.setTokenId(ACCESS_TOKEN_ID);
		accessToken.setToken(ACCESS_TOKEN_OBJECT_2);
		accessToken.setAuthenticationId(AUTHENTICATION_ID_2);
		accessToken.setAuthentication(AUTHENTICATION_OBJECT_2);
		accessToken.setClientId(CLIENT_ID_2);
		accessToken.setUserName(USER_NAME_2);

		try
		{
			oauthTokenService.saveAccessToken(accessToken);
		}
		catch (final ModelSavingException e)
		{
			Assert.assertTrue(e.getMessage().contains("ambiguous unique keys {tokenId=1} for model OAuthAccessTokenModel"));
			return;
		}
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void saveAccessTokenModelWithNullParamTest()
	{
		oauthTokenService.saveAccessToken(null);
	}

	@Test
	public void createAccessTokenTest()
	{
		final OAuthRefreshTokenModel refreshToken = new OAuthRefreshTokenModel();
		refreshToken.setTokenId(REFRESH_TOKEN_ID_2);
		refreshToken.setToken(REFRESH_TOKEN_OBJECT_2);
		refreshToken.setAuthentication(AUTHENTICATION_OBJECT_2);

		final OAuthAccessTokenModel newAccessToken = oauthTokenService.saveAccessToken(ACCESS_TOKEN_ID_2, ACCESS_TOKEN_OBJECT_2,
				AUTHENTICATION_ID_2, AUTHENTICATION_OBJECT_2, USER_NAME_2, CLIENT_ID_2, refreshToken);

		Assert.assertEquals(ACCESS_TOKEN_ID_2, newAccessToken.getTokenId());
		Assert.assertEquals(ACCESS_TOKEN_OBJECT_2, newAccessToken.getToken());
		Assert.assertEquals(AUTHENTICATION_OBJECT_2, newAccessToken.getAuthentication());
		Assert.assertEquals(AUTHENTICATION_ID_2, newAccessToken.getAuthenticationId());
		Assert.assertEquals(CLIENT_ID_2, newAccessToken.getClientId());
		Assert.assertEquals(USER_NAME_2, newAccessToken.getUserName());
		Assert.assertEquals(REFRESH_TOKEN_ID_2, newAccessToken.getRefreshToken().getTokenId());
		Assert.assertEquals(REFRESH_TOKEN_OBJECT_2, newAccessToken.getRefreshToken().getToken());
		Assert.assertEquals(AUTHENTICATION_OBJECT_2, newAccessToken.getRefreshToken().getAuthentication());
	}

	@Test
	public void saveAccessTokenTest()
	{
		final OAuthAccessTokenModel accessToken = oauthTokenService.saveAccessToken(ACCESS_TOKEN_ID, ACCESS_TOKEN_OBJECT_2,
				AUTHENTICATION_ID_2, AUTHENTICATION_OBJECT_2, USER_NAME_2, CLIENT_ID_2, null);

		Assert.assertEquals(ACCESS_TOKEN_ID, accessToken.getTokenId());
		Assert.assertEquals(ACCESS_TOKEN_OBJECT_2, accessToken.getToken());
		Assert.assertEquals(AUTHENTICATION_OBJECT_2, accessToken.getAuthentication());
		Assert.assertEquals(AUTHENTICATION_ID_2, accessToken.getAuthenticationId());
		Assert.assertEquals(CLIENT_ID_2, accessToken.getClientId());
		Assert.assertEquals(USER_NAME_2, accessToken.getUserName());
		Assert.assertNull(accessToken.getRefreshToken());
	}

	@Test(expected = IllegalArgumentException.class)
	public void saveAccessTokenWithNullParamTest()
	{
		oauthTokenService.saveAccessToken(null, null, AUTHENTICATION_ID, null, USER_NAME, CLIENT_ID, null);
	}

	@Test
	public void saveRefreshTokenModelTest()
	{
		OAuthRefreshTokenModel refreshToken = new OAuthRefreshTokenModel();
		refreshToken.setTokenId(REFRESH_TOKEN_ID_2);
		refreshToken.setToken(REFRESH_TOKEN_OBJECT_2);
		refreshToken.setAuthentication(AUTHENTICATION_OBJECT_2);

		oauthTokenService.saveRefreshToken(refreshToken);

		refreshToken = oauthTokenService.getRefreshToken(REFRESH_TOKEN_ID_2);
		Assert.assertEquals(REFRESH_TOKEN_ID_2, refreshToken.getTokenId());
		Assert.assertEquals(REFRESH_TOKEN_OBJECT_2, refreshToken.getToken());
		Assert.assertEquals(AUTHENTICATION_OBJECT_2, refreshToken.getAuthentication());
	}

	@Test
	public void saveDuplicateRefreshTokenModelTest()
	{
		final OAuthRefreshTokenModel refreshToken = new OAuthRefreshTokenModel();
		refreshToken.setTokenId(REFRESH_TOKEN_ID);
		refreshToken.setToken(REFRESH_TOKEN_OBJECT_2);
		refreshToken.setAuthentication(AUTHENTICATION_OBJECT_2);

		try
		{
			oauthTokenService.saveRefreshToken(refreshToken);
		}
		catch (final ModelSavingException e)
		{
			Assert.assertTrue(e.getMessage().contains("ambiguous unique keys {tokenId=2} for model OAuthRefreshTokenModel"));
			return;
		}
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void saveRefreshTokenModelWithNullParamTest()
	{
		oauthTokenService.saveRefreshToken(null);
	}

	@Test
	public void createRefreshTokenTest()
	{
		final OAuthRefreshTokenModel newRefreshToken = oauthTokenService.saveRefreshToken(REFRESH_TOKEN_ID, REFRESH_TOKEN_OBJECT,
				AUTHENTICATION_OBJECT);

		Assert.assertEquals(REFRESH_TOKEN_ID, newRefreshToken.getTokenId());
		Assert.assertEquals(REFRESH_TOKEN_OBJECT, newRefreshToken.getToken());
		Assert.assertEquals(AUTHENTICATION_OBJECT, newRefreshToken.getAuthentication());
	}

	@Test
	public void saveRefreshTokenTest()
	{
		final OAuthRefreshTokenModel refreshToken = oauthTokenService.saveRefreshToken(REFRESH_TOKEN_ID, REFRESH_TOKEN_OBJECT_2,
				AUTHENTICATION_OBJECT_2);

		Assert.assertEquals(REFRESH_TOKEN_ID, refreshToken.getTokenId());
		Assert.assertEquals(REFRESH_TOKEN_OBJECT_2, refreshToken.getToken());
		Assert.assertEquals(AUTHENTICATION_OBJECT_2, refreshToken.getAuthentication());
	}

	@Test(expected = IllegalArgumentException.class)
	public void saveRefreshTokenWithNullParamTest()
	{
		oauthTokenService.saveRefreshToken(null, REFRESH_TOKEN_OBJECT, AUTHENTICATION_OBJECT);
	}

	@Test
	public void removeAccessTokenTest()
	{
		oauthTokenService.removeAccessToken(ACCESS_TOKEN_ID);

		try
		{
			oauthTokenService.getAccessToken(ACCESS_TOKEN_ID);
		}
		catch (final UnknownIdentifierException e)
		{
			return;
		}
		fail();
	}

	@Test(expected = UnknownIdentifierException.class)
	public void removeAccessTokenWithIncorrectIdTest()
	{
		oauthTokenService.removeAccessToken(NOT_EXISTING_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeAccessTokenWithNullParamTest()
	{
		oauthTokenService.removeAccessToken(null);
	}

	@Test
	public void removeRefreshTokenTest()
	{
		oauthTokenService.removeRefreshToken(REFRESH_TOKEN_ID);

		try
		{
			oauthTokenService.getRefreshToken(REFRESH_TOKEN_ID);
		}
		catch (final UnknownIdentifierException e)
		{
			return;
		}
		fail();
	}

	@Test(expected = UnknownIdentifierException.class)
	public void removeRefreshTokenWithIncorrectIdTest()
	{
		oauthTokenService.removeRefreshToken(NOT_EXISTING_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeRefreshTokenWithNullParamTest()
	{
		oauthTokenService.removeRefreshToken(null);
	}

	@Test
	public void removeAccessTokenUsingRefreshTokenTest()
	{
		oauthTokenService.removeAccessTokenUsingRefreshToken(REFRESH_TOKEN_ID);

		try
		{
			oauthTokenService.getAccessToken(ACCESS_TOKEN_ID);
		}
		catch (final UnknownIdentifierException e)
		{
			return;
		}
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeAccessTokenUsingRefreshTokenWithNullParamTest()
	{
		oauthTokenService.removeAccessTokenUsingRefreshToken(null);
	}

	@Test
	public void removeAccessTokenForAuthenticationTest()
	{
		oauthTokenService.removeAccessTokenForAuthentication(AUTHENTICATION_ID);

		try
		{
			oauthTokenService.getAccessTokenForAuthentication(AUTHENTICATION_ID);
		}
		catch (final UnknownIdentifierException e)
		{
			return;
		}
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeAccessTokenForAuthenticationWithNullParamTest()
	{
		oauthTokenService.removeAccessTokenForAuthentication(null);
	}
}
