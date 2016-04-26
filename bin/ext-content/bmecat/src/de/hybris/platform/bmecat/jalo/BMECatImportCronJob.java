/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.bmecat.jalo;

import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.catalog.jalo.Keyword;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.cronjob.jalo.ChangeDescriptor;
import de.hybris.platform.europe1.jalo.TaxRow;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * Cronjob for BMECatImport
 */
public class BMECatImportCronJob extends GeneratedBMECatImportCronJob
{
	private static final Logger LOG = Logger.getLogger(BMECatImportCronJob.class.getName());

	/**
	 * Overrides super method to put default currency in currency map too.
	 */
	@Override
	public void setDefaultCurrency(final SessionContext ctx, final Currency currency)
	{
		super.setDefaultCurrency(ctx, currency);
		if (currency != null)
		{
			Map currencyMappings = getAllCurrencyMappings(ctx);
			currencyMappings = currencyMappings != null ? new HashMap(currencyMappings) : new HashMap();
			currencyMappings
					.put(getDefaultCurrencyIsoCode() != null ? getDefaultCurrencyIsoCode() : currency.getIsoCode(), currency);
			setAllCurrencyMappings(currencyMappings);
		}
		else if (getDefaultCurrencyIsoCode() != null)
		{
			Map currencyMappings = getAllCurrencyMappings(ctx);
			currencyMappings = currencyMappings != null ? new HashMap(currencyMappings) : new HashMap();
			currencyMappings.put(getDefaultCurrencyIsoCode(), null);
			setAllCurrencyMappings(currencyMappings);
		}
	}

	protected void undoLanguages()
	{
		for (final Iterator it = getChanges(BMECatConstants.ChangeTypes.CREATE_LANGUAGE).iterator(); it.hasNext();)
		{
			final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
			final Language language = (Language) changeDescriptor.getChangedItem();
			try
			{
				language.setActive(false);
				language.remove();
				changeDescriptor.remove();
			}
			catch (final ConsistencyCheckException e)
			{
				throw new JaloSystemException(e);
			}
		}
	}

	protected void undoCurrencies()
	{
		for (final Iterator it = getChanges(BMECatConstants.ChangeTypes.CREATE_CURRENCY).iterator(); it.hasNext();)
		{
			final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
			final Currency currency = (Currency) changeDescriptor.getChangedItem();
			try
			{
				currency.setActive(false);
				currency.remove();
				changeDescriptor.remove();
			}
			catch (final ConsistencyCheckException e)
			{
				throw new JaloSystemException(e);
			}
		}
	}

	protected void undoCountries()
	{
		for (final Iterator it = getChanges(BMECatConstants.ChangeTypes.CREATE_COUNTRY).iterator(); it.hasNext();)
		{
			final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
			final Country country = (Country) changeDescriptor.getChangedItem();
			try
			{
				country.remove();
				changeDescriptor.remove();
			}
			catch (final ConsistencyCheckException e)
			{
				throw new JaloSystemException(e);
			}
		}
	}

	protected void undoUnits()
	{
		for (final Iterator it = getChanges(BMECatConstants.ChangeTypes.CREATE_UNIT).iterator(); it.hasNext();)
		{
			final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
			final Unit unit = (Unit) changeDescriptor.getChangedItem();
			try
			{
				unit.remove();
				changeDescriptor.remove();
			}
			catch (final ConsistencyCheckException e)
			{
				throw new JaloSystemException(e);
			}
		}
	}

	public Unit getOrCreateUnit(final String code)
	{
		if (code == null)
		{
			return null;
		}

		Unit unit = getMappedUnit(code);
		if (unit == null)
		{
			final Collection coll = getSession().getProductManager().getUnitsByCode(code);
			if (coll.isEmpty())
			{
				unit = getSession().getProductManager().createUnit(code, code);
				addChangeDescriptor(null, BMECatConstants.ChangeTypes.CREATE_UNIT, unit, "created non existing unit '" + code + "'");
				return unit;
			}
			else
			{
				unit = (Unit) coll.iterator().next();
				if (coll.size() > 1)
				{
					LOG.warn("multiple units found for code '" + code + "' : " + coll + " - choosing " + unit);
				}
			}
			addToUnitMapping(code, unit);
		}
		return unit;
	}

