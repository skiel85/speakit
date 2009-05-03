package speakit.dictionary.trie.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.Configuration;
import speakit.dictionary.audiofile.WordNotFoundException;
import speakit.dictionary.trie.Trie;
import speakit.io.record.RecordSerializationException;
import speakit.test.TestFileManager;

public class TrieTest {

	Trie trie;
	TestFileManager fileManager;

	@Before
	public void setUp() throws Exception {
		fileManager = new TestFileManager(this.getClass().getName());
		trie = new Trie();
		Configuration conf = new Configuration();
		conf.setTrieDepth(4);
		conf.setBlockSize(512);
		trie.install(fileManager, conf);
	}

	@After
	public void tearDown() throws Exception {
		this.fileManager.destroyFiles();
	}

	@Ignore
	@Test
	public void testAddWord() throws IOException, WordNotFoundException, RecordSerializationException {
		this.trie.addWord("hola", 3);
		Assert.assertTrue(this.trie.contains("hola"));
		Assert.assertEquals(3, this.trie.getOffset("hola"));
	}

}
