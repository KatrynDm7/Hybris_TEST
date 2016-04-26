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
package de.hybris.platform.ycommercewebservices.strategies;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ycommercewebservices.strategies.impl.DefaultOrderCodeIdentificationStrategy;

import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class DefaultOrderCodeIdentificationStrategyTest {

    @Test(expected = IllegalArgumentException.class)
    public void isIdNullTest(){
        DefaultOrderCodeIdentificationStrategy strategy = new DefaultOrderCodeIdentificationStrategy();
        strategy.setIdPattern("[0-9a-f]{40}");
        strategy.isID(null);
    }

    @Test
    public void isIdGuidTest(){
        DefaultOrderCodeIdentificationStrategy strategy = new DefaultOrderCodeIdentificationStrategy();
        strategy.setIdPattern("[0-9a-f]{40}");
        Assert.assertTrue(strategy.isID("8ebefc6b4d8bc429006daf2fbef692002b10d636"));
    }

    @Test
    public void isIdCodeTest(){
        DefaultOrderCodeIdentificationStrategy strategy = new DefaultOrderCodeIdentificationStrategy();
        strategy.setIdPattern("[0-9a-f]{40}");
        Assert.assertFalse(strategy.isID("00001"));
    }
}
