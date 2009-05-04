package speakit.test;

import java.io.IOException;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.Speakit;
import speakit.TextDocument;
import speakit.documentstorage.TextDocumentList;

public class SpeakitSearchTest {

	private static final TextDocument	DOC1	= new TextDocument("los apuntes del cei");
	private static final TextDocument	DOC2	= new TextDocument("que apuntes uso");
	private FileManager	fileManager;
	private Speakit		sut;

	@Before
	public void setUp() throws Exception {
		this.fileManager = new TestFileManager(this.getClass().getName());
		this.sut = new Speakit();
		Configuration conf = new Configuration();
		conf.setBlockSize(1024);
		conf.setTrieDepth(4);
		this.sut.install(this.fileManager, conf);
		this.sut.load(this.fileManager);
		
		this.sut.addDocument(DOC1);
		this.sut.addDocument(DOC2);
	}

	@After
	public void tearDown() throws Exception {
		this.fileManager.destroyFiles();
	}
	
	@Test
	public void testSearch() throws IOException{ 
		TextDocumentList result = this.sut.search(new TextDocument("apuntes"));
		Iterator<TextDocument> iterator = result.iterator();
		Assert.assertEquals(DOC1.getText(), iterator.next().getText());
		Assert.assertEquals(DOC2.getText(), iterator.next().getText());
	}
}
