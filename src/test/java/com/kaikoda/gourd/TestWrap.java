package com.kaikoda.gourd;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.File;

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
	
	static private CommandLineXmlProcessor processor;
	
	@BeforeClass
	static public void setupOnce() {
		processor = new CommandLineXmlProcessor();
	}
	
	@Before
	public void setup() {
		processor.reset();
	}
	
	/**
	 * Output a result built using steps imported from a library.
	 * @throws Exception
	 */
	@Test
	public void testWrap_helloWorld() throws Exception {	
		
		String pathToPipeline = TestCommandLineXmlProcessor.getAbsolutePath("/xproc/wrap/hello_world.xpl", false);
		String expected = FileUtils.readFileToString(new File(TestCommandLineXmlProcessor.getAbsolutePath("/data/source/hello_world.xml")), "UTF-8");			
				
		processor.execute(pathToPipeline);			
		
		XMLUnit.setIgnoreWhitespace(true);
		
		assertXMLEqual(expected, processor.getResponse());
		
	}
	
}
