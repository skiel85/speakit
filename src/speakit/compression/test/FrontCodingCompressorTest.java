package speakit.compression.test;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.compression.FrontCodedWord;
import speakit.compression.FrontCodingCompressor;


public class FrontCodingCompressorTest {

	private static final String	WikipediaArticle	= "Se conoce como Revolución de Mayo a la serie de eventos revolucionarios que sucedieron en mayo de 1810 en la ciudad de Buenos Aires, por aquel entonces capital del Virreinato del Río de la Plata, una dependencia colonial de España. Como consecuencia de la revolución fue depuesto el virrey Baltasar Hidalgo de Cisneros y reemplazado por la Primera Junta. La Revolución de Mayo inició el proceso de surgimiento del Estado Argentino sin proclamación de la independencia formal, ya que la Primera Junta no reconocía la autoridad del Consejo de Regencia de España e Indias, pero aún gobernaba nominalmente en nombre del rey de España Fernando VII, quien había sido depuesto por las Abdicaciones de Bayona y su lugar ocupado por el francés José Bonaparte. Aun así, los historiadores consideran a dicha manifestación de lealtad (conocida como la máscara de Fernando VII) una maniobra política que ocultaba las auténticas intenciones independentistas de los revolucionarios. La declaración de independencia de la Argentina tuvo lugar durante el Congreso de Tucumán el 9 de julio de 1816";
	private FrontCodingCompressor compressor;

	@Before
	public void setUp() throws Exception {
		compressor = new FrontCodingCompressor();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testEncodeDecode() {
		String[] textToCompress = new String[] { "codo", "codazo", "codearse", "codera", "cordon", "cordura" };
		FrontCodedWord[] frontCodedWords = compressor.compress(textToCompress);
		String[] descompressedText = compressor.decompress(frontCodedWords);

		Assert.assertArrayEquals(textToCompress, descompressedText); 
	}
	
	@Test
	public void testEncodeDecode2() {
		String[] textToCompress = new String[] { "", "parabrisas", "para", "p", "papelon", "papel","gato" };
		FrontCodedWord[] frontCodedWords = compressor.compress(textToCompress);
		String[] descompressedText = compressor.decompress(frontCodedWords);
		Assert.assertArrayEquals(textToCompress, descompressedText);
	}
	
	@Test
	public void testEncodeDecodeWikipediaArticle() { 
		TextDocument article = new TextDocument( WikipediaArticle);
		ArrayList<String> sortedWords = new ArrayList<String>();
		for (String word : article) {
			sortedWords.add(word);
		} 
		Collections.sort(sortedWords);
		String[] sortedWordsArray = (String[])sortedWords.toArray(new String[sortedWords.size()]);
		FrontCodedWord[] frontCodedWords = compressor.compress(sortedWordsArray);
		String[] descompressedText = compressor.decompress(frontCodedWords);

		Assert.assertArrayEquals(sortedWordsArray, descompressedText);
	}
}
