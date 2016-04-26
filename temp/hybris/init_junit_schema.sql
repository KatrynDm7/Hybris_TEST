
CREATE CACHED TABLE junit_abstractcontactinfo
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_userpos INTEGER,
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_phonenumber NVARCHAR(255),
    p_type BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_userRelIDX_26 ON junit_abstractcontactinfo (p_user);

CREATE INDEX junit_userposPosIDX_26 ON junit_abstractcontactinfo (p_userpos);


CREATE CACHED TABLE junit_abstractlinkentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_area BIGINT,
    p_sortorder INTEGER,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_height INTEGER,
    p_spacer TINYINT,
    p_descriptionicon BIGINT,
    p_url NVARCHAR(255),
    p_extensionname NVARCHAR(255),
    p_parentlinkpos INTEGER,
    p_parentlink BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_code_6002 ON junit_abstractlinkentries (p_code);

CREATE INDEX junit_area_6002 ON junit_abstractlinkentries (p_area);

CREATE INDEX junit_parentlinkRelIDX_6002 ON junit_abstractlinkentries (p_parentlink);

CREATE INDEX junit_parentlinkposPosIDX_6002 ON junit_abstractlinkentries (p_parentlinkpos);


CREATE CACHED TABLE junit_abstractlinkentrieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_aclentries
(
    hjmpTS BIGINT,
    PermissionPK BIGINT,
    Negative TINYINT DEFAULT 0,
    PrincipalPK BIGINT,
    ItemPK BIGINT,
    PRIMARY KEY (PermissionPK, PrincipalPK, ItemPK)
);

CREATE INDEX junit_aclcheckindex_aclentries ON junit_aclentries (ItemPK, PrincipalPK);

CREATE INDEX junit_aclupdateindex_aclentries ON junit_aclentries (ItemPK);


CREATE CACHED TABLE junit_addresses
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_original BIGINT,
    p_duplicate TINYINT,
    p_appartment NVARCHAR(255),
    p_building NVARCHAR(255),
    p_cellphone NVARCHAR(255),
    p_company NVARCHAR(255),
    p_country BIGINT,
    p_department NVARCHAR(255),
    p_district NVARCHAR(255),
    p_email NVARCHAR(255),
    p_fax NVARCHAR(255),
    p_firstname NVARCHAR(255),
    p_lastname NVARCHAR(255),
    p_middlename NVARCHAR(255),
    p_middlename2 NVARCHAR(255),
    p_phone1 NVARCHAR(255),
    p_phone2 NVARCHAR(255),
    p_pobox NVARCHAR(255),
    p_postalcode NVARCHAR(255),
    p_region BIGINT,
    p_streetname NVARCHAR(255),
    p_streetnumber NVARCHAR(255),
    p_title BIGINT,
    p_town NVARCHAR(255),
    p_gender BIGINT,
    p_dateofbirth TIMESTAMP,
    p_remarks NVARCHAR(255),
    p_url NVARCHAR(255),
    p_shippingaddress TINYINT,
    p_unloadingaddress TINYINT,
    p_billingaddress TINYINT,
    p_contactaddress TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_testindex_23 ON junit_addresses (p_email);

CREATE INDEX junit_Address_Owner_23 ON junit_addresses (OwnerPkString);


CREATE CACHED TABLE junit_addressprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255),
    LANGPK BIGINT,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX junit_itempk_addressprops ON junit_addressprops (ITEMPK);

CREATE INDEX junit_nameidx_addressprops ON junit_addressprops (NAME);

CREATE INDEX junit_realnameidx_addressprops ON junit_addressprops (REALNAME);


CREATE CACHED TABLE junit_agreements
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_id NVARCHAR(255),
    p_startdate TIMESTAMP,
    p_enddate TIMESTAMP,
    p_catalogversion BIGINT,
    p_catalog BIGINT,
    p_buyer BIGINT,
    p_supplier BIGINT,
    p_buyercontact BIGINT,
    p_suppliercontact BIGINT,
    p_currency BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_atomictypes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    InternalCode NVARCHAR(255),
    p_defaultvalue LONGVARBINARY,
    InheritancePathString LONGVARCHAR,
    JavaClassName NVARCHAR(255),
    SuperTypePK BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    InternalCodeLowerCase NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX junit_inheritpsi_81 ON junit_atomictypes (InheritancePathString);

CREATE INDEX junit_typecode_81 ON junit_atomictypes (InternalCode);

CREATE INDEX junit_typecodelowercase_81 ON junit_atomictypes (InternalCodeLowerCase);


CREATE CACHED TABLE junit_atomictypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_attr2valuerel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_value BIGINT,
    p_attributeassignment BIGINT,
    p_attribute BIGINT,
    p_systemversion BIGINT,
    p_position INTEGER,
    p_externalid NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_sysVer_609 ON junit_attr2valuerel (p_systemversion);

CREATE INDEX junit_catRelIDX_609 ON junit_attr2valuerel (p_attributeassignment);

CREATE INDEX junit_idIDX_609 ON junit_attr2valuerel (p_externalid);

CREATE INDEX junit_valIDX_609 ON junit_attr2valuerel (p_value);

CREATE INDEX junit_attrIDX_609 ON junit_attr2valuerel (p_attribute);


CREATE CACHED TABLE junit_attributedescriptors
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    QualifierInternal NVARCHAR(255),
    AttributeTypePK BIGINT,
    columnName NVARCHAR(255),
    p_defaultvalue LONGVARBINARY,
    p_defaultvaluedefinitionstring NVARCHAR(255),
    EnclosingTypePK BIGINT,
    PersistenceQualifierInternal NVARCHAR(255),
    PersistenceTypePK BIGINT,
    p_attributehandler NVARCHAR(255),
    SelectionDescriptorPK BIGINT,
    modifiers INTEGER DEFAULT 0,
    p_unique TINYINT,
    p_hiddenforui TINYINT,
    p_readonlyforui TINYINT,
    p_dontcopy TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    QualifierLowerCaseInternal NVARCHAR(255),
    isHidden TINYINT DEFAULT 0,
    isProperty TINYINT DEFAULT 0,
    SuperAttributeDescriptorPK BIGINT,
    InheritancePathString LONGVARCHAR,
    p_externalqualifier NVARCHAR(255),
    p_storeindatabase TINYINT,
    p_needrestart TINYINT,
    p_param TINYINT,
    p_position INTEGER,
    p_defaultvalueexpression NVARCHAR(255),
    p_issource TINYINT,
    p_ordered TINYINT,
    p_relationname NVARCHAR(255),
    p_relationtype BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_inheritps_87 ON junit_attributedescriptors (InheritancePathString);

CREATE INDEX junit_enclosing_87 ON junit_attributedescriptors (EnclosingTypePK);

CREATE INDEX junit_lcqualifier_87 ON junit_attributedescriptors (QualifierLowerCaseInternal);

CREATE UNIQUE INDEX junit_qualifier_87 ON junit_attributedescriptors (QualifierInternal, EnclosingTypePK);


CREATE CACHED TABLE junit_attributedescriptorslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cartentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_baseprice DECIMAL(30,8),
    p_calculated TINYINT,
    p_discountvaluesinternal LONGVARCHAR,
    p_entrynumber INTEGER,
    p_info NVARCHAR(255),
    p_product BIGINT,
    p_quantity DECIMAL(30,8),
    p_taxvaluesinternal NVARCHAR(255),
    p_totalprice DECIMAL(30,8),
    p_unit BIGINT,
    p_giveaway TINYINT,
    p_rejected TINYINT,
    p_order BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_oeProd_44 ON junit_cartentries (p_product);

CREATE INDEX junit_oeOrd_44 ON junit_cartentries (p_order);


CREATE CACHED TABLE junit_carts
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_calculated TINYINT,
    p_code NVARCHAR(255),
    p_currency BIGINT,
    p_deliveryaddress BIGINT,
    p_deliverycost DECIMAL(30,8),
    p_deliverymode BIGINT,
    p_deliverystatus BIGINT,
    p_globaldiscountvaluesinternal LONGVARCHAR,
    p_net TINYINT,
    p_paymentaddress BIGINT,
    p_paymentcost DECIMAL(30,8),
    p_paymentinfo BIGINT,
    p_paymentmode BIGINT,
    p_paymentstatus BIGINT,
    p_status BIGINT,
    p_exportstatus BIGINT,
    p_statusinfo NVARCHAR(255),
    p_totalprice DECIMAL(30,8),
    p_totaldiscounts DECIMAL(30,8),
    p_totaltax DECIMAL(30,8),
    p_totaltaxvaluesinternal LONGVARCHAR,
    p_user BIGINT,
    p_subtotal DECIMAL(30,8),
    p_discountsincludedeliverycost TINYINT,
    p_discountsincludepaymentcost TINYINT,
    p_sessionid NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_OrderCode_43 ON junit_carts (p_code);

CREATE INDEX junit_OrderUser_43 ON junit_carts (p_user);


CREATE CACHED TABLE junit_cat2attrrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_classificationclass BIGINT,
    p_classificationattribute BIGINT,
    p_systemversion BIGINT,
    p_position INTEGER,
    p_externalid NVARCHAR(255),
    p_unit BIGINT,
    p_mandatory TINYINT,
    p_localized TINYINT,
    p_range TINYINT,
    p_multivalued TINYINT,
    p_searchable TINYINT,
    p_attributetype BIGINT,
    p_formatdefinition NVARCHAR(255),
    p_listable TINYINT,
    p_comparable TINYINT,
    p_visibility BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_sysVer_610 ON junit_cat2attrrel (p_systemversion);

CREATE INDEX junit_relSrcIDX_610 ON junit_cat2attrrel (p_classificationclass);

CREATE INDEX junit_idIDX_610 ON junit_cat2attrrel (p_externalid);

CREATE INDEX junit_relTgtIDX_610 ON junit_cat2attrrel (p_classificationattribute);

CREATE INDEX junit_posIdx_610 ON junit_cat2attrrel (p_position);


CREATE CACHED TABLE junit_cat2attrrellp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cat2catrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_144 ON junit_cat2catrel (SequenceNumber);

CREATE INDEX junit_rseqnr_144 ON junit_cat2catrel (RSequenceNumber);

CREATE INDEX junit_linksource_144 ON junit_cat2catrel (SourcePK);

CREATE INDEX junit_linktarget_144 ON junit_cat2catrel (TargetPK);

CREATE INDEX junit_qualifier_144 ON junit_cat2catrel (Qualifier);


CREATE CACHED TABLE junit_cat2keywordrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_605 ON junit_cat2keywordrel (SequenceNumber);

CREATE INDEX junit_rseqnr_605 ON junit_cat2keywordrel (RSequenceNumber);

CREATE INDEX junit_linksource_605 ON junit_cat2keywordrel (SourcePK);

CREATE INDEX junit_linktarget_605 ON junit_cat2keywordrel (TargetPK);

CREATE INDEX junit_qualifier_605 ON junit_cat2keywordrel (Qualifier);


CREATE CACHED TABLE junit_cat2medrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_145 ON junit_cat2medrel (SequenceNumber);

CREATE INDEX junit_rseqnr_145 ON junit_cat2medrel (RSequenceNumber);

CREATE INDEX junit_linksource_145 ON junit_cat2medrel (SourcePK);

CREATE INDEX junit_linktarget_145 ON junit_cat2medrel (TargetPK);

CREATE INDEX junit_qualifier_145 ON junit_cat2medrel (Qualifier);


CREATE CACHED TABLE junit_cat2princrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_613 ON junit_cat2princrel (SequenceNumber);

CREATE INDEX junit_rseqnr_613 ON junit_cat2princrel (RSequenceNumber);

CREATE INDEX junit_linksource_613 ON junit_cat2princrel (SourcePK);

CREATE INDEX junit_linktarget_613 ON junit_cat2princrel (TargetPK);

CREATE INDEX junit_qualifier_613 ON junit_cat2princrel (Qualifier);


CREATE CACHED TABLE junit_cat2prodrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_143 ON junit_cat2prodrel (SequenceNumber);

CREATE INDEX junit_rseqnr_143 ON junit_cat2prodrel (RSequenceNumber);

CREATE INDEX junit_linksource_143 ON junit_cat2prodrel (SourcePK);

CREATE INDEX junit_linktarget_143 ON junit_cat2prodrel (TargetPK);

CREATE INDEX junit_qualifier_143 ON junit_cat2prodrel (Qualifier);


CREATE CACHED TABLE junit_catalogs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_id varchar(200),
    p_activecatalogversion BIGINT,
    p_defaultcatalog TINYINT,
    p_supplier BIGINT,
    p_buyer BIGINT,
    p_previewurltemplate NVARCHAR(255),
    p_urlpatterns LONGVARBINARY,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_idIdx_600 ON junit_catalogs (p_id);


