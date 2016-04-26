package de.hybris.platform.sap.productconfig.frontend.util.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.frontend.constants.Sapproductconfigb2baddonConstants;
import de.hybris.platform.sap.productconfig.frontend.validator.ConflictError;
import de.hybris.platform.sap.productconfig.frontend.validator.MandatoryFieldError;
import de.hybris.platform.sap.productconfig.frontend.validator.ValidatorTestData;

@UnitTest
public class ErrorResolverTest {
	private static final String VALIDATION_ERROR_MESSAGE = "Validation error message";
	private static final String CONFLICT_ERROR_MESSAGE = "Conflict error message";
	private static final String MAND_FIELD_ERROR_MESSAGE = "Mandatory field error message";
	private ConfigurationData configData;
	private BindingResult errors;

	@Before
	public void setup() {
		configData = ValidatorTestData
				.createConfigurationWithConflict("Conflict");
		configData.getGroups().add(
				ValidatorTestData.createGroupWithNumeric("SECOND",
						"fieldName2", "12"));

		UiGroupData uiGroup = configData.getGroups().get(0);
		List<UiGroupData> subGroups = new ArrayList<>();
		subGroups.add(ValidatorTestData.createGroupWithNumeric("THIRD",
				"fieldName3", "14"));
		uiGroup.setSubGroups(subGroups);

		errors = new BeanPropertyBindingResult(configData,
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE);

		ConflictError conflictError = new ConflictError(null, getPath(0, 0),
				null, null, CONFLICT_ERROR_MESSAGE);
		errors.addError(conflictError);
		ObjectError validationError = new FieldError(
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, getPath(
						0, 0), VALIDATION_ERROR_MESSAGE);
		errors.addError(validationError);
		MandatoryFieldError mandatoryFieldError = new MandatoryFieldError(
				configData.getGroups().get(0).getCstics().get(0),
				getPath(0, 0), null, new String[] { "", "" },
				MAND_FIELD_ERROR_MESSAGE);
		errors.addError(mandatoryFieldError);

		conflictError = new ConflictError(null, getPath(0, 1), null, null,
				CONFLICT_ERROR_MESSAGE);
		errors.addError(conflictError);
		validationError = new FieldError(
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, getPath(
						0, 1), VALIDATION_ERROR_MESSAGE);
		errors.addError(validationError);
		mandatoryFieldError = new MandatoryFieldError(configData.getGroups()
				.get(0).getCstics().get(0), getPath(0, 1), null, new String[] {
				"", "" }, MAND_FIELD_ERROR_MESSAGE);
		errors.addError(mandatoryFieldError);

		conflictError = new ConflictError(null, getPath(1, 0), null, null,
				CONFLICT_ERROR_MESSAGE);
		errors.addError(conflictError);
		validationError = new FieldError(
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, getPath(
						1, 0), VALIDATION_ERROR_MESSAGE);
		errors.addError(validationError);
		mandatoryFieldError = new MandatoryFieldError(configData.getGroups()
				.get(1).getCstics().get(0), getPath(1, 0), null, new String[] {
				"", "" }, MAND_FIELD_ERROR_MESSAGE);
		errors.addError(mandatoryFieldError);

		mandatoryFieldError = new MandatoryFieldError(configData.getGroups()
				.get(0).getSubGroups().get(0).getCstics().get(0),
				"groups[0].subGroups[0].cstics[0]", null,
				new String[] { "", "" }, MAND_FIELD_ERROR_MESSAGE);
		errors.addError(mandatoryFieldError);
		validationError = new FieldError(
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE,
				"groups[0].subGroups[0].cstics[0]", VALIDATION_ERROR_MESSAGE);
		errors.addError(validationError);

		String[] codes = { "aaa.bbb" };
		Object[] args = { "arg0", "arg1", "arg2" };
		ObjectError validationErrorWithArg = new FieldError(
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, getPath(
						0, 2), "value", true, codes, args,
				VALIDATION_ERROR_MESSAGE);
		errors.addError(validationErrorWithArg);

		errors.setNestedPath(null);
	}

