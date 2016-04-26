package de.hybris.y2ysync.impex.typesystem;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class ImpexHeaderBuilderIntegrationTest extends ServicelayerBaseTest
{
	@Resource(name = "impexHeaderBuilder")
	private ImpexHeaderBuilder builder;

	@Test
	public void shouldReturnHeaderForSimpleAtomicAttribute() throws Exception
	{
		// given
		final String typeCode = "ProductFeature";
		final String qualifier = "valuePosition";

		// when
		final String header = builder.getHeaderFor(typeCode, qualifier);

		// then
		assertThat(header).isEqualTo("valuePosition[unique=true]");
	}

	@Test
	public void shouldReturnHeaderForReferenceAttributeMappingItsUniqueAttributes() throws Exception
	{
		// given
		final String typeCode = "ProductFeature";
		final String qualifier = "product";

		// when
		final String header = builder.getHeaderFor(typeCode, qualifier);

		// then
		assertThat(header).isEqualTo("product(catalogVersion(catalog(id),version),code)[unique=true]");
	}

	@Test
	public void shouldReturnHeaderForLocalizedRelation() throws Exception
	{
		// given
		final String typeCode = "Keyword";
		final String qualifier = "products";

		// when
		final String header = builder.getHeaderFor(typeCode, qualifier);

		// then
		assertThat(header).isEqualTo("products(key(isocode),value(catalogVersion(catalog(id),version),code))");
	}

	@Test
	public void shouldReturnHeaderForNonLocalizedRelation() throws Exception
	{
		// given
		final String typeCode = "MediaContainer";
		final String qualifier = "medias";

		// when
		final String header = builder.getHeaderFor(typeCode, qualifier);

		// then
		assertThat(header).isEqualTo("medias(catalogVersion(catalog(id),version),code)");
	}

	@Test
	public void shouldReturnHeaderForDateAttributeWithProperFormat() throws Exception
	{
		// given
		final String typeCode = "Item";
		final String qualifier = "modifiedtime";

		// when
		final String header = builder.getHeaderFor(typeCode, qualifier);

		// then
		assertThat(header).isEqualTo("modifiedtime[dateformat=dd.MM.yyyy hh:mm:ss]");
	}

}
