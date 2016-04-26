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
package de.hybris.platform.commercewebservicescommons.mapping.impl;

import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetBuilder;
import de.hybris.platform.commercewebservicescommons.mapping.WsDTOMapping;
import de.hybris.platform.commercewebservicescommons.mapping.config.FieldMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ma.glasnost.orika.*;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Implementation of {@link de.hybris.platform.commercewebservicescommons.mapping.DataMapper} used for mapping
 * commercefacade's Data objects into WsDTOs. <br/>
 * It automatically discovers and registers managed beans of type {@link ma.glasnost.orika.Mapper},
 * {@link ma.glasnost.orika.Converter} or {@link ma.glasnost.orika.Filter} annotated with
 * {@link de.hybris.platform.commercewebservicescommons.mapping.WsDTOMapping}.
 */
public class DefaultDataMapper extends ConfigurableMapper implements DataMapper, ApplicationContextAware
{
	private static final boolean DEFAULT_MAP_NULLS = false;
	private final MappingContextFactory mappingContextFactory = new MappingContext.Factory();
	private MapperFactory factory;
	private FieldSetBuilder fieldSetBuilder;
	private ApplicationContext applicationContext;

	public DefaultDataMapper()
	{
		super(false);
	}

	@Override
	protected void configureFactoryBuilder(final DefaultMapperFactory.Builder factoryBuilder)
	{
		factoryBuilder.captureFieldContext(true);
	}

	@Override
	protected void configure(final MapperFactory factory)
	{
		this.factory = factory;
		addAllSpringBeans();
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
		init();
	}

	/**
	 * Registers all managed beans of type {@link ma.glasnost.orika.Mapper}, {@link ma.glasnost.orika.Converter} or
	 * {@link ma.glasnost.orika.Filter} and annotated with
	 * {@link de.hybris.platform.commercewebservicescommons.mapping.WsDTOMapping}.
	 */
	protected void addAllSpringBeans()
	{
		final Map<String, Object> beans = applicationContext.getBeansWithAnnotation(WsDTOMapping.class);

		for (final Object bean : beans.values())
		{
			if (bean instanceof Converter)
			{
				addConverter((Converter) bean);
			}
			else if (bean instanceof Mapper)
			{
				addMapper((Mapper) bean);
			}
			else if (bean instanceof Filter)
			{
				addFilter((Filter) bean);
			}
		}

		final Map<String, FieldMapper> fieldMappers = applicationContext.getBeansOfType(FieldMapper.class);
		for (final FieldMapper mapper : fieldMappers.values())
		{
			addFieldMapper(mapper);
		}
	}

	/**
	 * Registers a {@link ma.glasnost.orika.Converter}.
	 * 
	 * @param converter
	 *           The converter.
	 */
	public void addConverter(final Converter<?, ?> converter)
	{
		factory.getConverterFactory().registerConverter(converter);
	}

	/**
	 * Registers a {@link ma.glasnost.orika.Mapper}.
	 * 
	 * @param mapper
	 *           The mapper.
	 */
	public void addMapper(final Mapper<?, ?> mapper)
	{
		factory.classMap(mapper.getAType(), mapper.getBType()).byDefault().customize((Mapper) mapper).register();
	}

	/**
	 * Registers a {@link ma.glasnost.orika.Filter}.
	 *
	 * @param filter
	 *           The filter.
	 */
	public void addFilter(final Filter<?, ?> filter)
	{
		factory.registerFilter(filter);
	}

	/**
	 * Registers a {@link ma.glasnost.orika.metadata.ClassMap} with field mapping given by fieldMapper object
	 * 
	 * @param fieldMapper
	 *           Object storing field mapping information.
	 */
	public void addFieldMapper(final FieldMapper fieldMapper)
	{
		ClassMapBuilder mapBuilder = null;
		if ((fieldMapper.getSourceClassArguments() != null && !fieldMapper.getSourceClassArguments().isEmpty())
				|| (fieldMapper.getDestClassArguments() != null && !fieldMapper.getDestClassArguments().isEmpty()))
		{
			final Type sourceType = TypeFactory.valueOf(fieldMapper.getSourceClass(), fieldMapper.getSourceActualTypeArguments());
			final Type destType = TypeFactory.valueOf(fieldMapper.getDestClass(), fieldMapper.getDestActualTypeArguments());
			mapBuilder = factory.classMap(sourceType, destType);
		}
		else
		{
			mapBuilder = factory.classMap(fieldMapper.getSourceClass(), fieldMapper.getDestClass());
		}

		if (fieldMapper.getFieldMapping() != null && !fieldMapper.getFieldMapping().isEmpty())
		{
			for (final Map.Entry<String, String> entry : fieldMapper.getFieldMapping().entrySet())
			{
				mapBuilder.field(entry.getKey(), entry.getValue());
			}
		}

		factory.registerClassMap(mapBuilder.byDefault().toClassMap());
	}

