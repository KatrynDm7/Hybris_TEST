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
 *
 *
 */
package de.hybris.platform.storefront.checkout.strategy.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.storefront.constants.InsurancecheckoutConstants;
import de.hybris.platform.storefront.form.data.FormDetailData;
import de.hybris.platform.xyformsfacades.strategy.preprocessor.YFormProcessorException;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Maps;


/**
 * The class of TravelInsuranceTransformerYFormPreprocessorStrategy.
 */
public class TravelInsuranceTransformerYFormPreprocessorStrategy extends InsuranceYFormDataPreprocessorStrategy
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(TravelInsuranceTransformerYFormPreprocessorStrategy.class);

	private CartService cartService;

	private ModelService modelService;

	/**
	 * Applies the actual transformation to a formData
	 *
	 * @param xmlContent
	 * @param params
	 * @throws de.hybris.platform.xyformsfacades.strategy.preprocessor.YFormProcessorException
	 */
	@Override
	protected String transform(final String xmlContent, final Map<String, Object> params) throws YFormProcessorException
	{
		final String xmlString = super.transform(xmlContent, params);

		if (!validation(params))
		{
			return xmlString;
		}

		final CartModel cartModel = getCartService().getSessionCart();

		final InsuranceQuoteModel quoteModel = cartModel.getInsuranceQuote();
		if (quoteModel == null || MapUtils.isEmpty(quoteModel.getProperties()))
		{
			return xmlString;
		}

		final Map<String, Object> travelInsuranceParams = Maps.newHashMap();

		if (quoteModel.getProperties().containsKey(InsurancecheckoutConstants.TRIP_DETAILS_NO_OF_TRAVELLERS))
		{
			final Integer noOfTravellers = MapUtils.getInteger(quoteModel.getProperties(),
					InsurancecheckoutConstants.TRIP_DETAILS_NO_OF_TRAVELLERS, NumberUtils.INTEGER_ZERO);
			travelInsuranceParams.put("/form/number-of-travellers", noOfTravellers);

			if (quoteModel.getProperties().containsKey(InsurancecheckoutConstants.TRIP_DETAILS_TRAVELLER_AGES_FOR_PRE_FORM_POPULATE)
					&& quoteModel.getProperties().get(InsurancecheckoutConstants.TRIP_DETAILS_TRAVELLER_AGES_FOR_PRE_FORM_POPULATE) instanceof List)
			{
				final List<String> ages = (List<String>) quoteModel.getProperties().get(
						InsurancecheckoutConstants.TRIP_DETAILS_TRAVELLER_AGES_FOR_PRE_FORM_POPULATE);
				travelInsuranceParams.put("/form/personal-details/age", ages.get(NumberUtils.INTEGER_ZERO));
				for (int i = NumberUtils.INTEGER_ONE; i < ages.size(); i++)
				{
					travelInsuranceParams.put("/form/traveller-" + i + "/age-" + i, ages.get(i));
				}

				removeAgesForPreFormPopulate(quoteModel);
			}
		}

		return updateXmlContent(xmlString, travelInsuranceParams);
	}

	protected void removeAgesForPreFormPopulate(final InsuranceQuoteModel quoteModel)
	{
		if (quoteModel.getProperties().containsKey(InsurancecheckoutConstants.TRIP_DETAILS_TRAVELLER_AGES_FOR_PRE_FORM_POPULATE)
				&& quoteModel.getProperties().get(InsurancecheckoutConstants.TRIP_DETAILS_TRAVELLER_AGES_FOR_PRE_FORM_POPULATE) instanceof List)
		{
			final Map<String, Object> newMap = Maps.newHashMap(quoteModel.getProperties());

			newMap.remove(InsurancecheckoutConstants.TRIP_DETAILS_TRAVELLER_AGES_FOR_PRE_FORM_POPULATE);
			quoteModel.setProperties(newMap);

			getModelService().save(quoteModel);
		}
	}

	protected boolean validation(final Map<String, Object> params)
	{
		return params.containsKey(FORM_DETAIL_DATA) && params.get(FORM_DETAIL_DATA) instanceof FormDetailData;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
