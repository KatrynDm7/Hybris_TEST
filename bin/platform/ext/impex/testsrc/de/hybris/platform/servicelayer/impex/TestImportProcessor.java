package de.hybris.platform.servicelayer.impex;

import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.imp.DefaultImportProcessor;
import de.hybris.platform.impex.jalo.imp.ExistingItemResolver;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImportProcessor;
import de.hybris.platform.impex.jalo.imp.ValueLine;


public class TestImportProcessor extends DefaultImportProcessor
{
	@Override
	protected ExistingItemResolver getExistingItemResolver(final ValueLine valueLine) throws HeaderValidationException
	{
		return new TestExistingItemResolver(this);
	}

    public boolean isSecondPass()
    {
        return this.getReader().isSecondPass();
    }
}
