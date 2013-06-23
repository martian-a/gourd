package com.kaikoda.gourd;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

/**
 * Tests exploring the use of p:identity.
 * @author Sheila Thomson
 *
 */
public class TestIdentity {
	
	/**
	 * Output a verbatim copy of the primary input.
	 * @throws Exception
	 */
	@Test
	public void testIdentity_copyVerbatim() throws Exception {	
		
		String pathToPipeline = TestCommandLineXmlProcessor.getAbsolutePath("/xproc/identity/copy_verbatim.xpl", false);
		String pathToSource = TestCommandLineXmlProcessor.getAbsolutePath("/data/source/hello_world.xml");
		String expected = FileUtils.readFileToString(new File(pathToSource), "UTF-8");		
		
		CommandLineXmlProcessor runtime = new CommandLineXmlProcessor();
				
		runtime.execute("--input source=" + pathToSource + " " + pathToPipeline);			
		
		XMLUnit.setIgnoreWhitespace(true);
		
		assertXMLEqual(expected, runtime.getResponse());
		
	}
	
}
