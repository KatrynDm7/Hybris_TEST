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
package de.hybris.liveeditaddon.cms.media;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;

import java.util.*;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;


/**
 * Test suite for {@link de.hybris.liveeditaddon.cms.media.DefaultCmsComponentMediaTypesResolver}
 */
@UnitTest
public class DefaultCmsComponentMediaTypesResolverTest
{
	private final static String TYPE_CODE = "CMSImageComponent";
	private final static String TYPE_MEDIA_ATTRIBUTE = "CMSImageComponent.media";
	private final static String SECOND_TYPE_CODE = "CMSBannerComponent";
	private final static String SECOND_TYPE_MEDIA_ATTRIBUTE = "CMSBannerComponent.media";
	private final static String PURE_TEXT_TYPE_CODE = "PureTextComponent";
	private final static String CONTENT_SLOT_NAME = "contentSlotName";
	private DefaultCmsComponentMediaTypesResolver resolver;
	private Collection<CmsComponentMediaTypeMapping> mediaComponentTypes;
	private List<ContentSlotNameModel> availableContentSlots;
	@Mock
	private ApplicationContext applicationContext;
	@Mock
	private Map fakeBeanMap;
	@Mock
	private AbstractPageModel abstractPageModel;
	@Mock
	private PageTemplateModel pageTemplateModel;
	@Mock
	private ContentSlotNameModel contentSlotNameModel;
	@Mock
	private CMSComponentTypeModel cmsComponentTypeModel;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		resolver = new DefaultCmsComponentMediaTypesResolver();
		resolver.setApplicationContext(applicationContext);

		mediaComponentTypes = new ArrayList<>();
		final CmsComponentMediaTypeMapping cmsComponentMediaTypeMapping = new CmsComponentMediaTypeMapping() {
			@Override
			public Map<String, String> getMappings() {
				final Map<String, String> mappings = new LinkedHashMap<>();
				mappings.put(TYPE_CODE, TYPE_MEDIA_ATTRIBUTE);
				mappings.put(SECOND_TYPE_CODE, SECOND_TYPE_MEDIA_ATTRIBUTE);
				return mappings;
			}
		};
		mediaComponentTypes.add(cmsComponentMediaTypeMapping);
		given(applicationContext.getBeansOfType(CmsComponentMediaTypeMapping.class)).willReturn(fakeBeanMap);
		given(fakeBeanMap.values()).willReturn(mediaComponentTypes);
		resolver.afterPropertiesSet();
	}

	@Test
	public void testIsMediaComponent()
	{
		Assert.assertTrue(resolver.isMediaComponent(TYPE_CODE));
	}

	@Test
	public void testIsNotMediaComponent()
	{
		Assert.assertFalse(resolver.isMediaComponent(PURE_TEXT_TYPE_CODE));
	}

	@Test
	public void testGetMediaComponentAttribute()
	{
		Assert.assertEquals(resolver.getMediaComponentAttribute(TYPE_CODE), TYPE_MEDIA_ATTRIBUTE);
	}

	@Test
	public void testFailGetMediaComponentAttribute()
	{
		Assert.assertNull(resolver.getMediaComponentAttribute(PURE_TEXT_TYPE_CODE));
	}

	@Test
	public void testGetMediaComponentTypesAsStringList()
	{
		Assert.assertTrue(resolver.getMediaComponentTypesAsStringList().contains(TYPE_CODE));
		Assert.assertTrue(resolver.getMediaComponentTypesAsStringList().contains(SECOND_TYPE_CODE));
	}

	@Test
	public void testIsContentSlotPositionSupportingMedia()
	{
		availableContentSlots = new ArrayList<>();
		availableContentSlots.add(contentSlotNameModel);
		given(contentSlotNameModel.getName()).willReturn(CONTENT_SLOT_NAME);
		given(abstractPageModel.getMasterTemplate()).willReturn(pageTemplateModel);
		given(pageTemplateModel.getAvailableContentSlots()).willReturn(availableContentSlots);
		given(contentSlotNameModel.getValidComponentTypes()).willReturn(new HashSet<CMSComponentTypeModel>() {{
			add(cmsComponentTypeModel);
		}});
		given(cmsComponentTypeModel.getCode()).willReturn(TYPE_CODE);
		
		Assert.assertTrue(resolver.isContentSlotPositionSupportingMedia(CONTENT_SLOT_NAME, abstractPageModel));
	}

	@Test
	public void testFailIsContentSlotPositionSupportingMedia()
	{
		availableContentSlots = new ArrayList<>();
		availableContentSlots.add(contentSlotNameModel);
		given(contentSlotNameModel.getName()).willReturn(CONTENT_SLOT_NAME);
		given(abstractPageModel.getMasterTemplate()).willReturn(pageTemplateModel);
		given(pageTemplateModel.getAvailableContentSlots()).willReturn(availableContentSlots);
		given(contentSlotNameModel.getValidComponentTypes()).willReturn(new HashSet<CMSComponentTypeModel>());
		given(cmsComponentTypeModel.getCode()).willReturn(TYPE_CODE);

		Assert.assertFalse(resolver.isContentSlotPositionSupportingMedia(CONTENT_SLOT_NAME, abstractPageModel));
	}
}