CREATE CACHED TABLE junit_catalogslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_catalogversions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_active TINYINT,
    p_version NVARCHAR(255),
    p_mimerootdirectory NVARCHAR(255),
    p_generationdate TIMESTAMP,
    p_defaultcurrency BIGINT,
    p_inclfreight TINYINT,
    p_inclpacking TINYINT,
    p_inclassurance TINYINT,
    p_inclduty TINYINT,
    p_territories LONGVARCHAR,
    p_languages LONGVARCHAR,
    p_generatorinfo NVARCHAR(255),
    p_categorysystemid NVARCHAR(255),
    p_previousupdateversion INTEGER,
    p_catalog BIGINT,
    p_mnemonic NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_versionIDX_601 ON junit_catalogversions (p_version);

CREATE INDEX junit_catalogIDX_601 ON junit_catalogversions (p_catalog);

CREATE INDEX junit_visibleIDX_601 ON junit_catalogversions (p_active);


CREATE CACHED TABLE junit_catalogversionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_categorysystemname NVARCHAR(255),
    p_categorysystemdescription NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_catalogversionsyncjob
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_624 ON junit_catalogversionsyncjob (SequenceNumber);

CREATE INDEX junit_rseqnr_624 ON junit_catalogversionsyncjob (RSequenceNumber);

CREATE INDEX junit_linksource_624 ON junit_catalogversionsyncjob (SourcePK);

CREATE INDEX junit_linktarget_624 ON junit_catalogversionsyncjob (TargetPK);

CREATE INDEX junit_qualifier_624 ON junit_catalogversionsyncjob (Qualifier);


CREATE CACHED TABLE junit_categories
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_order INTEGER,
    p_catalog BIGINT,
    p_catalogversion BIGINT,
    p_normal LONGVARCHAR,
    p_thumbnails LONGVARCHAR,
    p_detail LONGVARCHAR,
    p_logo LONGVARCHAR,
    p_data_sheet LONGVARCHAR,
    p_others LONGVARCHAR,
    p_thumbnail BIGINT,
    p_picture BIGINT,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_externalid NVARCHAR(255),
    p_revision NVARCHAR(255),
    p_showemptyattributes TINYINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_codeIDX_142 ON junit_categories (p_code);

CREATE INDEX junit_versionIDX_142 ON junit_categories (p_catalogversion);

CREATE INDEX junit_codeVersionIDX_142 ON junit_categories (p_code, p_catalogversion);

CREATE INDEX junit_extID_142 ON junit_categories (p_externalid);


CREATE CACHED TABLE junit_categorieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_catverdiffs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_sourceversion BIGINT,
    p_targetversion BIGINT,
    p_cronjob BIGINT,
    p_differencetext LONGVARCHAR,
    p_differencevalue DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_sourceproduct BIGINT,
    p_targetproduct BIGINT,
    p_mode BIGINT,
    p_sourcecategory BIGINT,
    p_targetcategory BIGINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_changedescriptors
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_cronjob BIGINT,
    p_step BIGINT,
    p_changeditem BIGINT,
    p_sequencenumber INTEGER,
    p_savetimestamp TIMESTAMP,
    p_changetype NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_targetitem BIGINT,
    p_done TINYINT,
    p_copiedimplicitely TINYINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_cronjobIDX_505 ON junit_changedescriptors (p_cronjob);

CREATE INDEX junit_stepIDX_505 ON junit_changedescriptors (p_step);

CREATE INDEX junit_changedItemIDX_505 ON junit_changedescriptors (p_changeditem);

CREATE INDEX junit_seqNrIDX_505 ON junit_changedescriptors (p_sequencenumber);

CREATE INDEX junit_doneIDX_505 ON junit_changedescriptors (p_done);


CREATE CACHED TABLE junit_classattrvalues
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_externalid NVARCHAR(255),
    p_systemversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_sysVer_608 ON junit_classattrvalues (p_systemversion);

CREATE INDEX junit_code_608 ON junit_classattrvalues (p_code);

CREATE INDEX junit_idIDX_608 ON junit_classattrvalues (p_externalid);


CREATE CACHED TABLE junit_classattrvalueslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_classificationattrs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_systemversion BIGINT,
    p_code NVARCHAR(255),
    p_externalid NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_sysVer_607 ON junit_classificationattrs (p_systemversion);

CREATE INDEX junit_code_607 ON junit_classificationattrs (p_code);

CREATE INDEX junit_idIDX_607 ON junit_classificationattrs (p_externalid);


CREATE CACHED TABLE junit_classificationattrslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_clattrunt
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_systemversion BIGINT,
    p_code NVARCHAR(255),
    p_externalid NVARCHAR(255),
    p_symbol NVARCHAR(255),
    p_unittype NVARCHAR(255),
    p_conversionfactor DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_sysVer_612 ON junit_clattrunt (p_systemversion);

CREATE INDEX junit_codeIdx_612 ON junit_clattrunt (p_code);

CREATE INDEX junit_extID_612 ON junit_clattrunt (p_externalid);


CREATE CACHED TABLE junit_clattruntlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cmptype2covgrprels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_978 ON junit_cmptype2covgrprels (SequenceNumber);

CREATE INDEX junit_rseqnr_978 ON junit_cmptype2covgrprels (RSequenceNumber);

CREATE INDEX junit_linksource_978 ON junit_cmptype2covgrprels (SourcePK);

CREATE INDEX junit_linktarget_978 ON junit_cmptype2covgrprels (TargetPK);

CREATE INDEX junit_qualifier_978 ON junit_cmptype2covgrprels (Qualifier);


CREATE CACHED TABLE junit_cockpitcollections
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_qualifier NVARCHAR(255),
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_collectiontype BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_userRelIDX_1700 ON junit_cockpitcollections (p_user);


CREATE CACHED TABLE junit_cockpitcollectionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_label NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cockpitcollelements
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_objecttypecode NVARCHAR(255),
    p_collection BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_collectionRelIDX_1701 ON junit_cockpitcollelements (p_collection);


CREATE CACHED TABLE junit_cockpitcollitemrefs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_objecttypecode NVARCHAR(255),
    p_collection BIGINT,
    p_item BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_itemIDX_1702 ON junit_cockpitcollitemrefs (p_item);

CREATE INDEX junit_collectionRelIDX_1702 ON junit_cockpitcollitemrefs (p_collection);


CREATE CACHED TABLE junit_cockpitcompaccessrights
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_cockpitcompconfigs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_factorybean NVARCHAR(255),
    p_code NVARCHAR(255),
    p_objecttemplatecode NVARCHAR(255),
    p_media BIGINT,
    p_principal BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_principalRelIDX_1707 ON junit_cockpitcompconfigs (p_principal);


CREATE CACHED TABLE junit_cockpitfavcategories
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_qualifier NVARCHAR(255),
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_userRelIDX_1718 ON junit_cockpitfavcategories (p_user);


CREATE CACHED TABLE junit_cockpitfavcategorieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_label NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cockpititemtemplates
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_relatedtype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_relatedtypeRelIDX_24242 ON junit_cockpititemtemplates (p_relatedtype);


CREATE CACHED TABLE junit_cockpititemtemplateslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cockpitsavedfacvalues
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_facetqualifier NVARCHAR(255),
    p_valuequalifier NVARCHAR(255),
    p_cockpitsavedquery BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_cockpitsavedqueryRelIDX_1704 ON junit_cockpitsavedfacvalues (p_cockpitsavedquery);


CREATE CACHED TABLE junit_cockpitsavedparamvals
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_rawvalue LONGVARCHAR,
    p_operatorqualifier NVARCHAR(255),
    p_languageiso NVARCHAR(255),
    p_parameterqualifier NVARCHAR(255),
    p_casesensitive TINYINT,
    p_reference TINYINT,
    p_cockpitsavedquery BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_cockpitsavedqueryRelIDX_1706 ON junit_cockpitsavedparamvals (p_cockpitsavedquery);


CREATE CACHED TABLE junit_cockpitsavedqueries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_label NVARCHAR(255),
    p_simpletext NVARCHAR(255),
    p_selectedtypecode NVARCHAR(255),
    p_selectedtemplatecode NVARCHAR(255),
    p_code NVARCHAR(255),
    p_defaultviewmode NVARCHAR(255),
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_userRelIDX_1703 ON junit_cockpitsavedqueries (p_user);


CREATE CACHED TABLE junit_cockpitsavedquerieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cockpitsavedsortcrits
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_criterionqualifier NVARCHAR(255),
    p_asc TINYINT,
    p_cockpitsavedquery BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_cockpitsavedqueryRelIDX_1705 ON junit_cockpitsavedsortcrits (p_cockpitsavedquery);


CREATE CACHED TABLE junit_cockpittemplclassifrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1713 ON junit_cockpittemplclassifrels (SequenceNumber);

CREATE INDEX junit_rseqnr_1713 ON junit_cockpittemplclassifrels (RSequenceNumber);

CREATE INDEX junit_linksource_1713 ON junit_cockpittemplclassifrels (SourcePK);

CREATE INDEX junit_linktarget_1713 ON junit_cockpittemplclassifrels (TargetPK);

CREATE INDEX junit_qualifier_1713 ON junit_cockpittemplclassifrels (Qualifier);


CREATE CACHED TABLE junit_collectiontypes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    InternalCode NVARCHAR(255),
    p_defaultvalue LONGVARBINARY,
    ElementTypePK BIGINT,
    typeOfCollection INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    InternalCodeLowerCase NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX junit_typecode_83 ON junit_collectiontypes (InternalCode);

CREATE INDEX junit_typecodelowercase_83 ON junit_collectiontypes (InternalCodeLowerCase);


CREATE CACHED TABLE junit_collectiontypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_commentassignrelations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1148 ON junit_commentassignrelations (SequenceNumber);

CREATE INDEX junit_rseqnr_1148 ON junit_commentassignrelations (RSequenceNumber);

CREATE INDEX junit_linksource_1148 ON junit_commentassignrelations (SourcePK);

CREATE INDEX junit_linktarget_1148 ON junit_commentassignrelations (TargetPK);

CREATE INDEX junit_qualifier_1148 ON junit_commentassignrelations (Qualifier);


CREATE CACHED TABLE junit_commentattachments
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_item BIGINT,
    p_abstractcomment BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_comm_att_comment_1146 ON junit_commentattachments (p_abstractcomment);


CREATE CACHED TABLE junit_commentcompcreaterels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1152 ON junit_commentcompcreaterels (SequenceNumber);

CREATE INDEX junit_rseqnr_1152 ON junit_commentcompcreaterels (RSequenceNumber);

CREATE INDEX junit_linksource_1152 ON junit_commentcompcreaterels (SourcePK);

CREATE INDEX junit_linktarget_1152 ON junit_commentcompcreaterels (TargetPK);

CREATE INDEX junit_qualifier_1152 ON junit_commentcompcreaterels (Qualifier);


CREATE CACHED TABLE junit_commentcomponents
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_name NVARCHAR(255),
    p_domain BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_commComponent_code_1142 ON junit_commentcomponents (p_code);

CREATE INDEX junit_domainRelIDX_1142 ON junit_commentcomponents (p_domain);


CREATE CACHED TABLE junit_commentcompreadrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1150 ON junit_commentcompreadrels (SequenceNumber);

CREATE INDEX junit_rseqnr_1150 ON junit_commentcompreadrels (RSequenceNumber);

CREATE INDEX junit_linksource_1150 ON junit_commentcompreadrels (SourcePK);

CREATE INDEX junit_linktarget_1150 ON junit_commentcompreadrels (TargetPK);

CREATE INDEX junit_qualifier_1150 ON junit_commentcompreadrels (Qualifier);


CREATE CACHED TABLE junit_commentcompremoverels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1153 ON junit_commentcompremoverels (SequenceNumber);

CREATE INDEX junit_rseqnr_1153 ON junit_commentcompremoverels (RSequenceNumber);

CREATE INDEX junit_linksource_1153 ON junit_commentcompremoverels (SourcePK);

CREATE INDEX junit_linktarget_1153 ON junit_commentcompremoverels (TargetPK);

CREATE INDEX junit_qualifier_1153 ON junit_commentcompremoverels (Qualifier);


CREATE CACHED TABLE junit_commentcompwriterels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1151 ON junit_commentcompwriterels (SequenceNumber);

CREATE INDEX junit_rseqnr_1151 ON junit_commentcompwriterels (RSequenceNumber);

CREATE INDEX junit_linksource_1151 ON junit_commentcompwriterels (SourcePK);

