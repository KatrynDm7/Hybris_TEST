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
 * <p>Java class for DataStructure complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataStructure">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="DataElement" type="{}DataElement"/>
 *         &lt;element name="DataStructure" type="{}DataStructure"/>
 *         &lt;element name="DataTable" type="{}DataTable"/>
 *       &lt;/choice>
 *       &lt;attGroup ref="{}StructureInfo-optional"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataStructure", propOrder = {
    "dataElementOrDataStructureOrDataTable"
})
public class DataStructure
    extends SuperToString
{

    @XmlElements({
        @XmlElement(name = "DataElement", type = DataElement.class, nillable = true),
        @XmlElement(name = "DataStructure", type = DataStructure.class),
        @XmlElement(name = "DataTable", type = DataTable.class)
    })
    protected List<SuperToString> dataElementOrDataStructureOrDataTable;
    @XmlAttribute(name = "structureName")
    protected String structureName;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "description")
    protected String description;

    /**
     * Gets the value of the dataElementOrDataStructureOrDataTable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataElementOrDataStructureOrDataTable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataElementOrDataStructureOrDataTable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataElement }
     * {@link DataStructure }
     * {@link DataTable }
     * 
     * 
     */
    public List<SuperToString> getDataElementOrDataStructureOrDataTable() {
        if (dataElementOrDataStructureOrDataTable == null) {
            dataElementOrDataStructureOrDataTable = new ArrayList<SuperToString>();
        }
        return this.dataElementOrDataStructureOrDataTable;
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
