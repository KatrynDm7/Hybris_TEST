<%@include file="../../head.inc"%>
<%@page import="de.hybris.platform.print.hmc.attribute.TranslatorConfigurationPreviewChip"%>
<%@page import="de.hybris.platform.hmc.webchips.AbstractChip"%>
<%--
  ~ [y] hybris Platform
  ~
  ~ Copyright (c) 2000-2015 hybris AG
  ~ All rights reserved.
  ~
  ~ This software is the confidential and proprietary information of hybris
  ~ ("Confidential Information"). You shall not disclose such Confidential
  ~ Information and shall use it only in accordance with the terms of the
  ~ license agreement you entered into with hybris.
  --%>

<%
	final TranslatorConfigurationPreviewChip theChip = (TranslatorConfigurationPreviewChip) request.getAttribute(AbstractChip.CHIP_KEY);
	//final boolean canChange = theChip.canChange();
	boolean active = theChip.isConfigured();
%>
		<table class="passwordEditorChip">
	<tr>
		<td class="arrowButton">&nbsp;</td>
		<td style="overflow: hidden; width: 140px;">
 			<div style="overflow: hidden; width: 140px;">
 				<%= localized("testconfiguration.rawinputdata.section")%>
 			</div>
 		</td>

 		<td>
 			<%
 			theChip.getRenderedInput().render(pageContext);
 			%>
 		</td>
 	</tr>
 	<tr>
 	<td>
			<div class="xp-button<%= active ? " chip-event" : "-disabled" %>" >
				<a style="width:100%;" href="#" title="<%= localized("shortcut.testconfiguration.generate") %>"
					name="<%= theChip.getCommandID(theChip.GENERATE) %>" hidefocus="true" id="<%= theChip.getUniqueName() %>_a">
					<span class="label" id="<%= theChip.getUniqueName() %>_span">
						<%= localized("shortcut.testconfiguration.generate") %>
					</span>
				</a>
			</div>
		</td>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
 		<td class="arrowButton">&nbsp;</td>
 		<td style="overflow: hidden; width: 140px;">
 			<div style="overflow: hidden; width: 140px;">
 				<%=localized("testconfiguration.generatedtagged.section")%>
 			</div>
 		</td>
 		<td>
 			<%
 			theChip.getRenderedOutput().render(pageContext);
 			%>
 		</td>
	</table>

