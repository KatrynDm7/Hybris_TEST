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
package de.hybris.platform.commerceservices.setup;

import java.util.Map;

import de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData;



/**
 * Service that handles importing impex files.
 */
public interface SetupImpexService
{
	/**
	 * Import impex file. The file is looked up from the classpath. If the file does not exist then an info message is
	 * logged. The file should used the ".impex" file extension. Any language specific files are found with the same root
	 * file name then they are also imported. Language specific files have the language iso code appended to the file
	 * name using an underscore as a separator. For example if the file <tt>/path/file.impex</tt> is imported and the
	 * language specific file <tt>/path/file_de.impex</tt> exists, then it will also be imported. Only files for
	 * languages that exist in the hybris system will be imported.
	 * 
	 * @param file
	 *           the file path to import
	 * @param errorIfMissing
	 *           flag, set to true to error if the file is not found
	 * @param legacyMode
	 *           flag, set to true to use legacy impex mode
	 */
	void importImpexFile(String file, boolean errorIfMissing, boolean legacyMode);

	/**
	 * Import impex file. The file is looked up from the classpath. If the file does not exist then an info message is
	 * logged. The file should used the ".impex" file extension. Any language specific files are found with the same root
	 * file name then they are also imported. Language specific files have the language iso code appended to the file
	 * name using an underscore as a separator. For example if the file <tt>/path/file.impex</tt> is imported and the
	 * language specific file <tt>/path/file_de.impex</tt> exists, then it will also be imported. Only files for
	 * languages that exist in the hybris system will be imported.
	 * 
	 * By default this method imports the impex data with legacy mode set to false.
	 * 
	 * @param file
	 *           the file path to import
	 * @param errorIfMissing
	 *           flag, set to true to error if the file is not found
	 */
	void importImpexFile(String file, boolean errorIfMissing);

	boolean importImpexFile(String file, Map<String, Object> macroParameters, boolean errorIfMissing, boolean legacyMode);

	boolean importImpexFile(final String file, Map<String, Object> macroParameters, boolean errorIfMissing);

	boolean importImpexFile(String file, ImpexMacroParameterData macroParameters, boolean errorIfMissing, boolean legacyMode);

	boolean importImpexFile(final String file, ImpexMacroParameterData macroParameters, boolean errorIfMissing);
}
