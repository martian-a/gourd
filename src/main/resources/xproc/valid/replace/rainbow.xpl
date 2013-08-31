<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:k="http://schema.kaikoda.com/ns/xproc" name="rainbow" version="1.0">
    
    <p:input port="source" sequence="false" primary="true" />
    
    <p:output port="result" sequence="false" primary="true">
        <p:pipe step="de-dupe" port="result" />
    </p:output>
    
    <p:replace name="replace-colours-with-mnemonic" match="//colours">
        
        <p:input port="source">
            <p:pipe step="rainbow" port="source" />
        </p:input> 
        
        <p:input port="replacement" select="//mnemonic">
            <p:pipe step="rainbow" port="source" />
        </p:input>
        
    </p:replace>
    
    
    <p:replace name="de-dupe" match="//mnemonic[preceding::mnemonic]">
        
        <p:input port="source">
            <p:pipe step="replace-colours-with-mnemonic" port="result" />
        </p:input> 
        
        <p:input port="replacement">
            <p:empty />
        </p:input>
        
    </p:replace>    
        
</p:declare-step>