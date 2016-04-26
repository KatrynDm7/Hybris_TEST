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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import de.hybris.platform.sap.core.jco.rec.version100.SuperToString;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RepositoryVersion" type="{}RepositoryVersion"/>
 *         &lt;element name="Functions" type="{}FunctionList" minOccurs="0"/>
 *         &lt;element name="Records" type="{}Records" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "repositoryVersion",
    "functions",
    "records"
})
@XmlRootElement(name = "Repository")
public class Repository
    extends SuperToString
{

    @XmlElement(name = "RepositoryVersion", required = true)
    protected String repositoryVersion;
    @XmlElement(name = "Functions")
    protected FunctionList functions;
    @XmlElement(name = "Records")
    protected Records records;

    /**
     * Gets the value of the repositoryVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepositoryVersion() {
        return repositoryVersion;
    }

    /**
     * Sets the value of the repositoryVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepositoryVersion(String value) {
        this.repositoryVersion = value;
    }

    /**
     * Gets the value of the functions property.
     * 
     * @return
     *     possible object is
     *     {@link FunctionList }
     *     
     */
    public FunctionList getFunctions() {
        return functions;
    }

    /**
     * Sets the value of the functions property.
     * 
     * @param value
     *     allowed object is
     *     {@link FunctionList }
     *     
     */
    public void setFunctions(FunctionList value) {
        this.functions = value;
    }

    /**
     * Gets the value of the records property.
     * 
     * @return
     *     possible object is
     *     {@link Records }
     *     
     */
    public Records getRecords() {
        return records;
    }

    /**
     * Sets the value of the records property.
     * 
     * @param value
     *     allowed object is
     *     {@link Records }
     *     
     */
    public void setRecords(Records value) {
        this.records = value;
    }

}