	public Currency getOrCreateCurrency(final de.hybris.platform.bmecat.parser.Catalog catalog, final String priceIsoCode)
	{
		final String iso = priceIsoCode == null || "".equals(priceIsoCode) ? catalog.getDefaultCurrency() : priceIsoCode;
		if (iso == null)
		{
			return null;
		}
		Currency currency = getMappedCurrency(iso);
		if (currency == null)
		{
			try
			{
				currency = getSession().getC2LManager().getCurrencyByIsoCode(iso);
			}
			catch (final JaloItemNotFoundException e)
			{
				try
				{
					currency = getSession().getC2LManager().createCurrency(iso);
					addChangeDescriptor(null, BMECatConstants.ChangeTypes.CREATE_CURRENCY, currency, "created non existing currency '"
							+ iso + "'");
				}
				catch (final ConsistencyCheckException e2)
				{
					throw new JaloInternalException(e2);
				}
			}
			addToCurrencyMapping(iso, currency);
		}
		return currency;
	}

	/**
	 * Returns a collection containing jalo {@link Country} jalo according to the iso codes specified in the
	 * <code>String</code> collection of the parameter territoryIsoCodes. If there exists no {@link Country} for a
	 * specified iso code, this {@link Country} will be created.
	 * 
	 * @param territoryIsocodes
	 *           a collection of iso codes (<code>String</code>s)
	 * @return Collection of countries
	 */
	public Collection getOrCreateCountries(final Collection territoryIsocodes)
	{
		if (territoryIsocodes == null || territoryIsocodes.isEmpty())
		{
			return null;
		}

		final Collection territories = new ArrayList();
		for (final Iterator it = territoryIsocodes.iterator(); it.hasNext();)
		{
			final String isoCode = (String) it.next();
			territories.add(getOrCreateCountry(isoCode));
		}
		return territories;
	}

	/**
	 * Locates a jalo language item for a given iso code. If the language does not exist it will be created.
	 * 
	 * @param isoCode
	 *           the iso code to search the language for
	 * @return the language with the given isoCode
	 */
	public Language getOrCreateCatalogLanguage(final String isoCode)
	{
		Language language = getImportLanguage(); // try to get preset language first
		if (language == null)
		{
			try
			{
				language = getSession().getC2LManager().getLanguageByIsoCode(isoCode);
			}
			catch (final JaloItemNotFoundException exp)
			{
				LOG.warn("Language with isocode '" + isoCode + "' not found! Creating language!");
				try
				{
					language = getSession().getC2LManager().createLanguage(isoCode);
					language.setActive(true);
					language.setName(isoCode);
					addChangeDescriptor(null, BMECatConstants.ChangeTypes.CREATE_LANGUAGE, language,
							"created non existing language for iso code '" + isoCode + "'");
				}
				catch (final ConsistencyCheckException e)
				{
					LOG.error("Language with isocode '" + isoCode + "' could not be created!");
					e.printStackTrace();
				}
			}
			setImportLanguage(language);
		}
		return language;
	}

	/**
	 * @param isoCode
	 * @return Country
	 */
	public Country getOrCreateCountry(final String isoCode)
	{
		if (isoCode == null)
		{
			return null;
		}
		Country country = getMappedCountry(isoCode);
		if (country == null)
		{
			try
			{
				country = getSession().getC2LManager().getCountryByIsoCode(isoCode);
			}
			catch (final JaloItemNotFoundException exp)
			{
				try
				{
					country = getSession().getC2LManager().createCountry(isoCode);
					country.setActive(true);
					addChangeDescriptor(null, BMECatConstants.ChangeTypes.CREATE_COUNTRY, country, "created non existing country '"
							+ isoCode + "'");
				}
				catch (final ConsistencyCheckException e)
				{
					LOG.error("Could not create country with isoCode '" + isoCode + "'! (" + e.getMessage() + ")");
					throw new JaloSystemException(e);
				}
			}
			addToCountryMapping(isoCode, country);
		}
		return country;
	}

