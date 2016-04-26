/**
 * 
 */
package de.hybris.platform.cuppy.services;

/**
 * Strategy for returning a random <tt>int</tt> within a range
 * 
 * @author andreas.thaler
 */
public interface RandomizeStrategy
{
	/**
	 * Returns a random <tt>int</tt> within a range
	 * 
	 * @param start
	 * @param end
	 * @return random <tt>int</tt> within a range
	 */
	//TODO:TEST
	int getNext(int start, int end);
}
