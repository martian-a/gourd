package com.kaikoda.gourd;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class CommandLineXmlProcessorCalabash extends CommandLineXmlProcessor {
	
	public static final String DEFAULT_INPUT_PORT = "source";
	
	private SaxonProcessor saxonProcessor;
	private File saxonConfiguration;
	private boolean schemaAware;
	private boolean debug;
	private boolean safeMode;
	private HashMap<String, URI> inputs;
	private URI pipeline;
	private TreeMap<String, TreeMap<String, String>> withParam;
	private TreeMap<String, String> options;
	
	/*
	private String config;
	private String logStyle;
	private String entityResolver;
	private String uriResolver;
	private String extension;
	private String binding;
	private String dataInput;
	private String output;
	private String library;
	private String step;
	*/
	
	
	protected enum SaxonProcessor {
		he, pe, ee
	}
	
	protected void init() {
		
		super.init();
		
		this.setSaxonProcessor(SaxonProcessor.he);
		this.setSaxonConfiguration(null);
		this.setSchemaAware(false);
		this.setDebug(false);
		this.setSafeMode(false);
		this.setInputs(new HashMap<String, URI>());
		this.setPipeline(null);
		this.setWithParam(null);
		this.setOptions(null);
		
	}
	
	public void reset() {
		super.reset();
		this.init();
	}
	
	public TreeMap<String, String> getOptions() {
		return this.options;
	}
	
	public void setOptions(TreeMap<String, String> list) {
		this.options = list;
	}
	
	public URI getPipeline() {
		return this.pipeline;
	}
	
	public void setPipeline(URI location) {
		this.pipeline = location;
	}
	
	public URI getInput(String port) {
		return this.inputs.get(port);
	}
	
	public HashMap<String, URI> getInputs() {
		return this.inputs;
	}
	
	public void setInput(URI location) {				
		this.inputs.put(DEFAULT_INPUT_PORT, location);
	}
	
	public void setInput(String port, URI location) {
		
		// If the value is effectively empty, override it with the default port name.
		if (port == null || port.trim().equals("")) {
			port = CommandLineXmlProcessorCalabash.DEFAULT_INPUT_PORT;
		}
		
		this.inputs.put(port, location);
	}
	
	public void setInputs(HashMap<String, URI> inputList) {		
		this.inputs = inputList;
	}
	
	public void setSafeMode(boolean option) {
		this.safeMode = option;		
	}

	public void setDebug(boolean option) {
		this.debug = option;		
	}

	public void setSchemaAware(boolean option) {
		
		if ((option == true) && !(this.getSaxonProcessor().equals(SaxonProcessor.ee))) {
			throw new IllegalArgumentException("The Enterprise Edition of Saxon is required for schema awareness.");
		}
		
		this.schemaAware = option;
	}

	public SaxonProcessor getSaxonProcessor() {
		return this.saxonProcessor;
	}
	
	public void setSaxonProcessor(SaxonProcessor option) {
		this.saxonProcessor = option;
	}

	public File getSaxonConfiguration() {
		return this.saxonConfiguration;
	}

	public void setSaxonConfiguration(File location) {
		
		if ((location != null) && (this.getSaxonProcessor().equals(SaxonProcessor.he))) {
			
			// Ensure that no configuration file is set
			this.saxonConfiguration = null;
			
			// Let the user know that this option isn't available in conjunction with the Home Edition of Saxon.
			throw new IllegalArgumentException("A Saxon configuration file may not be used with the Home Edition of Saxon.");
			
		}
		
		this.saxonConfiguration = location;
	}

	public boolean getSchemaAware() {
		return this.schemaAware;
	}

	public boolean getDebug() {
		return this.debug;
	}

	public boolean getSafeMode() {
		return this.safeMode;
	}
	
	public TreeMap<String,TreeMap<String, String>> getWithParam(){
		return this.withParam;
	}
	
	public void setWithParam(TreeMap<String, TreeMap<String, String>> parameters) {
		this.withParam = parameters;
	}
	
	public void execute() throws CommandLineXmlProcessorException, IOException, InterruptedException {
		super.execute(this);
	}
	
	public String toString() {
		
		ArrayList<String> options = new ArrayList<String>();
		
		// Path to XML Calabash
		options.add(this.getPathToXmlProcessor());
		
		// Which version of Saxon to use
		options.add("--saxon-processor=" + this.getSaxonProcessor().name());
		
		// Path to Saxon configuration file
		if (this.getSaxonConfiguration() != null) {
			options.add("--saxon-configuration=" + this.getSaxonConfiguration().getPath());
		}
		
		// Whether Saxon should be schema aware
		options.add("--schema-aware=" + String.valueOf(this.getSchemaAware()));
		
		// Whether debug mode should be activated
		options.add("--debug=" + String.valueOf(this.getDebug()));
		
		// Whether safe mode should be activated
		options.add("--safe-mode=" + String.valueOf(this.safeMode));
		
		// The input 
		if (this.inputs != null) {
			
			String inputList = "";
			for (String port : this.inputs.keySet()) {
				options.add("--input " + port + "=" + this.inputs.get(port).toString());	
			}
					
		}
		
		// Parameters (NOT options...)
		if (this.withParam != null) {

			String parameters = "";			
			for (String portName : this.withParam.keySet()) {		
				
				TreeMap<String, String> portParameters = this.withParam.get(portName);			
				for (String parameterName : portParameters.keySet()) {
					parameters = parameters + "--with-param " + portName + "@" + parameterName + "=" + portParameters.get(parameterName) + " ";
				}
			}
			if (parameters.trim() != "") {
				options.add(parameters.trim());
			}
			
		}
		
		// The pipeline
		if (this.pipeline != null) {
			options.add(this.getPipeline().toString());
		}
		
		// Options (NOT parameters...)		
		if (this.options != null) {

			String list = "";			
			for (String name : this.options.keySet()) {				
				String value = this.options.get(name);
				list = list + name + "=" + value + " ";
			}
			if (list.trim() != "") {
				options.add(list.trim());
			}
			
		}
		
		
		// Stick all the options together.
		String command = "";
		for (int i = 0; i < options.size(); i++) {
			
			// Append an option.
			command = command + options.get(i);
			
			// If it's not the final option, append a space.
			if (i != (options.size() - 1)) {
				command = command + " ";
			}
			
		}
		
		// Return the command that represents the current configuration of this object.
		return command;
	}

}
