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
 * Tests exploring the use of p:identity.
 * @author Sheila Thomson
 *
 */
public class TestIdentity {
	
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
	 * Output a verbatim copy of the primary input.
	 * @throws Exception
	 */
	@Test
	public void testIdentity_copyVerbatim() throws Exception {	
		
		File inputFile = TestCommandLineXmlProcessor.getFile("/data/source/hello_world.xml", true);
		
		processor.setPipeline(new URI(TestCommandLineXmlProcessor.getFile("/xproc/identity/copy_verbatim.xpl", false).toURI().toString()));
		processor.setInput(new URI(inputFile.toURI().toString()));

		String expected = FileUtils.readFileToString(inputFile, "UTF-8");			
				
		processor.execute();			
		
		XMLUnit.setIgnoreWhitespace(true);
		
		assertXMLEqual(expected, processor.getResponse());
		
	}
	
}
