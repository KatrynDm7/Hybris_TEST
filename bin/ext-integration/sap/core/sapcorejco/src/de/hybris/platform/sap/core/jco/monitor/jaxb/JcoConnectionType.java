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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for jcoConnectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="jcoConnectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="abapHost" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="abapClient" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="abapSystemNumber" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="abapLanguage" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="abapUser" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="applicationName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="connectionHandle" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="connectionType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="conversationId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dsrPassport" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="functionModuleName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="groupName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lastActivityTimestamp" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="lastActivityTimestampString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="protocol" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rfcDestinationName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sessionId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="state" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="stateString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="systemId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="threadId" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="threadName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "jcoConnectionType")
public class JcoConnectionType {

    @XmlAttribute(name = "abapHost")
    protected String abapHost;
    @XmlAttribute(name = "abapClient")
    protected String abapClient;
    @XmlAttribute(name = "abapSystemNumber")
    protected String abapSystemNumber;
    @XmlAttribute(name = "abapLanguage")
    protected String abapLanguage;
    @XmlAttribute(name = "abapUser")
    protected String abapUser;
    @XmlAttribute(name = "applicationName")
    protected String applicationName;
    @XmlAttribute(name = "connectionHandle")
    protected Long connectionHandle;
    @XmlAttribute(name = "connectionType")
    protected String connectionType;
    @XmlAttribute(name = "conversationId")
    protected String conversationId;
    @XmlAttribute(name = "dsrPassport")
    protected String dsrPassport;
    @XmlAttribute(name = "functionModuleName")
    protected String functionModuleName;
    @XmlAttribute(name = "groupName")
    protected String groupName;
    @XmlAttribute(name = "lastActivityTimestamp")
    protected Long lastActivityTimestamp;
    @XmlAttribute(name = "lastActivityTimestampString")
    protected String lastActivityTimestampString;
    @XmlAttribute(name = "protocol")
    protected String protocol;
    @XmlAttribute(name = "rfcDestinationName")
    protected String rfcDestinationName;
    @XmlAttribute(name = "sessionId")
    protected String sessionId;
    @XmlAttribute(name = "state")
    protected Integer state;
    @XmlAttribute(name = "stateString")
    protected String stateString;
    @XmlAttribute(name = "systemId")
    protected String systemId;
    @XmlAttribute(name = "threadId")
    protected Long threadId;
    @XmlAttribute(name = "threadName")
    protected String threadName;

    /**
     * Gets the value of the abapHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbapHost() {
        return abapHost;
    }

    /**
     * Sets the value of the abapHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbapHost(String value) {
        this.abapHost = value;
    }

    /**
     * Gets the value of the abapClient property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbapClient() {
        return abapClient;
    }

    /**
     * Sets the value of the abapClient property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbapClient(String value) {
        this.abapClient = value;
    }

    /**
     * Gets the value of the abapSystemNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbapSystemNumber() {
        return abapSystemNumber;
    }

    /**
     * Sets the value of the abapSystemNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbapSystemNumber(String value) {
        this.abapSystemNumber = value;
    }

    /**
     * Gets the value of the abapLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbapLanguage() {
        return abapLanguage;
    }

    /**
     * Sets the value of the abapLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbapLanguage(String value) {
        this.abapLanguage = value;
    }

    /**
     * Gets the value of the abapUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbapUser() {
        return abapUser;
    }

    /**
     * Sets the value of the abapUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbapUser(String value) {
        this.abapUser = value;
    }

    /**
     * Gets the value of the applicationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Sets the value of the applicationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationName(String value) {
        this.applicationName = value;
    }

    /**
     * Gets the value of the connectionHandle property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getConnectionHandle() {
        return connectionHandle;
    }

    /**
     * Sets the value of the connectionHandle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setConnectionHandle(Long value) {
        this.connectionHandle = value;
    }

    /**
     * Gets the value of the connectionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectionType() {
        return connectionType;
    }

    /**
     * Sets the value of the connectionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectionType(String value) {
        this.connectionType = value;
    }

    /**
     * Gets the value of the conversationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConversationId() {
        return conversationId;
    }

    /**
     * Sets the value of the conversationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConversationId(String value) {
        this.conversationId = value;
    }

    /**
     * Gets the value of the dsrPassport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDsrPassport() {
        return dsrPassport;
    }

    /**
     * Sets the value of the dsrPassport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDsrPassport(String value) {
        this.dsrPassport = value;
    }

    /**
     * Gets the value of the functionModuleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunctionModuleName() {
        return functionModuleName;
    }

    /**
     * Sets the value of the functionModuleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunctionModuleName(String value) {
        this.functionModuleName = value;
    }

    /**
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }

    /**
     * Gets the value of the lastActivityTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLastActivityTimestamp() {
        return lastActivityTimestamp;
    }

    /**
     * Sets the value of the lastActivityTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLastActivityTimestamp(Long value) {
        this.lastActivityTimestamp = value;
    }

    /**
     * Gets the value of the lastActivityTimestampString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastActivityTimestampString() {
        return lastActivityTimestampString;
    }

    /**
     * Sets the value of the lastActivityTimestampString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastActivityTimestampString(String value) {
        this.lastActivityTimestampString = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocol(String value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the rfcDestinationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcDestinationName() {
        return rfcDestinationName;
    }

    /**
     * Sets the value of the rfcDestinationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcDestinationName(String value) {
        this.rfcDestinationName = value;
    }

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setState(Integer value) {
        this.state = value;
    }

    /**
     * Gets the value of the stateString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateString() {
        return stateString;
    }

    /**
     * Sets the value of the stateString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateString(String value) {
        this.stateString = value;
    }

    /**
     * Gets the value of the systemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * Sets the value of the systemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemId(String value) {
        this.systemId = value;
    }

    /**
     * Gets the value of the threadId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getThreadId() {
        return threadId;
    }

    /**
     * Sets the value of the threadId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setThreadId(Long value) {
        this.threadId = value;
    }

    /**
     * Gets the value of the threadName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * Sets the value of the threadName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThreadName(String value) {
        this.threadName = value;
    }

}
