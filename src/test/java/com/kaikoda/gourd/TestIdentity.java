package com.kaikoda.gourd;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.File;
import java.net.URI;

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
	
	private static CommandLineXmlProcessorCalabash processor;

	@BeforeClass
	public static void setupOnce() {
		TestIdentity.processor = new CommandLineXmlProcessorCalabash();
	}
	
	@Before
	public void setup() {
		TestIdentity.processor.reset();
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
