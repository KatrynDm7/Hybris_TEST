/**
 * 
 */
package de.hybris.platform.sap.sapproductavailability.exceptions;

/**
 * @author Administrator
 * 
 */
public class SapProductAvailabilityException extends RuntimeException
{
	
	private static final long serialVersionUID = 7320793872171187671L;

	public SapProductAvailabilityException(final String message)
	{
		super(message);
	}

	public SapProductAvailabilityException(final String message, final Throwable throwable)
	{
		super(message, throwable);
	}

}
