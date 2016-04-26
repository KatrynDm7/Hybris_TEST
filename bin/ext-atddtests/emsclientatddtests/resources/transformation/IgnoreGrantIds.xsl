<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template match="node() | @*">
  		<xsl:copy>
	        <xsl:apply-templates select="node() | @*" />
	    </xsl:copy>
	</xsl:template>

	<xsl:template match="userId">
		<userId/>
	</xsl:template>
	<xsl:template match="grantSource">
		<grantSource/>
	</xsl:template>
	<xsl:template match="grantSourceId">
		<grantSourceId/>
	</xsl:template>

	<!-- When multiple fields have the same value, XML serializer stores the value only once and then references
		 on the entry like this:
		 <entitlement>
		 	<startTime>2014-09-14 12:50:10.0 UTC<startTime>
		 	<grantTime reference="../startTime"/>
		 </entitlement>
		 That is not suitable for our case, so these places are being unreferenced by this template -->
	<xsl:template match="*[@reference]">
		<xsl:variable name="caption" select="name(.)"/>
		<xsl:variable name="path" select="@reference"/>
		<xsl:element name="{$caption}">
			<xsl:call-template name="getNodeValue">
				<xsl:with-param name="pExpression" select="$path"/>
			</xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="getNodeValue">
		<xsl:param name="pExpression" select="string(.)"/>
		<xsl:param name="pCurrentNode" select="."/>
		<xsl:choose>
			<xsl:when test="not(contains($pExpression, '/'))">
				<xsl:element name="$pCurrentNode">
					<xsl:value-of select="$pCurrentNode/*[name()=$pExpression]"/>
				</xsl:element>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="node" select="substring-before($pExpression, '/')"/>
				<xsl:choose>
					<xsl:when test="$node='..'">
						<xsl:call-template name="getNodeValue">
							<xsl:with-param name="pExpression" select="substring-after($pExpression, '/')"/>
							<xsl:with-param name="pCurrentNode" select="$pCurrentNode/.."/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="getNodeValue">
							<xsl:with-param name="pExpression"
								select="substring-after($pExpression, '/')"/>
							<xsl:with-param name="pCurrentNode" select=
								"$pCurrentNode/*[name()=substring-before($pExpression, '/')]"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>