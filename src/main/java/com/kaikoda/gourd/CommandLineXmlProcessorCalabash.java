package com.kaikoda.gourd;

import java.io.File;

public class CommandLineXmlProcessorCalabash extends CommandLineXmlProcessor {
	
	private SaxonProcessor saxonProcessor;
	private File saxonConfiguration;
	private boolean schemaAware;
	private boolean debug;
	private boolean safeMode;
	private String config;
	private String logStyle;
	private String entityResolver;
	private String uriResolver;
	private String extension;
	private String binding;
	private String withParam;
	private String input;
	private String dataInput;
	private String output;
	private String library;
	private String step;
	private String name;
	
	protected enum SaxonProcessor {
		he, pe, ee
	}
	
	public void reset() {
		super.reset();
		
		this.setSaxonProcessor(SaxonProcessor.he);
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

	public void setSaxonConfiguration(File file) {
		
		if (this.getSaxonProcessor().equals(SaxonProcessor.he)) {
			
			// Ensure that no configuration file is set
			this.saxonConfiguration = null;
			
			// Let the user know that this option isn't available in conjunction with the Home Edition of Saxon.
			throw new IllegalArgumentException("A Saxon configuration file may not be used with the Home Edition of Saxon.");
			
		}
		
		this.saxonConfiguration = file;
	}

}
