package de.hybris.platform.warehousingbackoffice.actions.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousingbackoffice.actions.utils.VelocityRendererUtil;
import de.hybris.platform.warehousingbackoffice.context.CommonPrintLabelContext;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class VelocityRendererUtilTest
{
	private static final String TEST_TEMPLATE_CODE = "test-template";
	private static final String TEST_MESSAGE = "message de test";
	private static final String TEST_WIDTH = "222";
	private static final String TEST_HEIGH = "333";

	@Spy
	private final VelocityRendererUtil velocityRendererUtil = new VelocityRendererUtil();

	private CommonPrintLabelContext commonPrintLabelContext;

	@Mock
	private ConsignmentModel consignmentModel;

	@Mock
	private RendererService rendererService;

	@Mock
	private RendererTemplateModel rendererTemplateModel;

	private Map<String, String> localizedMap;

	@Before
	public void setUp()
	{
		commonPrintLabelContext = new CommonPrintLabelContext();

		localizedMap = new HashMap<>();
		localizedMap.put(VelocityRendererUtil.BLOCKED_POPUP_MESSAGE, TEST_MESSAGE);

		when(velocityRendererUtil.getRendererService()).thenReturn(rendererService);
		when(rendererService.getRendererTemplateForCode(TEST_TEMPLATE_CODE)).thenReturn(rendererTemplateModel);
		doNothing().when(rendererService).render(any(RendererTemplateModel.class), any(CommonPrintLabelContext.class),
				any(StringWriter.class));
	}

	@Test
	public void testGeneration()
	{
		velocityRendererUtil.generatePopupScript(TEST_TEMPLATE_CODE, commonPrintLabelContext, consignmentModel, localizedMap,
				TEST_WIDTH, TEST_HEIGH);

		assertEquals(TEST_MESSAGE, commonPrintLabelContext.getLabel(VelocityRendererUtil.BLOCKED_POPUP_MESSAGE));
		assertEquals(consignmentModel, commonPrintLabelContext.getConsignment());
		assertEquals(consignmentModel.getOrder(), commonPrintLabelContext.getOrder());

		verify(rendererService, times(1)).getRendererTemplateForCode(TEST_TEMPLATE_CODE);
	}

}
