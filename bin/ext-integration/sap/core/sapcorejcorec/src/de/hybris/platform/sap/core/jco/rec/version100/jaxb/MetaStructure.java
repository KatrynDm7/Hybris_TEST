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
package de.hybris.platform.sap.core.jco.rec.version100.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import de.hybris.platform.sap.core.jco.rec.version100.SuperToString;


/**
 * <p>Java class for MetaStructure complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MetaStructure">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="ElementMetaData" type="{}MetaElement"/>
 *         &lt;element name="StructureMetaData" type="{}MetaStructure"/>
 *         &lt;element name="TableMetaData" type="{}MetaTable"/>
 *       &lt;/choice>
 *       &lt;attGroup ref="{}StructureInfo"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetaStructure", propOrder = {
    "elementMetaDataOrStructureMetaDataOrTableMetaData"
})
public class MetaStructure
    extends SuperToString
{

    @XmlElements({
        @XmlElement(name = "ElementMetaData", type = MetaElement.class),
        @XmlElement(name = "StructureMetaData", type = MetaStructure.class),
        @XmlElement(name = "TableMetaData", type = MetaTable.class)
    })
    protected List<SuperToString> elementMetaDataOrStructureMetaDataOrTableMetaData;
    @XmlAttribute(name = "structureName")
    protected String structureName;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "description")
    protected String description;

    /**
     * Gets the value of the elementMetaDataOrStructureMetaDataOrTableMetaData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elementMetaDataOrStructureMetaDataOrTableMetaData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElementMetaDataOrStructureMetaDataOrTableMetaData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetaElement }
     * {@link MetaStructure }
     * {@link MetaTable }
     * 
     * 
     */
    public List<SuperToString> getElementMetaDataOrStructureMetaDataOrTableMetaData() {
        if (elementMetaDataOrStructureMetaDataOrTableMetaData == null) {
            elementMetaDataOrStructureMetaDataOrTableMetaData = new ArrayList<SuperToString>();
        }
        return this.elementMetaDataOrStructureMetaDataOrTableMetaData;
    }

    /**
     * Gets the value of the structureName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStructureName() {
        return structureName;
    }

    /**
     * Sets the value of the structureName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStructureName(String value) {
        this.structureName = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
