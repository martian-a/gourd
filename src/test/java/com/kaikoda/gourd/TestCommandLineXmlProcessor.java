package com.kaikoda.gourd;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class TestCommandLineXmlProcessor {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	/**
	 * Check that the CommandLineXmlProcessor is capable of executing a valid XML pipeline.
	 * @throws Exception
	 */
	@Test
	public void testExecute_success() throws Exception {	
		
		String expected = FileUtils.readFileToString(new File(TestCommandLineXmlProcessor.getAbsolutePath("/data/control/hello_world.xml")), "UTF-8");		
		
		CommandLineXmlProcessor runtime = new CommandLineXmlProcessor();
				
		runtime.execute(TestCommandLineXmlProcessor.getAbsolutePath("/xproc/hello_world.xpl"));			
		
		XMLUnit.setIgnoreWhitespace(true);
		
		assertXMLEqual(expected, runtime.getResponse());
		
	}
	
	/**
	 * Check that a CommandLineXmlProcessorException is thrown if a pipeline isn't specified.
	 * @throws Exception
	 */
	@Test
	public void testExecute_fail_noPipelineSpecified() throws Exception {				
		
		CommandLineXmlProcessor runtime = new CommandLineXmlProcessor();
		
		exception.expect(CommandLineXmlProcessorException.class);
		exception.expectMessage("Usage: com.xmlcalabash.drivers.Main [switches] [pipeline.xpl] [options]");
		
		runtime.execute("");						
		
	}
	
	/**
	 * Check that a CommandLineXmlProcessorException is thrown if a pipeline option value is required but not supplied.
	 * @throws Exception
	 */
	@Test
	public void testExecute_fail_optionRequired_missing() throws Exception {					
		
		CommandLineXmlProcessor runtime = new CommandLineXmlProcessor();
		
		exception.expect(CommandLineXmlProcessorException.class);
		exception.expectMessage("err:XS0018:No value provided for required option");
				
		runtime.execute(TestCommandLineXmlProcessor.getAbsolutePath("/xproc/option_required.xpl"));		
		
	}	
	
	/**
	 * Check that it's possible to change the XML processor used by CommandLineXmlProcessor.
	 * @throws Exception
	 */
	@Test
	public void testSetPathToXmlProcessor() throws Exception {
		
		String expected = "/testing/testing/one/two/one/two";
		
		CommandLineXmlProcessor runtime = new CommandLineXmlProcessor();		
		
		// Check that the initial value is the default value.
		assertEquals(CommandLineXmlProcessor.DEFAULT_PATH_TO_PROCESSOR, runtime.getPathToXmlProcessor());
		
		// Change the value of pathToXmlProcessor
		runtime.setPathToXmlProcessor(expected);
		
		// Check that the active value has changed, as specified.
		assertEquals(expected, runtime.getPathToXmlProcessor());
		
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
