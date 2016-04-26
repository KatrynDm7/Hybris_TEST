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
 */
package de.hybris.platform.entitlementfacades.entitlement.populator;

import com.hybris.services.entitlements.condition.ConditionData;
import de.hybris.platform.entitlementfacades.data.EntitlementData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MeteredConditionEntitlementPopulatorTest
{
    private static final String GRANT_PARAMETER_METERED = "maxQuantity";
    private static final String METERED_TYPE = "metered";
    private static final String WRONG_TYPE = "not_metered";
    private static final String WRONG_QUANTITY = "";
    private static final String CORRECT_QUANTITY = "1";

    private final MeteredConditionEntitlementPopulator<ConditionData, EntitlementData> meteredConditionEntitlementPopulator
            = new MeteredConditionEntitlementPopulator<>();

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testPopulateNotMetered()
    {

        final ConditionData source = new ConditionData();
        source.setType(WRONG_TYPE);

        final EntitlementData result = new EntitlementData();
        meteredConditionEntitlementPopulator.populate(source, result);

        assertNull(result.getConditionGeo());
    }

    @Test
    public void testPopulateNoUsageUnit()
    {
        final ConditionData source = new ConditionData();
        source.setType(METERED_TYPE);
        source.setProperty(GRANT_PARAMETER_METERED, CORRECT_QUANTITY);

        final EntitlementData result = new EntitlementData();
        meteredConditionEntitlementPopulator.populate(source, result);

        assertEquals(Integer.parseInt(CORRECT_QUANTITY), result.getQuantity());
    }

    @Test
    public void testPopulateWrongQuantity()
    {
        final ConditionData source = new ConditionData();
        source.setType(METERED_TYPE);
        source.setProperty(GRANT_PARAMETER_METERED, WRONG_QUANTITY);

        final EntitlementData result = new EntitlementData();

        thrown.expect(ConversionException.class);
        thrown.expectMessage("Quantity must be integer");

        meteredConditionEntitlementPopulator.populate(source, result);
    }

    @Test
    public void testPopulateMetered()
    {
        final ConditionData source = new ConditionData();
        source.setType(METERED_TYPE);
        source.setProperty(GRANT_PARAMETER_METERED, CORRECT_QUANTITY);

        final EntitlementData result = new EntitlementData();

        meteredConditionEntitlementPopulator.populate(source, result);

        assertEquals(1, result.getQuantity());
    }
}