CREATE INDEX junit_linktarget_1151 ON junit_commentcompwriterels (TargetPK);

CREATE INDEX junit_qualifier_1151 ON junit_commentcompwriterels (Qualifier);


CREATE CACHED TABLE junit_commentdomains
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_name NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_commDomain_code_1141 ON junit_commentdomains (p_code);


CREATE CACHED TABLE junit_commentitemrelations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1147 ON junit_commentitemrelations (SequenceNumber);

CREATE INDEX junit_rseqnr_1147 ON junit_commentitemrelations (RSequenceNumber);

CREATE INDEX junit_linksource_1147 ON junit_commentitemrelations (SourcePK);

CREATE INDEX junit_linktarget_1147 ON junit_commentitemrelations (TargetPK);

CREATE INDEX junit_qualifier_1147 ON junit_commentitemrelations (Qualifier);


CREATE CACHED TABLE junit_commentmetadatas
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_x INTEGER,
    p_y INTEGER,
    p_pageindex INTEGER,
    p_item BIGINT,
    p_comment BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_commentRelIDX_1716 ON junit_commentmetadatas (p_comment);


CREATE CACHED TABLE junit_comments
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_subject NVARCHAR(255),
    p_author BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_code NVARCHAR(255),
    p_priority INTEGER,
    p_component BIGINT,
    p_commenttype BIGINT,
    p_parentpos INTEGER,
    p_parent BIGINT,
    p_commentpos INTEGER,
    p_comment BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_comment_code_1140 ON junit_comments (p_code);

CREATE INDEX junit_comment_component_1140 ON junit_comments (p_component);

CREATE INDEX junit_reply_parent_1140 ON junit_comments (p_parent);

CREATE INDEX junit_reply_comment_1140 ON junit_comments (p_comment);

CREATE INDEX junit_parentposPosIDX_1140 ON junit_comments (p_parentpos);

CREATE INDEX junit_commentposPosIDX_1140 ON junit_comments (p_commentpos);

CREATE INDEX junit_authorRelIDX_1140 ON junit_comments (p_author);

CREATE INDEX junit_commenttypeRelIDX_1140 ON junit_comments (p_commenttype);


CREATE CACHED TABLE junit_commenttypes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_name NVARCHAR(255),
    p_metatype BIGINT,
    p_domain BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_comment_type_code_1145 ON junit_commenttypes (p_code, p_domain);

CREATE INDEX junit_domainRelIDX_1145 ON junit_commenttypes (p_domain);


CREATE CACHED TABLE junit_commentusersettings
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_read TINYINT,
    p_ignored TINYINT,
    p_priority INTEGER,
    p_comment BIGINT,
    p_user BIGINT,
    p_workstatus TINYINT,
    p_hidden TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_cus_user_comment_1144 ON junit_commentusersettings (p_user, p_comment);

CREATE INDEX junit_commentRelIDX_1144 ON junit_commentusersettings (p_comment);

CREATE INDEX junit_userRelIDX_1144 ON junit_commentusersettings (p_user);


CREATE CACHED TABLE junit_commentwatchrelations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1149 ON junit_commentwatchrelations (SequenceNumber);

CREATE INDEX junit_rseqnr_1149 ON junit_commentwatchrelations (RSequenceNumber);

CREATE INDEX junit_linksource_1149 ON junit_commentwatchrelations (SourcePK);

CREATE INDEX junit_linktarget_1149 ON junit_commentwatchrelations (TargetPK);

CREATE INDEX junit_qualifier_1149 ON junit_commentwatchrelations (Qualifier);


CREATE CACHED TABLE junit_competitionplayerrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_168 ON junit_competitionplayerrel (SequenceNumber);

CREATE INDEX junit_rseqnr_168 ON junit_competitionplayerrel (RSequenceNumber);

CREATE INDEX junit_linksource_168 ON junit_competitionplayerrel (SourcePK);

CREATE INDEX junit_linktarget_168 ON junit_competitionplayerrel (TargetPK);

CREATE INDEX junit_qualifier_168 ON junit_competitionplayerrel (Qualifier);


CREATE CACHED TABLE junit_composedtypes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    InternalCode NVARCHAR(255),
    p_defaultvalue LONGVARBINARY,
    InheritancePathString LONGVARCHAR,
    jaloClassName NVARCHAR(255),
    ItemJNDIName NVARCHAR(255),
    Singleton TINYINT DEFAULT 0,
    p_jaloonly TINYINT,
    p_dynamic TINYINT,
    SuperTypePK BIGINT,
    p_legacypersistence TINYINT,
    p_systemtype TINYINT,
    p_catalogitemtype TINYINT,
    p_catalogversionattributequali NVARCHAR(255),
    p_uniquekeyattributequalifier NVARCHAR(255),
    p_hmcicon BIGINT,
    p_loghmcchanges TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    InternalCodeLowerCase NVARCHAR(255),
    removable TINYINT DEFAULT 0,
    propertyTableStatus TINYINT DEFAULT 0,
    ItemTypeCode INTEGER DEFAULT 0,
    p_comparationattribute BIGINT,
    p_localized TINYINT,
    p_sourceattribute BIGINT,
    p_targetattribute BIGINT,
    p_sourcetype BIGINT,
    p_targettype BIGINT,
    p_orderingattribute BIGINT,
    p_localizationattribute BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_ComposedTypeSuperTypePKIDX_82 ON junit_composedtypes (SuperTypePK);

CREATE INDEX junit_inheritpsi_82 ON junit_composedtypes (InheritancePathString);

CREATE INDEX junit_typecode_82 ON junit_composedtypes (InternalCode);

CREATE INDEX junit_typecodelowercase_82 ON junit_composedtypes (InternalCodeLowerCase);


CREATE CACHED TABLE junit_composedtypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_compositeentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_executablecronjob BIGINT,
    p_triggerablejob BIGINT,
    p_compositecronjobpos INTEGER,
    p_compositecronjob BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_compositecronjobRelIDX_510 ON junit_compositeentries (p_compositecronjob);

CREATE INDEX junit_compositecronjobposPosIDX_510 ON junit_compositeentries (p_compositecronjobpos);


