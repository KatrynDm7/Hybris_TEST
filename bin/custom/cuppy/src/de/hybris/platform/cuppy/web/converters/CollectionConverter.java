/**
 * 
 */
package de.hybris.platform.cuppy.web.converters;

import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collection;
import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public interface CollectionConverter<SOURCE, TARGET> extends Converter<SOURCE, TARGET>
{
	/**
	 * Converts all sources to new instances of target type.
	 */
	List<TARGET> convertAll(final Collection<SOURCE> sources);
}
