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
package de.hybris.platform.cmscockpit.session.script.components;

import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Test;

/**
 * Created by i840081 on 2014-12-17.
 */
@UnitTest
public class GroupScriptTest {

    @Test(expected = IllegalArgumentException.class)
    public void null_setGroup_paramater_throws_exception()
    {
        GroupScript groupScript = new GroupScript();
        groupScript.setGroup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zero_length_setGroup_paramater_throws_exception()
    {
        GroupScript groupScript = new GroupScript();
        groupScript.setGroup("");
    }
}