CREATE CACHED TABLE junit_configitems
(
    hjmpTS BIGINT,
    PK BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    aCLTS BIGINT DEFAULT 0,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_constraintgroup
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_id NVARCHAR(255),
    p_interfacename NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_coveragedomainid NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX junit_CronstraintGroup_idx_982 ON junit_constraintgroup (p_id);


CREATE CACHED TABLE junit_conversionerrors
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_targetformat BIGINT,
    p_sourcemedia BIGINT,
    p_errormessage LONGVARCHAR,
    p_container BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_containerRelIDX_403 ON junit_conversionerrors (p_container);


CREATE CACHED TABLE junit_conversiongroups
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_ConvGroup_code_idx_401 ON junit_conversiongroups (p_code);


CREATE CACHED TABLE junit_conversiongroupslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_countries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_active TINYINT,
    p_isocode NVARCHAR(255),
    p_flag BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_dummy TINYINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_ISOCode_34 ON junit_countries (p_isocode);


CREATE CACHED TABLE junit_countrieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cronjobs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_errormode BIGINT,
    p_logtofile TINYINT,
    p_logtodatabase TINYINT,
    p_loglevelfile BIGINT,
    p_logleveldatabase BIGINT,
    p_sessionuser BIGINT,
    p_sessionlanguage BIGINT,
    p_sessioncurrency BIGINT,
    p_active TINYINT,
    p_retry TINYINT,
    p_singleexecutable TINYINT,
    p_emailaddress NVARCHAR(255),
    p_sendemail TINYINT,
    p_starttime TIMESTAMP,
    p_endtime TIMESTAMP,
    p_status BIGINT,
    p_result BIGINT,
    p_nodeid INTEGER,
    p_nodegroup NVARCHAR(255),
    p_runningonclusternode INTEGER,
    p_currentstep BIGINT,
    p_changerecordingenabled TINYINT,
    p_requestabort TINYINT,
    p_requestabortstep TINYINT,
    p_priority INTEGER,
    p_removeonexit TINYINT,
    p_emailnotificationtemplate BIGINT,
    p_alternativedatasourceid NVARCHAR(255),
    p_logsdaysold INTEGER,
    p_logscount INTEGER,
    p_logsoperator BIGINT,
    p_filesdaysold INTEGER,
    p_filescount INTEGER,
    p_filesoperator BIGINT,
    p_job BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_jobmedia BIGINT,
    p_currentline INTEGER,
    p_lastsuccessfulline INTEGER,
    p_url NVARCHAR(255),
    p_query NVARCHAR(255),
    p_failonunknown TINYINT,
    p_dontneedtotal TINYINT,
    p_rangestart INTEGER,
    p_count INTEGER,
    p_searchbase NVARCHAR(255),
    p_ldapquery NVARCHAR(255),
    p_resultfilter NVARCHAR(255),
    p_importmode BIGINT,
    p_ldiffile BIGINT,
    p_configfile BIGINT,
    p_destmedia BIGINT,
    p_dumpmedia BIGINT,
    p_usersearchfieldqualifier NVARCHAR(255),
    p_userrootdn NVARCHAR(255),
    p_userresultfilter NVARCHAR(255),
    p_importusers TINYINT,
    p_codeexecution TINYINT,
    p_itempks BIGINT,
    p_itemsfound INTEGER,
    p_itemsdeleted INTEGER,
    p_itemsrefused INTEGER,
    p_createsavedvalues TINYINT,
    p_medias LONGVARCHAR,
    p_targetfolder BIGINT,
    p_movedmediascount INTEGER,
    p_mediafolder BIGINT,
    p_timethreshold INTEGER,
    p_versionthreshold INTEGER,
    p_xdaysold INTEGER,
    p_excludecronjobs LONGVARCHAR,
    p_resultcoll LONGVARCHAR,
    p_statuscoll LONGVARCHAR,
    p_encoding BIGINT,
    p_mode BIGINT,
    p_dataexporttarget BIGINT,
    p_mediasexporttarget BIGINT,
    p_exporttemplate BIGINT,
    p_export BIGINT,
    p_itemsexported INTEGER,
    p_itemsmaxcount INTEGER,
    p_itemsskipped INTEGER,
    p_fieldseparator SMALLINT,
    p_quotecharacter SMALLINT,
    p_commentcharacter SMALLINT,
    p_dataexportmediacode NVARCHAR(255),
    p_mediasexportmediacode NVARCHAR(255),
    p_report BIGINT,
    p_converterclass BIGINT,
    p_singlefile TINYINT,
    p_workmedia BIGINT,
    p_mediasmedia BIGINT,
    p_externaldatacollection LONGVARCHAR,
    p_locale NVARCHAR(255),
    p_dumpfileencoding BIGINT,
    p_enablecodeexecution TINYINT,
    p_enableexternalcodeexecution TINYINT,
    p_enableexternalsyntaxparsing TINYINT,
    p_enablehmcsavedvalues TINYINT,
    p_mediastarget NVARCHAR(255),
    p_valuecount INTEGER,
    p_unresolveddatastore BIGINT,
    p_dumpingallowed TINYINT,
    p_unzipmediasmedia TINYINT,
    p_maxthreads INTEGER,
    p_legacymode TINYINT,
    p_processeditemscount INTEGER,
    p_sourceversion BIGINT,
    p_targetversion BIGINT,
    p_missingproducts INTEGER,
    p_newproducts INTEGER,
    p_maxpricetolerance DOUBLE,
    p_searchmissingproducts TINYINT,
    p_searchmissingcategories TINYINT,
    p_searchnewproducts TINYINT,
    p_searchnewcategories TINYINT,
    p_searchpricedifferences TINYINT,
    p_overwriteproductapprovalstat TINYINT,
    p_pricecomparecustomer BIGINT,
    p_catalog BIGINT,
    p_catalogversion BIGINT,
    p_dontremoveobjects TINYINT,
    p_notremoveditems BIGINT,
    p_totaldeleteitemcount INTEGER,
    p_currentprocessingitemcount INTEGER,
    p_forceupdate TINYINT,
    p_statusmessage NVARCHAR(255),
    p_includedformats LONGVARCHAR,
    p_asynchronous TINYINT,
    p_includeconverted TINYINT,
    p_containermediasonly TINYINT,
    p_laststarttime TIMESTAMP,
    p_competition BIGINT,
    p_paging INTEGER,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_IdxJob_501 ON junit_cronjobs (p_job);

CREATE INDEX junit_IdxNode_501 ON junit_cronjobs (p_nodeid);

CREATE INDEX junit_IdxActive_501 ON junit_cronjobs (p_active);


CREATE CACHED TABLE junit_cronjobslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cstrgr2abscstrrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_979 ON junit_cstrgr2abscstrrel (SequenceNumber);

CREATE INDEX junit_rseqnr_979 ON junit_cstrgr2abscstrrel (RSequenceNumber);

CREATE INDEX junit_linksource_979 ON junit_cstrgr2abscstrrel (SourcePK);

CREATE INDEX junit_linktarget_979 ON junit_cstrgr2abscstrrel (TargetPK);

CREATE INDEX junit_qualifier_979 ON junit_cstrgr2abscstrrel (Qualifier);


CREATE CACHED TABLE junit_cuppycompetition
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_type BIGINT,
    p_finished TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_Competition_unique_166 ON junit_cuppycompetition (p_code);


CREATE CACHED TABLE junit_cuppycompetitionlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cuppygroup
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_multiplier FLOAT,
    p_sequencenumber INTEGER,
    p_competition BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_Group_unique_162 ON junit_cuppygroup (p_code, p_competition);

CREATE INDEX junit_competitionRelIDX_162 ON junit_cuppygroup (p_competition);


CREATE CACHED TABLE junit_cuppygrouplp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cuppymatch
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_id INTEGER,
    p_guestgoals INTEGER,
    p_homegoals INTEGER,
    p_location NVARCHAR(255),
    p_date TIMESTAMP,
    p_matchday INTEGER,
    p_group BIGINT,
    p_guestteam BIGINT,
    p_hometeam BIGINT,
    p_stadium BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_Match_unique_161 ON junit_cuppymatch (p_id, p_group);

CREATE INDEX junit_Match_matchday_161 ON junit_cuppymatch (p_matchday);

CREATE INDEX junit_groupRelIDX_161 ON junit_cuppymatch (p_group);

CREATE INDEX junit_guestteamRelIDX_161 ON junit_cuppymatch (p_guestteam);

CREATE INDEX junit_hometeamRelIDX_161 ON junit_cuppymatch (p_hometeam);

CREATE INDEX junit_stadiumRelIDX_161 ON junit_cuppymatch (p_stadium);


CREATE CACHED TABLE junit_cuppymatchbet
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_guestgoals INTEGER,
    p_homegoals INTEGER,
    p_player BIGINT,
    p_match BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_MatchBet_unique_160 ON junit_cuppymatchbet (p_player, p_match);

CREATE INDEX junit_playerRelIDX_160 ON junit_cuppymatchbet (p_player);

CREATE INDEX junit_matchRelIDX_160 ON junit_cuppymatchbet (p_match);


CREATE CACHED TABLE junit_cuppynews
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_email TINYINT,
    p_competition BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_competitionRelIDX_163 ON junit_cuppynews (p_competition);


CREATE CACHED TABLE junit_cuppynewslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_content varchar(4000),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_cuppyoverallstats
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_playersonlinemaxcount INTEGER,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_cuppyplayerpreferences
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_currentcompetition BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_cuppytimepointstats
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_playersonlinecount INTEGER,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_cuppytrailstadium
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_capacity INTEGER,
    p_stadiumtype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_currencies
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_active TINYINT,
    p_isocode NVARCHAR(255),
    p_base TINYINT,
    p_conversion DOUBLE,
    p_digits INTEGER,
    p_symbol NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_ISOCode_33 ON junit_currencies (p_isocode);


CREATE CACHED TABLE junit_currencieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_deliverymodes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_active TINYINT,
    p_code NVARCHAR(255),
    p_supportedpaymentmodesinterna NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_propertyname NVARCHAR(255),
    p_net TINYINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_deliverymodeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_derivedmedias
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_mime NVARCHAR(255),
    p_size BIGINT,
    p_datapk BIGINT,
    p_location LONGVARCHAR,
    p_locationhash NVARCHAR(255),
    p_realfilename NVARCHAR(255),
    p_version NVARCHAR(255),
    p_media BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_dataPK_idx_31 ON junit_derivedmedias (p_datapk);

CREATE UNIQUE INDEX junit_version_idx_31 ON junit_derivedmedias (p_media, p_version);

CREATE INDEX junit_mediaRelIDX_31 ON junit_derivedmedias (p_media);


CREATE CACHED TABLE junit_discountrows
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_product BIGINT,
    p_pg BIGINT,
    p_productmatchqualifier BIGINT,
    p_starttime TIMESTAMP,
    p_endtime TIMESTAMP,
    p_user BIGINT,
    p_ug BIGINT,
    p_usermatchqualifier BIGINT,
    p_productid NVARCHAR(255),
    p_currency BIGINT,
    p_discount BIGINT,
    p_value DOUBLE,
    p_catalogversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_MatchIndexP_1052 ON junit_discountrows (p_productmatchqualifier);

CREATE INDEX junit_MatchIndexU_1052 ON junit_discountrows (p_usermatchqualifier);

CREATE INDEX junit_PIdx_1052 ON junit_discountrows (p_product);

CREATE INDEX junit_UIdx_1052 ON junit_discountrows (p_user);

CREATE INDEX junit_PGIdx_1052 ON junit_discountrows (p_pg);

CREATE INDEX junit_UGIdx_1052 ON junit_discountrows (p_ug);

CREATE INDEX junit_ProductIdIdx_1052 ON junit_discountrows (p_productid);

CREATE INDEX junit_versionIDX_1052 ON junit_discountrows (p_catalogversion);


CREATE CACHED TABLE junit_discounts
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_currency BIGINT,
    p_global TINYINT,
    p_priority INTEGER,
    p_value DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_discountslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_dynamiccontent
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_active TINYINT,
    p_checksum NVARCHAR(255),
    p_content LONGVARCHAR,
    p_version BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_codeVersionActiveIDX_101 ON junit_dynamiccontent (p_code, p_version, p_active);


CREATE CACHED TABLE junit_enumerationvalues
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    Code NVARCHAR(255),
    SequenceNumber INTEGER DEFAULT 0,
    p_extensionname NVARCHAR(255),
    p_icon BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    codeLowerCase NVARCHAR(255),
    Editable TINYINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnridx_91 ON junit_enumerationvalues (SequenceNumber);

CREATE INDEX junit_codeidx_91 ON junit_enumerationvalues (Code);

CREATE INDEX junit_code2idx_91 ON junit_enumerationvalues (codeLowerCase);


CREATE CACHED TABLE junit_enumerationvalueslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_exports
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_exportedmedias BIGINT,
    p_exporteddata BIGINT,
    p_exportscript BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_exportslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_externalimportkey
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_sourcesystemid NVARCHAR(255),
    p_sourcekey NVARCHAR(255),
    p_targetpk BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_sourceSystemIDSourceKeyIDX_110 ON junit_externalimportkey (p_sourcesystemid, p_sourcekey);


CREATE CACHED TABLE junit_format
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_initial BIGINT,
    p_documenttype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_format2comtyprel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_13102 ON junit_format2comtyprel (SequenceNumber);

CREATE INDEX junit_rseqnr_13102 ON junit_format2comtyprel (RSequenceNumber);

CREATE INDEX junit_linksource_13102 ON junit_format2comtyprel (SourcePK);

CREATE INDEX junit_linktarget_13102 ON junit_format2comtyprel (TargetPK);

CREATE INDEX junit_qualifier_13102 ON junit_format2comtyprel (Qualifier);


CREATE CACHED TABLE junit_format2medforrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_13101 ON junit_format2medforrel (SequenceNumber);

CREATE INDEX junit_rseqnr_13101 ON junit_format2medforrel (RSequenceNumber);

CREATE INDEX junit_linksource_13101 ON junit_format2medforrel (SourcePK);

CREATE INDEX junit_linktarget_13101 ON junit_format2medforrel (TargetPK);

CREATE INDEX junit_qualifier_13101 ON junit_format2medforrel (Qualifier);


CREATE CACHED TABLE junit_formatlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_genericitems
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_code NVARCHAR(255),
    p_actiontemplate BIGINT,
    p_action BIGINT,
    p_comparator BIGINT,
    p_emptyhandling BIGINT,
    p_valuetype BIGINT,
    p_searchparametername NVARCHAR(255),
    p_joinalias NVARCHAR(255),
    p_lower TINYINT,
    p_wherepart BIGINT,
    p_typedsearchparameter BIGINT,
    p_enclosingtype BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_actiontemplateRelIDX_99 ON junit_genericitems (p_actiontemplate);

CREATE INDEX junit_actionRelIDX_99 ON junit_genericitems (p_action);

CREATE INDEX junit_wherepartRelIDX_99 ON junit_genericitems (p_wherepart);


CREATE CACHED TABLE junit_genericitemslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_gentestitems
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_globaldiscountrows
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_product BIGINT,
    p_pg BIGINT,
    p_productmatchqualifier BIGINT,
    p_starttime TIMESTAMP,
    p_endtime TIMESTAMP,
    p_user BIGINT,
    p_ug BIGINT,
    p_usermatchqualifier BIGINT,
    p_productid NVARCHAR(255),
    p_currency BIGINT,
    p_discount BIGINT,
    p_value DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_MatchIndexP_1053 ON junit_globaldiscountrows (p_productmatchqualifier);

CREATE INDEX junit_MatchIndexU_1053 ON junit_globaldiscountrows (p_usermatchqualifier);

CREATE INDEX junit_PIdx_1053 ON junit_globaldiscountrows (p_product);

CREATE INDEX junit_UIdx_1053 ON junit_globaldiscountrows (p_user);

CREATE INDEX junit_PGIdx_1053 ON junit_globaldiscountrows (p_pg);

CREATE INDEX junit_UGIdx_1053 ON junit_globaldiscountrows (p_ug);

CREATE INDEX junit_ProductIdIdx_1053 ON junit_globaldiscountrows (p_productid);


CREATE CACHED TABLE junit_hmchistoryentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_user BIGINT,
    p_timestamp TIMESTAMP,
    p_actiontype BIGINT,
    p_comment NVARCHAR(255),
    p_referenceditem BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_indextestitem
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_column1 SMALLINT,
    p_column2 SMALLINT,
    p_column3 SMALLINT,
    p_column4 SMALLINT,
    p_column5 SMALLINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_OrderIndex_7777 ON junit_indextestitem (p_column3, p_column4, p_column1, p_column2, p_column5);


CREATE CACHED TABLE junit_itemcockpittemplrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1712 ON junit_itemcockpittemplrels (SequenceNumber);

CREATE INDEX junit_rseqnr_1712 ON junit_itemcockpittemplrels (RSequenceNumber);

CREATE INDEX junit_linksource_1712 ON junit_itemcockpittemplrels (SourcePK);

CREATE INDEX junit_linktarget_1712 ON junit_itemcockpittemplrels (TargetPK);

CREATE INDEX junit_qualifier_1712 ON junit_itemcockpittemplrels (Qualifier);


CREATE CACHED TABLE junit_itemsynctimestamps
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_syncjob BIGINT,
    p_sourceitem BIGINT,
    p_targetitem BIGINT,
    p_sourceversion BIGINT,
    p_targetversion BIGINT,
    p_lastsyncsourcemodifiedtime TIMESTAMP,
    p_lastsynctime TIMESTAMP,
    p_pendingattributesownerjob BIGINT,
    p_pendingattributesscheduledtu INTEGER,
    p_pendingattributequalifiers LONGVARCHAR,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_syncIDX_619 ON junit_itemsynctimestamps (p_sourceitem, p_targetversion, p_syncjob);

CREATE INDEX junit_srcIDX_619 ON junit_itemsynctimestamps (p_sourceitem);

CREATE INDEX junit_tgtIDX_619 ON junit_itemsynctimestamps (p_targetitem);

CREATE INDEX junit_jobIDX_619 ON junit_itemsynctimestamps (p_syncjob);

CREATE INDEX junit_srcVerIDX_619 ON junit_itemsynctimestamps (p_sourceversion);

CREATE INDEX junit_tgtVerIDX_619 ON junit_itemsynctimestamps (p_targetversion);


CREATE CACHED TABLE junit_jalotranslatorconfig
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_jalovelocityrenderer
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_name NVARCHAR(255),
    p_template LONGVARCHAR,
    p_translatorconfigurationpos INTEGER,
    p_translatorconfiguration BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_translatorconfigurationRelIDX_13211 ON junit_jalovelocityrenderer (p_translatorconfiguration);

CREATE INDEX junit_translatorconfigurationposPosIDX_13211 ON junit_jalovelocityrenderer (p_translatorconfigurationpos);


CREATE CACHED TABLE junit_joblogs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_step BIGINT,
    p_level BIGINT,
    p_cronjob BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_cronjobIDX_504 ON junit_joblogs (p_cronjob);


CREATE CACHED TABLE junit_jobs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_nodeid INTEGER,
    p_nodegroup NVARCHAR(255),
    p_errormode BIGINT,
    p_logtofile TINYINT,
    p_logtodatabase TINYINT,
    p_loglevelfile BIGINT,
    p_logleveldatabase BIGINT,
    p_sessionuser BIGINT,
    p_sessionlanguage BIGINT,
    p_sessioncurrency BIGINT,
    p_active TINYINT,
    p_retry TINYINT,
    p_singleexecutable TINYINT,
    p_emailaddress NVARCHAR(255),
    p_sendemail TINYINT,
    p_changerecordingenabled TINYINT,
    p_requestabort TINYINT,
    p_requestabortstep TINYINT,
    p_priority INTEGER,
    p_removeonexit TINYINT,
    p_emailnotificationtemplate BIGINT,
    p_alternativedatasourceid NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_springid NVARCHAR(255),
    p_springidcronjobfactory NVARCHAR(255),
    p_scripturi NVARCHAR(255),
    p_threshold INTEGER,
    p_searchtype BIGINT,
    p_searchscript NVARCHAR(255),
    p_processscript NVARCHAR(255),
    p_maxthreads INTEGER,
    p_sourceversion BIGINT,
    p_targetversion BIGINT,
    p_exclusivemode TINYINT,
    p_syncprincipalsonly TINYINT,
    p_createnewitems TINYINT,
    p_removemissingitems TINYINT,
    p_syncorder INTEGER,
    p_copycachesize INTEGER,
    p_enabletransactions TINYINT,
    p_maxschedulerthreads INTEGER,
    p_activationscript LONGVARCHAR,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_IdxCode_500 ON junit_jobs (p_code);


CREATE CACHED TABLE junit_jobsearchrestriction
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_type BIGINT,
    p_jobpos INTEGER,
    p_job BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_jobRelIDX_508 ON junit_jobsearchrestriction (p_job);

CREATE INDEX junit_jobposPosIDX_508 ON junit_jobsearchrestriction (p_jobpos);


CREATE CACHED TABLE junit_jobslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_keywords
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_keyword NVARCHAR(255),
    p_language BIGINT,
    p_catalog BIGINT,
    p_catalogversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_externalid NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX junit_keywordIDX_602 ON junit_keywords (p_keyword);

CREATE INDEX junit_versionIDX_602 ON junit_keywords (p_catalogversion);

CREATE INDEX junit_codeVersionIDX_602 ON junit_keywords (p_keyword, p_catalogversion);

CREATE INDEX junit_extIDX_602 ON junit_keywords (p_externalid);


CREATE CACHED TABLE junit_languages
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_active TINYINT,
    p_isocode NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_ISOCode_32 ON junit_languages (p_isocode);


CREATE CACHED TABLE junit_languageslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_links
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_7 ON junit_links (SequenceNumber);

CREATE INDEX junit_rseqnr_7 ON junit_links (RSequenceNumber);

CREATE INDEX junit_linksource_7 ON junit_links (SourcePK);

CREATE INDEX junit_linktarget_7 ON junit_links (TargetPK);

CREATE INDEX junit_qualifier_7 ON junit_links (Qualifier);


CREATE CACHED TABLE junit_maptypes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    InternalCode NVARCHAR(255),
    p_defaultvalue LONGVARBINARY,
    ArgumentTypePK BIGINT,
    ReturnTypePK BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    InternalCodeLowerCase NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX junit_typecode_84 ON junit_maptypes (InternalCode);

CREATE INDEX junit_typecodelowercase_84 ON junit_maptypes (InternalCodeLowerCase);


CREATE CACHED TABLE junit_maptypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_mediacontainer
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_qualifier NVARCHAR(255),
    p_catalogversion BIGINT,
    p_conversiongroup BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_versionIDX_50 ON junit_mediacontainer (p_catalogversion);

CREATE INDEX junit_codeVersionIDX_50 ON junit_mediacontainer (p_qualifier, p_catalogversion);


CREATE CACHED TABLE junit_mediacontainerlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_mediacontext
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_qualifier NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_qualifierIDX_52 ON junit_mediacontext (p_qualifier);


CREATE CACHED TABLE junit_mediacontextlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_mediaconttypeformats
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_402 ON junit_mediaconttypeformats (SequenceNumber);

CREATE INDEX junit_rseqnr_402 ON junit_mediaconttypeformats (RSequenceNumber);

CREATE INDEX junit_linksource_402 ON junit_mediaconttypeformats (SourcePK);

CREATE INDEX junit_linktarget_402 ON junit_mediaconttypeformats (TargetPK);

CREATE INDEX junit_qualifier_402 ON junit_mediaconttypeformats (Qualifier);


CREATE CACHED TABLE junit_mediafolders
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_qualifier NVARCHAR(255),
    p_path NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_qualifierIdx_54 ON junit_mediafolders (p_qualifier);


CREATE CACHED TABLE junit_mediaformat
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_qualifier NVARCHAR(255),
    p_externalid NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_mimetype NVARCHAR(255),
    p_conversion LONGVARCHAR,
    p_conversionstrategy NVARCHAR(255),
    p_inputformat BIGINT,
    p_mediaaddons LONGVARCHAR,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_qualifierIDX_51 ON junit_mediaformat (p_qualifier);


CREATE CACHED TABLE junit_mediaformatlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_mediaformatmapping
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_source BIGINT,
    p_target BIGINT,
    p_mediacontext BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_mediacontextRelIDX_53 ON junit_mediaformatmapping (p_mediacontext);


CREATE CACHED TABLE junit_mediametadata
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_value LONGVARCHAR,
    p_groupname NVARCHAR(255),
    p_media BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_mmetadata_uidx_400 ON junit_mediametadata (p_media, p_groupname, p_code);

CREATE INDEX junit_mediaRelIDX_400 ON junit_mediametadata (p_media);


CREATE CACHED TABLE junit_mediaprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255),
    LANGPK BIGINT,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX junit_nameidx_mediaprops ON junit_mediaprops (NAME);

