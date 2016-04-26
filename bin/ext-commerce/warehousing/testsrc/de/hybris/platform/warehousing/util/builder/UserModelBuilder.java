package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.user.UserModel;


public class UserModelBuilder
{
	private final UserModel model;

	private UserModelBuilder()
	{
		model = new UserModel();
	}

	private UserModel getModel()
	{
		return this.model;
	}

	public static UserModelBuilder aModel()
	{
		return new UserModelBuilder();
	}

	public UserModel build()
	{
		return getModel();
	}

	public UserModelBuilder withUid(final String uid)
	{
		getModel().setUid(uid);
		return this;
	}

}
