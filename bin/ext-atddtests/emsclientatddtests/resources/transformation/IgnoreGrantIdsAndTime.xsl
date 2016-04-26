<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>

	<xsl:template match="node() | @*">
  		<xsl:copy>
	        <xsl:apply-templates select="node() | @*" />
	    </xsl:copy>
	</xsl:template>

	<!-- userId is generated randomly -->
	<xsl:template match="userId">
		<userId/>
	</xsl:template>

	<xsl:template match="grantSource">
		<grantSource/>
	</xsl:template>

	<xsl:template match="grantSourceId">
		<grantSourceId/>
	</xsl:template>

	<!-- This is current time of grant creation, and can not be tested via xml comparator -->
    <xsl:template match="grantTime | startTime | endTime">
		<xsl:variable name="caption" select="name(.)"/>
		<xsl:element name="{$caption}"/>
    </xsl:template>

</xsl:stylesheet>