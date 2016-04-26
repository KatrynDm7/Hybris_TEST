/**
 * 
 */
package de.hybris.platform.cuppy.web.data;

/**
 * @author andreas.thaler
 * 
 */
public class PlayerStatisticData
{
	private PlayerRankingData player;
	private double averageScore;

	public PlayerRankingData getPlayer()
	{
		return player;
	}

	public void setPlayer(final PlayerRankingData player)
	{
		this.player = player;
	}

	public double getAverageScore()
	{
		return averageScore;
	}

	public void setAverageScore(final double averageScore)
	{
		this.averageScore = averageScore;
	}
}
