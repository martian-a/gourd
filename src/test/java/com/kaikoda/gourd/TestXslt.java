package com.kaikoda.gourd;

import java.io.File;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests exploring the use of p:xslt.
 * 
 * @author Sheila Thomson
 * 
 */
public class TestXslt {

	private static CommandLineXmlProcessorCalabash processor;

	@BeforeClass
	public static void setupOnce() {
		TestXslt.processor = new CommandLineXmlProcessorCalabash();
	}
	
	@Before
	public void setup() {
		TestXslt.processor.reset();
	}

	/**
	 * Output a verbatim copy of the primary input.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testXslt_copyVerbatim() throws Exception {

		File inputFile = TestCommandLineXmlProcessor.getFile("/data/source/hello_world.xml");

		TestXslt.processor.setPipeline(new URI(TestCommandLineXmlProcessor.getFile("/xproc/xslt/copy_verbatim.xpl", false).toURI().toString()));
		TestXslt.processor.setInput(new URI(inputFile.toURI().toString()));

		String expected = FileUtils.readFileToString(inputFile, "UTF-8");
	
		processor.execute();
		
		XMLUnit.setIgnoreWhitespace(true);

		XMLAssert.assertXMLEqual(expected, TestXslt.processor.getResponse());

	}

}
