/**
 *
 */
package de.hybris.platform.sap.ysapordermgmtb2baddon.checkout.steps.validation;

import de.hybris.platform.acceleratorfacades.flow.CheckoutFlowFacade;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.b2bacceleratoraddon.checkout.steps.validation.AbstractB2BCheckoutStepValidator;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public class SapB2BPaymentMethodCheckoutValidator extends AbstractB2BCheckoutStepValidator
{

	private CheckoutFlowFacade sapB2BCheckoutFlowFacade;

	/**
	 * @return the sapB2BCheckoutFlowFacade
	 */
	public CheckoutFlowFacade getSapB2BCheckoutFlowFacade()
	{
		return sapB2BCheckoutFlowFacade;
	}

	/**
	 * @param sapB2BCheckoutFlowFacade
	 *           the sapB2BCheckoutFlowFacade to set
	 */
	@Required
	public void setSapB2BCheckoutFlowFacade(final CheckoutFlowFacade sapB2BCheckoutFlowFacade)
	{
		this.sapB2BCheckoutFlowFacade = sapB2BCheckoutFlowFacade;
	}

	@Override
	protected ValidationResults doValidateOnEnter(final RedirectAttributes redirectAttributes)
	{
		final B2BPaymentTypeData checkoutPaymentType = getCheckoutFacade().getCheckoutCart().getPaymentType();

		if (checkoutPaymentType == null)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
					"checkout.multi.paymentType.notprovided");
			return ValidationResults.REDIRECT_TO_PAYMENT_TYPE;
		}

		if (CheckoutPaymentType.ACCOUNT.getCode().equalsIgnoreCase(checkoutPaymentType.getCode())
				&& getCheckoutFacade().getCheckoutCart().getCostCenter() == null)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
					"checkout.multi.costCenter.notprovided");
			return ValidationResults.REDIRECT_TO_PAYMENT_TYPE;
		}

		if (getSapB2BCheckoutFlowFacade().hasNoDeliveryAddress())
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
					"checkout.multi.deliveryAddress.notprovided");
			return ValidationResults.REDIRECT_TO_DELIVERY_ADDRESS;
		}

		if (getSapB2BCheckoutFlowFacade().hasNoDeliveryMode())
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
					"checkout.multi.deliveryMethod.notprovided");
			return ValidationResults.REDIRECT_TO_DELIVERY_METHOD;
		}

		// skip payment method step for account payment
		if (CheckoutPaymentType.ACCOUNT.getCode().equalsIgnoreCase(checkoutPaymentType.getCode()))
		{
			return ValidationResults.REDIRECT_TO_SUMMARY;
		}

		return ValidationResults.SUCCESS;
	}
}