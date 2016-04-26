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
package de.hybris.liveeditaddon.cockpit.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.NavigationParentElement;
import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.impl.ComponentParentElement;
import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.impl.SlotParentElement;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationNodeViewModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarCollectionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by fcanteloup on 06/10/14.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultNavigationEditorServiceTest {

    @Mock
    private ModelService modelService;
    @Mock
    private CMSAdminSiteService cmsAdminSiteService;

    private DefaultNavigationEditorService navigationEditorService;
    @Mock
    private ContentSlotModel contentSlotModel;
    @Mock
    private NavigationBarCollectionComponentModel wrapper;
    @Mock
    private NavigationBarComponentModel initialComponent;
    @Mock
    private NavigationBarComponentModel component;
    @Mock
    private CatalogVersionModel catalogVersion;
    @Mock
    private NavigationNodeViewModel node;
    @Mock
    private Collection<TypedObject> typedObjects;
    @Captor
    private ArgumentCaptor<List<AbstractCMSComponentModel>> slotComponentsCaptor;
    @Captor
    private ArgumentCaptor<List<NavigationBarComponentModel>> wrapperComponentsCaptor;

    private List<AbstractCMSComponentModel> slotChildren;
    private List<NavigationBarComponentModel> wrapperChildren;

    private NavigationParentElement parentElement;

    @Before
    public void setUp(){

        slotChildren = Arrays.asList((AbstractCMSComponentModel) initialComponent);
        wrapperChildren = Arrays.asList(initialComponent);

        navigationEditorService = spy(new DefaultNavigationEditorService());
        navigationEditorService.setModelService(modelService);
        navigationEditorService.setCmsAdminSiteService(cmsAdminSiteService);
    }

    @Test
    public void component_will_be_added_to_ContentSlotModel_if_parent_is__ContentSlotModel(){

        NavigationParentElement parentElement = new SlotParentElement(contentSlotModel);

        when(modelService.create("NavigationBarComponent")).thenReturn(component);
        when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);
        doNothing().when(navigationEditorService).populateCMSNavBarFromModel(any(NavigationNodeViewModel.class), any(NavigationBarComponentModel.class), any(Collection.class));
        when(contentSlotModel.getCmsComponents()).thenReturn(slotChildren);

        assertThat(navigationEditorService.createNavigationBar(node, typedObjects, parentElement), is(component));

        verify(contentSlotModel, times(1)).setCmsComponents(slotComponentsCaptor.capture());
        List<AbstractCMSComponentModel> finalChildren = slotComponentsCaptor.getValue();

        assertThat(finalChildren.size(), is(2));
        assertThat((NavigationBarComponentModel)finalChildren.get(0), is(initialComponent));
        assertThat((NavigationBarComponentModel)finalChildren.get(1), is(component));
        verifyZeroInteractions(wrapper);

    }

    @Test
    public void component_will_be_added_to_NavigationBarCollectionComponent_if_parent_is_NavigationBarCollectionComponent(){

        NavigationParentElement parentElement = new ComponentParentElement(wrapper, contentSlotModel);

        when(modelService.create("NavigationBarComponent")).thenReturn(component);
        when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);
        doNothing().when(navigationEditorService).populateCMSNavBarFromModel(any(NavigationNodeViewModel.class), any(NavigationBarComponentModel.class), any(Collection.class));
        when(wrapper.getComponents()).thenReturn(wrapperChildren);

        assertThat(navigationEditorService.createNavigationBar(node, typedObjects, parentElement), is(component));

        verify(wrapper, times(1)).setComponents(wrapperComponentsCaptor.capture());
        List<NavigationBarComponentModel> finalChildren = wrapperComponentsCaptor.getValue();

        assertThat(finalChildren.size(), is(2));
        assertThat(finalChildren.get(0), is(initialComponent));
        assertThat(finalChildren.get(1), is(component));
        verifyZeroInteractions(contentSlotModel);

    }


}
