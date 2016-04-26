/**
 *
 */
package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.commons.renderer.exceptions.RendererException;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.MailService;
import de.hybris.platform.cuppy.services.RankingData;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "mailService")
public class DefaultMailService implements MailService, InitializingBean
{
	private final static Logger LOG = Logger.getLogger(DefaultMailService.class);
	private String domain;
	private String fromAddress;
	private String replyToAddress;

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private I18NService i18nService;
	@Autowired
	private FormatFactory formatFactory;
	@Autowired
	private RendererService rendererService;
	@Autowired
	private L10NService l10NService;

	@Override
	public void afterPropertiesSet()
	{
		domain = Config.getParameter("cuppy.domain");
		fromAddress = Config.getParameter("mail.from");
		replyToAddress = Config.getParameter("mail.replyto");

		if (domain == null || domain.isEmpty() || fromAddress == null || fromAddress.isEmpty() || replyToAddress == null
				|| replyToAddress.isEmpty())
		{
			throw new IllegalStateException(
					"Can not start mail service, please configure properties 'cuppy.domain','mail.from' and 'mail.replyto'");
		}
	}

	@Override
	public void sendConfirmationMail(final PlayerModel player)
	{
		final MailPreparator preparer = new MailPreparator()
		{
			@Override
			public void prepare(final MimeMessageHelper message) throws MessagingException
			{
				message.setSubject(l10NService.getLocalizedString("mail.confirmation.subject"));
				message.setText(l10NService.getLocalizedString("mail.confirmation.body", new Object[]
				{ player.getName(), "http://" + domain + "/index.zul", player.getUid() }));
			}
		};
		send(preparer, player);
	}

	@Override
	public void sendRegistrationMail(final PlayerModel player, final List<PlayerModel> admins)
	{
		for (final PlayerModel admin : admins)
		{
			final MailPreparator preparer = new MailPreparator()
			{
				@Override
				public void prepare(final MimeMessageHelper message) throws MessagingException
				{
					message.setSubject(l10NService.getLocalizedString("mail.registration.subject", new Object[]
					{ player.getUid() }));
					message.setText(l10NService.getLocalizedString("mail.registration.body", new Object[]
					{
							admin.getName(),
							"http://" + domain + "/index.zul?persp=cuppy.perspective.cuppy&events=activation&act-item="
									+ player.getPk().toString() }));
				}
			};
			send(preparer, admin);
		}
	}

	@Override
	public void sendNewsletter(final NewsModel news, final List<PlayerModel> players)
	{
		for (final PlayerModel player : players)
		{
			if (player.isSendNewsletter())
			{
				final MailPreparator preparer = new MailPreparator()
				{
					@Override
					public void prepare(final MimeMessageHelper message) throws MessagingException
					{
						message.setSubject(l10NService.getLocalizedString("mail.news.subject"));
						message.setText("<html><body>" + l10NService.getLocalizedString("mail.news.text", new Object[]
						{ StringEscapeUtils.escapeHtml(player.getName()), news.getContent() }) + "</body></html>", true);
					}
				};
				send(preparer, player);
			}
		}
	}

	@Override
	public void sendReminder(final List<MatchModel> matches, final PlayerModel player)
	{
		final MailPreparator preparer = new MailPreparator()
		{
			@Override
			public void prepare(final MimeMessageHelper message) throws MessagingException
			{
				final DateFormat dateFormat = formatFactory.createDateTimeFormat(DateFormat.MEDIUM, DateFormat.MEDIUM);
				final List<MatchReminderHelper> matchReminders = new ArrayList<MatchReminderHelper>();
				for (final MatchModel match : matches)
				{
					final String name = match.getGroup().getCompetition().getName();
					final String kickOffTime = dateFormat.format(match.getDate());
					final String home = match.getHomeTeam().getName();
					final String guest = match.getGuestTeam().getName();
					final MatchReminderHelper reminder = new MatchReminderHelper(name, kickOffTime, home, guest);
					matchReminders.add(reminder);
				}
				final ReminderContext reminderContext = new ReminderContext(player, matchReminders);
				final StringWriter mailMessage = new StringWriter();
				final RendererTemplateModel reminderTemplate = rendererService.getRendererTemplateForCode("reminder");
				rendererService.render(reminderTemplate, reminderContext, mailMessage);

				message.setSubject(l10NService.getLocalizedString("mail.reminder.subject"));
				message.setText(mailMessage.toString(), true);
			}
		};
		send(preparer, player);
	}

	@Override
	public void sendNewPassword(final PlayerModel player, final String newPassword)
	{
		final MailPreparator preparer = new MailPreparator()
		{
			@Override
			public void prepare(final MimeMessageHelper message) throws MessagingException
			{
				message.setSubject(l10NService.getLocalizedString("mail.newpassword.subject"));
				message.setText(l10NService.getLocalizedString("mail.newpassword.body", new Object[]
				{ player.getName(), "http://" + domain + "/index.zul", player.getUid(), newPassword }));
			}
		};
		send(preparer, player);
	}

	private String getMailExceptionMessage(final MailException exception)
	{
		final StringBuilder result = new StringBuilder(exception.getMessage());
		if (exception.getCause() != null)
		{
			final Throwable cause = exception.getCause();
			result.append(": ").append(exception.getMessage());
			if (cause.getCause() != null)
			{
				result.append(": ").append(cause.getMessage());
			}
		}
		return result.toString();
	}

	protected void send(final MailPreparator preparer, final PlayerModel player)
	{
		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				i18nService.setLocalizationFallbackEnabled(true);
				if (player.getSessionLanguage() != null)
				{
					i18nService.setCurrentLocale(new Locale(player.getSessionLanguage().getIsocode()));
				}

				final MimeMessagePreparator preparator = new MimeMessagePreparator()
				{
					@Override
					public void prepare(final MimeMessage mimeMessage) throws Exception //NOPMD
					{
						final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
						message.setTo(player.getEMail());
						message.setFrom(fromAddress);
						message.setReplyTo(replyToAddress);
						preparer.prepare(message);
					}
				};
				try
				{
					mailSender.send(preparator);
				}
				catch (final MailException e)
				{
					//log it and go on
					LOG.error("Can not send mail to " + player.getUid() + " - " + player.getEMail() + ": "
							+ getMailExceptionMessage(e));
				}
			}
		});
	}

	@Override
	public void sendRankingMail(final PlayerModel player, final List<RankingData> rankings)
	{
		try
		{
			final MailPreparator preparer = new MailPreparator()
			{
				@Override
				public void prepare(final MimeMessageHelper message) throws MessagingException
				{
					message.setSubject(l10NService.getLocalizedString("mail.ranking.subject"));

					final RendererTemplateModel template = rendererService.getRendererTemplateForCode("rankingMail");
					final StringWriter renderedText = new StringWriter();

					rendererService.render(template, new RankingMailContext(rankings, player), renderedText);
					message.setText(renderedText.getBuffer().toString(), true);
				}
			};
			send(preparer, player);
		}
		catch (final RendererException e)
		{
			LOG.error("Error while rendering ranking mail for " + player.getUid() + ", skipping send of mail", e);
		}
	}

	protected interface MailPreparator
	{
		void prepare(MimeMessageHelper message) throws Exception; //NOPMD
	}

	public void setMailSender(final JavaMailSender mailSender)
	{
		this.mailSender = mailSender;
	}

}
