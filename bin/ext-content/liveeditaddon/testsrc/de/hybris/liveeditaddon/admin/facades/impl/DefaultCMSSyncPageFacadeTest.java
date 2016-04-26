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
package de.hybris.liveeditaddon.admin.facades.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.liveeditaddon.admin.facades.SyncResponse;
import de.hybris.liveeditaddon.admin.facades.SynchronisationStatus;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;

import static de.hybris.liveeditaddon.admin.facades.SynchronisationStatus.*;
import static de.hybris.platform.cockpit.services.sync.SynchronizationService.SyncContext;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSSyncPageFacadeTest {

    @Mock
    private CMSPreviewService cmsPreviewService;
    @Mock
    private CatalogVersionService catalogVersionService;
    private MockSessionService mockSessionService = spy(new MockSessionService());
    @Mock
    private SynchronizationService synchronizationService;
    @Mock
    private TypeService cockpitTypeService;
    @Mock
    private UserService userService;
    @Mock
    private CMSPageService cmsPageService;


    private DefaultCMSSyncPageFacade cmsSyncPageFacade;


    //data
    private String previewTicket = "previewTicketId";
    private String pageUid = "pageUid";
    private SynchronisationStatus syncStatus = SynchronisationStatus.SYNCHRONIZATION_OK;
    private boolean isSynchronized = true;
    @Mock
    private CMSPreviewTicketModel ticket;
    @Mock
    private PreviewDataModel previewDataModel;
    @Mock
    private Collection<CatalogVersionModel> sessionCatalogVersions;
    @Mock
    private AbstractPageModel pageModel;
    @Mock
    private TypedObject page;
    @Mock
    private EmployeeModel adminUser;
    @Mock
    private SyncContext syncContext;
    @Captor
    private ArgumentCaptor<List> collectionCaptor;

    @Before
    public void setUp() {
        cmsSyncPageFacade = spy(new DefaultCMSSyncPageFacade());
        cmsSyncPageFacade.setCmsPreviewService(cmsPreviewService);
        cmsSyncPageFacade.setCatalogVersionService(catalogVersionService);
        cmsSyncPageFacade.setSessionService(mockSessionService);
        cmsSyncPageFacade.setSynchronizationService(synchronizationService);
        cmsSyncPageFacade.setCockpitTypeService(cockpitTypeService);
        cmsSyncPageFacade.setUserService(userService);
        cmsSyncPageFacade.setCmsPageService(cmsPageService);

        when(ticket.getPreviewData()).thenReturn(previewDataModel);
        when(previewDataModel.getCatalogVersions()).thenReturn(sessionCatalogVersions);
        when(userService.getAdminUser()).thenReturn(adminUser);

    }

    @Test(expected = PreviewTicketInvalidException.class)
    public void invalid_ticket_will_raise_PreviewTicketInvalidException() throws PreviewTicketInvalidException, CMSItemNotFoundException {

        when(cmsPreviewService.getPreviewTicket(previewTicket)).thenReturn(null);
        cmsSyncPageFacade.getPageSynchronizationStatus(previewTicket, pageUid);

    }

    @Test(expected = CMSItemNotFoundException.class)
    public void page_not_found_will_raise_CMSItemNotFoundException() throws PreviewTicketInvalidException, CMSItemNotFoundException {

        when(cmsPreviewService.getPreviewTicket(previewTicket)).thenReturn(ticket);
        when(cmsPageService.getPageForId(pageUid)).thenThrow(new CMSItemNotFoundException(""));
        cmsSyncPageFacade.getPageSynchronizationStatus(previewTicket, pageUid);

    }

    @Test
    public void valid_ticket_and_pageUid_will_return_expected_page() throws PreviewTicketInvalidException, CMSItemNotFoundException {

        when(cmsPreviewService.getPreviewTicket(previewTicket)).thenReturn(ticket);
        when(cmsPageService.getPageForId(pageUid)).thenReturn(pageModel);
        when(cockpitTypeService.wrapItem(pageModel)).thenReturn(page);
        when(synchronizationService.getSyncContext(page)).thenReturn(syncContext);
        when(syncContext.isProductSynchronized()).thenReturn(syncStatus.getStatusValue());

        assertThat(cmsSyncPageFacade.getPageSynchronizationStatus(previewTicket, pageUid), is(syncStatus));

        verify(catalogVersionService, times(1)).setSessionCatalogVersions(sessionCatalogVersions);
        verify(userService, times(1)).setCurrentUser(adminUser);
        verify(mockSessionService, times(1)).executeInLocalView(any(SessionExecutionBody.class));
    }

    public void isPageSynchronized_returns_false_if_other_status_than_sync_ok() throws PreviewTicketInvalidException, CMSItemNotFoundException {

        doReturn(SYNCHRONIZATION_NOT_AVAILABLE).when(cmsSyncPageFacade).getPageSynchronizationStatus(previewTicket, pageUid);
        assertThat(cmsSyncPageFacade.isPageSynchronized(previewTicket, pageUid), is(false));

    }

    public void isPageSynchronized_returns_true_if__status_sync_ok() throws PreviewTicketInvalidException, CMSItemNotFoundException {

        doReturn(SYNCHRONIZATION_OK).when(cmsSyncPageFacade).getPageSynchronizationStatus(previewTicket, pageUid);
        assertThat(cmsSyncPageFacade.isPageSynchronized(previewTicket, pageUid), is(true));

    }


    @Test
    public void when_sync_status_is_SYNCHRONIZATION_NOT_AVAILABLE_synchronizePage_only_returns_a_warning() throws PreviewTicketInvalidException, CMSItemNotFoundException {

        doReturn(SYNCHRONIZATION_NOT_AVAILABLE).when(cmsSyncPageFacade).getPageSynchronizationStatus(previewTicket, pageUid);

        SyncResponse syncResponse = cmsSyncPageFacade.synchronizePage(previewTicket, pageUid);
        assertThat(syncResponse, notNullValue());
        assertThat(syncResponse.getSynchronisationStatus(), is(SYNCHRONIZATION_NOT_AVAILABLE));
        assertThat(syncResponse.getMessage1(), is("dialog.synchronizationNotPossible.message"));
        assertThat(syncResponse.getMessage2(), is("dialog.synchronizationNotPerformed.title"));
        verifyZeroInteractions(synchronizationService);
    }

    @Test
    public void when_sync_status_is_SYNCHRONIZATION_OK_synchronizePage_only_returns_a_warning() throws PreviewTicketInvalidException, CMSItemNotFoundException {

        doReturn(SYNCHRONIZATION_OK).when(cmsSyncPageFacade).getPageSynchronizationStatus(previewTicket, pageUid);

        SyncResponse syncResponse = cmsSyncPageFacade.synchronizePage(previewTicket, pageUid);
        assertThat(syncResponse, notNullValue());
        assertThat(syncResponse.getSynchronisationStatus(), is(SYNCHRONIZATION_OK));
        assertThat(syncResponse.getMessage1(), is("dialog.synchronizationNotRequired.message"));
        assertThat(syncResponse.getMessage2(), is("dialog.synchronizationNotPerformed.title"));
        verifyZeroInteractions(synchronizationService);
    }

    @Test
    public void when_sync_status_is_INITIAL_SYNC_IS_NEEDED_successful_synchronization_returns_info() throws PreviewTicketInvalidException, CMSItemNotFoundException {

        doReturn(INITIAL_SYNC_IS_NEEDED).doReturn(SYNCHRONIZATION_OK).when(cmsSyncPageFacade).getPageSynchronizationStatus(previewTicket, pageUid);
        //doReturn(pageModel).when(cmsSyncPageFacade).getPage(previewTicket, pageUid);
        when(cmsPreviewService.getPreviewTicket(previewTicket)).thenReturn(ticket);
        when(cmsPageService.getPageForId(pageUid)).thenReturn(pageModel);

        SyncResponse syncResponse = cmsSyncPageFacade.synchronizePage(previewTicket, pageUid);

        assertThat(syncResponse, notNullValue());
        assertThat(syncResponse.getSynchronisationStatus(), is(SYNCHRONIZATION_OK));
        assertThat(syncResponse.getMessage1(), is("dialog.synchronized.message"));
        assertThat(syncResponse.getMessage2(), is("dialog.synchronized.message"));

        verify(catalogVersionService, times(1)).setSessionCatalogVersions(sessionCatalogVersions);
        verify(userService, times(1)).setCurrentUser(adminUser);
        verify(synchronizationService, times(1)).performSynchronization(collectionCaptor.capture(), eq((List) null), eq((CatalogVersionModel) null), eq((String) null));
        List list = collectionCaptor.getValue();
        assertThat(list.size(), is(1));
        assertThat((AbstractPageModel) list.get(0), is(pageModel));
    }

    @Test
    public void when_sync_status_is_SYNCHRONIZATION_OK_successful_synchronization_returns_info() throws PreviewTicketInvalidException, CMSItemNotFoundException {

        doReturn(SYNCHRONIZATION_NOT_OK).doReturn(SYNCHRONIZATION_OK).when(cmsSyncPageFacade).getPageSynchronizationStatus(previewTicket, pageUid);
        //doReturn(pageModel).when(cmsSyncPageFacade).getPage(previewTicket, pageUid);
        when(cmsPreviewService.getPreviewTicket(previewTicket)).thenReturn(ticket);
        when(cmsPageService.getPageForId(pageUid)).thenReturn(pageModel);

        SyncResponse syncResponse = cmsSyncPageFacade.synchronizePage(previewTicket, pageUid);

        assertThat(syncResponse, notNullValue());
        assertThat(syncResponse.getSynchronisationStatus(), is(SYNCHRONIZATION_OK));
        assertThat(syncResponse.getMessage1(), is("dialog.synchronized.message"));
        assertThat(syncResponse.getMessage2(), is("dialog.synchronized.message"));

        verify(catalogVersionService, times(1)).setSessionCatalogVersions(sessionCatalogVersions);
        verify(userService, times(1)).setCurrentUser(adminUser);
        verify(synchronizationService, times(1)).performSynchronization(collectionCaptor.capture(), eq((List) null), eq((CatalogVersionModel) null), eq((String) null));
        List list = collectionCaptor.getValue();
        assertThat(list.size(), is(1));
        assertThat((AbstractPageModel) list.get(0), is(pageModel));
    }

}
