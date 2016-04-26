package de.hybris.platform.warehousing.sourcing.util;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.Sets;



/**
 * Build {@link SourcingContext} to be used for testing purposes.
 */
public class SourcingContextBuilder
{

	private final Collection<AbstractOrderEntryModel> orderEntries = new ArrayList<AbstractOrderEntryModel>();
	private final Collection<SourcingLocation> sourcingLocations = new ArrayList<SourcingLocation>();
	private SourcingResults sourcingResults;

	/**
	 * Builds a {@link SourcingContext} with default values for the attribute
	 *
	 * @return the {@link SourcingContext}
	 */
	public SourcingContext build()
	{
		if (this.sourcingResults == null)
		{
			withDefaultResults();
		}
		final SourcingContext sourcingContext = new SourcingContext();
		sourcingContext.setOrderEntries(getOrderEntries());
		sourcingContext.setResult(this.sourcingResults);
		sourcingContext.setSourcingLocations(this.sourcingLocations);
		return sourcingContext;
	}

	public static SourcingContextBuilder aSourcingContext()
	{
		return new SourcingContextBuilder();
	}

	/**
	 * Add {@link SourcingLocation} to {@link SourcingContext}
	 *
	 * @param location
	 *           the {@link SourcingLocation}
	 * @return the {@link SourcingContextBuilder}
	 */
	public SourcingContextBuilder withSourcingLocation(final SourcingLocation location)
	{
		this.sourcingLocations.add(location);
		return this;
	}

	/**
	 * Add {@link AbstractOrderEntryModel} to {@link SourcingContext}
	 * @param entry the {@link AbstractOrderEntryModel}
	 * @return {@link SourcingContextBuilder}
	 */
	public SourcingContextBuilder withOrderEntry(final AbstractOrderEntryModel entry){
		this.orderEntries.add(entry);
		return this;
	}


	public SourcingContextBuilder withDefaultResults()
	{
		final SourcingResults results = new SourcingResults();
		results.setResults(Sets.newHashSet());
		this.sourcingResults = results;
		return this;
	}

	public SourcingContextBuilder withResults(final SourcingResults results)
	{
		this.sourcingResults = results;
		return this;
	}

	/**
	 * @return {@link Collection} of {@link SourcingLocation}
	 */
	protected Collection<SourcingLocation> getSourcingLocations()
	{
		return sourcingLocations;
	}

	/**
	 * @return the {@link Collection} of {@link AbstractOrderEntryModel}
	 */
	protected Collection<AbstractOrderEntryModel> getOrderEntries()
	{
		return orderEntries;
	}

    protected SourcingResults getSourcingResults() {
        return sourcingResults;
    }

    public void setSourcingResults(SourcingResults sourcingResults) {
        this.sourcingResults = sourcingResults;
    }
}
