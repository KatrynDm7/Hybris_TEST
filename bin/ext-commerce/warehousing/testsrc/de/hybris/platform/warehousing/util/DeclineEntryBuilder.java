package de.hybris.platform.warehousing.util;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.allocation.DeclineEntries;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.enums.DeclineReason;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


/**
 * This is a DeclineEntry builder implementation of the Builder interface
 */
public class DeclineEntryBuilder
{

	public static DeclineEntryBuilder aDecline()
	{
		return new DeclineEntryBuilder();
	}

	public DeclineEntries build(final Map<ConsignmentEntryModel, Long> declineEntryInfo, final WarehouseModel warehouse)
	{

		final DeclineEntries declineEntries = new DeclineEntries();
		final Collection<DeclineEntry> entries = new ArrayList<>();
		declineEntryInfo.entrySet().stream().forEach(decline -> {
			final DeclineEntry entry = new DeclineEntry();
			entry.setConsignmentEntry(decline.getKey());
			entry.setQuantity(decline.getValue());
			entry.setReason(DeclineReason.OUTOFSTOCK);
			entry.setNotes("notes");
			entries.add(entry);
		});
		declineEntries.setEntries(entries);
		declineEntries.setReallocationWarehouse(warehouse);
		return declineEntries;
	}
}
