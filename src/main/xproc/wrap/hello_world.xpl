<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:k="http://schema.kaikoda.com/ns/xproc" version="1.0">
    
    <p:output port="result" sequence="false" primary="true">
        <p:pipe step="wrap-text" port="result" />
    </p:output>
   
    <p:import href="../inline/library.xpl" />   
   
    <k:hello-world name="hello-world" />
    
    <p:wrap name="wrap-text" match="p" wrapper="document">
       
        <p:input port="source" select="/document/p">
            <p:pipe step="hello-world" port="result" />
        </p:input>
        
    </p:wrap>
    
</p:declare-step>