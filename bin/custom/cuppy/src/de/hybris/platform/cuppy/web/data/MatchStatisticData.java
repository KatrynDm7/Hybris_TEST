/**
 * 
 */
package de.hybris.platform.cuppy.web.data;

/**
 * @author andreas.thaler
 * 
 */
public class MatchStatisticData
{
	private MatchData match;
	private long timeToBet;
	private int playersNotPlacedBetsCount;
	private int playersNotPlacedBetsPerc;
	private double averageScore;

	public MatchData getMatch()
	{
		return match;
	}

	public void setMatch(final MatchData match)
	{
		this.match = match;
	}

	public long getTimeToBet()
	{
		return timeToBet;
	}

	public void setTimeToBet(final long timeToBet)
	{
		this.timeToBet = timeToBet;
	}

	public int getPlayersNotPlacedBetsPerc()
	{
		return playersNotPlacedBetsPerc;
	}

	public void setPlayersNotPlacedBetsPerc(final int playersNotPlacedBetsPerc)
	{
		this.playersNotPlacedBetsPerc = playersNotPlacedBetsPerc;
	}

	public int getPlayersNotPlacedBetsCount()
	{
		return playersNotPlacedBetsCount;
	}

	public void setPlayersNotPlacedBetsCount(final int playersNotPlacedBetsCount)
	{
		this.playersNotPlacedBetsCount = playersNotPlacedBetsCount;
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
