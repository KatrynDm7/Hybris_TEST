package de.hybris.platform.cuppy.interceptors;

import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.MailService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;


/**
 * @author andreas.thaler
 * 
 */
public class PlayerConfirmedInterceptor implements ValidateInterceptor
{
	private MailService mailService;

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (!(model instanceof PlayerModel))
		{
			return;
		}
		final PlayerModel player = (PlayerModel) model;
		final boolean curValue = player.isConfirmed();
		if (curValue && !ctx.isNew(player))
		{
			final ItemModelContextImpl context = (ItemModelContextImpl) ModelContextUtils.getItemModelContext(player);
			final ModelValueHistory valueHistory = context.getValueHistory();
			boolean sendEmail = false;
			if (valueHistory.getLoadedAttributes().contains(PlayerModel.CONFIRMED))
			{
				sendEmail = Boolean.FALSE.equals(valueHistory.getOriginalValue(PlayerModel.CONFIRMED));
			}
			else
			{
				sendEmail = valueHistory.isDirty(PlayerModel.CONFIRMED);
			}
			if (sendEmail)
			{
				mailService.sendConfirmationMail(player);
			}
		}
	}

	public void setMailService(final MailService mailService)
	{
		this.mailService = mailService;
	}

}
