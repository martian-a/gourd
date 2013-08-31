package com.kaikoda.gourd;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.TreeMap;

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

	static private final String DEFAULT_ERROR_MESSAGE = null;
	static private final int DEFAULT_EXIT_VALUE = 0;
	static private final Boolean DEFAULT_IS_READY = true;
	static private final String DEFAULT_RESPONSE = null;
	static private final SaxonProcessor DEFAULT_SAXON_PROCESSOR = SaxonProcessor.he;
	static private final File DEFAULT_SAXON_CONFIGURATION = null;
	static private final boolean DEFAULT_SCHEMA_AWARE = false;
	static private final boolean DEFAULT_DEBUG = false;
	static private final boolean DEFAULT_SAFE_MODE = false;
	static private final File DEFAULT_INPUT = null;
	static private final String DEFAULT_INPUT_PORT = CommandLineXmlProcessorCalabash.DEFAULT_INPUT_PORT;
	static private final File DEFAULT_PIPELINE = null;
	static private final TreeMap<String, TreeMap<String, String>> DEFAULT_WITH_PARAM = null;
	
	static private String defaultPathToXmlProcessor;
	static private CommandLineXmlProcessorCalabash processor;

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
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_IS_READY, TestCommandLineXmlProcessorCalabash.processor.isReady());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_ERROR_MESSAGE, TestCommandLineXmlProcessorCalabash.processor.getErrorMessage());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_RESPONSE, TestCommandLineXmlProcessorCalabash.processor.getResponse());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_EXIT_VALUE, TestCommandLineXmlProcessorCalabash.processor.getExitValue());
		
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_SAXON_PROCESSOR, TestCommandLineXmlProcessorCalabash.processor.getSaxonProcessor());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_SAXON_CONFIGURATION, TestCommandLineXmlProcessorCalabash.processor.getSaxonConfiguration());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_SCHEMA_AWARE, TestCommandLineXmlProcessorCalabash.processor.getSchemaAware());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_DEBUG, TestCommandLineXmlProcessorCalabash.processor.getDebug());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_SAFE_MODE, TestCommandLineXmlProcessorCalabash.processor.getSafeMode());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_INPUT, TestCommandLineXmlProcessorCalabash.processor.getInput());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_INPUT_PORT, TestCommandLineXmlProcessorCalabash.processor.getInputPort());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_PIPELINE, TestCommandLineXmlProcessorCalabash.processor.getPipeline());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_WITH_PARAM, TestCommandLineXmlProcessorCalabash.processor.getWithParam());

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
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_IS_READY, TestCommandLineXmlProcessorCalabash.processor.isReady());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_ERROR_MESSAGE, TestCommandLineXmlProcessorCalabash.processor.getErrorMessage());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_RESPONSE, TestCommandLineXmlProcessorCalabash.processor.getResponse());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_EXIT_VALUE, TestCommandLineXmlProcessorCalabash.processor.getExitValue());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_SAXON_PROCESSOR, TestCommandLineXmlProcessorCalabash.processor.getSaxonProcessor());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_SAXON_CONFIGURATION, TestCommandLineXmlProcessorCalabash.processor.getSaxonConfiguration());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_SCHEMA_AWARE, TestCommandLineXmlProcessorCalabash.processor.getSchemaAware());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_DEBUG, TestCommandLineXmlProcessorCalabash.processor.getDebug());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_SAFE_MODE, TestCommandLineXmlProcessorCalabash.processor.getSafeMode());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_INPUT, TestCommandLineXmlProcessorCalabash.processor.getInput());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_INPUT_PORT, TestCommandLineXmlProcessorCalabash.processor.getInputPort());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_PIPELINE, TestCommandLineXmlProcessorCalabash.processor.getPipeline());
		Assert.assertEquals(TestCommandLineXmlProcessorCalabash.DEFAULT_WITH_PARAM, TestCommandLineXmlProcessorCalabash.processor.getWithParam());

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
		
		TreeMap<String, TreeMap<String, String>> params = new TreeMap<String, TreeMap<String, String>>();
		TreeMap<String, String> port1Params = new TreeMap<String, String>();
		TreeMap<String, String> port2Params = new TreeMap<String, String>();
		
		String port1 = "secondary"; 		
		String port1Param1Name = "root-publication-directory";
		String port1Param1Value = "/test/ing/again";
		port1Params.put(port1Param1Name, port1Param1Value);
		
		String port2 = "source"; 
		String port2Param1Name = "root-publication-directory";
		String port2Param1Value = "/test/ing";
		String port2Param2Name = "uri";
		String port2Param2Value = "http://localhost:8080/exist/apps/sapling-test/queries/person.xq?id=PER78";
		port2Params.put(port2Param1Name, port2Param1Value);
		port2Params.put(port2Param2Name, port2Param2Value);
		
		params.put(port1, port1Params);
		params.put(port2, port2Params);
				
		processor.setPipeline(new URI(pathToPipeline));											
		processor.setWithParam(params);
		
		String expected = defaultPathToXmlProcessor + " --saxon-processor=he --schema-aware=false --debug=false --safe-mode=false --with-param " +  port1 + "@" + port1Param1Name + "=" + port1Param1Value + " --with-param " + port2 + "@" + port2Param1Name + "=" + port2Param1Value + " --with-param " + port2 + "@" + port2Param2Name + "=" + port2Param2Value + " " + pathToPipeline;				
		
		Assert.assertEquals(expected, processor.toString());
		
	}	
	
	@Test
	public void testCommandLineXmlProcessorCalabash_setWithParam() {
		
		TreeMap<String, TreeMap<String, String>> expected = new TreeMap<String, TreeMap<String, String>>();
		TreeMap<String, String> port1Params = new TreeMap<String, String>();
		port1Params.put("uri", "http://localhost:8080/exist/apps/sapling-test/queries/person.xq?id=PER78");
		port1Params.put("root-publication-directory", "/test/ing");
		
		expected.put("source", port1Params);
		
		processor.setWithParam(expected);
		
		Assert.assertEquals(expected, processor.getWithParam());
		
	}
	
	@Test
	public void testCommandLineXmlProcessorCalabash_setOptions() {
		
		TreeMap<String, String> expected = new TreeMap<String, String>();
		
		expected.put("uri", "http://localhost:8080/exist/apps/sapling-test/queries/person.xq?id=PER78");
		expected.put("root-publication-directory", "=/test/ing");
		
		processor.setOptions(expected);
		
		Assert.assertEquals(expected, processor.getOptions());
		
	}
	
	/**
	 * Check that the command string is as expected.
	 * @throws URISyntaxException 
	 */
	@Test
	public void testCommandLineXmlProcessorCalabash_toString_options() throws URISyntaxException {			
		
		String pathToPipeline = "/xproc/identity/copy_verbatim.xpl";
		
		TreeMap<String, String> options = new TreeMap<String, String>();
		String optionName1 = "root-publication-directory";
		String optionValue1 = "/test/ing";
		options.put(optionName1, optionValue1);
		String optionName2 = "uri";
		String optionValue2 = "http://localhost:8080/exist/apps/sapling-test/queries/person.xq?id=PER78";
		options.put(optionName2, optionValue2);		
						
		processor.setPipeline(new URI(pathToPipeline));											
		processor.setOptions(options);
		
		String expected = defaultPathToXmlProcessor + " --saxon-processor=he --schema-aware=false --debug=false --safe-mode=false " + pathToPipeline + " " + optionName1 + "=" + optionValue1 + " " + optionName2 + "=" + optionValue2;				
		
		Assert.assertEquals(expected, processor.toString());
		
	}	
	
}