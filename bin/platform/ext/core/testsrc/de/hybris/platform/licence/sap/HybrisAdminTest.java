package de.hybris.platform.licence.sap;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.platform.licence.internal.SAPLicenseValidator;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Utilities;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sap.security.core.server.likey.Persistence;


@IntegrationTest
public class HybrisAdminTest extends HybrisJUnit4Test
{
	private SAPLicenseValidator validator;
	private PropertyBasedTestPersistence persistence;

	private String hwKeyBackup;

	@Before
	public void setUp() throws Exception
	{
		persistence = new PropertyBasedTestPersistence();

		// Need to patch hardware key to match test license file !!!
		hwKeyBackup = changeHardwareKeyTo("A0000000000");
		validator = new SAPLicenseValidator()
		{
			@Override
			protected Persistence getPersistence()
			{
				return persistence;
			}
		};
		System.setProperty("persistence.impl", PropertyBasedTestPersistence.class.getCanonicalName());
	}

	@After
	public void tearDown() throws Exception
	{
		restoreHardwareKey(hwKeyBackup);
		System.clearProperty("persistence.impl");
		persistence.removePersistenceFile();
	}

	String changeHardwareKeyTo(final String key)
	{
		return (String) Utilities.loadPlatformProperties().setProperty("license.hardware.key", key);
	}

	String changeSystemIDTo(final String id)
	{
		return (String) Utilities.loadPlatformProperties().setProperty("license.sap.sapsystem", id);
	}

	void restoreHardwareKey(final String original)
	{
		if (original == null)
		{
			Utilities.loadPlatformProperties().remove("license.hardware.key");
		}
		else
		{
			Utilities.loadPlatformProperties().setProperty("license.hardware.key", original);
		}
	}

	void restoreSystemID(final String original)
	{
		if (original == null)
		{
			Utilities.loadPlatformProperties().remove("license.sap.sapsystem");
		}
		else
		{
			Utilities.loadPlatformProperties().setProperty("license.sap.sapsystem", original);
		}
	}


	@Test
	public void shouldInstallTempLicense() throws Exception
	{
		// given
		final String[] args = new String[]
		{ "-t", "CPS_HDB" };
		assertThat(validator.validateLicense("CPS_HDB").isValid()).isFalse();

		// when
		HybrisAdmin.main(args);

		// then
		assertThat(validator.validateLicense("CPS_HDB").isValid()).isTrue();
	}

	@Test
	public void shouldInstallLicenseFromFile() throws Exception
	{
		// given
		final String licenseFileLocation = getLicenseFileLocation();
		writeLicenseFile(licenseFileLocation, getStandardLicenceFileContent());
		final String[] args = new String[]
		{ "-i", licenseFileLocation };
		assertThat(validator.validateLicense("CPS_ORA").isValid()).isFalse();

		// when
		HybrisAdmin.main(args);

		// then
		assertThat(validator.validateLicense("CPS_ORA").isValid()).isTrue();
		FileUtils.deleteQuietly(new File(licenseFileLocation));
	}

	@Test
	public void shouldInstallNonCPSSystemIDLicenseFromFile() throws Exception
	{
		restoreHardwareKey(hwKeyBackup); // this test license uses our default hardware key -> need to revert changes from setUp()
		final String defaultSystemID = changeSystemIDTo("CCC"); // change system ID
		try
		{
			final SAPLicenseValidator validatorDifferentSystemID = new SAPLicenseValidator()
			{
				@Override
				protected Persistence getPersistence()
				{
					return persistence;
				}
			};

			// given
			final String licenseFileLocation = getLicenseFileLocation();
			writeLicenseFile(licenseFileLocation, getDifferentSystemIDLicenseFileContent());
			final String[] args = new String[]
			{ "-i", licenseFileLocation };
			assertThat(validatorDifferentSystemID.validateLicense("CPS_SQL").isValid()).isFalse();

			// when
			HybrisAdmin.main(args);

			// then
			assertThat(validatorDifferentSystemID.validateLicense("CPS_SQL").isValid()).isTrue();
			FileUtils.deleteQuietly(new File(licenseFileLocation));
		}
		finally
		{
			restoreSystemID(defaultSystemID);
		}

	}


	@Test
	public void shouldDeleteExistingLicense() throws Exception
	{
		// given
		HybrisAdmin.main(new String[]
		{ "-t", "CPS_HDB" });
		final String[] deleteArgs = new String[]
		{ "-d", "CPS", "A0000000000", "CPS_HDB" };

		// when
		HybrisAdmin.main(deleteArgs);

		// then
		assertThat(validator.validateLicense("CPS_HDB").isValid()).isFalse();
	}

	private String getLicenseFileLocation()
	{
		return ConfigUtil.getPlatformConfig(HybrisAdminTest.class).getSystemConfig().getTempDir() + "/testLicense.txt";
	}

	private void writeLicenseFile(final String location, final String content)
	{
		final File file = new File(location);
		try
		{
			FileUtils.writeStringToFile(file, content);
		}
		catch (final IOException e)
		{
			fail(e.getMessage());
		}
	}

