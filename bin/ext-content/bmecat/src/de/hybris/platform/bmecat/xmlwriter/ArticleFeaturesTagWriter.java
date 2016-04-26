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
import de.hybris.platform.bmecat.xmlwriter.FeatureTagWriter.FeatureContext;
import de.hybris.platform.bmecat.xmlwriter.VariantTagWriter.VariantContext;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystem;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValue;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.variants.jalo.VariantAttributeDescriptor;
import de.hybris.platform.variants.jalo.VariantProduct;
import de.hybris.platform.variants.jalo.VariantType;
import de.hybris.platform.variants.jalo.VariantsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;ArticleFeatures&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class ArticleFeaturesTagWriter extends XMLTagWriter
{
	public static final char CLASSIFICATION_DELIMITER = '-'; // XXX

	/**
	 * @param parent
	 */
	public ArticleFeaturesTagWriter(final ArticleTagWriter parent)
	{
		super(parent);
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.REFERENCE_FEATURE_SYSTEM_NAME));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.REFERENCE_FEATURE_GROUP_ID));
		addSubTagWriter(new FeatureTagWriter(this));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_FEATURES;
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#writeContent(org.znerd.xmlenc.XMLOutputter,
	 *      java.lang.Object)
	 */
	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final BMECatExportContext exportCtx = (BMECatExportContext) object;
		final ClassificationSystemVersion classificationSystemVersion = exportCtx.getClassificationSystemVersion();
		final Category cat = exportCtx.getCategory();
		final Product product = exportCtx.getProduct();

		if (classificationSystemVersion != null)
		{
			final ClassificationClass cclass = (ClassificationClass) cat;
			final ClassificationSystem classSystem = (ClassificationSystem) classificationSystemVersion.getCatalog();
			final StringBuilder refFeatSysName = new StringBuilder(classSystem.getId().toLowerCase());

			refFeatSysName.append(CLASSIFICATION_DELIMITER);
			refFeatSysName.append(classificationSystemVersion.getVersion());
			getSubTagWriter(BMECatConstants.XML.TAG.REFERENCE_FEATURE_SYSTEM_NAME).write(xmlOut, refFeatSysName.toString());

			final String referenceFeatureGroupId = cclass.getCode();
			getSubTagWriter(BMECatConstants.XML.TAG.REFERENCE_FEATURE_GROUP_ID).write(xmlOut, referenceFeatureGroupId);

			// load all feature value of this classification class
			final FeatureContainer cont = FeatureContainer.loadTyped(product, Collections.singleton(cclass));
			for (final ClassAttributeAssignment asgnmt : cclass.getClassificationAttributeAssignments())
			{
				final ClassificationAttribute classificationAttribute = asgnmt.getClassificationAttribute();
				//PIM-361: localized name instead of code
				final String keyStr = classificationAttribute.getName();
				// getValueDirect() automatically converts number values into the feature unit 
				final List values = cont.getFeature(asgnmt).getValuesDirect();
				final FeatureContext fCtx = new FeatureContext(keyStr, asgnmt.getAttributeType(), new ArrayList(values).toArray(),
				// TODO better use assignment NumberFormat ??? 
						exportCtx.getClassificationNumberForamt());
				// set order
				fCtx.setOrder(Integer.valueOf(asgnmt.getPositionAsPrimitive()));
				// set unit
				if (asgnmt.getUnit() != null)
				{
					fCtx.setUnit(asgnmt.getUnit().getSymbol());
				}
				// append descriptions
				final StringBuilder desciptions = new StringBuilder();
				for (final FeatureValue fv : cont.getFeature(asgnmt).getValues())
				{
					if (fv.getDescription() != null)
					{
						if (desciptions.length() > 0)
						{
							desciptions.append(",");
						}
						desciptions.append(fv.getDescription());
					}
				}
				if (desciptions.length() > 0)
				{
					fCtx.setDescription(desciptions.toString());
					// TODO append value details
					//				StringBuilder valueDetails = new StringBuilder();
					//				for( FeatureValue fv : cont.getFeature(asgnmt).getValues())
					//				{
					//					if( fv.getValueDetails() != null )
					//					{
					//						if( valueDetails.length() > 0 ) valueDetails.append(",");
					//						valueDetails.append(fv.getValueDetails());
					//					}
					//				}
					//				if( valueDetails.length() > 0 ) fCtx.setValueDetails(valueDetails.toString());
				}

				// do output
				getSubTagWriter(BMECatConstants.XML.TAG.FEATURE).write(xmlOut, fCtx);
			}

			//			for( final Iterator it = getCatalogManager().getFeatures( product ).iterator(); it.hasNext(); )
			//			{
			//			   final ProductFeature feature = (ProductFeature)it.next();
			//			   
			//				final String key = feature.getQualifier();
			//				if( key.toUpperCase().startsWith( refFeatSysName.toString().toUpperCase() ) )
			//				{
			//					final int underlineIndex = key.indexOf( '_' );
			//					final String featureName = key.substring( underlineIndex + 1 );
			//					
			//					final FeatureContext featureCtx = new FeatureContext( featureName,
			//					      (String [])feature.getValues().toArray(new String[feature.getValues().size()]));
			//					featureCtx.setUnit( getCode(feature.getUnit()) );
			//					getSubTagWriter( BMECatConstants.XML.TAG.FEATURE ).write( xmlOut, featureCtx );
			//				}
			//			}
		}
		//		else
		//		{
		//		   int i=0;
		//			for( final Iterator it = getCatalogManager().getFeatures( product ).iterator(); it.hasNext(); i++)
		//			{
		//			   final ProductFeature feature = (ProductFeature)it.next();
		//			   final String key = feature.getQualifier();
		//			   if( ! key.toUpperCase().startsWith( AbstractEclassImportStep.CATALOG_ECLASS.toUpperCase() )
		//						&& ! key.toUpperCase().startsWith( AbstractEtimImportStep.ETIM.toUpperCase() )
		//						&& ! key.toUpperCase().startsWith( UnspscImportStep.UNSPSC.toUpperCase() ) )
		//				{
		//					final FeatureContext featureCtx = new FeatureContext( key,
		//					      (Object [])feature.getValues().toArray(new Object[feature.getValues().size()]));
		//					featureCtx.setUnit( getCode(feature.getUnit()) );
		//					featureCtx.setDescription( feature.getDescription() );
		//					featureCtx.setValueDetails( feature.getValueDetails() );
		//					featureCtx.setOrder( new Integer(i) );
		//					getSubTagWriter( BMECatConstants.XML.TAG.FEATURE ).write( xmlOut, featureCtx );					
		//				}
		//			}

		if (getVariantsManager().isBaseProduct(product))
		{
			int vOrder = 0;
			final VariantType variantType = getVariantsManager().getVariantType(product);
			for (final Iterator descrIt = variantType.getVariantAttributes().iterator(); descrIt.hasNext();)
			{
				final VariantAttributeDescriptor attrDescr = (VariantAttributeDescriptor) descrIt.next();
				final List variantContexts = new ArrayList();

				final String unit = null;
				vOrder++;
				final Collection fValues = new HashSet();
				for (final Iterator it = getAssignedAttributeValues(product, attrDescr).iterator(); it.hasNext();)
				{
					final DefaultKeyValue keyValue = (DefaultKeyValue) it.next();
					final VariantProduct variantProduct = (VariantProduct) keyValue.getKey();
					final Object rawValue = keyValue.getValue();
					final String value;
					if (fValues.add(rawValue))
					{
						if (rawValue instanceof EnumerationValue)
						{
							value = ((EnumerationValue) rawValue).getCode();
						}
						else
						{
							value = rawValue.toString();
						}
						final String supplierAidSupplement = getSupplierAidSupplement(variantProduct, rawValue);
						variantContexts.add(new VariantContext(value, supplierAidSupplement));
					}
				}

				final VariantsTagWriter.VariantsContext vCtx = new VariantsTagWriter.VariantsContext(variantContexts, vOrder);
				final FeatureContext featureCtx = new FeatureContext(attrDescr.getQualifier(), vCtx);
				featureCtx.setUnit(unit);
				getSubTagWriter(BMECatConstants.XML.TAG.FEATURE).write(xmlOut, featureCtx);
			}
		}
		//}
	}

	protected String getCode(final Unit unit)
	{
		return unit != null ? unit.getCode() : null;
	}

	/**
	 * Is used to get the SUPPLIER_AID_SUPPLEMENT value for a given <code>VariantProduct</code> and variant value.
	 * Overwrite this method to implement your own way to get this value.
	 * 
	 * @param variantProduct
	 *           a <code>VariantProduct</code>
	 * @param rawValue
	 *           the variant value
	 * @return the BMECat SUPPLIER_AID_SUPPLEMENT value
	 */
	protected String getSupplierAidSupplement(final VariantProduct variantProduct, final Object rawValue)
	{
		final String value;
		if (rawValue instanceof EnumerationValue)
		{
			value = ((EnumerationValue) rawValue).getCode();
		}
		else
		{
			value = rawValue.toString();
		}

		return value;
	}

	private Collection getAssignedAttributeValues(final Product product,
			final VariantAttributeDescriptor variantAttributeDescriptor)
	{
		final Collection values = new HashSet();
		for (final Iterator it = getVariantsManager().getVariants(product).iterator(); it.hasNext();)
		{
			final VariantProduct variant = (VariantProduct) it.next();
			try
			{
				values.add(new DefaultKeyValue(variant, variant.getAttribute(variantAttributeDescriptor.getQualifier())));
			}
			catch (final JaloInvalidParameterException e)
			{
				e.printStackTrace();
			}
			catch (final JaloSecurityException e)
			{
				e.printStackTrace();
			}
		}
		return values;
	}

	private VariantsManager getVariantsManager()
	{
		return VariantsManager.getInstance();
	}


}
