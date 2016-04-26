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
package de.hybris.platform.ycommercewebservices.jaxb;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.metadata.MetadataSource;


/**
 * MoxyJaxbContextFactoryImpl is a factory that creates JaxbContext using a Moxy implementation of JAXB. The context is
 * created for a given set of classes. The factory finally adds to this context also some global classes (provided in
 * otherClasses list).
 */
public class MoxyJaxbContextFactoryImpl implements JaxbContextFactory
{
	private static Set<Class> baseExcludeClasses;

	private List<Class> excludeClasses = new ArrayList<>();
	private List<Class> otherClasses = new ArrayList<>();
	private List<Class> typeAdapters = new ArrayList<>();
	private Boolean wrapCollections;
	private int wrapDepth;


	@Override
	public JAXBContext createJaxbContext(final Class... classes) throws JAXBException
	{
		final Map<String, Object> properties = new HashMap<String, Object>();
		final List<MetadataSource> mappings = new ArrayList();
		final Set<Class> allClasses = new HashSet<Class>();

		for (final Class clazz : classes)
		{
			final List<Class> classesInHierarchy = getAllSuperClasses(clazz);
			allClasses.addAll(classesInHierarchy);
			final Set<Class> classesFromFields = getInnerFields(clazz, getWrapDepth());
			allClasses.addAll(classesFromFields);
		}

		allClasses.removeAll(baseExcludeClasses);
		allClasses.removeAll(excludeClasses);

		for (final Class clazz : allClasses)
		{
			final MetadataSource ms = new WsDTOGenericMetadataSourceAdapter<>(clazz, typeAdapters, wrapCollections);
			mappings.add(ms);
		}

		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, mappings);

		Class[] otherClassesArray = new Class[otherClasses.size()];
		otherClassesArray = otherClasses.toArray(otherClassesArray);
		final JAXBContext jaxbContext = JAXBContextFactory.createContext(otherClassesArray, properties);

		return jaxbContext;
	}



	protected static List<Class> getAllSuperClasses(final Class clazz)
	{
		final List<Class> classList = new ArrayList<Class>();

		Class currentClass = clazz;
		while (currentClass != null && !currentClass.equals(Object.class))
		{
			classList.add(currentClass);
			currentClass = currentClass.getSuperclass();
		}
		return classList;
	}

	protected static Set<Class> getInnerFields(final Class clazz, final int depth)
	{
		final Set<Class> result = new HashSet<Class>();
		if (depth <= 0)
		{
			return result;
		}

		final Set<Class> visitedClesses = new HashSet<Class>();
		final Queue<Tuple2<Class, Integer>> workQueue = new LinkedList<MoxyJaxbContextFactoryImpl.Tuple2<Class, Integer>>();

		workQueue.add(new Tuple2<Class, Integer>(clazz, Integer.valueOf(0)));

		while (!workQueue.isEmpty())
		{
			final Tuple2<Class, Integer> item = workQueue.poll();
			final Class itemClass = item.getFirst();
			//work on one class only once
			if (visitedClesses.contains(itemClass))
			{
				continue;
			}


			final int itemDepth = item.getSecond().intValue();
			final Field[] fields = itemClass.getDeclaredFields();

			for (final Field field : fields)
			{
				final Class fieldClass = field.getType();
				if (Collection.class.isAssignableFrom(fieldClass))
				{
					final ParameterizedType pt = (ParameterizedType) field.getGenericType();
					final Type[] typesInside = pt.getActualTypeArguments();
					if (typesInside.length == 1 && typesInside[0] instanceof Class)
					{
						result.add((Class) typesInside[0]);
					}
				}

				if (itemDepth < depth)
				{
					workQueue.add(new Tuple2<Class, Integer>(field.getClass(), Integer.valueOf(itemDepth + 1)));
				}
			}

			visitedClesses.add(itemClass);
		}

		return result;
	}

	public List<Class> getOtherClasses()
	{
		return otherClasses;
	}

	public void setOtherClasses(final List<Class> otherClasses)
	{
		this.otherClasses = otherClasses;
	}

	public List<Class> getTypeAdapters()
	{
		return typeAdapters;
	}

	public void setTypeAdapters(final List<Class> typeAdapters)
	{
		this.typeAdapters = typeAdapters;
	}

	public Boolean getWrapCollections()
	{
		return wrapCollections;
	}

	public void setWrapCollections(final Boolean wrapCollections)
	{
		this.wrapCollections = wrapCollections;
	}

	public int getWrapDepth()
	{
		return wrapDepth;
	}

	public void setWrapDepth(final int wrapDepth)
	{
		this.wrapDepth = wrapDepth;
	}

	public List<Class> getExcludeClasses()
	{
		return excludeClasses;
	}

	public void setExcludeClasses(final List<Class> excludeClasses)
	{
		this.excludeClasses = excludeClasses;
	}

	public static class Tuple2<First, Second>
	{
		First first;
		Second second;

		public Tuple2(final First first, final Second second)
		{
			this.first = first;
			this.second = second;
		}

		public First getFirst()
		{
			return first;
		}

		public Second getSecond()
		{
			return second;
		}
	}

	static
	{
		//never try to declare custom wrappers for those classes
		//bad things will happen if you do
		final Set<Class> classes = new HashSet<Class>();
		classes.add(String.class);
		classes.add(Character.class);
		classes.add(Byte.class);
		classes.add(Short.class);
		classes.add(Integer.class);
		classes.add(Long.class);
		classes.add(Float.class);
		classes.add(Double.class);
		classes.add(Boolean.class);
		classes.add(Void.class);

		baseExcludeClasses = Collections.unmodifiableSet(classes);
	}

}