CREATE INDEX junit_realnameidx_mediaprops ON junit_mediaprops (REALNAME);

CREATE INDEX junit_itempk_mediaprops ON junit_mediaprops (ITEMPK);


CREATE CACHED TABLE junit_medias
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_mime NVARCHAR(255),
    p_size BIGINT,
    p_datapk BIGINT,
    p_location LONGVARCHAR,
    p_locationhash NVARCHAR(255),
    p_realfilename NVARCHAR(255),
    p_code NVARCHAR(255),
    p_internalurl LONGVARCHAR,
    p_description NVARCHAR(255),
    p_alttext NVARCHAR(255),
    p_removable TINYINT,
    p_mediaformat BIGINT,
    p_folder BIGINT,
    p_subfolderpath NVARCHAR(255),
    p_mediacontainer BIGINT,
    p_catalog BIGINT,
    p_catalogversion BIGINT,
    p_metadatadatapk BIGINT,
    p_originaldatapk BIGINT,
    p_original BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_outputmimetype NVARCHAR(255),
    p_inputmimetype NVARCHAR(255),
    p_itemtimestamp TIMESTAMP,
    p_format BIGINT,
    p_sourceitem BIGINT,
    p_fieldseparator SMALLINT,
    p_quotecharacter SMALLINT,
    p_commentcharacter SMALLINT,
    p_encoding BIGINT,
    p_linestoskip INTEGER,
    p_removeonsuccess TINYINT,
    p_zipentry NVARCHAR(255),
    p_allowscriptevaluation TINYINT,
    p_scheduledcount INTEGER,
    p_cronjobpos INTEGER,
    p_cronjob BIGINT,
    p_icon BIGINT,
    p_compiledreport BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_dataPK_idx_30 ON junit_medias (p_datapk);

CREATE INDEX junit_Media_Code_30 ON junit_medias (p_code);

CREATE INDEX junit_versionIDX_30 ON junit_medias (p_catalogversion);

CREATE INDEX junit_codeVersionIDX_30 ON junit_medias (p_code, p_catalogversion);

CREATE INDEX junit_containerformat_idx_30 ON junit_medias (p_mediacontainer, p_mediaformat);

CREATE INDEX junit_parentformat_idx_30 ON junit_medias (p_original, p_mediaformat);

CREATE INDEX junit_mediacontainerRelIDX_30 ON junit_medias (p_mediacontainer);

CREATE INDEX junit_sourceitemRelIDX_30 ON junit_medias (p_sourceitem);

CREATE INDEX junit_cronjobRelIDX_30 ON junit_medias (p_cronjob);

CREATE INDEX junit_cronjobposPosIDX_30 ON junit_medias (p_cronjobpos);

CREATE INDEX junit_originalRelIDX_30 ON junit_medias (p_original);


CREATE CACHED TABLE junit_mediaslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    p_reportdescription NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_metainformations
(
    hjmpTS BIGINT,
    PK BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    aCLTS BIGINT DEFAULT 0,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    propTS BIGINT DEFAULT 0,
    SystemPK NVARCHAR(255),
    SystemName NVARCHAR(255),
    isInitialized TINYINT DEFAULT 0,
    LicenceID NVARCHAR(255),
    LicenceName NVARCHAR(255),
    LicenceEdition NVARCHAR(255),
    AdminFactor INTEGER DEFAULT 0,
    LicenceExpirationDate TIMESTAMP,
    LicenceSignature NVARCHAR(255),
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_numberseries
(
    hjmpTS BIGINT,
    serieskey NVARCHAR(255),
    seriestype INTEGER DEFAULT 0,
    currentValue BIGINT,
    template NVARCHAR(255),
    PRIMARY KEY (serieskey)
);


CREATE CACHED TABLE junit_orderdiscrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_202 ON junit_orderdiscrels (SequenceNumber);

CREATE INDEX junit_rseqnr_202 ON junit_orderdiscrels (RSequenceNumber);

CREATE INDEX junit_linksource_202 ON junit_orderdiscrels (SourcePK);

CREATE INDEX junit_linktarget_202 ON junit_orderdiscrels (TargetPK);

CREATE INDEX junit_qualifier_202 ON junit_orderdiscrels (Qualifier);


CREATE CACHED TABLE junit_orderentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_baseprice DECIMAL(30,8),
    p_calculated TINYINT,
    p_discountvaluesinternal LONGVARCHAR,
    p_entrynumber INTEGER,
    p_info NVARCHAR(255),
    p_product BIGINT,
    p_quantity DECIMAL(30,8),
    p_taxvaluesinternal NVARCHAR(255),
    p_totalprice DECIMAL(30,8),
    p_unit BIGINT,
    p_giveaway TINYINT,
    p_rejected TINYINT,
    p_order BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_oeProd_46 ON junit_orderentries (p_product);

CREATE INDEX junit_oeOrd_46 ON junit_orderentries (p_order);


CREATE CACHED TABLE junit_orderentryprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255),
    LANGPK BIGINT,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX junit_nameidx_orderentryprops ON junit_orderentryprops (NAME);

CREATE INDEX junit_itempk_orderentryprops ON junit_orderentryprops (ITEMPK);

CREATE INDEX junit_realnameidx_orderentryprops ON junit_orderentryprops (REALNAME);


CREATE CACHED TABLE junit_orderprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255),
    LANGPK BIGINT,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX junit_nameidx_orderprops ON junit_orderprops (NAME);

CREATE INDEX junit_realnameidx_orderprops ON junit_orderprops (REALNAME);

CREATE INDEX junit_itempk_orderprops ON junit_orderprops (ITEMPK);


CREATE CACHED TABLE junit_orders
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_calculated TINYINT,
    p_code NVARCHAR(255),
    p_currency BIGINT,
    p_deliveryaddress BIGINT,
    p_deliverycost DECIMAL(30,8),
    p_deliverymode BIGINT,
    p_deliverystatus BIGINT,
    p_globaldiscountvaluesinternal LONGVARCHAR,
    p_net TINYINT,
    p_paymentaddress BIGINT,
    p_paymentcost DECIMAL(30,8),
    p_paymentinfo BIGINT,
    p_paymentmode BIGINT,
    p_paymentstatus BIGINT,
    p_status BIGINT,
    p_exportstatus BIGINT,
    p_statusinfo NVARCHAR(255),
    p_totalprice DECIMAL(30,8),
    p_totaldiscounts DECIMAL(30,8),
    p_totaltax DECIMAL(30,8),
    p_totaltaxvaluesinternal LONGVARCHAR,
    p_user BIGINT,
    p_subtotal DECIMAL(30,8),
    p_discountsincludedeliverycost TINYINT,
    p_discountsincludepaymentcost TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_OrderCode_45 ON junit_orders (p_code);

