package speakit.dictionary.trie.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.dictionary.audiofile.WordNotFoundException;
import speakit.dictionary.trie.Trie;
import speakit.io.record.RecordSerializationException;
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
		conf.setBlockSize(512);
		trie.install(fileManager, conf);
	}

	@After
	public void tearDown() throws Exception {
		this.fileManager.destroyFiles();
	}

	
	@Test
	public void testAddWord() throws IOException, WordNotFoundException, RecordSerializationException {
		this.trie.addWord("hola", 12150);
		this.trie.addWord("hora", 12152);
		this.trie.addWord("horma", 1000);
		this.trie.addWord("gatos", 15);
		//Assert.assertTrue(this.trie.contains("hola"));
		Assert.assertTrue(this.trie.contains("horma"));
		Assert.assertTrue(this.trie.contains("gatos"));
		Assert.assertEquals(12152, this.trie.getOffset("hora"));
	}

	@Ignore
	@Test
	public void testAddBigWord() throws IOException, WordNotFoundException, RecordSerializationException {
		this.trie.addWord("supercalifragilisticoespialidoso", 3);
		Assert.assertTrue(this.trie.contains("supercalifragilisticoespialidoso"));
		Assert.assertEquals(3, this.trie.getOffset("supercalifragilisticoespialidoso"));
	}

	@Ignore
	@Test
	public void testAddFewWordsWithSameBegining() throws RecordSerializationException, IOException {
		this.trie.addWord("codo", 3);
		this.trie.addWord("codazo", 3);
	}

	@Ignore
	@Test
	public void testAddSeveralWordsWithSameBegining1() throws Exception {
		String[] words = new String[] { "cama", "casa", "casarse", "casino", "catarvino" };
		testAddWords(trie, words, fileManager, conf);
	}

	@Ignore
	@Test
	public void testAddSeveralWordsWithSameBegining2() throws Exception {
		String[] words = new String[] { "codo", "codazo", "codearse", "codera", "cordon", "cordura" };
		testAddWords(trie, words, fileManager, conf);
	}

	public static void testAddWords(Trie initialTrie, String[] words, FileManager fileManager, Configuration conf) throws Exception {
		for (int i = 0; i < words.length; i++) {
			String word = words[i];

			System.out.println("Adding: " + word + ", " + i);
			initialTrie.addWord(word, i);

		}

		testContainsAllWords(initialTrie, words);

		Trie loadedTrie = new Trie();
		loadedTrie.load(fileManager, conf);

		testContainsAllWords(loadedTrie, words);
	}

	private static void testContainsAllWords(Trie trie, String[] words) throws RecordSerializationException, IOException, WordNotFoundException {
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			Assert.assertTrue("No contiene " + word, trie.contains(word));
			Assert.assertEquals(i, trie.getOffset("supercalifragilisticoespialidoso"));
		}
	}

}
