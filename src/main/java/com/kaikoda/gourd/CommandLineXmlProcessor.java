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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

public class CommandLineXmlProcessor {

	private Properties defaultProperties;

	private String errorMessage;

	private int exitValue;

	private String pathToXmlProcessor;
	private Boolean ready;
	private String response;
	private Runtime runtime;

	public CommandLineXmlProcessor() {
		this.reset();
	}

	protected void execute(CommandLineXmlProcessor implementation) throws CommandLineXmlProcessorException, IOException, InterruptedException {

		this.ready = false;
		this.errorMessage = null;
		this.response = null;

		String command = "java -jar " + implementation.toString();

		Process proc = this.runtime.exec(command);

		InputStream errorStream = proc.getErrorStream();
		InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
		BufferedReader bufferedErrorStreamReader = new BufferedReader(errorStreamReader);

		if (bufferedErrorStreamReader.ready()) {
			this.errorMessage = this.readStream(bufferedErrorStreamReader);
		}

		if ((this.errorMessage != null) && (this.errorMessage != "")) {
			throw new CommandLineXmlProcessorException(this.errorMessage);
		}

		InputStream outputStream = proc.getInputStream();
		InputStreamReader outputStreamReader = new InputStreamReader(outputStream);
		BufferedReader bufferedOutputStreamReader = new BufferedReader(outputStreamReader);

		this.response = this.readStream(bufferedOutputStreamReader);

		if (bufferedErrorStreamReader.ready()) {
			this.errorMessage = this.readStream(bufferedErrorStreamReader);
		}

		if ((this.errorMessage != null) && (this.errorMessage != "")) {
			throw new CommandLineXmlProcessorException(this.errorMessage);
		}

		this.exitValue = proc.waitFor();
		this.ready = true;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public int getExitValue() {
		return this.exitValue;
	}

	/**
	 * @return the path to the jar file of the XML processor currently being
	 *         used by this utility.
	 */
	public String getPathToXmlProcessor() {
		return this.pathToXmlProcessor;
	}

	public String getResponse() {
		return this.response;
	}

	public Boolean isReady() {
		return this.ready;
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

	/**
	 * Reset all properties to their default initial values.
	 * 
	 * @throws CommandLineXmlProcessorException
	 */
	public void reset() {

		// Load the default properties
		if (this.defaultProperties == null) {
			this.defaultProperties = CommandLineXmlProcessor.getProperties();
		}

		try {

			// Read the default path to XML processor value
			this.pathToXmlProcessor = this.defaultProperties.getProperty("xmlprocessor.path");

			if (this.pathToXmlProcessor == null) {
				throw new CommandLineXmlProcessorException("Default path to XML Processor (xmlprocessor.path) must be set in custom.properties file.");
			}

		} catch (CommandLineXmlProcessorException e) {
			e.printStackTrace();
		}

		this.runtime = Runtime.getRuntime();
		this.ready = true;
		this.errorMessage = null;
		this.response = null;
		this.exitValue = 0;
	}

	/**
	 * A convenience method for overriding the default path to the XML processor
	 * during reset.
	 * 
	 * @param processor
	 *            the path to the XML processor.
	 */
	public void reset(String processor) {
		this.reset();
		this.setPathToXmlProcessor(processor);
	}

	/**
	 * Overrides the default path to the jar file of the XML processor that this
	 * utility is to use.
	 * 
	 * @param path
	 *            the path to the XML processor (eg. Calabash).
	 */
	public void setPathToXmlProcessor(String path) {
		this.pathToXmlProcessor = path;
	}

	static protected Properties getProperties() {

		File user = new File("custom.properties");

		File application = null;
		URL resource = CommandLineXmlProcessor.class.getResource("/settings/default.properties");
		if (resource != null) {
			application = new File(resource.getFile());
		}

		try {
			return CommandLineXmlProcessor.mergeProperties(application, user);
		} catch (IOException e) {
			try {
				return CommandLineXmlProcessor.loadProperties(user);
			} catch (IOException f) {
				try {
					return CommandLineXmlProcessor.loadProperties(application);
				} catch (IOException g) {
					return new Properties();
				}
			}
		}

	}

	static private Properties loadProperties(File file) throws IOException {

		if (file == null) {
			throw new FileNotFoundException();
		}

		Properties properties = new Properties();

		FileInputStream in = new FileInputStream(file);
		properties.load(in);
		in.close();

		return properties;

	}

	static private Properties mergeProperties(File application, File user) throws IOException {

		if ((application == null) || (user == null)) {
			throw new FileNotFoundException();
		}

		Properties merged = new Properties();
		merged = CommandLineXmlProcessor.loadProperties(application);

		Properties custom = new Properties();
		custom = CommandLineXmlProcessor.loadProperties(user);

		Set<String> keys = custom.stringPropertyNames();
		for (String key : keys) {
			merged.setProperty(key, (String) custom.get(key));
		}

		return merged;

	}

}
