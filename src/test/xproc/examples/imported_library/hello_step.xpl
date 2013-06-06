<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:k="http://schema.kaikoda.com/ns/xproc" type="k:hello" version="1.0">
    
    <p:input port="source">
        <p:inline>
            <p:document />
        </p:inline>
    </p:input>
    
    <p:output port="result" />  
    
    <p:xslt version="1.0" name="transform">            
        
        <p:input port="stylesheet">
            <p:document href="../../../xslt/examples/hello.xsl"/>
        </p:input>
        
        <p:input port="parameters">
            <p:empty/>
        </p:input> 
        
    </p:xslt>
    
    <p:identity name="hello" />
    
</p:declare-step>