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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.commercefacades.insurance.data.TravellerDetailData;
import de.hybris.platform.xyformsfacades.data.YFormDataData;

import java.util.List;

import javax.xml.xpath.XPath;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.w3c.dom.Document;

import com.google.common.collect.Lists;


/**
 * The class of TravelInsuranceTravellersDetailsPopulator.
 */
public class TravelInsuranceTravellersDetailsPopulator extends AbstractInsuranceDetailsPopulator
{
	protected final String XPATH_TRAVELLER_HOLDER = "/form/personal-details";
	protected final String XPATH_OTHER_TRAVELLER = "/form";

	/**
	 * Populate the target instance with values from the source instance.
	 *
	 * @param yFormDataData
	 *           the source object
	 * @param detailData
	 *           the target to fill
	 */
	@Override
	public void populate(final YFormDataData yFormDataData, final InsurancePolicyData detailData)
	{
		Assert.notNull(yFormDataData, "yFormDataData cannot be null.");
		Assert.notNull(detailData, "InsuranceQuoteReviewDetailData cannot be null.");

		if (yFormDataData.getContent() != null)
		{

			final Document document = createDocument(yFormDataData.getContent());
			final XPath xpath = createXPath();

			final TravellerDetailData traveller1 = new TravellerDetailData();
			traveller1.setFirstName(getTextValue(xpath, document, XPATH_TRAVELLER_HOLDER + "/first-name"));
			traveller1.setLastName(getTextValue(xpath, document, XPATH_TRAVELLER_HOLDER + "/last-name"));

			final String mainTravellerAge = getTextValue(xpath, document, XPATH_TRAVELLER_HOLDER + "/age");
			if (StringUtils.isNotBlank(mainTravellerAge))
			{
				traveller1.setAge(Integer.valueOf(mainTravellerAge));
			}

			final List<TravellerDetailData> travellerDetailsList = Lists.newLinkedList();

			travellerDetailsList.add(traveller1);

			final int noOfTravellers = Integer.valueOf(getTextValue(xpath, document, "/form/number-of-travellers"));

			for (int i = 1; i < noOfTravellers; i++)
			{
				final TravellerDetailData traveller = new TravellerDetailData();
				final String firstName = getTextValue(xpath, document, XPATH_OTHER_TRAVELLER + "/traveller-" + i + "/first-name-" + i);
				final String lastName = getTextValue(xpath, document, XPATH_OTHER_TRAVELLER + "/traveller-" + i + "/last-name-" + i);
				final String age = getTextValue(xpath, document, XPATH_OTHER_TRAVELLER + "/traveller-" + i + "/age-" + i);
				traveller.setFirstName(firstName);
				traveller.setLastName(lastName);
				traveller.setAge(Integer.valueOf(age));

				travellerDetailsList.add(traveller);
			}


			detailData.setTravellers(travellerDetailsList);
		}
	}
}
