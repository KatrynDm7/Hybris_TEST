package de.hybris.platform.cuppy.interceptors;

import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.PlayerPreferencesModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.LoadInterceptor;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * @author andreas.thaler
 * 
 */
public class PlayerPreferencesInterceptor implements LoadInterceptor, PrepareInterceptor
{
	private ModelService modelService;

	@Override
	public void onLoad(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (!(model instanceof PlayerModel))
		{
			return;
		}
		final PlayerModel player = (PlayerModel) model;
		if (player.getPreferences() == null)
		{
			final PlayerPreferencesModel prefs = new PlayerPreferencesModel();
			player.setPreferences(prefs);
			modelService.save(player);
		}
	}

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (!(model instanceof PlayerModel))
		{
			return;
		}
		final PlayerModel player = (PlayerModel) model;
		if (ctx.isNew(player))
		{
			final PlayerPreferencesModel prefs = new PlayerPreferencesModel();
			player.setPreferences(prefs);
		}
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
