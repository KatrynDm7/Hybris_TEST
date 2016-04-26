package de.hybris.platform.warehousing.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.warehousing.model.PackagingInfoModel;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ConsignmentPackagingInfoPrepareInterceptorTest
{
	@InjectMocks
	private final ConsignmentPackagingInfoPrepareInterceptor interceptor = new ConsignmentPackagingInfoPrepareInterceptor();
	@Mock
	private ModelService modelService;
	@Mock
	private TimeService timeService;
	@Mock
	private ConsignmentModel consignment;
	@Mock
	private InterceptorContext context;
	private PackagingInfoModel packagingInfo;

	@Before
	public void setUp()
	{
		packagingInfo = new PackagingInfoModel();

		doNothing().when(consignment).setPackagingInfo(packagingInfo);
		doNothing().when(context).registerElementFor(packagingInfo, PersistenceOperation.SAVE);
		when(modelService.create(PackagingInfoModel.class)).thenReturn(packagingInfo);
		when(timeService.getCurrentTime()).thenReturn(new Date());
	}

	@Test
	public void shouldInitPackagingInfo() throws InterceptorException
	{
		when(context.isNew(consignment)).thenReturn(Boolean.TRUE);

		interceptor.onPrepare(consignment, context);

		assertEquals(consignment, packagingInfo.getConsignment());
		assertEquals(ConsignmentPackagingInfoPrepareInterceptor.DEFAULT_VALUE, packagingInfo.getGrossWeight());
		assertEquals(ConsignmentPackagingInfoPrepareInterceptor.DEFAULT_VALUE, packagingInfo.getHeight());
		assertEquals(ConsignmentPackagingInfoPrepareInterceptor.DEFAULT_VALUE, packagingInfo.getInsuredValue());
		assertEquals(ConsignmentPackagingInfoPrepareInterceptor.DEFAULT_VALUE, packagingInfo.getLength());
		assertEquals(ConsignmentPackagingInfoPrepareInterceptor.DEFAULT_VALUE, packagingInfo.getWidth());
		assertEquals(ConsignmentPackagingInfoPrepareInterceptor.DEFAULT_DIMENSION_UNIT, packagingInfo.getDimensionUnit());
		assertEquals(ConsignmentPackagingInfoPrepareInterceptor.DEFAULT_WEIGHT_UNIT, packagingInfo.getWeightUnit());
		assertTrue(packagingInfo.getCreationtime().compareTo(new Date(System.currentTimeMillis())) < 1);
		assertTrue(packagingInfo.getModifiedtime().compareTo(new Date(System.currentTimeMillis())) < 1);

		verify(context).registerElementFor(packagingInfo, PersistenceOperation.SAVE);
	}

	@Test
	public void shouldNotInitPackagingInfo() throws InterceptorException
	{
		when(context.isNew(consignment)).thenReturn(Boolean.FALSE);

		interceptor.onPrepare(consignment, context);

		assertNull(packagingInfo.getConsignment());
		assertNull(packagingInfo.getGrossWeight());
		assertNull(packagingInfo.getHeight());
		assertNull(packagingInfo.getInsuredValue());
		assertNull(packagingInfo.getLength());
		assertNull(packagingInfo.getWidth());
		assertNull(packagingInfo.getDimensionUnit());
		assertNull(packagingInfo.getWeightUnit());
		assertNull(packagingInfo.getCreationtime());
		assertNull(packagingInfo.getModifiedtime());

		verify(context, never()).registerElementFor(packagingInfo, PersistenceOperation.SAVE);
		verify(consignment, never()).setPackagingInfo(packagingInfo);
	}

}
