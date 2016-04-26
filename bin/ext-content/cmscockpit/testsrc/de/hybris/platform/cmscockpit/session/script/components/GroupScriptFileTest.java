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
import de.hybris.platform.cmscockpit.session.script.ScriptGroup;
import de.hybris.platform.cmscockpit.session.script.config.impl.SingleJavaScriptFile;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created by i840081 on 2014-12-17.
 */
@UnitTest
public class GroupScriptFileTest {

    public static final String TEST_JS = "test.js";
    public static final String MAP_ID_1 = "id1";
    public static final String MAP_ID_2 = "id2";
    public static final String INVALID_SCRIPT_GROUP = "INVALID_SCRIPT_GROUP";
    public static final String EMPTY_STRING = "";

    @Test(expected = IllegalArgumentException.class)
    public void null_setGroup_paramater_throws_exception()
    {
        GroupScriptFile groupScriptFile = new GroupScriptFile();
        groupScriptFile.setGroup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zero_length_setGroup_paramater_throws_exception()
    {
        GroupScriptFile groupScriptFile = new GroupScriptFile();
        groupScriptFile.setGroup(EMPTY_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void null_getScriptGroupType_parameter_throws_exception()
    {
        GroupScriptFile groupScriptFile = new GroupScriptFile();
        groupScriptFile.getScriptGroupType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void non_existant_getScriptGroupType_parameter_throws_exception()
    {
        GroupScriptFile groupScriptFile = new GroupScriptFile();
        groupScriptFile.getScriptGroupType(EMPTY_STRING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalid_script_group_throws_exception()
    {
        GroupScriptFile groupScriptFile = new GroupScriptFile();
        groupScriptFile.getScriptGroupType(INVALID_SCRIPT_GROUP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void more_than_one_SingleJavaScriptFile_for_group_throws_exception()
    {
        SingleJavaScriptFile singleJavaScriptFile = mock(SingleJavaScriptFile.class);
        when(singleJavaScriptFile.getGroup()).thenReturn(ScriptGroup.LIVEEDIT_SCRIPT_FILE);
        when(singleJavaScriptFile.getScript()).thenReturn(TEST_JS);

        SingleJavaScriptFile singleJavaScriptFile1 = mock(SingleJavaScriptFile.class);
        when(singleJavaScriptFile1.getGroup()).thenReturn(ScriptGroup.LIVEEDIT_SCRIPT_FILE);
        when(singleJavaScriptFile1.getScript()).thenReturn(TEST_JS);

        Map singleJavaScriptFiles = new HashedMap();
        singleJavaScriptFiles.put(MAP_ID_1, singleJavaScriptFile);
        singleJavaScriptFiles.put(MAP_ID_2, singleJavaScriptFile1);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBeansOfType(SingleJavaScriptFile.class)).thenReturn(singleJavaScriptFiles);

        GroupScriptFile groupScriptFile = spy(new GroupScriptFile());
        doReturn(applicationContext).when(groupScriptFile).getApplicationContext();
        doNothing().when(groupScriptFile).setSrc(anyString());

        groupScriptFile.setGroup(ScriptGroup.LIVEEDIT_SCRIPT_FILE.name());
    }

    @Test
    public void setSrc_method_called_in_GroupScriptFile_class()
    {
        SingleJavaScriptFile singleJavaScriptFile = mock(SingleJavaScriptFile.class);
        when(singleJavaScriptFile.getGroup()).thenReturn(ScriptGroup.LIVEEDIT_SCRIPT_FILE);
        when(singleJavaScriptFile.getScript()).thenReturn(TEST_JS);

        Map singleJavaScriptFiles = new HashedMap();
        singleJavaScriptFiles.put(MAP_ID_1, singleJavaScriptFile);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBeansOfType(SingleJavaScriptFile.class)).thenReturn(singleJavaScriptFiles);

        GroupScriptFile groupScriptFile = spy(new GroupScriptFile());
        doReturn(applicationContext).when(groupScriptFile).getApplicationContext();
        doNothing().when(groupScriptFile).setSrc(anyString());

        groupScriptFile.setGroup(ScriptGroup.LIVEEDIT_SCRIPT_FILE.name());

        verify(groupScriptFile, times(1)).setSrc(TEST_JS);
    }

}
