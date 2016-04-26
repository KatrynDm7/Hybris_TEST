/**
 *
 */
package de.hybris.eventtracking.ws.services;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;


/**
 * @author stevo.slavic
 *
 */
public class DefaultRawEventEnricher implements RawEventEnricher
{

	private static final String ENRICHMENT_SPEC_TEMPLATE = "[{\"operation\":\"default\", \"spec\": { \"session_id\": \"%s\", \"timestamp\": \"%s\", \"user_id\": \"%s\", \"user_email\": \"%s\" } }]";

	private final UserService userService;

	public DefaultRawEventEnricher(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @see de.hybris.eventtracking.ws.services.RawEventEnricher#enrich(java.lang.String,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String enrich(final String json, final HttpServletRequest req)
	{
		final HttpSession session = req.getSession();
		final String sessionId = session.getId();
		final String timestamp = Integer.toString(Math.round(System.currentTimeMillis() / 1000)); // seconds since Unix epoch
		final UserModel user = userService.getCurrentUser();
		String userId = null;
		String userEmail = null;
		if (user != null && CustomerModel.class.isAssignableFrom(user.getClass()))
		{
			userId = ((CustomerModel) user).getCustomerID();
			userEmail = ((CustomerModel) user).getContactEmail();
		}
		userId = StringUtils.trimToEmpty(userId);
		userEmail = StringUtils.trimToEmpty(userEmail);
		final Chainr chainr = Chainr.fromSpec(JsonUtils.jsonToList(String.format(ENRICHMENT_SPEC_TEMPLATE, sessionId, timestamp,
				userId, userEmail)));
		Map<String, Object> jsonObjectMap;
		try
		{
			jsonObjectMap = JsonUtils.javason(json);
		}
		catch (final IOException e)
		{
			throw new RuntimeException(e);
		}
		return JsonUtils.toJsonString(chainr.transform(jsonObjectMap));
	}

}