	@Override
	public <S, D> D map(S sourceObject, Class<D> destinationClass)
	{
		return super.map(sourceObject, destinationClass);
	}

	@Override
	public <S, D> D map(final S sourceObject, final Class<D> destinationClass, final String fields)
	{
		return super.map(sourceObject, destinationClass, createMappingContext(destinationClass, fields));
	}

	@Override
	public <S, D> D map(final S sourceObject, final Class<D> destinationClass, final Set<String> fields)
	{
		return super.map(sourceObject, destinationClass, createMappingContext(fields));
	}

	@Override
	public <S, D> void map(S sourceObject, D destinationObject, String fields)
	{
		super.map(sourceObject, destinationObject, createMappingContext(destinationObject.getClass(), fields));
	}

	@Override
	public <S, D> void map(final S sourceObject, final D destinationObject, final String fields, final boolean mapNulls)
	{
		super.map(sourceObject, destinationObject, createMappingContext(destinationObject.getClass(), fields, mapNulls));
	}

	@Override
	public <S, D> void map(S sourceObject, D destinationObject)
	{
		super.map(sourceObject, destinationObject);
	}

	@Override
	public <S, D> void map(final S sourceObject, final D destinationObject, final boolean mapNulls)
	{
		super.map(sourceObject, destinationObject, createMappingContext(destinationObject.getClass(), null, mapNulls));
	}

	@Override
	public <S, D> List<D> mapAsList(final Iterable<S> source, final Class<D> destinationClass, final String fields)
	{
		return super.mapAsList(source, destinationClass, createMappingContext(destinationClass, fields));
	}

	@Override
	public <S, D> Set<D> mapAsSet(final Iterable<S> source, final Class<D> destinationClass, final String fields)
	{
		return super.mapAsSet(source, destinationClass, createMappingContext(destinationClass, fields));
	}

	@Override
	public <S, D> void mapAsCollection(final Iterable<S> source, final Collection<D> destination, final Class<D> destinationClass,
			final String fields)
	{
		mapAsCollection(source, destination, destinationClass, createMappingContext(destinationClass, fields));
	}

	@Override
	public <S, D> void mapGeneric(final S sourceObject, final D destObject,
			final java.lang.reflect.Type[] sourceActualTypeArguments, final java.lang.reflect.Type[] destActualTypeArguments,
			final String fields, final Map<String, Class> destTypeVariableMap)
	{
		final Type sourceType = TypeFactory.valueOf(sourceObject.getClass(), sourceActualTypeArguments);
		final Type destType = TypeFactory.valueOf(destObject.getClass(), destActualTypeArguments);
		map(sourceObject, destObject, sourceType, destType,
				createMappingContextForGeneric(destObject.getClass(), fields, destTypeVariableMap));
	}

	protected MappingContext createMappingContext(final Set<String> fields)
	{
		final MappingContext context = mappingContextFactory.getContext();
		if (fields != null)
			context.setProperty(FIELD_SET_NAME, fields);
		return context;
	}

	protected MappingContext createMappingContext(final Class destinationClass, final String fields)
	{
		return createMappingContext(destinationClass, fields, DEFAULT_MAP_NULLS);
	}

	protected MappingContext createMappingContext(final Class destinationClass, final String fields, final boolean mapNulls)
	{
		final MappingContext context = mappingContextFactory.getContext();
		if (fields != null)
		{
			final Set<String> propertySet = fieldSetBuilder.createFieldSet(destinationClass, FIELD_PREFIX, fields);
			context.setProperty(FIELD_SET_NAME, propertySet);
		}
		context.setProperty(MAP_NULLS, Boolean.valueOf(mapNulls));
		return context;
	}

	protected MappingContext createMappingContextForGeneric(final Class destinationClass, final String fields,
			final Map<String, Class> typeVariableMap)
	{
		final MappingContext context = mappingContextFactory.getContext();
		if (fields != null)
		{
			final FieldSetBuilderContext fieldSetBuilderContext = new FieldSetBuilderContext();
			fieldSetBuilderContext.setTypeVariableMap(typeVariableMap);
			final Set<String> propertySet = fieldSetBuilder.createFieldSet(destinationClass, FIELD_PREFIX, fields,
					fieldSetBuilderContext);
			context.setProperty(FIELD_SET_NAME, propertySet);
		}
		return context;
	}

	@Required
	public void setFieldSetBuilder(final FieldSetBuilder fieldSetBuilder)
	{
		this.fieldSetBuilder = fieldSetBuilder;
	}
}
