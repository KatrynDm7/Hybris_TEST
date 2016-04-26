/**
 * 
 */
package de.hybris.platform.cuppy.web.data;

/**
 * @author andreas.thaler
 * 
 */
public class OverallStatisticData
{
	private int playersCount;

	private int playersCountOverall;

	private int playersOnlineCount;

	private int playersOnlineMaxCount;

	private double averageScore;

	public int getPlayersCount()
	{
		return playersCount;
	}

	public void setPlayersCount(final int playersCount)
	{
		this.playersCount = playersCount;
	}

	public int getPlayersOnlineCount()
	{
		return playersOnlineCount;
	}

	public void setPlayersOnlineCount(final int playersOnlineCount)
	{
		this.playersOnlineCount = playersOnlineCount;
	}

	public int getPlayersOnlineMaxCount()
	{
		return playersOnlineMaxCount;
	}

	public void setPlayersOnlineMaxCount(final int playersOnlineMaxCount)
	{
		this.playersOnlineMaxCount = playersOnlineMaxCount;
	}

	public double getAverageScore()
	{
		return averageScore;
	}

	public void setAverageScore(final double averageScore)
	{
		this.averageScore = averageScore;
	}

	public int getPlayersCountOverall()
	{
		return playersCountOverall;
	}

	public void setPlayersCountOverall(final int playersCountOverall)
	{
		this.playersCountOverall = playersCountOverall;
	}
}
