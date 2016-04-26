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
package de.hybris.platform.commerceorgaddon.controllers;

import de.hybris.platform.commerceorgaddon.constants.CommerceorgaddonConstants;


/**
 * Class with constants for controllers.
 */
public interface ControllerConstants
{
    /**
     * Class with view name constants
     */
    interface Views
    {
        interface Pages
        {
            interface Error
            {
                String ErrorNotFoundPage = "pages/error/errorNotFoundPage";
            }

            interface MyCompany
            {
                String ADD_ON_PREFIX = "addon:";
                String VIEW_PAGE_PREFIX = ADD_ON_PREFIX + "/" + CommerceorgaddonConstants.EXTENSIONNAME + "/";

                String MyCompanyLoginPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyLoginPage";
                String MyCompanyManageUnitsPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUnitsPage";
                String MyCompanyManageUnitEditPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUnitEditPage";
                String MyCompanyManageUnitDetailsPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUnitDetailsPage";
                String MyCompanyManageUnitCreatePage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUnitCreatePage";
                String MyCompanyManageBudgetsPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageBudgetsPage";
                String MyCompanyManageBudgetsViewPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageBudgetsViewPage";
                String MyCompanyManageBudgetsEditPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageBudgetsEditPage";
                String MyCompanyManageBudgetsAddPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageBudgetsAddPage";
                String MyCompanyManageCostCentersPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageCostCentersPage";
                String MyCompanyCostCenterViewPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyCostCenterViewPage";
                String MyCompanyCostCenterEditPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyCostCenterEditPage";
                String MyCompanyAddCostCenterPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyAddCostCenterPage";
                String MyCompanyManagePermissionsPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManagePermissionsPage";
                String MyCompanyManageUnitUserListPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUnitUserListPage";
                String MyCompanyManageUnitApproverListPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUnitApproversListPage";
                String MyCompanyManageUserDetailPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUserDetailPage";
                String MyCompanyManageUserAddEditFormPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUserAddEditFormPage";
                String MyCompanyManageUsersPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUsersPage";
                String MyCompanyManageUserDisbaleConfirmPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUserDisableConfirmPage";
                String MyCompanyManageUnitDisablePage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUnitDisablePage";
                String MyCompanySelectBudgetPage = VIEW_PAGE_PREFIX + "pages/company/myCompanySelectBudgetsPage";
                String MyCompanyCostCenterDisableConfirm = VIEW_PAGE_PREFIX + "pages/company/myCompanyDisableCostCenterConfirmPage";
                String MyCompanyManageUnitAddAddressPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUnitAddAddressPage";
                String MyCompanyManageUserPermissionsPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUserPermissionsPage";
                String MyCompanyManageUserResetPasswordPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUserPassword";
                String MyCompanyBudgetDisableConfirm = VIEW_PAGE_PREFIX + "pages/company/myCompanyDisableBudgetConfirmPage";
                String MyCompanyManageUserGroupsPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUserGroupsPage";
                String MyCompanyManageUsergroupViewPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUsergroupViewPage";
                String MyCompanyManageUsergroupEditPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUsergroupEditPage";
                String MyCompanyManageUsergroupCreatePage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUsergroupCreatePage";
                String MyCompanyManageUsergroupDisableConfirmationPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUsergroupDisableConfirmationPage";
                String MyCompanyManagePermissionDisablePage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManagePermissionDisablePage";
                String MyCompanyManagePermissionsViewPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManagePermissionsViewPage";
                String MyCompanyManagePermissionsEditPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManagePermissionsEditPage";
                String MyCompanyManagePermissionTypeSelectPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManagePermissionTypeSelectPage";
                String MyCompanyManagePermissionAddPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManagePermissionAddPage";
                String MyCompanyManageUserCustomersPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUserCustomersPage";
                String MyCompanyManageUserGroupPermissionsPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUserGroupPermissionsPage";
                String MyCompanyManageUserGroupMembersPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUserGroupMembersPage";
                String MyCompanyRemoveDisableConfirmationPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyRemoveDisableConfirmationPage";
                String MyCompanyManageUserB2BUserGroupsPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUserB2BUserGroupsPage";
                String MyCompanyManageUsergroupRemoveConfirmationPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUsergroupRemoveConfirmationPage";
            }
        }
    }
}
