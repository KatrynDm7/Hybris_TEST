/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.classification.daos;

import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;

import java.util.Collection;


/**
 * DAO for the {@link ClassificationSystemModel}.
 * 
 * @spring.bean classificationSystemDao
 */
public interface ClassificationSystemDao
{

	/**
	 * Finds all {@link ClassificationSystemModel}s with the given id.
	 * 
	 * @param id
	 *           ClassificationSystem id
	 * @return matching {@link ClassificationSystemModel}s
	 */
	Collection<ClassificationSystemModel> findSystemsById(String id);

}