	private String getPath(int groupId, int csticId) {
		return "groups[" + groupId + "].cstics[" + csticId + "]";
	}

	@Test
	public void testHasErrorMessages() {
		errors = new BeanPropertyBindingResult(configData,
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE);

		ConflictError conflictError = new ConflictError(null, getPath(0, 0),
				null, null, CONFLICT_ERROR_MESSAGE);
		errors.addError(conflictError);
		ObjectError validationError = new FieldError(
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, getPath(
						0, 0), VALIDATION_ERROR_MESSAGE);
		errors.addError(validationError);
		MandatoryFieldError mandatoryFieldError = new MandatoryFieldError(
				configData.getGroups().get(0).getCstics().get(0),
				getPath(0, 0), null, new String[] { "", "" },
				MAND_FIELD_ERROR_MESSAGE);
		errors.addError(mandatoryFieldError);

		conflictError = new ConflictError(null, getPath(0, 1), null, null,
				CONFLICT_ERROR_MESSAGE);
		errors.addError(conflictError);
		validationError = new FieldError(
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, getPath(
						0, 1), VALIDATION_ERROR_MESSAGE);
		errors.addError(validationError);
		mandatoryFieldError = new MandatoryFieldError(configData.getGroups()
				.get(0).getCstics().get(0), getPath(0, 1), null, new String[] {
				"", "" }, MAND_FIELD_ERROR_MESSAGE);
		errors.addError(mandatoryFieldError);

		assertTrue(ErrorResolver.hasErrorMessages(errors));
	}

	@Test
	public void testHasNoErrorMessages() {
		errors = new BeanPropertyBindingResult(configData,
				Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE);

		ConflictError conflictError = new ConflictError(null, getPath(0, 0),
				null, null, CONFLICT_ERROR_MESSAGE);
		errors.addError(conflictError);
		MandatoryFieldError mandatoryFieldError = new MandatoryFieldError(
				configData.getGroups().get(0).getCstics().get(0),
				getPath(0, 0), null, new String[] { "", "" },
				MAND_FIELD_ERROR_MESSAGE);
		errors.addError(mandatoryFieldError);

		conflictError = new ConflictError(null, getPath(0, 1), null, null,
				CONFLICT_ERROR_MESSAGE);
		errors.addError(conflictError);
		mandatoryFieldError = new MandatoryFieldError(configData.getGroups()
				.get(0).getCstics().get(0), getPath(0, 1), null, new String[] {
				"", "" }, MAND_FIELD_ERROR_MESSAGE);
		errors.addError(mandatoryFieldError);

		assertFalse(ErrorResolver.hasErrorMessages(errors));
	}

	@Test
	public void testGetConflictErrors() {
		List<ErrorMessage> conflictErrors = ErrorResolver
				.getConflictErrors(errors);
		assertNotNull(conflictErrors);
		assertEquals("Have to be three conflicts", 3, conflictErrors.size());
		assertEquals(CONFLICT_ERROR_MESSAGE, conflictErrors.get(0).getMessage());
		assertEquals(getPath(0, 0), conflictErrors.get(0).getPath());
	}

	@Test
	public void testGetMandatoryFieldErrors() {
		List<ErrorMessage> mandatoryFieldErrors = ErrorResolver
				.getMandatoryFieldErrors(errors);
		assertNotNull(mandatoryFieldErrors);
		assertEquals("Have to be three mandatoryFieldErrors", 4,
				mandatoryFieldErrors.size());
		assertEquals(MAND_FIELD_ERROR_MESSAGE, mandatoryFieldErrors.get(0)
				.getMessage());
		assertEquals(getPath(0, 0), mandatoryFieldErrors.get(0).getPath());
	}

	@Test
	public void testGetWarnings() {
		List<ErrorMessage> warnings = ErrorResolver.getWarnings(errors);
		assertNotNull(warnings);
		assertEquals("Have to be six warnings", 7, warnings.size());
	}

