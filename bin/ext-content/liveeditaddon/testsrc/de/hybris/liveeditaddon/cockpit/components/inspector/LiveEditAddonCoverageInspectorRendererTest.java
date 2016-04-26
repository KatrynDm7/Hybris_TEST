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
package de.hybris.liveeditaddon.cockpit.components.inspector;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.liveeditaddon.cockpit.service.CMSLockingService;
import de.hybris.liveeditaddon.constants.LiveeditaddonConstants;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.LayoutRegion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created by fcanteloup on 26/09/14.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LiveEditAddonCoverageInspectorRendererTest {

    //mocked dependencies
    @Mock
    private CMSLockingService cmsLockingService;

    private LiveEditAddonCoverageInspectorRenderer renderer;


    //mocked model and data
    @Mock
    private Iterator iterator;
    @Mock
    private Component editBtnCnt;
    @Mock
    private Component parent;
    @Mock
    private Component headerDiv;
    @Mock
    private Component toolbarDiv;
    @Mock
    private TypedObject object;
    @Captor
    private ArgumentCaptor<EventListener> eventListenerCaptor;
    @Mock
    private BaseUICockpitPerspective baseUICockpitPerspective;
    @Mock
    private UICockpitPerspective perspective;
    @Mock
    private LayoutRegion layoutRegion;
    @Mock
    private Event event;
    @Mock
    private LiveEditView view;

    private String slotId = "12345";

    ;

    @Before
    public void setUp() {
        renderer = spy(new LiveEditAddonCoverageInspectorRenderer(cmsLockingService));
        when(baseUICockpitPerspective.getEditorAreaComponent()).thenReturn(layoutRegion);
        doNothing().when(renderer).superPrepareEditActionButton(parent, object);
        doNothing().when(renderer).superRenderer(parent, object);
        doNothing().when(renderer).echoEvent(editBtnCnt);
        when(parent.getAttribute(LiveeditaddonConstants.VIEW_ATTRIBUTE)).thenReturn(view);
        when(parent.getAttribute(LiveeditaddonConstants.SLOT_ID)).thenReturn(slotId);

        when(parent.getLastChild()).thenReturn(editBtnCnt);
        when(editBtnCnt.getListenerIterator(Events.ON_CLICK)).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, false);
    }

    @Test
    public void render_will_invoke_super_then_copy_attributes_of_parent_into_toolbarDiv() {

        String attr1 = "attr1";
        String attr1Value = "attr1Value";
        String attr2 = "attr2";
        String attr2Value = "attr2Value";

        when(parent.getFirstChild()).thenReturn(headerDiv);
        when(headerDiv.getLastChild()).thenReturn(toolbarDiv);
        Map<Object, Object> attributeMap = new HashMap<>();
        attributeMap.put(attr1, attr1Value);
        attributeMap.put(attr2, attr2Value);
        when(parent.getAttributes()).thenReturn(attributeMap);

        renderer.render(parent, object);

        verify(toolbarDiv, times(2)).setAttribute(anyString(), anyString());
        verify(toolbarDiv, times(1)).setAttribute(attr1, attr1Value);
        verify(toolbarDiv, times(1)).setAttribute(attr2, attr2Value);
        verify(renderer, times(1)).superRenderer(parent, object);
    }

    @Test
    public void prepareEditActionButton_will_remove_onclick_event_listeners_from_editBtnCnt_and_add_a_new_one() {

        renderer.prepareEditActionButton(parent, object);

        verify(iterator, times(3)).hasNext();
        verify(iterator, times(2)).next();
        verify(iterator, times(2)).remove();
        verify(editBtnCnt, times(1)).addEventListener(eq(Events.ON_CLICK), any(EventListener.class));
        verify(renderer, times(1)).superPrepareEditActionButton(parent, object);

    }

    @Test
    public void newly_add_event_listener_will_not_do_anything_if_perspective_is_not_BaseUICockpitPerspective() throws Exception {

        doReturn(perspective).when(renderer).getCurrentPerspective();

        renderer.prepareEditActionButton(parent, object);

        verify(editBtnCnt, times(1)).addEventListener(eq(Events.ON_CLICK), eventListenerCaptor.capture());


        EventListener eventListener = eventListenerCaptor.getValue();


        eventListener.onEvent(event);

        verifyZeroInteractions(cmsLockingService);
        verify(renderer, never()).echoEvent(any(Component.class));
    }

    @Test
    public void newly_add_event_listener_will_not_echo_event_if_perspective_is_BaseUICockpitPerspective_and_slot_locked() throws Exception {

        doReturn(baseUICockpitPerspective).when(renderer).getCurrentPerspective();

        when(cmsLockingService.isSectionLocked(view, slotId)).thenReturn(true);

        renderer.prepareEditActionButton(parent, object);

        verify(editBtnCnt, times(1)).addEventListener(eq(Events.ON_CLICK), eventListenerCaptor.capture());


        EventListener eventListener = eventListenerCaptor.getValue();


        eventListener.onEvent(event);


        verify(cmsLockingService, times(1)).isSectionLocked(view, slotId);
        verify(renderer, never()).echoEvent(editBtnCnt);
    }

    @Test
    public void newly_add_event_listener_will_not_echo_event_if_perspective_is_BaseUICockpitPerspective_and_slot_not_locked_and_editorAreaComponent_is_open_and_item_is_active_item() throws Exception {

        doReturn(baseUICockpitPerspective).when(renderer).getCurrentPerspective();

        when(baseUICockpitPerspective.getActiveItem()).thenReturn(object);
        when(layoutRegion.isOpen()).thenReturn(true);

        when(cmsLockingService.isSectionLocked(view, slotId)).thenReturn(false);

        renderer.prepareEditActionButton(parent, object);

        verify(editBtnCnt, times(1)).addEventListener(eq(Events.ON_CLICK), eventListenerCaptor.capture());


        EventListener eventListener = eventListenerCaptor.getValue();


        eventListener.onEvent(event);


        verify(cmsLockingService, times(1)).isSectionLocked(view, slotId);
        verify(renderer, never()).echoEvent(editBtnCnt);
    }

    @Test
    public void newly_add_event_listener_will_echo_event_if_perspective_is_BaseUICockpitPerspective_and_slot_not_locked_and_editorAreaComponent_is_not_open() throws Exception {

        doReturn(baseUICockpitPerspective).when(renderer).getCurrentPerspective();

        when(layoutRegion.isOpen()).thenReturn(false);
        when(cmsLockingService.isSectionLocked(view, slotId)).thenReturn(false);


        renderer.prepareEditActionButton(parent, object);

        verify(editBtnCnt, times(1)).addEventListener(eq(Events.ON_CLICK), eventListenerCaptor.capture());


        EventListener eventListener = eventListenerCaptor.getValue();


        eventListener.onEvent(event);


        verify(cmsLockingService, times(1)).isSectionLocked(view, slotId);
        verify(renderer, times(1)).echoEvent(editBtnCnt);
    }

    @Test
    public void newly_add_event_listener_will_echo_event_if_perspective_is_BaseUICockpitPerspective_and_slot_not_locked_and_item_is_active_item() throws Exception {

        doReturn(baseUICockpitPerspective).when(renderer).getCurrentPerspective();

        when(baseUICockpitPerspective.getActiveItem()).thenReturn(object);

        when(cmsLockingService.isSectionLocked(view, slotId)).thenReturn(false);


        renderer.prepareEditActionButton(parent, object);

        verify(editBtnCnt, times(1)).addEventListener(eq(Events.ON_CLICK), eventListenerCaptor.capture());


        EventListener eventListener = eventListenerCaptor.getValue();


        eventListener.onEvent(event);


        verify(cmsLockingService, times(1)).isSectionLocked(view, slotId);
        verify(renderer, times(1)).echoEvent(editBtnCnt);
    }


}
