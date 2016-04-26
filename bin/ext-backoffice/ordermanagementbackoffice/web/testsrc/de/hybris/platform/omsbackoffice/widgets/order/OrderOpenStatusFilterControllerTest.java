package de.hybris.platform.omsbackoffice.widgets.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
//import de.hybris.platform.omsbackoffice.widgets.AbstractInitAdvanceSearchController;
//import de.hybris.platform.omsbackoffice.widgets.order.OrderOpenStatusFilterController;
import de.hybris.platform.enumeration.EnumerationService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AbstractInitAdvancedSearchAdapter;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchOperatorService;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionDataList;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldListType;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.testing.AbstractWidgetUnitTest;
import com.hybris.cockpitng.testing.annotation.DeclaredInput;


@DeclaredInput(value = OrderOpenStatusFilterController.SOCKET_IN_NODE_SELECTED, socketType = NavigationNode.class)
public class OrderOpenStatusFilterControllerTest extends AbstractWidgetUnitTest<OrderOpenStatusFilterController>
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
	@Mock
	protected EnumerationService enumerationService;

	OrderOpenStatusFilterController orderOpenStatusFilterController = new OrderOpenStatusFilterController();

	@Before
	public void setUpAbstractOrderWidgetUnitTest() throws TypeNotFoundException, CockpitConfigurationException
	{

		//final AbstractInitAdvanceSearchController controller = getWidgetController();
		final AbstractInitAdvancedSearchAdapter controller = (AbstractInitAdvancedSearchAdapter) getWidgetController();
		controller.setWidgetInstanceManager(widgetInstanceManager);
		controller.setTypeFacade(typeFacade);
		controller.setPermissionFacade(permissionFacade);
		controller.setAdvancedSearchOperatorService(searchOperatorService);
		orderOpenStatusFilterController.setEnumerationService(enumerationService);
		
		when(dataType.getCode()).thenReturn(OrderModel._TYPECODE);
		when(typeFacade.load(OrderModel._TYPECODE)).thenReturn(dataType);
		when(widgetInstanceManager.loadConfiguration(any(), any())).thenReturn(advanceSearch);
		when(advanceSearch.getFieldList()).thenReturn(new FieldListType());
		when(navigationNode.getId()).thenReturn(OrderOpenStatusFilterController.NAVIGATION_NODE_ID);
		when(enumerationService.getEnumerationValues(OrderStatus.class)).thenReturn((Arrays.asList(OrderStatus.CANCELLING,
				OrderStatus.CHECKED_VALID, OrderStatus.CHECKED_INVALID, OrderStatus.ON_VALIDATION, OrderStatus.SUSPENDED,
				OrderStatus.PAYMENT_AUTHORIZED, OrderStatus.CREATED, OrderStatus.PAYMENT_NOT_AUTHORIZED,
				OrderStatus.PAYMENT_AMOUNT_RESERVED, OrderStatus.PAYMENT_AMOUNT_NOT_RESERVED, OrderStatus.PAYMENT_CAPTURED,
				OrderStatus.PAYMENT_NOT_CAPTURED, OrderStatus.FRAUD_CHECKED, OrderStatus.ORDER_SPLIT, OrderStatus.PROCESSING_ERROR,
				OrderStatus.READY, OrderStatus.WAIT_FRAUD_MANUAL_CHECK, OrderStatus.CANCELLED, OrderStatus.COMPLETED)));
	}

	public void testInputNullNavigationNode(final String socketIn, final String socketOut)
	{
		executeInputSocketEvent(socketIn, (Object) null);
		assertNoSocketOutputInteractions(socketOut);
	}

	public void testInputNullSearchData(final String socketIn, final String socketOut) throws TypeNotFoundException
	{
		when(typeFacade.load(OrderModel._TYPECODE)).thenReturn(null);
		executeInputSocketEvent(socketIn, navigationNode);
		verify(widgetInstanceManager).sendOutput(eq(socketOut), any(AdvancedSearchInitContext.class));
	}


	@Test
	public void testAddOpenStatusSearchData()
	{
		executeInputSocketEvent(OrderOpenStatusFilterController.SOCKET_IN_NODE_SELECTED, navigationNode);
		
		assertNotNull(orderOpenStatusFilterController.getAdvancedSearchData());
		assertEquals(ValueComparisonOperator.AND, orderOpenStatusFilterController.getAdvancedSearchData().getGlobalOperator());
		assertEquals(OrderModel._TYPECODE, orderOpenStatusFilterController.getAdvancedSearchData().getTypeCode());

		final List<SearchConditionData> conditions = orderOpenStatusFilterController.getAdvancedSearchData().getConditions(OrderModel.STATUS);
		assertFalse(conditions.isEmpty());
		//assertEquals(1, conditions.size());
		
		final HashSet<OrderStatus> openOrderStatus = new HashSet<OrderStatus>(Arrays.asList(OrderStatus.CANCELLING,
				OrderStatus.CHECKED_VALID, OrderStatus.CHECKED_INVALID, OrderStatus.ON_VALIDATION, OrderStatus.SUSPENDED,
				OrderStatus.PAYMENT_AUTHORIZED, OrderStatus.CREATED, OrderStatus.PAYMENT_NOT_AUTHORIZED,
				OrderStatus.PAYMENT_AMOUNT_RESERVED, OrderStatus.PAYMENT_AMOUNT_NOT_RESERVED, OrderStatus.PAYMENT_CAPTURED,
				OrderStatus.PAYMENT_NOT_CAPTURED, OrderStatus.FRAUD_CHECKED, OrderStatus.ORDER_SPLIT, OrderStatus.PROCESSING_ERROR,
				OrderStatus.READY, OrderStatus.WAIT_FRAUD_MANUAL_CHECK));

		Assert.assertTrue(((SearchConditionDataList)((SearchConditionDataList) conditions.get(0)).getConditions().get(0)).getConditions().stream().allMatch(predicate -> openOrderStatus.contains(predicate.getValue())));

		verify(widgetInstanceManager).sendOutput(eq(OrderOpenStatusFilterController.SOCKET_OUT_CONTEXT),
				any(AdvancedSearchInitContext.class));
	}

	@Test
	public void testNullNavigationNode()
	{
		testInputNullNavigationNode(OrderOpenStatusFilterController.SOCKET_IN_NODE_SELECTED,
				OrderOpenStatusFilterController.SOCKET_OUT_CONTEXT);
		assertNull(orderOpenStatusFilterController.getAdvancedSearchData());
	}

	@Test
	public void testNullSearchData() throws TypeNotFoundException
	{
		testInputNullSearchData(OrderOpenStatusFilterController.SOCKET_IN_NODE_SELECTED,
				OrderOpenStatusFilterController.SOCKET_OUT_CONTEXT);
		assertNull(orderOpenStatusFilterController.getAdvancedSearchData());
	}

	@Override
	protected OrderOpenStatusFilterController getWidgetController()
	{
		return orderOpenStatusFilterController;
	}

}
