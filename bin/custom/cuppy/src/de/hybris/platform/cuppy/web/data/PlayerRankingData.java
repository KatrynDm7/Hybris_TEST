/**
 * 
 */
package de.hybris.platform.cuppy.web.data;

import java.util.Locale;


/**
 * @author andreas.thaler
 * 
 */
public class PlayerRankingData
{
	private int rank;
	private int lastRank;
	private String playerId;
	private String playerName;
	private String pictureUrl;
	private Locale locale;
	private String flagUrl;
	private int score;
	private int lastScore;
	private String playerEMail;
	private boolean playerOnline;
	private int matchday;
	private int lastMatchday;

	public int getRank()
	{
		return rank;
	}

	public void setRank(final int rank)
	{
		this.rank = rank;
	}

	public String getPictureUrl()
	{
		return pictureUrl;
	}

	public void setPictureUrl(final String pictureUrl)
	{
		this.pictureUrl = pictureUrl;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(final int score)
	{
		this.score = score;
	}

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(final Locale locale)
	{
		this.locale = locale;
	}

	public String getFlagUrl()
	{
		return flagUrl;
	}

	public void setFlagUrl(final String flagUrl)
	{
		this.flagUrl = flagUrl;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(final String playerName)
	{
		this.playerName = playerName;
	}

	public String getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(final String playerId)
	{
		this.playerId = playerId;
	}

	public String getPlayerEMail()
	{
		return playerEMail;
	}

	public void setPlayerEMail(final String playerEMail)
	{
		this.playerEMail = playerEMail;
	}

	public boolean isPlayerOnline()
	{
		return playerOnline;
	}

	public void setPlayerOnline(final boolean playerOnline)
	{
		this.playerOnline = playerOnline;
	}

	public void setLastRank(final int lastRank)
	{
		this.lastRank = lastRank;
	}

	public int getLastRank()
	{
		return lastRank;
	}

	public int getLastScore()
	{
		return lastScore;
	}

	public void setLastScore(final int lastScore)
	{
		this.lastScore = lastScore;
	}

	public int getMatchday()
	{
		return matchday;
	}

	public void setMatchday(final int matchday)
	{
		this.matchday = matchday;
	}

	public int getLastMatchday()
	{
		return lastMatchday;
	}

	public void setLastMatchday(final int lastMatchday)
	{
		this.lastMatchday = lastMatchday;
	}

	public int getTrend()
	{
		final int rankDifference = lastRank - rank;
		if (rankDifference > 0)
		{
			if (rankDifference > 1)
			{
				return 2;
			}
			else
			{
				return 1;
			}
		}
		else if (rankDifference < 0)
		{
			if (rankDifference < -1)
			{
				return -2;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return 0;
		}
	}
}
