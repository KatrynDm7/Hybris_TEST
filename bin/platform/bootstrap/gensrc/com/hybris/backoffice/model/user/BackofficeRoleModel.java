/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 18.04.2016 18:26:54                         ---
 * ----------------------------------------------------------------
 */
package com.hybris.backoffice.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type BackofficeRole first defined at extension backoffice.
 */
@SuppressWarnings("all")
public class BackofficeRoleModel extends UserGroupModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "BackofficeRole";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeRole.authorities</code> attribute defined at extension <code>backoffice</code>. */
	public static final String AUTHORITIES = "authorities";
	
	
	/** <i>Generated variable</i> - Variable of <code>BackofficeRole.authorities</code> attribute defined at extension <code>backoffice</code>. */
	private Collection<String> _authorities;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BackofficeRoleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BackofficeRoleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public BackofficeRoleModel(final String _uid)
	{
		super();
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public BackofficeRoleModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeRole.authorities</code> attribute defined at extension <code>backoffice</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the authorities
	 */
	@Accessor(qualifier = "authorities", type = Accessor.Type.GETTER)
	public Collection<String> getAuthorities()
	{
		if (this._authorities!=null)
		{
			return _authorities;
		}
		return _authorities = getPersistenceContext().getValue(AUTHORITIES, _authorities);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeRole.authorities</code> attribute defined at extension <code>backoffice</code>. 
	 *  
	 * @param value the authorities
	 */
	@Accessor(qualifier = "authorities", type = Accessor.Type.SETTER)
	public void setAuthorities(final Collection<String> value)
	{
		_authorities = getPersistenceContext().setValue(AUTHORITIES, value);
	}
	
}
