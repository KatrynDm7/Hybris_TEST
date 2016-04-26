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
package de.hybris.platform.test;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.EJBTools;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TypeInfoTest extends HybrisJUnit4TransactionalTest
{
	ComposedTypeRemote employeeType;

	@Before
	public void setUp() throws Exception
	{
		employeeType = SystemEJB.getInstance().getTypeManager().getComposedType(Constants.TYPES.Employee);
	}

	@Test
	public void testEmployeeTypeInfoMap()
	{
		final TypeInfoMap tim = Registry.getPersistenceManager().getPersistenceInfo(EJBTools.getPK(employeeType));
		final Collection attributeDescriptors = SystemEJB.getInstance().getTypeManager()
				.getAttributeDescriptors(employeeType, false);
		final Iterator iter = attributeDescriptors.iterator();
		while (iter.hasNext())
		{
			final AttributeDescriptorRemote attribute = (AttributeDescriptorRemote) iter.next();
			if (attribute.isProperty())
			{
				final int expectedPropertyType = attribute.isLocalized() ? TypeInfoMap.LOCALIZED : TypeInfoMap.UNLOCALIZED;
				final int propertyType = tim.getPropertyType(attribute.getQualifier());
				assertEquals("wrong property type for " + attribute.getEnclosingType().getCode() + "." + attribute.getQualifier(),
						expectedPropertyType, propertyType);
			}
		}
	}
}
