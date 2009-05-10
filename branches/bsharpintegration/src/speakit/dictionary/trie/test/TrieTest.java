package speakit.dictionary.trie.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.TextDocument;
import speakit.dictionary.audiofile.WordNotFoundException;
import speakit.dictionary.trie.Trie;
import speakit.io.bsharptree.test.TreeFullTest;
import speakit.io.record.RecordSerializationException;
import speakit.test.SpeakitSearchRealDocumentsTest;
import speakit.test.TestFileManager;

public class TrieTest {

	Trie trie;
	TestFileManager fileManager;
	private Configuration conf;

	@Before
	public void setUp() throws Exception {
		fileManager = new TestFileManager(this.getClass().getName());
		trie = new Trie();
		conf = new Configuration();
		conf.setTrieDepth(4);
		conf.setBlockSize(1512);
		trie.install(fileManager, conf);
	}

	@After
	public void tearDown() throws Exception {
		this.fileManager.destroyFiles();
	}

	
	@Test
	public void testAddWord() throws IOException, WordNotFoundException, RecordSerializationException {
		this.trie.addWord("o", 15);
		this.trie.addWord("ah", 12150);
		this.trie.addWord("hola", 30);
		this.trie.addWord("hora", 45);
		this.trie.addWord("ahi", 12152);
		this.trie.addWord("ahora", 1000);
		
		//Assert.assertTrue(this.trie.contains("hola"));
		Assert.assertTrue(this.trie.contains("o"));
		Assert.assertTrue(this.trie.contains("ah"));
		Assert.assertTrue(this.trie.contains("hola"));
		Assert.assertTrue(this.trie.contains("hora"));
		Assert.assertTrue(this.trie.contains("ahi"));
		Assert.assertTrue(this.trie.contains("ahora"));
		
		Assert.assertEquals(15,this.trie.getOffset("o"));
		Assert.assertEquals(12150,this.trie.getOffset("ah"));
		Assert.assertEquals(30,this.trie.getOffset("hola"));
		Assert.assertEquals(45,this.trie.getOffset("hora"));
		Assert.assertEquals(12152,this.trie.getOffset("ahi"));
		Assert.assertEquals(1000,this.trie.getOffset("ahora"));
		
		//Assert.assertEquals(12152, this.trie.getOffset("hora"));
	}

	
	@Test
	public void testAddBigWord() throws IOException, WordNotFoundException, RecordSerializationException {
		this.trie.addWord("supercalifragilisticoespialidoso", 3);
		Assert.assertTrue(this.trie.contains("supercalifragilisticoespialidoso"));
		Assert.assertEquals(3, this.trie.getOffset("supercalifragilisticoespialidoso"));
	}

	
	@Test
	public void testAddFewWordsWithSameBegining() throws RecordSerializationException, IOException, WordNotFoundException {
		this.trie.addWord("codo", 3);
		this.trie.addWord("codazo", 3);
		Assert.assertTrue(this.trie.contains("codazo"));
		Assert.assertEquals(3, this.trie.getOffset("codazo"));
		
	}

	
	@Test
	public void testAddSeveralWordsWithSameBegining1() throws Exception {
		String[] words = new String[] { "cama", "casa", "casarse", "casino", "catarvino" };
		testAddWords(trie, words, fileManager, conf);
	}

	
	@Test
	public void testAddSeveralWordsWithSameBegining2() throws Exception {
		String[] words = new String[] { "codo", "codazo", "codearse", "codera", "cordon", "cordura" };
		testAddWords(trie, words, fileManager, conf);
	}

	public static void testAddWords(Trie initialTrie, String[] words, FileManager fileManager, Configuration conf) throws Exception {
		for (int i = 0; i < words.length; i++) {
			String word = words[i];

//			System.out.println("Adding: " + word + ", " + i);
			initialTrie.addWord(word, i);

		}

		testContainsAllWords(initialTrie, words);

		Trie loadedTrie = new Trie();
		loadedTrie.load(fileManager, conf);

		testContainsAllWords(loadedTrie, words);
	}
	
	
	@Test
	public void testAddArticleWords() throws IOException, WordNotFoundException, RecordSerializationException {
		List<TextDocument> documents=new ArrayList<TextDocument>();
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_ABSTRACCION);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_ADOLESCENCIA);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_AUTOESTIMA);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_CONECTIVISMO);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_EFECTO_TETRIS);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_EQUIPAMIENTO);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_INSTINTO);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_PORTUGA);
		
		ArrayList<String> words = new ArrayList<String>();
		for (TextDocument textDocument : documents) {
			for (String word : textDocument) {
				if (!words.contains(word)) {
					words.add(word);
					this.trie.addWord(word, TreeFullTest.simulateBlockNumber(word));
				}
			}
		}
		System.out.println("Buscando " + words.size() + " palabras ...");
		testContainsAllWords(words);
		this.trie=new Trie( );
		this.trie.load(fileManager, conf);
		testContainsAllWords(words);
	}

	private void testContainsAllWords(ArrayList<String> words) throws WordNotFoundException, RecordSerializationException, IOException {
		for (String word : words) {
			long offset = this.trie.getOffset(word);
			System.out.println("("+word+","+offset+")");
			Assert.assertEquals("No encontró \"" + word + "\"" ,TreeFullTest.simulateBlockNumber(word), offset);
		}
	}

	private static void testContainsAllWords(Trie trie, String[] words) throws RecordSerializationException, IOException, WordNotFoundException {
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			Assert.assertTrue("No contiene " + word, trie.contains(word));
		}
	}

}
