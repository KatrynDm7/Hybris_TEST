
CREATE CACHED TABLE abstractcontactinfo
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

CREATE INDEX userRelIDX_26 ON abstractcontactinfo (p_user);

CREATE INDEX userposPosIDX_26 ON abstractcontactinfo (p_userpos);


CREATE CACHED TABLE abstractlinkentries
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

CREATE INDEX code_6002 ON abstractlinkentries (p_code);

CREATE INDEX area_6002 ON abstractlinkentries (p_area);

CREATE INDEX parentlinkRelIDX_6002 ON abstractlinkentries (p_parentlink);

CREATE INDEX parentlinkposPosIDX_6002 ON abstractlinkentries (p_parentlinkpos);


CREATE CACHED TABLE abstractlinkentrieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE aclentries
(
    hjmpTS BIGINT,
    PermissionPK BIGINT,
    Negative TINYINT DEFAULT 0,
    PrincipalPK BIGINT,
    ItemPK BIGINT,
    PRIMARY KEY (PermissionPK, PrincipalPK, ItemPK)
);

CREATE INDEX aclupdateindex_aclentries ON aclentries (ItemPK);

CREATE INDEX aclcheckindex_aclentries ON aclentries (ItemPK, PrincipalPK);


CREATE CACHED TABLE addresses
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

CREATE INDEX testindex_23 ON addresses (p_email);

CREATE INDEX Address_Owner_23 ON addresses (OwnerPkString);


CREATE CACHED TABLE addressprops
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

CREATE INDEX itempk_addressprops ON addressprops (ITEMPK);

CREATE INDEX nameidx_addressprops ON addressprops (NAME);

CREATE INDEX realnameidx_addressprops ON addressprops (REALNAME);


CREATE CACHED TABLE agreements
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


CREATE CACHED TABLE atomictypes
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

CREATE INDEX typecode_81 ON atomictypes (InternalCode);

CREATE INDEX typecodelowercase_81 ON atomictypes (InternalCodeLowerCase);

CREATE INDEX inheritpsi_81 ON atomictypes (InheritancePathString);


CREATE CACHED TABLE atomictypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE attr2valuerel
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

CREATE INDEX sysVer_609 ON attr2valuerel (p_systemversion);

CREATE INDEX catRelIDX_609 ON attr2valuerel (p_attributeassignment);

CREATE INDEX idIDX_609 ON attr2valuerel (p_externalid);

CREATE INDEX valIDX_609 ON attr2valuerel (p_value);

CREATE INDEX attrIDX_609 ON attr2valuerel (p_attribute);


CREATE CACHED TABLE attributedescriptors
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

CREATE UNIQUE INDEX qualifier_87 ON attributedescriptors (QualifierInternal, EnclosingTypePK);

CREATE INDEX inheritps_87 ON attributedescriptors (InheritancePathString);

CREATE INDEX lcqualifier_87 ON attributedescriptors (QualifierLowerCaseInternal);

CREATE INDEX enclosing_87 ON attributedescriptors (EnclosingTypePK);


CREATE CACHED TABLE attributedescriptorslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cartentries
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

CREATE INDEX oeProd_44 ON cartentries (p_product);

CREATE INDEX oeOrd_44 ON cartentries (p_order);


CREATE CACHED TABLE carts
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

CREATE INDEX OrderCode_43 ON carts (p_code);

CREATE INDEX OrderUser_43 ON carts (p_user);


CREATE CACHED TABLE cat2attrrel
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

CREATE INDEX sysVer_610 ON cat2attrrel (p_systemversion);

CREATE INDEX relSrcIDX_610 ON cat2attrrel (p_classificationclass);

CREATE INDEX idIDX_610 ON cat2attrrel (p_externalid);

CREATE INDEX relTgtIDX_610 ON cat2attrrel (p_classificationattribute);

CREATE INDEX posIdx_610 ON cat2attrrel (p_position);


CREATE CACHED TABLE cat2attrrellp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cat2catrel
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

CREATE INDEX rseqnr_144 ON cat2catrel (RSequenceNumber);

CREATE INDEX linksource_144 ON cat2catrel (SourcePK);

CREATE INDEX qualifier_144 ON cat2catrel (Qualifier);

CREATE INDEX linktarget_144 ON cat2catrel (TargetPK);

CREATE INDEX seqnr_144 ON cat2catrel (SequenceNumber);


CREATE CACHED TABLE cat2keywordrel
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

CREATE INDEX rseqnr_605 ON cat2keywordrel (RSequenceNumber);

CREATE INDEX linksource_605 ON cat2keywordrel (SourcePK);

CREATE INDEX qualifier_605 ON cat2keywordrel (Qualifier);

CREATE INDEX linktarget_605 ON cat2keywordrel (TargetPK);

CREATE INDEX seqnr_605 ON cat2keywordrel (SequenceNumber);


CREATE CACHED TABLE cat2medrel
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

CREATE INDEX rseqnr_145 ON cat2medrel (RSequenceNumber);

CREATE INDEX linksource_145 ON cat2medrel (SourcePK);

CREATE INDEX qualifier_145 ON cat2medrel (Qualifier);

CREATE INDEX linktarget_145 ON cat2medrel (TargetPK);

CREATE INDEX seqnr_145 ON cat2medrel (SequenceNumber);


CREATE CACHED TABLE cat2princrel
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

CREATE INDEX rseqnr_613 ON cat2princrel (RSequenceNumber);

CREATE INDEX linksource_613 ON cat2princrel (SourcePK);

CREATE INDEX qualifier_613 ON cat2princrel (Qualifier);

CREATE INDEX linktarget_613 ON cat2princrel (TargetPK);

CREATE INDEX seqnr_613 ON cat2princrel (SequenceNumber);


CREATE CACHED TABLE cat2prodrel
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

CREATE INDEX rseqnr_143 ON cat2prodrel (RSequenceNumber);

CREATE INDEX linksource_143 ON cat2prodrel (SourcePK);

CREATE INDEX qualifier_143 ON cat2prodrel (Qualifier);

CREATE INDEX linktarget_143 ON cat2prodrel (TargetPK);

CREATE INDEX seqnr_143 ON cat2prodrel (SequenceNumber);


CREATE CACHED TABLE catalogs
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

CREATE INDEX idIdx_600 ON catalogs (p_id);


CREATE CACHED TABLE catalogslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE catalogversions
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

CREATE INDEX versionIDX_601 ON catalogversions (p_version);

CREATE INDEX catalogIDX_601 ON catalogversions (p_catalog);

CREATE INDEX visibleIDX_601 ON catalogversions (p_active);


CREATE CACHED TABLE catalogversionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_categorysystemname NVARCHAR(255),
    p_categorysystemdescription NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE catalogversionsyncjob
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

