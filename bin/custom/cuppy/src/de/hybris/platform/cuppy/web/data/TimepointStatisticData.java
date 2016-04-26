/**
 * 
 */
package de.hybris.platform.cuppy.web.data;

import java.util.Date;


/**
 * @author andreas.thaler
 * 
 */
public class TimepointStatisticData
{
	private Date date;
	private int playerOnline;

	public Date getDate()
	{
		return date;
	}

	public void setDate(final Date date)
	{
		this.date = date;
	}

	public int getPlayerOnline()
	{
		return playerOnline;
	}

	public void setPlayerOnline(final int playerOnline)
	{
		this.playerOnline = playerOnline;
	}
}
