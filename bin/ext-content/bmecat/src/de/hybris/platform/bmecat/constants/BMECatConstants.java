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
package de.hybris.platform.bmecat.constants;

/**
 * Global class for all BMECat constants. <br />
 * This includes type codes for new items, managers and JNDI names. <br />
 * 
 */
public final class BMECatConstants extends GeneratedBMECatConstants
{
	public static final String LICENCE_BMECAT = "extension.bmecat";

	/**
	 * constants for transactions
	 * 
	 */
	public static final class TRANSACTION
	{
		public static final int T_NEW_CATALOG = 1;

		public static final int T_UPDATE_PRODUCTS = 2;

		public static final int T_UPDATE_PRICES = 3;
	}

	/**
	 * constants for change types like create or delete
	 * 
	 */
	public static interface ChangeTypes
	{
		String CREATE_LANGUAGE = "createLanguage";
		String CREATE_CURRENCY = "createCurrency";
		String CREATE_COUNTRY = "createCountry";
		String CREATE_UNIT = "createUnit";
		String CREATE_CATALOG = "createCatalog";
		String CREATE_CATALOG_VERSION = "createCatalogVersion";
		String CREATE_COMPANY = "createCompany";
		String CREATE_VARIANTPRODUCT = "createVariantProduct";
		String CREATE_PRODUCT = "createProduct";
		String CREATE_KEYWORD = "createKeyword";
		String CREATE_MEDIA = "createMedia";
		String CREATE_REGION = "createRegion";
		String CREATE_ADDRESS = "createAddress";
		String CREATE_AGREEMENT = "createAgreement";
		String CREATE_CATEGORY = "createCategory";
		String CREATE_PRODUCTREFERENCE = "createProductReference";
		String CREATE_CATEGORY2PRODUCT = "createCategory2Product";
		String CREATE_PRODUCT_PRICE = "createProductPrice";
		String DELETE_PRODUCT_PRICE = "deleteProductPrice";
		String ADD_PRODUCT_MEDIA = "addProductMedia";
		String ADD_CATEGORY_MEDIA = "addCategoryMedia";
		String ADD_SUPLLIER_MEDIA = "addSupplierMedia";

		String UPDATE_PRODUCT = "updateProduct";
		String DELETE_PRODUCT = "deleteProduct";
		String DELETE_VARIANTPRODUCT = "deleteVariantProduct";
		String DELETE_CATEGORY2PRODUCT = "deletedCategory2Product";
		String DELETE_PRODUCTREFERENCE = "deletedProductReference";

	}

	/**
	 * constants for different modes
	 * 
	 */
	public static final class MODE
	{
		public static final int NEW = 1;

		public static final int UPDATE = 2;

		public static final int DELETE = 3;
	}

	/**
	 * constants for the xml parser
	 * 
	 */
	public static final class PARSER
	{
		//	 FEATURES

		/** Namespaces feature id (http://xml.org/sax/features/namespaces). */
		public static final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";

		/** Namespace prefixes feature id (http://xml.org/sax/features/namespace-prefixes). */
		public static final String NAMESPACE_PREFIXES_FEATURE_ID = "http://xml.org/sax/features/namespace-prefixes";

		/** Validation feature id (http://xml.org/sax/features/validation). */
		public static final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";

		/** Schema validation feature id (http://apache.org/xml/features/validation/schema). */
		public static final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

		/** Schema full checking feature id (http://apache.org/xml/features/validation/schema-full-checking). */
		public static final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";

		/** Dynamic validation feature id (http://apache.org/xml/features/validation/dynamic). */
		public static final String DYNAMIC_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/dynamic";

		/** Load external DTD feature id (http://apache.org/xml/features/nonvalidating/load-external-dtd). */
		public static final String LOAD_EXTERNAL_DTD_FEATURE_ID = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

		// PROPERTY IDs

