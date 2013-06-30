package com.kaikoda.gourd;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.File;
import java.net.URI;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests exploring the use of p:inline.
 * @author Sheila Thomson
 *
 */
public class TestInline {
	
	static private CommandLineXmlProcessorCalabash processor;
	static private String defaultPathToXmlProcessor;
	
	@BeforeClass
	static public void setupOnce() {
		processor = new CommandLineXmlProcessorCalabash();
		
		Properties properties = CommandLineXmlProcessor.getProperties();
		defaultPathToXmlProcessor = properties.getProperty("xmlprocessor.path");
		
	}
	
	@Before
	public void setup() {
		processor.reset();
		processor.setPathToXmlProcessor(defaultPathToXmlProcessor);
	}
	
	/**
	 * Output a verbatim copy of an inline input.
	 * @throws Exception
	 */
	@Test
	public void testInline_copyVerbatim() throws Exception {					
		
		processor.setPipeline(new URI(TestCommandLineXmlProcessor.getFile("/xproc/inline/copy_verbatim.xpl", false).toURI().toString()));		
		
		String expected = FileUtils.readFileToString(new File(TestCommandLineXmlProcessor.getFile("/data/control/hello_world.xml", true).getAbsolutePath()), "UTF-8");		
		
		processor.execute();			
		
		XMLUnit.setIgnoreWhitespace(true);
		
		assertXMLEqual(expected, processor.getResponse());		
		
	}
	
}
