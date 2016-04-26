/**
 * 
 */
package de.hybris.platform.cuppy.web.data;

import java.util.Date;


/**
 * @author andreas.thaler
 * 
 */
public class NewsData
{
	private Date creationTime;
	private String content;
	private String competitionName;

	public Date getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(final Date creationTime)
	{
		this.creationTime = creationTime;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(final String content)
	{
		this.content = content;
	}

	public String getCompetitionName()
	{
		return competitionName;
	}

	public void setCompetitionName(final String competitionName)
	{
		this.competitionName = competitionName;
	}
}
