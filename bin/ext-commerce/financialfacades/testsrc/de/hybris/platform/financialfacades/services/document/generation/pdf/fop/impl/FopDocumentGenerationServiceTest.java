package de.hybris.platform.financialfacades.services.document.generation.pdf.fop.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * The class of FopDocumentGenerationServiceTest.
 */
@UnitTest
public class FopDocumentGenerationServiceTest
{
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setup()
	{
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void tearDown()
	{
		System.setOut(null);
		System.setErr(null);
	}

	@Test
	public void shouldGetXSLTransformer() throws TransformerConfigurationException
	{

		final String template = getTestTemplate();

		final String errorMessage = "\"margin-right\" attribute is not allowed on the fo:simple-page-master element!";

		FopDocumentGenerationService service = new FopDocumentGenerationService();

		StreamSource transformSource = new StreamSource(new StringReader(template));

        Transformer transformer = service.getXSLTransformerWithoutSecureFeature(transformSource);

        String err = new String(errContent.toByteArray());

        assertNotNull(transformer);
		assertFalse(err.contains(errorMessage));
	}

	private String getTestTemplate()
	{
		return "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" + "<xsl:stylesheet version=\"1.1\""
				+ " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"" + " xmlns:fo=\"http://www.w3.org/1999/XSL/Format\""
				+ " exclude-result-prefixes=\"fo\">" + "<xsl:template match=\"root\">"
				+ "<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\" xmlns:fox=\"http://xml.apache.org/fop/extensions\">"
				+ "<!-- Creator=\"html2fo\" Version=\"0.4.2\" -->" + "<fo:layout-master-set>"
				+ "<fo:simple-page-master margin-right=\"2.0cm\">" + "<fo:region-body/>" + "<fo:region-before/>"
				+ "<fo:region-after />" + "</fo:simple-page-master>" + "</fo:layout-master-set>" + "</fo:root>" + "</xsl:template>"
				+ "</xsl:stylesheet>";
	}
}
