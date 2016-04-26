<%@page import="java.util.*,de.hybris.platform.print.jalo.*,de.hybris.platform.print.hmc.attribute.*,de.hybris.platform.util.localization.Localization"%>
<%@include file="../../head.inc"%>

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
	PreviewContextDisplayChip theChip = (PreviewContextDisplayChip) request.getAttribute(AbstractChip.CHIP_KEY);

	if( theChip.getPreviewMedia() != null )
	{
%>
<img src="<%= theChip.getPreviewMedia().getURL()%>"/>
<%
	}

	if( theChip.getPlaceholderTemplate() != null )
	{
%>
<table>
	<tr>
		<td><b><%= Localization.getLocalizedString("print.placeholder.context.qualifier") %></b></td>
		<td><b><%= Localization.getLocalizedString("print.placeholder.description") %></b></td>
	</tr>
<%
		for( Placeholder placeholder : theChip.getPlaceholders() )
		{
%>
	<tr>
		<td><%= placeholder.getEncodedQualifier() %></td>
		<td><% if(placeholder.getDescription() !=null){%>
		<%= placeholder.getDescription()%>
		<%}else
			if(placeholder instanceof AttributePlaceholder){%>
			[<%=((AttributePlaceholder) placeholder).getAttributeQualifierPath() %> ]
			<%}%>

		</td>
	</tr>

<%
		}
%>
</table>
<%
	}
%>
