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
package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.model.template.CockpitItemTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Set;

/**
 * Generated model class for type ClassificationClass first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ClassificationClassModel extends CategoryModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ClassificationClass";
	
	/**<i>Generated relation code constant for relation <code>CockpitItemTemplate2ClassificationClassRelation</code> defining source attribute <code>cockpitItemTemplates</code> in extension <code>cockpit</code>.</i>*/
	public final static String _COCKPITITEMTEMPLATE2CLASSIFICATIONCLASSRELATION = "CockpitItemTemplate2ClassificationClassRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationClass.externalID</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXTERNALID = "externalID";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationClass.revision</code> attribute defined at extension <code>catalog</code>. */
	public static final String REVISION = "revision";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationClass.showEmptyAttributes</code> attribute defined at extension <code>catalog</code>. */
	public static final String SHOWEMPTYATTRIBUTES = "showEmptyAttributes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationClass.declaredClassificationAttributes</code> attribute defined at extension <code>catalog</code>. */
	public static final String DECLAREDCLASSIFICATIONATTRIBUTES = "declaredClassificationAttributes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationClass.inheritedClassificationAttributes</code> attribute defined at extension <code>catalog</code>. */
	public static final String INHERITEDCLASSIFICATIONATTRIBUTES = "inheritedClassificationAttributes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationClass.classificationAttributes</code> attribute defined at extension <code>catalog</code>. */
	public static final String CLASSIFICATIONATTRIBUTES = "classificationAttributes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationClass.declaredClassificationAttributeAssignments</code> attribute defined at extension <code>catalog</code>. */
	public static final String DECLAREDCLASSIFICATIONATTRIBUTEASSIGNMENTS = "declaredClassificationAttributeAssignments";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationClass.hmcXML</code> attribute defined at extension <code>catalog</code>. */
	public static final String HMCXML = "hmcXML";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationClass.allClassificationAttributeAssignments</code> attribute defined at extension <code>catalog</code>. */
	public static final String ALLCLASSIFICATIONATTRIBUTEASSIGNMENTS = "allClassificationAttributeAssignments";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationClass.cockpitItemTemplates</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITITEMTEMPLATES = "cockpitItemTemplates";
	
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationClass.externalID</code> attribute defined at extension <code>catalog</code>. */
	private String _externalID;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationClass.revision</code> attribute defined at extension <code>catalog</code>. */
	private String _revision;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationClass.showEmptyAttributes</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _showEmptyAttributes;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationClass.declaredClassificationAttributes</code> attribute defined at extension <code>catalog</code>. */
	private List<ClassificationAttributeModel> _declaredClassificationAttributes;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationClass.inheritedClassificationAttributes</code> attribute defined at extension <code>catalog</code>. */
	private List<ClassificationAttributeModel> _inheritedClassificationAttributes;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationClass.classificationAttributes</code> attribute defined at extension <code>catalog</code>. */
	private List<ClassificationAttributeModel> _classificationAttributes;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationClass.declaredClassificationAttributeAssignments</code> attribute defined at extension <code>catalog</code>. */
	private List<ClassAttributeAssignmentModel> _declaredClassificationAttributeAssignments;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationClass.hmcXML</code> attribute defined at extension <code>catalog</code>. */
	private String _hmcXML;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationClass.allClassificationAttributeAssignments</code> attribute defined at extension <code>catalog</code>. */
	private List<ClassAttributeAssignmentModel> _allClassificationAttributeAssignments;
	
	/** <i>Generated variable</i> - Variable of <code>ClassificationClass.cockpitItemTemplates</code> attribute defined at extension <code>cockpit</code>. */
	private Set<CockpitItemTemplateModel> _cockpitItemTemplates;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ClassificationClassModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ClassificationClassModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>ClassificationClass</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationClassModel(final ClassificationSystemVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>ClassificationClass</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ClassificationClassModel(final ClassificationSystemVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationClass.allClassificationAttributeAssignments</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allClassificationAttributeAssignments - list all attribute assignments (recursively to the top)
	 */
	@Accessor(qualifier = "allClassificationAttributeAssignments", type = Accessor.Type.GETTER)
	public List<ClassAttributeAssignmentModel> getAllClassificationAttributeAssignments()
	{
		return getPersistenceContext().getDynamicValue(this,ALLCLASSIFICATIONATTRIBUTEASSIGNMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.catalogVersion</code> attribute defined at extension <code>catalog</code> and redeclared at extension <code>catalog</code>. 
	 * @return the catalogVersion
	 */
	@Override
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public ClassificationSystemVersionModel getCatalogVersion()
	{
		return (ClassificationSystemVersionModel) super.getCatalogVersion();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationClass.classificationAttributes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the classificationAttributes - list of all attributes available within this class
	 */
	@Accessor(qualifier = "classificationAttributes", type = Accessor.Type.GETTER)
	public List<ClassificationAttributeModel> getClassificationAttributes()
	{
		if (this._classificationAttributes!=null)
		{
			return _classificationAttributes;
		}
		return _classificationAttributes = getPersistenceContext().getValue(CLASSIFICATIONATTRIBUTES, _classificationAttributes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationClass.cockpitItemTemplates</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cockpitItemTemplates
	 */
	@Accessor(qualifier = "cockpitItemTemplates", type = Accessor.Type.GETTER)
	public Set<CockpitItemTemplateModel> getCockpitItemTemplates()
	{
		if (this._cockpitItemTemplates!=null)
		{
			return _cockpitItemTemplates;
		}
		return _cockpitItemTemplates = getPersistenceContext().getValue(COCKPITITEMTEMPLATES, _cockpitItemTemplates);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationClass.declaredClassificationAttributeAssignments</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the declaredClassificationAttributeAssignments - list of attribute assignments
	 */
	@Accessor(qualifier = "declaredClassificationAttributeAssignments", type = Accessor.Type.GETTER)
	public List<ClassAttributeAssignmentModel> getDeclaredClassificationAttributeAssignments()
	{
		if (this._declaredClassificationAttributeAssignments!=null)
		{
			return _declaredClassificationAttributeAssignments;
		}
		return _declaredClassificationAttributeAssignments = getPersistenceContext().getValue(DECLAREDCLASSIFICATIONATTRIBUTEASSIGNMENTS, _declaredClassificationAttributeAssignments);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationClass.declaredClassificationAttributes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the declaredClassificationAttributes - list of assigned attributes
	 */
	@Accessor(qualifier = "declaredClassificationAttributes", type = Accessor.Type.GETTER)
	public List<ClassificationAttributeModel> getDeclaredClassificationAttributes()
	{
		if (this._declaredClassificationAttributes!=null)
		{
			return _declaredClassificationAttributes;
		}
		return _declaredClassificationAttributes = getPersistenceContext().getValue(DECLAREDCLASSIFICATIONATTRIBUTES, _declaredClassificationAttributes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationClass.externalID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the externalID - external identificator refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
	public String getExternalID()
	{
		if (this._externalID!=null)
		{
			return _externalID;
		}
		return _externalID = getPersistenceContext().getValue(EXTERNALID, _externalID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationClass.hmcXML</code> attribute defined at extension <code>catalog</code>. 
	 * @return the hmcXML - custom hmc.xml for this class
	 */
	@Accessor(qualifier = "hmcXML", type = Accessor.Type.GETTER)
	public String getHmcXML()
	{
		if (this._hmcXML!=null)
		{
			return _hmcXML;
		}
		return _hmcXML = getPersistenceContext().getValue(HMCXML, _hmcXML);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationClass.inheritedClassificationAttributes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the inheritedClassificationAttributes - list of assigned attributes
	 */
	@Accessor(qualifier = "inheritedClassificationAttributes", type = Accessor.Type.GETTER)
	public List<ClassificationAttributeModel> getInheritedClassificationAttributes()
	{
		if (this._inheritedClassificationAttributes!=null)
		{
			return _inheritedClassificationAttributes;
		}
		return _inheritedClassificationAttributes = getPersistenceContext().getValue(INHERITEDCLASSIFICATIONATTRIBUTES, _inheritedClassificationAttributes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationClass.revision</code> attribute defined at extension <code>catalog</code>. 
	 * @return the revision - revision field of this class
	 */
	@Accessor(qualifier = "revision", type = Accessor.Type.GETTER)
	public String getRevision()
	{
		if (this._revision!=null)
		{
			return _revision;
		}
		return _revision = getPersistenceContext().getValue(REVISION, _revision);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationClass.showEmptyAttributes</code> attribute defined at extension <code>catalog</code>. 
	 * @return the showEmptyAttributes - defines whether or not attributes without value are to be shown for a classified product
	 */
	@Accessor(qualifier = "showEmptyAttributes", type = Accessor.Type.GETTER)
	public Boolean getShowEmptyAttributes()
	{
		if (this._showEmptyAttributes!=null)
		{
			return _showEmptyAttributes;
		}
		return _showEmptyAttributes = getPersistenceContext().getValue(SHOWEMPTYATTRIBUTES, _showEmptyAttributes);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Category.catalogVersion</code> attribute defined at extension <code>catalog</code> and redeclared at extension <code>catalog</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel}.  
	 *  
	 * @param value the catalogVersion
	 */
	@Override
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		if( value == null || value instanceof ClassificationSystemVersionModel)
		{
			super.setCatalogVersion(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel");
		}
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationClass.cockpitItemTemplates</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitItemTemplates
	 */
	@Accessor(qualifier = "cockpitItemTemplates", type = Accessor.Type.SETTER)
	public void setCockpitItemTemplates(final Set<CockpitItemTemplateModel> value)
	{
		_cockpitItemTemplates = getPersistenceContext().setValue(COCKPITITEMTEMPLATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationClass.declaredClassificationAttributeAssignments</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the declaredClassificationAttributeAssignments - list of attribute assignments
	 */
	@Accessor(qualifier = "declaredClassificationAttributeAssignments", type = Accessor.Type.SETTER)
	public void setDeclaredClassificationAttributeAssignments(final List<ClassAttributeAssignmentModel> value)
	{
		_declaredClassificationAttributeAssignments = getPersistenceContext().setValue(DECLAREDCLASSIFICATIONATTRIBUTEASSIGNMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationClass.externalID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the externalID - external identificator refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
	public void setExternalID(final String value)
	{
		_externalID = getPersistenceContext().setValue(EXTERNALID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationClass.hmcXML</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the hmcXML - custom hmc.xml for this class
	 */
	@Accessor(qualifier = "hmcXML", type = Accessor.Type.SETTER)
	public void setHmcXML(final String value)
	{
		_hmcXML = getPersistenceContext().setValue(HMCXML, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationClass.revision</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the revision - revision field of this class
	 */
	@Accessor(qualifier = "revision", type = Accessor.Type.SETTER)
	public void setRevision(final String value)
	{
		_revision = getPersistenceContext().setValue(REVISION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationClass.showEmptyAttributes</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the showEmptyAttributes - defines whether or not attributes without value are to be shown for a classified product
	 */
	@Accessor(qualifier = "showEmptyAttributes", type = Accessor.Type.SETTER)
	public void setShowEmptyAttributes(final Boolean value)
	{
		_showEmptyAttributes = getPersistenceContext().setValue(SHOWEMPTYATTRIBUTES, value);
	}
	
}
