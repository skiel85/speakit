package speakit.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.Speakit;
import speakit.TextDocument;

public class SpeakitIssue40ReproductionTest {
	private static final String	DOC1	= "Jugó ocho partidos, metió tres goles (uno a Boca, nada menos), con 6,63 de Promedio es el mejor de River";
	private static final String	DOC2	= "Jugó siete partidos, no metió goles de Promedio es el mejor de Racing";
	private FileManager	fileManager;
	private Speakit		sut;

	@Before
	public void setUp() throws Exception {
		this.fileManager = new TestFileManager(this.getClass().getName());
		this.sut = new Speakit();
		Configuration conf = new Configuration();
		conf.setBlockSize(518);
		conf.setTrieDepth(4);
		this.sut.install(this.fileManager, conf);
		this.sut.load(this.fileManager);
	}

	@After
	public void tearDown() throws Exception {
		this.fileManager.destroyFiles();
	}

	@Test
	public void reproduceIssue40( ) throws FileNotFoundException, IOException {
		 this.sut.addDocument(new TextDocument( DOC1));
		 this.sut.addDocument(new TextDocument( DOC1));
		 this.sut.addDocument(new TextDocument( DOC2));
	}

}
