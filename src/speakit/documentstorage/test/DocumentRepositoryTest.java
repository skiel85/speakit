package speakit.documentstorage.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.TextDocument;
import speakit.compression.arithmetic.ArithmeticCompressor;
import speakit.compression.arithmetic.test.ArithmeticCompressorTest;
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
		this.sut.store(doc,3);
		Assert.assertEquals(text, this.sut.getByIdOfCompressedDocument(doc.getId()).getText());
	}

	@Test
	public void testAddSomeAudiosAndGetOne() throws IOException, RecordSerializationException {
		String text1 = "ue un escultor, arquitecto y pintor italiano renacentista, considerado uno de los más grandes artistas de la historia. Miguel Ángel (llamado Michelangelo)";
		String text2 = "fue uno de los artistas más reconocidos por sus esculturas, pinturas y arquitectura. Realizó su labor artística durante más de setenta años entre ";
		String text3 = "Florencia y Roma, que era donde vivían sus grandes mecenas, la familia Médicis de Florencia, y los diferentes papas romanos. Fue el primer artista occidental del que se publicaron dos biografías en vida";
		TextDocument doc1 = new TextDocument(text1);
		this.sut.store(doc1,3);
		TextDocument doc2 = new TextDocument(text2);
		this.sut.store(doc2,3);
		TextDocument doc3 = new TextDocument(text3);
		this.sut.store(doc3,3);
		Assert.assertEquals(text2, this.sut.getByIdOfCompressedDocument(doc2.getId()).getText());
		Assert.assertEquals(text3, this.sut.getByIdOfCompressedDocument(doc3.getId()).getText());
		Assert.assertEquals(text1, this.sut.getByIdOfCompressedDocument(doc1.getId()).getText());
		String text4 = "Vita de Michelangelo Buonarroti, escrita en 1553 por Ascanio Condivi, pintor y discípulo de Miguel Ángel, que recoge los datos facilitados por el mismo Buonarroti.";
		TextDocument doc4 = new TextDocument(text4);
		this.sut.store(doc4,3);
		Assert.assertEquals(text4, this.sut.getByIdOfCompressedDocument(doc4.getId()).getText());
		Assert.assertEquals(text1, this.sut.getByIdOfCompressedDocument(doc1.getId()).getText());
		Assert.assertEquals(text2, this.sut.getByIdOfCompressedDocument(doc2.getId()).getText());
		Assert.assertEquals(text3, this.sut.getByIdOfCompressedDocument(doc3.getId()).getText());
		Assert.assertEquals(text4, this.sut.getByIdOfCompressedDocument(doc4.getId()).getText());
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
	
	@Test
	public void testStoreOneCompressedDocumentAndGetItDecompressed() throws IOException{
		String text = ".E.l. vuelo 447 de Air France fue un vuelo internacional entre el Aeropuerto Internacional de Galeão y el Aeropuerto Internacional Charles de Gaulle de París. El 1 de junio de 2009, el avión, un Airbus A330-200, registrado F-GZCP (primer vuelo el 25 de febrero de 2005),1 desapareció sobre el Océano Atlántico con 216 pasajeros, entre ellos 61 franceses, 58 brasileños, 26 alemanes, 71 de otras 29 nacionalidades2 y 11 tripulantes a bordo, incluyendo tres pilotos.3 4 Las autoridades de Brasil confirmaron que la Fuerza Aérea Brasileña se encuentra realizando una búsqueda con el avión militar C-130 Hercules en la zona del archipiélago de Fernando de Noronha, donde se cree que pudo haber caído la aeronave.5 El estado del avión y sus pasajeros es actualmente desconocido, pero tanto las declaraciones oficiales de Air France como del Gobierno de Francia presumen que la aeronave ha sufrido un accidente y que todas las personas que iban a bordo han fallecido.6 Se han localizado los restos de un avión cerca de las costas de Senegal; se cree que pueda ser el vuelo 447. Un testigo afirma que vio restos en llamas caer al mar. Otro piloto de la Fuerza Aérea Brasileña informó haber visto luces naranjas en el mar cerca al archipielago de Fernando de Noronha.7";
		TextDocument document1 = new TextDocument(text);
		TextDocument document2 = new TextDocument(text);
		TextDocument document3 = new TextDocument(text);
		this.sut.store(document1,1);
		this.sut.store(document2,2);
		this.sut.store(document3,3);
		
		
//		TextDocument documentDecompressed1 = this.sut.getByIdOfCompressedDocument(document1.getId());
		TextDocument documentDecompressed2 = this.sut.getByIdOfCompressedDocument(document2.getId());
		TextDocument documentDecompressed3 = this.sut.getByIdOfCompressedDocument(document3.getId());
		
//		Assert.assertEquals(document1.getText(), documentDecompressed1.getText());
		Assert.assertEquals(document2.getText(), documentDecompressed2.getText());
		Assert.assertEquals(document3.getText(), documentDecompressed3.getText());
	}
}
