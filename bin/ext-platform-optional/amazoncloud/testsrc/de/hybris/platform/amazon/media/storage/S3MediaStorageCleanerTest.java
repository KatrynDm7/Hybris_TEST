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
package de.hybris.platform.amazon.media.storage;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.amazon.media.services.S3StorageServiceFactory;
import de.hybris.platform.amazon.media.services.impl.DefaultS3StorageServiceFactory;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageConfigService.GlobalMediaStorageConfig;
import de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.collect.Sets;


@UnitTest
public class S3MediaStorageCleanerTest
{
	private static final String FOLDER_QUALIFIER1 = "foobar1";
	private static final String FOLDER_QUALIFIER2 = "foobar2";
	private static final String TENANT_CONTAINER_PREFIX = "sys-junit";
	private static final String BUCKET_NAME1 = TENANT_CONTAINER_PREFIX + "-" + FOLDER_QUALIFIER1;
	private static final String BUCKET_NAME2 = TENANT_CONTAINER_PREFIX + "-" + FOLDER_QUALIFIER2;

	private S3MediaStorageCleaner cleaner;

	@Mock
	private ObjectListing objectListing;

	@Mock
	private S3StorageServiceFactory serviceFactory;

	private final String s3Bucket1 = BUCKET_NAME1;
	private final String s3Bucket2 = BUCKET_NAME2;

	@Mock
	private AmazonS3 s3Service;
	@Mock
	private S3Object s3Objcet, s3Object2, s3Object3;
	@Mock
	private MediaFolderConfig folderConfig1, folderConfig2;
	@Mock
	private MediaStorageConfigService storageConfigService;
	@Mock
	private GlobalMediaStorageConfig mediaStorageConfig;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		cleaner = new S3MediaStorageCleaner()
		{
			@Override
			public void setTenantPrefix()
			{
				tenantPrefix = TENANT_CONTAINER_PREFIX;
			}
		};
		cleaner.setTenantPrefix();
		cleaner.setStorageConfigService(storageConfigService);
		cleaner.setS3StorageServiceFactory(serviceFactory);

		given(storageConfigService.getDefaultStrategyId()).willReturn("s3MediaStorageStrategy");
		given(storageConfigService.getGlobalSettingsForStrategy("s3MediaStorageStrategy")).willReturn(mediaStorageConfig);
		given(mediaStorageConfig.getParameter(DefaultS3StorageServiceFactory.BUCKET_ID_KEY, String.class)).willReturn("fooBar");
		given(mediaStorageConfig.getParameter(DefaultS3StorageServiceFactory.ACCESS_KEY, String.class)).willReturn("12345");
		given(mediaStorageConfig.getParameter(DefaultS3StorageServiceFactory.SECRET_ACCESS_KEY, String.class)).willReturn("Secret");
		given(mediaStorageConfig.getParameter(DefaultS3StorageServiceFactory.ENDPOINT_KEY, String.class)).willReturn("fooBarBaz");

		final Set<MediaFolderConfig> foldersConfig = Sets.<MediaFolderConfig> newHashSet(folderConfig1, folderConfig2);
		given(storageConfigService.getFolderConfigsForStrategy("s3MediaStorageStrategy")).willReturn(foldersConfig);
		given(folderConfig1.getFolderQualifier()).willReturn(FOLDER_QUALIFIER1);
		given(folderConfig2.getFolderQualifier()).willReturn(FOLDER_QUALIFIER2);
		given(serviceFactory.getS3Service("12345", "Secret", "fooBarBaz")).willReturn(s3Service);
		given(serviceFactory.getS3BucketForFolder(folderConfig1, s3Service)).willReturn(s3Bucket1);
	}

	@Test
	public void shouldCleanStorageOnInitializationWhenS3IsDefaultStrategy() throws Exception
	{
		final List<S3ObjectSummary> objectsToDelete = new ArrayList<>();
		final S3ObjectSummary s1 = new S3ObjectSummary();
		s1.setKey("key1");
		final S3ObjectSummary s2 = new S3ObjectSummary();
		s2.setKey("key2");
		final S3ObjectSummary s3 = new S3ObjectSummary();
		s2.setKey("key3");
		objectsToDelete.add(s1);
		objectsToDelete.add(s2);
		objectsToDelete.add(s3);

		// given
		given(serviceFactory.getS3ServiceForFolder(folderConfig1)).willReturn(s3Service);
		given(serviceFactory.getS3ServiceForFolder(folderConfig2)).willReturn(s3Service);
		given(serviceFactory.getS3BucketForFolder(folderConfig1, s3Service)).willReturn(s3Bucket1);
		given(serviceFactory.getS3BucketForFolder(folderConfig2, s3Service)).willReturn(s3Bucket2);
		given(objectListing.getObjectSummaries()).willReturn(objectsToDelete);
		given(Boolean.valueOf(objectListing.isTruncated())).willReturn(Boolean.FALSE);

		given(s3Service.listObjects(BUCKET_NAME1, TENANT_CONTAINER_PREFIX)).willReturn(objectListing);
		given(s3Service.listObjects(BUCKET_NAME2, TENANT_CONTAINER_PREFIX)).willReturn(objectListing);

		// when
		cleaner.onInitialize();

		// then
		verify(s3Service, times(1)).deleteObjects(argThat(new ArgumentMatcher<DeleteObjectsRequest>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				return argument instanceof DeleteObjectsRequest
						&& ((DeleteObjectsRequest) argument).getBucketName().equals(BUCKET_NAME1)
						&& ((DeleteObjectsRequest) argument).getKeys().size() == 3;
			}

		}));
		verify(s3Service, times(1)).deleteObjects(argThat(new ArgumentMatcher<DeleteObjectsRequest>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				return argument instanceof DeleteObjectsRequest
						&& ((DeleteObjectsRequest) argument).getBucketName().equals(BUCKET_NAME2)
						&& ((DeleteObjectsRequest) argument).getKeys().size() == 3;
			}
		}));
	}
}
