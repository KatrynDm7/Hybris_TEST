/**
 * 
 */
package de.hybris.platform.cuppy.web.integration;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.impl.DefaultMailService;
import de.hybris.platform.cuppy.web.data.PlayerProfileData;
import de.hybris.platform.cuppy.web.data.PlayerRankingData;
import de.hybris.platform.cuppy.web.facades.PlayerFacade;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;
import java.util.Locale;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;


/**
 * @author andreas.thaler
 * 
 */
public class PlayerFacadeTest extends AbstractCuppyIntegrationTest
{
	@Autowired(required = true)
	private PlayerFacade playerFacade;

	@Autowired(required = true)
	private UserService userService;

	@Autowired(required = true)
	private DefaultMailService mailService;

	private JavaMailSender mailSender;

	@Before
	public void setUp()
	{
		mailSender = createMock(JavaMailSender.class);
		mailService.setMailSender(mailSender);
		playerFacade.setCurrentCompetition("wc2002");
	}

	@Test
	public void testGetRankings()
	{
		final List<PlayerRankingData> result = playerFacade.getRankings();
		assertEquals(19, result.size());

		assertEquals("Marc Digit", result.get(0).getPlayerName());
		assertEquals(1, result.get(0).getRank());
		assertEquals("/medias/fromjar/cuppy/flags/countries/se.png?mime=image%2Fpng&realname=se.png", result.get(0).getFlagUrl());
		assertEquals(new Locale("", "SE"), result.get(0).getLocale());
		assertNotNull(result.get(0).getPictureUrl());
		assertEquals(60, result.get(0).getScore());

		assertEquals("Peter Petersonson", result.get(1).getPlayerName());
		assertEquals(2, result.get(1).getRank());
		assertEquals("/medias/fromjar/cuppy/flags/countries/de.png?mime=image%2Fpng&realname=de.png", result.get(1).getFlagUrl());
		assertEquals(new Locale("", "DE"), result.get(1).getLocale());
		assertNotNull(result.get(1).getPictureUrl());
		assertEquals(54, result.get(1).getScore());

		assertEquals("Nadine von Paletzkie", result.get(18).getPlayerName());
		assertEquals(19, result.get(18).getRank());
		assertEquals("/medias/fromjar/cuppy/flags/countries/gb.png?mime=image%2Fpng&realname=gb.png", result.get(18).getFlagUrl());
		assertEquals(new Locale("", "GB"), result.get(18).getLocale());
		assertNotNull(result.get(18).getPictureUrl());
		assertEquals(26, result.get(18).getScore());
	}

	@Test
	public void testRegistration()
	{
		final PlayerProfileData data = new PlayerProfileData();
		data.setId("test");
		data.setName("test");
		data.setEMail("test@test.de");
		data.setLocale(Locale.GERMANY);

		final Capture<MimeMessagePreparator> cap = new Capture<MimeMessagePreparator>();
		final Capture<MimeMessagePreparator> cap2 = new Capture<MimeMessagePreparator>();
		mailSender.send(capture(cap));
		expectLastCall();
		mailSender.send(capture(cap2));
		expectLastCall();
		replay(mailSender);

		playerFacade.registerPlayer(data);

		final UserModel user = userService.getUserForUID("test");
		assertTrue(user instanceof PlayerModel);
		final PlayerModel player = (PlayerModel) user;
		assertFalse(player.isConfirmed());
		assertEquals("test", player.getUid());
		assertEquals("test", player.getName());
		assertEquals("test@test.de", player.getEMail());
		assertNotNull(cap.getValue());
		assertNotNull(cap.getValue());
		assertNotNull(cap2.getValue());

		verify(mailSender);
	}

	@Test
	public void testGetAllCountries()
	{
		final List<Locale> result = playerFacade.getAllCountries();
		assertEquals(9, result.size());
		assertTrue(result.contains(new Locale("de", "DE")));
		assertTrue(result.contains(new Locale("us", "US")));
	}
}
