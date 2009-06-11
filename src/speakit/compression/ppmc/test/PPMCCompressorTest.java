package speakit.compression.ppmc.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.SpeakitLogger;
import speakit.TextDocument;
import speakit.compression.arithmetic.ArithmeticCompressor;
import speakit.compression.ppmc.PPMC;

public class PPMCCompressorTest {
	
	private PPMC ppmc;
	ByteArrayOutputStream out;

	@Before
	public void setUp() throws Exception {
		out = new ByteArrayOutputStream();
		ppmc = new PPMC(out);
		
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testCompress() throws IOException {
		SpeakitLogger.activate();
		//this.ppmc.compress(new TextDocument("TATATAAAAALO"));
		//this.ppmc.compress(new TextDocument("ABAC"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		this.ppmc.compress(new TextDocument("TATATAAAAALO"));
		
		
	}

}
