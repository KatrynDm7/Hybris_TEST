package de.hybris.platform.warehousingbackoffice.widgets.consignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.DeliveryModeService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousingbackoffice.widgets.consignment.AbstractConsignmentFilterController;
import de.hybris.platform.warehousingbackoffice.widgets.consignment.ConsignmentForPickupFilterController;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionDataList;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.testing.annotation.DeclaredInput;


@DeclaredInput(value = AbstractConsignmentFilterController.SOCKET_IN_NODE_SELECTED, socketType = NavigationNode.class)
public class ConsignmentForPickupWidgetTest extends AbstractConsignmentWidgetTest<ConsignmentForPickupFilterController>
{
	@Mock
	private DeliveryModeModel pickup;
	
	@Mock
	private DeliveryModeService deliveryModeService;

	private final ConsignmentForPickupFilterController controller = new ConsignmentForPickupFilterController();

	@Before
	public void setUp() throws CockpitConfigurationException, TypeNotFoundException
	{
		setUpController();
		controller.setDeliveryModeService(deliveryModeService);

		when(navigationNode.getId()).thenReturn(ConsignmentForPickupFilterController.NAVIGATION_NODE_ID);
		when(deliveryModeService.getDeliveryModeForCode(ConsignmentForPickupFilterController.PICKUP_CODE)).thenReturn(pickup);
	}

	@Test
	/**
	 * Checking the following filter: (DeliveryMode=pickup) AND (Status=ready OR Status=ready_for_pickup)
	 */
	public void testAddPickupSearchDataWithCompoundConditions()
	{
		executeInputSocketEvent(AbstractConsignmentFilterController.SOCKET_IN_NODE_SELECTED, navigationNode);

		assertNotNull(controller.getAdvancedSearchData());
		assertEquals(ConsignmentModel._TYPECODE, controller.getAdvancedSearchData().getTypeCode());

		final List<SearchConditionData> conditions = controller.getAdvancedSearchData().getConditions(ConsignmentModel.DELIVERYMODE);
		assertFalse(conditions.isEmpty());
		assertEquals(2,((SearchConditionDataList)conditions.get(0)).getConditions().size());
		
		//check the type of the fields: DeliveryMode and Status should be selected
		final String firstField = ((SearchConditionDataList) conditions.get(0)).getConditions().get(0).getFieldType().getName();
		final String secondField = ((SearchConditionDataList)((SearchConditionDataList) conditions.get(0)).getConditions().get(1)).getConditions().get(0).getFieldType().getName();
		assertEquals(ConsignmentModel.DELIVERYMODE, firstField);
		assertEquals(ConsignmentModel.STATUS, secondField);
		
		//check the values (DekiveryMode = pickup and STATUS = READY, READY_FOR_PICKUP)
		final String firstFieldValue = ((SearchConditionDataList) conditions.get(0)).getConditions().get(0).getValue().toString();
		final String secondFieldValue1 = ((SearchConditionDataList)((SearchConditionDataList) conditions.get(0)).getConditions().get(1)).getConditions().get(0).getValue().toString();
		final String secondFieldValue2 = ((SearchConditionDataList)((SearchConditionDataList) conditions.get(0)).getConditions().get(1)).getConditions().get(1).getValue().toString();
		assertEquals("pickup",firstFieldValue);
		assertEquals("READY",secondFieldValue1);
		assertEquals("READY_FOR_PICKUP",secondFieldValue2);
		
		verify(widgetInstanceManager).sendOutput(eq(AbstractConsignmentFilterController.SOCKET_OUT_CONTEXT), any(AdvancedSearchInitContext.class));
	}

	@Test
	public void testNullNavigationNode()
	{
		testInputNullNavigationNode(AbstractConsignmentFilterController.SOCKET_IN_NODE_SELECTED,
				AbstractConsignmentFilterController.SOCKET_OUT_CONTEXT);
		assertNull(controller.getAdvancedSearchData());
		verify(deliveryModeService, never()).getDeliveryModeForCode(any(String.class));
	}

	@Test
	public void testNullSearchData() throws TypeNotFoundException
	{
		testInputNullSearchData(AbstractConsignmentFilterController.SOCKET_IN_NODE_SELECTED,
				AbstractConsignmentFilterController.SOCKET_OUT_CONTEXT);
		assertNull(controller.getAdvancedSearchData());
	}

	@Override
	protected ConsignmentForPickupFilterController getWidgetController()
	{
		return controller;
	}
}
