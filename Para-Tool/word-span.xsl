<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:idx="http://www.w3.org/2001/XMLSchema-instance"
 exclude-result-prefixes="idx" xmlns:xlink="http://www.w3.org/1999/xlink">
  <!-- <xsl:output method="html" indent="yes"/>-->
 <xsl:output method="xml" encoding="utf-8" omit-xml-declaration="yes" indent="no"/>
 <xsl:strip-space elements="*"/>
<xsl:variable name="i" select="0" />
   <xsl:template match="@*|node()">
      <xsl:copy>
         <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
   </xsl:template>

   <!-- Don't split the words in the title -->
   <xsl:template match="p[@class='page_even']">
      <xsl:copy-of select="." />
   </xsl:template>
      <xsl:template match="head">
      <xsl:copy-of select="." />
   </xsl:template>
   
      <xsl:template match="title">
      <xsl:copy-of select="." />
   </xsl:template>
   <xsl:template match="p[@class='page_odd']">
      <xsl:copy-of select="." />
   </xsl:template>
    <xsl:template match="span[@class='pronunciation']">
      <xsl:copy-of select="." />
   </xsl:template>
   
  <!--  <xsl:template match="span[@class='comment_class']/span[@class='draft-comment']">
   </xsl:template> -->
   <!-- Matches a text element. Given a name so it can be recursively called -->
   <xsl:template match="text()" name="wrapper">
      <xsl:param name="text" select="." />
      <!-- <xsl:variable name="new" select="normalize-space($text)" /> -->
      <xsl:variable name="new" select="$text" />
      <xsl:choose>
         <xsl:when test="contains($new, ' ')">

		 	<xsl:element name="span">	 
		 <xsl:attribute name="id">word<xsl:value-of select="$i+1" /></xsl:attribute>
		<xsl:attribute name="name"><xsl:value-of select="concat(substring-before($new, ' '), ' ')" /></xsl:attribute>
           <xsl:value-of select="concat(substring-before($new, ' '), ' ')" />
			</xsl:element>
			
          <!--  <span><xsl:value-of select="concat(substring-before($new, ' '), ' ')" /></span>-->
            <xsl:call-template name="wrapper">
               <xsl:with-param name="text" select="substring-after($new, ' ')" />
            </xsl:call-template>
         </xsl:when>
         <xsl:otherwise>
		 <xsl:element name="span">
		 <xsl:attribute name="id">word<xsl:value-of select="$i+1" /></xsl:attribute>
		 <xsl:attribute name="name"><xsl:value-of select="$new" /></xsl:attribute>
            <xsl:value-of select="$new" />
			</xsl:element>
         </xsl:otherwise>
      </xsl:choose>

   </xsl:template>
   
   <xsl:template name="loop">
    <xsl:param name="i"/>
          <div>
        <xsl:value-of select="$i+1"/>
		
      </div>

  </xsl:template>
   
   
</xsl:stylesheet>