	@Test
	public void testGetValidationErrors() {
		List<ErrorMessage> validationErrors = ErrorResolver
				.getValidationErrors(errors);
		assertNotNull(validationErrors);
		assertEquals("Have to be four error", 5, validationErrors.size());
		assertEquals(VALIDATION_ERROR_MESSAGE, validationErrors.get(0)
				.getMessage());
	}

	@Test
	public void testGetConflictErrorsForCstic() {
		List<ErrorMessage> conflictErrors = ErrorResolver.getWarningsForCstic(
				errors, getPath(0, 0));
		assertNotNull(conflictErrors);
		assertEquals("Have to be two warnings", 2, conflictErrors.size());
	}

	@Test
	public void testGetValidationErrorsForCstic() {
		List<ErrorMessage> conflictErrors = ErrorResolver
				.getValidationErrorsForCstic(errors, getPath(0, 1));
		assertNotNull(conflictErrors);
		assertEquals("Has to be one error", 1, conflictErrors.size());
	}

	@Test
	public void testErrorsWithArgs() {
		List<ErrorMessage> conflictErrors = ErrorResolver
				.getValidationErrorsForCstic(errors, getPath(0, 2));
		assertNotNull(conflictErrors);
		assertEquals("Has to be one error", 1, conflictErrors.size());

		ErrorMessage errorMessage = conflictErrors.get(0);

		assertNotNull("Code missing", errorMessage.getCode());
		assertEquals("Arguments missing", 3, errorMessage.getArgs().length);
	}

	@Test
	public void testWarningsOnGroupLevel() {
		List<ErrorMessage> conflictErrors = ErrorResolver.getWarningsForGroup(
				errors, "groups[1].");

		assertNotNull(conflictErrors);
		assertEquals("Has to be two errors", 2, conflictErrors.size());
		assertEquals(CONFLICT_ERROR_MESSAGE, conflictErrors.get(0).getMessage());

		conflictErrors = ErrorResolver
				.getWarningsForGroup(errors, "groups[0].");

		assertNotNull(conflictErrors);
		assertEquals("Has to be four errors", 4, conflictErrors.size());
		assertEquals(CONFLICT_ERROR_MESSAGE, conflictErrors.get(0).getMessage());
		assertEquals(MAND_FIELD_ERROR_MESSAGE, conflictErrors.get(1)
				.getMessage());
	}

	@Test
	public void testWarningsOnSubGroupLevel() {
		List<ErrorMessage> conflictErrors = ErrorResolver.getWarningsForGroup(
				errors, "groups[0].subGroups[0].");

		assertNotNull(conflictErrors);
		assertEquals("Have to be four errors", 1, conflictErrors.size());
		assertEquals(MAND_FIELD_ERROR_MESSAGE, conflictErrors.get(0)
				.getMessage());
	}

	@Test
	public void testErrorsOnGroupLevel() {
		List<ErrorMessage> validationErrors = ErrorResolver.getErrorsForGroup(
				errors, "groups[1].");

		assertNotNull(validationErrors);
		assertEquals("Has to be one error", 1, validationErrors.size());
		assertEquals(VALIDATION_ERROR_MESSAGE, validationErrors.get(0)
				.getMessage());

		validationErrors = ErrorResolver
				.getErrorsForGroup(errors, "groups[0].");

		assertNotNull(validationErrors);
		assertEquals("Have to be three errors", 3, validationErrors.size());
		assertEquals(VALIDATION_ERROR_MESSAGE, validationErrors.get(0)
				.getMessage());
	}

	@Test
	public void testErrorsOnSubGroupLevel() {
		List<ErrorMessage> validationErrors = ErrorResolver.getErrorsForGroup(
				errors, "groups[0].subGroups[0].");

		assertNotNull(validationErrors);
		assertEquals("Have to be three errors", 1, validationErrors.size());
		assertEquals(VALIDATION_ERROR_MESSAGE, validationErrors.get(0)
				.getMessage());
	}
}
