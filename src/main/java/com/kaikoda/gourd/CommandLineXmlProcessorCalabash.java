package com.kaikoda.gourd;

public class CommandLineXmlProcessorCalabash extends CommandLineXmlProcessor {
	
	private SaxonProcessor saxonProcessor;
	private String saxonConfiguration;
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
	
	public void setSaxonProcessor(SaxonProcessor processor) {
		this.saxonProcessor = processor;
	}

}
