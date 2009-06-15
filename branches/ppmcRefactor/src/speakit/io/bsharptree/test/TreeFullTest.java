package speakit.io.bsharptree.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.ftrs.index.InvertedIndexIndexRecord;
import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.bsharptree.RecordEncoder;
import speakit.io.bsharptree.Tree;
import speakit.io.bsharptree.TreeDuplicatedRecordException;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.test.TestFileManager;

public class TreeFullTest {
	

	/**
	 * Simula un numero de bloque. Devuelve el tamaño de la palabra como numero
	 * de bloque
	 * 
	 * @param word
	 * @return
	 */
	public static int simulateBlockNumber(String word) {
		return word.length();
	}

	public static int simulateUpdatedBlockNumber(String word) {
		return word.length() / 2;
	}

	public static void testRetrieveAllRecords(Tree<InvertedIndexIndexRecord, StringField> sut, Iterable<String> words) throws RecordSerializationException, IOException {
		for (String word : words) {
			StringField key = new StringField(word);
			InvertedIndexIndexRecord record = sut.getRecord(key);			
			verifyCorrectRecord(record, word, key);
		}
	}

	public static void verifyCorrectRecord(InvertedIndexIndexRecord record, String word, StringField key) {
		// verifica que el record obtenido sea el correcto
		Assert.assertNotNull("El arbol no devolvió ningún registro cuando se le pidió uno que había sido insertado. Palabra buscada: " + key.toString(), record);
		Assert.assertEquals(0, record.getKey().compareTo(key));
		Assert.assertEquals(simulateBlockNumber(word), record.getBlockNumber());
	}

	private Tree<InvertedIndexIndexRecord, StringField>	sut;

	private TestFileManager								filemanager;

	private RecordEncoder								encoder;

	private TextDocument								wikipediaArticle;

	@Before
	public void setUp() throws Exception {
		this.filemanager = new TestFileManager("");
		encoder = new InvertedIndexIndexRecordEncoder();
		this.sut = new Tree<InvertedIndexIndexRecord, StringField>(filemanager.openFile("FullBSTree.dat"), InvertedIndexIndexRecord.createRecordFactory(), encoder);
		this.sut.create(440);

		wikipediaArticle = new TextDocument(
				0,"El brote de gripe A (H1N1) de 2009,60 causado por una variante del Influenzavirus A originalmente de origen porcino (subtipo H1N1), Según la Organización Mundial de la Salud (OMS), los primeros casos de influenza en México se detectaron el 11 de abril en el estado mexicano de Veracruz, pero el primer enfermo registrado en el mundo fue un niño de 10 años de edad quien enfermó el 30 de marzo en San Diego, Estados Unidos61 que no habia tenido ningún contacto con cerdos y además no habia tenído ningún antecedente de haber viajado a México. Al mes se extendio por varios estados de México (Distrito Federal, Estado de México y San Luis Potosí) y Estados Unidos (Texas y California), para exportarse a partir de entonces, con aparición de numerosos casos en otros países de pacientes que habían viajado a México. Se han constatado unos pocos casos de contagios indirectos, de personas que no han estado en dicha región, que se han dado en España, Alemania, Corea del Sur y Reino Unido.62 El 29 de abril la Organización Mundial de la Salud (OMS) la clasificó como de nivel de alerta cinco, es decir, pandemia inminente.63 Ese nivel de alerta no define la gravedad de la enfermedad producida por el virus, sino su extensión geográfica.",
				true);

		insertAllWords(this.sut,wikipediaArticle);
	}
	/**
	 * Cierra el archivo y realiza la prueba de obtener todos los registros
	 * 
	 * @throws IOException
	 */
	@Test
	public void testRetrieveAllFromRecentlyOpenBTree() throws IOException {
		Tree<InvertedIndexIndexRecord, StringField> newTree = new Tree<InvertedIndexIndexRecord, StringField>(filemanager.openFile("FullBSTree.dat"), InvertedIndexIndexRecord
				.createRecordFactory(), encoder);
		newTree.load();
		testRetrieveAllRecords(newTree, wikipediaArticle);
	}

	/**
	 * Prueba obtener todos los registros
	 * 
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	@Test
	public void testRetrieveAllRecords() throws RecordSerializationException, IOException {
		testRetrieveAllRecords(this.sut, wikipediaArticle);
	}

	/**
	 * Prueba modificar todos los registros
	 * 
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	@Test
	public void testUpdateAllRecords() throws RecordSerializationException, IOException {
		for (String word : wikipediaArticle) {
			sut.updateRecord(new InvertedIndexIndexRecord(word, simulateUpdatedBlockNumber(word)));
		}
		for (String word : wikipediaArticle) {
			InvertedIndexIndexRecord record = this.sut.getRecord(new StringField(word));
			Assert.assertEquals(word, record.getKey().getString());
			Assert.assertEquals(simulateUpdatedBlockNumber(word), record.getBlockNumber());
		}
	}

	public static void insertAllWords(Tree<InvertedIndexIndexRecord, StringField> tree, Iterable<String> words) throws RecordSerializationException, IOException {
		for (String word : words) {
			try{
				 tree.insertRecord(new InvertedIndexIndexRecord(word, simulateBlockNumber(word)));				
			}catch (TreeDuplicatedRecordException e) {
				//words tiene palabras duplicadas, no interfiere con la prueba
			}
		}
	}

}
