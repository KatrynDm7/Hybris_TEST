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
package com.sap.hybris.reco.cockpit.editor;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractTextBasedUIEditor;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.Registry;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import com.sap.hybris.reco.bo.SAPRecommendationItemDSTypeReader;
import com.sap.hybris.reco.dao.SAPRecommendationItemDataSourceType;


/**
 * Simple text editor.
 */
public class DefaultSAPRecommendationItemDSUIEditor extends AbstractTextBasedUIEditor
{
	private static final Logger LOG = Logger.getLogger(DefaultSAPRecommendationItemDSUIEditor.class);
	private static final String ENUM_EDITOR_SCLASS = "enumEditor";

	private List<? extends Object> availableValues;
	private List<? extends Object> originalAvailableValues;
	private final Combobox editorView = new Combobox();
	private String searchString = "";
	private SAPRecommendationItemDSTypeReader recommendationItemDSReader;

	private String lastSearch;

	protected Comboitem addModelToCombo(final SAPRecommendationItemDataSourceType itemDSType, final Combobox box)
	{
		final String value = itemDSType.getId();
		final String label = itemDSType.getDescription();
		final Comboitem comboitem = new Comboitem();

		comboitem.setLabel(label);
		comboitem.setValue(value);
		box.appendChild(comboitem);
		return comboitem;
	}


	protected int getPosition(final Object item)
	{
		int index = -1;
		if (availableValues != null)
		{
			index = getAvailableValues().indexOf(item);
		}
		return index;
	}

	protected void setEnumValue(final Combobox combo, final Object value)
	{
		final int index = getPosition(value);
		if (index >= 0)
		{
			combo.setSelectedIndex(index);
		}
	}

	@Override
	public HtmlBasedComponent createViewComponent(final Object initialValue, final Map<String, ? extends Object> parameters,
			final EditorListener listener)
	{
		
		parseInitialInputString(parameters);
		SAPRecommendationItemDataSourceType itemDSType = null;

		editorView.setConstraint("strict");
		editorView.setAutodrop(true);

		final String intialValueString = (String) initialValue;
		if (intialValueString != null && !intialValueString.isEmpty())
		{
			itemDSType = new SAPRecommendationItemDataSourceType();
			itemDSType.setId(intialValueString);
		}

		if (isEditable())
		{
			try
			{
				fillComboBox(getItemDSTypeReader().getAllItemDSTypes());
			}
			catch (ODataException | URISyntaxException | IOException | IllegalArgumentException e){
				LOG.error("Error filling item datasource types dropdown", e);
				showMessagePopup(e);
			}

			if (itemDSType != null)
			{
				setEnumValue(editorView, itemDSType.getId());
			}

			final CancelButtonContainer ret = new CancelButtonContainer(listener, new CancelListener()
			{
				@Override
				public void cancelPressed()
				{
					setEnumValue(editorView, initialEditValue);
					setValue(initialEditValue);
					fireValueChanged(listener);
					listener.actionPerformed(EditorListener.ESCAPE_PRESSED);
				}
			});

			ret.setSclass(ENUM_EDITOR_SCLASS);
			ret.setContent(editorView);


			editorView.addEventListener(Events.ON_FOCUS, new EventListener()
			{

				@Override
				public void onEvent(final Event event) throws Exception
				{
					if (editorView.getSelectedItem() != null)
					{
						initialEditValue = editorView.getSelectedItem().getValue();
					}
					ret.showButton(Boolean.TRUE.booleanValue());
				}
			});

			editorView.addEventListener(Events.ON_CHANGE, new EventListener()
			{
				@Override
				public void onEvent(final Event arg0) throws Exception // NOPMD zk specific
				{
					validateAndFireEvent(listener);
				}
			});

			editorView.addEventListener(Events.ON_BLUR, new EventListener()
			{
				@Override
				public void onEvent(final Event arg0) throws Exception // NOPMD zk specific
				{
					ret.showButton(Boolean.FALSE.booleanValue());
				}
			});
			editorView.addEventListener(Events.ON_OK, new EventListener()
			{
				@Override
				public void onEvent(final Event arg0) throws Exception // NOPMD zk specific
				{
					validateAndFireEvent(listener);
					listener.actionPerformed(EditorListener.ENTER_PRESSED);
				}
			});
			editorView.addEventListener(Events.ON_CANCEL, new EventListener()
			{
				@Override
				public void onEvent(final Event arg0) throws Exception // NOPMD zk specific
				{
					ret.showButton(Boolean.FALSE.booleanValue());
					DefaultSAPRecommendationItemDSUIEditor.this.setEnumValue(editorView, initialEditValue);
					setValue(initialEditValue);
					fireValueChanged(listener);
					listener.actionPerformed(EditorListener.ESCAPE_PRESSED);
				}
			});

			if (UISessionUtils.getCurrentSession().isUsingTestIDs())
			{
				String id = "Enum_";
				String attributeQualifier = (String) parameters.get(AbstractUIEditor.ATTRIBUTE_QUALIFIER_PARAM);
				if (attributeQualifier != null)
				{
					attributeQualifier = attributeQualifier.replaceAll("\\W", "");
					id = id + attributeQualifier;
				}
				UITools.applyTestID(editorView, id);
			}

			return ret;
		}
		else
		{
			editorView.setDisabled(true);

			final Label ret;
			if (itemDSType != null)
			{
				ret = new Label(itemDSType.getId() + " " + itemDSType.getDescription());
			}
			else
			{
				ret = new Label(" ");
			}
			return ret;
		}


	}

