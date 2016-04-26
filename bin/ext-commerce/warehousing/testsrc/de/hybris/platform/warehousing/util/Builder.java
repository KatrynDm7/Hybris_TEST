package de.hybris.platform.warehousing.util;


/**
 * @param <T>
 *           the type of the instance that is going to be built
 */
public interface Builder<T> {

	/**
	 * Builds an instance of type T (e.g., a SourcingLocation, a FitnessContext, ...)
	 *
	 * @return the instance
	 */
	T build();

}
