package de.hybris.platform.cuppy.web.components;

import java.util.Collections;
import java.util.Map;


public class CuppyMainPage
{
	private String viewURI;
	private String label;
	private boolean ownWindow = false;
	private Map<String, Object> args = Collections.EMPTY_MAP;
	private boolean onlyTournament = false;

	public String getViewURI()
	{
		return viewURI;
	}

	public void setViewURI(final String viewURI)
	{
		this.viewURI = viewURI;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(final String label)
	{
		this.label = label;
	}

	public Map<String, Object> getArgs()
	{
		return args;
	}

	public void setArgs(final Map<String, Object> args)
	{
		this.args = args;
	}

	public boolean isOwnWindow()
	{
		return ownWindow;
	}

	public void setOwnWindow(final boolean ownWindow)
	{
		this.ownWindow = ownWindow;
	}

	public boolean isOnlyTournament()
	{
		return onlyTournament;
	}

	public void setOnlyTournament(final boolean onlyTournament)
	{
		this.onlyTournament = onlyTournament;
	}
}
