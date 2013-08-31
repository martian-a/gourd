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
 * Tests exploring the use of p:wrap.
 * @author Sheila Thomson
 *
 */
public class TestWrap {
	
	private static CommandLineXmlProcessorCalabash processor;

	@BeforeClass
	public static void setupOnce() {
		TestWrap.processor = new CommandLineXmlProcessorCalabash();
	}
	
	@Before
	public void setup() {
		TestWrap.processor.reset();
	}
	
	/**
	 * Output a result built using steps imported from a library.
	 * @throws Exception
	 */
	@Test
	public void testWrap_helloWorld() throws Exception {	
		
		processor.setPipeline(new URI(TestCommandLineXmlProcessor.getFile("/xproc/valid/wrap/hello_world.xpl", false).toURI().toString()));
		
		String expected = FileUtils.readFileToString(new File(TestCommandLineXmlProcessor.getFile("/data/source/hello_world.xml", true).getAbsolutePath()), "UTF-8");			
				
		processor.execute();			
		
		XMLUnit.setIgnoreWhitespace(true);
		
		assertXMLEqual(expected, processor.getResponse());
		
	}
	
}