		/** Lexical handler property id (http://xml.org/sax/properties/lexical-handler). */
		public static final String LEXICAL_HANDLER_PROPERTY_ID = "http://xml.org/sax/properties/lexical-handler";

		// DEFAULT SETTINGS

		/** Default parser name. */
		public static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";
	}

	/**
	 * this class contains constants for XML tags and attributes
	 * 
	 */
	public static final class XML
	{
		/**
		 * XML Tag constants
		 * 
		 */
		public static final class TAG
		{
			public static final String ARTICLE_TO_CATALOGGROUP_MAP_ORDER = "ARTICLE_TO_CATALOGGROUP_MAP_ORDER";
			public static final String FEATURE_GROUP_ID = "FEATURE_GROUP_ID";
			public static final String FEATURE_GROUP_NAME = "FEATURE_GROUP_NAME";
			public static final String FEATURE_GROUP_DESCR = "FEATURE_GROUP_DESCR";
			public static final String FT_NAME = "FT_NAME";
			public static final String FT_UNIT = "FT_UNIT";
			public static final String FT_ORDER = "FT_ORDER";
			public static final String FEATURE_SYSTEM_NAME = "FEATURE_SYSTEM_NAME";
			public static final String FEATURE_SYSTEM_DESCR = "FEATURE_SYSTEM_DESCR";
			public static final String FEATURE_GROUP = "FEATURE_GROUP";
			public static final String FEATURE_NAME = "FEATURE_NAME";
			public static final String FEATURE_GROUP_DESC = "FEATURE_GROUP_DESC";
			public static final String FEATURE_TEMPLATE = "FEATURE_TEMPLATE";
			public static final String CLASSIFICATION_SYSTEM_NAME = "CLASSIFICATION_SYSTEM_NAME";
			public static final String CLASSIFICATION_SYSTEM_VERSION = "CLASSIFICATION_SYSTEM_VERSION";
			public static final String CLASSIFICATION_GROUPS = "CLASSIFICATION_GROUPS";
			public static final String CLASSIFICATION_GROUP = "CLASSIFICATION_GROUP";
			public static final String CLASSIFICATION_GROUP_ID = "CLASSIFICATION_GROUP_ID";
			public static final String CLASSIFICATION_GROUP_NAME = "CLASSIFICATION_GROUP_NAME";
			public static final String CLASSIFICATION_GROUP_DESCR = "CLASSIFICATION_GROUP_DESCR";
			public static final String CLASSIFICATION_GROUP_PARENT_ID = "CLASSIFICATION_GROUP_PARENT_ID";
			public static final String GROUP_ORDER = "GROUP_ORDER";
			public static final String GROUP_DESCRIPTION = "GROUP_DESCRIPTION";
			public static final String QUANTITY_MIN = "QUANTITY_MIN";
			public static final String QUANTITY_INTERVAL = "QUANTITY_INTERVAL";
			public static final String PRICE_QUANTITY = "PRICE_QUANTITY";
			public static final String VORDER = "VORDER";
			public static final String AGREEMENT = "AGREEMENT";
			public static final String AGREEMENT_ID = "AGREEMENT_ID";
			public static final String ART_ID_TO = "ART_ID_TO";
			public static final String TERRITORY = "TERRITORY";
			public static final String REMARKS = "REMARKS";
			public static final String KEYWORD = "KEYWORD";
			public static final String SEGMENT = "SEGMENT";
			public static final String ARTICLE_ORDER = "ARTICLE_ORDER";
			public static final String ARTICLE_STATUS = "ARTICLE_STATUS";
			public static final String SPECIAL_TREATMENT_CLASS = "SPECIAL_TREATMENT_CLASS";
			public static final String DELIVERY_TIME = "DELIVERY_TIME";
			public static final String ERP_GROUP_SUPPLIER = "ERP_GROUP_SUPPLIER";
			public static final String ERP_GROUP_BUYER = "ERP_GROUP_BUYER";
			public static final String MANUFACTURER_NAME = "MANUFACTURER_NAME";
			public static final String MANUFACTURER_AID = "MANUFACTURER_AID";
			public static final String SUPPLIER_ALT_AID = "SUPPLIER_ALT_AID";
			public static final String ARTICLE_REFERENCE = "ARTICLE_REFERENCE";
			public static final String EAN = "EAN";
			public static final String MANUFACTURER_TYPE_DESCR = "MANUFACTURER_TYPE_DESCR";
			public static final String MIME_ORDER = "MIME_ORDER";
			public static final String MIME_ALT = "MIME_ALT";
			public static final String MIME_DESCR = "MIME_DESCR";
			public static final String PRICE_FLAG = "PRICE_FLAG";
			public static final String MIME_ROOT = "MIME_ROOT";
			public static final String ADDRESS_REMARKS = "ADDRESS_REMARKS";
			public static final String URL = "URL";
			public static final String PUBLIC_KEY = "PUBLIC_KEY";
			public static final String EMAIL = "EMAIL";
			public static final String FAX = "FAX";
			public static final String PHONE = "PHONE";
			public static final String STATE = "STATE";
			public static final String SUPPLIER_ID = "SUPPLIER_ID";
			public static final String BUYER_ID = "BUYER_ID";
			public static final String BUYER_AID = "BUYER_AID";
			public static final String TIMEZONE = "TIMEZONE";
			public static final String TIME = "TIME";
			public static final String DATE = "DATE";
			public static final String DATETIME = "DATETIME";
			public static final String BMECAT = "BMECAT";
			public static final String HEADER = "HEADER";
			public static final String CATALOG = "CATALOG";
			public static final String LANGUAGE = "LANGUAGE";
			public static final String CATALOG_ID = "CATALOG_ID";
			public static final String CATALOG_VERSION = "CATALOG_VERSION";
			public static final String CATALOG_NAME = "CATALOG_NAME";
			public static final String CURRENCY = "CURRENCY";
			public static final String BUYER = "BUYER";
			public static final String BUYER_NAME = "BUYER_NAME";
			public static final String ADDRESS = "ADDRESS";
			public static final String NAME = "NAME";
			public static final String NAME2 = "NAME2";
			public static final String NAME3 = "NAME3";
			public static final String CONTACT = "CONTACT";
			public static final String STREET = "STREET";
			public static final String ZIP = "ZIP";
			public static final String ZIPBOX = "ZIPBOX";
			public static final String CITY = "CITY";
			public static final String COUNTRY = "COUNTRY";
			public static final String SUPPLIER = "SUPPLIER";
			public static final String SUPPLIER_NAME = "SUPPLIER_NAME";
			public static final String T_NEW_CATALOG = "T_NEW_CATALOG";
			public static final String FEATURE_SYSTEM = "FEATURE_SYSTEM";
			public static final String CLASSIFICATION_SYSTEM = "CLASSIFICATION_SYSTEM";
			public static final String T_UPDATE_PRODUCTS = "T_UPDATE_PRODUCTS";
			public static final String T_UPDATE_PRICES = "T_UPDATE_PRICES";
			public static final String CATALOG_GROUP_SYSTEM = "CATALOG_GROUP_SYSTEM";
			public static final String GROUP_SYSTEM_ID = "GROUP_SYSTEM_ID";
			public static final String GROUP_SYSTEM_NAME = "GROUP_SYSTEM_NAME";
			public static final String GROUP_SYSTEM_DESCRIPTION = "GROUP_SYSTEM_DESCRIPTION";
			public static final String CATALOG_STRUCTURE = "CATALOG_STRUCTURE";
			public static final String GROUP_ID = "GROUP_ID";
			public static final String GROUP_NAME = "GROUP_NAME";
			public static final String PARENT_ID = "PARENT_ID";
			public static final String ARTICLE = "ARTICLE";
			public static final String SUPPLIER_AID = "SUPPLIER_AID";
			public static final String ARTICLE_DETAILS = "ARTICLE_DETAILS";
			public static final String ARTICLE_FEATURES = "ARTICLE_FEATURES";
			public static final String DESCRIPTION_SHORT = "DESCRIPTION_SHORT";
			public static final String DESCRIPTION_LONG = "DESCRIPTION_LONG";
			public static final String ARTICLE_ORDER_DETAILS = "ARTICLE_ORDER_DETAILS";
			public static final String ORDER_UNIT = "ORDER_UNIT";
			public static final String CONTENT_UNIT = "CONTENT_UNIT";
			public static final String NO_CU_PER_OU = "NO_CU_PER_OU";
			public static final String ARTICLE_PRICE_DETAILS = "ARTICLE_PRICE_DETAILS";
			public static final String ARTICLE_PRICE = "ARTICLE_PRICE";
			public static final String PRICE_AMOUNT = "PRICE_AMOUNT";
			public static final String PRICE_CURRENCY = "PRICE_CURRENCY";
			public static final String DAILY_PRICE = "DAILY_PRICE";
			public static final String TAX = "TAX";
			public static final String PRICE_FACTOR = "PRICE_FACTOR";
			public static final String LOWER_BOUND = "LOWER_BOUND";
			public static final String MIME_INFO = "MIME_INFO";
			public static final String MIME = "MIME";
			public static final String MIME_TYPE = "MIME_TYPE";
			public static final String MIME_SOURCE = "MIME_SOURCE";
			public static final String MIME_PURPOSE = "MIME_PURPOSE";
			public static final String ARTICLE_TO_CATALOGGROUP_MAP = "ARTICLE_TO_CATALOGGROUP_MAP";
			public static final String ART_ID = "ART_ID";
			public static final String CATALOG_GROUP_ID = "CATALOG_GROUP_ID";
			public static final String GENERATOR_INFO = "GENERATOR_INFO";
			public static final String USER_DEFINED_EXTENSIONS = "USER_DEFINED_EXTENSIONS";
			public static final String REFERENCE_FEATURE_SYSTEM_NAME = "REFERENCE_FEATURE_SYSTEM_NAME";
			public static final String REFERENCE_FEATURE_GROUP_NAME = "REFERENCE_FEATURE_GROUP_NAME";
			public static final String REFERENCE_FEATURE_GROUP_ID = "REFERENCE_FEATURE_GROUP_ID";
			public static final String FEATURE = "FEATURE";
			public static final String FNAME = "FNAME";
			public static final String FVALUE = "FVALUE";
			public static final String FVALUE_DETAILS = "FVALUE_DETAILS";
			public static final String FUNIT = "FUNIT";
			public static final String FDESCR = "FDESCR";
			public static final String FORDER = "FORDER";
			public static final String VARIANTS = "VARIANTS";
			public static final String VARIANT = "VARIANT";
			public static final String SUPPLIER_AID_SUPPLEMENT = "SUPPLIER_AID_SUPPLEMENT";
			public static final String ABORT = "ABORT";
			public static final String BOXNO = "BOXNO";
		}

