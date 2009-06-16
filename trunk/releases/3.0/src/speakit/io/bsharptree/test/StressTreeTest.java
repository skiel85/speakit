package speakit.io.bsharptree.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
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
import speakit.test.files.TestDocumentRepository;

public class StressTreeTest {
	TestDocumentRepository repo;	

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

	/**
	 * definimos un tamaño de bloque real, por ejemplo 512 e insertamos una gran cantidad de palabras"
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		repo = new TestDocumentRepository();
		this.filemanager = new TestFileManager("");
		encoder = new InvertedIndexIndexRecordEncoder();
		this.sut = new Tree<InvertedIndexIndexRecord, StringField>(filemanager.openFile("FullBSTree.dat"), InvertedIndexIndexRecord.createRecordFactory(), encoder);
		//this.sut.create(256);
		this.sut.create(128);

		for (TextDocument article : repo.documents) {
			insertAllWords(this.sut, article);
		}
		OutputStream outputStream = new FileOutputStream( new File("tree.txt"));
		outputStream.write(this.sut.toString().getBytes());
		outputStream.close();
	}
	/**
	 * Cierra el archivo y realiza la prueba de obtener todos los registros
	 * 
	 * @throws IOException
	 */
	@Ignore
	@Test
	public void testRetrieveAllFromRecentlyOpenBTree() throws IOException {
		Tree<InvertedIndexIndexRecord, StringField> newTree = new Tree<InvertedIndexIndexRecord, StringField>(filemanager.openFile("FullBSTree.dat"), InvertedIndexIndexRecord
				.createRecordFactory(), encoder);
		newTree.load();
		for (TextDocument article : repo.documents) {
			testRetrieveAllRecords(newTree, article);
		}
	}

	/**
	 * Prueba obtener todos los registros
	 * 
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	@Ignore
	@Test
	public void testRetrieveAllRecords() throws RecordSerializationException, IOException {
		for (TextDocument article : repo.documents) {
			testRetrieveAllRecords(this.sut, article);
		}
	}

	public static void insertAllWords(Tree<InvertedIndexIndexRecord, StringField> tree, Iterable<String> words) throws RecordSerializationException, IOException {
		for (String word : words) {
			try{
				//if (word.compareToIgnoreCase("texas") == 0)
					//System.out.println("insertando: " + word + "\n");
				 tree.insertRecord(new InvertedIndexIndexRecord(word, simulateBlockNumber(word)));
				 //System.out.println(tree.toString());
			}catch (TreeDuplicatedRecordException e) {
				//words tiene palabras duplicadas, no interfiere con la prueba
			}
		}
	}

}
