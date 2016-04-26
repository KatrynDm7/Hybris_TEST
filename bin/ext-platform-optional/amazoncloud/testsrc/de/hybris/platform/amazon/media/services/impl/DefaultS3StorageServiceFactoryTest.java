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
package de.hybris.platform.amazon.media.services.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.media.exceptions.ExternalStorageServiceException;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageConfigService.GlobalMediaStorageConfig;
import de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Region;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultS3StorageServiceFactoryTest
{
	private static final String FOLDER_QUALIFIER = "fooBar";
	private static final String ACCESS_KEY = DefaultS3StorageServiceFactory.ACCESS_KEY;
	private static final String SECRET_ACCESS_KEY = DefaultS3StorageServiceFactory.SECRET_ACCESS_KEY;
	private static final String ENDPOINT_KEY = DefaultS3StorageServiceFactory.ENDPOINT_KEY;
	private static final String ACCESS_KEY_VAL = "adjkAKLDJaklsdjak";
	private static final String SECRET_ACCESS_KEY_VAL = "JakjdaklJKLDJQ890";
	private static final String BUCKET_ID_KEY = DefaultS3StorageServiceFactory.BUCKET_ID_KEY;
	private static final String BUCKET_ID_VAL = FOLDER_QUALIFIER.toLowerCase();

	@Mock
	private MediaStorageConfigService storageConfigService;
	@Mock
	private AmazonS3 s3Service;
	@Mock
	private MediaFolderConfig folderConfig;
	@Mock
	private GlobalMediaStorageConfig storageConfig; //NOPMD
	private DefaultS3StorageServiceFactory serviceFactory;

	@Before
	public void setUp() throws Exception
	{
		serviceFactory = new DefaultS3StorageServiceFactory();
		given(storageConfigService.getConfigForFolder(FOLDER_QUALIFIER)).willReturn(folderConfig);
		given(folderConfig.getParameter(ACCESS_KEY)).willReturn(ACCESS_KEY_VAL);
		given(folderConfig.getParameter(SECRET_ACCESS_KEY)).willReturn(SECRET_ACCESS_KEY_VAL);
	}

	@Test
	public void shouldReturnS3ServiceWithoutConfiguredEndpoint()
	{
		// when
		final AmazonS3 s3Service = serviceFactory.getS3ServiceForFolder(folderConfig);

		// then
		assertThat(s3Service).isNotNull();
	}

	@Test
	public void shouldReturnS3ServiceWithConfiguredEuropeEndpoint()
	{
		// given
		given(folderConfig.getParameter(ENDPOINT_KEY)).willReturn("s3-eu-west-1.amazonaws.com");

		// when
		final AmazonS3Client s3Service = (AmazonS3Client) serviceFactory.getS3ServiceForFolder(folderConfig);

		// then
		assertThat(s3Service).isNotNull();
		assertThat(s3Service.getRegion()).isEqualTo(Region.EU_Ireland);
	}

	@Test
	public void shouldThrowExternalStorageServiceExceptionWhenBucketIdIsEmpty()
	{
		try
		{
			// when
			serviceFactory.getS3BucketForFolder(folderConfig, s3Service);
			fail("should throw ExternalStorageServiceException");
		}
		catch (final ExternalStorageServiceException e)
		{
			// then
			assertThat(e).hasMessage("Bucket ID not found in S3 configuration");
		}
	}

}
