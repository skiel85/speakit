package speakit.compression.lzp.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.compression.lzp.LZP;

public class TestLZP {

	private LZP lzp;
	@Before
	public void setUp() throws Exception {
		lzp = new LZP();
	}
	@Test
	public void testCompress() throws IOException {
		lzp.compress(new TextDocument("hola loco"));
	}


}