CREATE INDEX rseqnr_624 ON catalogversionsyncjob (RSequenceNumber);

CREATE INDEX linksource_624 ON catalogversionsyncjob (SourcePK);

CREATE INDEX qualifier_624 ON catalogversionsyncjob (Qualifier);

CREATE INDEX linktarget_624 ON catalogversionsyncjob (TargetPK);

CREATE INDEX seqnr_624 ON catalogversionsyncjob (SequenceNumber);


CREATE CACHED TABLE categories
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

CREATE INDEX codeIDX_142 ON categories (p_code);

CREATE INDEX versionIDX_142 ON categories (p_catalogversion);

CREATE INDEX codeVersionIDX_142 ON categories (p_code, p_catalogversion);

CREATE INDEX extID_142 ON categories (p_externalid);


CREATE CACHED TABLE categorieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE catverdiffs
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


CREATE CACHED TABLE changedescriptors
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

CREATE INDEX cronjobIDX_505 ON changedescriptors (p_cronjob);

CREATE INDEX stepIDX_505 ON changedescriptors (p_step);

CREATE INDEX changedItemIDX_505 ON changedescriptors (p_changeditem);

CREATE INDEX seqNrIDX_505 ON changedescriptors (p_sequencenumber);

CREATE INDEX doneIDX_505 ON changedescriptors (p_done);


CREATE CACHED TABLE classattrvalues
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

CREATE INDEX sysVer_608 ON classattrvalues (p_systemversion);

CREATE INDEX code_608 ON classattrvalues (p_code);

CREATE INDEX idIDX_608 ON classattrvalues (p_externalid);


CREATE CACHED TABLE classattrvalueslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE classificationattrs
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

CREATE INDEX sysVer_607 ON classificationattrs (p_systemversion);

CREATE INDEX code_607 ON classificationattrs (p_code);

CREATE INDEX idIDX_607 ON classificationattrs (p_externalid);


CREATE CACHED TABLE classificationattrslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE clattrunt
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

CREATE INDEX sysVer_612 ON clattrunt (p_systemversion);

CREATE INDEX codeIdx_612 ON clattrunt (p_code);

CREATE INDEX extID_612 ON clattrunt (p_externalid);


CREATE CACHED TABLE clattruntlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cmptype2covgrprels
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

CREATE INDEX rseqnr_978 ON cmptype2covgrprels (RSequenceNumber);

CREATE INDEX linksource_978 ON cmptype2covgrprels (SourcePK);

CREATE INDEX qualifier_978 ON cmptype2covgrprels (Qualifier);

CREATE INDEX linktarget_978 ON cmptype2covgrprels (TargetPK);

CREATE INDEX seqnr_978 ON cmptype2covgrprels (SequenceNumber);


CREATE CACHED TABLE cockpitcollections
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

CREATE INDEX userRelIDX_1700 ON cockpitcollections (p_user);


CREATE CACHED TABLE cockpitcollectionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_label NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cockpitcollelements
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

CREATE INDEX collectionRelIDX_1701 ON cockpitcollelements (p_collection);


CREATE CACHED TABLE cockpitcollitemrefs
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

CREATE INDEX itemIDX_1702 ON cockpitcollitemrefs (p_item);

CREATE INDEX collectionRelIDX_1702 ON cockpitcollitemrefs (p_collection);


CREATE CACHED TABLE cockpitcompaccessrights
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


CREATE CACHED TABLE cockpitcompconfigs
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

CREATE INDEX principalRelIDX_1707 ON cockpitcompconfigs (p_principal);


CREATE CACHED TABLE cockpitfavcategories
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

CREATE INDEX userRelIDX_1718 ON cockpitfavcategories (p_user);


CREATE CACHED TABLE cockpitfavcategorieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_label NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cockpititemtemplates
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

CREATE INDEX relatedtypeRelIDX_24242 ON cockpititemtemplates (p_relatedtype);


CREATE CACHED TABLE cockpititemtemplateslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cockpitsavedfacvalues
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

CREATE INDEX cockpitsavedqueryRelIDX_1704 ON cockpitsavedfacvalues (p_cockpitsavedquery);


CREATE CACHED TABLE cockpitsavedparamvals
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

CREATE INDEX cockpitsavedqueryRelIDX_1706 ON cockpitsavedparamvals (p_cockpitsavedquery);


CREATE CACHED TABLE cockpitsavedqueries
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

CREATE INDEX userRelIDX_1703 ON cockpitsavedqueries (p_user);


CREATE CACHED TABLE cockpitsavedquerieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cockpitsavedsortcrits
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

CREATE INDEX cockpitsavedqueryRelIDX_1705 ON cockpitsavedsortcrits (p_cockpitsavedquery);


CREATE CACHED TABLE cockpittemplclassifrels
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

CREATE INDEX rseqnr_1713 ON cockpittemplclassifrels (RSequenceNumber);

CREATE INDEX linksource_1713 ON cockpittemplclassifrels (SourcePK);

CREATE INDEX qualifier_1713 ON cockpittemplclassifrels (Qualifier);

CREATE INDEX linktarget_1713 ON cockpittemplclassifrels (TargetPK);

CREATE INDEX seqnr_1713 ON cockpittemplclassifrels (SequenceNumber);


CREATE CACHED TABLE collectiontypes
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

CREATE INDEX typecode_83 ON collectiontypes (InternalCode);

CREATE INDEX typecodelowercase_83 ON collectiontypes (InternalCodeLowerCase);


CREATE CACHED TABLE collectiontypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE commentassignrelations
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

CREATE INDEX rseqnr_1148 ON commentassignrelations (RSequenceNumber);

CREATE INDEX linksource_1148 ON commentassignrelations (SourcePK);

CREATE INDEX qualifier_1148 ON commentassignrelations (Qualifier);

CREATE INDEX linktarget_1148 ON commentassignrelations (TargetPK);

CREATE INDEX seqnr_1148 ON commentassignrelations (SequenceNumber);


CREATE CACHED TABLE commentattachments
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

CREATE INDEX comm_att_comment_1146 ON commentattachments (p_abstractcomment);


CREATE CACHED TABLE commentcompcreaterels
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

CREATE INDEX rseqnr_1152 ON commentcompcreaterels (RSequenceNumber);

CREATE INDEX linksource_1152 ON commentcompcreaterels (SourcePK);

CREATE INDEX qualifier_1152 ON commentcompcreaterels (Qualifier);

CREATE INDEX linktarget_1152 ON commentcompcreaterels (TargetPK);

CREATE INDEX seqnr_1152 ON commentcompcreaterels (SequenceNumber);


CREATE CACHED TABLE commentcomponents
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

