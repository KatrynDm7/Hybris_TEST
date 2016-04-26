<%@page import="de.hybris.platform.print.hmc.attribute.ContentBlockPreviewChip"%>
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

<% ContentBlockPreviewChip theChip = (ContentBlockPreviewChip) request.getAttribute( AbstractChip.CHIP_KEY ); %>

<table class="passwordEditorChip">
	<tr>
		<td class="arrowButton">&nbsp;</td>
 		<td class="attrLabel" style="color:silver;">Preview:</td>
 		<td>
 			<div style="border:0px; padding-top:5x;">
	 			<textarea id="<%= theChip.getID() %>_textarea" style="border:0px;"><%= theChip.getHTMLPreview() %></textarea>
				<iframe style="width:<%= theChip.getWidth() %>px; height:<%= theChip.getHeight() %>px;" class="disablediframe" id="<%= theChip.getID() %>_iframe" src="js/empty_iframe.html"></iframe>
				<script type="text/javascript">
					y_initIFrame($('<%= theChip.getID() %>_textarea'), $('<%= theChip.getID() %>_iframe'));
				</script>
			</div>
 		</td>
</table>
