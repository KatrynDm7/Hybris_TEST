package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.daos.UnitDao;
import de.hybris.platform.warehousing.util.builder.UnitModelBuilder;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class Units extends AbstractItems<UnitModel>
{
	public static final String UNIT_TYPE_QUANTITY = "quantity";
	public static final String CODE_PIECE = "piece";
	public static final String CODE_UNIT = "unit";

	private UnitDao unitDao;

	public UnitModel Piece()
	{
		return getFromCollectionOrSaveAndReturn(() -> getUnitDao().findUnitsByCode(CODE_PIECE), //
				() -> UnitModelBuilder.aModel() //
						.withUnitType(UNIT_TYPE_QUANTITY) //
						.withCode(CODE_PIECE) //
						.withName(CODE_PIECE, Locale.ENGLISH) //
						.build());
	}

	public UnitModel Unit()
	{
		return getFromCollectionOrSaveAndReturn(() -> getUnitDao().findUnitsByCode(CODE_UNIT), //
				() -> UnitModelBuilder.aModel() //
						.withUnitType(UNIT_TYPE_QUANTITY) //
						.withCode(CODE_UNIT) //
						.withName(CODE_UNIT, Locale.ENGLISH) //
						.build());
	}

	public UnitDao getUnitDao()
	{
		return unitDao;
	}

	@Required
	public void setUnitDao(final UnitDao unitDao)
	{
		this.unitDao = unitDao;
	}
}
