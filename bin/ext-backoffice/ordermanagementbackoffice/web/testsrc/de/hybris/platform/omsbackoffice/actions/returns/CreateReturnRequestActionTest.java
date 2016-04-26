/**
 *
 */
package de.hybris.platform.omsbackoffice.actions.returns;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.actions.ActionContext;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CreateReturnRequestActionTest
{
	private final CreateReturnRequestAction action = new CreateReturnRequestAction();

	@Mock
	private ActionContext context;
	@Mock
	private OrderModel order;
	@Mock
	private OrderEntryModel entry;

	@Before
	public void setUp()
	{
		when(context.getData()).thenReturn(order);
		when(order.getEntries()).thenReturn(Arrays.asList(entry));
	}

	@Test
	public void returnFalse_notInstanceOfOrderModel()
	{
		when(context.getData()).thenReturn("test");

		final boolean result = action.canPerform(context);
		assertFalse(result);
	}

	@Test
	public void returnFalse_nullOrder()
	{
		when(context.getData()).thenReturn(null);

		final boolean result = action.canPerform(context);
		assertFalse(result);
	}

	@Test
	public void returnFalse_nullEntries()
	{
		when(order.getEntries()).thenReturn(null);

		final boolean result = action.canPerform(context);
		assertFalse(result);
	}

	@Test
	public void returnFalse_emptyEntries()
	{
		when(order.getEntries()).thenReturn(Collections.emptyList());

		final boolean result = action.canPerform(context);
		assertFalse(result);
	}

	@Test
	public void returnFalse_nullQuantityShipped()
	{
		when(entry.getQuantityShipped()).thenReturn(null);

		final boolean result = action.canPerform(context);
		assertFalse(result);
	}

	@Test
	public void returnFalse_noQuantityShipped()
	{
		when(entry.getQuantityShipped()).thenReturn(0L);

		final boolean result = action.canPerform(context);
		assertFalse(result);
	}

	@Test
	public void returnTrue_hasQuantityShipped()
	{
		when(entry.getQuantityShipped()).thenReturn(5L);

		final boolean result = action.canPerform(context);
		assertTrue(result);
	}

}
