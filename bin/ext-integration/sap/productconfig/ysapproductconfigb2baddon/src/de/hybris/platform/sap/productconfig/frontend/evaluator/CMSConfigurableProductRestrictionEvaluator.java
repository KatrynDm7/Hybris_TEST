/**
 *
 */
package de.hybris.platform.sap.productconfig.frontend.evaluator;

import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.frontend.model.CMSConfigurableProductRestrictionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author D064607
 *
 */
public class CMSConfigurableProductRestrictionEvaluator implements CMSRestrictionEvaluator<CMSConfigurableProductRestrictionModel>
{

	private String configurableSource;

	private ModelService modelService;

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	@Required
	public void setConfigurableSource(final String configurableSource)
	{
		this.configurableSource = configurableSource;
	}

	@Override
	public boolean evaluate(final CMSConfigurableProductRestrictionModel restriction, final RestrictionData context)
	{

		final ProductModel product = context.getProduct();
		final Boolean isConfigurable = modelService.getAttributeValue(product, configurableSource);
		return isConfigurable.booleanValue();
	}



}
