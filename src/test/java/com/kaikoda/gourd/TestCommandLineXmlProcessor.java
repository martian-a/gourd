package com.kaikoda.gourd;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class TestCommandLineXmlProcessor {
	
	static private Boolean defaultIsReady = true;	
	static private String defaultErrorMessage = null;	
	static private String defaultResponse = null;
	static private int defaultExitValue = 0;	
	static private String defaultPathToXmlProcessor;	
	static private CommandLineXmlProcessor processor;
	
	@BeforeClass
	static public void setupOnce() throws IOException {
		
		processor = new CommandLineXmlProcessor();
		
		Properties properties = CommandLineXmlProcessor.getDefaultProperties();
		defaultPathToXmlProcessor = properties.getProperty("xmlprocessor.path");
		
	}
	
	@Before
	public void setup() {
		processor.reset();
	}
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testSettings() throws Exception {		
		
		assertEquals(this.defaultPathToXmlProcessor, processor.getPathToXmlProcessor());
		
	}
	
	/**
	 * Check that the CommandLineXmlProcessor is capable of executing a valid XML pipeline.
	 * @throws Exception
	 */
	@Test
	public void testExecute_success() throws Exception {	
		
		String expected = FileUtils.readFileToString(new File(TestCommandLineXmlProcessor.getAbsolutePath("/data/control/hello_world.xml")), "UTF-8");				
				
		processor.execute(TestCommandLineXmlProcessor.getAbsolutePath("/xproc/hello_world.xpl"));			
		
		XMLUnit.setIgnoreWhitespace(true);
		
		assertXMLEqual(expected, processor.getResponse());
		
	}
	
	/**
	 * Check that a CommandLineXmlProcessorException is thrown if a pipeline isn't specified.
	 * @throws Exception
	 */
	@Test
	public void testExecute_fail_noPipelineSpecified() throws Exception {					
		
		exception.expect(CommandLineXmlProcessorException.class);
		exception.expectMessage("Usage: com.xmlcalabash.drivers.Main [switches] [pipeline.xpl] [options]");
		
		processor.execute("");						
		
	}
	
	/**
	 * Check that a CommandLineXmlProcessorException is thrown if a pipeline option value is required but not supplied.
	 * @throws Exception
	 */
	@Test
	public void testExecute_fail_optionRequired_missing() throws Exception {					
		
		exception.expect(CommandLineXmlProcessorException.class);
		exception.expectMessage("err:XS0018:No value provided for required option");
				
		processor.execute(TestCommandLineXmlProcessor.getAbsolutePath("/xproc/option_required.xpl"));		
		
	}	
	
	/**
	 * Check that it's possible to change the XML processor used by CommandLineXmlProcessor.
	 * @throws Exception
	 */
	@Test
	public void testSetPathToXmlProcessor() throws Exception {
		
		String expected = "/testing/testing/one/two/one/two";	
		
		// Check that the initial value is the default value.
		assertEquals(this.defaultPathToXmlProcessor, processor.getPathToXmlProcessor());
		
		// Change the value of pathToXmlProcessor
		processor.setPathToXmlProcessor(expected);
		
		// Check that the active value has changed, as specified.
		assertEquals(expected, processor.getPathToXmlProcessor());
		
	}
	
	@Test
	public void testReset() throws Exception {
		
		String expected = "/testing/testing/one/two/one/two";
		
		/*
		 *  Check that the initial values are the default values.
		 */
		assertEquals(this.defaultPathToXmlProcessor, processor.getPathToXmlProcessor());
		assertEquals(this.defaultIsReady, processor.isReady());
		assertEquals(this.defaultErrorMessage, processor.getErrorMessage());
		assertEquals(this.defaultResponse, processor.getResponse());
		assertEquals(this.defaultExitValue, processor.getExitValue());
		
		// Change the value of pathToXmlProcessor
		processor.setPathToXmlProcessor(expected);
		
		// Check that the active value has changed, as specified.
		assertEquals(expected, processor.getPathToXmlProcessor());
		
		// Reset CommandLineXmlProcessor
		processor.reset();
		
		/*
		 *  Check that the initial values are once again the default values.
		 */
		assertEquals(this.defaultPathToXmlProcessor, processor.getPathToXmlProcessor());
		assertEquals(this.defaultIsReady, processor.isReady());
		assertEquals(this.defaultErrorMessage, processor.getErrorMessage());
		assertEquals(this.defaultResponse, processor.getResponse());
		assertEquals(this.defaultExitValue, processor.getExitValue());
		
	}
	
	@Test
	public void testReset_overridePathToXmlProcessor() throws Exception {
		
		String expected = "/testing/testing/one/two/one/two";
		
		/*
		 *  Check that the initial values are the default values.
		 */
		assertEquals(this.defaultPathToXmlProcessor, processor.getPathToXmlProcessor());
		assertEquals(this.defaultIsReady, processor.isReady());
		assertEquals(this.defaultErrorMessage, processor.getErrorMessage());
		assertEquals(this.defaultResponse, processor.getResponse());
		assertEquals(this.defaultExitValue, processor.getExitValue());
		
		// Change the value of pathToXmlProcessor
		processor.setPathToXmlProcessor(expected);
		
		// Check that the active value has changed, as specified.
		assertEquals(expected, processor.getPathToXmlProcessor());
		
		// Reset CommandLineXmlProcessor
		processor.reset(expected);
		
		/*
		 *  Check that the initial values are once again the default values, 
		 *  except for the path to the XML Processor which should be the custom value supplied		 
		 */
		assertEquals(expected, processor.getPathToXmlProcessor());
		assertEquals(this.defaultIsReady, processor.isReady());
		assertEquals(this.defaultErrorMessage, processor.getErrorMessage());
		assertEquals(this.defaultResponse, processor.getResponse());
		assertEquals(this.defaultExitValue, processor.getExitValue());
		
	}
	
	/**
	 * Converts a relative path to a test file into an absolute path.
	 * @param relativePath the relative path to the test file.
	 * @return the absolute path to the file.
	 */
	static protected String getAbsolutePath(String relativePath) {
		return getAbsolutePath(relativePath, true);
	}
		
	/**
	 * Converts a relative path into an absolute path.
	 * 
	 * @param relativePath the relative path to a file in the project.
	 * @param isTestFile whether the file is in the test or main part of the project.
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
}
