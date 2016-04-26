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
package de.hybris.platform.ycommercewebservices.util.ws.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.Resource;

import com.google.common.collect.Lists;


/**
 * MessageSource which can be aware of installed addons and extract message bundles from them.
 */
public class AddonAwareMessageSource extends ReloadableResourceBundleMessageSource implements ApplicationContextAware
{
	private static final Logger LOG = Logger.getLogger(AddonAwareMessageSource.class);

	protected boolean scanForAddons;
	protected ContextResource baseAddonDir;
	private ApplicationContext applicationContext;

	//	protected IOFileFilter fileFilter;
	//	protected IOFileFilter dirFilter;

	protected Predicate<String> fileFilter;
	protected Predicate<String> dirFilter;

	protected List<String> basenames;

	public AddonAwareMessageSource()
	{
		this.scanForAddons = true;
		this.dirFilter = n -> {
			final String base = StringUtils.substringAfterLast(n, baseAddonDir.getPathWithinContext());
			return StringUtils.contains(base, File.separator);
		};
		this.fileFilter = n -> StringUtils.endsWithIgnoreCase(n, "xml") || StringUtils.endsWithIgnoreCase(n, "properties");
	}

	/**
	 * Searches for messages in installed addons and adds them to basenames
	 */
	@PostConstruct
	public void setupAddonMessages()
	{
		final List<String> basenameList = new ArrayList<String>();

		if (baseAddonDir == null)
		{
			LOG.debug("baseLocation is null");
			return;
		}

		if (!scanForAddons)
		{
			return;
		}

		try
		{
			final String basePath = baseAddonDir.getPathWithinContext();

			final Collection<String> addonsPath = getAddonsMessages(basePath);
			final Collection<String> addonsMessages = mapAddonLocation(addonsPath, basePath);

			basenameList.addAll(addonsMessages);
		}
		catch (final Exception ex)
		{
			LOG.warn("Scan for addon messages failed", ex);
		}

		basenameList.addAll(basenames);

		final String[] result = basenameList.toArray(new String[basenameList.size()]);
		if (LOG.isDebugEnabled())
		{
			LOG.log(Level.DEBUG, "Loaded message bundles: " + Arrays.toString(result));
		}
		super.setBasenames(result);
	}

	/**
	 * Searches for files defined by fileFilter under baseFile directory and subdirectories defined by dirFilter.
	 *
	 * @param baseFile
	 *           base directory where search starts
	 * @return Collection of paths to message bundle files
	 * @throws IOException
	 */
	protected Collection<String> getAddonsMessages(final String baseFile) throws IOException
	{
		final List<String> result = Lists.newArrayList();

		final Resource[] resources = applicationContext.getResources(baseAddonDir.getFilename() + "**");

		for (final Resource resource : resources)
		{
			final String path = resource.getURL().toExternalForm();
			if (validatePath(path) && validateFilename(path))
			{
				result.add(path);
			}
		}
		return result;
	}

	protected boolean validatePath(final String path)
	{
		if (dirFilter == null)
		{
			return true;
		}
		final String basePath = FilenameUtils.getPath(path);
		return dirFilter.test(basePath);
	}

	protected boolean validateFilename(final String path)
	{
		if (fileFilter == null)
		{
			return true;
		}
		final String filename = FilenameUtils.getName(path);
		return fileFilter.test(filename);
	}

	/**
	 * Maps each element of <b>addonsPath</b> to valid message bundle path. Result collection is also filtered to remove
	 * empty, invalid and duplicated entries.
	 *
	 * @param addonsPath
	 *           paths to transform
	 * @param basePath
	 *           from where result path should start
	 * @return collection of paths to message bundles
	 */
	protected Collection<String> mapAddonLocation(final Collection<String> addonsPath, final String basePath)
	{
		return addonsPath.stream().map(p -> formatPath(p, basePath)).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
	}

	/**
	 * Formats absolute file path using basePath to format acceptable by @link ReloadableResourceBundleMessageSource}
	 * Basename property
	 */
	protected String formatPath(final String path, final String basePath)
	{
		int pos = path.lastIndexOf(basePath);
		//base path is not in the path -> shouldn't happen
		if (pos == -1)
		{
			return null;
		}

		final String pathFromBase = path.substring(pos);
		String fileName = FilenameUtils.getBaseName(pathFromBase);
		final String targetPath = FilenameUtils.getFullPath(pathFromBase);

		pos = fileName.indexOf("_");
		if (pos != -1)
		{
			fileName = fileName.substring(0, pos);
		}

		return FilenameUtils.concat(targetPath, fileName);
	}

	@Override
	public void setBasename(final String basename)
	{
		this.setBasenames(basename);
	}

	@Override
	public void setBasenames(final String... basenames)
	{
		this.basenames = Lists.newArrayList(basenames);
		super.setBasenames(basenames);
	}

	/**
	 * @return the scanForAddons
	 */
	public boolean isScanForAddons()
	{
		return scanForAddons;
	}

	/**
	 * @param scanForAddons
	 *           the scanForAddons to set
	 */
	public void setScanForAddons(final boolean scanForAddons)
	{
		this.scanForAddons = scanForAddons;
	}

	/**
	 * @return the baseAddonDir
	 */
	public ContextResource getBaseAddonDir()
	{
		return baseAddonDir;
	}

	/**
	 * @param baseAddonDir
	 *           the baseAddonDir to set
	 */
	public void setBaseAddonDir(final ContextResource baseAddonDir)
	{
		this.baseAddonDir = baseAddonDir;
	}

	/**
	 * @return the fileFilter
	 */
	public Predicate<String> getFileFilter()
	{
		return fileFilter;
	}

	/**
	 * @param fileFilter
	 *           the fileFilter to set
	 */
	public void setFileFilter(final Predicate<String> fileFilter)
	{
		this.fileFilter = fileFilter;
	}

	/**
	 * @return the dirFilter
	 */
	public Predicate<String> getDirFilter()
	{
		return dirFilter;
	}

	/**
	 * @param dirFilter
	 *           the dirFilter to set
	 */
	public void setDirFilter(final Predicate<String> dirFilter)
	{
		this.dirFilter = dirFilter;
	}

	@Override
	public void setApplicationContext(final ApplicationContext arg0) throws BeansException
	{
		applicationContext = arg0;
	}
}
