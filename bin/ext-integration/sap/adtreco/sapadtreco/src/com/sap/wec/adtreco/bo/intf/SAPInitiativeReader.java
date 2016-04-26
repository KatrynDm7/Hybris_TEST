/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.sap.wec.adtreco.bo.intf;

import de.hybris.platform.sap.core.bol.businessobject.BusinessObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.olingo.odata2.api.exception.ODataException;

import com.sap.wec.adtreco.bo.impl.SAPInitiative;


/**
 * 
 */
public interface SAPInitiativeReader extends BusinessObject
{

	/**
	 * Fetch the list of all hybris Marketing campaigns 
	 * 
	 * @return List<SAPInitiative>
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public List<SAPInitiative> getAllInitiatives() throws ODataException, URISyntaxException, IOException, RuntimeException;

	/**
	 * Search for a specific set of hybris Marketing campaigns based on search parameters.
	 * 	 * 
	 * @param search
	 * @return List<SAPInitiative>
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public List<SAPInitiative> searchInitiatives(final String search) throws ODataException, URISyntaxException, IOException, RuntimeException;

	/**
	 * Get the list of hybris Marketing campaigns for the current user
	 * 
	 * @param businesPartner
	 * @return List<SAPInitiative>
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public List<SAPInitiative> searchInitiativesForBP(final String businesPartner, final boolean isAnonymous) throws ODataException, URISyntaxException, IOException, RuntimeException;
	
	
	/**
	 * 
	 * @param businesPartner
	 * @return
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public List<SAPInitiative> searchInitiativesForMultiBP(final String[] businesPartners) throws ODataException, URISyntaxException, IOException, RuntimeException;

	/**
	 * Look for unique hybris Marking campaign by ID (in accelerator runtime)
	 * @param id
	 * @return SAPInitiative
	 * @throws ODataException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws RuntimeException
	 */
	public SAPInitiative getInitiative(final String id) throws ODataException, IOException, URISyntaxException, RuntimeException;

	/**
	 *  Get the selected hybris Marketing campaign data (in CMS)
	 *  
	 * @param id
	 * @return SAPInitiative
	 * @throws ODataException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws RuntimeException
	 */
	public SAPInitiative getSelectedInitiative(final String id) throws ODataException, IOException, URISyntaxException, RuntimeException;

}
