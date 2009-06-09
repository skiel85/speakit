package speakit.wordreader.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.wordreader.WordReaderImpl;

public class WordReaderImplTest {

	private WordReaderImpl sut;

	@Before
	public void setUp() throws Exception {
		this.sut = new WordReaderImpl(new ByteArrayInputStream("hola mundo".getBytes()));
	}

	@Test
	public void testGetFirstWord() throws IOException {
		Assert.assertEquals("hola", sut.next());
	}

	@Test
	public void testGetSecondWord() throws IOException {
		sut.next();
		Assert.assertEquals("mundo", sut.next());
	}

	@Test
	public void testHasNext() throws IOException {
		Assert.assertTrue(sut.hasNext());
	}

	@Test
	public void testHasNextAfterGettingNextAtFirstTime() throws IOException {
		sut.next();
		Assert.assertTrue(sut.hasNext());
	}

	@Test
	public void testNotHasNext() throws IOException {
		sut.next();
		sut.next();
		Assert.assertFalse(sut.hasNext());
	}

	@Test
	public void testGetAllWords() throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		while (sut.hasNext()) {
			list.add(sut.next());
		}
		Assert.assertArrayEquals(new String[] { "hola", "mundo" }, list.toArray());
	}

	@Test
	public void testGetAllWordsFromLongTextTakenFromWikipedia() throws IOException {
		WordReaderImpl sut = new WordReaderImpl(new ByteArrayInputStream("Todo ser inteligente -decía el geómetra- debe comprender el destino científico de esta figura. Los selenitas, si existen, responderán con una figura semejante, y una vez establecida la comunicación, fácil será crear un alfabeto que permita conversar con los habitantes de la Luna".getBytes()));
		ArrayList<String> list = new ArrayList<String>();
		while (sut.hasNext()) {
			list.add(sut.next());
		}
		Assert.assertArrayEquals(new String[] { "todo", "ser", "inteligente", "decía", "el", "geómetra", "debe", "comprender", "el", "destino", "científico", "de", "esta", "figura", "los", "selenitas", "si", "existen", "responderán", "con", "una", "figura", "semejante", "y", "una", "vez", "establecida", "la", "comunicación", "fácil", "será", "crear", "un", "alfabeto", "que", "permita", "conversar", "con", "los", "habitantes", "de", "la", "luna" }, list.toArray());
	}
}