		/**
		 * XML Attribute constants
		 * 
		 */
		public static final class ATTRIBUTE
		{
			public static final class DATETIME
			{
				public static final String TYPE = "type";
			}

			public static final class ARTICLE_REFERENCE
			{
				public static final String TYPE = "type";
				public static final String QUANTITY = "quantity";
			}

			public static final class ADDRESS
			{
				public static final String TYPE = "type";
			}

			public static final class T_UPDATE_PRICES
			{
				public static final String PREV_VERSION = "prev_version";
			}

			public static final class T_UPDATE_PRODUCTS
			{
				public static final String PREV_VERSION = "prev_version";
			}

			public static final class T_NEW_CATALOG
			{
				public static final String PREV_VERSION = "prev_version";
			}

			public static final class CATALOG_STRUCTURE
			{
				public static final String TYPE = "type";
			}

			public static final class ARTICLE_PRICE
			{
				public static final String PRICE_TYPE = "price_type";
			}

			public static final class BMECAT
			{
				public static final String VERSION = "version";
			}

			public static final class ARTICLE
			{
				public static final String MODE = "mode";
			}

			public static final class ARTICLE_TO_CATALOGGROUP_MAP
			{
				public static final String MODE = "mode";
			}

			public static final class BUYER_ID
			{
				public static final String TYPE = "type";
			}