CREATE INDEX junit_OrderUser_45 ON junit_orders (p_user);


CREATE CACHED TABLE junit_parserproperty
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_name NVARCHAR(255),
    p_startexp NVARCHAR(255),
    p_endexp NVARCHAR(255),
    p_parserclass NVARCHAR(255),
    p_translatorconfigurationpos INTEGER,
    p_translatorconfiguration BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_translatorconfigurationRelIDX_13213 ON junit_parserproperty (p_translatorconfiguration);

CREATE INDEX junit_translatorconfigurationposPosIDX_13213 ON junit_parserproperty (p_translatorconfigurationpos);


CREATE CACHED TABLE junit_paymentinfos
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_original BIGINT,
    p_code NVARCHAR(255),
    p_user BIGINT,
    p_duplicate TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_ccowner NVARCHAR(255),
    p_number NVARCHAR(255),
    p_type BIGINT,
    p_validtomonth NVARCHAR(255),
    p_validtoyear NVARCHAR(255),
    p_validfrommonth NVARCHAR(255),
    p_validfromyear NVARCHAR(255),
    p_bankidnumber NVARCHAR(255),
    p_bank NVARCHAR(255),
    p_accountnumber NVARCHAR(255),
    p_baowner NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX junit_PaymentInfo_User_42 ON junit_paymentinfos (p_user);


CREATE CACHED TABLE junit_paymentmodes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_active TINYINT,
    p_code NVARCHAR(255),
    p_paymentinfotype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_net TINYINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_paymentmodeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_pcp2wrtblecvrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_617 ON junit_pcp2wrtblecvrel (SequenceNumber);

CREATE INDEX junit_rseqnr_617 ON junit_pcp2wrtblecvrel (RSequenceNumber);

CREATE INDEX junit_linksource_617 ON junit_pcp2wrtblecvrel (SourcePK);

CREATE INDEX junit_linktarget_617 ON junit_pcp2wrtblecvrel (TargetPK);

CREATE INDEX junit_qualifier_617 ON junit_pcp2wrtblecvrel (Qualifier);


CREATE CACHED TABLE junit_pcpl2rdblecvrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_618 ON junit_pcpl2rdblecvrel (SequenceNumber);

CREATE INDEX junit_rseqnr_618 ON junit_pcpl2rdblecvrel (RSequenceNumber);

CREATE INDEX junit_linksource_618 ON junit_pcpl2rdblecvrel (SourcePK);

CREATE INDEX junit_linktarget_618 ON junit_pcpl2rdblecvrel (TargetPK);

CREATE INDEX junit_qualifier_618 ON junit_pcpl2rdblecvrel (Qualifier);


CREATE CACHED TABLE junit_pendingstepsrelation
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_507 ON junit_pendingstepsrelation (SequenceNumber);

CREATE INDEX junit_rseqnr_507 ON junit_pendingstepsrelation (RSequenceNumber);

CREATE INDEX junit_linksource_507 ON junit_pendingstepsrelation (SourcePK);

CREATE INDEX junit_linktarget_507 ON junit_pendingstepsrelation (TargetPK);

CREATE INDEX junit_qualifier_507 ON junit_pendingstepsrelation (Qualifier);


CREATE CACHED TABLE junit_pgrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_201 ON junit_pgrels (SequenceNumber);

CREATE INDEX junit_rseqnr_201 ON junit_pgrels (RSequenceNumber);

CREATE INDEX junit_linksource_201 ON junit_pgrels (SourcePK);

CREATE INDEX junit_linktarget_201 ON junit_pgrels (TargetPK);

CREATE INDEX junit_qualifier_201 ON junit_pgrels (Qualifier);


CREATE CACHED TABLE junit_previewtickets
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_previewcatalogversion BIGINT,
    p_validto TIMESTAMP,
    p_createdby BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_pricerows
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_product BIGINT,
    p_pg BIGINT,
    p_productmatchqualifier BIGINT,
    p_starttime TIMESTAMP,
    p_endtime TIMESTAMP,
    p_user BIGINT,
    p_ug BIGINT,
    p_usermatchqualifier BIGINT,
    p_productid NVARCHAR(255),
    p_catalogversion BIGINT,
    p_matchvalue INTEGER,
    p_currency BIGINT,
    p_minqtd BIGINT,
    p_net TINYINT,
    p_price DOUBLE,
    p_unit BIGINT,
    p_unitfactor INTEGER,
    p_giveawayprice TINYINT,
    p_channel BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_MatchIndexP_1055 ON junit_pricerows (p_productmatchqualifier);

CREATE INDEX junit_MatchIndexU_1055 ON junit_pricerows (p_usermatchqualifier);

CREATE INDEX junit_PIdx_1055 ON junit_pricerows (p_product);

CREATE INDEX junit_UIdx_1055 ON junit_pricerows (p_user);

CREATE INDEX junit_PGIdx_1055 ON junit_pricerows (p_pg);

CREATE INDEX junit_UGIdx_1055 ON junit_pricerows (p_ug);

CREATE INDEX junit_ProductIdIdx_1055 ON junit_pricerows (p_productid);

CREATE INDEX junit_versionIDX_1055 ON junit_pricerows (p_catalogversion);


CREATE CACHED TABLE junit_principcockpitreadrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1714 ON junit_principcockpitreadrels (SequenceNumber);

CREATE INDEX junit_rseqnr_1714 ON junit_principcockpitreadrels (RSequenceNumber);

CREATE INDEX junit_linksource_1714 ON junit_principcockpitreadrels (SourcePK);

CREATE INDEX junit_linktarget_1714 ON junit_principcockpitreadrels (TargetPK);

CREATE INDEX junit_qualifier_1714 ON junit_principcockpitreadrels (Qualifier);


CREATE CACHED TABLE junit_principcockpitwriterels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1715 ON junit_principcockpitwriterels (SequenceNumber);

CREATE INDEX junit_rseqnr_1715 ON junit_principcockpitwriterels (RSequenceNumber);

CREATE INDEX junit_linksource_1715 ON junit_principcockpitwriterels (SourcePK);

CREATE INDEX junit_linktarget_1715 ON junit_principcockpitwriterels (TargetPK);

CREATE INDEX junit_qualifier_1715 ON junit_principcockpitwriterels (Qualifier);


CREATE CACHED TABLE junit_princtolinkrelations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_6001 ON junit_princtolinkrelations (SequenceNumber);

CREATE INDEX junit_rseqnr_6001 ON junit_princtolinkrelations (RSequenceNumber);

CREATE INDEX junit_linksource_6001 ON junit_princtolinkrelations (SourcePK);

CREATE INDEX junit_linktarget_6001 ON junit_princtolinkrelations (TargetPK);

CREATE INDEX junit_qualifier_6001 ON junit_princtolinkrelations (Qualifier);


CREATE CACHED TABLE junit_processedstepsrelation
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_506 ON junit_processedstepsrelation (SequenceNumber);

CREATE INDEX junit_rseqnr_506 ON junit_processedstepsrelation (RSequenceNumber);

CREATE INDEX junit_linksource_506 ON junit_processedstepsrelation (SourcePK);

CREATE INDEX junit_linktarget_506 ON junit_processedstepsrelation (TargetPK);

CREATE INDEX junit_qualifier_506 ON junit_processedstepsrelation (Qualifier);


CREATE CACHED TABLE junit_processes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_processdefinitionname NVARCHAR(255),
    p_processdefinitionversion NVARCHAR(255),
    p_state BIGINT,
    p_endmessage LONGVARCHAR,
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_ProcessengineProcess_name_idx_32766 ON junit_processes (p_code);


CREATE CACHED TABLE junit_processparameters
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_name NVARCHAR(255),
    p_value LONGVARBINARY,
    p_process BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_BusinessProcessParameter_idx_32764 ON junit_processparameters (p_process, p_name);

CREATE INDEX junit_processRelIDX_32764 ON junit_processparameters (p_process);


CREATE CACHED TABLE junit_prod2keywordrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_604 ON junit_prod2keywordrel (SequenceNumber);

CREATE INDEX junit_rseqnr_604 ON junit_prod2keywordrel (RSequenceNumber);

CREATE INDEX junit_linksource_604 ON junit_prod2keywordrel (SourcePK);

CREATE INDEX junit_linktarget_604 ON junit_prod2keywordrel (TargetPK);

CREATE INDEX junit_qualifier_604 ON junit_prod2keywordrel (Qualifier);


CREATE CACHED TABLE junit_productfeatures
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_product BIGINT,
    p_qualifier NVARCHAR(255),
    p_classificationattributeassig BIGINT,
    p_language BIGINT,
    p_valueposition INTEGER,
    p_featureposition INTEGER,
    p_valuetype INTEGER,
    p_stringvalue LONGVARCHAR,
    p_booleanvalue TINYINT,
    p_numbervalue DECIMAL(30,8),
    p_rawvalue LONGVARBINARY,
    p_unit BIGINT,
    p_valuedetails NVARCHAR(255),
    p_description NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_featureIDX_611 ON junit_productfeatures (p_product);

CREATE INDEX junit_featureIDX2_611 ON junit_productfeatures (p_qualifier);

CREATE INDEX junit_featureIDX3_611 ON junit_productfeatures (p_classificationattributeassig);


CREATE CACHED TABLE junit_productprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255),
    LANGPK BIGINT,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX junit_realnameidx_productprops ON junit_productprops (REALNAME);

CREATE INDEX junit_itempk_productprops ON junit_productprops (ITEMPK);

CREATE INDEX junit_nameidx_productprops ON junit_productprops (NAME);


CREATE CACHED TABLE junit_productreferences
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_qualifier NVARCHAR(255),
    p_target BIGINT,
    p_quantity INTEGER,
    p_referencetype BIGINT,
    p_icon BIGINT,
    p_preselected TINYINT,
    p_active TINYINT,
    p_sourcepos INTEGER,
    p_source BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_targetIDX_606 ON junit_productreferences (p_target);

CREATE INDEX junit_qualifierIDX_606 ON junit_productreferences (p_qualifier);

CREATE INDEX junit_sourceRelIDX_606 ON junit_productreferences (p_source);

CREATE INDEX junit_sourceposPosIDX_606 ON junit_productreferences (p_sourcepos);


CREATE CACHED TABLE junit_productreferenceslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_products
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_unit BIGINT,
    p_thumbnail BIGINT,
    p_picture BIGINT,
    p_catalog BIGINT,
    p_catalogversion BIGINT,
    p_onlinedate TIMESTAMP,
    p_offlinedate TIMESTAMP,
    p_ean NVARCHAR(255),
    p_supplieralternativeaid NVARCHAR(255),
    p_buyerids LONGVARBINARY,
    p_manufactureraid NVARCHAR(255),
    p_manufacturername NVARCHAR(255),
    p_erpgroupbuyer NVARCHAR(255),
    p_erpgroupsupplier NVARCHAR(255),
    p_deliverytime DOUBLE,
    p_specialtreatmentclasses LONGVARBINARY,
    p_order INTEGER,
    p_approvalstatus BIGINT,
    p_contentunit BIGINT,
    p_numbercontentunits DOUBLE,
    p_minorderquantity INTEGER,
    p_maxorderquantity INTEGER,
    p_orderquantityinterval INTEGER,
    p_pricequantity DOUBLE,
    p_normal LONGVARCHAR,
    p_thumbnails LONGVARCHAR,
    p_detail LONGVARCHAR,
    p_logo LONGVARCHAR,
    p_data_sheet LONGVARCHAR,
    p_others LONGVARCHAR,
    p_startlinenumber INTEGER,
    p_endlinenumber INTEGER,
    p_varianttype BIGINT,
    p_europe1pricefactory_ppg BIGINT,
    p_europe1pricefactory_ptg BIGINT,
    p_europe1pricefactory_pdg BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_baseproduct BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_Product_Code_1 ON junit_products (p_code);

CREATE INDEX junit_catalogIDX_1 ON junit_products (p_catalog);

CREATE INDEX junit_visibilityIDX_1 ON junit_products (p_approvalstatus, p_onlinedate, p_offlinedate);

CREATE INDEX junit_codeVersionIDX_1 ON junit_products (p_code, p_catalogversion);

CREATE INDEX junit_versionIDX_1 ON junit_products (p_catalogversion);

CREATE INDEX junit_baseIDX_1 ON junit_products (p_baseproduct);


CREATE CACHED TABLE junit_productslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    p_manufacturertypedescription NVARCHAR(255),
    p_segment NVARCHAR(255),
    p_articlestatus LONGVARBINARY,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_props
