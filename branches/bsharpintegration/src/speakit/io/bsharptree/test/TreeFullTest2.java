package speakit.io.bsharptree.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.TextDocument;
import speakit.ftrs.index.InvertedIndexIndexRecord;
import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.bsharptree.RecordEncoder;
import speakit.io.bsharptree.Tree;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.test.SpeakitSearchRealDocumentsTest;
import speakit.test.TestFileManager;

@Ignore
public class TreeFullTest2 {

	private Tree<InvertedIndexIndexRecord, StringField>	sut;

	private TestFileManager								filemanager;

	private RecordEncoder								encoder;

	private List<String>	words; 

	@Before
	public void setUp() throws Exception {
		this.filemanager = new TestFileManager("");
		encoder = new InvertedIndexIndexRecordEncoder();
		this.sut = new Tree<InvertedIndexIndexRecord, StringField>(filemanager.openFile("FullBSTree.dat"), InvertedIndexIndexRecord.createRecordFactory(), encoder);
		this.sut.create(180);
		 
		List<TextDocument> documents=new ArrayList<TextDocument>();
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_ABSTRACCION);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_ADOLESCENCIA);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_AUTOESTIMA);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_CONECTIVISMO);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_EFECTO_TETRIS);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_EQUIPAMIENTO);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_INSTINTO);
		documents.add(SpeakitSearchRealDocumentsTest.ARTICLE_PORTUGA);
		
		words = new ArrayList<String>();
		for (TextDocument textDocument : documents) {
			for (String word : textDocument) {
				words.add(word);
			}
		}
		TreeFullTest.insertAllWords(this.sut,words);
	}
	 

	/**
	 * Prueba obtener todos los registros
	 * 
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	@Test
	public void testRetrieveAllRecords() throws RecordSerializationException, IOException {
		TreeFullTest.testRetrieveAllRecords(this.sut, words);
	} 

}