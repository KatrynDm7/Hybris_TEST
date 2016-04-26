/*
 *
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
 */
package de.hybris.platform.entitlementservices.daos.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * JUnit test suite for {@link DefaultEntitlementDao}
 */
@UnitTest
public class DefaultEntitlementDaoTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private DefaultEntitlementDao defaultEntitlementDao;

    @Before
    public void setUp() throws Exception
    {
        defaultEntitlementDao = new DefaultEntitlementDao();
    }

    @Test
    public void testFindEntitlementByIdWhenNoId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Entitlement id must not be null");

        defaultEntitlementDao.findEntitlementByCode(null);
    }
}
