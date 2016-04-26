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
package de.hybris.platform.solr.search;

import de.hybris.platform.solr.search.MultiMaxScoreQParser;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;

public class MultiMaxScoreQParserPlugin extends QParserPlugin
{
	@Override
	public QParser createParser(final String qstr, final SolrParams localParams, final SolrParams params,
			final SolrQueryRequest req)
	{
		return new MultiMaxScoreQParser(qstr, localParams, params, req);
	}

	@Override
	public void init(final NamedList args)
	{

	}
}
