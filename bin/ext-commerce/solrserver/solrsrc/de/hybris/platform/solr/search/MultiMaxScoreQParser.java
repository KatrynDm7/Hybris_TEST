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

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.LuceneQParser;
import org.apache.solr.search.SyntaxError;


public class MultiMaxScoreQParser extends LuceneQParser
{
	float tie = 0.0f;

	/**
	 * Constructor for the QParser
	 *
	 * @param qstr
	 * 		The part of the query string specific to this parser
	 * @param localParams
	 * 		The set of parameters that are specific to this QParser.  See http://wiki.apache.org/solr/LocalParams
	 * @param params
	 * 		The rest of the {@link org.apache.solr.common.params.SolrParams}
	 * @param req
	 * 		The original {@link org.apache.solr.request.SolrQueryRequest}.
	 */
	public MultiMaxScoreQParser(final String qstr, final SolrParams localParams, final SolrParams params,
			final SolrQueryRequest req)
	{
		super(qstr, localParams, params, req);

		if (getParam("tie") != null)
		{
			tie = Float.parseFloat(getParam("tie"));
		}
	}

	@Override
	public Query parse() throws SyntaxError
	{
		final Query q = super.parse();

		if (!(q instanceof BooleanQuery))
		{
			return q;
		}

		final BooleanQuery obq = (BooleanQuery) q;
		final BooleanQuery newq = new BooleanQuery();

		DisjunctionMaxQuery dmq = null;

		for (final BooleanClause clause : obq.getClauses())
		{
			if (clause.isProhibited() || clause.isRequired())
			{
				newq.add(clause);
			}
			else
			{
				final Query subQuery = clause.getQuery();
				if (!(subQuery instanceof BooleanQuery))
				{
					if (dmq == null)
					{
						dmq = new DisjunctionMaxQuery(tie);
						newq.add(dmq, BooleanClause.Occur.SHOULD);
					}

					dmq.add(clause.getQuery());
				}
				else
				{
					final DisjunctionMaxQuery subDmq = new DisjunctionMaxQuery(tie);
					for (final BooleanClause subQueryClause : ((BooleanQuery) subQuery).getClauses())
					{
						subDmq.add(subQueryClause.getQuery());
					}
					newq.add(subDmq, BooleanClause.Occur.SHOULD);
				}
			}
		}

		newq.setBoost(obq.getBoost());

		return newq;
	}
}
