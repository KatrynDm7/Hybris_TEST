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
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;


/**
 * Qualifier provider for currencies.
 *
 * <p>
 * The available qualifiers will be created based on the configured currencies for the index configuration (see
 * {@link IndexConfig#getCurrencies()}).
 *
 * <p>
 * It supports the following types:
 * <ul>
 * <li>{@link CurrencyModel}
 * </p>
 *
 * @deprecated replaced by {@link de.hybris.platform.solrfacetsearch.provider.impl.CurrencyQualifierProvider}
 */
@Deprecated
public class CurrencyQualifierProvider extends de.hybris.platform.solrfacetsearch.provider.impl.CurrencyQualifierProvider
{
	// deprecated, uses implementation from de.hybris.platform.solrfacetsearch.provider.impl.CurrencyQualifierProvider
}
