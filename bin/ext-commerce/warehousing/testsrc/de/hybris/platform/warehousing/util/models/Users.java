package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import de.hybris.platform.warehousing.util.builder.UserModelBuilder;

import org.springframework.beans.factory.annotation.Required;


public class Users extends AbstractItems<UserModel>
{
	public static final String UID_BOB = "bob";
	public static final String UID_NANCY = "nancy";
	public static final String UID_MANAGER_MONTREAL_DUKE = "mgr-montreal-duke";
	public static final String UID_MANAGER_MONTREAL_MAISONNEUVE = "mgr-montreal-maisonneuve";

	private UserDao userDao;

	public UserModel Bob()
	{
		return getOrCreateUser(UID_BOB);
	}

	public UserModel Nancy()
	{
		return getOrCreateUser(UID_NANCY);
	}

	public UserModel ManagerMontrealDuke()
	{
		return getOrCreateUser(UID_MANAGER_MONTREAL_DUKE);
	}

	public UserModel ManagerMontrealMaisonneuve()
	{
		return getOrCreateUser(UID_MANAGER_MONTREAL_MAISONNEUVE);
	}

	protected UserModel getOrCreateUser(final String uid)
	{
		return getOrSaveAndReturn(() -> getUserDao().findUserByUID(uid), () -> UserModelBuilder.aModel().withUid(uid)
				.build());
	}

	public UserDao getUserDao()
	{
		return userDao;
	}

	@Required
	public void setUserDao(final UserDao userDao)
	{
		this.userDao = userDao;
	}


}
