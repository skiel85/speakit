package speakit.wordreader.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.wordreader.TextCleaner;

public class TextCleanerTest {

	private TextCleaner sut;

	@Before
	public void setUp() throws Exception {
		this.sut = new TextCleaner();
	}

	@Test
	public void testReplaceStrangeCharacters() {
		Assert.assertEquals("hola  mundo", this.sut.replaceStrangeCharacters("hola, mundo"));
	}

	@Test
	public void testNotReplaceAccentedCharacters() {
		Assert.assertEquals("excepcion", this.sut.replaceStrangeCharacters("excepción"));
	}

	@Test
	public void testLongTextTakenFromWikipedia() {
		Assert.assertEquals("Todo ser inteligente  decia el geometra  debe comprender el destino cientifico de esta figura ".toLowerCase(), this.sut.replaceStrangeCharacters("Todo ser inteligente -decía el geómetra- debe comprender el destino científico de esta figura."));
	}

	@Test
	public void testCollapseSpaces() {
		Assert.assertEquals("hola mundo".toLowerCase(), this.sut.collapseSpaces("hola    mundo"));
	}

	@Test
	public void testCollapseSpacesFromLongTextTakenFromWikipedia() {
		Assert.assertEquals("Todo ser inteligente decía el geómetra debe comprender el destino científico de esta figura".toLowerCase(), this.sut.collapseSpaces("Todo ser inteligente  decía el geómetra  debe comprender el destino científico de esta figura "));
	}

	@Test
	public void testCleanText() {
		Assert.assertEquals("Todo ser inteligente decia el geometra debe comprender el destino cientifico de esta figura Los selenitas si existen responderan con una figura semejante y una vez establecida la comunicacion facil sera crear un alfabeto que permita conversar con los habitantes de la Luna".toLowerCase(), this.sut
				.cleanText("Todo ser inteligente -decia el geometra- debe comprender el destino cientifico de esta figura. Los selenitas, si existen, responderan con una figura semejante, y una vez establecida la comunicacion, facil sera crear un alfabeto que permita conversar con los habitantes de la Luna"));
	}

	@Test
	public void testDiaeresis() {
		Assert.assertEquals("Arguello".toLowerCase(), this.sut.cleanText("Argüello"));
	}

	@Test
	public void testGetWords() {
		String text = "Todo ser inteligente";
		String[] expecteds = new String[] { "todo", "ser", "inteligente" };
		String[] actuals = this.sut.getWords(text);

		Assert.assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void testGetRelevantWords(){
		String text = "un camión estaba ante nosotros argüello //,";
		TextDocument document = new TextDocument(text);
		Assert.assertEquals("camion arguello", this.sut.getRelevantWords(document).getText());
	}
	
	@Test
	public void testcleanDocument(){
		String text = "habia un        camión andando por la vereda, argüello// ";
		TextDocument document = new TextDocument(text);
		Assert.assertEquals("habia camion andando vereda arguello", this.sut.cleanDocument(document).getText());
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
