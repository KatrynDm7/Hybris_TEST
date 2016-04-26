/**
 * 
 */
package de.hybris.platform.cuppy.web.converters;

import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.cuppy.web.data.NewsData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "newsConverter")
public class NewsConverter extends GenericCollectionConverter<NewsModel, NewsData>
{
	@Autowired
	private SessionService sessionService;
	@Autowired
	private I18NService i18nService;

	@Override
	public NewsData convert(final NewsModel model, final NewsData data) throws ConversionException
	{
		super.convert(model, data);
		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				i18nService.setLocalizationFallbackEnabled(true);

				data.setCreationTime(model.getCreationtime());
				if (model.getCompetition() == null)
				{
					data.setCompetitionName("Administration");
				}
				else
				{
					data.setCompetitionName(model.getCompetition().getName());
				}
				data.setContent(model.getContent());
			}
		});
		return data;
	}

	@Override
	protected NewsData createDestObject() throws ConversionException
	{
		return new NewsData();
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}
}
