package com.kaikoda.gourd;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

/**
 * Tests exploring the use of p:inline.
 * @author Sheila Thomson
 *
 */
public class TestInline {
	
	/**
	 * Output a verbatim copy of an inline input.
	 * @throws Exception
	 */
	@Test
	public void testInline_copyVerbatim() throws Exception {		
		
		String expected = FileUtils.readFileToString(new File(TestCommandLineXmlProcessor.getAbsolutePath("/data/control/hello_world.xml")), "UTF-8");		
		
		CommandLineXmlProcessor runtime = new CommandLineXmlProcessor();
				
		runtime.execute(TestCommandLineXmlProcessor.getAbsolutePath("/xproc/inline/copy_verbatim.xpl", false));			
		
		XMLUnit.setIgnoreWhitespace(true);
		
		assertXMLEqual(expected, runtime.getResponse());
		
	}
	
}
