/**
 *
 */
package de.hybris.platform.samlsinglesignon;

import de.hybris.platform.core.model.user.UserModel;

import java.util.Collection;


/**
 *
 */
public interface SSOUserService
{
	/**
	 * @throws IllegalArgumentException
	 *            in case the user cannot be mapped due to roles being unknown or disallowed
	 * @param id
	 *           the user id
	 * @param roles
	 *           user roles
	 * @return existing or newly created user model
	 */
	UserModel getOrCreateSSOUser(String id, String name, Collection<String> roles);
}
