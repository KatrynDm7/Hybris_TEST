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
package de.hybris.platform.commercefacades.product.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.util.ConverterFactory;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;



@UnitTest
public class ImagePopulatorTest
{
	private static final String MEDIA_FORMAT_QUALIFIER = "mediaFormatQ";
	private static final String MEDIA_URL = "mediaURL";

	private final AbstractPopulatingConverter<MediaModel, ImageData> imageConverter = new ConverterFactory<MediaModel, ImageData, ImagePopulator>()
			.create(ImageData.class, new ImagePopulator());

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConvert()
	{
		final MediaModel source = mock(MediaModel.class);
		final MediaFormatModel mediaFormatModel = mock(MediaFormatModel.class);

		given(mediaFormatModel.getQualifier()).willReturn(MEDIA_FORMAT_QUALIFIER);
		given(source.getMediaFormat()).willReturn(mediaFormatModel);
		given(source.getURL()).willReturn(MEDIA_URL);

		final ImageData result = imageConverter.convert(source);

		Assert.assertEquals(MEDIA_FORMAT_QUALIFIER, result.getFormat());
		Assert.assertEquals(MEDIA_URL, result.getUrl());
	}

}