(
    hjmpTS BIGINT,
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255),
    LANGPK BIGINT,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX junit_nameidx_props ON junit_props (NAME);

CREATE INDEX junit_itempk_props ON junit_props (ITEMPK);

CREATE INDEX junit_realnameidx_props ON junit_props (REALNAME);


CREATE CACHED TABLE junit_readcockpitcollrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1710 ON junit_readcockpitcollrels (SequenceNumber);

CREATE INDEX junit_rseqnr_1710 ON junit_readcockpitcollrels (RSequenceNumber);

CREATE INDEX junit_linksource_1710 ON junit_readcockpitcollrels (SourcePK);

CREATE INDEX junit_linktarget_1710 ON junit_readcockpitcollrels (TargetPK);

CREATE INDEX junit_qualifier_1710 ON junit_readcockpitcollrels (Qualifier);


CREATE CACHED TABLE junit_readsavedqueryrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1717 ON junit_readsavedqueryrels (SequenceNumber);

CREATE INDEX junit_rseqnr_1717 ON junit_readsavedqueryrels (RSequenceNumber);

CREATE INDEX junit_linksource_1717 ON junit_readsavedqueryrels (SourcePK);

CREATE INDEX junit_linktarget_1717 ON junit_readsavedqueryrels (TargetPK);

CREATE INDEX junit_qualifier_1717 ON junit_readsavedqueryrels (Qualifier);


CREATE CACHED TABLE junit_regions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_active TINYINT,
    p_isocode NVARCHAR(255),
    p_country BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_ISOCode_35 ON junit_regions (p_isocode);

CREATE INDEX junit_Region_Country_35 ON junit_regions (p_country);


CREATE CACHED TABLE junit_regionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_renderersproperty
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_key NVARCHAR(255),
    p_value NVARCHAR(255),
    p_translatorconfigurationpos INTEGER,
    p_translatorconfiguration BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_translatorconfigurationRelIDX_13212 ON junit_renderersproperty (p_translatorconfiguration);

CREATE INDEX junit_translatorconfigurationposPosIDX_13212 ON junit_renderersproperty (p_translatorconfigurationpos);


CREATE CACHED TABLE junit_renderertemplate
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_contextclass NVARCHAR(255),
    p_outputmimetype NVARCHAR(255),
    p_renderertype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_renderertemplatelp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    p_content BIGINT,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_savedqueries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_paramtypes LONGVARCHAR,
    p_params LONGVARBINARY,
    p_query LONGVARCHAR,
    p_resulttype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_savedquerieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_savedvalueentry
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_modifiedattribute NVARCHAR(255),
    p_oldvalueattributedescriptor BIGINT,
    p_parent BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_parentRelIDX_335 ON junit_savedvalueentry (p_parent);


CREATE CACHED TABLE junit_savedvalues
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_modifieditemtype BIGINT,
    p_timestamp TIMESTAMP,
    p_user BIGINT,
    p_modificationtype BIGINT,
    p_modifieditempos INTEGER,
    p_modifieditem BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_savedvalmoditem_334 ON junit_savedvalues (p_modifieditem);

CREATE INDEX junit_modifieditemposPosIDX_334 ON junit_savedvalues (p_modifieditempos);


CREATE CACHED TABLE junit_scripts
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_active TINYINT,
    p_checksum NVARCHAR(255),
    p_content LONGVARCHAR,
    p_version BIGINT,
    p_scripttype BIGINT,
    p_autodisabling TINYINT,
    p_disabled TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_codeVersionActiveIDX_100 ON junit_scripts (p_code, p_version, p_active);


CREATE CACHED TABLE junit_scriptslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_searchrestrictions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    p_code NVARCHAR(255),
    p_active TINYINT,
    principal BIGINT,
    query LONGVARCHAR,
    RestrictedType BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_principal_90 ON junit_searchrestrictions (principal);

CREATE INDEX junit_restrtype_90 ON junit_searchrestrictions (RestrictedType);


CREATE CACHED TABLE junit_searchrestrictionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_slactions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_type BIGINT,
    p_target NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_stdpaymmodevals
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_currency BIGINT,
    p_value DOUBLE,
    p_paymentmode BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_paymentmodeRelIDX_1022 ON junit_stdpaymmodevals (p_paymentmode);


CREATE CACHED TABLE junit_steps
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_batchjob BIGINT,
    p_code NVARCHAR(255),
    p_sequencenumber INTEGER,
    p_synchronous TINYINT,
    p_errormode BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_IdxBatchJob_503 ON junit_steps (p_batchjob);

CREATE INDEX junit_seqNrIDX_503 ON junit_steps (p_sequencenumber);


CREATE CACHED TABLE junit_synattcfg
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_syncjob BIGINT,
    p_attributedescriptor BIGINT,
    p_includedinsync TINYINT,
    p_copybyvalue TINYINT,
    p_untranslatable TINYINT,
    p_translatevalue TINYINT,
    p_presetvalue LONGVARBINARY,
    p_partiallytranslatable TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_jobIdx_620 ON junit_synattcfg (p_syncjob);

CREATE INDEX junit_attrIdx_620 ON junit_synattcfg (p_attributedescriptor);


CREATE CACHED TABLE junit_syncjob2langrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_622 ON junit_syncjob2langrel (SequenceNumber);

CREATE INDEX junit_rseqnr_622 ON junit_syncjob2langrel (RSequenceNumber);

CREATE INDEX junit_linksource_622 ON junit_syncjob2langrel (SourcePK);

CREATE INDEX junit_linktarget_622 ON junit_syncjob2langrel (TargetPK);

CREATE INDEX junit_qualifier_622 ON junit_syncjob2langrel (Qualifier);


CREATE CACHED TABLE junit_syncjob2pcplrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_623 ON junit_syncjob2pcplrel (SequenceNumber);

CREATE INDEX junit_rseqnr_623 ON junit_syncjob2pcplrel (RSequenceNumber);

CREATE INDEX junit_linksource_623 ON junit_syncjob2pcplrel (SourcePK);

CREATE INDEX junit_linktarget_623 ON junit_syncjob2pcplrel (TargetPK);

CREATE INDEX junit_qualifier_623 ON junit_syncjob2pcplrel (Qualifier);


CREATE CACHED TABLE junit_syncjob2typerel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_621 ON junit_syncjob2typerel (SequenceNumber);

CREATE INDEX junit_rseqnr_621 ON junit_syncjob2typerel (RSequenceNumber);

CREATE INDEX junit_linksource_621 ON junit_syncjob2typerel (SourcePK);

CREATE INDEX junit_linktarget_621 ON junit_syncjob2typerel (TargetPK);

CREATE INDEX junit_qualifier_621 ON junit_syncjob2typerel (Qualifier);


CREATE CACHED TABLE junit_taskconditions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_uniqueid NVARCHAR(255),
    p_expirationtimemillis BIGINT,
    p_processeddate TIMESTAMP,
    p_fulfilled TINYINT,
    p_consumed TINYINT,
    p_choice NVARCHAR(255),
    p_task BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_Cond_idx_951 ON junit_taskconditions (p_uniqueid, p_consumed);

CREATE INDEX junit_Cond_match_idx_951 ON junit_taskconditions (p_task, p_fulfilled);

CREATE INDEX junit_taskRelIDX_951 ON junit_taskconditions (p_task);


CREATE CACHED TABLE junit_tasklogs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_returncode NVARCHAR(255),
    p_startdate TIMESTAMP,
    p_enddate TIMESTAMP,
    p_actionid NVARCHAR(255),
    p_clusterid INTEGER,
    p_logmessages LONGVARCHAR,
    p_process BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_processRelIDX_32767 ON junit_tasklogs (p_process);


CREATE CACHED TABLE junit_tasks
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_runnerbean NVARCHAR(255),
    p_executiontimemillis BIGINT,
    p_failed TINYINT,
    p_expirationtimemillis BIGINT,
    p_context LONGVARBINARY,
    p_contextitem BIGINT,
    p_nodeid INTEGER,
    p_nodegroup NVARCHAR(255),
    p_retry INTEGER,
    p_runningonclusternode INTEGER,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_action NVARCHAR(255),
    p_process BIGINT,
    p_scripturi NVARCHAR(255),
    p_trigger BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_Task_dr_idx_950 ON junit_tasks (p_runningonclusternode, p_expirationtimemillis, p_nodeid);

CREATE INDEX junit_processRelIDX_950 ON junit_tasks (p_process);


CREATE CACHED TABLE junit_taxes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_value DOUBLE,
    p_currency BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_taxeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_taxrows
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_product BIGINT,
    p_pg BIGINT,
    p_productmatchqualifier BIGINT,
    p_starttime TIMESTAMP,
    p_endtime TIMESTAMP,
    p_user BIGINT,
    p_ug BIGINT,
    p_usermatchqualifier BIGINT,
    p_productid NVARCHAR(255),
    p_catalogversion BIGINT,
    p_currency BIGINT,
    p_tax BIGINT,
    p_value DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_MatchIndexP_1054 ON junit_taxrows (p_productmatchqualifier);

CREATE INDEX junit_MatchIndexU_1054 ON junit_taxrows (p_usermatchqualifier);

CREATE INDEX junit_PIdx_1054 ON junit_taxrows (p_product);

CREATE INDEX junit_UIdx_1054 ON junit_taxrows (p_user);

CREATE INDEX junit_PGIdx_1054 ON junit_taxrows (p_pg);

CREATE INDEX junit_UGIdx_1054 ON junit_taxrows (p_ug);

CREATE INDEX junit_ProductIdIdx_1054 ON junit_taxrows (p_productid);

CREATE INDEX junit_versionIDX_1054 ON junit_taxrows (p_catalogversion);


CREATE CACHED TABLE junit_testitem
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    fieldA NVARCHAR(255),
    fieldB NVARCHAR(255),
    fieldBoolean TINYINT,
    fieldByte SMALLINT,
    fieldCharacter SMALLINT,
    fieldDate TIMESTAMP,
    fieldDouble DOUBLE,
    fieldFloat FLOAT,
    fieldInteger INTEGER,
    fieldLong BIGINT,
    fieldPrimitiveBoolean TINYINT DEFAULT 0,
    fieldPrimitiveByte SMALLINT DEFAULT 0,
    fieldPrimitiveChar SMALLINT,
    fieldPrimitiveDouble DOUBLE DEFAULT 0,
    fieldPrimitiveFloat FLOAT DEFAULT 0,
    fieldPrimitiveInteger INTEGER DEFAULT 0,
    fieldPrimitiveLong BIGINT DEFAULT 0,
    fieldPrimitiveShort SMALLINT,
    fieldSerializable LONGVARBINARY,
    fieldString NVARCHAR(255),
    fieldLongString LONGVARCHAR,
    p_testproperty0 NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    fieldBigDecimal DECIMAL(30,8),
    p_testproperty1 INTEGER,
    p_foo NVARCHAR(255),
    p_bar NVARCHAR(255),
    p_xxx NVARCHAR(255),
    p_itemtypetwo BIGINT,
    p_itemstypetwo LONGVARCHAR,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_testitemlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_testproperty2 NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_titles
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_titleslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_triggerscj
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_active TINYINT,
    p_second INTEGER,
    p_minute INTEGER,
    p_hour INTEGER,
    p_day INTEGER,
    p_month INTEGER,
    p_year INTEGER,
    p_relative TINYINT,
    p_daysofweek LONGVARCHAR,
    p_weekinterval INTEGER,
    p_daterange LONGVARBINARY,
    p_activationtime TIMESTAMP,
    p_cronexpression NVARCHAR(255),
    p_maxacceptabledelay INTEGER,
    p_job BIGINT,
    p_cronjob BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_IdxCronJob_502 ON junit_triggerscj (p_cronjob);

CREATE INDEX junit_IdxActive_502 ON junit_triggerscj (p_active);

CREATE INDEX junit_jobRelIDX_502 ON junit_triggerscj (p_job);


CREATE CACHED TABLE junit_typesystemprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255),
    LANGPK BIGINT,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX junit_nameidx_typesystemprops ON junit_typesystemprops (NAME);

CREATE INDEX junit_itempk_typesystemprops ON junit_typesystemprops (ITEMPK);

CREATE INDEX junit_realnameidx_typesystemprops ON junit_typesystemprops (REALNAME);


CREATE CACHED TABLE junit_units
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_conversion DOUBLE,
    p_unittype NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_unitslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_usergroupprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255),
    LANGPK BIGINT,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX junit_itempk_usergroupprops ON junit_usergroupprops (ITEMPK);

CREATE INDEX junit_nameidx_usergroupprops ON junit_usergroupprops (NAME);

CREATE INDEX junit_realnameidx_usergroupprops ON junit_usergroupprops (REALNAME);


