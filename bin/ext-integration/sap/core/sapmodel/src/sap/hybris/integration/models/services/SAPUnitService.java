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
package sap.hybris.integration.models.services;

import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.UnitService;

public interface SAPUnitService extends UnitService
{



public UnitModel getUnitForSAPCode(final String code);

}
