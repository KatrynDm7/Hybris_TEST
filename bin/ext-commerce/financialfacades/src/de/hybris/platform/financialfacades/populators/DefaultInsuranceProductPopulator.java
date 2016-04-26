package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * DefaultInsuranceProductPopulator
 */
public class DefaultInsuranceProductPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{
	private Converter<CategoryModel, CategoryData> categoryConverter;

	/**
	 * Populate the target instance with values from the source instance.
	 * 
	 * @param productModel
	 * @param productData
	 * @throws de.hybris.platform.servicelayer.dto.converter.ConversionException
	 *            if an error occurs
	 */
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		if (productModel.getDefaultCategory() != null)
		{
			final CategoryData categoryData = getCategoryConverter().convert(productModel.getDefaultCategory());
			productData.setDefaultCategory(categoryData);
			List<String> specifications = new ArrayList<>();
			if (!CollectionUtils.isEmpty(productModel.getData_sheet()))
			{
				for (MediaModel model : productModel.getData_sheet())
				{
					specifications.add(model.getDownloadURL());
				}
			}
			productData.setSpecifications(specifications);
		}
	}

	protected Converter<CategoryModel, CategoryData> getCategoryConverter()
	{
		return categoryConverter;
	}

	@Required
	public void setCategoryConverter(Converter<CategoryModel, CategoryData> categoryConverter)
	{
		this.categoryConverter = categoryConverter;
	}
}
