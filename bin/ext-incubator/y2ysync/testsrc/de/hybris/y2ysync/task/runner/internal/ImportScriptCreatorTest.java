package de.hybris.y2ysync.task.runner.internal;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.impex.ExportService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;


@IntegrationTest
public class ImportScriptCreatorTest extends ServicelayerBaseTest
{
	private static final String TYPE_CODE = ProductModel._TYPECODE;
	private static final String IMPEX_HEADER = "code[unique=true];ean";

	@Resource
	private ModelService modelService;

	@Resource
	private ExportService exportService;

	@Resource
	ProductService productService;

	private ProductModel product1;
	private ProductModel product2;
	private ProductModel product3;
	private ProductModel product4;

	@Before
	public void setUp()
	{
		final CatalogModel testCatalog = modelService.create(CatalogModel.class);
		testCatalog.setId(uniqueId());

		final CatalogVersionModel testVersion = modelService.create(CatalogVersionModel.class);
		testVersion.setCatalog(testCatalog);
		testVersion.setVersion(uniqueId());

		product1 = createProduct(testVersion);
		product2 = createProduct(testVersion);
		product3 = createProduct(testVersion);
		product4 = createProduct(testVersion);

		modelService.saveAll();
	}

	@Test
	public void shouldNotGenerateImportScriptsWhenThereAreNoChanges()
	{
		final ImportScriptCreator creator = givenImportScriptCreator();

		final Collection<ImportScript> scripts = creator.createImportScripts();

		assertThat(scripts).isEmpty();
	}

	@Test
	public void shouldGenerateOnlyRemoveScriptForRemovedItem()
	{
		final ImportScriptCreator creator = givenImportScriptCreator(removed(product1));

		final Collection<ImportScript> scripts = creator.createImportScripts();
		final TestableImportScript removeScripts = getRemoveScript(scripts);
		final TestableImportScript insertUpdateScript = getInsertUpdateScript(scripts);

		assertThat(scripts).hasSize(1);
		assertThat(removeScripts).isNotNull();
		assertThat(insertUpdateScript).isNull();
		assertThat(removeScripts).containsOnly(product1.getPk());
	}

	@Test
	public void shouldGenerateOnlyInsertUpdateScriptForAddedItem()
	{
		final ImportScriptCreator creator = givenImportScriptCreator(added(product2));

		final Collection<ImportScript> scripts = creator.createImportScripts();
		final TestableImportScript removeScripts = getRemoveScript(scripts);
		final TestableImportScript insertUpdateScript = getInsertUpdateScript(scripts);

		assertThat(scripts).hasSize(1);
		assertThat(removeScripts).isNull();
		assertThat(insertUpdateScript).isNotNull();
		assertThat(insertUpdateScript).containsOnly(product2.getPk());
	}

	@Test
	public void shouldGenerateInsertUpdateScriptForModifiedItem()
	{
		final ImportScriptCreator creator = givenImportScriptCreator(modified(product3));

		final Collection<ImportScript> scripts = creator.createImportScripts();
		final TestableImportScript removeScripts = getRemoveScript(scripts);
		final TestableImportScript insertUpdateScript = getInsertUpdateScript(scripts);

		assertThat(scripts).hasSize(1);
		assertThat(removeScripts).isNull();
		assertThat(insertUpdateScript).isNotNull();
		assertThat(insertUpdateScript).containsOnly(product3.getPk());
	}

	@Test
	public void shouldGenerateInsertUpdateScriptForModifiedAndAddedItem()
	{
		final ImportScriptCreator creator = givenImportScriptCreator(modified(product3), added(product4));

		final Collection<ImportScript> scripts = creator.createImportScripts();
		final TestableImportScript removeScripts = getRemoveScript(scripts);
		final TestableImportScript insertUpdateScript = getInsertUpdateScript(scripts);

		assertThat(scripts).hasSize(1);
		assertThat(removeScripts).isNull();
		assertThat(insertUpdateScript).isNotNull();
		assertThat(insertUpdateScript).containsOnly(product4.getPk(), product3.getPk());
	}

	@Test
	public void shouldGenerateInsertUpdateAndRemoveScripts()
	{
		final ImportScriptCreator creator = givenImportScriptCreator(modified(product3), removed(product2), added(product4),
				removed(product1));

		final Collection<ImportScript> scripts = creator.createImportScripts();
		final TestableImportScript removeScripts = getRemoveScript(scripts);
		final TestableImportScript insertUpdateScript = getInsertUpdateScript(scripts);

		assertThat(scripts).hasSize(2);
		assertThat(removeScripts).isNotNull();
		assertThat(insertUpdateScript).isNotNull();
		assertThat(removeScripts).containsOnly(product2.getPk(), product1.getPk());
		assertThat(insertUpdateScript).containsOnly(product4.getPk(), product3.getPk());
	}

	private TestableImportScript getRemoveScript(final Collection<ImportScript> scripts)
	{
		return scripts.stream().map(TestableImportScript::new).filter(TestableImportScript::isRemoveScript).findFirst().orElse(null);
	}

	private TestableImportScript getInsertUpdateScript(final Collection<ImportScript> scripts)
	{
		return scripts.stream().map(TestableImportScript::new).filter(TestableImportScript::isInsertUpdateScript).findFirst()
				.orElse(null);
	}

	private ProductModel createProduct(final CatalogVersionModel catalogVersion)
	{
		final ProductModel product = modelService.create(ProductModel.class);

		final String id = uniqueId();

		product.setCatalogVersion(catalogVersion);
		product.setCode("CODE_" + id);
		product.setEan("EAN_" + id);

		return product;
	}

	private ImportScriptCreator givenImportScriptCreator(final ItemChangeDTO... changes)
	{
		final ExportScriptCreator exportScriptCreator = new ExportScriptCreator(IMPEX_HEADER, TYPE_CODE,
				ImmutableList.copyOf(changes));
		return new ImportScriptCreator(modelService, exportService, exportScriptCreator);
	}

	private static ItemChangeDTO added(final ProductModel item)
	{
		return new ItemChangeDTO(item.getPk().getLong(), new Date(), ChangeType.NEW, "Added", TYPE_CODE, "testStream");
	}

	private static ItemChangeDTO modified(final ProductModel item)
	{
		return new ItemChangeDTO(item.getPk().getLong(), new Date(), ChangeType.MODIFIED, "Modified", TYPE_CODE, "testStream");
	}

	private static ItemChangeDTO removed(final ProductModel item)
	{
		return new ItemChangeDTO(item.getPk().getLong(), new Date(), ChangeType.DELETED, item.getCode(), TYPE_CODE, "testStream");
	}

	private static String uniqueId()
	{
		return UUID.randomUUID().toString();
	}

	private class TestableImportScript extends ImportScript implements Iterable<PK>
	{
		public TestableImportScript(final ImportScript target)
		{
			super(target.getTypeCode(), target.getHeader(), target.getContent(), null);
		}

		public boolean isRemoveScript()
		{
			return getHeader().toLowerCase().startsWith("remove");
		}

		public boolean isInsertUpdateScript()
		{
			return getHeader().toLowerCase().startsWith("insert_update");
		}

		@Override
		public Iterator<PK> iterator()
		{
			final ImmutableList.Builder<PK> resultBuilder = ImmutableList.builder();
			final Matcher matcher = Pattern.compile("(CODE_[\\w-]+);?").matcher(getContent());

			while (matcher.find())
			{
				final String productCode = matcher.group(1);
				final ProductModel product = productService.getProductForCode(productCode);
				resultBuilder.add(product.getPk());
			}

			return resultBuilder.build().iterator();
		}
	}
}