CREATE INDEX commComponent_code_1142 ON commentcomponents (p_code);

CREATE INDEX domainRelIDX_1142 ON commentcomponents (p_domain);


CREATE CACHED TABLE commentcompreadrels
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

CREATE INDEX rseqnr_1150 ON commentcompreadrels (RSequenceNumber);

CREATE INDEX linksource_1150 ON commentcompreadrels (SourcePK);

CREATE INDEX qualifier_1150 ON commentcompreadrels (Qualifier);

CREATE INDEX linktarget_1150 ON commentcompreadrels (TargetPK);

CREATE INDEX seqnr_1150 ON commentcompreadrels (SequenceNumber);


CREATE CACHED TABLE commentcompremoverels
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

CREATE INDEX rseqnr_1153 ON commentcompremoverels (RSequenceNumber);

CREATE INDEX linksource_1153 ON commentcompremoverels (SourcePK);

CREATE INDEX qualifier_1153 ON commentcompremoverels (Qualifier);

CREATE INDEX linktarget_1153 ON commentcompremoverels (TargetPK);

CREATE INDEX seqnr_1153 ON commentcompremoverels (SequenceNumber);


CREATE CACHED TABLE commentcompwriterels
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

CREATE INDEX rseqnr_1151 ON commentcompwriterels (RSequenceNumber);

CREATE INDEX linksource_1151 ON commentcompwriterels (SourcePK);

CREATE INDEX qualifier_1151 ON commentcompwriterels (Qualifier);

CREATE INDEX linktarget_1151 ON commentcompwriterels (TargetPK);

CREATE INDEX seqnr_1151 ON commentcompwriterels (SequenceNumber);


CREATE CACHED TABLE commentdomains
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

CREATE INDEX commDomain_code_1141 ON commentdomains (p_code);


CREATE CACHED TABLE commentitemrelations
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

CREATE INDEX rseqnr_1147 ON commentitemrelations (RSequenceNumber);

CREATE INDEX linksource_1147 ON commentitemrelations (SourcePK);

CREATE INDEX qualifier_1147 ON commentitemrelations (Qualifier);

CREATE INDEX linktarget_1147 ON commentitemrelations (TargetPK);

CREATE INDEX seqnr_1147 ON commentitemrelations (SequenceNumber);


CREATE CACHED TABLE commentmetadatas
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

CREATE INDEX commentRelIDX_1716 ON commentmetadatas (p_comment);


CREATE CACHED TABLE comments
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

CREATE INDEX comment_code_1140 ON comments (p_code);

CREATE INDEX comment_component_1140 ON comments (p_component);

CREATE INDEX reply_parent_1140 ON comments (p_parent);

CREATE INDEX reply_comment_1140 ON comments (p_comment);

CREATE INDEX parentposPosIDX_1140 ON comments (p_parentpos);

CREATE INDEX commentposPosIDX_1140 ON comments (p_commentpos);

CREATE INDEX authorRelIDX_1140 ON comments (p_author);

CREATE INDEX commenttypeRelIDX_1140 ON comments (p_commenttype);


CREATE CACHED TABLE commenttypes
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

CREATE INDEX comment_type_code_1145 ON commenttypes (p_code, p_domain);

CREATE INDEX domainRelIDX_1145 ON commenttypes (p_domain);


CREATE CACHED TABLE commentusersettings
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

CREATE UNIQUE INDEX cus_user_comment_1144 ON commentusersettings (p_user, p_comment);

CREATE INDEX commentRelIDX_1144 ON commentusersettings (p_comment);

CREATE INDEX userRelIDX_1144 ON commentusersettings (p_user);


CREATE CACHED TABLE commentwatchrelations
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

CREATE INDEX rseqnr_1149 ON commentwatchrelations (RSequenceNumber);

CREATE INDEX linksource_1149 ON commentwatchrelations (SourcePK);

CREATE INDEX qualifier_1149 ON commentwatchrelations (Qualifier);

CREATE INDEX linktarget_1149 ON commentwatchrelations (TargetPK);

CREATE INDEX seqnr_1149 ON commentwatchrelations (SequenceNumber);


CREATE CACHED TABLE composedtypes
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

CREATE INDEX ComposedTypeSuperTypePKIDX_82 ON composedtypes (SuperTypePK);

CREATE INDEX typecode_82 ON composedtypes (InternalCode);

CREATE INDEX typecodelowercase_82 ON composedtypes (InternalCodeLowerCase);

CREATE INDEX inheritpsi_82 ON composedtypes (InheritancePathString);


CREATE CACHED TABLE composedtypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE compositeentries
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

CREATE INDEX compositecronjobRelIDX_510 ON compositeentries (p_compositecronjob);

CREATE INDEX compositecronjobposPosIDX_510 ON compositeentries (p_compositecronjobpos);


CREATE CACHED TABLE configitems
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


CREATE CACHED TABLE constraintgroup
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

CREATE INDEX CronstraintGroup_idx_982 ON constraintgroup (p_id);


CREATE CACHED TABLE conversionerrors
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

CREATE INDEX containerRelIDX_403 ON conversionerrors (p_container);


CREATE CACHED TABLE conversiongroups
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

CREATE UNIQUE INDEX ConvGroup_code_idx_401 ON conversiongroups (p_code);


CREATE CACHED TABLE conversiongroupslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE countries
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

CREATE INDEX ISOCode_34 ON countries (p_isocode);


CREATE CACHED TABLE countrieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cronjobs
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
    p_paging INTEGER,
    PRIMARY KEY (PK)
);

CREATE INDEX IdxJob_501 ON cronjobs (p_job);

CREATE INDEX IdxNode_501 ON cronjobs (p_nodeid);

CREATE INDEX IdxActive_501 ON cronjobs (p_active);


CREATE CACHED TABLE cronjobslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cstrgr2abscstrrel
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

CREATE INDEX rseqnr_979 ON cstrgr2abscstrrel (RSequenceNumber);

CREATE INDEX linksource_979 ON cstrgr2abscstrrel (SourcePK);

CREATE INDEX qualifier_979 ON cstrgr2abscstrrel (Qualifier);

CREATE INDEX linktarget_979 ON cstrgr2abscstrrel (TargetPK);

CREATE INDEX seqnr_979 ON cstrgr2abscstrrel (SequenceNumber);


CREATE CACHED TABLE currencies
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

CREATE INDEX ISOCode_33 ON currencies (p_isocode);


CREATE CACHED TABLE currencieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE deliverymodes
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


CREATE CACHED TABLE deliverymodeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE derivedmedias
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

CREATE INDEX dataPK_idx_31 ON derivedmedias (p_datapk);

CREATE UNIQUE INDEX version_idx_31 ON derivedmedias (p_media, p_version);

