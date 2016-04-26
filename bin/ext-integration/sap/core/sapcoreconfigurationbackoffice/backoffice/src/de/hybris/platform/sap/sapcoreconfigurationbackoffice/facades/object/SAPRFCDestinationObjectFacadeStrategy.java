/*
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
 *
 */
package de.hybris.platform.sap.sapcoreconfigurationbackoffice.facades.object;

 
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.DefaultPlatformObjectFacadeStrategy;
import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.ItemModificationHistoryService;
import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.ItemModificationInfo;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectDeletionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.sap.core.configuration.enums.JCoTraceLevel;
import de.hybris.platform.sap.core.configuration.model.SAPRFCDestinationModel;
import de.hybris.platform.sap.core.configuration.rfc.event.SAPRFCDestinationEvent;
import de.hybris.platform.sap.core.configuration.rfc.event.SAPRFCDestinationRemoveEvent;
import de.hybris.platform.sap.core.configuration.rfc.event.SAPRFCDestinationJCoTraceEvent;
import de.hybris.platform.sap.core.configuration.rfc.event.SAPRFCDestinationUpdateEvent;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

public class SAPRFCDestinationObjectFacadeStrategy  extends DefaultPlatformObjectFacadeStrategy {

	 private ItemModificationHistoryService itemModificationHistoryService;
	 private ModelService modelService;
	 private EventService eventService;
	
	 private static final String ITEM_SAVED_MSG = "user.notification.item.saved";
	private static final String ITEM_CREATED_MSG = "user.notification.item.created";
	
	public static final String JCO_TRACE_LEVEL = "jcoTraceLevel";
	public static final String JCO_TRACE_PATH = "jcoTracePath";

	@Override
	public boolean canHandle(final Object object){
		
		return object instanceof SAPRFCDestinationModel;
		
	}

	@Override
	public <T> T save(final T objectToSave, final Context ctx) throws ObjectSavingException{
	
		try
		{
			String message = ITEM_SAVED_MSG;
			if (modelService.isNew(objectToSave))
			{
				message = ITEM_CREATED_MSG;
			}
			final ItemModificationInfo modificationInfo = itemModificationHistoryService
					.createModificationInfo((ItemModel) objectToSave);
			
	
			modelService.save(objectToSave);
			
			final SAPRFCDestinationModel destination = (SAPRFCDestinationModel) objectToSave;
			
		 	
		     Set<String> modifiedAttributes = modificationInfo.getModifiedAttributes();
		     
		     
		     if ((modifiedAttributes.size() >2 ) || 
		    		 ((modifiedAttributes.size() <= 2) && (!modifiedAttributes.contains(JCO_TRACE_LEVEL)) && (!modifiedAttributes.contains(JCO_TRACE_PATH)) )){
		    	 
		    	 eventService.publishEvent(new SAPRFCDestinationUpdateEvent(destination.getRfcDestinationName()));
		    	 
		     }else {
		    	 if (modifiedAttributes.contains(JCO_TRACE_LEVEL)){
		    		
		    		JCoTraceLevel  jcoTraceLevel = (JCoTraceLevel) modificationInfo.getModifiedValue(JCO_TRACE_LEVEL);
		    		
		    		final SAPRFCDestinationJCoTraceEvent event = new SAPRFCDestinationJCoTraceEvent(jcoTraceLevel.toString());
		    		
		    		if (modifiedAttributes.contains(JCO_TRACE_PATH)){
		    			String path = (String) modificationInfo.getModifiedValue(JCO_TRACE_PATH);
		    			event.setJCoTracePath(path);
		    		}
		    		
		    		eventService.publishEvent(event);
		    	 }
		   
		     }
		     
			NotificationUtils.notifyUser(Labels.getLabel(message, new Object[] { getLabelService().getObjectLabel(objectToSave) }),
					NotificationEvent.Type.SUCCESS, NotificationEvent.Behavior.STICKY);
			
			itemModificationHistoryService.logItemModification(modificationInfo);
			
			return objectToSave;
		}
		catch (final ModelSavingException ex)
		{
			throw new ObjectSavingException(getObjectId(objectToSave), ex);
		}
	
	}
				 
	@Override
	public <T> void delete(final T object, final Context ctx) throws ObjectDeletionException
	{
		super.delete(object, ctx);
		
		SAPRFCDestinationModel desitnation = (SAPRFCDestinationModel) object;
		
		eventService.publishEvent(new SAPRFCDestinationRemoveEvent(desitnation.getRfcDestinationName()));
		
	}
	
	
	@Required
    @Override
    public void setItemModificationHistoryService(final ItemModificationHistoryService itemModificationHistoryService) {
		
        super.setItemModificationHistoryService(itemModificationHistoryService);
        this.itemModificationHistoryService = itemModificationHistoryService;
    }
	
	@Required
	public void setEventService(final EventService eventService){
		this.eventService = eventService;
		
	}
	
	@Required
	public void setModelService(final ModelService modelService){
		
		super.setModelService(modelService);
		this.modelService = modelService;
		
	}
}
	
	