	public Country getCountry(final String isoCode)
	{
		if (isoCode == null)
		{
			return null;
		}
		Country country = getMappedCountry(isoCode);
		if (country != null)
		{
			try
			{
				country = getSession().getC2LManager().getCountryByIsoCode(isoCode);
			}
			catch (final JaloItemNotFoundException exp)
			{
				LOG.warn("country with isoCode '" + isoCode + "' not found!");
			}
		}

		if (country != null)
		{
			addToCountryMapping(isoCode, country);
		}

		return country;
	}

	protected Country getMappedCountry(final String isoCode)
	{
		final Map countryMapping = getAllCountryMappings();
		return countryMapping != null ? (Country) countryMapping.get(isoCode) : null;
	}

	protected void addToCountryMapping(final String code, final Country country)
	{
		final Map current = getAllCountryMappings();
		final Map map = current != null ? new HashMap(current) : new HashMap();

		final boolean contained = map.containsKey(code);
		final Object prev = map.put(code, country);
		if (!contained || (prev != code && (prev == null || !prev.equals(code))))
		{
			setAllCountryMappings(map);
		}
	}

	protected EnumerationValue getMappedUserPriceGroup(final EnumerationValue priceType)
	{
		final Map priceMapping = getAllPriceTypeMappings();
		return priceMapping != null ? (EnumerationValue) priceMapping.get(priceType) : null;
	}

	protected void addToPriceTypeMapping(final EnumerationValue priceType, final EnumerationValue userPriceGroup)
	{
		final Map current = getAllPriceTypeMappings();
		final Map map = current != null ? new HashMap(current) : new HashMap();
		final boolean contained = map.containsKey(priceType);
		final Object prev = map.put(priceType, userPriceGroup);
		if (!contained || (prev != userPriceGroup && (prev == null || !prev.equals(userPriceGroup))))
		{
			setAllPriceTypeMappings(map);
		}
	}

	protected void addToTaxTypeMapping(final String taxType, final Object value)
	{
		final Map current = getAllTaxTypeMappings();
		final Map map = current != null ? new HashMap(current) : new HashMap();
		final boolean contained = map.containsKey(taxType);
		final Object prev = map.put(taxType, value);
		/*
		 * System.out.println("\n--== DUMP TAXTYPEMAPPING ==--"); for(Iterator it = map.entrySet().iterator();
		 * it.hasNext();) { Map.Entry me = (Map.Entry)it.next(); System.out.println( me.getKey() + " // " + me.getValue()
		 * ); }
		 */
		if (!contained || (prev != value && (prev == null || !prev.equals(value))))
		{
			setAllTaxTypeMappings(map);
		}
	}

	protected TaxRow getMappedTaxType(final String bmecat)
	{
		final Map taxTypeMapping = getAllTaxTypeMappings();
		return taxTypeMapping != null ? (TaxRow) taxTypeMapping.get(bmecat) : null;
	}


	protected Currency getMappedCurrency(final String isoCode)
	{
		final Map currencyMapping = getAllCurrencyMappings();
		return currencyMapping != null ? (Currency) currencyMapping.get(isoCode) : null;
	}

	protected void addToCurrencyMapping(final String code, final Currency currency)
	{
		final Map current = getAllCurrencyMappings();
		final Map map = current != null ? new HashMap(current) : new HashMap();

		final boolean contained = map.containsKey(code);
		final Object prev = map.put(code, currency);
		if (!contained || (prev != code && (prev == null || !prev.equals(code))))
		{
			setAllCurrencyMappings(map);
		}
	}

	protected ClassificationSystemVersion getMappedClassification(final String qualifier)
	{
		final Map classificationMapping = getAllClassificationMappings();
		return classificationMapping != null ? (ClassificationSystemVersion) classificationMapping.get(qualifier) : null;
	}

	protected void addToClassificationMapping(final String qualifier, final ClassificationSystemVersion classificationSystemVersion)
	{
		final Map current = getAllClassificationMappings();
		final Map map = current != null ? new HashMap(current) : new HashMap();

		final boolean contained = map.containsKey(qualifier);
		final Object prev = map.put(qualifier, classificationSystemVersion);
		if (!contained || (prev != qualifier && (prev == null || !prev.equals(qualifier))))
		{
			setAllClassificationMappings(map);
		}
	}

	protected Unit getMappedUnit(final String code)
	{
		final Map unitMapping = getAllUnitMappings();
		return unitMapping != null ? (Unit) unitMapping.get(code) : null;
	}

