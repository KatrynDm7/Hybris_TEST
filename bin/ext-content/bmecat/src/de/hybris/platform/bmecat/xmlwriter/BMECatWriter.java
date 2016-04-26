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
package de.hybris.platform.bmecat.xmlwriter;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.Utilities;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;


/**
 * The <code>BMECatWriter</code> is an interface to easy specify an BMECatExport. The mandatory informations have to be
 * specified in the constructor.
 * 
 * If you want to customize the export via the <code>USER_DEFINED_EXTENSIONS</code> you can add own
 * <code>XMLTagWriter</code> to the <code>UserDefinedExtensionsTagWriter</code> provided by the methods:
 * <ul>
 * <li><code>getHeaderUserDefinedExtensionsTagWriter()</code></li>
 * <li><code>getArticleUserDefinedExtensionsTagWriter()</code></li>
 * <li><code>getCatalogStructureUserDefinedExtensionsTagWriter()</code></li>
 * </ul>
 * of <code>BMECatTagWriter</code>
 * 
 * Here is an example:
 * 
 * <pre>
 * final UserDefinedExtensionsTagWriter articleUdxTagWriter = bmeCatTagWriter.getArticleUserDefinedExtensionsTagWriter();
 * articleUdxTagWriter.addSubTagWriter(new SimpleTagWriter(articleUdxTagWriter, &quot;UDX.HYBRIS.PK&quot;)
 * {
 * 	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
 * 	{
 * 		final Product p = (Product) object;
 * 		xmlOut.setLineBreak(LineBreak.NONE);
 * 		xmlOut.pcdata(p.getPK().toString());
 * 	}
 * });
 * </pre>
 * 
 * You can also add, remove or replace any <code>XMLTagWriter</code> via the <code>addSubTagWriter</code>-methods of
 * <code>XMLTagWriter</code>.
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class BMECatWriter
{
	private final CatalogVersion catVersion;
	private final EnumerationValue transactionMode;
	private final Collection classificationSystemVersions;
	private final BMECatTagWriter bmeCatTagWriter;
	private final Language exportLang;
	private final Currency exportCurrency;
	private final Map hybris2bmecatMediaMap;
	private final Map priceMapping;
	private final boolean udpNet;
	private final Customer refCustomer;
	private final Date refDate;
	private final boolean ignoreErrorsFlag;
	private final boolean suppressEmptyCategoriesFlag;
	private final boolean suppressProductsWithoutPricesFlag;
	private final String classificationNumberFormat;
	private RangedItemProvider productsProvider = null;
	private RangedItemProvider categoriesProvider = null;
	private Media exportedMedias = null;
	private boolean gotErrors;

	private static final Logger _log = Logger.getLogger(BMECatWriter.class.getName());

	/*
	 * Logging methods to be overwritten
	 */
	protected boolean isInfoEnabled()
	{
		return _log.isInfoEnabled();
	}

	protected boolean isDebugEnabled()
	{
		return _log.isDebugEnabled();
	}

	protected boolean isWarnEnabled()
	{
		return _log.isEnabledFor(Level.WARN);
	}

	protected boolean isErrorEnabled()
	{
		return _log.isEnabledFor(Level.ERROR);
	}

	protected void info(final String message)
	{
		_log.info(message);
	}

	protected void debug(final String message)
	{
		_log.debug(message);
	}

	protected void warn(final String message)
	{
		_log.warn(message);
	}

	protected void error(final String message)
	{
		_log.error(message);
	}

	/*
	 * Own BMECatTagWriter class to redirect logging to own (possibly overwritten) logging methods
	 */

	protected class MyBMECatTagWriter extends BMECatTagWriter
	{
		@Override
		protected boolean isInfoEnabled()
		{
			return BMECatWriter.this.isInfoEnabled();
		}

		@Override
		protected boolean isDebugEnabled()
		{
			return BMECatWriter.this.isDebugEnabled();
		}

		@Override
		protected boolean isWarnEnabled()
		{
			return BMECatWriter.this.isWarnEnabled();
		}

		@Override
		protected boolean isErrorEnabled()
		{
			return BMECatWriter.this.isErrorEnabled();
		}

		@Override
		protected void info(final String message)
		{
			BMECatWriter.this.info(message);
		}

		@Override
		protected void debug(final String message)
		{
			BMECatWriter.this.debug(message);
		}

		@Override
		protected void warn(final String message)
		{
			BMECatWriter.this.warn(message);
		}

		@Override
		protected void error(final String message)
		{
			BMECatWriter.this.error(message);
		}
	}

	/**
	 * Constructs an <code>BMECatWriter</code>.
	 * 
	 * @param catVersion
	 *           <code>CatalogVersion</code> that should be exported
	 * @param transactionMode
	 *           the BMECat mode that should be exported
	 * @param classificationSystemVersions
	 *           a <code>Collection</code> of <code>ClassificationSystemVersion</code>s that should be exported
	 * @param exportLang
	 *           the <code>Language</code> of the BMECat export
	 */
	public BMECatWriter(final CatalogVersion catVersion, final EnumerationValue transactionMode,
			final Collection classificationSystemVersions, final Language exportLang, final Map hybris2bmecatMediaMap,
			final Media exportedMedias, final Map priceMapping, final boolean udpNet, final Date refDate,
			final Customer refCustomer, final Currency exportCurrency, final boolean ignoreErrors,
			final boolean suppressEmptyCategories, final boolean suppressProductsWithoutPrices,
			final String classificationNumberFormat)
	{
		this.catVersion = catVersion;
		this.transactionMode = transactionMode;
		this.ignoreErrorsFlag = ignoreErrors;
		this.classificationSystemVersions = classificationSystemVersions != null ? new HashSet(classificationSystemVersions)
				: Collections.EMPTY_SET;
		this.hybris2bmecatMediaMap = hybris2bmecatMediaMap != null ? new HashMap(hybris2bmecatMediaMap) : Collections.EMPTY_MAP;
		this.priceMapping = priceMapping != null ? new HashMap(priceMapping) : Collections.EMPTY_MAP;
		this.exportLang = exportLang;
		this.exportedMedias = exportedMedias;
		this.udpNet = udpNet;
		this.refDate = refDate;
		this.bmeCatTagWriter = new MyBMECatTagWriter();
		this.exportCurrency = exportCurrency;
		this.refCustomer = refCustomer;
		this.suppressEmptyCategoriesFlag = suppressEmptyCategories;
		this.suppressProductsWithoutPricesFlag = suppressProductsWithoutPrices;
		this.classificationNumberFormat = classificationNumberFormat;
	}

	public String getClassificationNumberFormat()
	{
		return classificationNumberFormat;
	}

	public boolean suppressEmptyCategories()
	{
		return this.suppressEmptyCategoriesFlag;
	}

	public boolean suppressProductsWithoutPrices()
	{
		return this.suppressProductsWithoutPricesFlag;
	}

	public Customer getReferenceCustomer()
	{
		return this.refCustomer;
	}

	public boolean isUDPNet()
	{
		return this.udpNet;
	}

	public boolean ignoreErrors()
	{
		return this.ignoreErrorsFlag;
	}

	public Currency getExportCurrency()
	{
		return this.exportCurrency;
	}

	public boolean errorsOccured()
	{
		return gotErrors;
	}

	protected XMLOutputter createXMLOutputter(final Writer writer) throws IllegalStateException, IllegalArgumentException,
			IOException
	{
		final UndoableXMLOutputter xmlOut = new UndoableXMLOutputter(writer, "UTF-8");
		xmlOut.setEscaping(true);
		xmlOut.setLineBreak(LineBreak.UNIX);
		xmlOut.setIndentation("\t");
		xmlOut.setQuotationMark('"');

		xmlOut.declaration();
		xmlOut.dtd("BMECAT", null, "bmecat_new_catalog.dtd");
		xmlOut.whitespace("\n");
		return xmlOut;
	}

	public Date getReferenceDate()
	{
		return this.refDate;
	}

	protected static class SessionSettings
	{
		private final Map attributes;

		SessionSettings(final JaloSession session)
		{
			this.attributes = new HashMap(session.getSessionContext().getAttributes());
		}

		void restore(final JaloSession session)
		{
			session.getSessionContext().setAttributes(this.attributes);
		}
	}

	protected SessionSettings changeSessionSettings(final JaloSession session)
	{
		final SessionSettings ret = new SessionSettings(session);

		/*
		 * adjust session settings: reference customer -> product and category visibility -> prices, taxes and discounts
		 */

		if (getReferenceCustomer() != null)
		{
			session.setUser(getReferenceCustomer());
		}
		else
		{
			_log.warn("reference cusotmer not specified! Using " + session.getUser() + " instead!");
		}

		// export language
		session.getSessionContext().setLanguage(getExportLang());
		// export currency
		session.getSessionContext().setCurrency(getExportCurrency());
		/*
		 * adjust current date(s) -> product visibility
		 */
		session.getSessionContext().setAttribute(SessionContext.USER + "." + User.CURRENT_DATE, getReferenceDate());
		session.getSessionContext().setAttribute(SessionContext.USER + "." + User.CURRENT_TIME, getReferenceDate());
		/*
		 * set exported catalog version as current version -> visibility
		 */
		final Collection versions = new HashSet();
		versions.add(getCatVersion());
		if (this.classificationSystemVersions != null)
		{
			versions.addAll(this.classificationSystemVersions);
		}
		CatalogManager.getInstance().setSessionCatalogVersions(versions);


		return ret;
	}

	/**
	 * Writes a BMECat catalog to the given <code>Writer</code>.
	 * 
	 * @param writer
	 *           the <code>Writer</code> the export should be written to
	 * @throws IOException
	 */
	public void write(final Writer writer) throws IOException
	{
		final JaloSession currentSession = JaloSession.getCurrentSession();
		SessionSettings settings = null;
		try
		{
			settings = changeSessionSettings(currentSession);

			final BMECatExportContext bmeCatExportContext = new BMECatExportContext(this);
			bmeCatExportContext.setCategoriesProvider(getCategoriesProvider());
			bmeCatExportContext.setProductsProvider(getProductsProvider());

			// export catalog file
			bmeCatTagWriter.write(createXMLOutputter(writer), bmeCatExportContext);

			this.gotErrors = bmeCatExportContext.errorsOccured();

			// export media data
			if (exportedMedias != null)
			{
				if (!bmeCatExportContext.getExportMedias().isEmpty())
				{
					final File tmp = File.createTempFile(exportedMedias.getPK().toString(), "temp");
					final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tmp));
					final byte[] buffer = new byte[4096];
					for (final Iterator it = bmeCatExportContext.getExportMedias().iterator(); it.hasNext();)
					{
						final Media media = (Media) it.next();
						ZipEntry entry = null;
						try
						{
							final String entryID = MimeInfoTagWriter.getMimeID(media);
							zos.putNextEntry(entry = new ZipEntry(entryID));
							if (media.hasData())
							{
								final InputStream inputStream = media.getDataFromStream();
								while (inputStream.read(buffer, 0, buffer.length) != -1)
								{
									zos.write(buffer, 0, buffer.length);
								}
							}
						}
						catch (final Exception e)
						{
							if (isErrorEnabled())
							{
								error("error writing media " + media + " : " + e.getLocalizedMessage() + " : "
										+ Utilities.getStackTraceAsString(e));
							}
							this.gotErrors = true;
						}
						finally
						{
							if (entry != null)
							{
								try
								{
									zos.closeEntry();
								}
								catch (final Exception e)
								{
									if (_log.isDebugEnabled())
									{
										_log.debug(e.getMessage());
									}
								}
							}
						}
					}
					try
					{
						zos.close();
						// now copy temp file to target media
						exportedMedias.setData(new DataInputStream(new FileInputStream(tmp)), exportedMedias.getRealFileName(),
								exportedMedias.getMime());
					}
					catch (final Exception e)
					{
						if (isErrorEnabled())
						{
							error("error closing zip stream : " + e.getLocalizedMessage() + " : " + Utilities.getStackTraceAsString(e));
						}
						this.gotErrors = true;
					}
				}
				else
				{
					//no medias to export -> no need of a media
					try
					{
						exportedMedias.remove();
					}
					catch (final ConsistencyCheckException e)
					{
						if (isErrorEnabled())
						{
							error("error removing temp zip file : " + e.getLocalizedMessage() + " : "
									+ Utilities.getStackTraceAsString(e));
						}
						this.gotErrors = true;
					}
				}
			}
		}
		finally
		{
			if (settings != null)
			{
				settings.restore(currentSession);
			}
		}
	}

	public int getTotalItemsCount()
	{
		return getBMECatTagWriter().getTotalItemsCount();
	}

	public int getCurrentItemsCount()
	{
		return getBMECatTagWriter().getCurrentItemsCount();
	}

	/**
	 * @return the <code>BMECatTagWriter</code> from there you have access to all subwriters
	 */
	public BMECatTagWriter getBMECatTagWriter()
	{
		return bmeCatTagWriter;
	}

	/**
	 * An interface to provide items in a range.
	 */
	public interface RangedItemProvider
	{
		/**
		 * @return count of items
		 */
		public int getItemCount();

		/**
		 * Returns <code>Item</code>s in a given range.
		 * 
		 * @param start
		 *           start range
		 * @param count
		 *           return count
		 * @return a <code>Collection</code> of items in the given range
		 */
		public Collection getItems(final int start, final int count);
	}

	/**
	 * @return the provider of the categories to export or <code>null</code> if not set
	 */
	public RangedItemProvider getCategoriesProvider()
	{
		return categoriesProvider;
	}

	/**
	 * Here you can specify the categories that you want to export. If you don't specify an
	 * <code>RangedItemProvider</code> here, all categories of the selected <code>CatalogVersion</code> will be exported.
	 * 
	 * @param categoriesProvider
	 *           the <code>RangedItemProvider</code> for the categories to export
	 */
	public void setCategoriesProvider(final RangedItemProvider categoriesProvider)
	{
		this.categoriesProvider = categoriesProvider;
	}

	/**
	 * @return the provider of the products to export or <code>null</code> if not set
	 */
	public RangedItemProvider getProductsProvider()
	{
		return productsProvider;
	}

	/**
	 * Here you can specify the products that you want to export. If you don't specify an <code>RangedItemProvider</code>
	 * here, all products of the selected <code>CatalogVersion</code> will be exported.
	 * 
	 * @param productsProvider
	 *           the <code>RangedItemProvider</code> for the products to export
	 */
	public void setProductsProvider(final RangedItemProvider productsProvider)
	{
		this.productsProvider = productsProvider;
	}

	/**
	 * @return Returns the catVersion.
	 */
	public CatalogVersion getCatVersion()
	{
		return catVersion;
	}

	/**
	 * @return Returns the classificationSystemVersions.
	 */
	public Collection getClassificationSystemVersions()
	{
		return classificationSystemVersions;
	}

	/**
	 * @return Returns the exportLang.
	 */
	public Language getExportLang()
	{
		return exportLang;
	}

	/**
	 * @return Returns the transactionMode.
	 */
	public EnumerationValue getTransactionMode()
	{
		return transactionMode;
	}

	/**
	 * @return Returns the hybris2bmecatMediaMap.
	 */
	public Map getHybris2bmecatMediaMap()
	{
		return hybris2bmecatMediaMap;
	}

	/**
	 * @return Returns the price mapping map.
	 */
	public Map getPriceMapping()
	{
		return priceMapping;
	}
}