CREATE CACHED TABLE junit_usergroups
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_description NVARCHAR(255),
    p_name NVARCHAR(255),
    p_uid NVARCHAR(255),
    p_profilepicture BIGINT,
    p_ldapsearchbase NVARCHAR(255),
    p_dn LONGVARCHAR,
    p_cn NVARCHAR(255),
    p_backofficelogindisabled TINYINT,
    p_writeablelanguages LONGVARCHAR,
    p_readablelanguages LONGVARCHAR,
    p_userdiscountgroup BIGINT,
    p_userpricegroup BIGINT,
    p_usertaxgroup BIGINT,
    p_hmclogindisabled TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_dunsid NVARCHAR(255),
    p_ilnid NVARCHAR(255),
    p_buyerspecificid NVARCHAR(255),
    p_id NVARCHAR(255),
    p_supplierspecificid NVARCHAR(255),
    p_medias LONGVARCHAR,
    p_shippingaddress BIGINT,
    p_unloadingaddress BIGINT,
    p_billingaddress BIGINT,
    p_contactaddress BIGINT,
    p_contact BIGINT,
    p_vatid NVARCHAR(255),
    p_responsiblecompany BIGINT,
    p_country BIGINT,
    p_lineofbuisness BIGINT,
    p_buyer TINYINT,
    p_supplier TINYINT,
    p_manufacturer TINYINT,
    p_carrier TINYINT,
    p_authorities LONGVARBINARY,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_UID_5 ON junit_usergroups (p_uid);


CREATE CACHED TABLE junit_usergroupslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_locname NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_userprofiles
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_readablelanguages LONGVARCHAR,
    p_writablelanguages LONGVARCHAR,
    p_expandinitial TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_userprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255),
    LANGPK BIGINT,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX junit_nameidx_userprops ON junit_userprops (NAME);

CREATE INDEX junit_itempk_userprops ON junit_userprops (ITEMPK);

CREATE INDEX junit_realnameidx_userprops ON junit_userprops (REALNAME);


CREATE CACHED TABLE junit_userrights
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE junit_userrightslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_users
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_description NVARCHAR(255),
    p_name NVARCHAR(255),
    p_uid NVARCHAR(255),
    p_profilepicture BIGINT,
    p_ldapsearchbase NVARCHAR(255),
    p_dn LONGVARCHAR,
    p_cn NVARCHAR(255),
    p_backofficelogindisabled TINYINT,
    p_defaultpaymentaddress BIGINT,
    p_defaultshipmentaddress BIGINT,
    p_passwordencoding NVARCHAR(255),
    passwd LONGVARCHAR,
    p_passwordanswer LONGVARCHAR,
    p_passwordquestion LONGVARCHAR,
    p_sessionlanguage BIGINT,
    p_sessioncurrency BIGINT,
    p_logindisabled TINYINT,
    p_lastlogin TIMESTAMP,
    p_hmclogindisabled TINYINT,
    p_userprofile BIGINT,
    p_europe1pricefactory_udg BIGINT,
    p_europe1pricefactory_upg BIGINT,
    p_europe1pricefactory_utg BIGINT,
    p_ldapaccount TINYINT,
    p_domain NVARCHAR(255),
    p_ldaplogin NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_customerid NVARCHAR(255),
    p_previewcatalogversions LONGVARCHAR,
    p_email NVARCHAR(255),
    p_confirmed TINYINT,
    p_preferences BIGINT,
    p_sendnewsletter TINYINT,
    p_country BIGINT,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_UID_4 ON junit_users (p_uid);

CREATE INDEX junit_countryRelIDX_4 ON junit_users (p_country);


CREATE CACHED TABLE junit_validationconstraints
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_id NVARCHAR(255),
    p_active TINYINT,
    p_annotation LONGVARCHAR,
    p_severity BIGINT,
    p_target LONGVARCHAR,
    p_type BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_language BIGINT,
    p_expression LONGVARCHAR,
    p_firstfieldname NVARCHAR(255),
    p_secondfieldname NVARCHAR(255),
    p_qualifier NVARCHAR(255),
    p_languages LONGVARCHAR,
    p_descriptor BIGINT,
    p_regexp NVARCHAR(255),
    p_flags LONGVARCHAR,
    p_integer INTEGER,
    p_fraction INTEGER,
    p_value BIGINT,
    p_valu0 DECIMAL(30,8),
    p_min BIGINT,
    p_max BIGINT,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_AbstractConstraint_idx_980 ON junit_validationconstraints (p_id);

CREATE INDEX junit_typeRelIDX_980 ON junit_validationconstraints (p_type);

CREATE INDEX junit_descriptorRelIDX_980 ON junit_validationconstraints (p_descriptor);


CREATE CACHED TABLE junit_validationconstraintslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_message LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_whereparts
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_and TINYINT,
    p_replacepattern NVARCHAR(255),
    p_savedquery BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_savedqueryRelIDX_1300 ON junit_whereparts (p_savedquery);


CREATE CACHED TABLE junit_widgetparameter
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_name NVARCHAR(255),
    p_type BIGINT,
    p_description NVARCHAR(255),
    p_defaultvalueexpression NVARCHAR(255),
    p_targettype NVARCHAR(255),
    p_value LONGVARBINARY,
    p_widgetpreferences BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_widgetpreferencesRelIDX_2072 ON junit_widgetparameter (p_widgetpreferences);


CREATE CACHED TABLE junit_widgetpreferences
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_owneruser BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_report BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_owneruserRelIDX_2071 ON junit_widgetpreferences (p_owneruser);


CREATE CACHED TABLE junit_widgetpreferenceslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_workflowactioncomments
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_comment NVARCHAR(255),
    p_user BIGINT,
    p_workflowaction BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_workflowactionRelIDX_1118 ON junit_workflowactioncomments (p_workflowaction);


CREATE CACHED TABLE junit_workflowactionitemsrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1116 ON junit_workflowactionitemsrel (SequenceNumber);

CREATE INDEX junit_rseqnr_1116 ON junit_workflowactionitemsrel (RSequenceNumber);

CREATE INDEX junit_linksource_1116 ON junit_workflowactionitemsrel (SourcePK);

CREATE INDEX junit_linktarget_1116 ON junit_workflowactionitemsrel (TargetPK);

CREATE INDEX junit_qualifier_1116 ON junit_workflowactionitemsrel (Qualifier);


CREATE CACHED TABLE junit_workflowactionlinkrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    p_active TINYINT,
    p_andconnection TINYINT,
    p_template BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1124 ON junit_workflowactionlinkrel (SequenceNumber);

CREATE INDEX junit_rseqnr_1124 ON junit_workflowactionlinkrel (RSequenceNumber);

CREATE INDEX junit_linksource_1124 ON junit_workflowactionlinkrel (SourcePK);

CREATE INDEX junit_linktarget_1124 ON junit_workflowactionlinkrel (TargetPK);

CREATE INDEX junit_qualifier_1124 ON junit_workflowactionlinkrel (Qualifier);


CREATE CACHED TABLE junit_workflowactions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_actiontype BIGINT,
    p_code NVARCHAR(255),
    p_principalassigned BIGINT,
    p_sendemail TINYINT,
    p_emailaddress NVARCHAR(255),
    p_renderertemplate BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_workflowpos INTEGER,
    p_workflow BIGINT,
    p_jobclass LONGVARCHAR,
    p_jobhandler NVARCHAR(255),
    p_selecteddecision BIGINT,
    p_firstactivated TIMESTAMP,
    p_activated TIMESTAMP,
    p_comment NVARCHAR(255),
    p_status BIGINT,
    p_template BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_codeIDX_1113 ON junit_workflowactions (p_code);

CREATE INDEX junit_workflowRelIDX_1113 ON junit_workflowactions (p_workflow);

CREATE INDEX junit_workflowposPosIDX_1113 ON junit_workflowactions (p_workflowpos);


CREATE CACHED TABLE junit_workflowactionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_workflowactionsrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1115 ON junit_workflowactionsrel (SequenceNumber);

CREATE INDEX junit_rseqnr_1115 ON junit_workflowactionsrel (RSequenceNumber);

CREATE INDEX junit_linksource_1115 ON junit_workflowactionsrel (SourcePK);

CREATE INDEX junit_linktarget_1115 ON junit_workflowactionsrel (TargetPK);

CREATE INDEX junit_qualifier_1115 ON junit_workflowactionsrel (Qualifier);


CREATE CACHED TABLE junit_workflowitematts
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    p_comment NVARCHAR(255),
    p_item BIGINT,
    p_typeofitem BIGINT,
    p_workflowpos INTEGER,
    p_workflow BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_codeIDX_1114 ON junit_workflowitematts (p_code);

CREATE INDEX junit_workflowRelIDX_1114 ON junit_workflowitematts (p_workflow);

CREATE INDEX junit_workflowposPosIDX_1114 ON junit_workflowitematts (p_workflowpos);


CREATE CACHED TABLE junit_workflowitemattslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE junit_workflowtemplatelinkrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    p_andconnectiontemplate TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1125 ON junit_workflowtemplatelinkrel (SequenceNumber);

CREATE INDEX junit_rseqnr_1125 ON junit_workflowtemplatelinkrel (RSequenceNumber);

CREATE INDEX junit_linksource_1125 ON junit_workflowtemplatelinkrel (SourcePK);

CREATE INDEX junit_linktarget_1125 ON junit_workflowtemplatelinkrel (TargetPK);

CREATE INDEX junit_qualifier_1125 ON junit_workflowtemplatelinkrel (Qualifier);


CREATE CACHED TABLE junit_workflowtemplprincrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1117 ON junit_workflowtemplprincrel (SequenceNumber);

CREATE INDEX junit_rseqnr_1117 ON junit_workflowtemplprincrel (RSequenceNumber);

CREATE INDEX junit_linksource_1117 ON junit_workflowtemplprincrel (SourcePK);

CREATE INDEX junit_linktarget_1117 ON junit_workflowtemplprincrel (TargetPK);

CREATE INDEX junit_qualifier_1117 ON junit_workflowtemplprincrel (Qualifier);


CREATE CACHED TABLE junit_writecockpitcollrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1711 ON junit_writecockpitcollrels (SequenceNumber);

CREATE INDEX junit_rseqnr_1711 ON junit_writecockpitcollrels (RSequenceNumber);

CREATE INDEX junit_linksource_1711 ON junit_writecockpitcollrels (SourcePK);

CREATE INDEX junit_linktarget_1711 ON junit_writecockpitcollrels (TargetPK);

CREATE INDEX junit_qualifier_1711 ON junit_writecockpitcollrels (Qualifier);


CREATE CACHED TABLE junit_ydeployments
(
    hjmpTS BIGINT,
    Typecode INTEGER DEFAULT 0,
    TableName NVARCHAR(255),
    PropsTableName NVARCHAR(255),
    Name NVARCHAR(255),
    PackageName NVARCHAR(255),
    SuperName NVARCHAR(255),
    ExtensionName NVARCHAR(255),
    Modifiers INTEGER DEFAULT 0,
    TypeSystemName NVARCHAR(255),
    PRIMARY KEY (Name, TypeSystemName)
);

CREATE INDEX junit_deplselect_ydeployments ON junit_ydeployments (ExtensionName);

CREATE INDEX junit_deplselect2_ydeployments ON junit_ydeployments (Typecode);

CREATE INDEX junit_tsnameidx_ydeployments ON junit_ydeployments (TypeSystemName);


CREATE CACHED TABLE junit_zone2country
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX junit_seqnr_1204 ON junit_zone2country (SequenceNumber);

CREATE INDEX junit_rseqnr_1204 ON junit_zone2country (RSequenceNumber);

CREATE INDEX junit_linksource_1204 ON junit_zone2country (SourcePK);

CREATE INDEX junit_linktarget_1204 ON junit_zone2country (TargetPK);

CREATE INDEX junit_qualifier_1204 ON junit_zone2country (Qualifier);


CREATE CACHED TABLE junit_zonedeliverymodevalues
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_currency BIGINT,
    p_minimum DOUBLE,
    p_value DOUBLE,
    p_zone BIGINT,
    p_deliverymode BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_IdentityIDX_1202 ON junit_zonedeliverymodevalues (p_deliverymode, p_zone, p_currency, p_minimum);

CREATE INDEX junit_ModeIDX_1202 ON junit_zonedeliverymodevalues (p_deliverymode);

CREATE INDEX junit_ZoneIDX_1202 ON junit_zonedeliverymodevalues (p_zone);


CREATE CACHED TABLE junit_zones
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX junit_IdentityIDX_1203 ON junit_zones (p_code);

