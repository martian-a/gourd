/*
 * Gourd
 * Copyright (C) 2012  Sheila Thomson
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kaikoda.gourd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandLineXmlProcessor {

	private static String DEFAULT_PATH_TO_PROCESSOR = "/home/sheila/Software/Calabash/xmlcalabash-1.0.9-94/calabash.jar"; 
	
	private String pathToXmlProcessor;
	private Runtime runtime;
	private Boolean ready;
	private String errorMessage;
	private String response;
	private int exitValue;
	
	public CommandLineXmlProcessor() {
		this(CommandLineXmlProcessor.DEFAULT_PATH_TO_PROCESSOR);
	}
	
	public CommandLineXmlProcessor(String processor) {
		
		this.pathToXmlProcessor = processor;
        this.runtime = Runtime.getRuntime();
        this.ready = true;
        this.errorMessage = null;
        this.response = null;
        this.exitValue = 0;
		
	}
	
	public Boolean isReady() {
		return this.ready;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public String getResponse() {
		return this.response;
	}
	
	public int getExitValue() {
		return this.exitValue;
	}
	
    public void execute(String args) throws CommandLineXmlProcessorException, IOException, InterruptedException {
    	
    	this.ready = false;
    	this.errorMessage = null;
    	this.response = null;
    
    	String command = "java -jar " + this.pathToXmlProcessor + " -P he " + args;    	    	
        	           
        Process proc = runtime.exec(command);
        
        InputStream errorStream = proc.getErrorStream();
        InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
        BufferedReader bufferedErrorStreamReader = new BufferedReader(errorStreamReader);                                       
        
        
        if (bufferedErrorStreamReader.ready()) {
        	this.errorMessage = this.readStream(bufferedErrorStreamReader);	
        }        
                
        if (this.errorMessage != null && this.errorMessage != "") {
        	throw new CommandLineXmlProcessorException(this.errorMessage);
        }        
        
        InputStream outputStream = proc.getInputStream();
        InputStreamReader outputStreamReader = new InputStreamReader(outputStream);
        BufferedReader bufferedOutputStreamReader = new BufferedReader(outputStreamReader);                  
        
        this.response = this.readStream(bufferedOutputStreamReader);        
        
        if (bufferedErrorStreamReader.ready()) {
        	this.errorMessage = this.readStream(bufferedErrorStreamReader);	
        }        
                
        if (this.errorMessage != null && this.errorMessage != "") {
        	throw new CommandLineXmlProcessorException(this.errorMessage);
        }   
        
        this.exitValue = proc.waitFor();                
        this.ready = true;
    }
    
    private String readStream(BufferedReader reader) {
    	
    	String output = "";
    	String resultLine = null;
    	
        try {
        	
			while ((resultLine = reader.readLine()) != null) {
				output = output + resultLine + "\n";
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return output;
    }
	
}
