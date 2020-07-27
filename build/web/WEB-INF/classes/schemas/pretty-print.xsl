<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
                    
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>

  
  <xsl:template match="@* | text() | processing-instruction() | comment()">
    <xsl:copy/>
  </xsl:template>

  <xsl:template match="*">
    <xsl:copy copy-namespaces="no">
      <xsl:for-each select="descendant-or-self::*/namespace::*">
        <xsl:copy-of select="."/>
      </xsl:for-each>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>
  
</xsl:stylesheet>
