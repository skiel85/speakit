package speakit.compression.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.FrontCodedWord;
import speakit.compression.FrontCodingWordEncoder;

public class FrontCodingWordEncoderTest {

	private FrontCodingWordEncoder encoder;

	@Before
	public void setUp() throws Exception {
		encoder = new FrontCodingWordEncoder();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testEncodeSingleWord() {
		String word1 = "casa";
		testEncode(encoder.encode(word1), (short) 0, word1);
	}

	@Test
	public void testEncodeSimilarWords() {
		String word1 = "cuantica";
		String word2 = "cuaderno";

		testEncode(encoder.encode(word1), (short) 0, word1);
		testEncode(encoder.encode(word2), (short) 3, "erno");
	}

	@Test
	public void testEncodeDifferentWords() {
		String word1 = "alarma";
		String word2 = "videocable";
		
		testEncode(encoder.encode(word1), (short) 0, word1);
		testEncode(encoder.encode(word2), (short) 0, word2);
	}

	@Test
	public void testEqualWords() {
		String word1 = "razonar";
		String word2 = "razonar";

		testEncode(encoder.encode(word1), (short) 0, word1);
		testEncode(encoder.encode(word2), (short) word2.length(), "");
	}

	@Test
	public void testFirstWordNullWords() {
		String word1 = "";
		String word2 = "panza";

		testEncode(encoder.encode(word1), (short) 0, word1);
		testEncode(encoder.encode(word2), (short) 0, word2);
	}

	@Test
	public void testLargeText() {
		testEncode(encoder.encode("codo"), (short) 0, "codo");
		testEncode(encoder.encode("codazo"), (short) 3, "azo");
		testEncode(encoder.encode("codearse"), (short) 3, "earse");
		testEncode(encoder.encode("codera"), (short) 4, "ra");
		testEncode(encoder.encode("cordon"), (short) 2, "don");
		testEncode(encoder.encode("cordura"), (short) 4, "ura");
	}

	private void testEncode(FrontCodedWord wordencoded, short matchingChars,
			String end) {
		Assert.assertEquals(wordencoded.getMatchingCharacters(), matchingChars);
		Assert.assertEquals(wordencoded.getEndingCharacters(), end);
	}

}
