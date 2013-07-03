<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:k="http://schema.kaikoda.com/ns/xproc" version="1.0">
    
    <p:input port="source" sequence="false" primary="true" />
    
    <p:output port="result" sequence="false" primary="true">
        <p:pipe step="wrap-creators" port="result" />
    </p:output>
    
    <p:for-each name="get-creators">       
        
        <p:iteration-source select="/collection//creator[not(. = preceding::creator)]" />                                
        
        <p:output port="result" sequence="true" primary="true" />        
        
        <p:identity />
        
    </p:for-each>
    

    <p:wrap-sequence name="wrap-creators" wrapper="collection">
        
        <p:input port="source">
            <p:pipe step="get-creators" port="result" />
        </p:input>
        
    </p:wrap-sequence> 
        
</p:declare-step>