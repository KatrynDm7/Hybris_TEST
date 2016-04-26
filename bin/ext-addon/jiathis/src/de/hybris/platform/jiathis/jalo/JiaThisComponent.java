package de.hybris.platform.jiathis.jalo;

import de.hybris.platform.chinaaccelerator.social.jiathis.jalo.GeneratedJiaThisComponent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

import org.apache.log4j.Logger;

public class JiaThisComponent extends GeneratedJiaThisComponent
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger( JiaThisComponent.class.getName() );
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem( ctx, type, allAttributes );
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}
	
}
