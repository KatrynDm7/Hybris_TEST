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
 */

package com.hybris.datahub.core.facades.impl.converter;

import de.hybris.bootstrap.annotations.UnitTest;

import com.hybris.datahub.core.facades.ImportError;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;


@SuppressWarnings("javadoc")
@UnitTest
public class PreviewParserUnitTest
{
	private final PreviewParser parser = new PreviewParser();

	@Test
	public void testNoPreviewResultsInEmptyErrors()
	{
		final Collection<ImportError> errors = parser.parse(null);

		Assert.assertTrue(errors.isEmpty());
	}

	@Test
	public void testEmptyPreviewresultsInEmptyErrors()
	{
		final Collection<ImportError> errors = parser.parse("");

		Assert.assertTrue(errors.isEmpty());
	}

	@Test
	public void testCreatesAnErrorWhenPreviewIsNotEmpty()
	{
		final Collection<ImportError> errors = parser.parse(preview());

		Assert.assertEquals(1, errors.size());
	}

	@Test
	public void testCorrectlyExtractsMessageFromThePreviewError()
	{
		final ImportError err = parser.parse(preview()).iterator().next();

		Assert.assertEquals("no existing item found for update", err.getMessage());
	}

	@Test
	public void testCorrectlyExtractsImpexLineFromThePreviewError()
	{
		final ImportError err = parser.parse(preview()).iterator().next();

		Assert.assertEquals(";123;A4;New Product;pieces;;Experimental product by A Company;approved;0123123456789;A Company;1",
				err.getScriptLine());
	}

	@Test
	public void testClassifiesParsedError()
	{
		final ImportError err = parser.parse(preview()).iterator().next();

		Assert.assertNotNull(err.getCode());
	}

	@Test
	public void testExtractsItemIdFromTheErrorMessage()
	{
		final ImportError err = parser.parse(preview()).iterator().next();

		Assert.assertEquals(new Long(123), err.getCanonicalItemId());
	}

	@Test
	public void testParsesAnErrorFromAMessageLineBeginningWithItemType()
	{
		final String[] previewLines =
				{
						"INSERT_UPDATE ApparelProduct;;ean;unit(code);supercategories(code,catalogversion(catalog(id[default=apparelProductCatalog]),version[default='Staged'])[unique=true,default=apparelProductCatalog:Staged]);code[unique=true];catalogversion(catalog(id[default=apparelProductCatalog]),version[default='Staged'])[unique=true,default=apparelProductCatalog:Staged];varianttype(code)[default=ApparelStyleVariantProduct]",
						"ApparelProduct,8796387966977,,, column 3: could not resolve item for bad_unit, column 3: cannot resolve value 'bad_unit' for attribute 'unit';<ignore>5000001;<ignore>;bad_unit;<ignore>;<ignore>M1-bad_unit;<ignore>;<ignore>"};
		final String preview = StringUtils.join(previewLines, System.lineSeparator());

		final ImportError err = parser.parse(preview).iterator().next();

		Assert.assertEquals("column 3: cannot resolve value 'bad_unit' for attribute 'unit'", err.getMessage());
	}

	@Test
	public void testParsesAMessageWrappedInQuotes()
	{
		final String[] previewLines =
				{
						"INSERT_UPDATE ApparelProduct;;ean;unit(code);numberContentUnits",
						"\",,,cannot parse number 'one' with format specified pattern '#,##0.###' due to Unparseable number: \"\"one\"\"\";2;<ignore>;pieces;<ignore>;M1-invalid-data-format;;;one"};
		final String preview = StringUtils.join(previewLines, System.lineSeparator());

		final ImportError err = parser.parse(preview).iterator().next();

		Assert.assertEquals(
				"cannot parse number 'one' with format specified pattern '#,##0.###' due to Unparseable number: \"\"one\"\"\"",
				err.getMessage());
	}

	@Test
	public void testParsesAMessageWithRemoveError()
	{
		final String[] previewLines =
				{
						"REMOVE Category;;supercategories(code,$catalogVersion);$catalogVersion;code[unique=true]",
						",,,could not remove item 8796160688270 due to [de.hybris.platform.category.interceptors.CategoryRemovalValidator@6427d506]:cannot remove [C12], since this category still has sub-categories;9;C2;;C12"};
		final String preview = StringUtils.join(previewLines, System.lineSeparator());

		final ImportError err = parser.parse(preview).iterator().next();

		Assert.assertEquals(
				"could not remove item 8796160688270 due to [de.hybris.platform.category.interceptors.CategoryRemovalValidator@6427d506]:cannot remove [C12], since this category still has sub-categories",
				err.getMessage());
	}

	private String preview()
	{
		final String[] previewLines =
				{
						"UPDATE Product;;code[unique=true];name[lang=en];Unit(code);catalogVersion(catalog(id),version [unique=true,allowNull=true];description[lang=en];approvalStatus(code);ean;manufacturerName;numberContentUnits",
						",,,no existing item found for update;123;A4;New Product;pieces;;Experimental product by A Company;approved;0123123456789;A Company;1"};
		return StringUtils.join(previewLines, System.lineSeparator());
	}

	@Test
	public void testRemovesImpexDecorationsAddedByTheImportServiceFromTheScriptLine()
	{
		final String preview = ",8796191358977,,, column 4: could not resolve item for piece;<ignore>123;<ignore>A2;<ignore>Another Product;piece;<ignore>;<ignore>Highly rated product by consumers;<ignore>approved;<ignore>1230123456789;<ignore>A Company;<ignore>3";

		final ImportError err = parser.parse(preview).iterator().next();

		Assert.assertEquals(";123;A2;Another Product;piece;;Highly rated product by consumers;approved;1230123456789;A Company;3",
				err.getScriptLine());
	}
}
