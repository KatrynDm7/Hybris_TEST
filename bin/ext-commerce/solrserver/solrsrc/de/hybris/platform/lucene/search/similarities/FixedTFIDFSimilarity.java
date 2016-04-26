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

import org.apache.lucene.search.similarities.DefaultSimilarity;

/**
 * Similarity implementation that uses a fixed TF and IDF of 1.0 for scoring.
 * This is intended to be used for boosted fields where standard full-text scoring metrics are not relevant.
 */
public class FixedTFIDFSimilarity extends DefaultSimilarity
{
    /**
     * Always returns 1.0 as inverse document frequency
     */
    @Override
    public float idf(long docFreq, long numDocs)
    {
        return 1.0F;
    }

    /**
     * Always returns 1.0 as term frequency
     */
    @Override
    public float tf(float freq)
    {
        return 1.0F;
    }
}
