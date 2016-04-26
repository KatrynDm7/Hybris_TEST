/**
 * 
 */
package de.hybris.platform.cuppy.web.converters;

import de.hybris.platform.servicelayer.dto.converter.GenericConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public class GenericCollectionConverter<SOURCE, TARGET> extends GenericConverter<SOURCE, TARGET> implements
		CollectionConverter<SOURCE, TARGET>
{
	@Override
	public List<TARGET> convertAll(final Collection<SOURCE> sources)
	{
		if (sources == null || sources.isEmpty())
		{
			return Collections.emptyList();
		}
		final List<TARGET> result = new ArrayList<TARGET>(sources.size());
		for (final SOURCE source : sources)
		{
			result.add(convert(source));
		}
		return result;
	}
}
