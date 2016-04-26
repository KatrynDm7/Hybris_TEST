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
package de.hybris.platform.sap.core.bol.businessobject;

import de.hybris.platform.sap.core.bol.backend.BackendBusinessObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Defines the type of the backend interface which belongs to the business object.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.TYPE })
public @interface BackendInterface
{

	/**
	 * Interface the specific {@link BackendBusinessObject} implements.
	 */
	Class<? extends BackendBusinessObject> value();

}