			public static final class SUPPLIER_ID
			{
				public static final String TYPE = "type";
			}

			public static final class FEATURE_TEMPLATE
			{
				public static final String TYPE = "type";
			}
		}
	}

	/**
	 * constants for import and export groups
	 * 
	 */
	public static final class Groups
	{
		public static final String IMPORT_USERS = "BMECatImportUsers";
		public static final String EXPORT_USERS = "BMECatExportUsers";
	}

	public static final class BMECat2CSV
	{
		public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
		public static final String TIME_FORMAT_PATTERN = "HH:mm:ss";
		public static final String TIMEZONE_FORMAT_PATTERN = "Z";
		public static final String DATETIMETIMEZONE_FORMAT_PATTERN = DATE_FORMAT_PATTERN + " " + TIME_FORMAT_PATTERN + " "
				+ TIMEZONE_FORMAT_PATTERN;

		public static final String NUMBERFORMAT = "#,##0.000";

		public static final String CATALOG_OBJECT_FILENAME = "catalog.csv";
		public static final String CATALOG_VERSION_FILENAME = "catalogversion.csv";
		public static final String CLASSIFICATION_SYSTEM_FILENAME = "classificationsystem.csv";
		public static final String CLASSIFICATION_SYSTEM_VERSION_FILENAME = "classificationsystemversion.csv";
		public static final String CLASSIFICATION_CLASS_FILENAME = "classificationclass.csv";
		public static final String CLASSIFICATION_ATTRIBUTE_FILENAME = "classificationattribute.csv";
		public static final String CLASSIFICATION_ATTRIBUTE_ASSIGNMENT_FILENAME = "classificationattributeassignment.csv";
		public static final String ARTICLE_OBJECT_FILENAME = "products.csv";
		public static final String ARTICLE_UPDATE_FILENAME = "productsupdate.csv";
		public static final String ARTICLEREFERENCES_FILENAME = "productreferences.csv";
		public static final String KEYWORD_OBJECT_FILENAME = "keywords.csv";
		public static final String MIME_OBJECT_FILENAME = "medias.csv";
		public static final String CATALOGSTRUCTUE_OBJECT_FILENAME = "categories.csv";
		public static final String ARTICLE2CATALOGGROUP_NEW_RELATION_FILENAME = "productnew2category.csv";
		public static final String ARTICLE2CATALOGGROUP_DELETE_RELATION_FILENAME = "productdelete2category.csv";
		public static final String COMPANY_FILENAME = "customers.csv";
		public static final String ADDRESS_FILENAME = "addresses.csv";
		public static final String COUNTRY_FILENAME = "countries.csv";
		public static final String REGION_FILENAME = "regions.csv";
		public static final String AGREEMENT_FILENAME = "agreements.csv";
		public static final String ARTICLE2ARTICLEPRICE = "productprices.csv";
		public static final String ARTICLE_UPDATE_PRICES = "productsupdateprices.csv";
		public static final String ARTICLEFEATURE_FILENAME = "productfeatures.csv";
		public static final String ARTICLEFEATURE_DETAILS = "productfeaturedetails.csv";
		public static final String ARTICLEFEATURE_VARIANTS = "productfeaturevariants.csv";
		public static final String ARTICLEFEATURE_VALUE_MAPPINGS = "productfeaturevaluemappings.csv";
		public static final String ARTICLE_DELETE_MODE = "productdelete.csv";
		public static final String GROUPSYSTEM_FILENAME = "groupsystem.csv";
	}
}
