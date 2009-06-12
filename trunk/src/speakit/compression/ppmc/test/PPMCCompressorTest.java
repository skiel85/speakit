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
		ppmc = new PPMC(out,1);
		
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testCompress() throws IOException {
		SpeakitLogger.activate();
		//this.ppmc.compress(new TextDocument("TATATAAAAALO"));
		
		
		byte[] compressedbytes = this.compress("ABAC");
		byte[] sourcebytes = "ABAC".getBytes();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//SpeakitLogger.Log("***Descomprimiendo: " + article);
		PPMC compressor = new PPMC(out,1);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
		
		
	}
	
	private byte[] compress(String document) throws IOException {
		this.ppmc.compress(new TextDocument(document));
		// SpeakitLogger.Log(out.toString());
		return out.toByteArray();
	}

}
