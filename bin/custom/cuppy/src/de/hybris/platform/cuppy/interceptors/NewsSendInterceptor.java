package de.hybris.platform.cuppy.interceptors;

import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.services.MailService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author andreas.thaler
 * 
 */
public class NewsSendInterceptor implements ValidateInterceptor
{
	private MailService mailService;
	private PlayerService playerService;
	private ModelService modelService;

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (!(model instanceof NewsModel))
		{
			return;
		}
		final NewsModel news = (NewsModel) model;
		final boolean curValue = news.isEMail();
		if (curValue)
		{
			if (modelService.isNew(news))
			{
				sendNewsletter(news);
			}
			else
			{
				final ItemModelContextImpl context = (ItemModelContextImpl) ModelContextUtils.getItemModelContext(news);
				final Object oldValue = context.getValueHistory().getOriginalValue(NewsModel.EMAIL);
				if (oldValue == null || Boolean.FALSE.equals(oldValue))
				{
					sendNewsletter(news);
				}
			}
		}
	}

	private void sendNewsletter(final NewsModel news)
	{
		if (news.getCompetition() == null)
		{
			mailService.sendNewsletter(news, playerService.getAllPlayers());
		}
		else
		{
			mailService.sendNewsletter(news, playerService.getPlayers(news.getCompetition()));
		}
	}

	@Required
	public void setMailService(final MailService mailService)
	{
		this.mailService = mailService;
	}

	@Required
	public void setPlayerService(final PlayerService playerService)
	{
		this.playerService = playerService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
