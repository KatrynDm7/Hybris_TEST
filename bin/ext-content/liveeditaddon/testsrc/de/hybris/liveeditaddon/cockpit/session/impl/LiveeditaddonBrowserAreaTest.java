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
package de.hybris.liveeditaddon.cockpit.session.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.liveeditaddon.constants.LiveeditaddonConstants;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by fcanteloup on 26/09/14.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LiveeditaddonBrowserAreaTest {

    @Mock
    private TypedObject item;
    private String div = "div";
    @Mock
    private LiveEditView view;
    private String slotId = "slotId";
    @Mock
    private Component infoArea;

    @Test
    public void openInspectorInDiv_will(){

        LiveeditaddonBrowserArea liveeditaddonBrowserArea = spy(new LiveeditaddonBrowserArea());

        doReturn(infoArea).when(liveeditaddonBrowserArea).getInfoArea(div);
        doNothing().when(liveeditaddonBrowserArea).superOpenInspectorInArea(item, infoArea);

        liveeditaddonBrowserArea.openInspectorInDiv(item, div, view, slotId);

        verify(infoArea, times(1)).setAttribute(LiveeditaddonConstants.VIEW_ATTRIBUTE, view);
        verify(infoArea, times(1)).setAttribute(LiveeditaddonConstants.SLOT_ID, slotId);
        verify(liveeditaddonBrowserArea, times(1)).getInfoArea(div);
        verify(liveeditaddonBrowserArea, times(1)).superOpenInspectorInArea(item, infoArea);
    }

}