CREATE INDEX mediaRelIDX_31 ON derivedmedias (p_media);


CREATE CACHED TABLE discountrows
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

CREATE INDEX MatchIndexP_1052 ON discountrows (p_productmatchqualifier);

CREATE INDEX MatchIndexU_1052 ON discountrows (p_usermatchqualifier);

CREATE INDEX PIdx_1052 ON discountrows (p_product);

CREATE INDEX UIdx_1052 ON discountrows (p_user);

CREATE INDEX PGIdx_1052 ON discountrows (p_pg);

CREATE INDEX UGIdx_1052 ON discountrows (p_ug);

CREATE INDEX ProductIdIdx_1052 ON discountrows (p_productid);

CREATE INDEX versionIDX_1052 ON discountrows (p_catalogversion);


CREATE CACHED TABLE discounts
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


CREATE CACHED TABLE discountslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE dynamiccontent
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

CREATE UNIQUE INDEX codeVersionActiveIDX_101 ON dynamiccontent (p_code, p_version, p_active);


CREATE CACHED TABLE enumerationvalues
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

CREATE INDEX codeidx_91 ON enumerationvalues (Code);

CREATE INDEX code2idx_91 ON enumerationvalues (codeLowerCase);

CREATE INDEX seqnridx_91 ON enumerationvalues (SequenceNumber);


CREATE CACHED TABLE enumerationvalueslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE exports
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


CREATE CACHED TABLE exportslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE externalimportkey
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

CREATE UNIQUE INDEX sourceSystemIDSourceKeyIDX_110 ON externalimportkey (p_sourcesystemid, p_sourcekey);


CREATE CACHED TABLE format
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


CREATE CACHED TABLE format2comtyprel
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

CREATE INDEX rseqnr_13102 ON format2comtyprel (RSequenceNumber);

CREATE INDEX linksource_13102 ON format2comtyprel (SourcePK);

CREATE INDEX qualifier_13102 ON format2comtyprel (Qualifier);

CREATE INDEX linktarget_13102 ON format2comtyprel (TargetPK);

CREATE INDEX seqnr_13102 ON format2comtyprel (SequenceNumber);


CREATE CACHED TABLE format2medforrel
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

CREATE INDEX rseqnr_13101 ON format2medforrel (RSequenceNumber);

CREATE INDEX linksource_13101 ON format2medforrel (SourcePK);

CREATE INDEX qualifier_13101 ON format2medforrel (Qualifier);

CREATE INDEX linktarget_13101 ON format2medforrel (TargetPK);

CREATE INDEX seqnr_13101 ON format2medforrel (SequenceNumber);


CREATE CACHED TABLE formatlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE genericitems
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

CREATE INDEX actiontemplateRelIDX_99 ON genericitems (p_actiontemplate);

CREATE INDEX actionRelIDX_99 ON genericitems (p_action);

CREATE INDEX wherepartRelIDX_99 ON genericitems (p_wherepart);


CREATE CACHED TABLE genericitemslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE gentestitems
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


CREATE CACHED TABLE globaldiscountrows
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

CREATE INDEX MatchIndexP_1053 ON globaldiscountrows (p_productmatchqualifier);

CREATE INDEX MatchIndexU_1053 ON globaldiscountrows (p_usermatchqualifier);

CREATE INDEX PIdx_1053 ON globaldiscountrows (p_product);

CREATE INDEX UIdx_1053 ON globaldiscountrows (p_user);

CREATE INDEX PGIdx_1053 ON globaldiscountrows (p_pg);

CREATE INDEX UGIdx_1053 ON globaldiscountrows (p_ug);

CREATE INDEX ProductIdIdx_1053 ON globaldiscountrows (p_productid);


CREATE CACHED TABLE hmchistoryentries
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


CREATE CACHED TABLE indextestitem
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

CREATE INDEX OrderIndex_7777 ON indextestitem (p_column3, p_column4, p_column1, p_column2, p_column5);


CREATE CACHED TABLE itemcockpittemplrels
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

CREATE INDEX rseqnr_1712 ON itemcockpittemplrels (RSequenceNumber);

CREATE INDEX linksource_1712 ON itemcockpittemplrels (SourcePK);

CREATE INDEX qualifier_1712 ON itemcockpittemplrels (Qualifier);

CREATE INDEX linktarget_1712 ON itemcockpittemplrels (TargetPK);

CREATE INDEX seqnr_1712 ON itemcockpittemplrels (SequenceNumber);


CREATE CACHED TABLE itemsynctimestamps
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

CREATE UNIQUE INDEX syncIDX_619 ON itemsynctimestamps (p_sourceitem, p_targetversion, p_syncjob);

CREATE INDEX srcIDX_619 ON itemsynctimestamps (p_sourceitem);

CREATE INDEX tgtIDX_619 ON itemsynctimestamps (p_targetitem);

CREATE INDEX jobIDX_619 ON itemsynctimestamps (p_syncjob);

CREATE INDEX srcVerIDX_619 ON itemsynctimestamps (p_sourceversion);

CREATE INDEX tgtVerIDX_619 ON itemsynctimestamps (p_targetversion);


CREATE CACHED TABLE jalotranslatorconfig
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


CREATE CACHED TABLE jalovelocityrenderer
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

CREATE INDEX translatorconfigurationRelIDX_13211 ON jalovelocityrenderer (p_translatorconfiguration);

CREATE INDEX translatorconfigurationposPosIDX_13211 ON jalovelocityrenderer (p_translatorconfigurationpos);


CREATE CACHED TABLE joblogs
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

CREATE INDEX cronjobIDX_504 ON joblogs (p_cronjob);


CREATE CACHED TABLE jobs
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

CREATE INDEX IdxCode_500 ON jobs (p_code);


CREATE CACHED TABLE jobsearchrestriction
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

CREATE INDEX jobRelIDX_508 ON jobsearchrestriction (p_job);

CREATE INDEX jobposPosIDX_508 ON jobsearchrestriction (p_jobpos);


CREATE CACHED TABLE jobslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE keywords
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

CREATE INDEX keywordIDX_602 ON keywords (p_keyword);

CREATE INDEX versionIDX_602 ON keywords (p_catalogversion);

CREATE INDEX codeVersionIDX_602 ON keywords (p_keyword, p_catalogversion);

CREATE INDEX extIDX_602 ON keywords (p_externalid);


CREATE CACHED TABLE languages
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

CREATE INDEX ISOCode_32 ON languages (p_isocode);


CREATE CACHED TABLE languageslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE links
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

CREATE INDEX rseqnr_7 ON links (RSequenceNumber);

CREATE INDEX linksource_7 ON links (SourcePK);

