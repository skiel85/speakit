package speakit.compression.ppmc.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.SpeakitLogger;
import speakit.TextDocument;
import speakit.compression.arithmetic.test.BruteForceCompressionTester;
import speakit.compression.arithmetic.test.SingleCompressionTester;
import speakit.compression.ppmc.PPMC;
import speakit.io.ByteArrayConverter;

public class PPMCCompressorTest {
	
	private PPMC ppmc;
	ByteArrayOutputStream out;

	@Before
	public void setUp() throws Exception {
		out = new ByteArrayOutputStream();
		ppmc = new PPMC(out,2);
		
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testCompress1() throws IOException {
		SpeakitLogger.activate();
		//this.ppmc.compress(new TextDocument("TATATAAAAALO"));
		
		//Este funciona ok
		byte[] compressedbytes = this.compress("ABAAAB");
		byte[] sourcebytes = "ABAAAB".getBytes();
		
		//Este no funciona
		//byte[] compressedbytes = this.compress("ABAAAC");
		//byte[] sourcebytes = "ABAAAC".getBytes();
		
		//Este no funciona
		//byte[] compressedbytes = this.compress("ABAAAD");
		//byte[] sourcebytes = "ABAAAD".getBytes();
		
		//byte[] compressedbytes = this.compress(".E.l. vuélo 447 de Air France France.");
		//byte[] sourcebytes = ".E.l. vuélo 447 de Air France France.".getBytes();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//SpeakitLogger.Log("***Descomprimiendo: " + article);
		PPMC compressor = new PPMC(out,2);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
		
		
	}
	
	//@Ignore
	@Test
	public void testCompress2() throws IOException {
		SpeakitLogger.activate();
		//this.ppmc.compress(new TextDocument("TATATAAAAALO"));
		
		//Este funciona ok
		//byte[] compressedbytes = this.compress("ABAAAB");
		//byte[] sourcebytes = "ABAAAB".getBytes();
		
		//Este no funciona
		byte[] compressedbytes = this.compress("ABAAAC");
		byte[] sourcebytes = "ABAAAC".getBytes();
		
		//Este no funciona
		//byte[] compressedbytes = this.compress("ABAAAD");
		//byte[] sourcebytes = "ABAAAD".getBytes();
		
		//byte[] compressedbytes = this.compress(".E.l. vuélo 447 de Air France France.");
		//byte[] sourcebytes = ".E.l. vuélo 447 de Air France France.".getBytes();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//SpeakitLogger.Log("***Descomprimiendo: " + article);
		PPMC compressor = new PPMC(out,2);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
		
		
	}
	
	@Test
	public void testCompress3() throws IOException {
		SpeakitLogger.activate();
		//this.ppmc.compress(new TextDocument("TATATAAAAALO"));
		
		//Este funciona ok
		//byte[] compressedbytes = this.compress("ABAAAB");
		//byte[] sourcebytes = "ABAAAB".getBytes();
		
		//Este no funciona
		//byte[] compressedbytes = this.compress("ABAAAC");
		//byte[] sourcebytes = "ABAAAC".getBytes();
		
		//Este no funciona
		byte[] compressedbytes = this.compress("ABAAAD");
		byte[] sourcebytes = "ABAAAD".getBytes();
		
		//byte[] compressedbytes = this.compress(".E.l. vuélo 447 de Air France France.");
		//byte[] sourcebytes = ".E.l. vuélo 447 de Air France France.".getBytes();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//SpeakitLogger.Log("***Descomprimiendo: " + article);
		PPMC compressor = new PPMC(out,2);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
		
		
	}
	
	private byte[] compress(String document) throws IOException {
		ppmc.compress(new TextDocument(document));
		return out.toByteArray();
	}
	
	private static byte[] compress(String document,PPMC ppmc,ByteArrayOutputStream out) throws IOException {
		ppmc.compress(new TextDocument(document));
		// SpeakitLogger.Log(out.toString());
		return out.toByteArray();
	}
	
	public static void testCompress(String file) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PPMC ppmc = new PPMC(out,2);
		//Este no funciona
		ppmc.compress(new TextDocument(file));
		byte[] compressedbytes = out.toByteArray(); 
		
		ByteArrayOutputStream decompressionOutput = new ByteArrayOutputStream(); 
		PPMC compressor = new PPMC(decompressionOutput,2);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(file, new String(decompressionOutput.toByteArray()));
		
	}
	
	//@Ignore
	@Test
	public void testCompressAAB() throws IOException{
		SpeakitLogger.activate();
		testCompress("aab");
	}
	
	@Ignore
	@Test
	public void testProgressiveTextFiles() throws IOException {
		SpeakitLogger.deactivate();
		BruteForceCompressionTester tester = new BruteForceCompressionTester(5,new SingleCompressionTester(){

			@Override
			public void testCompress(String file) throws IOException {
				PPMCCompressorTest.testCompress(file);
			}
			
		},true);
		tester.runTests();
	}

}
