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
package de.hybris.platform.b2bpunchoutaddon.security;

import de.hybris.platform.core.model.user.UserGroupModel;

import java.util.Collection;


/**
 * Defines the strategy to select user groups.
 */
public interface PunchOutUserGroupSelectionStrategy {

	/**
	 * Selects the user groups.
	 * 
	 * @param userId
	 * @return A collection of user groups related to this {@code userId}.
	 */
	Collection<UserGroupModel> select(String userId);
}
