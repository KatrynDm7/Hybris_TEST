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
package de.hybris.platform.core.model.product;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.enums.ArticleStatus;
import de.hybris.platform.catalog.enums.IDType;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.enums.ProductTaxGroup;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Generated model class for type Product first defined at extension core.
 */
@SuppressWarnings("all")
public class ProductModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Product";
	
	/**<i>Generated relation code constant for relation <code>Product2KeywordRelation</code> defining source attribute <code>keywords</code> in extension <code>catalog</code>.</i>*/
	public final static String _PRODUCT2KEYWORDRELATION = "Product2KeywordRelation";
	
	/**<i>Generated relation code constant for relation <code>CategoryProductRelation</code> defining source attribute <code>supercategories</code> in extension <code>catalog</code>.</i>*/
	public final static String _CATEGORYPRODUCTRELATION = "CategoryProductRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.unit</code> attribute defined at extension <code>core</code>. */
	public static final String UNIT = "unit";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.description</code> attribute defined at extension <code>core</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.thumbnail</code> attribute defined at extension <code>core</code>. */
	public static final String THUMBNAIL = "thumbnail";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.picture</code> attribute defined at extension <code>core</code>. */
	public static final String PICTURE = "picture";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.onlineDate</code> attribute defined at extension <code>catalog</code>. */
	public static final String ONLINEDATE = "onlineDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.offlineDate</code> attribute defined at extension <code>catalog</code>. */
	public static final String OFFLINEDATE = "offlineDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.ean</code> attribute defined at extension <code>catalog</code>. */
	public static final String EAN = "ean";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.supplierAlternativeAID</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPPLIERALTERNATIVEAID = "supplierAlternativeAID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.buyerIDS</code> attribute defined at extension <code>catalog</code>. */
	public static final String BUYERIDS = "buyerIDS";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.manufacturerAID</code> attribute defined at extension <code>catalog</code>. */
	public static final String MANUFACTURERAID = "manufacturerAID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.manufacturerName</code> attribute defined at extension <code>catalog</code>. */
	public static final String MANUFACTURERNAME = "manufacturerName";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.manufacturerTypeDescription</code> attribute defined at extension <code>catalog</code>. */
	public static final String MANUFACTURERTYPEDESCRIPTION = "manufacturerTypeDescription";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.erpGroupBuyer</code> attribute defined at extension <code>catalog</code>. */
	public static final String ERPGROUPBUYER = "erpGroupBuyer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.erpGroupSupplier</code> attribute defined at extension <code>catalog</code>. */
	public static final String ERPGROUPSUPPLIER = "erpGroupSupplier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.deliveryTime</code> attribute defined at extension <code>catalog</code>. */
	public static final String DELIVERYTIME = "deliveryTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.specialTreatmentClasses</code> attribute defined at extension <code>catalog</code>. */
	public static final String SPECIALTREATMENTCLASSES = "specialTreatmentClasses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.remarks</code> attribute defined at extension <code>catalog</code>. */
	public static final String REMARKS = "remarks";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.segment</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEGMENT = "segment";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.order</code> attribute defined at extension <code>catalog</code>. */
	public static final String ORDER = "order";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.articleStatus</code> attribute defined at extension <code>catalog</code>. */
	public static final String ARTICLESTATUS = "articleStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.approvalStatus</code> attribute defined at extension <code>catalog</code>. */
	public static final String APPROVALSTATUS = "approvalStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.contentUnit</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONTENTUNIT = "contentUnit";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.numberContentUnits</code> attribute defined at extension <code>catalog</code>. */
	public static final String NUMBERCONTENTUNITS = "numberContentUnits";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.minOrderQuantity</code> attribute defined at extension <code>catalog</code>. */
	public static final String MINORDERQUANTITY = "minOrderQuantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.maxOrderQuantity</code> attribute defined at extension <code>catalog</code>. */
	public static final String MAXORDERQUANTITY = "maxOrderQuantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.orderQuantityInterval</code> attribute defined at extension <code>catalog</code>. */
	public static final String ORDERQUANTITYINTERVAL = "orderQuantityInterval";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.priceQuantity</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRICEQUANTITY = "priceQuantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.normal</code> attribute defined at extension <code>catalog</code>. */
	public static final String NORMAL = "normal";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.thumbnails</code> attribute defined at extension <code>catalog</code>. */
	public static final String THUMBNAILS = "thumbnails";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.detail</code> attribute defined at extension <code>catalog</code>. */
	public static final String DETAIL = "detail";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.logo</code> attribute defined at extension <code>catalog</code>. */
	public static final String LOGO = "logo";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.data_sheet</code> attribute defined at extension <code>catalog</code>. */
	public static final String DATA_SHEET = "data_sheet";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.others</code> attribute defined at extension <code>catalog</code>. */
	public static final String OTHERS = "others";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.features</code> attribute defined at extension <code>catalog</code>. */
	public static final String FEATURES = "features";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.untypedFeatures</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNTYPEDFEATURES = "untypedFeatures";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.classificationClasses</code> attribute defined at extension <code>catalog</code>. */
	public static final String CLASSIFICATIONCLASSES = "classificationClasses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.startLineNumber</code> attribute defined at extension <code>catalog</code>. */
	public static final String STARTLINENUMBER = "startLineNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.endLineNumber</code> attribute defined at extension <code>catalog</code>. */
	public static final String ENDLINENUMBER = "endLineNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.xmlcontent</code> attribute defined at extension <code>catalog</code>. */
	public static final String XMLCONTENT = "xmlcontent";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.variants</code> attribute defined at extension <code>catalog</code>. */
	public static final String VARIANTS = "variants";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.variantType</code> attribute defined at extension <code>catalog</code>. */
	public static final String VARIANTTYPE = "variantType";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.keywords</code> attribute defined at extension <code>catalog</code>. */
	public static final String KEYWORDS = "keywords";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.productReferences</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRODUCTREFERENCES = "productReferences";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.supercategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPERCATEGORIES = "supercategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.Europe1PriceFactory_PPG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_PPG = "Europe1PriceFactory_PPG";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.Europe1PriceFactory_PTG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_PTG = "Europe1PriceFactory_PTG";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.Europe1PriceFactory_PDG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_PDG = "Europe1PriceFactory_PDG";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.europe1Prices</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICES = "europe1Prices";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.europe1Taxes</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1TAXES = "europe1Taxes";
	
	/** <i>Generated constant</i> - Attribute key of <code>Product.europe1Discounts</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1DISCOUNTS = "europe1Discounts";
	
	
	/** <i>Generated variable</i> - Variable of <code>Product.code</code> attribute defined at extension <code>core</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>Product.unit</code> attribute defined at extension <code>core</code>. */
	private UnitModel _unit;
	
	/** <i>Generated variable</i> - Variable of <code>Product.thumbnail</code> attribute defined at extension <code>core</code>. */
	private MediaModel _thumbnail;
	
	/** <i>Generated variable</i> - Variable of <code>Product.picture</code> attribute defined at extension <code>core</code>. */
	private MediaModel _picture;
	
	/** <i>Generated variable</i> - Variable of <code>Product.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _catalogVersion;
	
	/** <i>Generated variable</i> - Variable of <code>Product.onlineDate</code> attribute defined at extension <code>catalog</code>. */
	private Date _onlineDate;
	
	/** <i>Generated variable</i> - Variable of <code>Product.offlineDate</code> attribute defined at extension <code>catalog</code>. */
	private Date _offlineDate;
	
	/** <i>Generated variable</i> - Variable of <code>Product.ean</code> attribute defined at extension <code>catalog</code>. */
	private String _ean;
	
	/** <i>Generated variable</i> - Variable of <code>Product.supplierAlternativeAID</code> attribute defined at extension <code>catalog</code>. */
	private String _supplierAlternativeAID;
	
	/** <i>Generated variable</i> - Variable of <code>Product.buyerIDS</code> attribute defined at extension <code>catalog</code>. */
	private Map<IDType,String> _buyerIDS;
	
	/** <i>Generated variable</i> - Variable of <code>Product.manufacturerAID</code> attribute defined at extension <code>catalog</code>. */
	private String _manufacturerAID;
	
	/** <i>Generated variable</i> - Variable of <code>Product.manufacturerName</code> attribute defined at extension <code>catalog</code>. */
	private String _manufacturerName;
	
	/** <i>Generated variable</i> - Variable of <code>Product.erpGroupBuyer</code> attribute defined at extension <code>catalog</code>. */
	private String _erpGroupBuyer;
	
	/** <i>Generated variable</i> - Variable of <code>Product.erpGroupSupplier</code> attribute defined at extension <code>catalog</code>. */
	private String _erpGroupSupplier;
	
	/** <i>Generated variable</i> - Variable of <code>Product.deliveryTime</code> attribute defined at extension <code>catalog</code>. */
	private Double _deliveryTime;
	
	/** <i>Generated variable</i> - Variable of <code>Product.specialTreatmentClasses</code> attribute defined at extension <code>catalog</code>. */
	private Map<String,String> _specialTreatmentClasses;
	
	/** <i>Generated variable</i> - Variable of <code>Product.order</code> attribute defined at extension <code>catalog</code>. */
	private Integer _order;
	
	/** <i>Generated variable</i> - Variable of <code>Product.approvalStatus</code> attribute defined at extension <code>catalog</code>. */
	private ArticleApprovalStatus _approvalStatus;
	
	/** <i>Generated variable</i> - Variable of <code>Product.contentUnit</code> attribute defined at extension <code>catalog</code>. */
	private UnitModel _contentUnit;
	
	/** <i>Generated variable</i> - Variable of <code>Product.numberContentUnits</code> attribute defined at extension <code>catalog</code>. */
	private Double _numberContentUnits;
	
	/** <i>Generated variable</i> - Variable of <code>Product.minOrderQuantity</code> attribute defined at extension <code>catalog</code>. */
	private Integer _minOrderQuantity;
	
	/** <i>Generated variable</i> - Variable of <code>Product.maxOrderQuantity</code> attribute defined at extension <code>catalog</code>. */
	private Integer _maxOrderQuantity;
	
	/** <i>Generated variable</i> - Variable of <code>Product.orderQuantityInterval</code> attribute defined at extension <code>catalog</code>. */
	private Integer _orderQuantityInterval;
	
	/** <i>Generated variable</i> - Variable of <code>Product.priceQuantity</code> attribute defined at extension <code>catalog</code>. */
	private Double _priceQuantity;
	
	/** <i>Generated variable</i> - Variable of <code>Product.normal</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _normal;
	
	/** <i>Generated variable</i> - Variable of <code>Product.thumbnails</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _thumbnails;
	
	/** <i>Generated variable</i> - Variable of <code>Product.detail</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _detail;
	
	/** <i>Generated variable</i> - Variable of <code>Product.logo</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _logo;
	
	/** <i>Generated variable</i> - Variable of <code>Product.data_sheet</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _data_sheet;
	
	/** <i>Generated variable</i> - Variable of <code>Product.others</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _others;
	
	/** <i>Generated variable</i> - Variable of <code>Product.features</code> attribute defined at extension <code>catalog</code>. */
	private List<ProductFeatureModel> _features;
	
	/** <i>Generated variable</i> - Variable of <code>Product.untypedFeatures</code> attribute defined at extension <code>catalog</code>. */
	private List<ProductFeatureModel> _untypedFeatures;
	
	/** <i>Generated variable</i> - Variable of <code>Product.classificationClasses</code> attribute defined at extension <code>catalog</code>. */
	private List<ClassificationClassModel> _classificationClasses;
	
	/** <i>Generated variable</i> - Variable of <code>Product.startLineNumber</code> attribute defined at extension <code>catalog</code>. */
	private Integer _startLineNumber;
	
	/** <i>Generated variable</i> - Variable of <code>Product.endLineNumber</code> attribute defined at extension <code>catalog</code>. */
	private Integer _endLineNumber;
	
	/** <i>Generated variable</i> - Variable of <code>Product.xmlcontent</code> attribute defined at extension <code>catalog</code>. */
	private String _xmlcontent;
	
	/** <i>Generated variable</i> - Variable of <code>Product.variants</code> attribute defined at extension <code>catalog</code>. */
	private Collection<VariantProductModel> _variants;
	
	/** <i>Generated variable</i> - Variable of <code>Product.variantType</code> attribute defined at extension <code>catalog</code>. */
	private VariantTypeModel _variantType;
	
	/** <i>Generated variable</i> - Variable of <code>Product.productReferences</code> attribute defined at extension <code>catalog</code>. */
	private Collection<ProductReferenceModel> _productReferences;
	
	/** <i>Generated variable</i> - Variable of <code>Product.supercategories</code> attribute defined at extension <code>catalog</code>. */
	private Collection<CategoryModel> _supercategories;
	
	/** <i>Generated variable</i> - Variable of <code>Product.Europe1PriceFactory_PPG</code> attribute defined at extension <code>europe1</code>. */
	private ProductPriceGroup _Europe1PriceFactory_PPG;
	
	/** <i>Generated variable</i> - Variable of <code>Product.Europe1PriceFactory_PTG</code> attribute defined at extension <code>europe1</code>. */
	private ProductTaxGroup _Europe1PriceFactory_PTG;
	
	/** <i>Generated variable</i> - Variable of <code>Product.Europe1PriceFactory_PDG</code> attribute defined at extension <code>europe1</code>. */
	private ProductDiscountGroup _Europe1PriceFactory_PDG;
	
	/** <i>Generated variable</i> - Variable of <code>Product.europe1Prices</code> attribute defined at extension <code>europe1</code>. */
	private Collection<PriceRowModel> _europe1Prices;
	
	/** <i>Generated variable</i> - Variable of <code>Product.europe1Taxes</code> attribute defined at extension <code>europe1</code>. */
	private Collection<TaxRowModel> _europe1Taxes;
	
	/** <i>Generated variable</i> - Variable of <code>Product.europe1Discounts</code> attribute defined at extension <code>europe1</code>. */
	private Collection<DiscountRowModel> _europe1Discounts;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Product</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Product</code> at extension <code>core</code>
	 */
	@Deprecated
	public ProductModel(final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Product</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Product</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ProductModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.approvalStatus</code> attribute defined at extension <code>catalog</code>. 
	 * @return the approvalStatus
	 */
	@Accessor(qualifier = "approvalStatus", type = Accessor.Type.GETTER)
	public ArticleApprovalStatus getApprovalStatus()
	{
		if (this._approvalStatus!=null)
		{
			return _approvalStatus;
		}
		return _approvalStatus = getPersistenceContext().getValue(APPROVALSTATUS, _approvalStatus);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.articleStatus</code> attribute defined at extension <code>catalog</code>. 
	 * @return the articleStatus
	 */
	@Accessor(qualifier = "articleStatus", type = Accessor.Type.GETTER)
	public Map<ArticleStatus,String> getArticleStatus()
	{
		return getArticleStatus(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.articleStatus</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the articleStatus
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "articleStatus", type = Accessor.Type.GETTER)
	public Map<ArticleStatus,String> getArticleStatus(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(ARTICLESTATUS, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.buyerIDS</code> attribute defined at extension <code>catalog</code>. 
	 * @return the buyerIDS
	 */
	@Accessor(qualifier = "buyerIDS", type = Accessor.Type.GETTER)
	public Map<IDType,String> getBuyerIDS()
	{
		if (this._buyerIDS!=null)
		{
			return _buyerIDS;
		}
		return _buyerIDS = getPersistenceContext().getValue(BUYERIDS, _buyerIDS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersion
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
	 * <i>Generated method</i> - Getter of the <code>Product.classificationClasses</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the classificationClasses
	 */
	@Accessor(qualifier = "classificationClasses", type = Accessor.Type.GETTER)
	public List<ClassificationClassModel> getClassificationClasses()
	{
		if (this._classificationClasses!=null)
		{
			return _classificationClasses;
		}
		return _classificationClasses = getPersistenceContext().getValue(CLASSIFICATIONCLASSES, _classificationClasses);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.code</code> attribute defined at extension <code>core</code>. 
	 * @return the code
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
	 * <i>Generated method</i> - Getter of the <code>Product.contentUnit</code> attribute defined at extension <code>catalog</code>. 
	 * @return the contentUnit
	 */
	@Accessor(qualifier = "contentUnit", type = Accessor.Type.GETTER)
	public UnitModel getContentUnit()
	{
		if (this._contentUnit!=null)
		{
			return _contentUnit;
		}
		return _contentUnit = getPersistenceContext().getValue(CONTENTUNIT, _contentUnit);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.data_sheet</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Product.deliveryTime</code> attribute defined at extension <code>catalog</code>. 
	 * @return the deliveryTime
	 */
	@Accessor(qualifier = "deliveryTime", type = Accessor.Type.GETTER)
	public Double getDeliveryTime()
	{
		if (this._deliveryTime!=null)
		{
			return _deliveryTime;
		}
		return _deliveryTime = getPersistenceContext().getValue(DELIVERYTIME, _deliveryTime);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.description</code> attribute defined at extension <code>core</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.description</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.detail</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Product.ean</code> attribute defined at extension <code>catalog</code>. 
	 * @return the ean
	 */
	@Accessor(qualifier = "ean", type = Accessor.Type.GETTER)
	public String getEan()
	{
		if (this._ean!=null)
		{
			return _ean;
		}
		return _ean = getPersistenceContext().getValue(EAN, _ean);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.endLineNumber</code> attribute defined at extension <code>catalog</code>. 
	 * @return the endLineNumber
	 */
	@Accessor(qualifier = "endLineNumber", type = Accessor.Type.GETTER)
	public Integer getEndLineNumber()
	{
		if (this._endLineNumber!=null)
		{
			return _endLineNumber;
		}
		return _endLineNumber = getPersistenceContext().getValue(ENDLINENUMBER, _endLineNumber);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.erpGroupBuyer</code> attribute defined at extension <code>catalog</code>. 
	 * @return the erpGroupBuyer
	 */
	@Accessor(qualifier = "erpGroupBuyer", type = Accessor.Type.GETTER)
	public String getErpGroupBuyer()
	{
		if (this._erpGroupBuyer!=null)
		{
			return _erpGroupBuyer;
		}
		return _erpGroupBuyer = getPersistenceContext().getValue(ERPGROUPBUYER, _erpGroupBuyer);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.erpGroupSupplier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the erpGroupSupplier
	 */
	@Accessor(qualifier = "erpGroupSupplier", type = Accessor.Type.GETTER)
	public String getErpGroupSupplier()
	{
		if (this._erpGroupSupplier!=null)
		{
			return _erpGroupSupplier;
		}
		return _erpGroupSupplier = getPersistenceContext().getValue(ERPGROUPSUPPLIER, _erpGroupSupplier);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.europe1Discounts</code> attribute defined at extension <code>europe1</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the europe1Discounts
	 */
	@Accessor(qualifier = "europe1Discounts", type = Accessor.Type.GETTER)
	public Collection<DiscountRowModel> getEurope1Discounts()
	{
		if (this._europe1Discounts!=null)
		{
			return _europe1Discounts;
		}
		return _europe1Discounts = getPersistenceContext().getValue(EUROPE1DISCOUNTS, _europe1Discounts);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.Europe1PriceFactory_PDG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_PDG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PDG", type = Accessor.Type.GETTER)
	public ProductDiscountGroup getEurope1PriceFactory_PDG()
	{
		if (this._Europe1PriceFactory_PDG!=null)
		{
			return _Europe1PriceFactory_PDG;
		}
		return _Europe1PriceFactory_PDG = getPersistenceContext().getValue(EUROPE1PRICEFACTORY_PDG, _Europe1PriceFactory_PDG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.Europe1PriceFactory_PPG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_PPG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PPG", type = Accessor.Type.GETTER)
	public ProductPriceGroup getEurope1PriceFactory_PPG()
	{
		if (this._Europe1PriceFactory_PPG!=null)
		{
			return _Europe1PriceFactory_PPG;
		}
		return _Europe1PriceFactory_PPG = getPersistenceContext().getValue(EUROPE1PRICEFACTORY_PPG, _Europe1PriceFactory_PPG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.Europe1PriceFactory_PTG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_PTG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PTG", type = Accessor.Type.GETTER)
	public ProductTaxGroup getEurope1PriceFactory_PTG()
	{
		if (this._Europe1PriceFactory_PTG!=null)
		{
			return _Europe1PriceFactory_PTG;
		}
		return _Europe1PriceFactory_PTG = getPersistenceContext().getValue(EUROPE1PRICEFACTORY_PTG, _Europe1PriceFactory_PTG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.europe1Prices</code> attribute defined at extension <code>europe1</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the europe1Prices
	 */
	@Accessor(qualifier = "europe1Prices", type = Accessor.Type.GETTER)
	public Collection<PriceRowModel> getEurope1Prices()
	{
		if (this._europe1Prices!=null)
		{
			return _europe1Prices;
		}
		return _europe1Prices = getPersistenceContext().getValue(EUROPE1PRICES, _europe1Prices);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.europe1Taxes</code> attribute defined at extension <code>europe1</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the europe1Taxes
	 */
	@Accessor(qualifier = "europe1Taxes", type = Accessor.Type.GETTER)
	public Collection<TaxRowModel> getEurope1Taxes()
	{
		if (this._europe1Taxes!=null)
		{
			return _europe1Taxes;
		}
		return _europe1Taxes = getPersistenceContext().getValue(EUROPE1TAXES, _europe1Taxes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.features</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the features
	 */
	@Accessor(qualifier = "features", type = Accessor.Type.GETTER)
	public List<ProductFeatureModel> getFeatures()
	{
		if (this._features!=null)
		{
			return _features;
		}
		return _features = getPersistenceContext().getValue(FEATURES, _features);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.keywords</code> attribute defined at extension <code>catalog</code>. 
	 * @return the keywords - Keywords
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
	public List<KeywordModel> getKeywords()
	{
		return getKeywords(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.keywords</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the keywords - Keywords
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
	public List<KeywordModel> getKeywords(final Locale loc)
	{
		return getPersistenceContext().getLocalizedRelationValue(KEYWORDS, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.logo</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Product.manufacturerAID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the manufacturerAID
	 */
	@Accessor(qualifier = "manufacturerAID", type = Accessor.Type.GETTER)
	public String getManufacturerAID()
	{
		if (this._manufacturerAID!=null)
		{
			return _manufacturerAID;
		}
		return _manufacturerAID = getPersistenceContext().getValue(MANUFACTURERAID, _manufacturerAID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.manufacturerName</code> attribute defined at extension <code>catalog</code>. 
	 * @return the manufacturerName
	 */
	@Accessor(qualifier = "manufacturerName", type = Accessor.Type.GETTER)
	public String getManufacturerName()
	{
		if (this._manufacturerName!=null)
		{
			return _manufacturerName;
		}
		return _manufacturerName = getPersistenceContext().getValue(MANUFACTURERNAME, _manufacturerName);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.manufacturerTypeDescription</code> attribute defined at extension <code>catalog</code>. 
	 * @return the manufacturerTypeDescription
	 */
	@Accessor(qualifier = "manufacturerTypeDescription", type = Accessor.Type.GETTER)
	public String getManufacturerTypeDescription()
	{
		return getManufacturerTypeDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.manufacturerTypeDescription</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the manufacturerTypeDescription
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "manufacturerTypeDescription", type = Accessor.Type.GETTER)
	public String getManufacturerTypeDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(MANUFACTURERTYPEDESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.maxOrderQuantity</code> attribute defined at extension <code>catalog</code>. 
	 * @return the maxOrderQuantity
	 */
	@Accessor(qualifier = "maxOrderQuantity", type = Accessor.Type.GETTER)
	public Integer getMaxOrderQuantity()
	{
		if (this._maxOrderQuantity!=null)
		{
			return _maxOrderQuantity;
		}
		return _maxOrderQuantity = getPersistenceContext().getValue(MAXORDERQUANTITY, _maxOrderQuantity);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.minOrderQuantity</code> attribute defined at extension <code>catalog</code>. 
	 * @return the minOrderQuantity
	 */
	@Accessor(qualifier = "minOrderQuantity", type = Accessor.Type.GETTER)
	public Integer getMinOrderQuantity()
	{
		if (this._minOrderQuantity!=null)
		{
			return _minOrderQuantity;
		}
		return _minOrderQuantity = getPersistenceContext().getValue(MINORDERQUANTITY, _minOrderQuantity);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.name</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.normal</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Product.numberContentUnits</code> attribute defined at extension <code>catalog</code>. 
	 * @return the numberContentUnits
	 */
	@Accessor(qualifier = "numberContentUnits", type = Accessor.Type.GETTER)
	public Double getNumberContentUnits()
	{
		if (this._numberContentUnits!=null)
		{
			return _numberContentUnits;
		}
		return _numberContentUnits = getPersistenceContext().getValue(NUMBERCONTENTUNITS, _numberContentUnits);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.offlineDate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the offlineDate
	 */
	@Accessor(qualifier = "offlineDate", type = Accessor.Type.GETTER)
	public Date getOfflineDate()
	{
		if (this._offlineDate!=null)
		{
			return _offlineDate;
		}
		return _offlineDate = getPersistenceContext().getValue(OFFLINEDATE, _offlineDate);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.onlineDate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the onlineDate
	 */
	@Accessor(qualifier = "onlineDate", type = Accessor.Type.GETTER)
	public Date getOnlineDate()
	{
		if (this._onlineDate!=null)
		{
			return _onlineDate;
		}
		return _onlineDate = getPersistenceContext().getValue(ONLINEDATE, _onlineDate);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.order</code> attribute defined at extension <code>catalog</code>. 
	 * @return the order
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
	 * <i>Generated method</i> - Getter of the <code>Product.orderQuantityInterval</code> attribute defined at extension <code>catalog</code>. 
	 * @return the orderQuantityInterval
	 */
	@Accessor(qualifier = "orderQuantityInterval", type = Accessor.Type.GETTER)
	public Integer getOrderQuantityInterval()
	{
		if (this._orderQuantityInterval!=null)
		{
			return _orderQuantityInterval;
		}
		return _orderQuantityInterval = getPersistenceContext().getValue(ORDERQUANTITYINTERVAL, _orderQuantityInterval);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.others</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Product.picture</code> attribute defined at extension <code>core</code>. 
	 * @return the picture
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
	 * <i>Generated method</i> - Getter of the <code>Product.priceQuantity</code> attribute defined at extension <code>catalog</code>. 
	 * @return the priceQuantity
	 */
	@Accessor(qualifier = "priceQuantity", type = Accessor.Type.GETTER)
	public Double getPriceQuantity()
	{
		if (this._priceQuantity!=null)
		{
			return _priceQuantity;
		}
		return _priceQuantity = getPersistenceContext().getValue(PRICEQUANTITY, _priceQuantity);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.productReferences</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the productReferences
	 */
	@Accessor(qualifier = "productReferences", type = Accessor.Type.GETTER)
	public Collection<ProductReferenceModel> getProductReferences()
	{
		if (this._productReferences!=null)
		{
			return _productReferences;
		}
		return _productReferences = getPersistenceContext().getValue(PRODUCTREFERENCES, _productReferences);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.remarks</code> attribute defined at extension <code>catalog</code>. 
	 * @return the remarks
	 */
	@Accessor(qualifier = "remarks", type = Accessor.Type.GETTER)
	public String getRemarks()
	{
		return getRemarks(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.remarks</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the remarks
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "remarks", type = Accessor.Type.GETTER)
	public String getRemarks(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(REMARKS, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.segment</code> attribute defined at extension <code>catalog</code>. 
	 * @return the segment
	 */
	@Accessor(qualifier = "segment", type = Accessor.Type.GETTER)
	public String getSegment()
	{
		return getSegment(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.segment</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the segment
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "segment", type = Accessor.Type.GETTER)
	public String getSegment(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(SEGMENT, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.specialTreatmentClasses</code> attribute defined at extension <code>catalog</code>. 
	 * @return the specialTreatmentClasses
	 */
	@Accessor(qualifier = "specialTreatmentClasses", type = Accessor.Type.GETTER)
	public Map<String,String> getSpecialTreatmentClasses()
	{
		if (this._specialTreatmentClasses!=null)
		{
			return _specialTreatmentClasses;
		}
		return _specialTreatmentClasses = getPersistenceContext().getValue(SPECIALTREATMENTCLASSES, _specialTreatmentClasses);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.startLineNumber</code> attribute defined at extension <code>catalog</code>. 
	 * @return the startLineNumber
	 */
	@Accessor(qualifier = "startLineNumber", type = Accessor.Type.GETTER)
	public Integer getStartLineNumber()
	{
		if (this._startLineNumber!=null)
		{
			return _startLineNumber;
		}
		return _startLineNumber = getPersistenceContext().getValue(STARTLINENUMBER, _startLineNumber);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.supercategories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the supercategories - Super Categories
	 */
	@Accessor(qualifier = "supercategories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getSupercategories()
	{
		if (this._supercategories!=null)
		{
			return _supercategories;
		}
		return _supercategories = getPersistenceContext().getValue(SUPERCATEGORIES, _supercategories);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.supplierAlternativeAID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the supplierAlternativeAID
	 */
	@Accessor(qualifier = "supplierAlternativeAID", type = Accessor.Type.GETTER)
	public String getSupplierAlternativeAID()
	{
		if (this._supplierAlternativeAID!=null)
		{
			return _supplierAlternativeAID;
		}
		return _supplierAlternativeAID = getPersistenceContext().getValue(SUPPLIERALTERNATIVEAID, _supplierAlternativeAID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.thumbnail</code> attribute defined at extension <code>core</code>. 
	 * @return the thumbnail
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
	 * <i>Generated method</i> - Getter of the <code>Product.thumbnails</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Product.unit</code> attribute defined at extension <code>core</code>. 
	 * @return the unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
	public UnitModel getUnit()
	{
		if (this._unit!=null)
		{
			return _unit;
		}
		return _unit = getPersistenceContext().getValue(UNIT, _unit);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.untypedFeatures</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the untypedFeatures
	 */
	@Accessor(qualifier = "untypedFeatures", type = Accessor.Type.GETTER)
	public List<ProductFeatureModel> getUntypedFeatures()
	{
		if (this._untypedFeatures!=null)
		{
			return _untypedFeatures;
		}
		return _untypedFeatures = getPersistenceContext().getValue(UNTYPEDFEATURES, _untypedFeatures);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.variants</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the variants
	 */
	@Accessor(qualifier = "variants", type = Accessor.Type.GETTER)
	public Collection<VariantProductModel> getVariants()
	{
		if (this._variants!=null)
		{
			return _variants;
		}
		return _variants = getPersistenceContext().getValue(VARIANTS, _variants);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.variantType</code> attribute defined at extension <code>catalog</code>. 
	 * @return the variantType
	 */
	@Accessor(qualifier = "variantType", type = Accessor.Type.GETTER)
	public VariantTypeModel getVariantType()
	{
		if (this._variantType!=null)
		{
			return _variantType;
		}
		return _variantType = getPersistenceContext().getValue(VARIANTTYPE, _variantType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.xmlcontent</code> attribute defined at extension <code>catalog</code>. 
	 * @return the xmlcontent
	 */
	@Accessor(qualifier = "xmlcontent", type = Accessor.Type.GETTER)
	public String getXmlcontent()
	{
		if (this._xmlcontent!=null)
		{
			return _xmlcontent;
		}
		return _xmlcontent = getPersistenceContext().getValue(XMLCONTENT, _xmlcontent);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.approvalStatus</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the approvalStatus
	 */
	@Accessor(qualifier = "approvalStatus", type = Accessor.Type.SETTER)
	public void setApprovalStatus(final ArticleApprovalStatus value)
	{
		_approvalStatus = getPersistenceContext().setValue(APPROVALSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.articleStatus</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the articleStatus
	 */
	@Accessor(qualifier = "articleStatus", type = Accessor.Type.SETTER)
	public void setArticleStatus(final Map<ArticleStatus,String> value)
	{
		setArticleStatus(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Product.articleStatus</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the articleStatus
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "articleStatus", type = Accessor.Type.SETTER)
	public void setArticleStatus(final Map<ArticleStatus,String> value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(ARTICLESTATUS, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.buyerIDS</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the buyerIDS
	 */
	@Accessor(qualifier = "buyerIDS", type = Accessor.Type.SETTER)
	public void setBuyerIDS(final Map<IDType,String> value)
	{
		_buyerIDS = getPersistenceContext().setValue(BUYERIDS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		_catalogVersion = getPersistenceContext().setValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.code</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.contentUnit</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the contentUnit
	 */
	@Accessor(qualifier = "contentUnit", type = Accessor.Type.SETTER)
	public void setContentUnit(final UnitModel value)
	{
		_contentUnit = getPersistenceContext().setValue(CONTENTUNIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.data_sheet</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the data_sheet
	 */
	@Accessor(qualifier = "data_sheet", type = Accessor.Type.SETTER)
	public void setData_sheet(final Collection<MediaModel> value)
	{
		_data_sheet = getPersistenceContext().setValue(DATA_SHEET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.deliveryTime</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the deliveryTime
	 */
	@Accessor(qualifier = "deliveryTime", type = Accessor.Type.SETTER)
	public void setDeliveryTime(final Double value)
	{
		_deliveryTime = getPersistenceContext().setValue(DELIVERYTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Product.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.detail</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the detail
	 */
	@Accessor(qualifier = "detail", type = Accessor.Type.SETTER)
	public void setDetail(final Collection<MediaModel> value)
	{
		_detail = getPersistenceContext().setValue(DETAIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.ean</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the ean
	 */
	@Accessor(qualifier = "ean", type = Accessor.Type.SETTER)
	public void setEan(final String value)
	{
		_ean = getPersistenceContext().setValue(EAN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.endLineNumber</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the endLineNumber
	 */
	@Accessor(qualifier = "endLineNumber", type = Accessor.Type.SETTER)
	public void setEndLineNumber(final Integer value)
	{
		_endLineNumber = getPersistenceContext().setValue(ENDLINENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.erpGroupBuyer</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the erpGroupBuyer
	 */
	@Accessor(qualifier = "erpGroupBuyer", type = Accessor.Type.SETTER)
	public void setErpGroupBuyer(final String value)
	{
		_erpGroupBuyer = getPersistenceContext().setValue(ERPGROUPBUYER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.erpGroupSupplier</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the erpGroupSupplier
	 */
	@Accessor(qualifier = "erpGroupSupplier", type = Accessor.Type.SETTER)
	public void setErpGroupSupplier(final String value)
	{
		_erpGroupSupplier = getPersistenceContext().setValue(ERPGROUPSUPPLIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.europe1Discounts</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the europe1Discounts
	 */
	@Accessor(qualifier = "europe1Discounts", type = Accessor.Type.SETTER)
	public void setEurope1Discounts(final Collection<DiscountRowModel> value)
	{
		_europe1Discounts = getPersistenceContext().setValue(EUROPE1DISCOUNTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.Europe1PriceFactory_PDG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_PDG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PDG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_PDG(final ProductDiscountGroup value)
	{
		_Europe1PriceFactory_PDG = getPersistenceContext().setValue(EUROPE1PRICEFACTORY_PDG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.Europe1PriceFactory_PPG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_PPG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PPG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_PPG(final ProductPriceGroup value)
	{
		_Europe1PriceFactory_PPG = getPersistenceContext().setValue(EUROPE1PRICEFACTORY_PPG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.Europe1PriceFactory_PTG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_PTG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PTG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_PTG(final ProductTaxGroup value)
	{
		_Europe1PriceFactory_PTG = getPersistenceContext().setValue(EUROPE1PRICEFACTORY_PTG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.europe1Prices</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the europe1Prices
	 */
	@Accessor(qualifier = "europe1Prices", type = Accessor.Type.SETTER)
	public void setEurope1Prices(final Collection<PriceRowModel> value)
	{
		_europe1Prices = getPersistenceContext().setValue(EUROPE1PRICES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.europe1Taxes</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the europe1Taxes
	 */
	@Accessor(qualifier = "europe1Taxes", type = Accessor.Type.SETTER)
	public void setEurope1Taxes(final Collection<TaxRowModel> value)
	{
		_europe1Taxes = getPersistenceContext().setValue(EUROPE1TAXES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.features</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the features
	 */
	@Accessor(qualifier = "features", type = Accessor.Type.SETTER)
	public void setFeatures(final List<ProductFeatureModel> value)
	{
		_features = getPersistenceContext().setValue(FEATURES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.keywords</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the keywords - Keywords
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
	public void setKeywords(final List<KeywordModel> value)
	{
		setKeywords(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Product.keywords</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the keywords - Keywords
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
	public void setKeywords(final List<KeywordModel> value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(KEYWORDS, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.logo</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the logo
	 */
	@Accessor(qualifier = "logo", type = Accessor.Type.SETTER)
	public void setLogo(final Collection<MediaModel> value)
	{
		_logo = getPersistenceContext().setValue(LOGO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.manufacturerAID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the manufacturerAID
	 */
	@Accessor(qualifier = "manufacturerAID", type = Accessor.Type.SETTER)
	public void setManufacturerAID(final String value)
	{
		_manufacturerAID = getPersistenceContext().setValue(MANUFACTURERAID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.manufacturerName</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the manufacturerName
	 */
	@Accessor(qualifier = "manufacturerName", type = Accessor.Type.SETTER)
	public void setManufacturerName(final String value)
	{
		_manufacturerName = getPersistenceContext().setValue(MANUFACTURERNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.manufacturerTypeDescription</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the manufacturerTypeDescription
	 */
	@Accessor(qualifier = "manufacturerTypeDescription", type = Accessor.Type.SETTER)
	public void setManufacturerTypeDescription(final String value)
	{
		setManufacturerTypeDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Product.manufacturerTypeDescription</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the manufacturerTypeDescription
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "manufacturerTypeDescription", type = Accessor.Type.SETTER)
	public void setManufacturerTypeDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(MANUFACTURERTYPEDESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.maxOrderQuantity</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the maxOrderQuantity
	 */
	@Accessor(qualifier = "maxOrderQuantity", type = Accessor.Type.SETTER)
	public void setMaxOrderQuantity(final Integer value)
	{
		_maxOrderQuantity = getPersistenceContext().setValue(MAXORDERQUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.minOrderQuantity</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the minOrderQuantity
	 */
	@Accessor(qualifier = "minOrderQuantity", type = Accessor.Type.SETTER)
	public void setMinOrderQuantity(final Integer value)
	{
		_minOrderQuantity = getPersistenceContext().setValue(MINORDERQUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Product.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.normal</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the normal
	 */
	@Accessor(qualifier = "normal", type = Accessor.Type.SETTER)
	public void setNormal(final Collection<MediaModel> value)
	{
		_normal = getPersistenceContext().setValue(NORMAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.numberContentUnits</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the numberContentUnits
	 */
	@Accessor(qualifier = "numberContentUnits", type = Accessor.Type.SETTER)
	public void setNumberContentUnits(final Double value)
	{
		_numberContentUnits = getPersistenceContext().setValue(NUMBERCONTENTUNITS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.offlineDate</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the offlineDate
	 */
	@Accessor(qualifier = "offlineDate", type = Accessor.Type.SETTER)
	public void setOfflineDate(final Date value)
	{
		_offlineDate = getPersistenceContext().setValue(OFFLINEDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.onlineDate</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the onlineDate
	 */
	@Accessor(qualifier = "onlineDate", type = Accessor.Type.SETTER)
	public void setOnlineDate(final Date value)
	{
		_onlineDate = getPersistenceContext().setValue(ONLINEDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.order</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final Integer value)
	{
		_order = getPersistenceContext().setValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.orderQuantityInterval</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the orderQuantityInterval
	 */
	@Accessor(qualifier = "orderQuantityInterval", type = Accessor.Type.SETTER)
	public void setOrderQuantityInterval(final Integer value)
	{
		_orderQuantityInterval = getPersistenceContext().setValue(ORDERQUANTITYINTERVAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.others</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the others
	 */
	@Accessor(qualifier = "others", type = Accessor.Type.SETTER)
	public void setOthers(final Collection<MediaModel> value)
	{
		_others = getPersistenceContext().setValue(OTHERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.picture</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the picture
	 */
	@Accessor(qualifier = "picture", type = Accessor.Type.SETTER)
	public void setPicture(final MediaModel value)
	{
		_picture = getPersistenceContext().setValue(PICTURE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.priceQuantity</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the priceQuantity
	 */
	@Accessor(qualifier = "priceQuantity", type = Accessor.Type.SETTER)
	public void setPriceQuantity(final Double value)
	{
		_priceQuantity = getPersistenceContext().setValue(PRICEQUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.productReferences</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the productReferences
	 */
	@Accessor(qualifier = "productReferences", type = Accessor.Type.SETTER)
	public void setProductReferences(final Collection<ProductReferenceModel> value)
	{
		_productReferences = getPersistenceContext().setValue(PRODUCTREFERENCES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.remarks</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the remarks
	 */
	@Accessor(qualifier = "remarks", type = Accessor.Type.SETTER)
	public void setRemarks(final String value)
	{
		setRemarks(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Product.remarks</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the remarks
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "remarks", type = Accessor.Type.SETTER)
	public void setRemarks(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(REMARKS, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.segment</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the segment
	 */
	@Accessor(qualifier = "segment", type = Accessor.Type.SETTER)
	public void setSegment(final String value)
	{
		setSegment(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Product.segment</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the segment
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "segment", type = Accessor.Type.SETTER)
	public void setSegment(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(SEGMENT, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.specialTreatmentClasses</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the specialTreatmentClasses
	 */
	@Accessor(qualifier = "specialTreatmentClasses", type = Accessor.Type.SETTER)
	public void setSpecialTreatmentClasses(final Map<String,String> value)
	{
		_specialTreatmentClasses = getPersistenceContext().setValue(SPECIALTREATMENTCLASSES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.startLineNumber</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the startLineNumber
	 */
	@Accessor(qualifier = "startLineNumber", type = Accessor.Type.SETTER)
	public void setStartLineNumber(final Integer value)
	{
		_startLineNumber = getPersistenceContext().setValue(STARTLINENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.supercategories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supercategories - Super Categories
	 */
	@Accessor(qualifier = "supercategories", type = Accessor.Type.SETTER)
	public void setSupercategories(final Collection<CategoryModel> value)
	{
		_supercategories = getPersistenceContext().setValue(SUPERCATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.supplierAlternativeAID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supplierAlternativeAID
	 */
	@Accessor(qualifier = "supplierAlternativeAID", type = Accessor.Type.SETTER)
	public void setSupplierAlternativeAID(final String value)
	{
		_supplierAlternativeAID = getPersistenceContext().setValue(SUPPLIERALTERNATIVEAID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.thumbnail</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the thumbnail
	 */
	@Accessor(qualifier = "thumbnail", type = Accessor.Type.SETTER)
	public void setThumbnail(final MediaModel value)
	{
		_thumbnail = getPersistenceContext().setValue(THUMBNAIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.thumbnails</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the thumbnails
	 */
	@Accessor(qualifier = "thumbnails", type = Accessor.Type.SETTER)
	public void setThumbnails(final Collection<MediaModel> value)
	{
		_thumbnails = getPersistenceContext().setValue(THUMBNAILS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.unit</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
	public void setUnit(final UnitModel value)
	{
		_unit = getPersistenceContext().setValue(UNIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.untypedFeatures</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the untypedFeatures
	 */
	@Accessor(qualifier = "untypedFeatures", type = Accessor.Type.SETTER)
	public void setUntypedFeatures(final List<ProductFeatureModel> value)
	{
		_untypedFeatures = getPersistenceContext().setValue(UNTYPEDFEATURES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.variants</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the variants
	 */
	@Accessor(qualifier = "variants", type = Accessor.Type.SETTER)
	public void setVariants(final Collection<VariantProductModel> value)
	{
		_variants = getPersistenceContext().setValue(VARIANTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.variantType</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the variantType
	 */
	@Accessor(qualifier = "variantType", type = Accessor.Type.SETTER)
	public void setVariantType(final VariantTypeModel value)
	{
		_variantType = getPersistenceContext().setValue(VARIANTTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Product.xmlcontent</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the xmlcontent
	 */
	@Accessor(qualifier = "xmlcontent", type = Accessor.Type.SETTER)
	public void setXmlcontent(final String value)
	{
		_xmlcontent = getPersistenceContext().setValue(XMLCONTENT, value);
	}
	
}
