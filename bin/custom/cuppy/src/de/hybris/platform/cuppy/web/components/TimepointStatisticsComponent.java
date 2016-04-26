/**
 * 
 */
package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cuppy.web.data.TimepointStatisticData;
import de.hybris.platform.cuppy.web.facades.StatisticsFacade;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.spring.SpringUtil;
import org.zkoss.zul.Chart;
import org.zkoss.zul.Div;
import org.zkoss.zul.SimpleXYModel;
import org.zkoss.zul.XYModel;


/**
 * @author andreas.thaler
 * 
 */
public class TimepointStatisticsComponent extends Div
{
	public TimepointStatisticsComponent()
	{
		super();

		final Chart chart = new Chart();
		chart.setType(Chart.TIME_SERIES);
		chart.setThreeD(false);
		chart.setFgAlpha(128);
		chart.setTitle("Players Online");
		chart.setXAxis("Time");
		chart.setYAxis("Players Online");

		final XYModel xymodel = new SimpleXYModel();
		fillModel(xymodel);
		chart.setModel(xymodel);

		this.appendChild(chart);
	}

	private void fillModel(final XYModel xymodel)
	{
		final Calendar calendar = Calendar.getInstance(UISessionUtils.getCurrentSession().getLocale());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);

		List<TimepointStatisticData> stats = getStatisticsFacade().getTimepointStatistics(
				new Date().getTime() - calendar.getTimeInMillis());
		for (final TimepointStatisticData stat : stats)
		{
			xymodel.addValue("Today", Long.valueOf(stat.getDate().getTime()), Integer.valueOf(stat.getPlayerOnline()));
		}

		stats = getStatisticsFacade().getTimepointStatistics(
				new Date().getTime() - calendar.getTimeInMillis() + 24 * 60 * 60 * 1000,
				new Date().getTime() - calendar.getTimeInMillis());
		for (final TimepointStatisticData stat : stats)
		{
			xymodel.addValue("Yesterday", Long.valueOf(stat.getDate().getTime() + (24 * 60 * 60 * 1000)),
					Integer.valueOf(stat.getPlayerOnline()));
		}
	}

	private StatisticsFacade getStatisticsFacade()
	{
		return (StatisticsFacade) SpringUtil.getBean("statisticsFacade");
	}
}
