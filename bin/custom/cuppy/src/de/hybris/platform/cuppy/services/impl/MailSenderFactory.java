/**
 * 
 */
package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.core.Registry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;


/**
 * @author andreas.thaler
 * 
 */
@Service("mailSender")
public class MailSenderFactory implements FactoryBean
{
	private static final Logger LOG = Logger.getLogger(MailSenderFactory.class);

	private static final String PARAM_HOST = "mail.smtp.server";
	private static final String PARAM_PORT = "mail.smtp.port";
	private static final String PARAM_USER = "mail.smtp.user";
	private static final String PARAM_PWD = "mail.smtp.password";
	private static final String PARAM_START_TLS = "mail.smtp.starttls.enable";

	private final JavaMailSender mailSender;

	public MailSenderFactory()
	{
		final String host = Registry.getMasterTenant().getConfig().getParameter(PARAM_HOST);
		final String user = Registry.getMasterTenant().getConfig().getParameter(PARAM_USER);
		final String port = Registry.getMasterTenant().getConfig().getParameter(PARAM_PORT);
		final String pwd = Registry.getMasterTenant().getConfig().getParameter(PARAM_PWD);
		final String startTls = Registry.getMasterTenant().getConfig().getParameter(PARAM_START_TLS);

		if (host == null || host.isEmpty() || port == null || port.isEmpty())
		{
			LOG.error("Can not start mail sender, please configure properties " + PARAM_HOST + " and " + PARAM_PORT);
			mailSender = new StubJavaMailSender();
		}
		else
		{
			final JavaMailSenderImpl sender = new JavaMailSenderImpl();
			sender.setHost(host);

			sender.setPort(Integer.parseInt(port));
			if (user != null)
			{
				sender.setUsername(user);
			}
			if (pwd != null)
			{
				sender.setPassword(pwd);
			}
			if (startTls != null && Boolean.parseBoolean(startTls))
			{
				final Properties javaMailProperties = new Properties();
				javaMailProperties.setProperty(PARAM_START_TLS, "true");
				sender.setJavaMailProperties(javaMailProperties);
			}
			mailSender = sender;
		}
	}

	@Override
	public Object getObject()
	{
		return mailSender;
	}

	@Override
	public Class getObjectType()
	{
		return JavaMailSender.class;
	}

	@Override
	public boolean isSingleton()
	{
		return true;
	}

	private static class StubJavaMailSender implements JavaMailSender
	{
		private static final Logger LOG = Logger.getLogger(StubJavaMailSender.class);

		@Override
		public MimeMessage createMimeMessage()
		{
			return new MimeMessage(Session.getDefaultInstance(new Properties()));
		}

		@Override
		public MimeMessage createMimeMessage(final InputStream contentStream) throws MailException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void send(final MimeMessage mimeMessage) throws MailException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void send(final MimeMessage... mimeMessages) throws MailException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void send(final MimeMessagePreparator mimeMessagePreparator) throws MailException
		{
			final MimeMessage message = createMimeMessage();
			try
			{
				mimeMessagePreparator.prepare(message);
			}
			catch (final Exception e)
			{
				LOG.error("Error while creating mime message", e);
			}
			try
			{
				LOG.info("Mail send ..");
				LOG.info("  Recipients: " + Arrays.toString(message.getRecipients(RecipientType.TO)));
				LOG.info("  Sender:     " + Arrays.toString(message.getFrom()));
				LOG.info("  Subject:    " + message.getSubject());
				try
				{
					LOG.info("  Content:    " + message.getContent());
				}
				catch (final IOException e) //NOPMD
				{
					// Ignoring it as it was for debug reasons only
				}
			}
			catch (final MessagingException e)
			{
				LOG.error("Error while getting TO", e);
			}
		}

		@Override
		public void send(final MimeMessagePreparator... mimeMessagePreparators) throws MailException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void send(final SimpleMailMessage simpleMessage) throws MailException
		{
			LOG.info("Mail send ..");
			LOG.info("  Recipients: " + Arrays.toString(simpleMessage.getTo()));
			LOG.info("  Sender:     " + simpleMessage.getFrom());
			LOG.info("  Subject:    " + simpleMessage.getSubject());
			LOG.info("  Content:    " + simpleMessage.getText());
		}

		@Override
		public void send(final SimpleMailMessage... simpleMessages) throws MailException
		{
			throw new UnsupportedOperationException();
		}
	}
}
