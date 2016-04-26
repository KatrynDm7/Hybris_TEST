/**
 * 
 */
package de.hybris.platform.cuppy.web.data;


/**
 * @author andreas.thaler
 * 
 */
public class BetData
{
	private String playerId;
	private String playerName;
	private String playerPictureUrl;
	private int score;
	private Integer homeBet;
	private Integer guestBet;
	private boolean matchFinished;
	private String playerFlagUrl;

	public String getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(final String playerId)
	{
		this.playerId = playerId;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(final String playerName)
	{
		this.playerName = playerName;
	}

	public String getPlayerPictureUrl()
	{
		return playerPictureUrl;
	}

	public void setPlayerPictureUrl(final String playerPictureUrl)
	{
		this.playerPictureUrl = playerPictureUrl;
	}

	public String getPlayerFlagUrl()
	{
		return playerFlagUrl;
	}

	public void setPlayerFlagUrl(final String playerFlagUrl)
	{
		this.playerFlagUrl = playerFlagUrl;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(final int score)
	{
		this.score = score;
	}

	public Integer getHomeBet()
	{
		return homeBet;
	}

	public void setHomeBet(final Integer homeBet)
	{
		this.homeBet = homeBet;
	}

	public Integer getGuestBet()
	{
		return guestBet;
	}

	public void setGuestBet(final Integer guestBet)
	{
		this.guestBet = guestBet;
	}

	public boolean isMatchFinished()
	{
		return matchFinished;
	}

	public void setMatchFinished(final boolean matchFinished)
	{
		this.matchFinished = matchFinished;
	}
}