	private String getStandardLicenceFileContent()
	{
		//		return "----- Begin SAP License -----\n"
		//				+ "SAPSYSTEM=CPS\n"
		//				+ "HARDWARE-KEY=A0000000000\n"
		//				+ "INSTNO=SAP-INTERN\n"
		//				+ "BEGIN=20140813\n"
		//				+ "EXPIRATION=20150814\n"
		//				+ "LKEY=MIIBOgYJKoZIhvcNAQcCoIIBKzCCAScCAQExCzAJBgUrDgMCGgUAMAsGCSqGSIb3DQEHATGCAQYwggECAgEBMFgwUjELMAkGA1UEBhMCREUxHDAaBgNVBAoTE215U0FQLmNvbSBXb3JrcGxhY2UxJTAjBgNVBAMTHG15U0FQLmNvbSBXb3JrcGxhY2UgQ0EgKGRzYSkCAgGhMAkGBSsOAwIaBQCgXTAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqGSIb3DQEJBTEPFw0xNDA4MTQxMzMzMTVaMCMGCSqGSIb3DQEJBDEWBBRreAQ3rZmQKxKjeNh5qHx6pSAVdzAJBgcqhkjOOAQDBC4wLAIUKb6k1fKfiSBsWlx3MflEYGhluEICFB3wRSRexRpjmohKk0uBviNawyXo\n"
		//				+ "SWPRODUCTNAME=CPS_HDB\n" + "SWPRODUCTLIMIT=2147483647\n" + "SYSTEM-NR=000000000311440630\n";

		// TODO get new license by 17.08.2016
		return "----- Begin SAP License -----\n"
				+ "SAPSYSTEM=CPS\n"
				+ "HARDWARE-KEY=A0000000000\n"
				+ "INSTNO=SAP-INTERN\n"
				+ "BEGIN=20150816\n"
				+ "EXPIRATION=20160817\n"
				+ "LKEY=MIIBOgYJKoZIhvcNAQcCoIIBKzCCAScCAQExCzAJBgUrDgMCGgUAMAsGCSqGSIb3DQEHATGCAQYwggECAgEBMFgwUjELMAkGA1UEBhMCREUxHDAaBgNVBAoTE215U0FQLmNvbSBXb3JrcGxhY2UxJTAjBgNVBAMTHG15U0FQLmNvbSBXb3JrcGxhY2UgQ0EgKGRzYSkCAgGhMAkGBSsOAwIaBQCgXTAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqGSIb3DQEJBTEPFw0xNTA4MTcxMzU3NThaMCMGCSqGSIb3DQEJBDEWBBQI2atTfUQYT5KSuLJoGQbK0CAVjTAJBgcqhkjOOAQDBC4wLAIULoeDVRgExIq5bqjq+7ZJUQXlJHwCFCPHNPjDGzG+7yulsWhgD8z7ZXtn\n"
				+ "SWPRODUCTNAME=CPS_ORA\n" + "SWPRODUCTLIMIT=2147483647\n" + "SYSTEM-NR=000000000312346866\n";
	}

	private String getDifferentSystemIDLicenseFileContent()
	{
		//		return "----- Begin SAP License -----\n"
		//				+ "SAPSYSTEM=CCC\n"
		//				+ "HARDWARE-KEY=Y4989890650\n"
		//				+ "INSTNO=SAP-INTERN\n"
		//				+ "BEGIN=20150223\n"
		//				+ "EXPIRATION=20160224\n"
		//				+ "LKEY=MIIBOwYJKoZIhvcNAQcCoIIBLDCCASgCAQExCzAJBgUrDgMCGgUAMAsGCSqGSIb3DQEHATGCAQcwggEDAgEBMFgwUjELMAkGA1UEBhMCREUxHDAaBgNVBAoTE215U0FQLmNvbSBXb3JrcGxhY2UxJTAjBgNVBAMTHG15U0FQLmNvbSBXb3JrcGxhY2UgQ0EgKGRzYSkCAgGhMAkGBSsOAwIaBQCgXTAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqGSIb3DQEJBTEPFw0xNTAyMjQwODM4NDFaMCMGCSqGSIb3DQEJBDEWBBRyqBKCeBmtynrm1zcX063Pubg3EjAJBgcqhkjOOAQDBC8wLQIVAI42agiyXoSGkFmSlvFene0XyjGmAhQGLmcur4SkKiwxOlpgt1IF1fObNw==\n"
		//				+ "SWPRODUCTNAME=CPS_SQL\n" + "SWPRODUCTLIMIT=2147483647\n" + "SYSTEM-NR=000000000312420744\n";

		// TODO get new license by 17.08.2016
		return "----- Begin SAP License -----\n"
				+ "SAPSYSTEM=CCC\n"
				+ "HARDWARE-KEY=Y4989890650\n"
				+ "INSTNO=SAP-INTERN\n"
				+ "BEGIN=20150816\n"
				+ "EXPIRATION=20160817\n"
				+ "LKEY=MIIBPAYJKoZIhvcNAQcCoIIBLTCCASkCAQExCzAJBgUrDgMCGgUAMAsGCSqGSIb3DQEHATGCAQgwggEEAgEBMFgwUjELMAkGA1UEBhMCREUxHDAaBgNVBAoTE215U0FQLmNvbSBXb3JrcGxhY2UxJTAjBgNVBAMTHG15U0FQLmNvbSBXb3JrcGxhY2UgQ0EgKGRzYSkCAgGhMAkGBSsOAwIaBQCgXTAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqGSIb3DQEJBTEPFw0xNTA4MTcxMzQ5MzFaMCMGCSqGSIb3DQEJBDEWBBREznxsNOQ3ZSZsrdl4KSVovPVq+zAJBgcqhkjOOAQDBDAwLgIVAJNKMEmv1OLiQFM+6IS7+eOEOq/XAhUAll0q7r/wTI7II8oDV+HWeAV5X9U=\n"
				+ "SWPRODUCTNAME=CPS_SQL\n" + "SWPRODUCTLIMIT=2147483647\n" + "SYSTEM-NR=000000000312420744\n";

	}
}
