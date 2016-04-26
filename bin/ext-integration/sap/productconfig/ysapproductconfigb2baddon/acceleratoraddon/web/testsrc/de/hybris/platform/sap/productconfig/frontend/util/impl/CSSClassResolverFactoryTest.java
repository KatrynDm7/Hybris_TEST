/**
 * 
 */
package de.hybris.platform.sap.productconfig.frontend.util.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.frontend.util.CSSClassResolver;

@UnitTest
public class CSSClassResolverFactoryTest {

	private CSSClassResolverFactory classUnderTest;
	private CsticData cstic;
	private UiGroupData group;
	@Mock
	private CSSClassResolver resolver;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		classUnderTest = new CSSClassResolverFactory();
		CSSClassResolverFactory.setResolver(resolver);
		cstic = new CsticData();
		group = new UiGroupData();
		
		Mockito.when(resolver.getLabelStyleClass(cstic)).thenReturn(
				"labelStyle");
		Mockito.when(resolver.getValueStyleClass(cstic)).thenReturn(
				"valueStyle");
		Mockito.when(resolver.getGroupStyleClass(group)).thenReturn(
				"groupStyle");
	}

	@Test
	public void testGetLabelStyleClassForCstic() {
		final String labelStyleClassForCstic = classUnderTest
				.getLabelStyleClassForCstic(cstic);
		assertEquals("labelStyle", labelStyleClassForCstic);
	}

	@Test
	public void testGetValueStyleClassForCstic() {
		final String valueStyleClassForCstic = classUnderTest
				.getValueStyleClassForCstic(cstic);
		assertEquals("valueStyle", valueStyleClassForCstic);
	}

	@Test
	public void testGetStyleClassForGroup() {
		final String styleClassForGroup = classUnderTest
				.getStyleClassForGroup(group);
		assertEquals("groupStyle", styleClassForGroup);
	}
}
