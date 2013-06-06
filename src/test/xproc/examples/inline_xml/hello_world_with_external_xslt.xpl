<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" name="hello-world-transformation" version="1.0">
	
	<p:input port="source" primary="true">
		<p:inline>
			<doc>Hello world!</doc>			
		</p:inline>		
	</p:input>
	
	<p:output port="result" sequence="true">
		<p:empty />
	</p:output>

	<p:xslt version="1.0" name="transform">
		<p:input port="stylesheet">
			<p:document href="../../../xslt/examples/hello_world.xsl"/>
		</p:input>
		
		<p:input port="parameters">
			<p:empty/>
		</p:input> 
		
	</p:xslt>

	<p:store omit-xml-declaration="false" encoding="utf-8" name="serialize">
		<p:with-option name="href" select="'output_external_xslt.xml'"/>
	</p:store>     

</p:declare-step>