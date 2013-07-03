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
public class TestReplace {

	private static CommandLineXmlProcessorCalabash processor;

	@BeforeClass
	public static void setupOnce() {
		TestReplace.processor = new CommandLineXmlProcessorCalabash();
	}
	
	@Before
	public void setup() {
		TestReplace.processor.reset();
	}

	/**
	 * Replace one node tree with another.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReplace_library() throws Exception {

		File inputFile = TestCommandLineXmlProcessor.getFile("/data/source/rainbow.xml");

		TestReplace.processor.setPipeline(new URI(TestCommandLineXmlProcessor.getFile("/xproc/replace/rainbow.xpl", false).toURI().toString()));
		TestReplace.processor.setInput(new URI(inputFile.toURI().toString()));

		String expected = FileUtils.readFileToString(TestCommandLineXmlProcessor.getFile("/data/control/rainbow_mnemonic.xml", true), "UTF-8");
	
		processor.execute();
		
		XMLUnit.setIgnoreWhitespace(true);	

		XMLAssert.assertXMLEqual(expected, TestReplace.processor.getResponse());

	}

}
