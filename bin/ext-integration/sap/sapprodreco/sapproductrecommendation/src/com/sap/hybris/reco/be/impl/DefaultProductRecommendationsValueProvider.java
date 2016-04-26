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
package com.sap.hybris.reco.be.impl;

import com.google.common.collect.Lists;
import com.sap.hybris.reco.be.ProductRecommendationsValueProvider;
import com.sap.hybris.reco.dao.ProductRecommendationData;

import org.apache.log4j.Logger;

import java.util.List;

public class DefaultProductRecommendationsValueProvider implements ProductRecommendationsValueProvider {

    private static final Logger LOG = Logger.getLogger(DefaultProductRecommendationsValueProvider.class);

    @Override
    public List<ProductRecommendationData> getDefaultResult() {
        if(LOG.isDebugEnabled()){
            LOG.debug("Providing default product recommendation result");
        }
        return Lists.newArrayList();
    }
}
