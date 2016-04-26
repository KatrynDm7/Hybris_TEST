/**
 * 
 */
package de.hybris.platform.cuppy.web.components;

import java.util.Comparator;

import org.zkoss.zk.ui.Component;


/**
 * @author andreas.thaler
 * 
 */
public class ComponentComparator<T extends Comparable> implements Comparator<Component>
{
	private final boolean asc;
	private final String key;

	public ComponentComparator(final boolean asc, final String key)
	{
		super();
		this.asc = asc;
		this.key = key;
	}

	@Override
	public int compare(final Component cmp1, final Component cmp2)
	{
		final T val1 = (T) cmp1.getAttribute(key);
		final T val2 = (T) cmp2.getAttribute(key);
		if (val1 instanceof String && val2 instanceof String)
		{
			return asc ? ((String) val1).compareToIgnoreCase((String) val2) : val2.compareTo(val1);
		}
		return asc ? val1.compareTo(val2) : val2.compareTo(val1);
	}
}
