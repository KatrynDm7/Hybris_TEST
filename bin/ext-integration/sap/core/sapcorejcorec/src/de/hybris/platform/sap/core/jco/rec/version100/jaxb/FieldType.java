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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FieldType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FieldType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="BCD"/>
 *     &lt;enumeration value="BYTE"/>
 *     &lt;enumeration value="CHAR"/>
 *     &lt;enumeration value="DATE"/>
 *     &lt;enumeration value="DECF16"/>
 *     &lt;enumeration value="DECF34"/>
 *     &lt;enumeration value="FLOAT"/>
 *     &lt;enumeration value="INT"/>
 *     &lt;enumeration value="INT1"/>
 *     &lt;enumeration value="INT2"/>
 *     &lt;enumeration value="NUM"/>
 *     &lt;enumeration value="STRING"/>
 *     &lt;enumeration value="TIME"/>
 *     &lt;enumeration value="XSTRING"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FieldType")
@XmlEnum
public enum FieldType {

    BCD("BCD"),
    BYTE("BYTE"),
    CHAR("CHAR"),
    DATE("DATE"),
    @XmlEnumValue("DECF16")
    DECF_16("DECF16"),
    @XmlEnumValue("DECF34")
    DECF_34("DECF34"),
    FLOAT("FLOAT"),
    INT("INT"),
    @XmlEnumValue("INT1")
    INT_1("INT1"),
    @XmlEnumValue("INT2")
    INT_2("INT2"),
    NUM("NUM"),
    STRING("STRING"),
    TIME("TIME"),
    XSTRING("XSTRING");
    private final String value;

    FieldType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FieldType fromValue(String v) {
        for (FieldType c: FieldType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
