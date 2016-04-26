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
package de.hybris.platform.commerceservices.product;

import de.hybris.platform.commerceservices.product.data.ReferenceData;

import java.util.List;


public interface CommerceProductReferenceService<TYPE, TARGET>
{
	/**
	 * @deprecated use getProductReferencesForCode(final String code, final List<TYPE> referenceTypes, final Integer
	 *             limit); instead.
	 */
	@Deprecated
	List<ReferenceData<TYPE, TARGET>> getProductReferencesForCode(final String code, final TYPE referenceType, final Integer limit);

	/**
	 * Retrieves product references for a given product and product reference type.
	 * 
	 * @param code
	 *           the product code
	 * @param referenceTypes
	 *           the product reference types to return
	 * @param limit
	 *           maximum number of references to retrieve. If null, all available references will be retrieved.
	 * @return a collection product references.
	 */
	List<ReferenceData<TYPE, TARGET>> getProductReferencesForCode(final String code, final List<TYPE> referenceTypes,
			final Integer limit);
}
