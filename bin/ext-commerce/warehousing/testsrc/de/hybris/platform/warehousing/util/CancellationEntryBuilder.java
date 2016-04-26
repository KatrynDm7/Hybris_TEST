package de.hybris.platform.warehousing.util;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.warehousing.data.cancellation.CancellationEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


/**
 * This is a Sourcing order builder implementation of the Builder interface
 */

public class CancellationEntryBuilder
{
    public static CancellationEntryBuilder aCancellation() {
        return new CancellationEntryBuilder();
    }

    public Collection<CancellationEntry> build(Map <AbstractOrderEntryModel, Long> cancellationEntryInfo)
    {
        Collection<CancellationEntry> cancellationEntryCollection = new ArrayList<CancellationEntry>();
        cancellationEntryInfo.entrySet().stream().forEach(
                cancellation ->
                {
                    CancellationEntry cancellationEntry = new CancellationEntry();

                    cancellationEntryCollection.add(cancellationEntry);
                    cancellationEntry.setOrderEntry(cancellation.getKey());
                    cancellationEntry.setQuantity(cancellation.getValue());
                    cancellationEntry.setReason("");
                }
        );
        return cancellationEntryCollection;
    }
}
