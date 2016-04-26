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
 */

package de.hybris.y2ysync.deltadetection.collector;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.deltadetection.ChangesCollectorFactory;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.platform.core.PK;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;


@UnitTest
public class ItemTypeGroupingCollectorWithBatchingTest
{

	private static final String PRODUCT = "Product";
	private static final String CATEGORY = "Category";
	private static final String VARIANT_PRODUCT = "VariantProduct";

	@Test
	public void shouldGroupChangesByItemType()
	{
		// given
		final TestBatchingCollector productsCollector = new TestBatchingCollector(PRODUCT);
		final TestBatchingCollector categoriesCollector = new TestBatchingCollector(CATEGORY);
		final TestBatchingCollector productVariantsCollector = new TestBatchingCollector(VARIANT_PRODUCT);

		final List<TestBatchingCollector> collectorsPool = Lists.newArrayList(productsCollector, //
				categoriesCollector, //
				productVariantsCollector);

		final ItemTypeGroupingCollectorWithBatching itemTypeGroupingCollectorWithBatching = new ItemTypeGroupingCollectorWithBatching(new TestCollectorFactory(collectorsPool));

		// when
		itemTypeGroupingCollectorWithBatching.collect(dto(1, PRODUCT));
		itemTypeGroupingCollectorWithBatching.collect(dto(2, PRODUCT));
		itemTypeGroupingCollectorWithBatching.collect(dto(11, CATEGORY));
		itemTypeGroupingCollectorWithBatching.collect(dto(112, VARIANT_PRODUCT));
		itemTypeGroupingCollectorWithBatching.collect(dto(12, CATEGORY));
		itemTypeGroupingCollectorWithBatching.collect(dto(3, PRODUCT));

		// then
		assertThat(productsCollector.getCollectedChanges()).hasSize(3);
		assertThat(categoriesCollector.getCollectedChanges()).hasSize(2);
		assertThat(productVariantsCollector.getCollectedChanges()).hasSize(1);
	}

	private ItemChangeDTO dto(final long pk, final String typeCode)
	{
		return new ItemChangeDTO(Long.valueOf(pk), new Date(), ChangeType.MODIFIED, "", typeCode, "ProductStream");
	}

	class TestCollectorFactory implements ChangesCollectorFactory<BatchingCollector>
	{

		private final List<TestBatchingCollector> collectors;
		private int lastReturnedCollectorIdx = 0;

		public TestCollectorFactory(final List<TestBatchingCollector> collectors)
		{
			this.collectors = collectors;
		}

		@Override
		public BatchingCollector create()
		{
			return collectors.get(lastReturnedCollectorIdx++);
		}
	}

	class TestBatchingCollector implements BatchingCollector
	{

		private final List<ItemChangeDTO> collectedChanges = Lists.newArrayList();
		private final String allowedTypeCode;

		public TestBatchingCollector(final String allowedTypeCode)
		{
			this.allowedTypeCode = allowedTypeCode;
		}

		@Override
		public List<PK> getPksOfBatches()
		{
			return null;
		}

		@Override
		public void setId(final String id)
		{
			// empty
		}

		@Override
		public boolean collect(final ItemChangeDTO change)
		{
			if (!change.getItemComposedType().equals(allowedTypeCode))
			{
				throw new IllegalArgumentException("This collector is not allowed to collect changes for type "
						+ change.getItemComposedType());
			}

			collectedChanges.add(change);
			return true;
		}

		@Override
		public void finish()
		{
			// empty
		}

		public List<ItemChangeDTO> getCollectedChanges()
		{
			return collectedChanges;
		}
	}
}