CREATE INDEX qualifier_7 ON links (Qualifier);

CREATE INDEX linktarget_7 ON links (TargetPK);

CREATE INDEX seqnr_7 ON links (SequenceNumber);


CREATE CACHED TABLE maptypes
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

CREATE INDEX typecode_84 ON maptypes (InternalCode);

CREATE INDEX typecodelowercase_84 ON maptypes (InternalCodeLowerCase);


CREATE CACHED TABLE maptypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE mediacontainer
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

CREATE INDEX versionIDX_50 ON mediacontainer (p_catalogversion);

CREATE INDEX codeVersionIDX_50 ON mediacontainer (p_qualifier, p_catalogversion);


CREATE CACHED TABLE mediacontainerlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE mediacontext
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

CREATE UNIQUE INDEX qualifierIDX_52 ON mediacontext (p_qualifier);


CREATE CACHED TABLE mediacontextlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE mediaconttypeformats
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

CREATE INDEX rseqnr_402 ON mediaconttypeformats (RSequenceNumber);

CREATE INDEX linksource_402 ON mediaconttypeformats (SourcePK);

CREATE INDEX qualifier_402 ON mediaconttypeformats (Qualifier);

CREATE INDEX linktarget_402 ON mediaconttypeformats (TargetPK);

CREATE INDEX seqnr_402 ON mediaconttypeformats (SequenceNumber);


CREATE CACHED TABLE mediafolders
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

CREATE UNIQUE INDEX qualifierIdx_54 ON mediafolders (p_qualifier);


CREATE CACHED TABLE mediaformat
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

CREATE UNIQUE INDEX qualifierIDX_51 ON mediaformat (p_qualifier);


CREATE CACHED TABLE mediaformatlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE mediaformatmapping
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

CREATE INDEX mediacontextRelIDX_53 ON mediaformatmapping (p_mediacontext);


CREATE CACHED TABLE mediametadata
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

CREATE UNIQUE INDEX mmetadata_uidx_400 ON mediametadata (p_media, p_groupname, p_code);

CREATE INDEX mediaRelIDX_400 ON mediametadata (p_media);


CREATE CACHED TABLE mediaprops
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

CREATE INDEX realnameidx_mediaprops ON mediaprops (REALNAME);

CREATE INDEX itempk_mediaprops ON mediaprops (ITEMPK);

CREATE INDEX nameidx_mediaprops ON mediaprops (NAME);


CREATE CACHED TABLE medias
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

CREATE INDEX dataPK_idx_30 ON medias (p_datapk);

CREATE INDEX Media_Code_30 ON medias (p_code);

CREATE INDEX versionIDX_30 ON medias (p_catalogversion);

CREATE INDEX codeVersionIDX_30 ON medias (p_code, p_catalogversion);

CREATE INDEX containerformat_idx_30 ON medias (p_mediacontainer, p_mediaformat);

CREATE INDEX parentformat_idx_30 ON medias (p_original, p_mediaformat);

CREATE INDEX mediacontainerRelIDX_30 ON medias (p_mediacontainer);

CREATE INDEX sourceitemRelIDX_30 ON medias (p_sourceitem);

CREATE INDEX cronjobRelIDX_30 ON medias (p_cronjob);

CREATE INDEX cronjobposPosIDX_30 ON medias (p_cronjobpos);

CREATE INDEX originalRelIDX_30 ON medias (p_original);


CREATE CACHED TABLE mediaslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    p_reportdescription NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE metainformations
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


CREATE CACHED TABLE numberseries
(
    hjmpTS BIGINT,
    serieskey NVARCHAR(255),
    seriestype INTEGER DEFAULT 0,
    currentValue BIGINT,
    template NVARCHAR(255),
    PRIMARY KEY (serieskey)
);


CREATE CACHED TABLE orderdiscrels
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

CREATE INDEX rseqnr_202 ON orderdiscrels (RSequenceNumber);

CREATE INDEX linksource_202 ON orderdiscrels (SourcePK);

CREATE INDEX qualifier_202 ON orderdiscrels (Qualifier);

CREATE INDEX linktarget_202 ON orderdiscrels (TargetPK);

CREATE INDEX seqnr_202 ON orderdiscrels (SequenceNumber);


CREATE CACHED TABLE orderentries
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

CREATE INDEX oeProd_46 ON orderentries (p_product);

CREATE INDEX oeOrd_46 ON orderentries (p_order);


CREATE CACHED TABLE orderentryprops
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

CREATE INDEX itempk_orderentryprops ON orderentryprops (ITEMPK);

CREATE INDEX nameidx_orderentryprops ON orderentryprops (NAME);

CREATE INDEX realnameidx_orderentryprops ON orderentryprops (REALNAME);


CREATE CACHED TABLE orderprops
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

CREATE INDEX itempk_orderprops ON orderprops (ITEMPK);

CREATE INDEX realnameidx_orderprops ON orderprops (REALNAME);

CREATE INDEX nameidx_orderprops ON orderprops (NAME);


CREATE CACHED TABLE orders
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

CREATE INDEX OrderCode_45 ON orders (p_code);

CREATE INDEX OrderUser_45 ON orders (p_user);


CREATE CACHED TABLE parserproperty
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

CREATE INDEX translatorconfigurationRelIDX_13213 ON parserproperty (p_translatorconfiguration);

CREATE INDEX translatorconfigurationposPosIDX_13213 ON parserproperty (p_translatorconfigurationpos);


CREATE CACHED TABLE paymentinfos
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

CREATE INDEX PaymentInfo_User_42 ON paymentinfos (p_user);


CREATE CACHED TABLE paymentmodes
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


CREATE CACHED TABLE paymentmodeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE pcp2wrtblecvrel
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

CREATE INDEX rseqnr_617 ON pcp2wrtblecvrel (RSequenceNumber);

CREATE INDEX linksource_617 ON pcp2wrtblecvrel (SourcePK);

CREATE INDEX qualifier_617 ON pcp2wrtblecvrel (Qualifier);

CREATE INDEX linktarget_617 ON pcp2wrtblecvrel (TargetPK);

CREATE INDEX seqnr_617 ON pcp2wrtblecvrel (SequenceNumber);


CREATE CACHED TABLE pcpl2rdblecvrel
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

CREATE INDEX rseqnr_618 ON pcpl2rdblecvrel (RSequenceNumber);

CREATE INDEX linksource_618 ON pcpl2rdblecvrel (SourcePK);

CREATE INDEX qualifier_618 ON pcpl2rdblecvrel (Qualifier);

CREATE INDEX linktarget_618 ON pcpl2rdblecvrel (TargetPK);

CREATE INDEX seqnr_618 ON pcpl2rdblecvrel (SequenceNumber);


