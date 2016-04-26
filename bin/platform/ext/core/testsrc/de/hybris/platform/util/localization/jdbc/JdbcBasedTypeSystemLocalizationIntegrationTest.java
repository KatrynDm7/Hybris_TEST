/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.util.localization.jdbc;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.TestEnum;
import de.hybris.platform.core.model.test.TestItemModel;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.localization.jdbc.executor.BatchedStatementsExecutor;
import de.hybris.platform.util.localization.jdbc.info.JaloBasedDbInfo;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;


@IntegrationTest
public class JdbcBasedTypeSystemLocalizationIntegrationTest extends ServicelayerBaseTest
{
	private static final String TEST_TYPECODE = TestItemModel._TYPECODE.toLowerCase();
	private static final String TEST_TYPE_NAME_KEY = "type." + TEST_TYPECODE + ".name";
	private static final String TEST_TYPE_DESCRIPTION_KEY = "type." + TEST_TYPECODE + ".description";

	private static final String TEST_ATTRIBUTE_QUALIFIER = TestItemModel.DOUBLE.toLowerCase();
	private static final String TEST_ATTRIBUTE_NAME_KEY = "type." + TEST_TYPECODE + "." + TEST_ATTRIBUTE_QUALIFIER + ".name";
	private static final String TEST_ATTRIBUTE_DESCRIPTION_KEY = "type." + TEST_TYPECODE + "." + TEST_ATTRIBUTE_QUALIFIER
			+ ".description";

	private static final String TEST_ENUM_TYPECODE = TestEnum._TYPECODE.toLowerCase();
	private static final String TEST_ENUM_VALUE_CODE = TestEnum.TESTVALUE2.getCode().toLowerCase();
	private static final String TEST_ENUM_VALUE_NAME_KEY = "type." + TEST_ENUM_TYPECODE + "." + TEST_ENUM_VALUE_CODE + ".name";

	@Resource
	private DataSource dataSource;

	@Resource
	private TypeService typeService;

	@Resource
	private ModelService modelService;

	private Language currentLanguage;
	private JaloBasedDbInfo dbInfo;


	@Before
	public void setUp()
	{
		currentLanguage = Registry.getCurrentTenantNoFallback().getActiveSession().getSessionContext().getLanguage();
		dbInfo = new JaloBasedDbInfo();
	}

