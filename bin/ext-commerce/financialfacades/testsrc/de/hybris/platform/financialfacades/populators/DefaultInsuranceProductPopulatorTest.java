package de.hybris.platform.financialfacades.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialfacades.populators.DefaultInsuranceProductPopulator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.HashSet;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * Test suite for {@link DefaultInsuranceProductPopulatorTest}.
 */
@UnitTest
public class DefaultInsuranceProductPopulatorTest
{

	@Mock
	private Converter<CategoryModel, CategoryData> categoryConverter;

	@InjectMocks
	private DefaultInsuranceProductPopulator<ProductModel, ProductData> defaultInsuranceProductPopulator;

	@Before
	public void setup()
	{
		defaultInsuranceProductPopulator = new DefaultInsuranceProductPopulator<>();
		MockitoAnnotations.initMocks(this);
		defaultInsuranceProductPopulator.setCategoryConverter(categoryConverter);
	}

	@Test
	public void testPopulate()
	{
		final ProductModel source = mock(ProductModel.class);
		final CategoryModel category1 = mock(CategoryModel.class);
		final CategoryData categoryData1 = mock(CategoryData.class);
		final Collection<MediaModel> medias = new HashSet<>();
		MediaModel media = new MediaModel();
		media.setURL("");
		medias.add(media);
		given(source.getDefaultCategory()).willReturn(category1);
		given(categoryConverter.convert(category1)).willReturn(categoryData1);
		given(source.getData_sheet()).willReturn(medias);
		final ProductData result = new ProductData();
		defaultInsuranceProductPopulator.populate(source, result);

		Assert.assertEquals(categoryData1, result.getDefaultCategory());
		Assert.assertNotNull(result.getSpecifications());
        Assert.assertTrue(result.getSpecifications().size() > 0);
	}
}
