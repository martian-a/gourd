<?xml version="1.0" encoding="UTF-8"?>
<p:library xmlns:p="http://www.w3.org/ns/xproc" xmlns:k="http://schema.kaikoda.com/ns/xproc" version="1.0">
    
    <p:declare-step type="k:hello">
        
        <p:input port="source" />
        
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
    
    
    <p:declare-step type="k:world">        
        
        <p:input port="source" />
        
        <p:output port="result" />
        
        <p:xslt version="1.0" name="transform">            
            
            <p:input port="stylesheet">
                <p:document href="../../../xslt/examples/world.xsl"/>
            </p:input>
            
            <p:input port="parameters">
                <p:empty/>
            </p:input> 
            
        </p:xslt>
        
        <p:identity name="world" />
        
    </p:declare-step>
    
    
</p:library>    