CREATE CACHED TABLE pendingstepsrelation
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

CREATE INDEX rseqnr_507 ON pendingstepsrelation (RSequenceNumber);

CREATE INDEX linksource_507 ON pendingstepsrelation (SourcePK);

CREATE INDEX qualifier_507 ON pendingstepsrelation (Qualifier);

CREATE INDEX linktarget_507 ON pendingstepsrelation (TargetPK);

CREATE INDEX seqnr_507 ON pendingstepsrelation (SequenceNumber);


CREATE CACHED TABLE pgrels
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

CREATE INDEX rseqnr_201 ON pgrels (RSequenceNumber);

CREATE INDEX linksource_201 ON pgrels (SourcePK);

CREATE INDEX qualifier_201 ON pgrels (Qualifier);

CREATE INDEX linktarget_201 ON pgrels (TargetPK);

CREATE INDEX seqnr_201 ON pgrels (SequenceNumber);


CREATE CACHED TABLE previewtickets
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


CREATE CACHED TABLE pricerows
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

CREATE INDEX MatchIndexP_1055 ON pricerows (p_productmatchqualifier);

CREATE INDEX MatchIndexU_1055 ON pricerows (p_usermatchqualifier);

CREATE INDEX PIdx_1055 ON pricerows (p_product);

CREATE INDEX UIdx_1055 ON pricerows (p_user);

CREATE INDEX PGIdx_1055 ON pricerows (p_pg);

CREATE INDEX UGIdx_1055 ON pricerows (p_ug);

CREATE INDEX ProductIdIdx_1055 ON pricerows (p_productid);

CREATE INDEX versionIDX_1055 ON pricerows (p_catalogversion);


CREATE CACHED TABLE principcockpitreadrels
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

CREATE INDEX rseqnr_1714 ON principcockpitreadrels (RSequenceNumber);

CREATE INDEX linksource_1714 ON principcockpitreadrels (SourcePK);

CREATE INDEX qualifier_1714 ON principcockpitreadrels (Qualifier);

CREATE INDEX linktarget_1714 ON principcockpitreadrels (TargetPK);

CREATE INDEX seqnr_1714 ON principcockpitreadrels (SequenceNumber);


CREATE CACHED TABLE principcockpitwriterels
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

CREATE INDEX rseqnr_1715 ON principcockpitwriterels (RSequenceNumber);

CREATE INDEX linksource_1715 ON principcockpitwriterels (SourcePK);

CREATE INDEX qualifier_1715 ON principcockpitwriterels (Qualifier);

CREATE INDEX linktarget_1715 ON principcockpitwriterels (TargetPK);

CREATE INDEX seqnr_1715 ON principcockpitwriterels (SequenceNumber);


CREATE CACHED TABLE princtolinkrelations
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

CREATE INDEX rseqnr_6001 ON princtolinkrelations (RSequenceNumber);

CREATE INDEX linksource_6001 ON princtolinkrelations (SourcePK);

CREATE INDEX qualifier_6001 ON princtolinkrelations (Qualifier);

CREATE INDEX linktarget_6001 ON princtolinkrelations (TargetPK);

CREATE INDEX seqnr_6001 ON princtolinkrelations (SequenceNumber);


CREATE CACHED TABLE processedstepsrelation
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

CREATE INDEX rseqnr_506 ON processedstepsrelation (RSequenceNumber);

CREATE INDEX linksource_506 ON processedstepsrelation (SourcePK);

CREATE INDEX qualifier_506 ON processedstepsrelation (Qualifier);

CREATE INDEX linktarget_506 ON processedstepsrelation (TargetPK);

CREATE INDEX seqnr_506 ON processedstepsrelation (SequenceNumber);


CREATE CACHED TABLE processes
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

CREATE UNIQUE INDEX ProcessengineProcess_name_idx_32766 ON processes (p_code);


CREATE CACHED TABLE processparameters
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

CREATE UNIQUE INDEX BusinessProcessParameter_idx_32764 ON processparameters (p_process, p_name);

CREATE INDEX processRelIDX_32764 ON processparameters (p_process);


CREATE CACHED TABLE prod2keywordrel
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

CREATE INDEX rseqnr_604 ON prod2keywordrel (RSequenceNumber);

CREATE INDEX linksource_604 ON prod2keywordrel (SourcePK);

CREATE INDEX qualifier_604 ON prod2keywordrel (Qualifier);

CREATE INDEX linktarget_604 ON prod2keywordrel (TargetPK);

CREATE INDEX seqnr_604 ON prod2keywordrel (SequenceNumber);


CREATE CACHED TABLE productfeatures
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

CREATE INDEX featureIDX_611 ON productfeatures (p_product);

CREATE INDEX featureIDX2_611 ON productfeatures (p_qualifier);

CREATE INDEX featureIDX3_611 ON productfeatures (p_classificationattributeassig);


CREATE CACHED TABLE productprops
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

CREATE INDEX nameidx_productprops ON productprops (NAME);

CREATE INDEX realnameidx_productprops ON productprops (REALNAME);

CREATE INDEX itempk_productprops ON productprops (ITEMPK);


CREATE CACHED TABLE productreferences
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

CREATE INDEX targetIDX_606 ON productreferences (p_target);

CREATE INDEX qualifierIDX_606 ON productreferences (p_qualifier);

CREATE INDEX sourceRelIDX_606 ON productreferences (p_source);

CREATE INDEX sourceposPosIDX_606 ON productreferences (p_sourcepos);


CREATE CACHED TABLE productreferenceslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE products
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

CREATE INDEX Product_Code_1 ON products (p_code);

CREATE INDEX catalogIDX_1 ON products (p_catalog);

CREATE INDEX visibilityIDX_1 ON products (p_approvalstatus, p_onlinedate, p_offlinedate);

CREATE INDEX codeVersionIDX_1 ON products (p_code, p_catalogversion);

CREATE INDEX versionIDX_1 ON products (p_catalogversion);

CREATE INDEX baseIDX_1 ON products (p_baseproduct);


CREATE CACHED TABLE productslp
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


CREATE CACHED TABLE props
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

CREATE INDEX itempk_props ON props (ITEMPK);

CREATE INDEX realnameidx_props ON props (REALNAME);

CREATE INDEX nameidx_props ON props (NAME);


CREATE CACHED TABLE readcockpitcollrels
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

CREATE INDEX rseqnr_1710 ON readcockpitcollrels (RSequenceNumber);

CREATE INDEX linksource_1710 ON readcockpitcollrels (SourcePK);

CREATE INDEX qualifier_1710 ON readcockpitcollrels (Qualifier);

CREATE INDEX linktarget_1710 ON readcockpitcollrels (TargetPK);