	@Test
	public void shouldSeeChangesAfterTypeNameChange()
	{
		//given
		final String nameBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		final String newName = concat(nameBefore, "CN");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_TYPE_NAME_KEY, newName));

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		assertThat(nameAfter).isNotNull().isNotEqualTo(nameBefore).isEqualTo(newName);
	}

	@Test
	public void shouldSeeChangesAfterTypeDescriptionChange()
	{
		//given
		final String descriptionBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		final String newDescription = concat(descriptionBefore, "CD");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_TYPE_DESCRIPTION_KEY, newDescription));

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String descriptionAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		assertThat(descriptionAfter).isNotNull().isNotEqualTo(descriptionBefore).isEqualTo(newDescription);
	}

	@Test
	public void shouldSeeChangesWhenBothNameAndDescriptionOfTypeHaveCahnged()
	{
		//given
		final String nameBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		final String descriptionBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		final String newName = concat(nameBefore, "CN");
		final String newDescription = concat(descriptionBefore, "CD");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(),
				ImmutableMap.<String, String> of(TEST_TYPE_NAME_KEY, newName, TEST_TYPE_DESCRIPTION_KEY, newDescription));

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		final String descriptionAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		assertThat(nameAfter).isNotNull().isNotEqualTo(nameBefore).isEqualTo(newName);
		assertThat(descriptionAfter).isNotNull().isNotEqualTo(descriptionBefore).isEqualTo(newDescription);
	}

	@Test
	public void shouldNotChangeTypeNameOrDescriptionWhenNoLocalizationIsGiven()
	{
		//given
		final String nameBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		final String descriptionBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(),
				ImmutableMap.<String, String> of());

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		final String descriptionAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		assertThat(nameAfter).isEqualTo(nameBefore);
		assertThat(descriptionAfter).isEqualTo(descriptionBefore);
	}

	@Test
	public void shouldSeeChangesAfterAttributeNameChange()
	{
		//given
		final String nameBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		final String newName = concat(nameBefore, "CN");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_ATTRIBUTE_NAME_KEY, newName));

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		assertThat(nameAfter).isNotNull().isNotEqualTo(nameBefore).isEqualTo(newName);
	}

	@Test
	public void shouldSeeChangesAfterAttributeDescriptionChange()
	{
		//given
		final String descriptionBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		final String newDescription = concat(descriptionBefore, "CD");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_ATTRIBUTE_DESCRIPTION_KEY, newDescription));

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String descriptionAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		assertThat(descriptionAfter).isNotNull().isNotEqualTo(descriptionBefore).isEqualTo(newDescription);
	}

	@Test
	public void shouldSeeChangesWhenBothNameAndDescriptionOfAttributeHaveCahnged()
	{
		//given
		final String nameBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		final String descriptionBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		final String newName = concat(nameBefore, "CN");
		final String newDescription = concat(descriptionBefore, "CD");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(),
				ImmutableMap.<String, String> of(TEST_ATTRIBUTE_NAME_KEY, newName, TEST_ATTRIBUTE_DESCRIPTION_KEY, newDescription));

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		final String descriptionAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		assertThat(nameAfter).isNotNull().isNotEqualTo(nameBefore).isEqualTo(newName);
		assertThat(descriptionAfter).isNotNull().isNotEqualTo(descriptionBefore).isEqualTo(newDescription);
	}

	@Test
	public void shouldNotChangeAttributeNameOrDescriptionWhenNoLocalizationIsGiven()
	{
		//given
		final String nameBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		final String descriptionBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(),
				ImmutableMap.<String, String> of());

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		final String descriptionAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		assertThat(nameAfter).isEqualTo(nameBefore);
		assertThat(descriptionAfter).isEqualTo(descriptionBefore);
	}

	@Test
	public void shouldSeeChangesAfterEnumValueNameChange()
	{
		//given
		final String nameBefore = typeService.getEnumerationValue(TEST_ENUM_TYPECODE, TEST_ENUM_VALUE_CODE).getName();
		final String newName = concat(nameBefore, "CN");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_ENUM_VALUE_NAME_KEY, newName));

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getEnumerationValue(TEST_ENUM_TYPECODE, TEST_ENUM_VALUE_CODE).getName();
		assertThat(nameAfter).isNotNull().isNotEqualTo(nameBefore).isEqualTo(newName);
	}

	@Test
	public void shouldNotChangeEnumValueNameWhenNoLocalizationIsGiven()
	{
		//given
		final String nameBefore = typeService.getEnumerationValue(TEST_ENUM_TYPECODE, TEST_ENUM_VALUE_CODE).getName();
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(),
				ImmutableMap.<String, String> of());

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getEnumerationValue(TEST_ENUM_TYPECODE, TEST_ENUM_VALUE_CODE).getName();
		assertThat(nameAfter).isEqualTo(nameBefore);
	}

	private String concat(final String s, final String suffix)
	{
		Preconditions.checkNotNull(suffix);
		return (s == null ? "" : s) + suffix;
	}

	private void localizeTypeSystem(final LocalizationInfo localizationInfo)
	{
		modelService.detachAll();
		final ExecutorService executorService = MoreExecutors.sameThreadExecutor();
		final BatchedStatementsExecutor statementsExecutor = new BatchedStatementsExecutor(executorService, dataSource);
		new JdbcBasedTypeSystemLocalization(executorService, dataSource, dbInfo, localizationInfo, statementsExecutor)
				.localizeTypeSystem();
	}

	private static class TestLocalizationInfo implements LocalizationInfo
	{
		private final Map<String, String> localizations;
		private final PK languagePK;

		public TestLocalizationInfo(final PK languagePK, final Map<String, String> localizations)
		{
			Preconditions.checkNotNull(languagePK);
			Preconditions.checkNotNull(localizations);

			this.languagePK = languagePK;
			this.localizations = localizations;
		}

		@Override
		public Collection<PK> getLanguagePKs()
		{
			return ImmutableSet.of(languagePK);
		}

		@Override
		public String getLocalizedProperty(final PK languagePK, final String propertyKey)
		{
			if (!this.languagePK.equals(languagePK))
			{
				return null;
			}
			return localizations.get(propertyKey);
		}

	}
}
