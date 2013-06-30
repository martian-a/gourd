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
	static private CommandLineXmlProcessor processor;

	/**
	 * Converts a relative path to a test file into an absolute path.
	 * 
	 * @param relativePath
	 *            the relative path to the test file.
	 * @return the absolute path to the file.
	 */
	static protected String getAbsolutePath(String relativePath) {
		return TestCommandLineXmlProcessor.getAbsolutePath(relativePath, true);
	}

	/**
	 * Converts a relative path into an absolute path.
	 * 
	 * @param relativePath
	 *            the relative path to a file in the project.
	 * @param isTestFile
	 *            whether the file is in the test or main part of the project.
	 * @return the absolute path to the file.
	 */
	static protected String getAbsolutePath(String relativePath, Boolean isTestFile) {

		@SuppressWarnings("rawtypes")
		Class relativeTo = CommandLineXmlProcessor.class;
		if (isTestFile) {
			relativeTo = TestCommandLineXmlProcessor.class;
		}

		File file = new File(relativeTo.getResource(relativePath).getFile());

		return file.getAbsolutePath();

	}

	@BeforeClass
	static public void setupOnce() throws IOException {

		TestCommandLineXmlProcessor.processor = new CommandLineXmlProcessor();

		Properties properties = CommandLineXmlProcessor.getProperties();
		TestCommandLineXmlProcessor.defaultPathToXmlProcessor = properties.getProperty("xmlprocessor.path");

	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

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

		this.exception.expect(CommandLineXmlProcessorException.class);
		this.exception.expectMessage("Usage: com.xmlcalabash.drivers.Main [switches] [pipeline.xpl] [options]");

		TestCommandLineXmlProcessor.processor.execute("");

	}

	/**
	 * Check that a CommandLineXmlProcessorException is thrown if a pipeline
	 * option value is required but not supplied.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCommandLineXmlProcessor_execute_fail_optionRequired_missing() throws Exception {

		this.exception.expect(CommandLineXmlProcessorException.class);
		this.exception.expectMessage("err:XS0018:No value provided for required option");

		TestCommandLineXmlProcessor.processor.execute(TestCommandLineXmlProcessor.getAbsolutePath("/xproc/option_required.xpl"));

	}

	/**
	 * Check that the CommandLineXmlProcessor is capable of executing a valid
	 * XML pipeline.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCommandLineXmlProcessor_execute_success() throws Exception {

		String expected = FileUtils.readFileToString(new File(TestCommandLineXmlProcessor.getAbsolutePath("/data/control/hello_world.xml")), "UTF-8");

		TestCommandLineXmlProcessor.processor.execute(TestCommandLineXmlProcessor.getAbsolutePath("/xproc/hello_world.xpl"));

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

	}

	@Test
	public void testCommandLineXmlProcessor_reset_overridePathToXmlProcessor() throws Exception {

		String expected = "/testing/testing/one/two/one/two";

		// Change the value of pathToXmlProcessor
		TestCommandLineXmlProcessor.processor.setPathToXmlProcessor(expected);

		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, TestCommandLineXmlProcessor.processor.getPathToXmlProcessor());

		// Reset CommandLineXmlProcessor
		TestCommandLineXmlProcessor.processor.reset(expected);

		/*
		 * Check that the new values are once again the default values, except
		 * for the path to the XML Processor which should be the custom value
		 * supplied
		 */
		Assert.assertEquals(expected, TestCommandLineXmlProcessor.processor.getPathToXmlProcessor());
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultIsReady, TestCommandLineXmlProcessor.processor.isReady());
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultErrorMessage, TestCommandLineXmlProcessor.processor.getErrorMessage());
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultResponse, TestCommandLineXmlProcessor.processor.getResponse());
		Assert.assertEquals(TestCommandLineXmlProcessor.defaultExitValue, TestCommandLineXmlProcessor.processor.getExitValue());

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
}
