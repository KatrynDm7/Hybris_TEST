<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="account-section-header">
	<spring:theme code="text.account.addressBook" text="Address Book"/>
</div>
<div class="account-section-content	 account-section-content-small">
	<div class="account-addressbook">
		<c:if test="${empty addressData}">
			<spring:theme code="text.account.addressBook.noSavedAddresses" text="No Saved Addresses Found"/>
		</c:if>
		
		<ycommerce:testId code="addressBook_addNewAddress_button">
			<div class="container">
				<div class="col-xs-12 col-sm-6 col-md-5 accountAddAddress">
					<a href="add-address" class="btn btn-primary btn-block"><spring:theme code="text.account.addressBook.addAddress" text="Add New Address"/></a>
				</div>
			</div>
		</ycommerce:testId>
		
		<c:if test="${not empty addressData}">
			<div class="account-addressbook-list container">
				<c:forEach items="${addressData}" var="address">
					<div class="col-sm-12 col-md-6 col-lg-4 accountAddressItem">
						<div class="remove">
							<ycommerce:testId code="addressBook_removeAddress_button">
								<a class="btn btn-default removeAddressFromBookButton" data-address-id="${address.id}" data-popup-title="<spring:theme code="text.address.delete.popup.title" text="Delete Address"/>">
									<span class="glyphicon glyphicon-trash"></span>
								</a>
							</ycommerce:testId>										
						</div>
					 	<strong>${fn:escapeXml(address.title)}&nbsp;${fn:escapeXml(address.firstName)}&nbsp;${fn:escapeXml(address.lastName)}
					 	<c:if test="${address.defaultAddress}">
					 		(<spring:theme code="text.default" text="Default" />)
					 	</c:if>
					 	</strong><br>${fn:escapeXml(address.line1)}
						<c:if test="${not empty fn:escapeXml(address.line2)}">
							<br>
							${fn:escapeXml(address.line2)}
						</c:if>
						<br>${fn:escapeXml(address.town)}&nbsp;${fn:escapeXml(address.region.name)}
						<br> ${fn:escapeXml(address.country.name)}&nbsp;${fn:escapeXml(address.postalCode)}
                        <br>${fn:escapeXml(address.phone)}

						<div class="actions">
							<c:if test="${not address.defaultAddress}">
								<ycommerce:testId code="addressBook_isDefault_button">
									<a class="btn btn-default" href="set-default-address/${address.id}">
										<spring:theme code="text.setDefault" text="Set as Default"/>
									</a>
								</ycommerce:testId>
							</c:if>
							
							<ycommerce:testId code="addressBook_editAddress_button">
								<a class="btn btn-default" href="edit-address/${address.id}">
									<spring:theme code="text.edit" text="Edit"/>
								</a>
							</ycommerce:testId>
						</div>
					</div>
					<div style="display:none">
						<div id="popup_confirm_address_removal_${address.id}">
							<div class="addressItem">
								<spring:theme code="text.address.remove.following" text="The following address will be deleted from your Address Book"/>
									<br>
										<strong>
											${fn:escapeXml(address.title)}&nbsp;
											${fn:escapeXml(address.firstName)}&nbsp;
											${fn:escapeXml(address.lastName)}
										</strong>
										<br>
										${fn:escapeXml(address.line1)}&nbsp;
										${fn:escapeXml(address.line2)}
										<br>
										${fn:escapeXml(address.town)}&nbsp;
										<c:if test="${not empty address.region.name }">
											${fn:escapeXml(address.region.name)}&nbsp;
										</c:if>
										<br>
										${fn:escapeXml(address.country.name)}&nbsp;
										${fn:escapeXml(address.postalCode)}
                                        <br/>
                                        ${fn:escapeXml(address.phone)}
								<div class="buttons">
									<a class="btn btn-default closeColorBox" data-address-id="${address.id}">
										<spring:theme code="text.button." text="Cancel"/>
									</a>
									
									<ycommerce:testId code="addressRemove_delete_button">
										<a class="btn btn-default btn-primary" data-address-id="${address.id}" href="remove-address/${address.id}">
											<spring:theme code="text.address.delete" text="Delete"/>
										</a>
									</ycommerce:testId>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</c:if>
	</div>
</div>