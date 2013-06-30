package com.kaikoda.gourd;

import java.io.File;
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

import com.kaikoda.gourd.CommandLineXmlProcessorCalabash.SaxonProcessor;

public class TestCommandLineXmlProcessorCalabash {

	private static String defaultErrorMessage = null;
	private static int defaultExitValue = 0;
	private static Boolean defaultIsReady = true;
	private static String defaultPathToXmlProcessor;
	private static String defaultResponse = null;
	private static CommandLineXmlProcessorCalabash processor;
	private static SaxonProcessor defaultSaxonProcessor = SaxonProcessor.he;
	private static File defaultSaxonConfiguration = null;
	private static boolean defaultSchemaAware = false;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	static public void setupOnce() {
		
		TestCommandLineXmlProcessorCalabash.processor = new CommandLineXmlProcessorCalabash();
		
		Properties properties = CommandLineXmlProcessor.getProperties();
		TestCommandLineXmlProcessorCalabash.defaultPathToXmlProcessor = properties.getProperty("xmlprocessor.path");
		
	}
	
	@Before
	public void setup() {
		TestCommandLineXmlProcessorCalabash.processor.reset();
	}

	@Test
	public void TestCommandLineXmlProcessorCalabash_constructor() throws Exception {

		/*
		 * Check that the initial values are the default values.
		 */
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultPathToXmlProcessor, TestCommandLineXmlProcessorCalabash.processor.getPathToXmlProcessor());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultIsReady, TestCommandLineXmlProcessorCalabash.processor.isReady());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultErrorMessage, TestCommandLineXmlProcessorCalabash.processor.getErrorMessage());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultResponse, TestCommandLineXmlProcessorCalabash.processor.getResponse());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultExitValue, TestCommandLineXmlProcessorCalabash.processor.getExitValue());
		
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultSaxonProcessor, TestCommandLineXmlProcessorCalabash.processor.getSaxonProcessor());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultSaxonConfiguration, TestCommandLineXmlProcessorCalabash.processor.getSaxonConfiguration());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultSchemaAware, TestCommandLineXmlProcessorCalabash.processor.getSchemaAware());

	}

	/**
	 * Check that a CommandLineXmlProcessorException is thrown if a pipeline
	 * isn't specified.
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_execute_fail_noPipelineSpecified() throws Exception {

		this.exception.expect(CommandLineXmlProcessorException.class);
		this.exception.expectMessage("Usage: com.xmlcalabash.drivers.Main [switches] [pipeline.xpl] [options]");

		TestCommandLineXmlProcessorCalabash.processor.execute("");

	}

	/**
	 * Check that a CommandLineXmlProcessorException is thrown if a pipeline
	 * option value is required but not supplied.
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_execute_fail_optionRequired_missing() throws Exception {

		this.exception.expect(CommandLineXmlProcessorException.class);
		this.exception.expectMessage("err:XS0018:No value provided for required option");

		TestCommandLineXmlProcessorCalabash.processor.execute(TestCommandLineXmlProcessor.getAbsolutePath("/xproc/option_required.xpl"));

	}

	/**
	 * Check that the CommandLineXmlProcessor is capable of executing a valid
	 * XML pipeline.
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_execute_success() throws Exception {

		String expected = FileUtils.readFileToString(new File(TestCommandLineXmlProcessor.getAbsolutePath("/data/control/hello_world.xml")), "UTF-8");

		TestCommandLineXmlProcessorCalabash.processor.execute(TestCommandLineXmlProcessor.getAbsolutePath("/xproc/hello_world.xpl"));

		XMLUnit.setIgnoreWhitespace(true);

		XMLAssert.assertXMLEqual(expected, TestCommandLineXmlProcessorCalabash.processor.getResponse());

	}

	@Test
	public void TestCommandLineXmlProcessorCalabash_reset() throws Exception {

		String expected = "/testing/testing/one/two/one/two";

		// Change the value of pathToXmlProcessor
		TestCommandLineXmlProcessorCalabash.processor.setPathToXmlProcessor(expected);

		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, TestCommandLineXmlProcessorCalabash.processor.getPathToXmlProcessor());

		// Reset CommandLineXmlProcessor
		TestCommandLineXmlProcessorCalabash.processor.reset();

		/*
		 * Check that the new values are once again the default values.
		 */
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultPathToXmlProcessor, TestCommandLineXmlProcessorCalabash.processor.getPathToXmlProcessor());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultIsReady, TestCommandLineXmlProcessorCalabash.processor.isReady());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultErrorMessage, TestCommandLineXmlProcessorCalabash.processor.getErrorMessage());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultResponse, TestCommandLineXmlProcessorCalabash.processor.getResponse());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultExitValue, TestCommandLineXmlProcessorCalabash.processor.getExitValue());

	}

	@Test
	public void TestCommandLineXmlProcessorCalabash_reset_overridePathToXmlProcessor() throws Exception {

		String expected = "/testing/testing/one/two/one/two";

		// Change the value of pathToXmlProcessor
		TestCommandLineXmlProcessorCalabash.processor.setPathToXmlProcessor(expected);

		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, TestCommandLineXmlProcessorCalabash.processor.getPathToXmlProcessor());

		// Reset CommandLineXmlProcessor
		TestCommandLineXmlProcessorCalabash.processor.reset(expected);

		/*
		 * Check that the new values are once again the default values, except
		 * for the path to the XML Processor which should be the custom value
		 * supplied
		 */
		Assert.assertEquals(expected, TestCommandLineXmlProcessorCalabash.processor.getPathToXmlProcessor());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultIsReady, TestCommandLineXmlProcessorCalabash.processor.isReady());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultErrorMessage, TestCommandLineXmlProcessorCalabash.processor.getErrorMessage());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultResponse, TestCommandLineXmlProcessorCalabash.processor.getResponse());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultExitValue, TestCommandLineXmlProcessorCalabash.processor.getExitValue());

	}

	/**
	 * Check that it's possible to change the XML processor used by
	 * CommandLineXmlProcessor.
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setPathToXmlProcessor() throws Exception {

		String expected = "/testing/testing/one/two/one/two";

		// Check that the initial value is the default value.
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultPathToXmlProcessor, TestCommandLineXmlProcessorCalabash.processor.getPathToXmlProcessor());

		// Change the value of pathToXmlProcessor
		TestCommandLineXmlProcessorCalabash.processor.setPathToXmlProcessor(expected);

		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, TestCommandLineXmlProcessorCalabash.processor.getPathToXmlProcessor());

	}
	
	/**
	 * Check that it's possible to change the version of Saxon used by CommandLineXmlProcessorCalabash.
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setSaxonProcessor_professionalEdition() {
		
		SaxonProcessor expected = SaxonProcessor.pe;
		
		// Change the value of SaxonProcessor
		processor.setSaxonProcessor(expected);
		
		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, processor.getSaxonProcessor());
		
	}
	
	/**
	 * Check that it's possible to change the version of Saxon used by CommandLineXmlProcessorCalabash.
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setSaxonProcessor_enterpriseEdition() {
		
		SaxonProcessor expected = SaxonProcessor.ee;
		
		// Change the value of SaxonProcessor
		processor.setSaxonProcessor(expected);
		
		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, processor.getSaxonProcessor());
		
	}
	
	
	/**
	 * Check that it's possible to change the Saxon configuration file.
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setSaxonConfiguration_professionalEdition() {
		
		File expected = new File("test");
		
		// Ensure that a valid edition of Saxon is being used.
		processor.setSaxonProcessor(SaxonProcessor.pe);
		
		// Change the value of SaxonProcessor
		processor.setSaxonConfiguration(expected);
		
		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, processor.getSaxonConfiguration());
		
	}
	
	/**
	 * Check that it's possible to change the Saxon configuration file.
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setSaxonConfiguration_enterpriseEdition() {
		
		File expected = new File("test");
		
		// Ensure that a valid edition of Saxon is being used.
		processor.setSaxonProcessor(SaxonProcessor.ee);
		
		// Change the value of SaxonProcessor
		processor.setSaxonConfiguration(expected);
		
		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, processor.getSaxonConfiguration());
		
	}
	
	/**
	 * Check that it's possible to change the Saxon configuration file.
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setSaxonConfiguration_fail() throws Exception {
		
		// Ensure that the Home Edition of Saxon is being used.
		processor.setSaxonProcessor(SaxonProcessor.he);
		
		// Specify which exception is expected and all or part of the expected error message.
		this.exception.expect(IllegalArgumentException.class);
		this.exception.expectMessage("A Saxon configuration file may not be used with the Home Edition of Saxon.");
		
		// Attempt to set a Saxon configuration file.
		processor.setSaxonConfiguration(new File("test"));	
		
	}


	/**
	 * Check that it's possible to set Saxon to schema aware when the Enterprise Edition is in use.
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setSchemaAware() {
		
		boolean expected = true;
		
		// Ensure that a valid edition of Saxon is being used.
		processor.setSaxonProcessor(SaxonProcessor.ee);
		
		// Change the value of SaxonProcessor
		processor.setSchemaAware(expected);
		
		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, processor.getSchemaAware());
		
	}
	
	
	/**
	 * Check that it's not possible to set Saxon to schema aware when the Enterprise Edition isn't in use.
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setSchemaAware_fail() throws Exception {
		
		// Ensure that the Home Edition of Saxon is being used.
		processor.setSaxonProcessor(SaxonProcessor.he);
		
		// Specify which exception is expected and all or part of the expected error message.
		this.exception.expect(IllegalArgumentException.class);
		this.exception.expectMessage("The Enterprise Edition of Saxon is required for schema awareness.");
		
		// Attempt to set a Saxon configuration file.
		processor.setSchemaAware(true);	
		
	}
	
}
