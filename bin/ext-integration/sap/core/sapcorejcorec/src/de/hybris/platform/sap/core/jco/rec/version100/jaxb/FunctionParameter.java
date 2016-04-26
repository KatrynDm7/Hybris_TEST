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

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import de.hybris.platform.sap.core.jco.rec.version100.SuperToString;


/**
 * <p>Java class for FunctionParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FunctionParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Import" type="{}ImportParameterList" minOccurs="0"/>
 *         &lt;element name="Export" type="{}ExportParameterList" minOccurs="0"/>
 *         &lt;element name="Changing" type="{}ChangingParameterList" minOccurs="0"/>
 *         &lt;element name="Tables" type="{}TableParameterList" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="functionName" use="required" type="{}FunctionType" />
 *       &lt;attribute name="executionOrder" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FunctionParameter", propOrder = {
    "_import",
    "export",
    "changing",
    "tables"
})
public class FunctionParameter
    extends SuperToString
{

    @XmlElement(name = "Import")
    protected ImportParameterList _import;
    @XmlElement(name = "Export")
    protected ExportParameterList export;
    @XmlElement(name = "Changing")
    protected ChangingParameterList changing;
    @XmlElement(name = "Tables")
    protected TableParameterList tables;
    @XmlAttribute(name = "functionName", required = true)
    protected String functionName;
    @XmlAttribute(name = "executionOrder", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger executionOrder;

    /**
     * Gets the value of the import property.
     * 
     * @return
     *     possible object is
     *     {@link ImportParameterList }
     *     
     */
    public ImportParameterList getImport() {
        return _import;
    }

    /**
     * Sets the value of the import property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImportParameterList }
     *     
     */
    public void setImport(ImportParameterList value) {
        this._import = value;
    }

    /**
     * Gets the value of the export property.
     * 
     * @return
     *     possible object is
     *     {@link ExportParameterList }
     *     
     */
    public ExportParameterList getExport() {
        return export;
    }

    /**
     * Sets the value of the export property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExportParameterList }
     *     
     */
    public void setExport(ExportParameterList value) {
        this.export = value;
    }

    /**
     * Gets the value of the changing property.
     * 
     * @return
     *     possible object is
     *     {@link ChangingParameterList }
     *     
     */
    public ChangingParameterList getChanging() {
        return changing;
    }

    /**
     * Sets the value of the changing property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChangingParameterList }
     *     
     */
    public void setChanging(ChangingParameterList value) {
        this.changing = value;
    }

    /**
     * Gets the value of the tables property.
     * 
     * @return
     *     possible object is
     *     {@link TableParameterList }
     *     
     */
    public TableParameterList getTables() {
        return tables;
    }

    /**
     * Sets the value of the tables property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableParameterList }
     *     
     */
    public void setTables(TableParameterList value) {
        this.tables = value;
    }

    /**
     * Gets the value of the functionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * Sets the value of the functionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunctionName(String value) {
        this.functionName = value;
    }

    /**
     * Gets the value of the executionOrder property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getExecutionOrder() {
        return executionOrder;
    }

    /**
     * Sets the value of the executionOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setExecutionOrder(BigInteger value) {
        this.executionOrder = value;
    }

}
