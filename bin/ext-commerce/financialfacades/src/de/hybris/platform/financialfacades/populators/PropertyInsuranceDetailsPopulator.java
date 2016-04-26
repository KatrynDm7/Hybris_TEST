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
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.xyformsfacades.data.YFormDataData;

import javax.xml.xpath.XPath;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.w3c.dom.Document;


public class PropertyInsuranceDetailsPopulator extends AbstractInsuranceDetailsPopulator
{
	private static final Logger LOG = Logger.getLogger(PropertyInsuranceDetailsPopulator.class);
	protected final String XPATH_PERSONAL_DETAIL = "/form/holder-details-section";

	@Override
	public void populate(final YFormDataData yFormDataData, final InsurancePolicyData detailData) throws ConversionException
	{

		Assert.notNull(yFormDataData, "yFormDataData cannot be null.");
		Assert.notNull(detailData, "InsuranceQuoteReviewDetailData cannot be null.");
		if (yFormDataData.getContent() != null)
		{
			final Document document = createDocument(yFormDataData.getContent());
			final XPath xpath = createXPath();

			PolicyHolderDetailData policyHolderDetail = detailData.getPolicyHolderDetail();
			if (policyHolderDetail == null)
			{
				policyHolderDetail = new PolicyHolderDetailData();
			}

			policyHolderDetail.setTitle(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/title"));
			policyHolderDetail.setFirstName(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/first-name"));
			policyHolderDetail.setLastName(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/last-name"));
			policyHolderDetail.setMaritalStatus(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/marital-status"));
			policyHolderDetail.setEmailAddress(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/email-address"));

			policyHolderDetail.setHomePhoneNumber(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/home-phone-number"));
			policyHolderDetail.setMobilePhoneNumber(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/mobile-number/"));
			policyHolderDetail.setPreferredContactNumber(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL
					+ "/preferred-contact-number"));

			policyHolderDetail.setPropertyCorrespondenceAddress(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL
					+ "/property-correspondance-address"));
			policyHolderDetail.setAddressLine1(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/address-line-1"));
			policyHolderDetail.setAddressLine2(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/address-line-2"));
			policyHolderDetail.setAddressCity(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/city"));
			policyHolderDetail.setPostcode(getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/control-13"));

			final String country = getTextValue(xpath, document, XPATH_PERSONAL_DETAIL + "/country");
			policyHolderDetail.setAddressCountry(getCountryNameForIsocode(country));

			detailData.setPolicyHolderDetail(policyHolderDetail);

		}
	}
}
