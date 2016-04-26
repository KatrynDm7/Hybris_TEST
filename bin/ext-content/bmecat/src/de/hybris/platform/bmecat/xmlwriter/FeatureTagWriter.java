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

import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.xmlwriter.VariantsTagWriter.VariantsContext;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.jalo.enumeration.EnumerationValue;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Feature&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class FeatureTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public FeatureTagWriter(final ArticleFeaturesTagWriter parent)
	{
		super(parent);
		this.addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.FNAME, true));
		this.addSubTagWriter(new ClassificationAttributeValueTagWriter(this, BMECatConstants.XML.TAG.FVALUE));
		this.addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.FUNIT));
		this.addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.FDESCR));
		this.addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.FVALUE_DETAILS));
		this.addSubTagWriter(new NumberTagWriter(this, BMECatConstants.XML.TAG.FORDER));
		this.addSubTagWriter(new VariantsTagWriter(this));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.FEATURE;
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final FeatureContext featureCtx = (FeatureContext) object;
		final Object[] values = featureCtx.getValues();
		final VariantsContext variantsCtx = featureCtx.getVariantsContext();

		if (values == null && variantsCtx == null)
		{
			if (isErrorEnabled())
			{
				error("There has to be FVALUE(s) or VARIANTS!!");
			}
			throw new RuntimeException("There has to be FVALUE(s) or VARIANTS!!");
		}

		getSubTagWriter(BMECatConstants.XML.TAG.FNAME).write(xmlOut, featureCtx.getName());

		if (values != null)
		{
			if (featureCtx.getValues().length == 0) // FVALUE is mandatory!!
			{
				getSubTagWriter(BMECatConstants.XML.TAG.FVALUE).write(xmlOut, "-");
			}

			for (int i = 0; i < values.length; i++)
			{
				Object val = featureCtx.getValues()[i];

				if (featureCtx.getType() != null
						&& featureCtx.getType().getCode().equals(CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER))
				{
					final DecimalFormat decimalFormat = new DecimalFormat(featureCtx.getClassificationNumberFormat());
					val = decimalFormat.format(val);
				}

				getSubTagWriter(BMECatConstants.XML.TAG.FVALUE).write(xmlOut, val);
			}
		}

		getSubTagWriter(BMECatConstants.XML.TAG.FUNIT).write(xmlOut, featureCtx.getUnit());
		getSubTagWriter(BMECatConstants.XML.TAG.FDESCR).write(xmlOut, featureCtx.getDescription());
		getSubTagWriter(BMECatConstants.XML.TAG.FVALUE_DETAILS).write(xmlOut, featureCtx.getValueDetails());
		getSubTagWriter(BMECatConstants.XML.TAG.FORDER).write(xmlOut, featureCtx.getOrder());
		if (variantsCtx != null)
		{
			getSubTagWriter(BMECatConstants.XML.TAG.VARIANTS).write(xmlOut, variantsCtx);
		}
	}

	/**
	 * 
	 */
	public static class FeatureContext
	{
		private final String name;
		private final Object[] values;
		private String unit = null;
		private String description = null;
		private String valueDetails = null;
		private Integer order = null;
		private EnumerationValue type = null;
		private final VariantsTagWriter.VariantsContext variantsContext;
		private String classificationNumberFormat = null;

		/**
		 * @param name
		 * @param values
		 */
		public FeatureContext(final String name, final EnumerationValue type, final Object[] values,
				final String classificationNumberFormat)
		{
			this(name, type, values, null, classificationNumberFormat);
		}

		/**
		 * @param name
		 * @param variantsContext
		 */
		public FeatureContext(final String name, final VariantsTagWriter.VariantsContext variantsContext)
		{
			this(name, null, null, variantsContext, null);
		}

		private FeatureContext(final String name, final EnumerationValue type, final Object[] values,
				final VariantsTagWriter.VariantsContext variantsContext, final String classificationNumberFormat)
		{
			this.name = name;
			this.values = values;
			this.variantsContext = variantsContext;
			this.type = type;
			this.classificationNumberFormat = classificationNumberFormat;
		}

		/**
		 * @return the feature name
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * @return the feature unit
		 */
		public String getUnit()
		{
			return unit;
		}

		/**
		 * @return the feature values
		 */
		public Object[] getValues()
		{
			return values;
		}

		/**
		 * @return Returns the description.
		 */
		public String getDescription()
		{
			return description;
		}

		/**
		 * @return Returns the order.
		 */
		public Integer getOrder()
		{
			return order;
		}

		/**
		 * @return Returns the valueDetails.
		 */
		public String getValueDetails()
		{
			return valueDetails;
		}

		/**
		 * @param description
		 *           The description to set.
		 */
		public void setDescription(final String description)
		{
			this.description = description;
		}

		/**
		 * @param order
		 *           The order to set.
		 */
		public void setOrder(final Integer order)
		{
			this.order = order;
		}

		/**
		 * @param unit
		 *           The unit to set.
		 */
		public void setUnit(final String unit)
		{
			this.unit = unit;
		}

		/**
		 * @param valueDetails
		 *           The valueDetails to set.
		 */
		public void setValueDetails(final String valueDetails)
		{
			this.valueDetails = valueDetails;
		}

		/**
		 * @return the VariantsContext
		 */
		public VariantsTagWriter.VariantsContext getVariantsContext()
		{
			return variantsContext;
		}

		public EnumerationValue getType()
		{
			return type;
		}

		public String getClassificationNumberFormat()
		{
			return classificationNumberFormat != null ? classificationNumberFormat : NumberFormat.getInstance().toString();
		}
	}

}
