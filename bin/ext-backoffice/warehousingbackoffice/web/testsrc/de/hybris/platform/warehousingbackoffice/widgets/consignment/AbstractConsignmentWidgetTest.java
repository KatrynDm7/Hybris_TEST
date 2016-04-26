package de.hybris.platform.warehousingbackoffice.widgets.consignment;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.backoffice.widgets.advancedsearch.AbstractInitAdvancedSearchAdapter;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import org.junit.Before;
import org.mockito.Mock;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchOperatorService;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldListType;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.testing.AbstractWidgetUnitTest;

public abstract class AbstractConsignmentWidgetTest<T> extends AbstractWidgetUnitTest<T>
{
	@Mock
	protected DataType dataType;
	@Mock
	protected AdvancedSearch advanceSearch;
	@Mock
	protected NavigationNode navigationNode;
	@Mock
	protected TypeFacade typeFacade;
	@Mock
	protected PermissionFacade permissionFacade;
	@Mock
	protected AdvancedSearchOperatorService searchOperatorService;

	@Override
	protected abstract T getWidgetController();

	@Before
	public void setUpAbstractConsignmentWidgetUnitTest() throws TypeNotFoundException, CockpitConfigurationException
	{
		when(dataType.getCode()).thenReturn(ConsignmentModel._TYPECODE);
		when(typeFacade.load(ConsignmentModel._TYPECODE)).thenReturn(dataType);
		when(widgetInstanceManager.loadConfiguration(any(), any())).thenReturn(advanceSearch);
		when(advanceSearch.getFieldList()).thenReturn(new FieldListType());
	}

	public void setUpController()
	{
		final AbstractInitAdvancedSearchAdapter controller = (AbstractInitAdvancedSearchAdapter) getWidgetController();
		controller.setWidgetInstanceManager(widgetInstanceManager);
		controller.setTypeFacade(typeFacade);
		controller.setPermissionFacade(permissionFacade);
		controller.setAdvancedSearchOperatorService(searchOperatorService);
	}

	public void testInputNullNavigationNode(final String socketIn, final String socketOut)
	{
		executeInputSocketEvent(socketIn, (Object) null);
		assertNoSocketOutputInteractions(socketOut);
	}

	public void testInputNullSearchData(final String socketIn, final String socketOut) throws TypeNotFoundException
	{
		when(typeFacade.load(ConsignmentModel._TYPECODE)).thenReturn(null);
		executeInputSocketEvent(socketIn, navigationNode);
		verify(widgetInstanceManager).sendOutput(eq(socketOut), any(AdvancedSearchInitContext.class));
	}
}
