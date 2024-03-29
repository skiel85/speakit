package speakit.wordreader.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
		Assert.assertEquals("excepci�n", this.sut.replaceStrangeCharacters("excepci�n"));
	}

	@Test
	public void testLongTextTakenFromWikipedia() {
		Assert.assertEquals("Todo ser inteligente  dec�a el ge�metra  debe comprender el destino cient�fico de esta figura ".toLowerCase(), this.sut.replaceStrangeCharacters("Todo ser inteligente -dec�a el ge�metra- debe comprender el destino cient�fico de esta figura."));
	}

	@Test
	public void testCollapseSpaces() {
		Assert.assertEquals("hola mundo".toLowerCase(), this.sut.collapseSpaces("hola    mundo"));
	}

	@Test
	public void testCollapseSpacesFromLongTextTakenFromWikipedia() {
		Assert.assertEquals("Todo ser inteligente dec�a el ge�metra debe comprender el destino cient�fico de esta figura".toLowerCase(), this.sut.collapseSpaces("Todo ser inteligente  dec�a el ge�metra  debe comprender el destino cient�fico de esta figura "));
	}

	@Test
	public void testCleanText() {
		Assert.assertEquals("Todo ser inteligente dec�a el ge�metra debe comprender el destino cient�fico de esta figura Los selenitas si existen responder�n con una figura semejante y una vez establecida la comunicaci�n f�cil ser� crear un alfabeto que permita conversar con los habitantes de la Luna".toLowerCase(), this.sut
				.cleanText("Todo ser inteligente -dec�a el ge�metra- debe comprender el destino cient�fico de esta figura. Los selenitas, si existen, responder�n con una figura semejante, y una vez establecida la comunicaci�n, f�cil ser� crear un alfabeto que permita conversar con los habitantes de la Luna"));
	}

	@Test
	public void testDiaeresis() {
		Assert.assertEquals("Arg�ello".toLowerCase(), this.sut.cleanText("Arg�ello"));
	}

	@Test
	public void testGetWords() {
		String text = "Todo ser inteligente";
		String[] expecteds = new String[] { "todo", "ser", "inteligente" };
		String[] actuals = this.sut.getWords(text);

		Assert.assertArrayEquals(expecteds, actuals);
	}

	@After
	public void tearDown() throws Exception {
	}

}
