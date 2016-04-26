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
package de.hybris.platform.validation.model.constraints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.validation.annotations.HybrisDecimalMax;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.interceptors.AttributeConstraintValidator;
import de.hybris.platform.validation.interceptors.TypeConstraintValidator;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.DecimalMaxConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.DigitsConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NotNullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.SizeConstraintModel;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.junit.Test;


@IntegrationTest
public class AttributeConstraintTest extends AbstractConstraintTest
{
	@Test
	public void testAttributeAssignmentForModelFillInFromDescriptor() throws ClassNotFoundException
	{
		final AttributeConstraintModel constraint = modelService.create(AttributeConstraintModel.class);
		constraint.setId("bar constraint");
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForClass(ProductModel.class), ProductModel.CODE);
		constraint.setDescriptor(descrModel);

		try
		{
			modelService.save(constraint);
			validationService.reloadValidationEngine();
		}
		catch (final ModelSavingException mse)
		{
			fail("Should be able to assign attribute descriptor " + descrModel.getItemtype() + "." + descrModel.getQualifier()
					+ " while already target class.property is different " + constraint.getTarget() + "." + constraint.getQualifier());
		}

		final Class targetClass = constraint.getTarget();

		assertEquals("Should assign compatible ct from atrribute descriptor ", constraint.getDescriptor().getEnclosingType()
				.getJaloclass(), constraint.getType().getJaloclass());
		assertEquals("Should assign target clas according to attribute descriptor's ", constraint.getDescriptor()
				.getEnclosingType().getJaloclass(), modelService.getModelTypeClass(targetClass));
		assertEquals("Should assign appropriate qualifier according to attribute descriptor's ", constraint.getDescriptor()
				.getQualifier(), constraint.getQualifier());
	}

	@Test
	public void testAttributeAssignmentEmptyTypeForModel()
	{
		final AttributeConstraintModel constraint = modelService.create(AttributeConstraintModel.class);
		constraint.setId("bar constraint");
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		constraint.setTarget(ProductModel.class);
		constraint.setQualifier(ProductModel.CODE);

		try
		{
			modelService.save(constraint);
			fail("expected ModelSavingException");
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, TypeConstraintValidator.class);
		}
	}

	/**
	 * provided annotation class is not an annotation - case
	 */
	@Test
	public void testAttributeAssignmentNotCorrectAnnotationClass()
	{
		final AttributeConstraintModel constraint = modelService.create(AttributeConstraintModel.class);
		constraint.setId("bar constraint");
		constraint.setAnnotation(String.class);
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForClass(ProductModel.class), ProductModel.CODE);
		constraint.setDescriptor(descrModel);
		try
		{
			modelService.save(constraint);
			fail("expected ModelSavingException");
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, TypeConstraintValidator.class);
		}
	}


	@Test
	public void testAttributeAssignmentForModel() throws ClassNotFoundException
	{
		final AttributeConstraintModel constraint = modelService.create(AttributeConstraintModel.class);
		constraint.setId("bar constraint");
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForClass(ProductModel.class), ProductModel.CODE);
		constraint.setDescriptor(descrModel);

		try
		{
			modelService.save(constraint);
			validationService.reloadValidationEngine();
		}
		catch (final ModelSavingException mse)
		{
			fail("Should be able to assign attribute descriptor " + descrModel.getItemtype() + "." + descrModel.getQualifier()
					+ " while already target class.property is different " + constraint.getTarget() + "." + constraint.getQualifier());
		}

		final Class targetClass = constraint.getTarget();

		assertEquals("Should assign compatible ct from atrribute descriptor ", constraint.getDescriptor().getEnclosingType()
				.getJaloclass(), constraint.getType().getJaloclass());
		assertEquals("Should assign ct according to class literal ", constraint.getDescriptor().getEnclosingType().getJaloclass(),
				modelService.getModelTypeClass(targetClass));
		assertEquals("Should assign appropriate qualifier ", constraint.getDescriptor().getQualifier(), constraint.getQualifier());
	}

	@Test
	public void testAttributeAssignmentIncompatibleQualifier()
	{
		final AttributeConstraintModel constraint = modelService.create(AttributeConstraintModel.class);
		constraint.setId("bar constraint");
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		constraint.setQualifier("otherOne");
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForClass(ProductModel.class), ProductModel.CODE);
		constraint.setDescriptor(descrModel);

		modelService.save(constraint);
		validationService.reloadValidationEngine();
		assertEquals("", ProductModel.CODE, constraint.getQualifier());
	}

	@Test
	public void testAttributeDescriptorOverridesType()
	{
		final AttributeConstraintModel constraint = modelService.create(AttributeConstraintModel.class);
		constraint.setId("bar constraint");
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForClass(ProductModel.class), ProductModel.CODE);
		constraint.setDescriptor(descrModel);

		assertEquals("", descrModel, constraint.getDescriptor());
		modelService.save(constraint);
		validationService.reloadValidationEngine();

		//setting wrong composed type won't harm we have an attribute descriptor
		final ComposedTypeModel catalogCompsedTypeModel = typeService.getComposedTypeForClass(CatalogModel.class);
		constraint.setType(catalogCompsedTypeModel);

		assertEquals("", descrModel, constraint.getDescriptor());
		assertTrue("", catalogCompsedTypeModel.equals(constraint.getType()));

		//setting wrong composed type won't harm we have an attribute descriptor
		modelService.save(constraint);

		assertEquals("", descrModel, constraint.getDescriptor());
		assertFalse("", catalogCompsedTypeModel.equals(constraint.getType()));
	}

	@Test
	public void testAttributeAssignmentIncompatibleDescriptorTargetClass()
	{
		final AttributeConstraintModel constraint = modelService.create(AttributeConstraintModel.class);
		constraint.setId("bar constraint");
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForClass(ProductModel.class), ProductModel.CODE);
		constraint.setDescriptor(descrModel);
		constraint.setTarget(BeanOne.class);

		modelService.save(constraint);
		validationService.reloadValidationEngine();
		assertEquals("", descrModel, constraint.getDescriptor());
	}

	@Test
	public void testAttributeAssignmentIncompatibleTargetClassType()
	{
		final AttributeConstraintModel constraint = modelService.create(AttributeConstraintModel.class);
		constraint.setId("bar constraint");
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		constraint.setTarget(ProductModel.class);
		final ComposedTypeModel catalogCompsedTypeModel = typeService.getComposedTypeForClass(CatalogModel.class);
		constraint.setType(catalogCompsedTypeModel);
		try
		{
			modelService.save(constraint);
			fail("expected ModelSavingException");
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, TypeConstraintValidator.class);
		}

	}

	@Test
	public void testAttributeAssignmentInvalidModel()
	{
		final AttributeConstraintModel constraint = modelService.create(AttributeConstraintModel.class);
		constraint.setId("bar constraint");
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		constraint.setTarget(ProductModel.class);
		constraint.setQualifier("notExistingProperty");
		try
		{
			modelService.save(constraint);
			fail("expected ModelSavingException");
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, TypeConstraintValidator.class);
		}
	}

	@Test
	public void testAttributeAssignmentTypeNotSetModel()
	{
		final AttributeConstraintModel constraint = modelService.create(AttributeConstraintModel.class);
		constraint.setId("bar constraint");
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		constraint.setTarget(ProductModel.class);
		constraint.setQualifier(ProductModel.CODE);
		try
		{
			modelService.save(constraint);
			fail("expected ModelSavingException");
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, TypeConstraintValidator.class);
		}
	}

	@Test
	public void testAttributeAssignmentForPojo()
	{
		final AttributeConstraintModel constraint = modelService.create(AttributeConstraintModel.class);
		constraint.setId("bar constraint");
		constraint.setAnnotation(javax.validation.constraints.Null.class);
		constraint.setTarget(BeanOne.class);
		constraint.setQualifier("attribute");
		modelService.save(constraint);
		validationService.reloadValidationEngine();

		assertNull("Shouldn't assign ct for a POJO ", constraint.getType());
		assertEquals("expected beanone", BeanOne.class, constraint.getTarget());
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForClass(ProductModel.class), ProductModel.CODE);
		constraint.setDescriptor(descrModel);

		modelService.save(constraint);
		assertEquals("", ProductModel.class, constraint.getTarget());
	}


	/**
	 * Check case for a HOR-847, PLA-9240
	 */
	@Test
	public void testNullConstraintWithoutDescriptor()
	{
		//create constraint
		final NullConstraintModel nullconstraint = modelService.create(NullConstraintModel.class);
		nullconstraint.setId("Nullconstraint");
		assertEquals("", javax.validation.constraints.Null.class, nullconstraint.getAnnotation());

		final ComposedTypeModel productModel = typeService.getComposedTypeForClass(ProductModel.class);

		nullconstraint.setQualifier(CustomerModel.DESCRIPTION);
		nullconstraint.setType(productModel);
		nullconstraint.setTarget(ProductModel.class);
		try
		{
			modelService.save(nullconstraint);
			fail("ModelSavingException expected");
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, AttributeConstraintValidator.class);
		}
	}

	/**
	 * Testing annotation for BeanTwoModel. This bean contains 3 annotations which are not violated here. Expect no
	 * violation.
	 */
	@Test
	public void testValidateAnnotationInPOJOok()
	{
		//simple pojo with everything is fine
		final BeanTwoModel test = new BeanTwoModel();
		test.setTest(2.4);
		test.setOtherTest(2);
		assertEquals("", 0, validationService.validate(test, Collections.EMPTY_LIST).size());

		//a reload shouldn't change anything
		validationService.reloadValidationEngine();
		assertEquals("", 0, validationService.validate(test, Collections.EMPTY_LIST).size());
	}

	/**
	 * Testing annotation for BeanTwoModel.test. The given value is greater than the decimalmax rule value but the digit
	 * rule is ok. Only the decimalmax violation is expected and this should not change after reloading the framework.
	 */
	@Test
	public void testValidateAnnotationInPOJOOneViolation()
	{
		final BeanTwoModel test = new BeanTwoModel();
		test.setTest(2.6); //false value, max=2.5
		test.setOtherTest(2);
		Set<HybrisConstraintViolation> violations = validationService.validate(test, Collections.EMPTY_LIST);
		assertEquals("", 1, violations.size()); //ok, found it
		assertEquals("", "{" + Constraint.HYBRIS_DECIMAL_MAX.msgKey + "}", violations.iterator().next().getConstraintViolation()
				.getMessageTemplate());

		//a reload shouldn't change anything
		validationService.reloadValidationEngine();
		violations = validationService.validate(test, Collections.EMPTY_LIST);
		assertEquals("", 1, violations.size());
		assertEquals("", "{" + Constraint.HYBRIS_DECIMAL_MAX.msgKey + "}", violations.iterator().next().getConstraintViolation()
				.getMessageTemplate());

		//change value and get different violation
		test.setTest(1.61); //false - allowed is x.x! not x.xx 
		violations = validationService.validate(test, Collections.EMPTY_LIST);
		assertEquals("", 1, violations.size());
		assertEquals("", "{javax.validation.constraints.Digits.message}", violations.iterator().next().getConstraintViolation()
				.getMessageTemplate());
	}

	/**
	 * Same as testValidateAnnotationInPOJO_OneViolation() but here an additional hybris constraint is created BUT FOR A
	 * DIFFERENT POJO. The violation has to be the same as before.
	 */
	@Test
	public void testAnnotationInPOJODifferentAttributes()
	{
		final BeanTwoModel test = new BeanTwoModel();
		test.setTest(2.6); //false value, max=2.5
		test.setOtherTest(2);
		Set<HybrisConstraintViolation> violations = validationService.validate(test, Collections.EMPTY_LIST);
		assertEquals("", 1, violations.size());
		assertEquals("", "{" + Constraint.HYBRIS_DECIMAL_MAX.msgKey + "}", violations.iterator().next().getConstraintViolation()
				.getMessageTemplate());

		//now create some constraint for different pojo

		final NotNullConstraintModel constraint1 = modelService.create(NotNullConstraintModel.class);
		constraint1.setId("notnull");
		constraint1.setTarget(BeanOne.class); // the different pojo
		constraint1.setQualifier("attribute");
		modelService.save(constraint1);

		validationService.reloadValidationEngine();
		//nothing should changed
		violations = validationService.validate(test, Collections.EMPTY_LIST);
		assertEquals("", 1, violations.size());
		assertEquals("", "{" + Constraint.HYBRIS_DECIMAL_MAX.msgKey + "}", violations.iterator().next().getConstraintViolation()
				.getMessageTemplate());
	}

	/**
	 * Same as testValidateAnnotationInPOJO_OneViolation() BUT here an additional hybris constraint is created TO THE
	 * SAME POJO (different attribute, this doesn't matter here). The new constraint won't be violated. Therefore the
	 * same violation (decimalmax) should be thrown.
	 */
	@Test
	public void testValidateAnnotationInPOJOSameAttribute()
	{
		final BeanTwoModel test = new BeanTwoModel();
		test.setTest(2.6); //false value, max=2.5
		test.setOtherTest(2.1);
		Set<HybrisConstraintViolation> violations = validationService.validate(test, Collections.EMPTY_LIST);
		assertEquals("", 1, violations.size());
		assertEquals("", "{" + Constraint.HYBRIS_DECIMAL_MAX.msgKey + "}", violations.iterator().next().getConstraintViolation()
				.getMessageTemplate());

		//constraint for same POJO but different attribute
		//this constraint does not report any violation, value is 2
		final DigitsConstraintModel constraint1 = modelService.create(DigitsConstraintModel.class);
		constraint1.setId("digits");
		constraint1.setTarget(BeanTwoModel.class);
		constraint1.setQualifier("otherTest");
		constraint1.setInteger(Integer.valueOf(1));
		constraint1.setFraction(Integer.valueOf(1));
		modelService.save(constraint1);

		//reload and expecting the same as before
		validationService.reloadValidationEngine();
		violations = validationService.validate(test, Collections.EMPTY_LIST);
		assertEquals("If actual size is 0 than the annotation was deleted by the hybris constraint ", 1, violations.size());

		final ConstraintViolation viol1 = violations.iterator().next().getConstraintViolation();
		assertEquals("", "{" + Constraint.HYBRIS_DECIMAL_MAX.msgKey + "}", viol1.getMessageTemplate());
		assertTrue("", viol1.getPropertyPath().toString().equals("test"));

		//no we fix the test value but violate the otherTest
		test.setTest(1.1);
		test.setOtherTest(1.221);
		violations = validationService.validate(test, Collections.EMPTY_LIST);
		final ConstraintViolation viol2 = violations.iterator().next().getConstraintViolation();
		assertEquals("", "{javax.validation.constraints.Digits.message}", viol2.getMessageTemplate());
		assertTrue("", viol2.getPropertyPath().toString().equals("otherTest"));
	}

	@Test
	public void testNeedReloadAttributeTest() throws InterruptedException
	{
		final SizeConstraintModel sizeConstraint = modelService.create(SizeConstraintModel.class);
		sizeConstraint.setId("four");
		sizeConstraint.setMin(Long.valueOf(1));
		sizeConstraint.setMax(Long.valueOf(Integer.MAX_VALUE));
		final AttributeDescriptorModel sizedescrModelProduct = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForClass(ProductModel.class), ProductModel.NAME);
		sizeConstraint.setDescriptor(sizedescrModelProduct);

		assertTrue("", modelService.isNew(sizeConstraint));

		assertFalse("", sizeConstraint.isNeedReload());
		modelService.save(sizeConstraint);
		assertFalse("", sizeConstraint.isNeedReload());
		modelService.refresh(sizeConstraint);
		assertTrue("", sizeConstraint.isNeedReload());

		validationService.reloadValidationEngine();
		assertTrue(sizeConstraint.isNeedReload());

		modelService.refresh(sizeConstraint);
		assertFalse("", sizeConstraint.isNeedReload());
		sizeConstraint.setId("four1");
		modelService.save(sizeConstraint);
		assertFalse("", sizeConstraint.isNeedReload());
		modelService.refresh(sizeConstraint);
		assertTrue("", sizeConstraint.isNeedReload());
	}

	/**
	 * Testing both class annotation for one attribute
	 */
	@Test
	public void testAnnotationInPOJOTwoViolation()
	{
		final BeanTwoModel test = new BeanTwoModel();
		test.setTest(20.4); //violates 2 rules, > 2.5 and has two integer
		test.setOtherTest(2);

		final Set<HybrisConstraintViolation> violations = validationService.validate(test, Collections.EMPTY_LIST);
		boolean foundSize = false;
		boolean foundNotNull = false;

		assertEquals("", 2, violations.size());
		for (final HybrisConstraintViolation viol : violations)
		{
			if ("{javax.validation.constraints.Digits.message}".equals(viol.getConstraintViolation().getMessageTemplate()))
			{
				foundSize = true;
			}
			if (("{" + Constraint.HYBRIS_DECIMAL_MAX.msgKey + "}").equals(viol.getConstraintViolation().getMessageTemplate()))
			{
				foundNotNull = true;
			}
		}
		assertTrue("", foundSize && foundNotNull);
	}

	/**
	 * BeanTwoModel.value has one annotation and 2 additional hybris constraints are created. First only the annotation
	 * should report the violation, than the validation framework is reloaded and all THREE should report a violation!
	 */
	@Test
	public void testAnnotationInPOJOandConstraints()
	{
		final DigitsConstraintModel constraint1 = modelService.create(DigitsConstraintModel.class);
		constraint1.setId("xxx1");
		constraint1.setTarget(BeanTwoModel.class);
		constraint1.setQualifier("otherTest");
		constraint1.setInteger(Integer.valueOf(1));
		constraint1.setFraction(Integer.valueOf(1));
		modelService.save(constraint1);

		final DecimalMaxConstraintModel constraint2 = modelService.create(DecimalMaxConstraintModel.class);
		constraint2.setId("xxx2");
		constraint2.setTarget(BeanTwoModel.class);
		constraint2.setQualifier("otherTest");
		constraint2.setValue(BigDecimal.valueOf(10.3));
		modelService.save(constraint2);

		final BeanTwoModel test1 = new BeanTwoModel();
		test1.setTest(2);
		test1.setOtherTest(13.45); //this value violates both hybris constaints which are not loaded yet

		Set<HybrisConstraintViolation> violations = validationService.validate(test1, Collections.EMPTY_LIST);
		assertEquals("", 1, violations.size());
		final ConstraintViolation viol1 = violations.iterator().next().getConstraintViolation();
		assertEquals("", "{" + Constraint.HYBRIS_DECIMAL_MAX.msgKey + "}", viol1.getMessageTemplate());
		assertTrue("", viol1.getPropertyPath().toString().equals("otherTest"));
		//the annotation DecimalMax has 3, the hybris constraint DecimnalMax has 10.3

		validationService.reloadValidationEngine();

		violations = validationService.validate(test1, Collections.EMPTY_LIST);
		assertEquals("", 3, violations.size());
	}

	@Test
	public void testHOR969()
	{
		final BeanOne beanone = new BeanOne();
		Set<HybrisConstraintViolation> violations = validationService.validate(beanone);
		assertFalse("", violations.isEmpty());
		assertEquals("", 1, violations.size());

		final BeanTwoModel bbb = new BeanTwoModel();
		bbb.setOtherTest(200);
		bbb.setTest(1.1);
		violations = validationService.validate(bbb);
		assertFalse("", violations.isEmpty());
		assertEquals("", 1, violations.size());
	}

	@SuppressWarnings("unused")
	private class BeanOne
	{
		private String attribute;

		@NotNull
		private String attribute2;

		public void setAttribute(final String attribute)
		{
			this.attribute = attribute;
		}

		public String getAttribute()
		{
			return attribute;
		}

		public void setAttribute2(final String attribute2)
		{
			this.attribute2 = attribute2;
		}

		public String getAttribute2()
		{
			return attribute2;
		}
	}

	@SuppressWarnings("unused")
	private class BeanTwoModel
	{
		@Digits(integer = 1, fraction = 1)
		@HybrisDecimalMax(value = "2.5")
		private double test = 0;

		@HybrisDecimalMax(value = "3")
		private double otherTest = 0;

		public void setTest(final double test)
		{
			this.test = test;
		}

		public double getTest()
		{
			return test;
		}

		public void setOtherTest(final double value)
		{
			this.otherTest = value;
		}

		public double getOtherTest()
		{
			return otherTest;
		}
	}

}
