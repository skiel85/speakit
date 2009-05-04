package speakit.ftrs.index.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.ftrs.index.InvertedIndex;
import speakit.ftrs.index.InvertedIndexRecord;
import speakit.ftrs.index.InvertedList;
import speakit.ftrs.index.TermOcurrence;
import speakit.test.TestFileManager;

public class InvertedIndexTest {

	private InvertedIndex	index;
	private FileManager		filemanager;
	private Configuration	conf;

	@Before
	public void setUp() throws Exception {
		index = new InvertedIndex();
		filemanager = new TestFileManager(this.getClass().getName());
		conf = new Configuration();
		conf.setBlockSize(1024);
		conf.setTrieDepth(4);
		index.install(filemanager, conf);
		// bella|0,6|1 (2, 1)
		// cosas|0,3|1 (1, 1), (3, 1)
		// querer|0,6|1 (3, 1)
		// vida|0,125|2 (4, 2), (1,1), (2, 1)
		// Notar q las listas se arman en el siguiente orden, frecuencia, luego
		// nro de documento.
		index.updateRecord(new InvertedIndexRecord("bella", (new InvertedList()).add(new TermOcurrence(2, 1))));
		index.updateRecord(new InvertedIndexRecord("cosas", (new InvertedList()).add(new TermOcurrence(1, 1)).add(new TermOcurrence(3, 1))));
		index.updateRecord(new InvertedIndexRecord("querer", (new InvertedList()).add(new TermOcurrence(3, 1))));
		index.updateRecord(new InvertedIndexRecord("vida", (new InvertedList()).add(new TermOcurrence(4, 2)).add(new TermOcurrence(1, 1)).add(new TermOcurrence(2, 1))));
	}

	@After
	public void tearDown() throws Exception {
		this.filemanager.destroyFiles();
	}
	
	@Test
	public void testSimpleRetrieve() throws IOException {
		InvertedIndexRecord documentsFor = index.getDocumentsFor("bella");
		Assert.assertNotNull("Se esperaba no nulo",documentsFor);
		Assert.assertEquals("bella", documentsFor.getTerm());
		index.updateRecord(new InvertedIndexRecord("aaa", (new InvertedList()).add(new TermOcurrence(7, 2))));
		Assert.assertNotNull("Se esperaba no nulo",index.getDocumentsFor("bella"));
	}

	@Test
	public void testUpdatingRecordMergesLists() throws IOException {
		index.updateRecord(new InvertedIndexRecord("querer", (new InvertedList()).add(new TermOcurrence(7, 2))));
		InvertedIndexRecord indexRecord = index.getDocumentsFor("querer");
		InvertedList mergedList = indexRecord.getInvertedList();

		InvertedList expectedList = new InvertedList();
		expectedList.add(new TermOcurrence(7, 2)).add(new TermOcurrence(3, 1));

		Assert.assertEquals(true, expectedList.equals(mergedList));
	}

	@Test
	public void testCompare() {
		InvertedList list1 = new InvertedList();
		list1.add(new TermOcurrence(7, 2)).add(new TermOcurrence(4, 2)).add(new TermOcurrence(1, 1)).add(new TermOcurrence(2, 1));

		InvertedList list2 = new InvertedList();
		list2.add(new TermOcurrence(7, 2)).add(new TermOcurrence(2, 2)).add(new TermOcurrence(1, 1)).add(new TermOcurrence(2, 1));

		InvertedList list3 = new InvertedList();
		list3.add(new TermOcurrence(7, 2)).add(new TermOcurrence(4, 2)).add(new TermOcurrence(1, 1));

		InvertedList listEquals1 = new InvertedList();
		listEquals1.add(new TermOcurrence(7, 2)).add(new TermOcurrence(4, 2)).add(new TermOcurrence(1, 1)).add(new TermOcurrence(2, 1));

		Assert.assertEquals(true, list1.equals(listEquals1));
		Assert.assertEquals(false, list1.equals(list2));
		Assert.assertEquals(false, list1.equals(list3));
	}

}