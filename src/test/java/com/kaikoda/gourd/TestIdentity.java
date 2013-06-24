package com.kaikoda.gourd;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.File;

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
	 * Output a verbatim copy of the primary input.
	 * @throws Exception
	 */
	@Test
	public void testIdentity_copyVerbatim() throws Exception {	
		
		String pathToPipeline = TestCommandLineXmlProcessor.getAbsolutePath("/xproc/identity/copy_verbatim.xpl", false);
		String pathToSource = TestCommandLineXmlProcessor.getAbsolutePath("/data/source/hello_world.xml");
		String expected = FileUtils.readFileToString(new File(pathToSource), "UTF-8");			
				
		processor.execute("--input source=" + pathToSource + " " + pathToPipeline);			
		
		XMLUnit.setIgnoreWhitespace(true);
		
		assertXMLEqual(expected, processor.getResponse());
		
	}
	
}