	protected void addToUnitMapping(final String code, final Unit unit)
	{
		final Map current = getAllUnitMappings();
		final Map map = current != null ? new HashMap(current) : new HashMap();
		map.put(code, unit);
		setAllUnitMappings(map);
	}

	/**
	 * Gets singleExecutable
	 * 
	 * @param ctx
	 *           session context
	 * 
	 * @return singleExecutable
	 */
	@Override
	public java.lang.Boolean isSingleExecutable(final SessionContext ctx)
	{
		return Boolean.TRUE;
	}

	/**
	 * @see de.hybris.platform.bmecat.jalo.GeneratedBMECatImportCronJob#getKeywordType(de.hybris.platform.jalo.SessionContext)
	 */
	@Override
	public ComposedType getKeywordType(final SessionContext ctx)
	{
		final ComposedType customKeywordType = super.getKeywordType(ctx);
		if (customKeywordType == null)
		{
			return getSession().getTypeManager().getComposedType(Keyword.class);
		}
		return customKeywordType;
	}

	/**
	 * Overwritten to avoid NULL as return value.
	 */
	@Override
	public Map getAllPriceTypeMappings(final SessionContext ctx)
	{
		final Map ret = super.getAllPriceTypeMappings(ctx);
		return ret != null ? ret : Collections.EMPTY_MAP;
	}

	/**
	 * Overwritten to avoid NULL as return value.
	 */
	@Override
	public Map getAllTaxTypeMappings(final SessionContext ctx)
	{
		final Map ret = super.getAllTaxTypeMappings(ctx);
		return ret != null ? ret : Collections.EMPTY_MAP;
	}

	/**
	 * Overwritten to avoid NULL as return value.
	 */
	@Override
	public Map getAllCurrencyMappings(final SessionContext ctx)
	{
		final Map ret = super.getAllCurrencyMappings(ctx);
		return ret != null ? ret : Collections.EMPTY_MAP;
	}

	/**
	 * Overwritten to avoid NULL as return value.
	 */
	@Override
	public Map getAllClassificationMappings(final SessionContext ctx)
	{
		final Map ret = super.getAllClassificationMappings(ctx);
		return ret != null ? ret : Collections.EMPTY_MAP;
	}

	/**
	 * Overwritten to avoid NULL as return value.
	 */
	@Override
	public Map getAllCountryMappings(final SessionContext ctx)
	{
		final Map ret = super.getAllCountryMappings(ctx);
		return ret != null ? ret : Collections.EMPTY_MAP;
	}

	/**
	 * Overwritten to avoid NULL as return value.
	 */
	@Override
	public Map getAllUnitMappings(final SessionContext ctx)
	{
		final Map ret = super.getAllUnitMappings(ctx);
		return ret != null ? ret : Collections.EMPTY_MAP;
	}

	private de.hybris.platform.bmecat.parser.Catalog cataloValueObject = null;

	public void setCataloValueObject(final de.hybris.platform.bmecat.parser.Catalog cat)
	{
		cataloValueObject = cat;
	}

	public de.hybris.platform.bmecat.parser.Catalog getCataloValueObject()
	{
		return cataloValueObject;
	}

	/**
	 * START: HACK (BMECAT-167)
	 */
	private boolean errorFlag = false;
	private String msg = null;

	/**
	 * HACK: notifies the cronjob, that some critical errors has occured! (BMECAT-167) ...used by @see
	 * BMECatInfoStep#importBMECatObject( Catalog catalog, AbstractValueObject object, BMECatImportCronJob cronJob )
	 * 
	 * Note: parsing the assigned logfiles of the cronjob for 'ERROR' entries is awful, too!
	 */
	public void setErrorFlag(final boolean errorFlag)
	{
		this.errorFlag = errorFlag;
	}

	/**
	 * returns the 'critical errors occured flag' ...used by @see BMECatImportWizard#tabChanges( WizardEditorContext ctx,
	 * String fromTabName, String toTabName )
	 */
	public boolean getErrorFlag() // NOPMD
	{
		return errorFlag;
	}

	public String getCriticalErrorMsg()
	{
		return msg;
	}

	public void setCriticalErrorMsg(final String msg)
	{
		this.msg = msg;
	}
	/**
	 * END: HACK (BMECAT-167)
	 */
}
