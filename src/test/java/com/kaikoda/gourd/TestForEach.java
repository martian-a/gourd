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
public class TestForEach {

	private static CommandLineXmlProcessorCalabash processor;

	@BeforeClass
	public static void setupOnce() {
		TestForEach.processor = new CommandLineXmlProcessorCalabash();
	}
	
	@Before
	public void setup() {
		TestForEach.processor.reset();
	}

	/**
	 * Output a verbatim copy of the primary input.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testForEach_collectionCreators() throws Exception {

		File inputFile = TestCommandLineXmlProcessor.getFile("/data/source/collection.xml");

		TestForEach.processor.setPipeline(new URI(TestCommandLineXmlProcessor.getFile("/xproc/for-each/collection_creators.xpl", false).toURI().toString()));
		TestForEach.processor.setInput(new URI(inputFile.toURI().toString()));

		String expected = FileUtils.readFileToString(TestCommandLineXmlProcessor.getFile("/data/control/collection_creators.xml", true), "UTF-8");
	
		processor.execute();
		
		XMLUnit.setIgnoreWhitespace(true);

		XMLAssert.assertXMLEqual(expected, TestForEach.processor.getResponse());

	}

}
