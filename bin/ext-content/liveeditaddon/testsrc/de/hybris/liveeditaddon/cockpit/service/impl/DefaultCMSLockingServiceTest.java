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
package de.hybris.liveeditaddon.cockpit.service.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.components.liveedit.impl.DefaultLiveEditViewModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.MockSessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zul.Messagebox;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by fcanteloup on 26/09/14.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSLockingServiceTest {

    //mocked dependencies
    @Mock
    private ModelService modelService;
    @Mock
    private CatalogVersionService catalogVersionService;
    private MockSessionService sessionService = spy(new MockSessionService());
    @Mock
    private CMSContentSlotService cmsContentSlotService;
    @Mock
    private CMSAdminContentSlotService cmsAdminContentSlotService;

    private DefaultCMSLockingService cmsLockingService;

    //mocked model and data
    @Mock
    private Collection<CatalogVersionModel> sessionCatalogVersions;
    @Mock
    private ContentSlotModel contentSlotModel;
    @Mock
    private CatalogVersionModel catalogVersionModel;
    @Mock
    private PreviewDataModel previewDataModel;
    @Mock
    private DefaultLiveEditViewModel model;
    @Mock
    private LiveEditView view;


    @Before
    public void setUp() {
        cmsLockingService = spy(new DefaultCMSLockingService(modelService, catalogVersionService, sessionService, cmsContentSlotService, cmsAdminContentSlotService));
        when(view.getModel()).thenReturn(model);
        when(model.getCurrentPreviewData()).thenReturn(previewDataModel);
        when(previewDataModel.getCatalogVersions()).thenReturn(sessionCatalogVersions);

    }

    @Test
    public void protected_getContentSlotForPreviewCatalogVersions_will_return_contentslotModel_when_given_slot_id_and_view() {
        String slotId = "1233";

        when(cmsContentSlotService.getContentSlotForId(slotId)).thenReturn(contentSlotModel);
        assertThat(cmsLockingService.getContentSlotForPreviewCatalogVersions(slotId, view), is(contentSlotModel));
        verify(catalogVersionService, times(1)).setSessionCatalogVersions(sessionCatalogVersions);
    }

    @Test
    public void isSectionLocked_will_return_false_if_slot_id_empty() throws InterruptedException {

        assertThat(cmsLockingService.isSectionLocked(view, null), is(false));

        verifyZeroInteractions(sessionService);
        verifyZeroInteractions(catalogVersionService);
        verifyZeroInteractions(cmsContentSlotService);
        verifyZeroInteractions(modelService);
        verify(cmsLockingService, never()).showMessageBox(any(ContentSlotModel.class));

    }

    @Test
    public void isSectionLocked_will_return_false_if_slot_not_lockable() throws InterruptedException {

        String slotId = "NavigationBar";

        doReturn(contentSlotModel).when(cmsLockingService).getContentSlotForPreviewCatalogVersions(slotId, view);
        when(cmsAdminContentSlotService.hasRelations(contentSlotModel)).thenReturn(true);

        assertThat(cmsLockingService.isSectionLocked(view, slotId), is(false));

        verify(cmsLockingService, times(1)).getContentSlotForPreviewCatalogVersions(slotId, view);
        verify(cmsLockingService, never()).showMessageBox(any(ContentSlotModel.class));
        verifyZeroInteractions(modelService);
    }

    @Test
    public void isSectionLocked_will_return_false_if_lockable_and_not_locked() throws InterruptedException {

        String slotId = "NavigationBar";

        doReturn(contentSlotModel).when(cmsLockingService).getContentSlotForPreviewCatalogVersions(slotId, view);
        when(cmsAdminContentSlotService.hasRelations(contentSlotModel)).thenReturn(false);
        when(contentSlotModel.getLocked()).thenReturn(false);

        assertThat(cmsLockingService.isSectionLocked(view, slotId), is(false));

        verify(cmsLockingService, times(1)).getContentSlotForPreviewCatalogVersions(slotId, view);
        verify(cmsLockingService, never()).showMessageBox(any(ContentSlotModel.class));
        verifyZeroInteractions(modelService);
    }

    @Test
    public void isSectionLocked_will_return_true_if_lockable_and_locked_and_user_chooses_not_to_unlock() throws InterruptedException {

        String slotId = "NavigationBar";

        doReturn(contentSlotModel).when(cmsLockingService).getContentSlotForPreviewCatalogVersions(slotId, view);
        when(cmsAdminContentSlotService.hasRelations(contentSlotModel)).thenReturn(false);
        when(contentSlotModel.getLocked()).thenReturn(true);
        doReturn(Messagebox.NO).when(cmsLockingService).showMessageBox(contentSlotModel);

        assertThat(cmsLockingService.isSectionLocked(view, slotId), is(true));

        verify(cmsLockingService, times(1)).getContentSlotForPreviewCatalogVersions(slotId, view);
        verify(cmsLockingService, times(1)).showMessageBox(any(ContentSlotModel.class));
        verify(cmsLockingService, times(1)).showMessageBox(contentSlotModel);
        verifyZeroInteractions(modelService);
    }

    @Test
    public void isSectionLocked_will_return_false_if_lockable_and_locked_and_user_chooses_to_unlock() throws InterruptedException {

        String slotId = "NavigationBar";

        doReturn(contentSlotModel).when(cmsLockingService).getContentSlotForPreviewCatalogVersions(slotId, view);
        when(cmsAdminContentSlotService.hasRelations(contentSlotModel)).thenReturn(false);
        when(contentSlotModel.getLocked()).thenReturn(true);
        doReturn(Messagebox.OK).when(cmsLockingService).showMessageBox(contentSlotModel);

        assertThat(cmsLockingService.isSectionLocked(view, slotId), is(false));

        verify(cmsLockingService, times(1)).getContentSlotForPreviewCatalogVersions(slotId, view);
        verify(cmsLockingService, times(1)).showMessageBox(any(ContentSlotModel.class));
        verify(cmsLockingService, times(1)).showMessageBox(contentSlotModel);
        verify(modelService, times(1)).save(contentSlotModel);
    }

}
