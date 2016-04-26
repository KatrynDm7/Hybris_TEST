/*
 *
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
package de.hybris.y2ysync.services.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.TestImportCsvUtil;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.services.StreamConfigCloningService;
import de.hybris.y2ysync.services.SyncConfigService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultStreamConfigCloningServiceTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private StreamConfigCloningService streamConfigCloningService;
	@Resource
	private ModelService modelService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private SyncConfigService syncConfigService;
	@Resource
	private TypeService typeService;
	@Resource(name = "testImportCsvUtil")
	private TestImportCsvUtil importCsvUtil;

	private Y2YStreamConfigurationContainerModel sourceContainer;
	private CatalogVersionModel catVerA1;

	@Before
	public void setUp() throws Exception
	{
		importCsvUtil.importCsv("/test/source_test_catalog.csv", "UTF-8");
		catVerA1 = catalogVersionService.getCatalogVersion("CatalogA", "CatalogVersionA1");
		sourceContainer = modelService.create(Y2YStreamConfigurationContainerModel.class);
		sourceContainer.setId("testContainer");
		sourceContainer.setCatalogVersion(catVerA1);
		modelService.save(sourceContainer);
	}

	@Test
	public void testCloneStreamContainer() throws Exception
	{
		//given - productStream and categoryStream
		modelService.save(syncConfigService.createStreamConfiguration(sourceContainer, "Product", prepareProductAttributeDescriptors(), Collections.emptySet()));
		modelService.save(syncConfigService.createStreamConfiguration(sourceContainer, "Category", prepareCategoryAttributeDescriptors(), Collections.emptySet()));
		final String targetContainerId = "targetContainerId";

		//when
		final Y2YStreamConfigurationContainerModel targetContainer = streamConfigCloningService.cloneStreamContainer(
				sourceContainer, targetContainerId);

		//then
		assertThat(targetContainer).isNotSameAs(sourceContainer);
		assertThat(targetContainer.getConfigurations()).hasSize(sourceContainer.getConfigurations().size()).isNotSameAs(
				sourceContainer.getConfigurations());
		assureContainerFullyClonedAndNotPersisted(targetContainer);
		assertThat(targetContainer.getCatalogVersion()).isSameAs(sourceContainer.getCatalogVersion());
		assertThat(targetContainer.getId()).isEqualTo(targetContainerId);
		checkStreamIdUniqueness(sourceContainer.getConfigurations(), targetContainer.getConfigurations());
	}

	@Test
	public void testCloneStreamConfigurationsSubset() throws Exception
	{
		//given - productStream and categoryStream
		final Y2YStreamConfigurationModel sourceProductStream = syncConfigService.createStreamConfiguration(sourceContainer,
				"Product", prepareProductAttributeDescriptors(), Collections.emptySet());
		final Y2YStreamConfigurationModel sourceCategoryStream = syncConfigService.createStreamConfiguration(sourceContainer,
				"Category", prepareCategoryAttributeDescriptors(), Collections.emptySet());
		modelService.saveAll(sourceProductStream, sourceCategoryStream);

		//when
		final Set<Y2YStreamConfigurationModel> streamConfigurationsCloned = streamConfigCloningService
				.cloneStreamConfigurations(sourceProductStream);
		//then
		assertThat(streamConfigurationsCloned).hasSize(1);
		streamConfigurationsCloned.stream().forEach(e -> assertThat(e.getContainer()).isNull());
		assureStreamsClonedAndNotPersisted(streamConfigurationsCloned);
		checkStreamIdUniqueness(sourceContainer.getConfigurations(), streamConfigurationsCloned);
	}

	private void checkStreamIdUniqueness(final Set<? extends StreamConfigurationModel> sourceStreamConfigs,
			final Set<? extends StreamConfigurationModel> clonedStreamConfigs)
	{
		final List<String> sourceStreamIds = sourceStreamConfigs.stream().map(e -> e.getStreamId()).collect(Collectors.toList());
		clonedStreamConfigs.stream().forEach(e -> assertThat(e.getStreamId()).isNotIn(sourceStreamIds));
	}

	private void assureContainerFullyClonedAndNotPersisted(final Y2YStreamConfigurationContainerModel targetContainer)
	{
		assertThat(targetContainer).isNotNull();
		assertThat(targetContainer.getPk()).isNull();
		assureStreamsClonedAndNotPersisted(targetContainer.getConfigurations());
	}

	private void assureStreamsClonedAndNotPersisted(final Set<? extends StreamConfigurationModel> streamConfigs)
	{
		assertThat(streamConfigs).isNotEmpty();
		streamConfigs.stream().forEach(e -> assertThat(e.getPk()).isNull());
		for (final StreamConfigurationModel conf : streamConfigs)
		{
			final Set<Y2YColumnDefinitionModel> columnDefinitions = ((Y2YStreamConfigurationModel) conf).getColumnDefinitions();
			assertThat(columnDefinitions).isNotEmpty();
			columnDefinitions.stream().forEach(e -> assertThat(e.getPk()).isNull());
		}
	}

	private Set<AttributeDescriptorModel> prepareProductAttributeDescriptors()
	{
		final Set<AttributeDescriptorModel> res = new HashSet<>();

		res.add(typeService.getAttributeDescriptor("Product", "code"));
		res.add(typeService.getAttributeDescriptor("Product", "catalogVersion"));
		res.add(typeService.getAttributeDescriptor("Product", "name"));
		res.add(typeService.getAttributeDescriptor("Product", "unit"));
		res.add(typeService.getAttributeDescriptor("Product", "supercategories"));
		res.add(typeService.getAttributeDescriptor("Product", "approvalStatus"));

		return res;
	}


    private Set<AttributeDescriptorModel> prepareCategoryAttributeDescriptors()
    {
        final Set<AttributeDescriptorModel> res = new HashSet<>();

        res.add(typeService.getAttributeDescriptor("Category", "code"));
        res.add(typeService.getAttributeDescriptor("Category", "name"));
        res.add(typeService.getAttributeDescriptor("Category", "catalogVersion"));

        return res;
    }

}
