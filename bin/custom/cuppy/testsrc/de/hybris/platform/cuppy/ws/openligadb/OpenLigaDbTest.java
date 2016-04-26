package de.hybris.platform.cuppy.ws.openligadb;

import static org.junit.Assert.assertNotNull;

import de.hybris.platform.cuppy.ws.openligadb.types.GetMatchdataByGroupLeagueSaison;
import de.hybris.platform.cuppy.ws.openligadb.types.GetMatchdataByGroupLeagueSaisonResponse;
import de.hybris.platform.cuppy.ws.openligadb.types.Matchdata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.core.WebServiceTemplate;


@ContextConfiguration(locations =
{ "classpath:cuppy-ws-spring.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class OpenLigaDbTest
{
	@Autowired
	private WebServiceTemplate openLigaDBTemplate;

	@Test
	public void testWebServiceWithJAXB() throws Exception
	{
		final GetMatchdataByGroupLeagueSaison request = new GetMatchdataByGroupLeagueSaison();
		request.setLeagueShortcut("bl1");
		request.setLeagueSaison("2010");
		request.setGroupOrderID(1);

		final GetMatchdataByGroupLeagueSaisonResponse response = (GetMatchdataByGroupLeagueSaisonResponse) openLigaDBTemplate
				.marshalSendAndReceive(request);

		assertNotNull(response);

		for (final Matchdata group : response.getGetMatchdataByGroupLeagueSaisonResult().getMatchdata())
		{
			System.out.println(group.getGroupOrderID() + " - " + group.getMatchID() + " - " + group.getIdTeam1() + " - "
					+ group.getNameTeam1());
		}
	}
}
