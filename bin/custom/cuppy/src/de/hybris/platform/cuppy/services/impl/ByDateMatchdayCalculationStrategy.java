/**
 * 
 */
package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.MatchdayCalculationStrategy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
public class ByDateMatchdayCalculationStrategy implements MatchdayCalculationStrategy
{
	@Autowired
	private MatchService matchService;

	@Override
	public int getMatchday(final CompetitionModel competition, final int matchdayId, final Date date)
	{
		final Date adjustedDate = getLastCalendarForDay(date);
		final MatchModel lastMatch = matchService.getMatchBefore(competition, adjustedDate);
		if (lastMatch == null)
		{
			return 1;
		}
		if (getLastCalendarForDay(lastMatch.getDate()).equals(adjustedDate))
		{
			return lastMatch.getMatchday();
		}
		else
		{
			return lastMatch.getMatchday() + 1;
		}
	}

	private Date getLastCalendarForDay(final Date date)
	{
		final Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 11);
		calendar.set(Calendar.AM_PM, Calendar.PM);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public void setMatchService(final MatchService matchService)
	{
		this.matchService = matchService;
	}
}
