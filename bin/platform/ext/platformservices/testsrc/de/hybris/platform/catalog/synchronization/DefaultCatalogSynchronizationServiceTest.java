package de.hybris.platform.catalog.synchronization;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;



public class DefaultCatalogSynchronizationServiceTest extends ServicelayerBaseTest
{
	public static final int NUM_OF_PRODUCTS = 500;
	public static final int NUM_OF_REFS = 20;
	public static final int NUMBER_OF_THREADS = 10;

	@Resource
	private ModelService modelService;
	@Resource(name = "catalogSynchronizationService")
	private CatalogSynchronizationService syncService;
	private CatalogVersionModel source, target;
	private List<ProductModel> sourceProducts;
	private SyncConfig testSyncConfig;

	@Before
	public void setUp() throws Exception
	{
		final CatalogModel catalog = createCatalog("TestCatalog");
		source = createCatalogVersion(catalog, "staged");
		target = createCatalogVersion(catalog, "online");

		sourceProducts = createProducts(NUM_OF_PRODUCTS, source);
		createProductReferences(sourceProducts, NUM_OF_REFS);

		CatalogVersionAssert.assertThat(source).hasNumOfProducts(NUM_OF_PRODUCTS);

		testSyncConfig = prepareDefaultSyncConfig();
	}

	@Test
	public void shouldSynchronizeTwoCatalogsFullyMultithreaded() throws Exception
	{
		syncService.synchronizeFully(source, target, NUMBER_OF_THREADS);

		final ImmutableMap<String, String> expectedProperties = ImmutableMap.<String, String> builder().put("code", "MyCode")
				.put("ean", "MyEan").build();
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedProperties);

		modifyProductsInSource();
		final ImmutableMap<String, String> expectedPropertiesAfterMod = ImmutableMap.<String, String> builder()
				.put("code", "NewCode").put("ean", "NewEan").build();
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(source).hasAllProductsWithPropertiesAs(expectedPropertiesAfterMod);

		syncService.synchronizeFully(source, target, NUMBER_OF_THREADS);
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedPropertiesAfterMod);
	}

	@Test
	public void shouldSynchronizeWithSyncJobAndConfigSynchronous() throws Exception
	{
		syncService.synchronizeFully(source, target, NUMBER_OF_THREADS);

		final ImmutableMap<String, String> expectedProperties = ImmutableMap.<String, String> builder().put("code", "MyCode")
				.put("ean", "MyEan").build();
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedProperties);

		modifyProductsInSource();

		final ImmutableMap<String, String> expectedPropertiesAfterMod = ImmutableMap.<String, String> builder()
				.put("code", "NewCode").put("ean", "NewEan").build();
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(source).hasAllProductsWithPropertiesAs(expectedPropertiesAfterMod);

		testSyncConfig.setSynchronous(Boolean.TRUE);
		final CatalogVersionSyncJobModel syncJob = (CatalogVersionSyncJobModel) createSyncJob(source, target);

		final SyncResult syncResult = syncService.synchronize(syncJob, testSyncConfig);

		assertThat(syncResult).isNotNull();
		assertThat(syncResult.isFinished()).isTrue();
		assertThat(syncResult.isSuccessful()).isTrue();
		assertThat(syncResult.getCronJob()).isInstanceOf(CatalogVersionSyncCronJobModel.class);
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedPropertiesAfterMod);
	}

	private SyncItemJobModel createSyncJob(final CatalogVersionModel source, final CatalogVersionModel target)
	{
		final CatalogVersionSyncJobModel job = modelService.create(CatalogVersionSyncJobModel.class);
		job.setCode("testSyncJob");
		job.setSourceVersion(source);
		job.setTargetVersion(target);
		job.setRemoveMissingItems(true);
		job.setCreateNewItems(true);
		job.setMaxThreads(NUMBER_OF_THREADS);
		modelService.save(job);
		return job;
	}

	@Test
	public void shouldSynchronizeRemovedItems()
	{
		syncService.synchronizeFully(source, target, NUMBER_OF_THREADS);
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS);

		final List<ProductModel> removed = removeSomeProductsInSource();
		assertThat(removed).isNotEmpty();
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(NUM_OF_PRODUCTS - removed.size());

		syncService.synchronizeFully(source, target, NUMBER_OF_THREADS);

		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS - removed.size());
	}

	private List<ProductModel> removeSomeProductsInSource()
	{
		final List<ProductModel> toRemove = sourceProducts.subList(sourceProducts.size() / 2, sourceProducts.size());
		modelService.removeAll(toRemove);
		return toRemove;
	}

	private void modifyProductsInSource()
	{
		int i = 0;
		for (final ProductModel product : sourceProducts)
		{
			product.setCode("NewCode" + i);
			product.setEan("NewEan" + i);
			modelService.save(product);
			i++;
		}
	}

	private CatalogModel createCatalog(final String id)
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(id);
		modelService.save(catalog);

		return catalog;
	}

	private CatalogVersionModel createCatalogVersion(final CatalogModel catalog, final String version)
	{
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion(version);
		catalogVersion.setCatalog(catalog);
		modelService.save(catalogVersion);

		return catalogVersion;
	}

	private List<ProductModel> createProducts(final int numOfProducts, final CatalogVersionModel version)
	{
		final List<ProductModel> products = new ArrayList<>(numOfProducts);
		for (int i = 0; i < numOfProducts; i++)
		{
			products.add(createProduct("MyCode-" + i, "MyEan-" + i, version));
		}

		return products;
	}

	private ProductModel createProduct(final String code, final String ean, final CatalogVersionModel version)
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode(code);
		product.setEan(ean);
		product.setCatalogVersion(version);
		modelService.save(product);

		return product;
	}

	private List<ProductReferenceModel> createProductReferences(final List<ProductModel> products, final int numOfRefs)
	{
		final List<ProductReferenceModel> refs = new ArrayList<>(numOfRefs);
		for (int i = 0; i < products.size(); i++)
		{
			final ProductModel sourceProduct = products.get(i);

			for (int j = 0; j < numOfRefs; j++)
			{
				final ProductModel targetProduct = products.get((i + j + 1) % products.size());

				refs.add(createProductReference("Ref" + i, sourceProduct, targetProduct));
			}
		}

		return refs;
	}

	private ProductReferenceModel createProductReference(final String qualifier, final ProductModel source,
			final ProductModel target)
	{
		final ProductReferenceModel ref = modelService.create(ProductReferenceModel.class);
		ref.setQualifier(qualifier);
		ref.setSource(source);
		ref.setTarget(target);
		ref.setQuantity(Integer.valueOf(1));
		ref.setActive(Boolean.FALSE);
		ref.setPreselected(Boolean.FALSE);

		modelService.save(ref);

		return ref;
	}

	private SyncConfig prepareDefaultSyncConfig()
	{
		final SyncConfig config = new SyncConfig();
		config.setCreateSavedValues(Boolean.FALSE);
		config.setLogToDatabase(Boolean.FALSE);
		config.setLogToFile(Boolean.TRUE);
		config.setLogLevelDatabase(JobLogLevel.WARNING);
		config.setLogLevelFile(JobLogLevel.INFO);
		config.setForceUpdate(Boolean.FALSE);
		config.setErrorMode(ErrorMode.IGNORE);

		config.setSynchronous(Boolean.FALSE);
		config.setFullSync(Boolean.TRUE);

		return config;
	}

}
