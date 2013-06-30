<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:c="http://www.w3.org/ns/xproc-step" xmlns:k="http://schema.kaikoda.com/ns/xproc" version="1.0">
    
    <p:input port="source" sequence="false" primary="true" />
    
    <p:output port="result" primary="true">
        <p:pipe step="transform" port="result" />
    </p:output>
    
    <p:xslt name="transform">
        
        <p:input port="stylesheet">
            <p:document href="../../xslt/copy_verbatim.xsl"/>
        </p:input>       
        
        <p:input port="parameters">
            <p:empty/>
        </p:input> 
        
    </p:xslt>
    
</p:declare-step>