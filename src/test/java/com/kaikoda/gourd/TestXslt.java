package com.kaikoda.gourd;

import java.io.File;
import java.net.URI;
import java.util.Properties;

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

	private static String defaultPathToXmlProcessor;
	private static CommandLineXmlProcessorCalabash processor;

	@BeforeClass
	public static void setupOnce() {

		TestXslt.processor = new CommandLineXmlProcessorCalabash();

		Properties properties = CommandLineXmlProcessor.getProperties();
		TestXslt.defaultPathToXmlProcessor = properties.getProperty("xmlprocessor.path");

	}
	
	@Before
	public void setup() {
		TestXslt.processor.reset();
		TestXslt.processor.setPathToXmlProcessor(TestXslt.defaultPathToXmlProcessor);
	}

	/**
	 * Output a verbatim copy of the primary input.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testXslt_copyVerbatim() throws Exception {

		String pathToInput = TestCommandLineXmlProcessor.getAbsolutePath("/data/source/hello_world.xml");

		TestXslt.processor.setPipeline(new URI(TestCommandLineXmlProcessor.getAbsolutePath("/xproc/xslt/copy_verbatim.xpl", false)));
		TestXslt.processor.setInput(new URI(pathToInput));

		String expected = FileUtils.readFileToString(new File(pathToInput), "UTF-8");
	
		processor.execute();
		
		XMLUnit.setIgnoreWhitespace(true);

		XMLAssert.assertXMLEqual(expected, TestXslt.processor.getResponse());

	}

}
