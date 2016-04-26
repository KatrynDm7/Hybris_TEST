package de.hybris.y2ysync.services.impl;

import static de.hybris.y2ysync.ColumnDefinitionsAssert.assertThat;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.services.SyncConfigService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


public class DefaultSyncConfigServiceTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private TypeService typeService;
	@Resource(name = "syncConfigService")
	private SyncConfigService service;
	@Resource
	private ModelService modelService;
	@Resource
	private CommonI18NService commonI18NService;
	private LanguageModel currentLang;

	@Before
	public void setUp() throws Exception
	{
		currentLang = commonI18NService.getCurrentLanguage();
	}

	@Test
	public void shouldCreateColumnDefintionsBasedOnUnlocalizedAttributeDescriptor() throws Exception
	{
		// given
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor("Title", "code");
		final HashSet<AttributeDescriptorModel> attrDescriptors = Sets.newHashSet(attributeDescriptor);
		final Y2YStreamConfigurationContainerModel container = service.createStreamConfigurationContainer("testId");

		// when
		final Y2YStreamConfigurationModel configuration = service.createStreamConfiguration(container, "Title", attrDescriptors,
				Collections.emptySet());
		modelService.save(configuration);

		// then
		Assertions.assertThat(configuration).isNotNull();
		assertThat(configuration.getColumnDefinitions()).hasSize(1) //
				.containsDefintionFor(attributeDescriptor) //
				.withoutLanguage() //
				.withColumnName("code") //
				.withImpexHeader("code[unique=true]");
	}

	@Test
	public void shouldCreateColumnDefintionsBasedOnLocalizedAttributeDescriptor() throws Exception
	{
		// given
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor("Title", "name");
		final HashSet<AttributeDescriptorModel> attrDescriptors = Sets.newHashSet(attributeDescriptor);
		final Y2YStreamConfigurationContainerModel container = service.createStreamConfigurationContainer("testId");

		// when
		final Y2YStreamConfigurationModel configuration = service.createStreamConfiguration(container, "Title", attrDescriptors,
				Collections.emptySet());
		modelService.save(configuration);

		// then
		Assertions.assertThat(configuration).isNotNull();
		assertThat(configuration.getColumnDefinitions()).hasSize(1) //
				.containsDefintionFor(attributeDescriptor) //
				.withLanguage(currentLang) //
				.withColumnName("name_en") //
				.withImpexHeader("name[lang=en]");
	}

	@Test
	public void shouldCreateDefaultColumnDefintionsForComposedType() throws Exception
	{
		// given
		final ComposedTypeModel titleComposedType = typeService.getComposedTypeForClass(TitleModel.class);

		// when
		final List<Y2YColumnDefinitionModel> defaultColumnDefinitions = service.createDefaultColumnDefinitions(titleComposedType);

		// then
		assertThat(defaultColumnDefinitions).hasSize(5);
		assertThat(defaultColumnDefinitions).containsDefintionFor(titleDescriptor("owner")) //
				.withoutLanguage() //
				.withColumnName("owner") //
				.withImpexHeader("owner()");
		assertThat(defaultColumnDefinitions).containsDefintionFor(titleDescriptor("creationtime")) //
				.withoutLanguage() //
				.withColumnName("creationtime") //
				.withImpexHeader("creationtime[dateformat=dd.MM.yyyy hh:mm:ss]");
		assertThat(defaultColumnDefinitions).containsDefintionFor(titleDescriptor("name")) //
				.withLanguage(currentLang) //
				.withColumnName("name_en") //
				.withImpexHeader("name[lang=en]");
		assertThat(defaultColumnDefinitions).containsDefintionFor(titleDescriptor("modifiedtime")) //
				.withoutLanguage() //
				.withColumnName("modifiedtime") //
				.withImpexHeader("modifiedtime[dateformat=dd.MM.yyyy hh:mm:ss]");
		assertThat(defaultColumnDefinitions).containsDefintionFor(titleDescriptor("code")) //
				.withoutLanguage() //
				.withColumnName("code") //
				.withImpexHeader("code[unique=true]");
	}

	private AttributeDescriptorModel titleDescriptor(final String qualifier)
	{
		return typeService.getAttributeDescriptor("Title", qualifier);
	}


}
