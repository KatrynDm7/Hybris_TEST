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
package com.sap.hybris.reco.bo;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import de.hybris.platform.sap.core.jco.connection.JCoConnection;

import java.util.concurrent.Callable;

public class ProductRecommendationPrefetcher implements Callable<JCoParameterList> {

    protected JCoConnection jCoConnection;
    protected JCoFunction function;

    public ProductRecommendationPrefetcher(JCoConnection jCoConnection, JCoFunction function) {
        this.jCoConnection = jCoConnection;
        this.function = function;
    }

    @Override
    public JCoParameterList call() throws Exception {
        jCoConnection.execute(function);
        return function.getExportParameterList();
    }
}
