<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="asm" tagdir="/WEB-INF/tags/addons/assistedservicestorefront/asm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<div id="_asm">
    <script>
    	var body = document.getElementsByTagName("body")[0];
    	body.insertBefore(document.getElementById('_asm'), body.childNodes[0]);
    </script>
	<div class="ASM_header">
        <div class="container">
            <asm:redirect />
            <button class="ASM_close ASM_close_all closeBtn" aria-hidden="true" data-dismiss="alert" type="button">&times;</button>

            <c:if test="${not empty asm_message}">
                <div class="ASM_alert ${asm_alert_class}"><spring:theme code="${asm_message}"/></div>
            </c:if>

			<cms:component uid="ASMHeaderComponent" />

			<%-- logo text --%>
            <asm:logo />
            <c:choose>
                <c:when test="${empty agent.uid}">
                	<cms:component uid="ASMLoginComponent"/>
			    </c:when>
                <c:otherwise>
                    <div id="_asmLogged" class="ASM_loggedin">
                        
                        <div class="ASM_timer">
                            <span id="sessionTimer" class="ASM_loggedin_text_name"><span class="hidden-xs hidden-sm hidden-md">Session timeout: </span><span class='ASM_timer_count' id="timerCount"><script>document.getElementById('timerCount').innerHTML=Math.floor(${agentTimer}/60)+":00";</script></span> min</span>
                            <button type="submit" id="resetButton" class="ASM-btn ASM-btn-reset" disabled><spring:theme code="asm.emulate.reset"/></button>
                            <script>var timer=${agentTimer};</script>
                        </div>

                        <div class="ASM_loggedin_text">
                            <span class="hidden-xs hidden-sm hidden-md"><spring:theme code="asm.login.logged"/> </span>
                            <span class="ASM_loggedin_text_name">${agent.name}</span>
                        </div>

                        <c:url value="/assisted-service/logoutasm" var="logoutActionUrl" />
                        <form action="${logoutActionUrl}" method="post" id="asmLogoutForm" class="asmForm">
                            <fieldset>
                                <input type="hidden" name="CSRFToken" value="${CSRFToken}">
                                <button type="submit" class="ASM-btn ASM-btn-logout" disabled><spring:theme code="asm.logout"/></button>
                            </fieldset>
                        </form>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <c:if test="${not empty agent.uid}">
        <div class="ASM_session">
            <div class="container" id="_asmCustomer">
                <c:choose>
                    <c:when test="${empty emulatedUser}">
                    	<cms:component uid="ASMEmulateUserComponent"/>    
                    </c:when>

                    <c:otherwise>
						<cms:component uid="ASMBindComponent" />
						
                        <span class="ASM_end_session">
                            <c:url value="/assisted-service/personify-stop" var="sessionEndActionUrl" />
                            <form action="${sessionEndActionUrl}" method="post" id="_asmSessionEndForm" class="asmForm">
                                <fieldset>
                                    <button type="submit" id="stopEmulate" class="ASM-btn ASM-btn-end-session" disabled>
                                        <spring:theme code="asm.emulate.end"/><span class="hidden-xs hidden-sm hidden-md"><spring:theme code="asm.emulate.end.ending"/></span>
                                    </button>
                                </fieldset>
                            </form>
                        </span>
                    </c:otherwise>
                </c:choose>
                <c:if test="${not createDisabled}">
                	<asm:createcustomerform/>
                </c:if>
            </div>
        </div>
    </c:if>
    
    <cms:component uid="ASMFooterComponent" />
    
    <div id="asmAutoComplete" class="asmAutoComplete"></div>
    <div id="asmAutoCompleteCartId" class="asmAutoComplete"></div>
</div>