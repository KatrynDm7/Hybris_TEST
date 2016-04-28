/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 28.04.2016 16:51:49                         ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *  
 */
package de.hybris.platform.servicelayer.internal.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type IndexTestItem first defined at extension core.
 */
@SuppressWarnings("all")
public class IndexTestItemModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "IndexTestItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>IndexTestItem.column1</code> attribute defined at extension <code>core</code>. */
	public static final String COLUMN1 = "column1";
	
	/** <i>Generated constant</i> - Attribute key of <code>IndexTestItem.column2</code> attribute defined at extension <code>core</code>. */
	public static final String COLUMN2 = "column2";
	
	/** <i>Generated constant</i> - Attribute key of <code>IndexTestItem.column3</code> attribute defined at extension <code>core</code>. */
	public static final String COLUMN3 = "column3";
	
	/** <i>Generated constant</i> - Attribute key of <code>IndexTestItem.column4</code> attribute defined at extension <code>core</code>. */
	public static final String COLUMN4 = "column4";
	
	/** <i>Generated constant</i> - Attribute key of <code>IndexTestItem.column5</code> attribute defined at extension <code>core</code>. */
	public static final String COLUMN5 = "column5";
	
	
	/** <i>Generated variable</i> - Variable of <code>IndexTestItem.column1</code> attribute defined at extension <code>core</code>. */
	private Short _column1;
	
	/** <i>Generated variable</i> - Variable of <code>IndexTestItem.column2</code> attribute defined at extension <code>core</code>. */
	private Short _column2;
	
	/** <i>Generated variable</i> - Variable of <code>IndexTestItem.column3</code> attribute defined at extension <code>core</code>. */
	private Short _column3;
	
	/** <i>Generated variable</i> - Variable of <code>IndexTestItem.column4</code> attribute defined at extension <code>core</code>. */
	private Short _column4;
	
	/** <i>Generated variable</i> - Variable of <code>IndexTestItem.column5</code> attribute defined at extension <code>core</code>. */
	private Short _column5;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public IndexTestItemModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public IndexTestItemModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _column1 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column2 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column3 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column4 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column5 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 */
	@Deprecated
	public IndexTestItemModel(final Short _column1, final Short _column2, final Short _column3, final Short _column4, final Short _column5)
	{
		super();
		setColumn1(_column1);
		setColumn2(_column2);
		setColumn3(_column3);
		setColumn4(_column4);
		setColumn5(_column5);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _column1 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column2 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column3 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column4 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column5 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public IndexTestItemModel(final Short _column1, final Short _column2, final Short _column3, final Short _column4, final Short _column5, final ItemModel _owner)
	{
		super();
		setColumn1(_column1);
		setColumn2(_column2);
		setColumn3(_column3);
		setColumn4(_column4);
		setColumn5(_column5);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IndexTestItem.column1</code> attribute defined at extension <code>core</code>. 
	 * @return the column1
	 */
	@Accessor(qualifier = "column1", type = Accessor.Type.GETTER)
	public Short getColumn1()
	{
		if (this._column1!=null)
		{
			return _column1;
		}
		return _column1 = getPersistenceContext().getValue(COLUMN1, _column1);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IndexTestItem.column2</code> attribute defined at extension <code>core</code>. 
	 * @return the column2
	 */
	@Accessor(qualifier = "column2", type = Accessor.Type.GETTER)
	public Short getColumn2()
	{
		if (this._column2!=null)
		{
			return _column2;
		}
		return _column2 = getPersistenceContext().getValue(COLUMN2, _column2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IndexTestItem.column3</code> attribute defined at extension <code>core</code>. 
	 * @return the column3
	 */
	@Accessor(qualifier = "column3", type = Accessor.Type.GETTER)
	public Short getColumn3()
	{
		if (this._column3!=null)
		{
			return _column3;
		}
		return _column3 = getPersistenceContext().getValue(COLUMN3, _column3);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IndexTestItem.column4</code> attribute defined at extension <code>core</code>. 
	 * @return the column4
	 */
	@Accessor(qualifier = "column4", type = Accessor.Type.GETTER)
	public Short getColumn4()
	{
		if (this._column4!=null)
		{
			return _column4;
		}
		return _column4 = getPersistenceContext().getValue(COLUMN4, _column4);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IndexTestItem.column5</code> attribute defined at extension <code>core</code>. 
	 * @return the column5
	 */
	@Accessor(qualifier = "column5", type = Accessor.Type.GETTER)
	public Short getColumn5()
	{
		if (this._column5!=null)
		{
			return _column5;
		}
		return _column5 = getPersistenceContext().getValue(COLUMN5, _column5);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>IndexTestItem.column1</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the column1
	 */
	@Accessor(qualifier = "column1", type = Accessor.Type.SETTER)
	public void setColumn1(final Short value)
	{
		_column1 = getPersistenceContext().setValue(COLUMN1, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>IndexTestItem.column2</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the column2
	 */
	@Accessor(qualifier = "column2", type = Accessor.Type.SETTER)
	public void setColumn2(final Short value)
	{
		_column2 = getPersistenceContext().setValue(COLUMN2, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>IndexTestItem.column3</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the column3
	 */
	@Accessor(qualifier = "column3", type = Accessor.Type.SETTER)
	public void setColumn3(final Short value)
	{
		_column3 = getPersistenceContext().setValue(COLUMN3, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>IndexTestItem.column4</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the column4
	 */
	@Accessor(qualifier = "column4", type = Accessor.Type.SETTER)
	public void setColumn4(final Short value)
	{
		_column4 = getPersistenceContext().setValue(COLUMN4, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>IndexTestItem.column5</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the column5
	 */
	@Accessor(qualifier = "column5", type = Accessor.Type.SETTER)
	public void setColumn5(final Short value)
	{
		_column5 = getPersistenceContext().setValue(COLUMN5, value);
	}
	
}
