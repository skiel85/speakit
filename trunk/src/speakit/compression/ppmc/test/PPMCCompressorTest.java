package speakit.compression.ppmc.test;

import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.SpeakitLogger;
import speakit.TextDocument;
import speakit.compression.ppmc.PPMC;

public class PPMCCompressorTest {
	
	private PPMC ppmc;

	@Before
	public void setUp() throws Exception {
		ppmc = new PPMC();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testCompress() throws IOException {
		SpeakitLogger.activate();
		this.ppmc.compress(new TextDocument("ABAC"));
	}

}
