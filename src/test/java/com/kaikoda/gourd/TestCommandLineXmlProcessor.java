package com.kaikoda.gourd;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestCommandLineXmlProcessor {

	static private String defaultErrorMessage = null;
	static private int defaultExitValue = 0;
	static private Boolean defaultIsReady = true;
	static private String defaultPathToXmlProcessor;
	static private String defaultResponse = null;
	static private MockCommandLineXmlProcessorImpl processor;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	static public void setupOnce() throws IOException {

		TestCommandLineXmlProcessor.processor = new MockCommandLineXmlProcessorImpl();

		Properties properties = CommandLineXmlProcessor.getProperties();
		TestCommandLineXmlProcessor.defaultPathToXmlProcessor = properties.getProperty("xmlprocessor.path");

	}
	
	@Before
	public void setup() {
		TestCommandLineXmlProcessor.processor.reset();
	}

	@Test
	public void testCommandLineXmlProcessor() throws Exception {

		/*
		 * Check that the initial values are the default values.
		 */
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultPathToXmlProcessor, TestCommandLineXmlProcessor.processor.getPathToXmlProcessor());
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultIsReady, TestCommandLineXmlProcessor.processor.isReady());
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultErrorMessage, TestCommandLineXmlProcessor.processor.getErrorMessage());
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultResponse, TestCommandLineXmlProcessor.processor.getResponse());
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultExitValue, TestCommandLineXmlProcessor.processor.getExitValue());

	}

	/**
	 * Check that a CommandLineXmlProcessorException is thrown if a pipeline
	 * isn't specified.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCommandLineXmlProcessor_execute_fail_noPipelineSpecified() throws Exception {

		TestCommandLineXmlProcessor.processor.setCommand(""); 
		
		this.exception.expect(CommandLineXmlProcessorException.class);
		this.exception.expectMessage("Usage: com.xmlcalabash.drivers.Main [switches] [pipeline.xpl] [options]");

		TestCommandLineXmlProcessor.processor.execute();

	}

	/**
	 * Check that a CommandLineXmlProcessorException is thrown if a pipeline
	 * option value is required but not supplied.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCommandLineXmlProcessor_execute_fail_optionRequired_missing() throws Exception {

		TestCommandLineXmlProcessor.processor.setCommand(TestCommandLineXmlProcessor.getFile("/xproc/option_required.xpl").toURI().toString()); 
		
		this.exception.expect(CommandLineXmlProcessorException.class);
		this.exception.expectMessage("err:XS0018:No value provided for required option");

		TestCommandLineXmlProcessor.processor.execute();

	}

	/**
	 * Check that the CommandLineXmlProcessor is capable of executing a valid
	 * XML pipeline.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCommandLineXmlProcessor_execute_success() throws Exception {

		String expected = FileUtils.readFileToString(new File(TestCommandLineXmlProcessor.getFile("/data/control/hello_world.xml").getAbsolutePath()), "UTF-8");
		
		TestCommandLineXmlProcessor.processor.setCommand(TestCommandLineXmlProcessor.getFile("/xproc/hello_world.xpl").toURI().toString()); 

		TestCommandLineXmlProcessor.processor.execute();

		XMLUnit.setIgnoreWhitespace(true);

		XMLAssert.assertXMLEqual(expected, TestCommandLineXmlProcessor.processor.getResponse());

	}

	@Test
	public void testCommandLineXmlProcessor_reset() throws Exception {

		String expected = "/testing/testing/one/two/one/two";

		// Change the value of pathToXmlProcessor
		TestCommandLineXmlProcessor.processor.setPathToXmlProcessor(expected);

		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, TestCommandLineXmlProcessor.processor.getPathToXmlProcessor());

		// Reset CommandLineXmlProcessor
		TestCommandLineXmlProcessor.processor.reset();

		/*
		 * Check that the new values are once again the default values.
		 */
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultPathToXmlProcessor, TestCommandLineXmlProcessor.processor.getPathToXmlProcessor());	
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultIsReady, TestCommandLineXmlProcessor.processor.isReady());
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultErrorMessage, TestCommandLineXmlProcessor.processor.getErrorMessage());
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultResponse, TestCommandLineXmlProcessor.processor.getResponse());
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultExitValue, TestCommandLineXmlProcessor.processor.getExitValue());
		
		/**
		 * Check that the path to the XML Processor isn't null.
		 */
		Assert.assertTrue(processor.getPathToXmlProcessor() != null);

	}


	/**
	 * Check that it's possible to change the XML processor used by
	 * CommandLineXmlProcessor.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCommandLineXmlProcessor_setPathToXmlProcessor() throws Exception {

		String expected = "/testing/testing/one/two/one/two";

		// Check that the initial value is the default value.
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultPathToXmlProcessor, TestCommandLineXmlProcessor.processor.getPathToXmlProcessor());

		// Change the value of pathToXmlProcessor
		TestCommandLineXmlProcessor.processor.setPathToXmlProcessor(expected);

		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, TestCommandLineXmlProcessor.processor.getPathToXmlProcessor());

	}

	
	/**
	 * Locates a test file within the project.
	 * 
	 * @param relativePath
	 *            the relative path to a file in the project.
	 * @param isTestFile
	 *            whether the file is in the test or main part of the project.
	 * @return the absolute path to the file.
	 */
	static protected File getFile(String relativePath) {
		return TestCommandLineXmlProcessor.getFile(relativePath, true);
	}
	
	/**
	 * Locates a file within the project.
	 * 
	 * @param relativePath
	 *            the relative path to a file in the project.
	 * @param isTestFile
	 *            whether the file is in the test or main part of the project.
	 * @return the absolute path to the file.
	 */
	static protected File getFile(String relativePath, Boolean isTestFile) {
		
		@SuppressWarnings("rawtypes")
		Class relativeTo = CommandLineXmlProcessor.class;
		if (isTestFile) {
			relativeTo = TestCommandLineXmlProcessor.class;
		}

		return new File(relativeTo.getResource(relativePath).getFile());
		
	}

	static protected class MockCommandLineXmlProcessorImpl extends CommandLineXmlProcessor {
		
		private String command;
		
		public void setCommand(String commandIn) {
			this.command = commandIn;
		}
		
		public void execute() throws CommandLineXmlProcessorException, IOException, InterruptedException {
			super.execute(this);
		}
		
		public String toString() {
			return this.getPathToXmlProcessor() + " -P he " + this.command;
		}
		
	}
	
}
