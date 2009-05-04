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
		String[] array = new String[1];
		array[0] = word1;
		
		testEncode(encoder.encode(array,word1), (short) 0, word1);
	}

	@Test
	public void testEncodeSimilarWords() {
		String word1 = "cuantica";
		String word2 = "cuaderno";
		String word3 = "cuadro";
		String[] array = new String[3];
		array[0] = word1;
		array[1] = word2;
		array[2] = word3;
		

		testEncode(encoder.encode(array,word1), (short) 0, word1);
		testEncode(encoder.encode(array,word2), (short) 3, "derno");
		testEncode(encoder.encode(array,word3), (short) 4, "ro");
	}

	@Test
	public void testEncodeDifferentWords() {
		String word1 = "alarma";
		String word2 = "videocable";
		String[] array = new String[2];
		array[0] = word1;
		array[1] = word2;

		testEncode(encoder.encode(array,word1), (short) 0, word1);
		testEncode(encoder.encode(array,word2), (short) 0, word2);
	}

	@Test
	public void testEqualWords() {
		String word1 = "razonar";
		String word2 = "razonar";
		String[] array = new String[2];
		array[0] = word1;
		array[1] = word2;
		
		testEncode(encoder.encode(array,word1), (short) 0, word1);
		testEncode(encoder.encode(array,word2), (short) word1.length(), "");
	}

	@Test
	public void testFirstWordNullWords() {
		String word1 = "";
		String word2 = "panza";
		String[] array = new String[2];
		array[0] = word1;
		array[1] = word2;

		testEncode(encoder.encode(array,word1), (short) 0, word1);
		testEncode(encoder.encode(array,word2), (short) 0, word2);
	}

	@Test
	public void testLargeText() {
		
		String[] array = new String[]{"codo","codazo","codearse","codera","cordon","cordura"};
		testEncode(encoder.encode(array,"codo"), (short) 0, "codo");
		testEncode(encoder.encode(array,"codazo"), (short) 3, "azo");
		testEncode(encoder.encode(array,"codearse"), (short) 3, "earse");
		testEncode(encoder.encode(array,"codera"), (short) 4, "ra");
		testEncode(encoder.encode(array,"cordon"), (short) 2, "rdon");
		testEncode(encoder.encode(array,"cordura"), (short) 4, "ura");
	}

	private void testEncode(FrontCodedWord wordencoded, short matchingChars, String end) {
		Assert.assertEquals(wordencoded.getMatchingCharacters(), matchingChars);
		Assert.assertEquals(wordencoded.getEndingCharacters(), end);
	}

}
