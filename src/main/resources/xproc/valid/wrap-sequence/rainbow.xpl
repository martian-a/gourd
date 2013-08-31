<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:k="http://schema.kaikoda.com/ns/xproc" version="1.0" name="wrap-sequence">
    
    <p:input port="source" sequence="false" primary="true" />
    <p:input port="supplement" sequence="false" />
    
    <p:output port="result" sequence="false" primary="true">
        <p:pipe step="wrap" port="result" />
    </p:output>
   
    <p:import href="../inline/library.xpl" />   
    
    <p:wrap-sequence name="wrap" wrapper="rainbow">
       
        <p:input port="source" select="/rainbow/*">
            <p:pipe step="wrap-sequence" port="source" />
            <p:pipe step="wrap-sequence" port="supplement" />
        </p:input>
        
    </p:wrap-sequence>
    
</p:declare-step>