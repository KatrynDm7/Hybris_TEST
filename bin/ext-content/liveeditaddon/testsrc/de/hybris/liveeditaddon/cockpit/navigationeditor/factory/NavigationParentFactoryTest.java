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
package de.hybris.liveeditaddon.cockpit.navigationeditor.factory;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.NavigationParentElement;
import de.hybris.liveeditaddon.cockpit.services.NavigationEditorService;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarCollectionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by fcanteloup on 06/10/14.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class NavigationParentFactoryTest {

    @Mock
    private NavigationEditorService service;
    @Mock
    private LiveEditViewModel viewModel;
    private String barId = "barId";
    private String barCollectionId = "barCollectionId";
    private String slotUid = "dfasdfasdf0";

    @Mock
    private ContentSlotModel contentSlot;

    @Mock
    private NavigationBarComponentModel otherComponentModel;
    @Mock
    private NavigationBarComponentModel bar;
    @Mock
    private NavigationBarCollectionComponentModel barCollection;

    private List<AbstractCMSComponentModel> componentList1;
    private List<AbstractCMSComponentModel> componentList2;
    private List<NavigationBarComponentModel> componentList3;

    @Before
    public void setup(){
        when(service.getNavigationBarContentSlot(slotUid, viewModel)).thenReturn(contentSlot);
        when(service.getComponentForPreviewCatalogVersions(barId, viewModel)).thenReturn(bar);
        when(service.getComponentForPreviewCatalogVersions(barCollectionId, viewModel)).thenReturn(barCollection);
        componentList1 = Arrays.asList((AbstractCMSComponentModel) otherComponentModel, (AbstractCMSComponentModel) bar, (AbstractCMSComponentModel) barCollection);
        componentList2 = Arrays.asList((AbstractCMSComponentModel) otherComponentModel, (AbstractCMSComponentModel) barCollection);
        componentList3 = Arrays.asList(otherComponentModel, bar);
    }

    @Test
    public void will_return_SlotParentElement_enclosing_content_slot_when_component_is_NavigationBarComponent_direct_child_of_content_slot(){

        when(contentSlot.getCmsComponents()).thenReturn(componentList1);

        NavigationParentElement parentElement = NavigationParentFactory.build(service, barId, slotUid, viewModel);

        assertThat(parentElement.getElement(), is((CMSItemModel) contentSlot));
        assertThat(parentElement.getSlot(), is((CMSItemModel) contentSlot));

    }
    @Test
    public void will_return_ComponentParentElement_enclosing_content_slot_and_NavigationBarCollectionComponentModel_when_component_is_NavigationBarCollectionComponentModel_direct_child_of_content_slot(){

        when(contentSlot.getCmsComponents()).thenReturn(componentList1);
        NavigationParentElement parentElement = NavigationParentFactory.build(service, barCollectionId, slotUid, viewModel);

        assertThat(parentElement.getElement(), is((CMSItemModel) barCollection));
        assertThat(parentElement.getSlot(), is((CMSItemModel) contentSlot));
    }
    @Test
    public void will_return_ComponentParentElement_enclosing_content_slot_and_NavigationBarCollectionComponentModel_when_component_is_NavigationBarComponentModel_indirect_child_of_content_slot(){

        when(contentSlot.getCmsComponents()).thenReturn(componentList2);
        when(barCollection.getComponents()).thenReturn(componentList3);

        NavigationParentElement parentElement = NavigationParentFactory.build(service, barId, slotUid, viewModel);

        assertThat(parentElement.getElement(), is((CMSItemModel) barCollection));
        assertThat(parentElement.getSlot(), is((CMSItemModel) contentSlot));

    }

}