	@Override
	public boolean isInline()
	{
		return true;
	}


	@Override
	public String getEditorType()
	{
		return PropertyDescriptor.TEXT;
	}

	/**
	 * @return availableValues
	 */
	public List<? extends Object> getAvailableValues()
	{
		return this.availableValues;
	}

	@Override
	public boolean isOptional()
	{
		return false;
	}

	/**
	 * @param availableValues
	 */
	public void setAvailableValues(final List<? extends Object> availableValues)
	{
		if (availableValues == null || availableValues.isEmpty())
		{
			editorView.setValue(Labels.getLabel("general.nothingtodisplay"));
			editorView.setDisabled(true);
			this.availableValues = null;
			this.originalAvailableValues = null;
		}
		else
		{
			this.availableValues = new ArrayList<Object>(availableValues);
			if (isOptional())
			{
				this.availableValues.add(0, null);
			}
			this.originalAvailableValues = new ArrayList<Object>(availableValues);
		}
	}

	@Override
	public void setFocus(final HtmlBasedComponent rootEditorComponent, final boolean selectAll)
	{
		final Combobox element = (Combobox) ((CancelButtonContainer) rootEditorComponent).getContent();
		element.setFocus(true);

		if (initialInputString != null)
		{
			element.setText(initialInputString);
		}
	}

	@Override
	public void setOptional(final boolean optional)
	{
		if (!optional)
		{
			availableValues = originalAvailableValues;
		}
		super.setOptional(optional);
	}

	protected void validateAndFireEvent(final EditorListener listener)
	{
		if (editorView.getSelectedItem() == null)
		{
			setEnumValue(editorView, initialEditValue);
		}
		else
		{
			DefaultSAPRecommendationItemDSUIEditor.this.setValue(editorView.getSelectedItem().getValue());
			listener.valueChanged(getValue());
		}
	}

	protected void handleChangingEvents(final EditorListener listener, final Event event)
	{
		final String newSearchString = ((InputEvent) event).getValue();
		lastSearch = newSearchString;
		LOG.debug("Event raise for: " + newSearchString + newSearchString.length());
		if (newSearchString.length() >= 4 && !searchString.equals(newSearchString))
		{
			final List<SAPRecommendationItemDataSourceType> itemDataSourceTypes = searchValues(newSearchString);
			synchronized (this)
			{
				if (newSearchString.equals(lastSearch))
				{
					LOG.debug("String used for display: " + newSearchString);
					searchString = lastSearch;
					clearComboBox();
					fillComboBox(itemDataSourceTypes);
					listener.valueChanged(getValue());
				}
			}
		}
	}

	/**
	 * 
	 */
	protected List<SAPRecommendationItemDataSourceType> searchValues(final String newSearchString)
	{
		final List<SAPRecommendationItemDataSourceType> itemDataSourceTypes = new ArrayList<SAPRecommendationItemDataSourceType>();
		// searching to be implemented
		return itemDataSourceTypes;

	}

	/**
	 * 
	 */
	protected void fillComboBox(final List<SAPRecommendationItemDataSourceType> itemDataSourceTypes)
	{
		clearComboBox();
		final List<String> values = new ArrayList<String>();
		for (final SAPRecommendationItemDataSourceType itemDataSourceType : itemDataSourceTypes)
		{
			addModelToCombo(itemDataSourceType, editorView);
			values.add(itemDataSourceType.getId());
		}
		setAvailableValues(values);

	}

	/**
	 * 
	 */
	protected SAPRecommendationItemDSTypeReader getItemDSTypeReader()
	{
		if (recommendationItemDSReader != null)
		{
			return recommendationItemDSReader;
		}
		return recommendationItemDSReader = (SAPRecommendationItemDSTypeReader) Registry.getApplicationContext().getBean(
				"sapRecommendationItemDSTypeReader");
	}

	/**
	 * 
	 */
	protected void clearComboBox()
	{
		final int size = editorView.getChildren().size();
		for (int i = 0; i < size; i++)
		{
			editorView.removeItemAt(0);
		}
	}

	protected Combobox getEditorView()
	{
		return editorView;
	}
	
	private void showMessagePopup(Exception e){
		try
		{
			final String exceptionText = de.hybris.platform.util.localization.Localization
					.getLocalizedString("connectionError.description");
			final String exceptionTitle = de.hybris.platform.util.localization.Localization.getLocalizedString("connectionError.title");

			Messagebox.show(exceptionText, exceptionTitle, Messagebox.OK, Messagebox.ERROR);
		}
		catch (final InterruptedException e1)
		{
			LOG.error("Messagebox Exception", e1);
		}
	}

}
