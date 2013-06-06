<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:k="http://schema.kaikoda.com/ns/xproc" version="1.0">    
    
    <p:output port="result">
        <p:pipe step="world" port="result" />
    </p:output>
    
    <p:import href="library.xpl"/>
        
        
    <k:hello name="hello">
        
        <p:input port="source">
            <p:document href="../../../xml/examples/hello_world.xml"/>
        </p:input>                
        
    </k:hello>
        
        
    <k:world name="world">
        
        <p:input port="source">
            <p:pipe step="hello" port="result" />
        </p:input>
        
    </k:world>          
       
    
</p:declare-step>