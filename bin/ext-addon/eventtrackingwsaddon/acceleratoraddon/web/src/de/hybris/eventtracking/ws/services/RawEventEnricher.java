/**
 *
 */
package de.hybris.eventtracking.ws.services;

import javax.servlet.http.HttpServletRequest;


/**
 * @author stevo.slavic
 *
 */
public interface RawEventEnricher
{
	String enrich(String event, HttpServletRequest req);
}
