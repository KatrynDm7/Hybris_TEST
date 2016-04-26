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
package de.hybris.platform.financialfacades.services.document.generation.pdf.fop;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;


/**
 * Document Generation Service
 */
public interface DocumentGenerationService
{
	byte[] generatePdf(InsurancePolicyData policyData);
}
