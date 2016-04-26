<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/common/footer" %>
       

<div class="footer">
<cms:pageSlot position="Footer" var="feature" element="div">
	<cms:component component="${feature}"/>
</cms:pageSlot>
  
</div>
