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
 */
package de.hybris.deltadetection.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;


@IntegrationTest
public class DefaultChangeDetectionServiceIntegrationTest extends ServicelayerBaseTest
{
	private static final String CATALOG_VERSION_ONE = "CATALOG_VERSION_ONE";
	private static final String CATALOG_VERSION_TWO = "CATALOG_VERSION_TWO";

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Resource
	private ChangeDetectionService changeDetectionService;

	private ComposedTypeModel catalogVersionType;
	private CatalogVersionModel catalogVersionOne;
	private CatalogVersionModel catalogVersionTwo;
	private CatalogModel catalog;

	@Before
	public void setUp()
	{
		catalog = modelService.create(CatalogModel.class);
		catalog.setId(uniqueId());

		catalogVersionOne = createCatalogVersion(CATALOG_VERSION_ONE);
		catalogVersionTwo = createCatalogVersion(CATALOG_VERSION_TWO);

		modelService.saveAll();

		catalogVersionType = typeService.getComposedTypeForClass(CatalogVersionModel.class);
	}

	@Test
	public void shouldDetectNewItemsWithoutItemSelector()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId());

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(added(catalogVersionOne), added(catalogVersionTwo));
	}

	@Test
	public void shouldDetectNewItemsWithItemSelectorWithoutParameters()
	{
		final String selector = "{item.version}='" + CATALOG_VERSION_ONE + "'";
		final StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector(selector);

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(added(catalogVersionOne));
	}

	@Test
	public void shouldDetectNewItemsWithItemSelectorWithParameters()
	{
		final String selector = "{item.version}=?cv";
		final Map<String, Object> params = ImmutableMap.of("cv", CATALOG_VERSION_TWO);
		final StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector(selector).withParameters(params);

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(added(catalogVersionTwo));
	}

	@Test
	public void shouldDetectModifiedItemsWithoutItemSelector()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId());
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);

		catalogVersionOne.setCategorySystemID("CHANGED");
		catalogVersionTwo.setCategorySystemID("CHANGED");
		modelService.saveAll();
		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(modified(catalogVersionOne), modified(catalogVersionTwo));
	}

	@Test
	public void shouldDetectModifiedItemsWithItemSelectorWithoutParameters()
	{
		final String selector = "{item.version}='" + CATALOG_VERSION_ONE + "'";
		final StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector(selector);
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);

		catalogVersionOne.setCategorySystemID("CHANGED");
		catalogVersionTwo.setCategorySystemID("CHANGED");
		modelService.saveAll();
		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(modified(catalogVersionOne));
	}

	@Test
	public void shouldDetectModifiedItemsWithItemSelectorWithParameters()
	{
		final String selector = "{item.version}=?cv";
		final Map<String, Object> params = ImmutableMap.of("cv", CATALOG_VERSION_TWO);
		final StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector(selector).withParameters(params);
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);

		catalogVersionOne.setCategorySystemID("CHANGED");
		catalogVersionTwo.setCategorySystemID("CHANGED");
		modelService.saveAll();
		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(modified(catalogVersionTwo));
	}

	@Test
	public void shouldDetectRemovedItemsWithoutItemSelector()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId());
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);

		modelService.remove(catalogVersionOne);
		modelService.remove(catalogVersionTwo);
		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(removed(catalogVersionOne), removed(catalogVersionTwo));
	}

	@Test
	public void shouldDetectRemovedItemsWithItemSelectorWithoutParameters()
	{
		final String selector = "{item.version}='" + CATALOG_VERSION_ONE + "'";
		final StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector(selector);
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);

		modelService.remove(catalogVersionOne);
		modelService.remove(catalogVersionTwo);
		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(removed(catalogVersionOne));
	}

	@Test
	public void shouldDetectRemovedItemsWithItemSelectorWithParameters()
	{
		final String selector = "{item.version}=?cv";
		final Map<String, Object> params = ImmutableMap.of("cv", CATALOG_VERSION_TWO);
		final StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector(selector).withParameters(params);
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);

		modelService.remove(catalogVersionOne);
		modelService.remove(catalogVersionTwo);
		modelService.saveAll();
		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(removed(catalogVersionTwo));
	}

	@Test
	public void shouldDetectAllChangeTypesWithoutItemSelector()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId());
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);

		modelService.remove(catalogVersionOne);
		catalogVersionTwo.setCategorySystemID("CHANGED");
		final CatalogVersionModel newCatalogVersion = createCatalogVersion("TMP_VERSION");
		modelService.saveAll();
		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(removed(catalogVersionOne), modified(catalogVersionTwo), added(newCatalogVersion));
	}

	@Test
	public void shouldDetectAllChangeTypesWithItemSelectorWithoutParameters()
	{
		final String selector = "{item.version} like '%VERSION%'";
		final StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector(selector);
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);

		modelService.remove(catalogVersionTwo);
		catalogVersionOne.setCategorySystemID("CHANGED");
		final CatalogVersionModel newCatalogVersion = createCatalogVersion("TMP_VERSION");
		modelService.saveAll();
		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(removed(catalogVersionTwo), added(newCatalogVersion), modified(catalogVersionOne));
	}

	@Test
	public void shouldDetectAllChangeTypesWithItemSelectorWithParameters()
	{
		final String selector = "{item.version} like ?cv";
		final Map<String, Object> params = ImmutableMap.of("cv", "%VERSION%");
		final StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector(selector).withParameters(params);
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);

		modelService.remove(catalogVersionTwo);
		catalogVersionOne.setCategorySystemID("CHANGED");
		final CatalogVersionModel newCatalogVersion = createCatalogVersion("TMP_VERSION");
		modelService.saveAll();
		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(removed(catalogVersionTwo), added(newCatalogVersion), modified(catalogVersionOne));
	}

	@Test
	public void shouldDetectAsRemovedItemsWhichNoLongerMatchSelector()
	{
		final String selector = "{item.version} like ?cv";
		final Map<String, Object> params = ImmutableMap.of("cv", "%VERSION%");
		final StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector(selector).withParameters(params);
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);

		catalogVersionOne.setVersion("CHANGED");
		modelService.saveAll();
		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(removed(catalogVersionOne));
	}

	@Test
	public void shouldDetectAsNewItemsWhichBecomeToMatchSelector()
	{
		final String selector = "{item.version} like ?cv";
		final Map<String, Object> params = ImmutableMap.of("cv", "%ONE%");
		final StreamConfiguration config = givenConfiguration(uniqueId()).withItemSelector(selector).withParameters(params);
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);

		catalogVersionTwo.setVersion(catalogVersionTwo.getVersion() + "ONE");
		modelService.saveAll();
		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(added(catalogVersionTwo));
	}

	private CatalogVersionModel createCatalogVersion(final String version)
	{
		final CatalogVersionModel result = modelService.create(CatalogVersionModel.class);
		result.setVersion(version);
		result.setCatalog(catalog);
		return result;
	}

	private Change added(final ItemModel item)
	{
		return new Change(ChangeType.NEW, item.getPk().getLong());
	}

	private Change modified(final ItemModel item)
	{
		return new Change(ChangeType.MODIFIED, item.getPk().getLong());
	}

	private Change removed(final ItemModel item)
	{
		return new Change(ChangeType.DELETED, item.getPk().getLong());
	}

	private StreamConfiguration givenConfiguration(final String streamId)
	{
		final StreamConfigurationContainerModel streamCfgContainer = modelService.create(StreamConfigurationContainerModel.class);
		streamCfgContainer.setId("containerId");
		modelService.save(streamCfgContainer);

		final StreamConfigurationModel streamCfg = modelService.create(StreamConfigurationModel.class);
		streamCfg.setStreamId(streamId);
		streamCfg.setContainer(streamCfgContainer);
		streamCfg.setItemTypeForStream(typeService.getComposedTypeForClass(ProductModel.class));
		streamCfg.setWhereClause("not used");
		streamCfg.setInfoExpression("#{getPk()}");

		modelService.save(streamCfg);

		return StreamConfiguration.buildFor(streamId);
	}

	private void consumeChanges(final List<Change> changes)
	{
		final List<ItemChangeDTO> changesToConsume = changes.stream().map(c -> c.getChangeDTO()).collect(Collectors.toList());
		changeDetectionService.consumeChanges(changesToConsume);
	}

	private List<Change> detectChanges(final ComposedTypeModel composedType, final StreamConfiguration configuration)
	{
		final ImmutableList.Builder<Change> resultBuilder = ImmutableList.builder();
		changeDetectionService.collectChangesForType(composedType, configuration, new ChangesCollector()
		{
			@Override
			public void finish()
			{
				//do nothing
			}

			@Override
			public boolean collect(final ItemChangeDTO change)
			{
				resultBuilder.add(new Change(change));
				return true;
			}
		});
		return resultBuilder.build();
	}

	private static String uniqueId()
	{
		return UUID.randomUUID().toString();
	}

	private static class Change
	{
		private final ChangeType changeType;
		private final Long itemPK;
		private final ItemChangeDTO changeDTO;

		public Change(final ChangeType changeType, final Long itemPK)
		{
			this.changeType = Objects.requireNonNull(changeType);
			this.itemPK = Objects.requireNonNull(itemPK);
			this.changeDTO = null;
		}

		public Change(final ItemChangeDTO changeDTO)
		{
			this.changeType = changeDTO.getChangeType();
			this.itemPK = changeDTO.getItemPK();
			this.changeDTO = changeDTO;
		}

		public ItemChangeDTO getChangeDTO()
		{
			return changeDTO;
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(changeType, itemPK);
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (obj == null || !(obj instanceof Change))
			{
				return false;
			}
			if (obj == this)
			{
				return true;
			}

			final Change other = (Change) obj;
			return Objects.equals(this.changeType, other.changeType) && Objects.equals(this.itemPK, other.itemPK);
		}

		@Override
		public String toString()
		{
			return MoreObjects.toStringHelper(this).add("changeType", changeType).add("itemPK", itemPK).toString();
		}
	}
}
