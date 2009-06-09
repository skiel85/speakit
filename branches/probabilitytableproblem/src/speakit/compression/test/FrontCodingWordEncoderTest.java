package speakit.compression.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.frontcoding.FrontCodedWord;
import speakit.compression.frontcoding.FrontCodingWordEncoder;


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
		String word3 = "cuadro";
		
		testEncode(encoder.encode(word1), (short) 0, word1);
		testEncode(encoder.encode(word2), (short) 3, "derno");
		testEncode(encoder.encode(word3), (short) 4, "ro");

	
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
		testEncode(encoder.encode(word2), (short) word1.length(), "");
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
		testEncode(encoder.encode("cordon"), (short) 2, "rdon");
		testEncode(encoder.encode("cordura"), (short) 4, "ura");
	}
	
	@Test
	public void testLargeText2() {

		testEncode(encoder.encode("cuadrado"), (short) 0, "cuadrado");
		testEncode(encoder.encode("cuadratura"), (short) 6, "tura");
		testEncode(encoder.encode("cuaderno"), (short) 4, "erno");
		testEncode(encoder.encode("codera"), (short) 1, "odera");
		testEncode(encoder.encode("cereza"), (short) 1, "ereza");
		testEncode(encoder.encode("arbol"), (short) 0, "arbol");
		testEncode(encoder.encode("azul"), (short) 1, "zul");
		testEncode(encoder.encode("arboldos"), (short) 1, "rboldos");
	}
	
	@Test
	public void testLargeText3() {

		testEncode(encoder.encode("cuadradocuadrado"), (short) 0, "cuadradocuadrado");
		testEncode(encoder.encode("pepepepecuadrado"), (short) 0, "pepepepecuadrado");
	}
	
	@Test
	public void testOneNullWord() {
		testEncode(encoder.encode(""), (short) 0, "");
		testEncode(encoder.encode("parabrisas"), (short) 0, "parabrisas");
		testEncode(encoder.encode(""), (short) 0, "");
		testEncode(encoder.encode("parabrisas"), (short) 0, "parabrisas");  
	}
	
	@Test
	public void testPrefixWords() { 
		testEncode(encoder.encode(""), (short) 0, "");
		testEncode(encoder.encode("parabrisas"), (short) 0, "parabrisas");
		testEncode(encoder.encode("para"), (short) 4, ""); 
		testEncode(encoder.encode("p"), (short) 1, "");
		testEncode(encoder.encode("papelon"), (short) 1, "apelon");
		testEncode(encoder.encode("papel"), (short) 5, "");
		testEncode(encoder.encode("gato"), (short) 0, "gato");
	}
	
	@Test
	public void testSomeEqualWords() {  
		testEncode(encoder.encode("arroz"), (short) 0, "arroz");
		testEncode(encoder.encode("arroz"), (short) 5, ""); 
		testEncode(encoder.encode("papelon"), (short) 0, "papelon");
		testEncode(encoder.encode("arroz"), (short) 0, "arroz"); 
	}

	private void testEncode(FrontCodedWord wordencoded, short expectedMatchingCount, String expectedEnding) {
		Assert.assertEquals(expectedEnding,wordencoded.getEndingCharacters());
		Assert.assertEquals(expectedMatchingCount,wordencoded.getMatchingCharacters());
	}

}
