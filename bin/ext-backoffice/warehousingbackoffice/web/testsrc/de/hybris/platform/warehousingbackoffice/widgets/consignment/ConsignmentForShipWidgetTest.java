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

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.DeliveryModeService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousingbackoffice.widgets.consignment.ConsignmentForShipFilterController;

import java.util.Arrays;
import java.util.Iterator;
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


@DeclaredInput(value = ConsignmentForShipFilterController.SOCKET_IN_NODE_SELECTED, socketType = NavigationNode.class)
public class ConsignmentForShipWidgetTest extends AbstractConsignmentWidgetTest<ConsignmentForShipFilterController>
{
	@Mock
	private DeliveryModeModel freeStandardShipping;
	@Mock
	private DeliveryModeModel premiumGross;
	@Mock
	private DeliveryModeModel standardNet;
	@Mock
	private DeliveryModeModel pickup;
	@Mock
	private DeliveryModeService deliveryModeService;

	private final ConsignmentForShipFilterController controller = new ConsignmentForShipFilterController();

	@Before
	public void setUp() throws CockpitConfigurationException, TypeNotFoundException
	{
		setUpController();
		controller.setDeliveryModeService(deliveryModeService);

		when(navigationNode.getId()).thenReturn(ConsignmentForShipFilterController.NAVIGATION_NODE_ID);
		when(deliveryModeService.getAllDeliveryModes()).thenReturn(
				Arrays.asList(freeStandardShipping, premiumGross, standardNet, pickup));

		when(freeStandardShipping.getCode()).thenReturn("freeStandardShipping");
		when(premiumGross.getCode()).thenReturn("premiumGross");
		when(standardNet.getCode()).thenReturn("standardNet");
		when(pickup.getCode()).thenReturn(ConsignmentForShipFilterController.PICKUP_CODE);
	}
	
	@Test
	/**
	 * the filter: (STATUS=Ready OR STATUS=ready_for_shipping) AND (DeliveryMoede=freeStandardShipping OR DeliveryMoede=premiumGross OR DeliveryMoede=standardNet OR DeliveryMoede=pickup)
	 */
	public void testAddShippingSearchDataWithCompoundConditions()
	{
		executeInputSocketEvent(ConsignmentForShipFilterController.SOCKET_IN_NODE_SELECTED, navigationNode);

		assertNotNull(controller.getAdvancedSearchData());
		assertEquals(ConsignmentModel._TYPECODE, controller.getAdvancedSearchData().getTypeCode());

		final List<SearchConditionData> conditions = controller.getAdvancedSearchData().getConditions(ConsignmentModel.DELIVERYMODE);
		assertFalse(conditions.isEmpty());
		assertEquals(1, conditions.size());

		SearchConditionData condition = conditions.get(0); 
		assertEquals(ValueComparisonOperator.AND, condition.getOperator());
		
		SearchConditionData innerCondition = ((SearchConditionDataList) conditions.get(0)).getConditions().get(0);
		final String firstField =  ((SearchConditionDataList) innerCondition).getConditions().get(0).getFieldType().getName();
		assertEquals(ConsignmentModel.DELIVERYMODE, firstField);
		assertEquals(ValueComparisonOperator.OR, innerCondition.getOperator());
		List<SearchConditionData> deliverModevalues = ((SearchConditionDataList) innerCondition).getConditions();
		assertEquals(freeStandardShipping,deliverModevalues.get(0).getValue());
		assertEquals(premiumGross,deliverModevalues.get(1).getValue());
		assertEquals(standardNet,deliverModevalues.get(2).getValue());
		//make sure there is no pickup in delivery mode
		for(SearchConditionData e : deliverModevalues)
			assertFalse(e.getValue().equals(pickup));
		
		//Move to the next inner condition
		innerCondition = ((SearchConditionDataList) conditions.get(0)).getConditions().get(1);
		final String secondField = ((SearchConditionDataList) innerCondition).getConditions().get(1).getFieldType().getName();
		assertEquals(ConsignmentModel.STATUS, secondField);
		assertEquals(ValueComparisonOperator.OR, innerCondition.getOperator());
		assertEquals(ConsignmentStatus.READY,((SearchConditionDataList) innerCondition).getConditions().get(0).getValue());
		assertEquals(ConsignmentStatus.READY_FOR_SHIPPING,((SearchConditionDataList) innerCondition).getConditions().get(1).getValue());

		verify(widgetInstanceManager).sendOutput(eq(ConsignmentForShipFilterController.SOCKET_OUT_CONTEXT),
				any(AdvancedSearchInitContext.class));
	}

	
	@Test
	public void testNullNavigationNode()
	{
		testInputNullNavigationNode(ConsignmentForShipFilterController.SOCKET_IN_NODE_SELECTED,
				ConsignmentForShipFilterController.SOCKET_OUT_CONTEXT);
		assertNull(controller.getAdvancedSearchData());
		verify(deliveryModeService, never()).getDeliveryModeForCode(any(String.class));
	}

	@Test
	public void testNullSearchData() throws TypeNotFoundException
	{
		testInputNullSearchData(ConsignmentForShipFilterController.SOCKET_IN_NODE_SELECTED,
				ConsignmentForShipFilterController.SOCKET_OUT_CONTEXT);
		assertNull(controller.getAdvancedSearchData());
	}

	@Override
	protected ConsignmentForShipFilterController getWidgetController()
	{
		return controller;
	}
}