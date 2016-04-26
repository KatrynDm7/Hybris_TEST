/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.bmecat.jalo;

import de.hybris.bootstrap.xml.AbstractValueObject;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.BMECatParser;
import de.hybris.platform.bmecat.parser.Catalog;
import de.hybris.platform.bmecat.parser.taglistener.DefaultBMECatTagListener;
import de.hybris.platform.bmecat.parser.taglistener.HeaderUserDefinedExtensionsTagListener;

import org.apache.log4j.Logger;


/**
 * TestBMECatCatalogStep
 */
public class TestBMECatCatalogStep extends GeneratedTestBMECatCatalogStep
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(TestBMECatCatalogStep.class.getName());

	@Override
	protected void importBMECatObject(final Catalog catalog, final AbstractValueObject object, final BMECatImportCronJob cronJob)
			throws ParseAbortException
	{
		if (object instanceof TestObject)
		{
			cronJob.setTransientObject("TEST", ((TestObject) object).getName());
		}
		super.importBMECatObject(catalog, object, cronJob);
	}

	@Override
	protected void customizeImport(final BMECatParser parser)
	{
		final HeaderUserDefinedExtensionsTagListener headerUserDefinedExtensionsTagListener = parser
				.getHeaderUserDefinedExtensionsTagListener();
		headerUserDefinedExtensionsTagListener.registerSubTagListener(new DefaultBMECatTagListener(
				headerUserDefinedExtensionsTagListener)
		{

			/**
			 * @see de.hybris.platform.bmecat.parser.taglistener.DefaultBMECatTagListener#processEndElement(de.hybris.platform.bmecat.parser.BMECatObjectProcessor)
			 */
			@Override
			protected Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
			{
				final TestObject testObject = new TestObject(getCharacters());
				processor.process(this, testObject);
				return testObject;
			}

			/**
			 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getTagName()
			 */
			public String getTagName()
			{
				return "UDX.EXEDIO.TEST";
			}
		});
	}

	public static class TestObject extends AbstractValueObject
	{
		private final String name;

		public TestObject(final String name)
		{
			super();
			this.name = name;
		}

		/**
		 * @return Returns the name.
		 */
		public String getName()
		{
			return name;
		}
	}
}
