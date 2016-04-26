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
package de.hybris.platform.sap.core.jco.monitor.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for nodeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nodeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="jcoConnections" type="{}jcoConnectionsType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="resultAvailable" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodeType", propOrder = {
    "jcoConnections"
})
public class NodeType {

    @XmlElement(required = true)
    protected JcoConnectionsType jcoConnections;
    @XmlAttribute(name = "id")
    protected Integer id;
    @XmlAttribute(name = "resultAvailable")
    protected Boolean resultAvailable;

    /**
     * Gets the value of the jcoConnections property.
     * 
     * @return
     *     possible object is
     *     {@link JcoConnectionsType }
     *     
     */
    public JcoConnectionsType getJcoConnections() {
        return jcoConnections;
    }

    /**
     * Sets the value of the jcoConnections property.
     * 
     * @param value
     *     allowed object is
     *     {@link JcoConnectionsType }
     *     
     */
    public void setJcoConnections(JcoConnectionsType value) {
        this.jcoConnections = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the resultAvailable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isResultAvailable() {
        return resultAvailable;
    }

    /**
     * Sets the value of the resultAvailable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setResultAvailable(Boolean value) {
        this.resultAvailable = value;
    }

}
