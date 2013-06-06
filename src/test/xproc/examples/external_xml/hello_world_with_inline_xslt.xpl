<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" version="1.0">
	
	<p:input port="source">
		<p:document href="../../../xml/examples/hello_world.xml"/>
	</p:input>
	
	<p:output port="result" sequence="true">
		<p:empty />
	</p:output>
	
	<p:xslt version="1.0" name="transform">
		<p:input port="stylesheet">
			<p:inline>
				<xsl:stylesheet 
					xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
					xmlns:xs="http://www.w3.org/2001/XMLSchema"
					exclude-result-prefixes="xs"
					version="1.0">
					
					<xsl:output method="xml" indent="yes" encoding="UTF-8" />
					
					<xsl:template match="/">
						<p>Hello world!</p>
					</xsl:template>
				</xsl:stylesheet>
			</p:inline>
		</p:input>
		
		<p:input port="parameters">
			<p:empty/>
		</p:input> 
		
	</p:xslt>
	
	<p:store omit-xml-declaration="false" encoding="utf-8" name="serialize">
		<p:with-option name="href" select="'output_inline_xslt.xml'"/>
	</p:store>     
		
</p:declare-step>