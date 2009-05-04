package speakit.compression.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.compression.FrontCodedWord;
import speakit.compression.FrontCodingCompressor;


public class FrontCodingCompressorTest {

	private FrontCodingCompressor compressor;

	@Before
	public void setUp() throws Exception {
		compressor = new FrontCodingCompressor();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Ignore
	@Test
	public void testEncode() {
		String[] textToCompress = new String[] { "codo", "codazo", "codearse", "codera", "cordon", "cordura" };
		FrontCodedWord[] frontCodedWords = compressor.compress(textToCompress);
		String[] descompressedText = compressor.decompress(frontCodedWords);
	
		Assert.assertArrayEquals(textToCompress, descompressedText);
	}

}