CREATE INDEX seqnr_1710 ON readcockpitcollrels (SequenceNumber);


CREATE CACHED TABLE readsavedqueryrels
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

CREATE INDEX rseqnr_1717 ON readsavedqueryrels (RSequenceNumber);

CREATE INDEX linksource_1717 ON readsavedqueryrels (SourcePK);

CREATE INDEX qualifier_1717 ON readsavedqueryrels (Qualifier);

CREATE INDEX linktarget_1717 ON readsavedqueryrels (TargetPK);

CREATE INDEX seqnr_1717 ON readsavedqueryrels (SequenceNumber);


CREATE CACHED TABLE regions
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

CREATE INDEX ISOCode_35 ON regions (p_isocode);

CREATE INDEX Region_Country_35 ON regions (p_country);


CREATE CACHED TABLE regionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE renderersproperty
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

CREATE INDEX translatorconfigurationRelIDX_13212 ON renderersproperty (p_translatorconfiguration);

CREATE INDEX translatorconfigurationposPosIDX_13212 ON renderersproperty (p_translatorconfigurationpos);


CREATE CACHED TABLE renderertemplate
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


CREATE CACHED TABLE renderertemplatelp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    p_content BIGINT,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE savedqueries
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


CREATE CACHED TABLE savedquerieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE savedvalueentry
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

CREATE INDEX parentRelIDX_335 ON savedvalueentry (p_parent);


CREATE CACHED TABLE savedvalues
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

CREATE INDEX savedvalmoditem_334 ON savedvalues (p_modifieditem);

CREATE INDEX modifieditemposPosIDX_334 ON savedvalues (p_modifieditempos);


CREATE CACHED TABLE scripts
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

CREATE UNIQUE INDEX codeVersionActiveIDX_100 ON scripts (p_code, p_version, p_active);


CREATE CACHED TABLE scriptslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE searchrestrictions
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

CREATE INDEX restrtype_90 ON searchrestrictions (RestrictedType);

CREATE INDEX principal_90 ON searchrestrictions (principal);


CREATE CACHED TABLE searchrestrictionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE slactions
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


CREATE CACHED TABLE stdpaymmodevals
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

CREATE INDEX paymentmodeRelIDX_1022 ON stdpaymmodevals (p_paymentmode);


CREATE CACHED TABLE steps
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

CREATE INDEX IdxBatchJob_503 ON steps (p_batchjob);

CREATE INDEX seqNrIDX_503 ON steps (p_sequencenumber);


CREATE CACHED TABLE synattcfg
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

CREATE INDEX jobIdx_620 ON synattcfg (p_syncjob);

CREATE INDEX attrIdx_620 ON synattcfg (p_attributedescriptor);


CREATE CACHED TABLE syncjob2langrel
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

CREATE INDEX rseqnr_622 ON syncjob2langrel (RSequenceNumber);

CREATE INDEX linksource_622 ON syncjob2langrel (SourcePK);

CREATE INDEX qualifier_622 ON syncjob2langrel (Qualifier);

CREATE INDEX linktarget_622 ON syncjob2langrel (TargetPK);

CREATE INDEX seqnr_622 ON syncjob2langrel (SequenceNumber);


CREATE CACHED TABLE syncjob2pcplrel
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

CREATE INDEX rseqnr_623 ON syncjob2pcplrel (RSequenceNumber);

CREATE INDEX linksource_623 ON syncjob2pcplrel (SourcePK);

CREATE INDEX qualifier_623 ON syncjob2pcplrel (Qualifier);

CREATE INDEX linktarget_623 ON syncjob2pcplrel (TargetPK);

CREATE INDEX seqnr_623 ON syncjob2pcplrel (SequenceNumber);


CREATE CACHED TABLE syncjob2typerel
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

CREATE INDEX rseqnr_621 ON syncjob2typerel (RSequenceNumber);

CREATE INDEX linksource_621 ON syncjob2typerel (SourcePK);

CREATE INDEX qualifier_621 ON syncjob2typerel (Qualifier);

CREATE INDEX linktarget_621 ON syncjob2typerel (TargetPK);

CREATE INDEX seqnr_621 ON syncjob2typerel (SequenceNumber);


CREATE CACHED TABLE taskconditions
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

CREATE UNIQUE INDEX Cond_idx_951 ON taskconditions (p_uniqueid, p_consumed);

CREATE INDEX Cond_match_idx_951 ON taskconditions (p_task, p_fulfilled);

CREATE INDEX taskRelIDX_951 ON taskconditions (p_task);


CREATE CACHED TABLE tasklogs
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

CREATE INDEX processRelIDX_32767 ON tasklogs (p_process);


CREATE CACHED TABLE tasks
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

CREATE INDEX Task_dr_idx_950 ON tasks (p_runningonclusternode, p_expirationtimemillis, p_nodeid);

CREATE INDEX processRelIDX_950 ON tasks (p_process);


CREATE CACHED TABLE taxes
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


CREATE CACHED TABLE taxeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE taxrows
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

CREATE INDEX MatchIndexP_1054 ON taxrows (p_productmatchqualifier);

CREATE INDEX MatchIndexU_1054 ON taxrows (p_usermatchqualifier);

CREATE INDEX PIdx_1054 ON taxrows (p_product);

CREATE INDEX UIdx_1054 ON taxrows (p_user);

CREATE INDEX PGIdx_1054 ON taxrows (p_pg);

CREATE INDEX UGIdx_1054 ON taxrows (p_ug);

CREATE INDEX ProductIdIdx_1054 ON taxrows (p_productid);

CREATE INDEX versionIDX_1054 ON taxrows (p_catalogversion);


CREATE CACHED TABLE testitem
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


CREATE CACHED TABLE testitemlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_testproperty2 NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE titles
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


CREATE CACHED TABLE titleslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE triggerscj
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

CREATE INDEX IdxCronJob_502 ON triggerscj (p_cronjob);

CREATE INDEX IdxActive_502 ON triggerscj (p_active);

CREATE INDEX jobRelIDX_502 ON triggerscj (p_job);


CREATE CACHED TABLE typesystemprops
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

CREATE INDEX itempk_typesystemprops ON typesystemprops (ITEMPK);

CREATE INDEX nameidx_typesystemprops ON typesystemprops (NAME);

CREATE INDEX realnameidx_typesystemprops ON typesystemprops (REALNAME);


CREATE CACHED TABLE units
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


CREATE CACHED TABLE unitslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE usergroupprops
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

CREATE INDEX nameidx_usergroupprops ON usergroupprops (NAME);

CREATE INDEX itempk_usergroupprops ON usergroupprops (ITEMPK);

CREATE INDEX realnameidx_usergroupprops ON usergroupprops (REALNAME);


