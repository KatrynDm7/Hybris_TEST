/**
 * 
 */
package de.hybris.platform.cuppy.web.data;

import java.util.Date;


/**
 * @author andreas.thaler
 * 
 */
public class MatchData
{
	private int id;
	private String homeTeam;
	private int homeGoals;
	private Integer homeBet;
	private String homeFlagUrl;
	private String guestTeam;
	private int guestGoals;
	private Integer guestBet;
	private String guestFlagUrl;
	private String location;
	private Date date;
	private boolean matchBetable;
	private boolean matchFinished;
	private GroupData group;
	private int score;
	private int matchday;

	public int getId()
	{
		return id;
	}

	public void setId(final int id)
	{
		this.id = id;
	}

	public String getHomeTeam()
	{
		return homeTeam;
	}

	public void setHomeTeam(final String homeTeam)
	{
		this.homeTeam = homeTeam;
	}

	public Integer getHomeBet()
	{
		return homeBet;
	}

	public void setHomeBet(final Integer homeBet)
	{
		this.homeBet = homeBet;
	}

	public String getGuestTeam()
	{
		return guestTeam;
	}

	public void setGuestTeam(final String guestTeam)
	{
		this.guestTeam = guestTeam;
	}

	public Integer getGuestBet()
	{
		return guestBet;
	}

	public void setGuestBet(final Integer guestBet)
	{
		this.guestBet = guestBet;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(final String location)
	{
		this.location = location;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(final Date date)
	{
		this.date = date;
	}

	public GroupData getGroup()
	{
		return group;
	}

	public void setGroup(final GroupData group)
	{
		this.group = group;
	}

	public String getHomeFlagUrl()
	{
		return homeFlagUrl;
	}

	public void setHomeFlagUrl(final String homeFlagUrl)
	{
		this.homeFlagUrl = homeFlagUrl;
	}

	public String getGuestFlagUrl()
	{
		return guestFlagUrl;
	}

	public void setGuestFlagUrl(final String guestFlagUrl)
	{
		this.guestFlagUrl = guestFlagUrl;
	}

	public boolean isMatchFinished()
	{
		return matchFinished;
	}

	public void setMatchFinished(final boolean matchFinished)
	{
		this.matchFinished = matchFinished;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(final int score)
	{
		this.score = score;
	}

	public int getHomeGoals()
	{
		return homeGoals;
	}

	public void setHomeGoals(final int homeGoals)
	{
		this.homeGoals = homeGoals;
	}

	public int getGuestGoals()
	{
		return guestGoals;
	}

	public void setGuestGoals(final int guestGoals)
	{
		this.guestGoals = guestGoals;
	}

	public boolean isMatchBetable()
	{
		return matchBetable;
	}

	public void setMatchBetable(final boolean matchBetable)
	{
		this.matchBetable = matchBetable;
	}

	public int getMatchday()
	{
		return matchday;
	}

	public void setMatchday(final int matchday)
	{
		this.matchday = matchday;
	}
}
