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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.amazon.media.services.S3StorageServiceFactory;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.exceptions.MediaStoreException;
import de.hybris.platform.media.services.MediaHeadersRegistry;
import de.hybris.platform.media.services.MediaLocationHashService;
import de.hybris.platform.media.storage.MediaMetaData;
import de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig;
import de.hybris.platform.media.storage.impl.StoredMediaData;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class S3MediaStorageStrategyTest
{
	private static final String FOLDER_QUALIFIER = "fooBar";
	private static final String MEDIA_ID = "123456";
	private static final String REAL_FILENAME = "foo.jpg";
	private static final String MIME = "image/jpeg";
	private static final String FOLDER_PATH = "foo";
	private static final String TENANT_CONTAINER_PREFIX = "sys-master";
	private static final String BUCKET_NAME = TENANT_CONTAINER_PREFIX + "/" + FOLDER_QUALIFIER;
	private static final String LOCATION = BUCKET_NAME + '/' + MEDIA_ID + '/' + REAL_FILENAME;

	@Mock
	private MediaFolderConfig folderConfig;
	@Mock
	private MediaLocationHashService locationHashService;
	@Mock
	private S3StorageServiceFactory serviceFactory;
	@Mock
	private AmazonS3 s3Service;

	private final String s3Bucket = BUCKET_NAME;
	@Mock
	private ObjectMetadata storageObject;
	@Mock
	private S3Object s3Objcet, s3Object2, s3Object3; // NOPMD
	@Mock
	private S3ObjectInputStream dataStream;
	@Mock
	private MediaHeadersRegistry mediaHeadersRegistry;

	@InjectMocks
	private final S3MediaStorageStrategy strategy = new S3MediaStorageStrategy()
	{
		@Override
		public void setTenantPrefix()
		{
			tenantPrefix = TENANT_CONTAINER_PREFIX;
		}
	};

	@Before
	public void setUp() throws Exception
	{
		strategy.setTenantPrefix();
		given(mediaHeadersRegistry.getHeaders()).willReturn(Collections.EMPTY_MAP);
	}


	@Test
	public void shouldThrowIllegalArgumentExceptionWhenFolderConfigIsNullOnStoringMedia()
	{
		// given
		final MediaFolderConfig folderConfig = null;

		try
		{
			// when
			strategy.store(folderConfig, MEDIA_ID, Collections.EMPTY_MAP, dataStream);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("config is required!");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenMediaIdIsNullOnStoringMedia()
	{
		// given
		final String mediaId = null;

		try
		{
			// when
			strategy.store(folderConfig, mediaId, Collections.EMPTY_MAP, dataStream);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("mediaId is required!");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenMetaDataIsNullOnStoringMedia()
	{
		// given
		final Map<String, Object> metaData = null;

		try
		{
			// when
			strategy.store(folderConfig, MEDIA_ID, metaData, dataStream);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("metaData is required!");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenDataStreamIsNullOnStoringMedia()
	{
		// given
		final InputStream dataStream = null;

		try
		{
			// when
			strategy.store(folderConfig, MEDIA_ID, Collections.EMPTY_MAP, dataStream);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("dataStream is required!");
		}
	}

	@Test
	public void shouldStoreMediaUsingS3Service() throws Exception
	{
		// given
		final Map<String, Object> metaData = buildMediaMetaData(MIME, REAL_FILENAME, FOLDER_PATH);
		given(serviceFactory.getS3ServiceForFolder(folderConfig)).willReturn(s3Service);
		given(serviceFactory.getS3BucketForFolder(folderConfig, s3Service)).willReturn(s3Bucket);
		given(s3Service.getObjectMetadata(BUCKET_NAME, LOCATION)).willReturn(storageObject);
		given(folderConfig.getFolderQualifier()).willReturn(FOLDER_QUALIFIER);
		given(Long.valueOf(storageObject.getContentLength())).willReturn(Long.valueOf(123456));

		// when
		final StoredMediaData storedMediaData = strategy.store(folderConfig, MEDIA_ID, metaData,
				new ByteArrayInputStream("FooBar".getBytes()));

		// then
		verify(s3Service, times(1)).putObject(argThat(new ArgumentMatcher<PutObjectRequest>()
		{
			@Override
			public boolean matches(final Object argument)
			{
				return argument instanceof PutObjectRequest && ((PutObjectRequest) argument).getBucketName().equals(s3Bucket);
			}
		}));
		verify(locationHashService, times(1)).createHashForLocation(FOLDER_QUALIFIER, LOCATION);
		assertThat(storedMediaData).isNotNull();
		assertThat(storedMediaData.getLocation()).isEqualTo(LOCATION);
		assertThat(storedMediaData.getSize()).isEqualTo(Long.valueOf(123456));
	}


	@Test
	public void shouldThrowMediaStoreExceptionWhenStoringMediaAndServiceExceptionWasThrown()
	{
		// given
		final Map<String, Object> metaData = buildMediaMetaData(MIME, REAL_FILENAME, FOLDER_PATH);
		given(serviceFactory.getS3ServiceForFolder(folderConfig)).willReturn(s3Service);
		given(serviceFactory.getS3BucketForFolder(folderConfig, s3Service)).willReturn(s3Bucket);
		given(s3Service.putObject(any(PutObjectRequest.class))).willThrow(new AmazonClientException("Test message"));

		try
		{
			// when
			strategy.store(folderConfig, MEDIA_ID, metaData, new ByteArrayInputStream("FooBar".getBytes()));
			fail("Should throw MediaStoreException");
		}
		catch (final MediaStoreException e)
		{
			// then
			assertThat(e.getMessage()).contains("Test message");
		}
	}

	@Test
	public void shouldDeleteMediaFromStorage()
	{
		// given
		given(serviceFactory.getS3ServiceForFolder(folderConfig)).willReturn(s3Service);
		given(serviceFactory.getS3BucketForFolder(folderConfig, s3Service)).willReturn(s3Bucket);

		// when
		strategy.delete(folderConfig, LOCATION);

		// then
		verify(s3Service, times(1)).deleteObject(BUCKET_NAME, LOCATION);
	}


	@Test
	public void shouldThrowIllegalArgumentExceptionWhenFolderConfigIsNullWhenDeletingMedia()
	{
		// given
		final MediaFolderConfig folderConfig = null;

		try
		{
			// when
			strategy.delete(folderConfig, LOCATION);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("config is required!");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenLocationIsNullWhenDeletingMedia()
	{
		// given
		final String location = null;

		try
		{
			// when
			strategy.delete(folderConfig, location);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("location is required!");
		}
	}

	@Test
	public void shouldThrowUnsupportedOperationExceptionWhenGettingStreamAsFile()
	{
		try
		{
			// when
			strategy.getAsFile(folderConfig, LOCATION);
			fail("should throw UnsupportedOperationException");
		}
		catch (final UnsupportedOperationException e)
		{
			// then
			assertThat(e).hasMessage("Obtaining media as file is not supported for S3 storage. Use getMediaAsStream method.");
		}
	}


	@Test
	public void shouldThrowIllegalArgumentExceptionWhenFolderConfigIsNullWhenGettingMediaAsStream()
	{
		// given
		final MediaFolderConfig folderConfig = null;

		try
		{
			// when
			strategy.getAsStream(folderConfig, LOCATION);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("config is required!");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenLocationIsNullWhenGettingMediaAsStream()
	{
		// given
		final String location = null;

		try
		{
			// when
			strategy.getAsStream(folderConfig, location);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("location is required!");
		}
	}

	@Test
	public void shouldGetMediaAsStream()
	{
		// given
		given(serviceFactory.getS3ServiceForFolder(folderConfig)).willReturn(s3Service);
		given(serviceFactory.getS3BucketForFolder(folderConfig, s3Service)).willReturn(s3Bucket);
		given(s3Service.getObject(BUCKET_NAME, LOCATION)).willReturn(s3Objcet);
		given(s3Objcet.getObjectContent()).willReturn(dataStream);

		// when
		final InputStream stream = strategy.getAsStream(folderConfig, LOCATION);

		// then
		assertThat(stream).isNotNull();
		verify(s3Objcet, times(1)).getObjectContent();
	}

	@Test
	public void shouldReturnSizeOfAnObjectInStorage() throws Exception
	{
		// given
		given(serviceFactory.getS3ServiceForFolder(folderConfig)).willReturn(s3Service);
		given(serviceFactory.getS3BucketForFolder(folderConfig, s3Service)).willReturn(s3Bucket);
		given(s3Service.getObjectMetadata(BUCKET_NAME, LOCATION)).willReturn(storageObject);
		given(Long.valueOf(storageObject.getContentLength())).willReturn(Long.valueOf(12345));

		// when
		final long size = strategy.getSize(folderConfig, LOCATION);

		// then
		assertThat(size).isEqualTo(12345);
	}

	@Test
	public void shouldThrowMediaNotFoundExceptionWhenAskingForSizeForNonExistentObject() throws Exception
	{
		// given
		final String mediaLocation = "NON_EXISISTENT";
		given(serviceFactory.getS3ServiceForFolder(folderConfig)).willReturn(s3Service);
		given(serviceFactory.getS3BucketForFolder(folderConfig, s3Service)).willReturn(s3Bucket);
		given(s3Service.getObjectMetadata(BUCKET_NAME, mediaLocation)).willThrow(new AmazonClientException("Foo"));

		try
		{
			// when
			strategy.getSize(folderConfig, mediaLocation);
			fail("Should throw MediaNotFoundException");

		}
		catch (final MediaNotFoundException e)
		{
			// then fine
		}
	}

	private Map<String, Object> buildMediaMetaData(final String mime, final String originalName, final String folderPath)
	{
		final Map<String, Object> metaData = new HashMap<String, Object>();
		metaData.put(MediaMetaData.MIME, mime);
		metaData.put(MediaMetaData.FILE_NAME, originalName);
		metaData.put(MediaMetaData.FOLDER_PATH, folderPath);
		return metaData;
	}

}
