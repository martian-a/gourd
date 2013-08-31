package com.kaikoda.gourd;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.File;
import java.net.URI;
import java.util.HashMap;

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
public class TestWrapSequence {
	
	private static CommandLineXmlProcessorCalabash processor;

	@BeforeClass
	public static void setupOnce() {
		TestWrapSequence.processor = new CommandLineXmlProcessorCalabash();
	}
	
	@Before
	public void setup() {
		TestWrapSequence.processor.reset();
	}
	
	/**
	 * Output a result built using steps imported from a library.
	 * @throws Exception
	 */
	@Test
	public void testWrapSequence_helloWorld() throws Exception {	
		
		File input1File = TestCommandLineXmlProcessor.getFile("/data/source/rainbow_colours.xml", true);
		File input2File = TestCommandLineXmlProcessor.getFile("/data/source/rainbow_mnemonic.xml", true);
		
		HashMap<String, URI> inputs = new HashMap<String, URI>();
		inputs.put("source", input1File.toURI());
		inputs.put("supplement", input2File.toURI());
				
		processor.setPipeline(new URI(TestCommandLineXmlProcessor.getFile("/xproc/valid/wrap-sequence/rainbow.xpl", false).toURI().toString()));
		processor.setInputs(inputs);
		
		String expected = FileUtils.readFileToString(new File(TestCommandLineXmlProcessor.getFile("/data/source/rainbow.xml", true).getAbsolutePath()), "UTF-8");			
				
		processor.execute();			
		
		XMLUnit.setIgnoreWhitespace(true);
		
		assertXMLEqual(expected, processor.getResponse());
		
	}
	
}
