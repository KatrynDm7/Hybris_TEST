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
package de.hybris.platform.chinaaccelerator.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.ProductDescriptionPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;


public class EnhancedProductDescriptionPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		ProductDescriptionPopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		productData.setDescription(resolveImagesRef(safeToString(getProductAttribute(productModel, ProductModel.DESCRIPTION)),
				productModel));
	}

	/**
	 * @param rawDescription
	 * @param productModel
	 * @return
	 */
	private String resolveImagesRef(final String rawDescription, final SOURCE productModel)
	{
		final ExpressionParser parser = new SpelExpressionParser();

		final StandardEvaluationContext sec = new StandardEvaluationContext();
		sec.setVariable("detailMedia", productModel.getDetail());

		final TemplateParserContext ctx = new TemplateParserContext();
		final String result = parser.parseExpression(rawDescription, ctx).getValue(sec, String.class);

		return result;
	}

	public static void main(final String[] args)
	{
		final ExpressionParser parser = new SpelExpressionParser();

		final StandardEvaluationContext sec = new StandardEvaluationContext();
		final Collection<String> c = new ArrayList<>();
		c.add("0123456");
		c.add("1234567");
		sec.setVariable("v1", c);
		final TemplateParserContext ctx = new TemplateParserContext();

		final String randomPhrase = parser.parseExpression("random number is #{#v1[1].length()}", ctx).getValue(sec, String.class);

		System.out.println(randomPhrase);

	}
}
