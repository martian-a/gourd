package com.kaikoda.gourd;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
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
	private static boolean defaultDebug = false;
	private static boolean defaultSafeMode = false;
	private static File defaultInput = null;
	private static String defaultInputPort = CommandLineXmlProcessorCalabash.DEFAULT_INPUT_PORT;
	private static File defaultPipeline = null;
	private static HashMap<String, String[]> defaultWithParam = null;

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
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultDebug, TestCommandLineXmlProcessorCalabash.processor.getDebug());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultSafeMode, TestCommandLineXmlProcessorCalabash.processor.getSafeMode());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultInput, TestCommandLineXmlProcessorCalabash.processor.getInput());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultInputPort, TestCommandLineXmlProcessorCalabash.processor.getInputPort());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultPipeline, TestCommandLineXmlProcessorCalabash.processor.getPipeline());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultWithParam, TestCommandLineXmlProcessorCalabash.processor.getWithParam());

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

		TestCommandLineXmlProcessorCalabash.processor.execute();

	}

	/**
	 * Check that a CommandLineXmlProcessorException is thrown if a pipeline
	 * option value is required but not supplied.
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_execute_fail_optionRequired_missing() throws Exception {

		processor.setPipeline(new URI(TestCommandLineXmlProcessor.getFile("/xproc/option_required.xpl").toURI().toString()));
		
		this.exception.expect(CommandLineXmlProcessorException.class);
		this.exception.expectMessage("err:XS0018:No value provided for required option");

		TestCommandLineXmlProcessorCalabash.processor.execute();

	}

	/**
	 * Check that the CommandLineXmlProcessor is capable of executing a valid
	 * XML pipeline.
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_execute_success() throws Exception {
		
		processor.setPipeline(new URI(TestCommandLineXmlProcessor.getFile("/xproc/hello_world.xpl").toURI().toString()));

		String expected = FileUtils.readFileToString(new File(TestCommandLineXmlProcessor.getFile("/data/control/hello_world.xml").getAbsolutePath()), "UTF-8");

		TestCommandLineXmlProcessorCalabash.processor.execute();

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
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultSaxonProcessor, TestCommandLineXmlProcessorCalabash.processor.getSaxonProcessor());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultSaxonConfiguration, TestCommandLineXmlProcessorCalabash.processor.getSaxonConfiguration());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultSchemaAware, TestCommandLineXmlProcessorCalabash.processor.getSchemaAware());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultDebug, TestCommandLineXmlProcessorCalabash.processor.getDebug());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultSafeMode, TestCommandLineXmlProcessorCalabash.processor.getSafeMode());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultInput, TestCommandLineXmlProcessorCalabash.processor.getInput());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultInputPort, TestCommandLineXmlProcessorCalabash.processor.getInputPort());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultPipeline, TestCommandLineXmlProcessorCalabash.processor.getPipeline());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.defaultWithParam, TestCommandLineXmlProcessorCalabash.processor.getWithParam());

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

	/**
	 * Check that it's possible to activate debug mode.
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setDebug() {
		
		boolean expected = true;
		
		// Change the value of Debug
		processor.setDebug(expected);
		
		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, processor.getDebug());
		
	}
	
	/**
	 * Check that it's possible to activate safe mode.
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setSafeMode() {
		
		boolean expected = true;
		
		// Change the value of SafeMode
		processor.setSafeMode(expected);
		
		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, processor.getSafeMode());
		
	}
	
	/**
	 * Check that it's possible to set the location of the input.
	 * @throws URISyntaxException 
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setInput() throws URISyntaxException {
		
		URI expected = new URI("/data/source/hello_world.xml");
		
		// Attempt to set the location of the input file.
		processor.setInput(expected);
		
		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, processor.getInput());
		
	}
	
	/**
	 * Check that it's possible to change the input port.
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setInputPort() {
		
		String expected = "stylesheet";
		
		// Check that the test value isn't accidentally the same as the default value.
		Assert.assertNotEquals(CommandLineXmlProcessorCalabash.DEFAULT_INPUT_PORT, expected);
		
		// Attempt to change the input port
		processor.setInputPort(expected);
		
		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, processor.getInputPort());
		
	}

	
	/**
	 * Check that it's possible to set an input XML document.
	 * @throws URISyntaxException 
	 */
	@Test
	public void TestCommandLineXmlProcessorCalabash_setPipeline() throws URISyntaxException {
		
		URI expected = new URI("/xproc/identity/copy_verbatim.xpl");
		
		// Attempt to set the pipeline
		processor.setPipeline(expected);
		
		// Check that the active value has changed, as specified.
		Assert.assertEquals(expected, processor.getPipeline());
		
	}
	
	/**
	 * Check that the command string is as expected.
	 * @throws URISyntaxException 
	 */
	@Test
	public void testCommandLineXmlProcessorCalabash_toString_inlineInput() throws URISyntaxException {			
		
		String pathToPipeline = "/xproc/identity/copy_verbatim.xpl";
		
		processor.setPipeline(new URI(pathToPipeline));
											
		String expected = defaultPathToXmlProcessor + " --saxon-processor=he --schema-aware=false --debug=false --safe-mode=false " + pathToPipeline;				
		
		Assert.assertEquals(expected, processor.toString());
		
	}
	
	/**
	 * Check that the command string is as expected.
	 * @throws URISyntaxException 
	 */
	@Test
	public void testCommandLineXmlProcessorCalabash_toString_withParam() throws URISyntaxException {			
		
		String pathToPipeline = "/xproc/identity/copy_verbatim.xpl";
		
		HashMap<String, String[]> params = new HashMap<String, String[]>();
		String port1 = "source"; 
		String port1param1 = "uri=http://localhost:8080/exist/apps/sapling-test/queries/person.xq?id=PER78";
		String port1param2 = "root-publication-directory=/test/ing";
		String port2 = "secondary"; 		
		String port2param1 = "root-publication-directory=/test/ing/again";
		
		params.put(port1, new String[]{port1param1, port1param2});
		params.put(port2, new String[]{port2param1});
				
		processor.setPipeline(new URI(pathToPipeline));											
		processor.setWithParam(params);
		
		String expected = defaultPathToXmlProcessor + " --saxon-processor=he --schema-aware=false --debug=false --safe-mode=false " + pathToPipeline + " " + port1 + "@" + port1param1 + " " + port1 + "@" + port1param2 + " " + port2 + "@" + port2param1;				
		
		Assert.assertEquals(expected, processor.toString());
		
	}	
	
	@Test
	public void testCommandLineXmlProcessorCalabash_setWithParam() {
		
		HashMap<String, String[]> expected = new HashMap<String, String[]>();
		
		expected.put("source", new String[]{"uri=http://localhost:8080/exist/apps/sapling-test/queries/person.xq?id=PER78", "root-publication-directory=/test/ing"});
		
		processor.setWithParam(expected);
		
		Assert.assertEquals(expected, processor.getWithParam());
		
	}
}