package de.hybris.platform.warehousing.util.dao;

public interface WarehousingDao<T>
{
	public T getByCode(String code);
}
