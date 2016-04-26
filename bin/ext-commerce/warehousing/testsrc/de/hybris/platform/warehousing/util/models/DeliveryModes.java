package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.daos.DeliveryModeDao;
import de.hybris.platform.warehousing.util.builder.DeliveryModeModelBuilder;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class DeliveryModes extends AbstractItems<DeliveryModeModel>
{
	public static final String CODE_PICKUP = "pickup";
	public static final String CODE_REGULAR = "regular";

	private DeliveryModeDao deliveryModeDao;

	public DeliveryModeModel Pickup()
	{
		return getFromCollectionOrSaveAndReturn(() -> getDeliveryModeDao().findDeliveryModesByCode(CODE_PICKUP), //
				() -> DeliveryModeModelBuilder.aModel() //
						.withCode(CODE_PICKUP) //
						.withActive(Boolean.TRUE) //
						.withName("Pickup", Locale.ENGLISH) //
						.build());
	}

	public DeliveryModeModel Regular()
	{
		return getFromCollectionOrSaveAndReturn(() -> getDeliveryModeDao().findDeliveryModesByCode(CODE_REGULAR), //
				() -> DeliveryModeModelBuilder.aModel() //
						.withCode(CODE_REGULAR) //
						.withActive(Boolean.TRUE) //
						.withName("Regular Delivery", Locale.ENGLISH) //
						.build());
	}

	public DeliveryModeDao getDeliveryModeDao()
	{
		return deliveryModeDao;
	}

	@Required
	public void setDeliveryModeDao(final DeliveryModeDao deliveryModeDao)
	{
		this.deliveryModeDao = deliveryModeDao;
	}

}
