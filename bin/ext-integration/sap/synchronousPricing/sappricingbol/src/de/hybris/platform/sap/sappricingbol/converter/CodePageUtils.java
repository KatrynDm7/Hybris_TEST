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
package de.hybris.platform.sap.sappricingbol.converter;

import java.util.HashMap;
import java.util.Map;


public class CodePageUtils
{
	public CodePageUtils()
	{
	}

	private static Map<String, String> initCodeMapJavaLangToSapLang()
	{
		Map<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("IW", "HE");
		hashmap.put("ZH_CN", "ZH");
		hashmap.put("ZH_TW", "ZF");
		return hashmap;
	}

	public static String getSapLangForJavaLanguage(String s)
	{
		s = s.toUpperCase();
		String s1 = (String) codeMapJavaLang.get(s);
		if (s1 == null)
		{
			s1 = s;
		}
		return s1;
	}

	private static Map<String, String> codeMapJavaLang = initCodeMapJavaLangToSapLang();

}
