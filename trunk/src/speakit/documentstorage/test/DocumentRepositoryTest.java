package speakit.documentstorage.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.dictionary.files.RecordSerializationException;
import speakit.documentstorage.DocumentRepository;

public class DocumentRepositoryTest {
	File file;
	DocumentRepository sut;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.sut = new DocumentRepository();
		this.sut.load(file);
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testAddAndGetOneDocument() throws IOException, RecordSerializationException {
		String text = "esto es una prueba";
		Long id = this.sut.store(new TextDocument(text));
		Assert.assertEquals(text, this.sut.getById(id).getText());
	}

	@Test
	public void testAddSomeAudiosAndGetOne() throws IOException, RecordSerializationException {
		String text1 = "ue un escultor, arquitecto y pintor italiano renacentista, considerado uno de los más grandes artistas de la historia. Miguel Ángel (llamado Michelangelo)";
		String text2 = "fue uno de los artistas más reconocidos por sus esculturas, pinturas y arquitectura. Realizó su labor artística durante más de setenta años entre ";
		String text3 = "Florencia y Roma, que era donde vivían sus grandes mecenas, la familia Médicis de Florencia, y los diferentes papas romanos. Fue el primer artista occidental del que se publicaron dos biografías en vida";
		Long id1 = this.sut.store(new TextDocument(text1));
		Long id2 = this.sut.store(new TextDocument(text2));
		Long id3 = this.sut.store(new TextDocument(text3));
		Assert.assertEquals(text2, this.sut.getById(id2).getText());		
		Assert.assertEquals(text3, this.sut.getById(id3).getText());
		Assert.assertEquals(text1, this.sut.getById(id1).getText());
		String text4 = "Vita de Michelangelo Buonarroti, escrita en 1553 por Ascanio Condivi, pintor y discípulo de Miguel Ángel, que recoge los datos facilitados por el mismo Buonarroti.";
		Long id4 = this.sut.store(new TextDocument(text4));
		Assert.assertEquals(text4, this.sut.getById(id4).getText());
		Assert.assertEquals(text1, this.sut.getById(id1).getText());
		Assert.assertEquals(text2, this.sut.getById(id2).getText());		
		Assert.assertEquals(text3, this.sut.getById(id3).getText());		
		Assert.assertEquals(text4, this.sut.getById(id4).getText());
	}
}
