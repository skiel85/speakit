package speakit.compression.ppmc.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.SpeakitLogger;
import speakit.TextDocument;
import speakit.compression.arithmetic.ArithmeticCompressor;
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
	public void testCompress() throws IOException {
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
	
	private byte[] compress(String document) throws IOException {
		this.ppmc.compress(new TextDocument(document));
		// SpeakitLogger.Log(out.toString());
		return out.toByteArray();
	}

}
