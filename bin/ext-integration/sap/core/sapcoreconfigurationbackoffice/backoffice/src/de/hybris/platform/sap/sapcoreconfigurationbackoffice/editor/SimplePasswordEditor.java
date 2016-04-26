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
package de.hybris.platform.sap.sapcoreconfigurationbackoffice.editor;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.hybris.cockpitng.editor.defaultpassword.DefaultPasswordEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.util.YTestTools;

public class SimplePasswordEditor extends DefaultPasswordEditor {

	@Override
	public void render(final Component parent,
			final EditorContext<String> context,
			final EditorListener<String> listener) {

		
		final Div divContainer = new Div();

		final Vlayout vlayout = new Vlayout();
		divContainer.appendChild(vlayout);
	
		
		final Popup passwordHint = new Popup();
		
		passwordHint.appendChild(new Label(getL10nDecorator(context, HINT_LABEL, HINT_LABEL)));

		final Textbox password = new Textbox();

		password.setType(PASSWORD_TYPE);
		
		char mask = '\u00B7';
 		
 		if (context.getInitialValue() != null){
 			
 	     String  passwordMask = StringUtils.repeat(mask, context.getInitialValue().length());
 			password.setPlaceholder(passwordMask);
 
     	}		
		
		password.setTooltip(passwordHint);
	
		password.setInstant(false);

		vlayout.appendChild(password);
		
		password.addEventListener(Events.ON_CHANGE,
				new EventListener<InputEvent>() {
					@Override
					public void onEvent(final InputEvent event) {
						
						SimplePasswordEditor.this.action(password, listener);
						
					}
				});

		divContainer.setParent(parent);
	}

	protected void action(Textbox password, EditorListener<String> listener) {
		  listener.onValueChanged(password.getValue());
	}

}