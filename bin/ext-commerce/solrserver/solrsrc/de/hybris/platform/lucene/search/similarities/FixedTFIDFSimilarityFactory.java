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
package de.hybris.platform.lucene.search.similarities;

import org.apache.lucene.search.similarities.Similarity;
import org.apache.solr.schema.SimilarityFactory;

/**
 * Similarity factory that returns the FixedTFIDFSimilarity
 */
public class FixedTFIDFSimilarityFactory extends SimilarityFactory
{

    @Override
    public Similarity getSimilarity()
    {
        return new FixedTFIDFSimilarity();
    }

}
