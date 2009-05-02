package speakit.documentstorage.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.TextDocument;
import speakit.documentstorage.DocumentRepository;
import speakit.io.record.RecordSerializationException;
import speakit.test.TestFileManager;

public class DocumentRepositoryTest {

	DocumentRepository	sut;
	private FileManager	fileSet;

	@Before
	public void setUp() throws Exception {
		this.fileSet = new TestFileManager(this.getClass().getName());
		this.sut = new DocumentRepository();
		this.sut.load(fileSet, new Configuration());
	}

	@After
	public void tearDown() throws Exception {
		this.fileSet.destroyFiles();
	}

	@Test
	public void testAddAndGetOneDocument() throws IOException, RecordSerializationException {
		String text = "esto es una prueba";
		TextDocument doc = new TextDocument(text);
		this.sut.store(doc);
		Assert.assertEquals(text, this.sut.getById(doc.getId()).getText());
	}

	@Test
	public void testAddSomeAudiosAndGetOne() throws IOException, RecordSerializationException {
		String text1 = "ue un escultor, arquitecto y pintor italiano renacentista, considerado uno de los más grandes artistas de la historia. Miguel Ángel (llamado Michelangelo)";
		String text2 = "fue uno de los artistas más reconocidos por sus esculturas, pinturas y arquitectura. Realizó su labor artística durante más de setenta años entre ";
		String text3 = "Florencia y Roma, que era donde vivían sus grandes mecenas, la familia Médicis de Florencia, y los diferentes papas romanos. Fue el primer artista occidental del que se publicaron dos biografías en vida";
		TextDocument doc1 = new TextDocument(text1);
		this.sut.store(doc1);
		TextDocument doc2 = new TextDocument(text2);
		this.sut.store(doc2);
		TextDocument doc3 = new TextDocument(text3);
		this.sut.store(doc3);
		Assert.assertEquals(text2, this.sut.getById(doc2.getId()).getText());
		Assert.assertEquals(text3, this.sut.getById(doc3.getId()).getText());
		Assert.assertEquals(text1, this.sut.getById(doc1.getId()).getText());
		String text4 = "Vita de Michelangelo Buonarroti, escrita en 1553 por Ascanio Condivi, pintor y discípulo de Miguel Ángel, que recoge los datos facilitados por el mismo Buonarroti.";
		TextDocument doc4 = new TextDocument(text4);
		this.sut.store(doc4);
		Assert.assertEquals(text4, this.sut.getById(doc4.getId()).getText());
		Assert.assertEquals(text1, this.sut.getById(doc1.getId()).getText());
		Assert.assertEquals(text2, this.sut.getById(doc2.getId()).getText());
		Assert.assertEquals(text3, this.sut.getById(doc3.getId()).getText());
		Assert.assertEquals(text4, this.sut.getById(doc4.getId()).getText());
	}

	@Test
	public void testStoreChangesTextDocumentId() throws IOException, RecordSerializationException {
		String text1 = "fue uno de los artistas más reconocidos por sus esculturas, pinturas y arquitectura. Realizó su labor artística durante más de setenta años entre ";
		TextDocument doc1 = new TextDocument(text1);
		doc1.setId(-1L);
		Assert.assertEquals(-1L, doc1.getId());
		this.sut.store(doc1);
		Assert.assertEquals(0,doc1.getId());
	}
}
