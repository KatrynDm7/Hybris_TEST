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
import de.hybris.platform.commercefacades.insurance.data.PolicyHolderDetailData;
import de.hybris.platform.xyformsfacades.data.YFormDataData;

import javax.xml.xpath.XPath;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.w3c.dom.Document;


/**
 * The class of AutoInsuranceInsurancePolicyHolderDetailsPopulator.
 */
public class AutoInsurancePolicyHolderDetailsPopulator extends AbstractInsuranceDetailsPopulator
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AutoInsurancePolicyHolderDetailsPopulator.class);
	protected final String XPATH_AUTO_HOLDER = "/form/personal-details";

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
			final String firstName = getTextValue(xpath, document, XPATH_AUTO_HOLDER + "/first-name");
			final String lastName = getTextValue(xpath, document, XPATH_AUTO_HOLDER + "/last-name");
			final String address1 = getTextValue(xpath, document, XPATH_AUTO_HOLDER + "/address-line1");
			final String address2 = getTextValue(xpath, document, XPATH_AUTO_HOLDER + "/address-line2");
			final String city = getTextValue(xpath, document, XPATH_AUTO_HOLDER + "/address-city");
			final String postcode = getTextValue(xpath, document, XPATH_AUTO_HOLDER + "/address-postcode");
			final String country = getTextValue(xpath, document, XPATH_AUTO_HOLDER + "/address-country");
			final String phone = getTextValue(xpath, document, XPATH_AUTO_HOLDER + "/phone");
			final String email = getTextValue(xpath, document, XPATH_AUTO_HOLDER + "/email");
			PolicyHolderDetailData policyHolderDetail = detailData.getPolicyHolderDetail();

			if (policyHolderDetail == null)
			{
				policyHolderDetail = new PolicyHolderDetailData();
				detailData.setPolicyHolderDetail(policyHolderDetail);
			}

			policyHolderDetail.setFirstName(firstName);
			policyHolderDetail.setLastName(lastName);
			policyHolderDetail.setAddressLine1(address1);
			policyHolderDetail.setAddressLine2(address2);
			policyHolderDetail.setAddressCity(city);
			policyHolderDetail.setPostcode(postcode);
			policyHolderDetail.setAddressCountry(getCountryNameForIsocode(country));
			policyHolderDetail.setPhoneNumber(phone);
			policyHolderDetail.setEmailAddress(email);
		}
	}
}
