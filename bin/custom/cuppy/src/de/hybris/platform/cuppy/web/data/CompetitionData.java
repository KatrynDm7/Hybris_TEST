/**
 * 
 */
package de.hybris.platform.cuppy.web.data;

/**
 * @author andreas.thaler
 * 
 */
public class CompetitionData
{
	private String code;
	private String name;
	private boolean currentCompetition;
	private boolean active;
	private boolean deactivatable;
	private boolean tournament;
	private boolean finished;

	public String getCode()
	{
		return code;
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public boolean isCurrentCompetition()
	{
		return currentCompetition;
	}

	public void setCurrentCompetition(final boolean currentCompetition)
	{
		this.currentCompetition = currentCompetition;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(final boolean active)
	{
		this.active = active;
	}

	public boolean isDeactivatable()
	{
		return deactivatable;
	}

	public void setDeactivatable(final boolean deactivatable)
	{
		this.deactivatable = deactivatable;
	}

	public boolean isTournament()
	{
		return tournament;
	}

	public void setTournament(final boolean tournament)
	{
		this.tournament = tournament;
	}

	public boolean isFinished()
	{
		return finished;
	}

	public void setFinished(final boolean finished)
	{
		this.finished = finished;
	}
}
