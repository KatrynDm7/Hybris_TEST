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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type PreviewTicket first defined at extension catalog.
 */
@SuppressWarnings("all")
public class PreviewTicketModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "PreviewTicket";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewTicket.previewCatalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String PREVIEWCATALOGVERSION = "previewCatalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewTicket.validTo</code> attribute defined at extension <code>catalog</code>. */
	public static final String VALIDTO = "validTo";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewTicket.createdBy</code> attribute defined at extension <code>catalog</code>. */
	public static final String CREATEDBY = "createdBy";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewTicket.ticketCode</code> attribute defined at extension <code>catalog</code>. */
	public static final String TICKETCODE = "ticketCode";
	
	
	/** <i>Generated variable</i> - Variable of <code>PreviewTicket.previewCatalogVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _previewCatalogVersion;
	
	/** <i>Generated variable</i> - Variable of <code>PreviewTicket.validTo</code> attribute defined at extension <code>catalog</code>. */
	private Date _validTo;
	
	/** <i>Generated variable</i> - Variable of <code>PreviewTicket.createdBy</code> attribute defined at extension <code>catalog</code>. */
	private UserModel _createdBy;
	
	/** <i>Generated variable</i> - Variable of <code>PreviewTicket.ticketCode</code> attribute defined at extension <code>catalog</code>. */
	private String _ticketCode;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PreviewTicketModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PreviewTicketModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _createdBy initial attribute declared by type <code>PreviewTicket</code> at extension <code>catalog</code>
	 * @param _previewCatalogVersion initial attribute declared by type <code>PreviewTicket</code> at extension <code>catalog</code>
	 * @param _validTo initial attribute declared by type <code>PreviewTicket</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public PreviewTicketModel(final UserModel _createdBy, final CatalogVersionModel _previewCatalogVersion, final Date _validTo)
	{
		super();
		setCreatedBy(_createdBy);
		setPreviewCatalogVersion(_previewCatalogVersion);
		setValidTo(_validTo);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _createdBy initial attribute declared by type <code>PreviewTicket</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _previewCatalogVersion initial attribute declared by type <code>PreviewTicket</code> at extension <code>catalog</code>
	 * @param _validTo initial attribute declared by type <code>PreviewTicket</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public PreviewTicketModel(final UserModel _createdBy, final ItemModel _owner, final CatalogVersionModel _previewCatalogVersion, final Date _validTo)
	{
		super();
		setCreatedBy(_createdBy);
		setOwner(_owner);
		setPreviewCatalogVersion(_previewCatalogVersion);
		setValidTo(_validTo);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewTicket.createdBy</code> attribute defined at extension <code>catalog</code>. 
	 * @return the createdBy
	 */
	@Accessor(qualifier = "createdBy", type = Accessor.Type.GETTER)
	public UserModel getCreatedBy()
	{
		if (this._createdBy!=null)
		{
			return _createdBy;
		}
		return _createdBy = getPersistenceContext().getValue(CREATEDBY, _createdBy);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewTicket.previewCatalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the previewCatalogVersion
	 */
	@Accessor(qualifier = "previewCatalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getPreviewCatalogVersion()
	{
		if (this._previewCatalogVersion!=null)
		{
			return _previewCatalogVersion;
		}
		return _previewCatalogVersion = getPersistenceContext().getValue(PREVIEWCATALOGVERSION, _previewCatalogVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewTicket.ticketCode</code> attribute defined at extension <code>catalog</code>. 
	 * @return the ticketCode
	 */
	@Accessor(qualifier = "ticketCode", type = Accessor.Type.GETTER)
	public String getTicketCode()
	{
		if (this._ticketCode!=null)
		{
			return _ticketCode;
		}
		return _ticketCode = getPersistenceContext().getValue(TICKETCODE, _ticketCode);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewTicket.validTo</code> attribute defined at extension <code>catalog</code>. 
	 * @return the validTo
	 */
	@Accessor(qualifier = "validTo", type = Accessor.Type.GETTER)
	public Date getValidTo()
	{
		if (this._validTo!=null)
		{
			return _validTo;
		}
		return _validTo = getPersistenceContext().getValue(VALIDTO, _validTo);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>PreviewTicket.createdBy</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the createdBy
	 */
	@Accessor(qualifier = "createdBy", type = Accessor.Type.SETTER)
	public void setCreatedBy(final UserModel value)
	{
		_createdBy = getPersistenceContext().setValue(CREATEDBY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>PreviewTicket.previewCatalogVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the previewCatalogVersion
	 */
	@Accessor(qualifier = "previewCatalogVersion", type = Accessor.Type.SETTER)
	public void setPreviewCatalogVersion(final CatalogVersionModel value)
	{
		_previewCatalogVersion = getPersistenceContext().setValue(PREVIEWCATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewTicket.validTo</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the validTo
	 */
	@Accessor(qualifier = "validTo", type = Accessor.Type.SETTER)
	public void setValidTo(final Date value)
	{
		_validTo = getPersistenceContext().setValue(VALIDTO, value);
	}
	
}
