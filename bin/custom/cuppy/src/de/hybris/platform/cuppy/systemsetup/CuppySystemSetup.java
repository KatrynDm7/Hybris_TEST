/**
 * 
 */
package de.hybris.platform.cuppy.systemsetup;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author andreas.thaler
 * 
 */
@SystemSetup(extension = CuppyConstants.EXTENSIONNAME, process = Process.ALL, type = Type.PROJECT)
public class CuppySystemSetup
{
	private final static Logger LOG = Logger.getLogger(CuppySystemSetup.class);
	private ImportService importService;

	@SystemSetup
	public void importBasics(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_BASICS);
		if (params.contains(CuppyConstants.PARAM_BASICS_PLAYERS))
		{
			importCsv(CuppyConstants.RESOURCE_PLAYERS);
		}
		if (params.contains(CuppyConstants.PARAM_BASICS_TEMPLATES))
		{
			importCsv(CuppyConstants.RESOURCE_TEMPLATES);
		}
	}

	@SystemSetup
	public void importWC2002(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_WC2002);
		if (params.contains(CuppyConstants.PARAM_WC2002_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_WC2002_SETUP);
		}
		if (params.contains(CuppyConstants.PARAM_WC2002_RESULTS_PRELIMINARIES))
		{
			importCsv(CuppyConstants.RESOURCE_WC2002_RESULTS_PRELIS);
		}
		if (params.contains(CuppyConstants.PARAM_WC2002_RESULTS_FINALS))
		{
			importCsv(CuppyConstants.RESOURCE_WC2002_RESULTS_FINALS);
		}
		if (params.contains(CuppyConstants.PARAM_WC2002_BETS_PRELIMINARIES))
		{
			importCsv(CuppyConstants.RESOURCE_WC2002_BETS_PRELIS);
		}
		if (params.contains(CuppyConstants.PARAM_WC2002_BETS_FINALS))
		{
			importCsv(CuppyConstants.RESOURCE_WC2002_BETS_FINALS);
		}
	}

	@SystemSetup
	public void importWC2006(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_WC2006);
		if (params.contains(CuppyConstants.PARAM_WC2006_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_WC2006_SETUP);
		}
		if (params.contains(CuppyConstants.PARAM_WC2006_RESULTS_PRELIMINARIES))
		{
			importCsv(CuppyConstants.RESOURCE_WC2006_RESULTS_PRELIS);
		}
		if (params.contains(CuppyConstants.PARAM_WC2006_RESULTS_FINALS))
		{
			importCsv(CuppyConstants.RESOURCE_WC2006_RESULTS_FINALS);
		}
		if (params.contains(CuppyConstants.PARAM_WC2006_BETS_PRELIMINARIES))
		{
			importCsv(CuppyConstants.RESOURCE_WC2006_BETS_PRELIS);
		}
		if (params.contains(CuppyConstants.PARAM_WC2006_BETS_FINALS))
		{
			importCsv(CuppyConstants.RESOURCE_WC2006_BETS_FINALS);
		}
	}

	@SystemSetup
	public void importEC2004(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_EC2004);
		if (params.contains(CuppyConstants.PARAM_EC2004_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_EC2004_SETUP);
		}
		if (params.contains(CuppyConstants.PARAM_EC2004_RESULTS_PRELIMINARIES))
		{
			importCsv(CuppyConstants.RESOURCE_EC2004_RESULTS_PRELIS);
		}
		if (params.contains(CuppyConstants.PARAM_EC2004_RESULTS_FINALS))
		{
			importCsv(CuppyConstants.RESOURCE_EC2004_RESULTS_FINALS);
		}
		if (params.contains(CuppyConstants.PARAM_EC2004_BETS_PRELIMINARIES))
		{
			importCsv(CuppyConstants.RESOURCE_EC2004_BETS_PRELIS);
		}
		if (params.contains(CuppyConstants.PARAM_EC2004_BETS_FINALS))
		{
			importCsv(CuppyConstants.RESOURCE_EC2004_BETS_FINALS);
		}
	}

	@SystemSetup
	public void importEC2008(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_EC2008);
		if (params.contains(CuppyConstants.PARAM_EC2008_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_EC2008_SETUP);
		}
		if (params.contains(CuppyConstants.PARAM_EC2008_RESULTS_PRELIMINARIES))
		{
			importCsv(CuppyConstants.RESOURCE_EC2008_RESULTS_PRELIS);
		}
		if (params.contains(CuppyConstants.PARAM_EC2008_RESULTS_FINALS))
		{
			importCsv(CuppyConstants.RESOURCE_EC2008_RESULTS_FINALS);
		}
		if (params.contains(CuppyConstants.PARAM_EC2008_BETS_PRELIMINARIES))
		{
			importCsv(CuppyConstants.RESOURCE_EC2008_BETS_PRELIS);
		}
		if (params.contains(CuppyConstants.PARAM_EC2008_BETS_FINALS))
		{
			importCsv(CuppyConstants.RESOURCE_EC2008_BETS_FINALS);
		}
	}

	@SystemSetup
	public void importEC2012(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_EC2012);
		if (params.contains(CuppyConstants.PARAM_EC2012_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_EC2012_SETUP);
		}
	}

	@SystemSetup
	public void importWC2010(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_WC2010);
		if (params.contains(CuppyConstants.PARAM_WC2010_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_WC2010_SETUP);
		}
		if (params.contains(CuppyConstants.PARAM_WC2010_RESULTS_PRELIMINARIES))
		{
			importCsv(CuppyConstants.RESOURCE_WC2010_RESULTS_PRELIS);
		}
		if (params.contains(CuppyConstants.PARAM_WC2010_RESULTS_FINALS))
		{
			importCsv(CuppyConstants.RESOURCE_WC2010_RESULTS_FINALS);
		}
		if (params.contains(CuppyConstants.PARAM_WC2010_BETS_PRELIMINARIES))
		{
			importCsv(CuppyConstants.RESOURCE_WC2010_BETS_PRELIS);
		}
		if (params.contains(CuppyConstants.PARAM_WC2010_BETS_FINALS))
		{
			importCsv(CuppyConstants.RESOURCE_WC2010_BETS_FINALS);
		}
	}

	public void importTestNews()
	{
		if (cuppytrailInstalled())
		{
			importCsv(CuppyConstants.RESOURCE_TEST_NEWS_CATALOG);
		}
		else
		{
			importCsv(CuppyConstants.RESOURCE_TEST_NEWS);
		}
	}

	private boolean cuppytrailInstalled()
	{
		return Utilities.getExtensionNames().contains("cuppytrail");
	}

	@SystemSetup
	public void importWWC2011(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_WWC2011);
		if (params.contains(CuppyConstants.PARAM_WWC2011_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_WWC2011_SETUP);
		}
	}

	@SystemSetup
	public void import1LG2010(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_1LGER2010);
		if (params.contains(CuppyConstants.PARAM_1LGER2010_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_1LGER2010_SETUP);
		}
	}

	@SystemSetup
	public void import2LG2010(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_2LGER2010);
		if (params.contains(CuppyConstants.PARAM_2LGER2010_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_2LGER2010_SETUP);
		}
	}

	@SystemSetup
	public void import1LG2011(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_1LGER2011);
		if (params.contains(CuppyConstants.PARAM_1LGER2011_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_1LGER2011_SETUP);
		}
	}

	@SystemSetup
	public void import1LG2012(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_1LGER2012);
		if (params.contains(CuppyConstants.PARAM_1LGER2012_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_1LGER2012_SETUP);
		}
	}

	@SystemSetup
	public void importCL2010(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_CL2010);
		if (params.contains(CuppyConstants.PARAM_CL2010_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_CL2010_SETUP);
		}
	}

	@SystemSetup
	public void importCL2011(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_CL2011);
		if (params.contains(CuppyConstants.PARAM_CL2011_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_CL2011_SETUP);
		}
	}

	@SystemSetup
	public void importCL2012(final SystemSetupContext context)
	{
		final List<String> params = getParams(context, CuppyConstants.PARAM_CL2012);
		if (params.contains(CuppyConstants.PARAM_CL2012_SETUP))
		{
			importCsv(CuppyConstants.RESOURCE_CL2012_SETUP);
		}
	}

	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getSystemSetupParameters()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		SystemSetupParameter param = new SystemSetupParameter(CuppyConstants.PARAM_BASICS);
		param.setMultiSelect(true);
		param.setLabel("Basics");
		param.addValue(CuppyConstants.PARAM_BASICS_PLAYERS);
		param.addValue(CuppyConstants.PARAM_BASICS_TEMPLATES);
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_WC2002);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_WC2002);
		param.addValues(new String[]
		{ CuppyConstants.PARAM_WC2002_SETUP, CuppyConstants.PARAM_WC2002_RESULTS_PRELIMINARIES,
				CuppyConstants.PARAM_WC2002_RESULTS_FINALS, CuppyConstants.PARAM_WC2002_BETS_PRELIMINARIES,
				CuppyConstants.PARAM_WC2002_BETS_FINALS });
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_EC2004);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_EC2004);
		param.addValues(new String[]
		{ CuppyConstants.PARAM_EC2004_SETUP, CuppyConstants.PARAM_EC2004_RESULTS_PRELIMINARIES,
				CuppyConstants.PARAM_EC2004_RESULTS_FINALS, CuppyConstants.PARAM_EC2004_BETS_PRELIMINARIES,
				CuppyConstants.PARAM_EC2004_BETS_FINALS });
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_WC2006);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_WC2006);
		param.addValues(new String[]
		{ CuppyConstants.PARAM_WC2006_SETUP, CuppyConstants.PARAM_WC2006_RESULTS_PRELIMINARIES,
				CuppyConstants.PARAM_WC2006_RESULTS_FINALS, CuppyConstants.PARAM_WC2006_BETS_PRELIMINARIES,
				CuppyConstants.PARAM_WC2006_BETS_FINALS });
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_EC2008);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_EC2008);
		param.addValues(new String[]
		{ CuppyConstants.PARAM_EC2008_SETUP, CuppyConstants.PARAM_EC2008_RESULTS_PRELIMINARIES,
				CuppyConstants.PARAM_EC2008_RESULTS_FINALS, CuppyConstants.PARAM_EC2008_BETS_PRELIMINARIES,
				CuppyConstants.PARAM_EC2008_BETS_FINALS });
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_EC2012);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_EC2012);
		param.addValues(new String[]
		{ CuppyConstants.PARAM_EC2012_SETUP });
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_WC2010);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_WC2010);
		param.addValues(new String[]
		{ CuppyConstants.PARAM_WC2010_SETUP, CuppyConstants.PARAM_WC2010_RESULTS_PRELIMINARIES,
				CuppyConstants.PARAM_WC2010_RESULTS_FINALS, CuppyConstants.PARAM_WC2010_BETS_PRELIMINARIES,
				CuppyConstants.PARAM_WC2010_BETS_FINALS });
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_WWC2011);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_WWC2011);
		param.addValue(CuppyConstants.PARAM_WWC2011_SETUP);
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_1LGER2010);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_1LGER2010);
		param.addValue(CuppyConstants.PARAM_1LGER2010_SETUP);
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_2LGER2010);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_2LGER2010);
		param.addValue(CuppyConstants.PARAM_2LGER2010_SETUP);
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_1LGER2011);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_1LGER2011);
		param.addValue(CuppyConstants.PARAM_1LGER2011_SETUP);
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_1LGER2012);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_1LGER2012);
		param.addValue(CuppyConstants.PARAM_1LGER2012_SETUP);
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_CL2010);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_CL2010);
		param.addValue(CuppyConstants.PARAM_CL2010_SETUP);
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_CL2011);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_CL2011);
		param.addValue(CuppyConstants.PARAM_CL2011_SETUP);
		params.add(param);

		param = new SystemSetupParameter(CuppyConstants.PARAM_CL2012);
		param.setMultiSelect(true);
		param.setLabel(CuppyConstants.PARAM_CL2012);
		param.addValue(CuppyConstants.PARAM_CL2012_SETUP);
		params.add(param);

		return params;
	}

	private List<String> getParams(final SystemSetupContext context, final String param)
	{
		String[] params = context.getParameters(param);
		if (params == null)
		{
			params = context.getParameters(CuppyConstants.EXTENSIONNAME + "_" + param);
		}
		if (params == null)
		{
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList(params);
	}

	private void importCsv(final String resource)
	{
		LOG.info("Importing resource " + resource);
		final ImportResult result = importService.importData(new ClasspathImpExResource(resource, CSVConstants.HYBRIS_ENCODING));
		if (result.hasUnresolvedLines())
		{
			LOG.warn("Import of " + resource + " had unresolved lines:\n" + result.getUnresolvedLines().getPreview());
		}
		else if (result.isError())
		{
			LOG.error("Import of " + resource + " failed");
		}
	}

	@Required
	public void setImportService(final ImportService importService)
	{
		this.importService = importService;
	}

}
