<?xml version="1.0" encoding="UTF-8"?>
<p:library xmlns:p="http://www.w3.org/ns/xproc" xmlns:k="http://schema.kaikoda.com/ns/xproc" version="1.0">
    
    <p:declare-step type="k:hello-world">
        
        <p:input port="source">
            <p:inline>
                <document>
                    <p>Hello World!</p>
                </document>   
            </p:inline>
        </p:input>
        
        <p:output port="result" />         
        
        <p:identity />
        
    </p:declare-step>             
    
</p:library>    