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
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class ChangeDetectionVersioningTest extends ServicelayerBaseTest
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
	public void shouldDetectNewItemsWithForcedScalarVersionValue()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId(), "'GIVEN_VERSION_VALUE'");

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(added(catalogVersionOne), added(catalogVersionTwo));
		assertThat(changes.get(0).getChangeDTO().getVersionValue()).isEqualTo("GIVEN_VERSION_VALUE");
		assertThat(changes.get(1).getChangeDTO().getVersionValue()).isEqualTo("GIVEN_VERSION_VALUE");
	}

	@Test
	public void shouldDetectNewItemsWithForcedAttributeVersionValue()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId(), "{item.version}");

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(added(catalogVersionOne), added(catalogVersionTwo));
		final Set<String> allVersions = changes.stream().map(cv -> cv.getChangeDTO().getVersionValue()).collect(Collectors.toSet());
		assertThat(allVersions).containsOnly(CATALOG_VERSION_ONE, CATALOG_VERSION_TWO);
	}

	@Test
	public void shouldDetectNewItemsWithForcedComputedVersionValue()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId(),
				"{{select CONCAT(count(*), '') from {CatalogVersion as cv} where {cv.catalog}=?catalog}}").withParameters(
				ImmutableMap.of("catalog", catalog));

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(added(catalogVersionOne), added(catalogVersionTwo));
		assertThat(changes.get(0).getChangeDTO().getVersionValue()).isEqualTo("2");
		assertThat(changes.get(1).getChangeDTO().getVersionValue()).isEqualTo("2");
	}

	@Test
	public void shouldNotDetectModifiedItemsWithForcedScalarVersionValue()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId(), "'GIVEN_VERSION_VALUE'");
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);
		catalogVersionOne.setCategorySystemID("CHANGED");
		modelService.save(catalogVersionOne);

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).isEmpty();
	}

	@Test
	public void shouldNotDetectModifiedItemsWithForcedAttributeVersionValue()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId(), "{item.version}");
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);
		catalogVersionOne.setCategorySystemID("CHANGED");
		modelService.save(catalogVersionOne);

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).isEmpty();
	}

	@Test
	public void shouldNotDetectModifiedItemsWithForcedComputedVersionValue()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId(),
				"{{select count(*) || '' from {CatalogVersion as cv} where {cv.catalog}=?catalog}}").withParameters(
				ImmutableMap.of("catalog", catalog));

		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);
		catalogVersionOne.setCategorySystemID("CHANGED");
		modelService.save(catalogVersionOne);

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).isEmpty();
	}

	@Test
	public void shouldDetectModifiedItemsWithForcedAttributeVersionValue()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId(), "{item.categorySystemID}");
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);
		catalogVersionTwo.setCategorySystemID("CHANGED");
		modelService.save(catalogVersionTwo);

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(modified(catalogVersionTwo));
	}

	@Test
	public void shouldDetectModifiedItemsWithForcedComputedVersionValue()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId(),
				"{{select CONCAT(count(*), '') from {CatalogVersion as cv} where {cv.categorySystemID} is null}}").withParameters(
				ImmutableMap.of("catalog", catalog));

		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);
		catalogVersionOne.setCategorySystemID("CHANGED");
		modelService.save(catalogVersionOne);

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(modified(catalogVersionOne), modified(catalogVersionTwo));
		assertThat(changes.get(0).getChangeDTO().getVersionValue()).isEqualTo("1");
		assertThat(changes.get(1).getChangeDTO().getVersionValue()).isEqualTo("1");
	}

	@Test
	public void shouldDetectOnlyModifiedItemsByForcedVersion()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId(), "{item.categorySystemID}");
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);
		catalogVersionOne.setActive(Boolean.valueOf(!catalogVersionOne.getActive().booleanValue()));
		catalogVersionTwo.setCategorySystemID("CHANGED");
		modelService.save(catalogVersionTwo);

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(modified(catalogVersionTwo));
	}

	@Test
	public void shouldDetectRemovalOfItemWithForcedVersion()
	{
		final StreamConfiguration config = givenConfiguration(uniqueId(), "{item.categorySystemID}");
		final List<Change> changesToConsume = detectChanges(catalogVersionType, config);
		consumeChanges(changesToConsume);
		modelService.remove(catalogVersionTwo);

		final List<Change> changes = detectChanges(catalogVersionType, config);

		assertThat(changes).containsOnly(removed(catalogVersionTwo));
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

	private StreamConfiguration givenConfiguration(final String streamId, final String versionValue)
	{
		return StreamConfiguration.buildFor(streamId).withVersionValue(versionValue);
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
