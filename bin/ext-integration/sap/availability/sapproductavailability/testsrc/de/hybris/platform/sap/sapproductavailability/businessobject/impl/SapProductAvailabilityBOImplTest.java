package de.hybris.platform.sap.sapproductavailability.businessobject.impl;

import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;





import de.hybris.platform.sap.core.jco.connection.JCoConnection;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.sapproductavailability.unittests.base.SapProductAvailabilityBolSpringJunitTest;

public class SapProductAvailabilityBOImplTest extends SapProductAvailabilityBolSpringJunitTest{
	
	
	SapProductAvailabilityBOImpl classUnderTest = null;
	
	JCoConnection connection = null;
	
	@Before
	public void init() throws BackendException
	{
		connection = EasyMock.createMock(JCoConnection.class);
	}
	
	
	@Test
	public void  testReadProductAvailability(){
		
		assertNotNull(connection);
		
		
		
	}

}