CREATE CACHED TABLE usergroups
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

CREATE UNIQUE INDEX UID_5 ON usergroups (p_uid);


CREATE CACHED TABLE usergroupslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_locname NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE userprofiles
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


CREATE CACHED TABLE userprops
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

CREATE INDEX realnameidx_userprops ON userprops (REALNAME);

CREATE INDEX itempk_userprops ON userprops (ITEMPK);

CREATE INDEX nameidx_userprops ON userprops (NAME);


CREATE CACHED TABLE userrights
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


CREATE CACHED TABLE userrightslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE users
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
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX UID_4 ON users (p_uid);


CREATE CACHED TABLE validationconstraints
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

CREATE UNIQUE INDEX AbstractConstraint_idx_980 ON validationconstraints (p_id);

CREATE INDEX typeRelIDX_980 ON validationconstraints (p_type);

CREATE INDEX descriptorRelIDX_980 ON validationconstraints (p_descriptor);


CREATE CACHED TABLE validationconstraintslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_message LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE whereparts
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

CREATE INDEX savedqueryRelIDX_1300 ON whereparts (p_savedquery);


CREATE CACHED TABLE widgetparameter
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

CREATE INDEX widgetpreferencesRelIDX_2072 ON widgetparameter (p_widgetpreferences);


CREATE CACHED TABLE widgetpreferences
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

CREATE INDEX owneruserRelIDX_2071 ON widgetpreferences (p_owneruser);


CREATE CACHED TABLE widgetpreferenceslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE workflowactioncomments
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

CREATE INDEX workflowactionRelIDX_1118 ON workflowactioncomments (p_workflowaction);


CREATE CACHED TABLE workflowactionitemsrel
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

CREATE INDEX rseqnr_1116 ON workflowactionitemsrel (RSequenceNumber);

CREATE INDEX linksource_1116 ON workflowactionitemsrel (SourcePK);

CREATE INDEX qualifier_1116 ON workflowactionitemsrel (Qualifier);

CREATE INDEX linktarget_1116 ON workflowactionitemsrel (TargetPK);

CREATE INDEX seqnr_1116 ON workflowactionitemsrel (SequenceNumber);


CREATE CACHED TABLE workflowactionlinkrel
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

CREATE INDEX rseqnr_1124 ON workflowactionlinkrel (RSequenceNumber);

CREATE INDEX linksource_1124 ON workflowactionlinkrel (SourcePK);

CREATE INDEX qualifier_1124 ON workflowactionlinkrel (Qualifier);

CREATE INDEX linktarget_1124 ON workflowactionlinkrel (TargetPK);

CREATE INDEX seqnr_1124 ON workflowactionlinkrel (SequenceNumber);


CREATE CACHED TABLE workflowactions
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

CREATE INDEX codeIDX_1113 ON workflowactions (p_code);

CREATE INDEX workflowRelIDX_1113 ON workflowactions (p_workflow);

CREATE INDEX workflowposPosIDX_1113 ON workflowactions (p_workflowpos);


CREATE CACHED TABLE workflowactionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE workflowactionsrel
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

CREATE INDEX rseqnr_1115 ON workflowactionsrel (RSequenceNumber);

CREATE INDEX linksource_1115 ON workflowactionsrel (SourcePK);

CREATE INDEX qualifier_1115 ON workflowactionsrel (Qualifier);

CREATE INDEX linktarget_1115 ON workflowactionsrel (TargetPK);

CREATE INDEX seqnr_1115 ON workflowactionsrel (SequenceNumber);


CREATE CACHED TABLE workflowitematts
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

CREATE INDEX codeIDX_1114 ON workflowitematts (p_code);

CREATE INDEX workflowRelIDX_1114 ON workflowitematts (p_workflow);

CREATE INDEX workflowposPosIDX_1114 ON workflowitematts (p_workflowpos);


CREATE CACHED TABLE workflowitemattslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE workflowtemplatelinkrel
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

CREATE INDEX rseqnr_1125 ON workflowtemplatelinkrel (RSequenceNumber);

CREATE INDEX linksource_1125 ON workflowtemplatelinkrel (SourcePK);

CREATE INDEX qualifier_1125 ON workflowtemplatelinkrel (Qualifier);

CREATE INDEX linktarget_1125 ON workflowtemplatelinkrel (TargetPK);

CREATE INDEX seqnr_1125 ON workflowtemplatelinkrel (SequenceNumber);


CREATE CACHED TABLE workflowtemplprincrel
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

CREATE INDEX rseqnr_1117 ON workflowtemplprincrel (RSequenceNumber);

CREATE INDEX linksource_1117 ON workflowtemplprincrel (SourcePK);

CREATE INDEX qualifier_1117 ON workflowtemplprincrel (Qualifier);

CREATE INDEX linktarget_1117 ON workflowtemplprincrel (TargetPK);

CREATE INDEX seqnr_1117 ON workflowtemplprincrel (SequenceNumber);


CREATE CACHED TABLE writecockpitcollrels
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

CREATE INDEX rseqnr_1711 ON writecockpitcollrels (RSequenceNumber);

CREATE INDEX linksource_1711 ON writecockpitcollrels (SourcePK);

CREATE INDEX qualifier_1711 ON writecockpitcollrels (Qualifier);

CREATE INDEX linktarget_1711 ON writecockpitcollrels (TargetPK);

CREATE INDEX seqnr_1711 ON writecockpitcollrels (SequenceNumber);


CREATE CACHED TABLE ydeployments
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

CREATE INDEX deplselect_ydeployments ON ydeployments (ExtensionName);

CREATE INDEX deplselect2_ydeployments ON ydeployments (Typecode);

CREATE INDEX tsnameidx_ydeployments ON ydeployments (TypeSystemName);


CREATE CACHED TABLE zone2country
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

CREATE INDEX rseqnr_1204 ON zone2country (RSequenceNumber);

CREATE INDEX linksource_1204 ON zone2country (SourcePK);

CREATE INDEX qualifier_1204 ON zone2country (Qualifier);

CREATE INDEX linktarget_1204 ON zone2country (TargetPK);

CREATE INDEX seqnr_1204 ON zone2country (SequenceNumber);


CREATE CACHED TABLE zonedeliverymodevalues
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

CREATE UNIQUE INDEX IdentityIDX_1202 ON zonedeliverymodevalues (p_deliverymode, p_zone, p_currency, p_minimum);

CREATE INDEX ModeIDX_1202 ON zonedeliverymodevalues (p_deliverymode);

CREATE INDEX ZoneIDX_1202 ON zonedeliverymodevalues (p_zone);


CREATE CACHED TABLE zones
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

CREATE UNIQUE INDEX IdentityIDX_1203 ON zones (p_code);

