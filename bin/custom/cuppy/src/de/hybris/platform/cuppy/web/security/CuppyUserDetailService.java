/**
 * 
 */
package de.hybris.platform.cuppy.web.security;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cockpit.security.CockpitUserDetailsService;
import de.hybris.platform.core.PK;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.jalo.Player;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.spring.security.CoreUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * @author andreas.thaler
 * 
 */
public class CuppyUserDetailService extends CockpitUserDetailsService
{
	private static final Logger LOG = Logger.getLogger(CuppyUserDetailService.class);

	@Override
	public CoreUserDetails loadUserByUsername(final String username)
	{
		if (username == null)
		{
			return null;
		}

		final User user;
		try
		{
			user = UserManager.getInstance().getUserByLogin(username);
		}
		catch (final JaloItemNotFoundException e)
		{
			LOG.warn("User " + username + " not found at login");
			throw new UsernameNotFoundException("User not found!", e);
		}
		UserGroup usergroup = null;
		try
		{
			usergroup = UserManager.getInstance().getUserGroupByGroupID(CuppyConstants.USERGROUP_CUPPYPLAYERS);
		}
		catch (final JaloItemNotFoundException e)
		{
			LOG.error("Usergroup cuppyplayer not found, Login not possible.");
			throw new UsernameNotFoundException("Can not find cuppyplayer usergroup!", e);
		}
		if (!(user instanceof Player))
		{
			LOG.warn("User " + username + " is not a player and can not login");
			throw new UsernameNotFoundException("User is not playing CuppY!");
		}
		if (!user.getGroups().contains(usergroup))
		{
			LOG.warn("User " + username + " is not member of cuppyplayer and can not login");
			throw new UsernameNotFoundException("User is not a CuppY player!");
		}
		if (!((Player) user).isConfirmedAsPrimitive())
		{
			LOG.warn("User " + username + " is not confirmed yet, no login possible");
			throw new UsernameNotFoundException("Player not confirmed yet!");
		}

		final CoreUserDetails details = super.loadUserByUsername(username);

		if (isActivateCatalogVersions())
		{
			final SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
			ctx.setUser(user);
			final Collection<CatalogVersion> allowedVersions = new ArrayList<CatalogVersion>();
			if (user.isAdmin())
			{
				allowedVersions.addAll(CatalogManager.getInstance().getAllCatalogVersions());
			}
			else
			{
				allowedVersions.addAll(CatalogManager.getInstance().getAllReadableCatalogVersions(ctx, user));
			}

			final Collection<CatalogVersion> catalogVersions = CatalogManager.getInstance().getSessionCatalogVersions(ctx);

			if (CollectionUtils.isEmpty(catalogVersions))
			{
				JaloSession.getCurrentSession().setAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS,
						Collections.singleton(PK.createFixedUUIDPK(0, 23)));
			}
		}

		return details;
	}
}
