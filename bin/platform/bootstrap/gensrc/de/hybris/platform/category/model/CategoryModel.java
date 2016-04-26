/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 18.04.2016 18:26:54                         ---
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
package de.hybris.platform.category.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type Category first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CategoryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Category";
	
	/**<i>Generated relation code constant for relation <code>CategoryCategoryRelation</code> defining source attribute <code>supercategories</code> in extension <code>catalog</code>.</i>*/
	public final static String _CATEGORYCATEGORYRELATION = "CategoryCategoryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.description</code> attribute defined at extension <code>catalog</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.order</code> attribute defined at extension <code>catalog</code>. */
	public static final String ORDER = "order";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.normal</code> attribute defined at extension <code>catalog</code>. */
	public static final String NORMAL = "normal";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.thumbnails</code> attribute defined at extension <code>catalog</code>. */
	public static final String THUMBNAILS = "thumbnails";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.detail</code> attribute defined at extension <code>catalog</code>. */
	public static final String DETAIL = "detail";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.logo</code> attribute defined at extension <code>catalog</code>. */
	public static final String LOGO = "logo";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.data_sheet</code> attribute defined at extension <code>catalog</code>. */
	public static final String DATA_SHEET = "data_sheet";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.others</code> attribute defined at extension <code>catalog</code>. */
	public static final String OTHERS = "others";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.thumbnail</code> attribute defined at extension <code>catalog</code>. */
	public static final String THUMBNAIL = "thumbnail";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.picture</code> attribute defined at extension <code>catalog</code>. */
	public static final String PICTURE = "picture";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.name</code> attribute defined at extension <code>catalog</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.code</code> attribute defined at extension <code>catalog</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.allSubcategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String ALLSUBCATEGORIES = "allSubcategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.allSupercategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String ALLSUPERCATEGORIES = "allSupercategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.keywords</code> attribute defined at extension <code>catalog</code>. */
	public static final String KEYWORDS = "keywords";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.allowedPrincipals</code> attribute defined at extension <code>catalog</code>. */
	public static final String ALLOWEDPRINCIPALS = "allowedPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.products</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRODUCTS = "products";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.supercategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPERCATEGORIES = "supercategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.categories</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATEGORIES = "categories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.medias</code> attribute defined at extension <code>catalog</code>. */
	public static final String MEDIAS = "medias";
	
	
	/** <i>Generated variable</i> - Variable of <code>Category.order</code> attribute defined at extension <code>catalog</code>. */
	private Integer _order;
	
	/** <i>Generated variable</i> - Variable of <code>Category.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _catalogVersion;
	
	/** <i>Generated variable</i> - Variable of <code>Category.normal</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _normal;
	
	/** <i>Generated variable</i> - Variable of <code>Category.thumbnails</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _thumbnails;
	
	/** <i>Generated variable</i> - Variable of <code>Category.detail</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _detail;
	
	/** <i>Generated variable</i> - Variable of <code>Category.logo</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _logo;
	
	/** <i>Generated variable</i> - Variable of <code>Category.data_sheet</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _data_sheet;
	
	/** <i>Generated variable</i> - Variable of <code>Category.others</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _others;
	
	/** <i>Generated variable</i> - Variable of <code>Category.thumbnail</code> attribute defined at extension <code>catalog</code>. */
	private MediaModel _thumbnail;
	
	/** <i>Generated variable</i> - Variable of <code>Category.picture</code> attribute defined at extension <code>catalog</code>. */
	private MediaModel _picture;
	
	/** <i>Generated variable</i> - Variable of <code>Category.code</code> attribute defined at extension <code>catalog</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>Category.allSubcategories</code> attribute defined at extension <code>catalog</code>. */
	private Collection<CategoryModel> _allSubcategories;
	
	/** <i>Generated variable</i> - Variable of <code>Category.allSupercategories</code> attribute defined at extension <code>catalog</code>. */
	private Collection<CategoryModel> _allSupercategories;
	
	/** <i>Generated variable</i> - Variable of <code>Category.allowedPrincipals</code> attribute defined at extension <code>catalog</code>. */
	private List<PrincipalModel> _allowedPrincipals;
	
	/** <i>Generated variable</i> - Variable of <code>Category.products</code> attribute defined at extension <code>catalog</code>. */
	private List<ProductModel> _products;
	
	/** <i>Generated variable</i> - Variable of <code>Category.supercategories</code> attribute defined at extension <code>catalog</code>. */
	private List<CategoryModel> _supercategories;
	
	/** <i>Generated variable</i> - Variable of <code>Category.categories</code> attribute defined at extension <code>catalog</code>. */
	private List<CategoryModel> _categories;
	
	/** <i>Generated variable</i> - Variable of <code>Category.medias</code> attribute defined at extension <code>catalog</code>. */
	private List<MediaModel> _medias;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CategoryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CategoryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CategoryModel(final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CategoryModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.allowedPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allowedPrincipals - Principals which are allowed to access this catalog category
	 */
	@Accessor(qualifier = "allowedPrincipals", type = Accessor.Type.GETTER)
	public List<PrincipalModel> getAllowedPrincipals()
	{
		if (this._allowedPrincipals!=null)
		{
			return _allowedPrincipals;
		}
		return _allowedPrincipals = getPersistenceContext().getValue(ALLOWEDPRINCIPALS, _allowedPrincipals);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.allSubcategories</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSubcategories
	 */
	@Accessor(qualifier = "allSubcategories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getAllSubcategories()
	{
		return getPersistenceContext().getDynamicValue(this,ALLSUBCATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.allSupercategories</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSupercategories
	 */
	@Accessor(qualifier = "allSupercategories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getAllSupercategories()
	{
		return getPersistenceContext().getDynamicValue(this,ALLSUPERCATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersion - CatalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		if (this._catalogVersion!=null)
		{
			return _catalogVersion;
		}
		return _catalogVersion = getPersistenceContext().getValue(CATALOGVERSION, _catalogVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.categories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the categories - Sub Categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
	public List<CategoryModel> getCategories()
	{
		if (this._categories!=null)
		{
			return _categories;
		}
		return _categories = getPersistenceContext().getValue(CATEGORIES, _categories);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.code</code> attribute defined at extension <code>catalog</code>. 
	 * @return the code - Code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		if (this._code!=null)
		{
			return _code;
		}
		return _code = getPersistenceContext().getValue(CODE, _code);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.data_sheet</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the data_sheet
	 */
	@Accessor(qualifier = "data_sheet", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getData_sheet()
	{
		if (this._data_sheet!=null)
		{
			return _data_sheet;
		}
		return _data_sheet = getPersistenceContext().getValue(DATA_SHEET, _data_sheet);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.description</code> attribute defined at extension <code>catalog</code>. 
	 * @return the description - Catalog Category Description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.description</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the description - Catalog Category Description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.detail</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the detail
	 */
	@Accessor(qualifier = "detail", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getDetail()
	{
		if (this._detail!=null)
		{
			return _detail;
		}
		return _detail = getPersistenceContext().getValue(DETAIL, _detail);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.keywords</code> attribute defined at extension <code>catalog</code>. 
	 * @return the keywords - Keyword
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
	public List<KeywordModel> getKeywords()
	{
		return getKeywords(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.keywords</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the keywords - Keyword
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
	public List<KeywordModel> getKeywords(final Locale loc)
	{
		return getPersistenceContext().getLocalizedRelationValue(KEYWORDS, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.logo</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the logo
	 */
	@Accessor(qualifier = "logo", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getLogo()
	{
		if (this._logo!=null)
		{
			return _logo;
		}
		return _logo = getPersistenceContext().getValue(LOGO, _logo);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.medias</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the medias - Category medias
	 */
	@Accessor(qualifier = "medias", type = Accessor.Type.GETTER)
	public List<MediaModel> getMedias()
	{
		if (this._medias!=null)
		{
			return _medias;
		}
		return _medias = getPersistenceContext().getValue(MEDIAS, _medias);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.name</code> attribute defined at extension <code>catalog</code>. 
	 * @return the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.name</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the name - Name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.normal</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the normal
	 */
	@Accessor(qualifier = "normal", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getNormal()
	{
		if (this._normal!=null)
		{
			return _normal;
		}
		return _normal = getPersistenceContext().getValue(NORMAL, _normal);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.order</code> attribute defined at extension <code>catalog</code>. 
	 * @return the order - Order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public Integer getOrder()
	{
		if (this._order!=null)
		{
			return _order;
		}
		return _order = getPersistenceContext().getValue(ORDER, _order);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.others</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the others
	 */
	@Accessor(qualifier = "others", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getOthers()
	{
		if (this._others!=null)
		{
			return _others;
		}
		return _others = getPersistenceContext().getValue(OTHERS, _others);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.picture</code> attribute defined at extension <code>catalog</code>. 
	 * @return the picture - Image
	 */
	@Accessor(qualifier = "picture", type = Accessor.Type.GETTER)
	public MediaModel getPicture()
	{
		if (this._picture!=null)
		{
			return _picture;
		}
		return _picture = getPersistenceContext().getValue(PICTURE, _picture);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.products</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the products - Products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public List<ProductModel> getProducts()
	{
		if (this._products!=null)
		{
			return _products;
		}
		return _products = getPersistenceContext().getValue(PRODUCTS, _products);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.supercategories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the supercategories - Super Categories
	 */
	@Accessor(qualifier = "supercategories", type = Accessor.Type.GETTER)
	public List<CategoryModel> getSupercategories()
	{
		if (this._supercategories!=null)
		{
			return _supercategories;
		}
		return _supercategories = getPersistenceContext().getValue(SUPERCATEGORIES, _supercategories);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.thumbnail</code> attribute defined at extension <code>catalog</code>. 
	 * @return the thumbnail - Small Image
	 */
	@Accessor(qualifier = "thumbnail", type = Accessor.Type.GETTER)
	public MediaModel getThumbnail()
	{
		if (this._thumbnail!=null)
		{
			return _thumbnail;
		}
		return _thumbnail = getPersistenceContext().getValue(THUMBNAIL, _thumbnail);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.thumbnails</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the thumbnails
	 */
	@Accessor(qualifier = "thumbnails", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getThumbnails()
	{
		if (this._thumbnails!=null)
		{
			return _thumbnails;
		}
		return _thumbnails = getPersistenceContext().getValue(THUMBNAILS, _thumbnails);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.allowedPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the allowedPrincipals - Principals which are allowed to access this catalog category
	 */
	@Accessor(qualifier = "allowedPrincipals", type = Accessor.Type.SETTER)
	public void setAllowedPrincipals(final List<PrincipalModel> value)
	{
		_allowedPrincipals = getPersistenceContext().setValue(ALLOWEDPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersion - CatalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		_catalogVersion = getPersistenceContext().setValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.categories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categories - Sub Categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
	public void setCategories(final List<CategoryModel> value)
	{
		_categories = getPersistenceContext().setValue(CATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.code</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the code - Code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.data_sheet</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the data_sheet
	 */
	@Accessor(qualifier = "data_sheet", type = Accessor.Type.SETTER)
	public void setData_sheet(final Collection<MediaModel> value)
	{
		_data_sheet = getPersistenceContext().setValue(DATA_SHEET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.description</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the description - Catalog Category Description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Category.description</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the description - Catalog Category Description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.detail</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the detail
	 */
	@Accessor(qualifier = "detail", type = Accessor.Type.SETTER)
	public void setDetail(final Collection<MediaModel> value)
	{
		_detail = getPersistenceContext().setValue(DETAIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.keywords</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the keywords - Keyword
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
	public void setKeywords(final List<KeywordModel> value)
	{
		setKeywords(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Category.keywords</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the keywords - Keyword
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
	public void setKeywords(final List<KeywordModel> value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(KEYWORDS, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.logo</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the logo
	 */
	@Accessor(qualifier = "logo", type = Accessor.Type.SETTER)
	public void setLogo(final Collection<MediaModel> value)
	{
		_logo = getPersistenceContext().setValue(LOGO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.medias</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the medias - Category medias
	 */
	@Accessor(qualifier = "medias", type = Accessor.Type.SETTER)
	public void setMedias(final List<MediaModel> value)
	{
		_medias = getPersistenceContext().setValue(MEDIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Category.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.normal</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the normal
	 */
	@Accessor(qualifier = "normal", type = Accessor.Type.SETTER)
	public void setNormal(final Collection<MediaModel> value)
	{
		_normal = getPersistenceContext().setValue(NORMAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.order</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the order - Order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final Integer value)
	{
		_order = getPersistenceContext().setValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.others</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the others
	 */
	@Accessor(qualifier = "others", type = Accessor.Type.SETTER)
	public void setOthers(final Collection<MediaModel> value)
	{
		_others = getPersistenceContext().setValue(OTHERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.picture</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the picture - Image
	 */
	@Accessor(qualifier = "picture", type = Accessor.Type.SETTER)
	public void setPicture(final MediaModel value)
	{
		_picture = getPersistenceContext().setValue(PICTURE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.products</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the products - Products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final List<ProductModel> value)
	{
		_products = getPersistenceContext().setValue(PRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.supercategories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supercategories - Super Categories
	 */
	@Accessor(qualifier = "supercategories", type = Accessor.Type.SETTER)
	public void setSupercategories(final List<CategoryModel> value)
	{
		_supercategories = getPersistenceContext().setValue(SUPERCATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.thumbnail</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the thumbnail - Small Image
	 */
	@Accessor(qualifier = "thumbnail", type = Accessor.Type.SETTER)
	public void setThumbnail(final MediaModel value)
	{
		_thumbnail = getPersistenceContext().setValue(THUMBNAIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.thumbnails</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the thumbnails
	 */
	@Accessor(qualifier = "thumbnails", type = Accessor.Type.SETTER)
	public void setThumbnails(final Collection<MediaModel> value)
	{
		_thumbnails = getPersistenceContext().setValue(THUMBNAILS, value);
	}
	
}
