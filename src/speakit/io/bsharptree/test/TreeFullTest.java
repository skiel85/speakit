package speakit.io.bsharptree.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.TextDocument;
import speakit.ftrs.index.InvertedIndexIndexRecord;
import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.bsharptree.Tree;
import speakit.io.bsharptree.RecordEncoder;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.test.TestFileManager;

@Ignore
public class TreeFullTest {

	private Tree<InvertedIndexIndexRecord, StringField>	sut;
	private TestFileManager										filemanager;
	private RecordEncoder										encoder;
	private TextDocument										wikipediaArticle;

	@Before
	public void setUp() throws Exception {
		this.filemanager = new TestFileManager("");
		encoder = new InvertedIndexIndexRecordEncoder();
		this.sut = new Tree<InvertedIndexIndexRecord, StringField>(filemanager.openFile("FullBSTree.dat"), InvertedIndexIndexRecord.createRecordFactory(), encoder);
		this.sut.create(150);
		wikipediaArticle = new TextDocument(
				"El brote de gripe A (H1N1) de 2009,60 causado por una variante del Influenzavirus A originalmente de origen porcino (subtipo H1N1), Seg�n la Organizaci�n Mundial de la Salud (OMS), los primeros casos de influenza en M�xico se detectaron el 11 de abril en el estado mexicano de Veracruz, pero el primer enfermo registrado en el mundo fue un ni�o de 10 a�os de edad quien enferm� el 30 de marzo en San Diego, Estados Unidos61 que no habia tenido ning�n contacto con cerdos y adem�s no habia ten�do ning�n antecedente de haber viajado a M�xico. Al mes se extendio por varios estados de M�xico (Distrito Federal, Estado de M�xico y San Luis Potos�) y Estados Unidos (Texas y California), para exportarse a partir de entonces, con aparici�n de numerosos casos en otros pa�ses de pacientes que hab�an viajado a M�xico. Se han constatado unos pocos casos de contagios indirectos, de personas que no han estado en dicha regi�n, que se han dado en Espa�a, Alemania, Corea del Sur y Reino Unido.62 El 29 de abril la Organizaci�n Mundial de la Salud (OMS) la clasific� como de nivel de alerta cinco, es decir, pandemia inminente.63 Ese nivel de alerta no define la gravedad de la enfermedad producida por el virus, sino su extensi�n geogr�fica.");
		for (String word : wikipediaArticle) {
			if (word.equals("porcino")) {
				System.out.println(word);
				System.out.println(this.sut.toString());
			}
			sut.insertRecord(new InvertedIndexIndexRecord(word, simulateBlockNumber(word)));
		}
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
	 * Cierra el archivo y realiza la prueba de obtener todos los registros
	 * 
	 * @throws IOException
	 */
	@Test
	public void testRetrieveAllFromRecentlyOpenBTree() throws IOException {
		Tree<InvertedIndexIndexRecord, StringField> newTree = new Tree<InvertedIndexIndexRecord, StringField>(filemanager.openFile("FullBSTree.dat"),
				InvertedIndexIndexRecord.createRecordFactory(), encoder);
		newTree.load();
		testRetrieveAllRecords(newTree, wikipediaArticle);
	}

	/**
	 * Simula un numero de bloque. Devuelve el tama�o de la palabra como numero
	 * de bloque
	 * 
	 * @param word
	 * @return
	 */
	private static int simulateBlockNumber(String word) {
		return word.length();
	}

	public static void testRetrieveAllRecords(Tree<InvertedIndexIndexRecord, StringField> sut, TextDocument words) throws RecordSerializationException, IOException {
		for (String word : words) {
			StringField key = new StringField(word);
			InvertedIndexIndexRecord record = sut.getRecord(key);
			verifyCorrectRecord(record, word, key);
		}
	}

	private static void verifyCorrectRecord(InvertedIndexIndexRecord record, String word, StringField key) {
		// verifica que el record obtenido sea el correcto
		Assert.assertEquals(0, record.getKey().compareTo(key));
		Assert.assertEquals(simulateBlockNumber(word), record.getBlockNumber());
	}

}
