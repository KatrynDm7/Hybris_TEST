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
package de.hybris.platform.platformbackoffice.services.catalogversion;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.services.catalogversion.CatalogVersionCompareService.CatalogVersionComparison;
import de.hybris.platform.platformbackoffice.services.catalogversion.CatalogVersionCompareService.CatalogVersionDifference;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CatalogVersionCompareServiceTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private CatalogVersionCompareService catalogVersionCompareService;

	private CatalogVersionModel sourceVersion;
	private CatalogVersionModel targetVersion;
	private CatalogVersionModel otherVersion;
	private CatalogVersionModel notConfiguredVersion;

	private SyncItemJobModel sourceToTargetSyncJob;
	private SyncItemJobModel targetToOtherSyncJob;

	private ProductModel product;

	@Before
	public void setUp()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("TEST_CATALOG");

		sourceVersion = modelService.create(CatalogVersionModel.class);
		sourceVersion.setCatalog(catalog);
		sourceVersion.setVersion("TEST_VERSION_1");

		targetVersion = modelService.create(CatalogVersionModel.class);
		targetVersion.setCatalog(catalog);
		targetVersion.setVersion("TEST_VERSION_2");

		otherVersion = modelService.create(CatalogVersionModel.class);
		otherVersion.setCatalog(catalog);
		otherVersion.setVersion("TEST_VERSION_3");

		notConfiguredVersion = modelService.create(CatalogVersionModel.class);
		notConfiguredVersion.setCatalog(catalog);
		notConfiguredVersion.setVersion("TEST_VERSION_4");

		sourceToTargetSyncJob = modelService.create(SyncItemJobModel.class);
		sourceToTargetSyncJob.setSourceVersion(sourceVersion);
		sourceToTargetSyncJob.setTargetVersion(targetVersion);

		targetToOtherSyncJob = modelService.create(SyncItemJobModel.class);
		targetToOtherSyncJob.setSourceVersion(targetVersion);
		targetToOtherSyncJob.setTargetVersion(otherVersion);

		product = modelService.create(ProductModel.class);
		product.setCatalogVersion(sourceVersion);
		product.setCode("TEST_PRODUCT");

		modelService.saveAll();
	}

	@Test
	public void shouldBeAbleToCompareSourceCatalogVersion()
	{
		assertThat(catalogVersionCompareService.canBeCompared(sourceVersion)).isTrue();
	}

	@Test
	public void shouldBeAbleToCompareTargetCatalogVersion()
	{
		assertThat(catalogVersionCompareService.canBeCompared(targetVersion)).isTrue();
	}

	@Test
	public void shouldNotBeAbleToCompareNotConfiguredCatalogVersion()
	{
		assertThat(catalogVersionCompareService.canBeCompared(notConfiguredVersion)).isFalse();
	}

	@Test
	public void shouldNotFindPossibleComparisonForNotConfiguredVersion()
	{
		final Collection<CatalogVersionComparison> comparisons = catalogVersionCompareService
				.getPossibleComparisons(notConfiguredVersion);

		assertThat(comparisons).isEmpty();
	}

	@Test
	public void shouldFindExactlyOnePossibleComparisonForCatalogVersionConfiguredInOnlyOneSyncJob()
	{
		final Collection<CatalogVersionComparison> comparisons = catalogVersionCompareService.getPossibleComparisons(sourceVersion);

		assertThat(comparisons).hasSize(1);
		assertThat(comparisons.stream().findFirst().get().getSyncItemJobPK()).isEqualTo(sourceToTargetSyncJob.getPk());
	}

	@Test
	public void shouldFindAsManyPossibleComparisonAsConfiguredSyncJobs()
	{
		final Collection<CatalogVersionComparison> comparisons = catalogVersionCompareService.getPossibleComparisons(targetVersion);

		final List<PK> pks = comparisons.stream().map(c -> c.getSyncItemJobPK()).collect(Collectors.toList());

		assertThat(pks).hasSize(2);
		assertThat(pks).containsOnly(sourceToTargetSyncJob.getPk(), targetToOtherSyncJob.getPk());
	}

	@Test
	public void shouldFindDifferenceBetweenCatalogVersions()
	{
		final CatalogVersionComparison comparison = catalogVersionCompareService.getPossibleComparisons(sourceVersion).stream()
				.findFirst().get();

		final Collection<CatalogVersionDifference> differences = catalogVersionCompareService.findDifferences(comparison);

		assertThat(differences).hasSize(1);
		final CatalogVersionDifference difference = differences.stream().findFirst().get();
		assertThat(difference.getSourceItemPK()).isEqualTo(product.getPk());
		assertThat(difference.getTypeName()).isEqualTo(ProductModel._TYPECODE);
	}
}
