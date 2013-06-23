<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:c="http://www.w3.org/ns/xproc-step" xmlns:k="http://schema.kaikoda.com/ns/xproc" version="1.0">
    
    <p:input port="source" sequence="false" primary="true">
        <p:inline>
            <document>
                <p>Hello World!</p>
            </document>            
        </p:inline>
    </p:input>
    
    <p:output port="result" primary="true" />
    
    <p:identity />
    
</p:declare-step>