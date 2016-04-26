/**
 * 
 */
package de.hybris.platform.cuppy.web.data;

import java.util.Locale;


/**
 * @author andreas.thaler
 * 
 */
public class PlayerProfileData
{
	private String id;
	private String name;
	private String eMail;
	private Locale locale;
	private String password;
	private String pictureUrl;

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getEMail()
	{
		return eMail;
	}

	public void setEMail(final String eMail)
	{
		this.eMail = eMail;
	}

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(final Locale locale)
	{
		this.locale = locale;
	}

	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	public String getPassword()
	{
		return password;
	}

	public String getPictureUrl()
	{
		return pictureUrl;
	}

	public void setPictureUrl(final String pictureUrl)
	{
		this.pictureUrl = pictureUrl;
	}

}
