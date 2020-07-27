<?xml version="1.0" encoding="UTF-8"?>
<!--
    Document   : MyBookSchema.xsl
    Created on : January 22, 2012, 8:46 PM
    Author     : suitcase
    Description:
        Purpose of transformation follows.
-->
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:dc="http://purl.org/dc/elements/1.1/"
xmlns:mb="http://xml.netbeans.org/schema/MyBookSchema"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:dcterms="http://purl.org/dc/terms/">
<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
<xsl:template match="/mb:Collection/mb:books/mb:book">
    <xsl:element name="mb:Collection">
        <xsl:attribute name="xsi:schemaLocation">http://purl.org/dc/terms/ http://dublincore.org/schemas/xmls/qdc/dcterms.xsd</xsl:attribute> 
        <xsl:element name="mb:books">
            <xsl:element name="mb:book">                  
              <xsl:apply-templates select="./mb:metadata"/>         
            </xsl:element>
        </xsl:element>
    </xsl:element>
  </xsl:template>

<xsl:template match="/mb:Collection/mb:books/mb:book/mb:metadata">
    <xsl:apply-templates select="./mb:title"/>
    <xsl:apply-templates select="./mb:creator"/>
    <xsl:apply-templates select="./mb:description"/>
    <xsl:apply-templates select="./mb:publisher"/>
    <xsl:apply-templates select="./mb:type"/>
    <xsl:apply-templates select="./mb:format"/>
    <xsl:apply-templates select="./mb:isbn"/>
    <xsl:apply-templates select="./mb:rights"/>    
    <xsl:apply-templates select="./mb:copyrightDate"/>
    <xsl:apply-templates select="./mb:publicationDate"/>
    <xsl:apply-templates select="./mb:reprintingDate"/>
    <xsl:apply-templates select="./mb:publicationPlace"/>    
  </xsl:template>
  
  
  <!-- DC Element Instantiation -->
  <xsl:template match="/mb:Collection/mb:books/mb:book/mb:metadata/mb:isbn">
    <xsl:element name="dc:identifier">
      <xsl:apply-templates select="@xml:lang"/>
      <xsl:value-of select="."/>    
    </xsl:element>
  </xsl:template> 
  
  <xsl:template match="/mb:Collection/mb:books/mb:book/mb:metadata/mb:title">
    <xsl:element name="dc:title">
      <xsl:apply-templates select="@xml:lang"/>
      <xsl:value-of select="."/>    
    </xsl:element>
  </xsl:template>  
  
  <xsl:template match="/mb:Collection/mb:books/mb:book/mb:metadata/mb:creator">
    <xsl:element name="dc:creator">
      <xsl:apply-templates select="@xml:lang"/>
        <xsl:value-of select="."/>      
    </xsl:element>
  </xsl:template> 
  
  <xsl:template match="/mb:Collection/mb:books/mb:book/mb:metadata/mb:description">
    <xsl:element name="dc:description">
      <xsl:apply-templates select="@xml:lang"/>
      <xsl:value-of select="."/>    
    </xsl:element>
  </xsl:template> 
  
  <xsl:template match="/mb:Collection/mb:books/mb:book/mb:metadata/mb:publisher">
    <xsl:element name="dc:publisher">
      <xsl:apply-templates select="@xml:lang"/>
      <xsl:value-of select="."/>    
    </xsl:element>
  </xsl:template> 
  
  <xsl:template match="/mb:Collection/mb:books/mb:book/mb:metadata/mb:type">
    <xsl:element name="dc:type">
      <xsl:apply-templates select="@xml:lang"/>
      <xsl:value-of select="."/>    
    </xsl:element>
  </xsl:template> 
  
  <xsl:template match="/mb:Collection/mb:books/mb:book/mb:metadata/mb:format">
    <xsl:element name="dc:format">
      <xsl:apply-templates select="@xml:lang"/>
      <xsl:value-of select="."/>    
    </xsl:element>
  </xsl:template> 

  
  <xsl:template match="/mb:Collection/mb:books/mb:book/mb:metadata/mb:rights">
    <xsl:element name="dc:rights">
      <xsl:apply-templates select="@xml:lang"/>
         <xsl:value-of select="."/>    
    </xsl:element>
  </xsl:template> 

  <xsl:template match="/mb:Collection/mb:books/mb:book/mb:metadata/mb:publicationDate">
    <xsl:element name="dcterms:issued">
      <xsl:apply-templates select="@xml:lang"/>
      <xsl:value-of select="."/>    
    </xsl:element>
  </xsl:template> 
  
  <xsl:template match="/mb:Collection/mb:books/mb:book/mb:metadata/mb:publicationPlace">
    <xsl:element name="dcterms:spatial">
      <xsl:apply-templates select="@xml:lang"/>
      <xsl:value-of select="."/>    
    </xsl:element>
  </xsl:template>

  <xsl:template match="@xml:lang">
    <xsl:if test=".">
      <xsl:attribute name="xml:lang"><xsl:value-of select="."/></xsl:attribute>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>