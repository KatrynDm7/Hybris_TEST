/**
 * 
 */
package de.hybris.e2e.transport.cts;

/**
 * @author I308855
 * 
 */
public class TestConfigurationHolder implements ConfigurationHolder
{

	/**
	 * Gets application type
	 * 
	 * @return application type
	 */
	@Override
	public String getApplicationType()
	{
		return "HYBRIS";
	}

	/**
	 * Gets SID
	 * 
	 * @return SID
	 */
	@Override
	public String getSid()
	{
		return "TEST";
	}

	/**
	 * Gets user
	 * 
	 * @return user
	 */
	@Override
	public String getUser()
	{
		return "TestUser";
	}

	/**
	 * Gets password
	 * 
	 * @return password
	 */
	@Override
	public String getPassword()
	{
		return "TestPwd";
	}

	/**
	 * Gets WS' url
	 * 
	 * @return url
	 */
	@Override
	public String getUrl()
	{
		return "sap.fake_url.exportwebservice?wsdl";
	}

	/**
	 * Gets transport package size
	 * 
	 * @return transport package size
	 */
	@Override
	public int getPackageSize()
	{
		return 2;
	}

	/**
	 * Gets WS' name
	 * 
	 * @return name
	 */
	@Override
	public String getWsName()
	{
		return "wWsFakeName";
	}

	/**
	 * Gets WS' bidning's name
	 * 
	 * @return name
	 */
	@Override
	public String getWsBindingName()
	{
		return "fakeBindingName";
	}

}
