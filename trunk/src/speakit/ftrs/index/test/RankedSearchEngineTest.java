package speakit.ftrs.index.test;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.ftrs.RankedSearchEngine;
import speakit.ftrs.index.Index;
import speakit.ftrs.index.IndexRecord;
import speakit.ftrs.index.InvertedList;
import speakit.ftrs.index.InvertedListItem;

public class RankedSearchEngineTest {

	RankedSearchEngine	sut;

	@Before
	public void setUp() throws Exception {
		Index index = new Index();
		int resultQty = 10;
		sut = new RankedSearchEngine(index, resultQty, 1);

//		TextDocument doc1 = new TextDocument("cosas vida");
//		TextDocument doc2 = new TextDocument("vida bella");
//		TextDocument doc3 = new TextDocument("cosas querer");
//		TextDocument doc4 = new TextDocument("vida despues vida");

		// bella|0,6|1 (2, 1)
		// cosas|0,3|1 (1, 1), (3, 1)
		// querer|0,6|1 (3, 1)
		// vida|0,125|2 (4, 2), (1,1), (2, 1)

		index.updateRecord(new IndexRecord("bella", (new InvertedList()).add(new InvertedListItem(2, 1))));
		index.updateRecord(new IndexRecord("cosas", (new InvertedList()).add(new InvertedListItem(1, 1)).add(new InvertedListItem(3, 1))));
		index.updateRecord(new IndexRecord("querer", (new InvertedList()).add(new InvertedListItem(3, 1))));
		index.updateRecord(new IndexRecord("vida", (new InvertedList()).add(new InvertedListItem(1, 1)).add(new InvertedListItem(2, 1)).add(new InvertedListItem(4, 2))));
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testSearchWithoutOrder() throws IOException {
		List<Long> docIds = sut.search(new TextDocument("bella querer"));
		Assert.assertEquals(2, docIds.size());
		Assert.assertTrue(docIds.contains(new Long(2)));
		Assert.assertTrue(docIds.contains(new Long(3))); 
	}
	
	//Ejemplo de la diapositiva
	@Test
	public void testSearchWithOrder1() throws IOException {
		sut.setResultItemsCount(3);
		List<Long> docIds = sut.search(new TextDocument("cosas vida"));
		Assert.assertEquals(3, docIds.size()); 
		Assert.assertEquals(1L, docIds.get(0).longValue());
		Assert.assertEquals(3L, docIds.get(1).longValue());
		Assert.assertEquals(4L, docIds.get(2).longValue()); 
	}
	
	@Test
	public void testSearchWithOrder2() throws IOException {
		List<Long> docIds = sut.search(new TextDocument("vida querer"));
		Assert.assertEquals(4, docIds.size());
		//"querer" tiene mayor peso que "vida", pues "querer" es mas raro
		//Se espera que primero vengan los documentos de "querer" (3) y luego de "vida" (4,1,2) 
		Assert.assertEquals(3L, docIds.get(0).longValue());
		Assert.assertEquals(4L, docIds.get(1).longValue());
		Assert.assertEquals(1L, docIds.get(2).longValue());
		Assert.assertEquals(2L, docIds.get(3).longValue());
	}
	
	@Test
	public void testSearchWithOrder3() throws IOException {
		List<Long> docIds = sut.search(new TextDocument("cosas querer"));
		Assert.assertEquals(2, docIds.size());
		//Se espera que primero vengan los documentos de "querer" (3) y luego de "cosas" (1,[el 3 ya apareció]) 
		Assert.assertEquals(3L, docIds.get(0).longValue());
		Assert.assertEquals(1L, docIds.get(1).longValue()); 
	}
	
	@Test
	public void testSearchWithoutResultDuplication() throws IOException {
		List<Long> docIds = sut.search(new TextDocument("bella querer bella querer"));
		Assert.assertEquals(2, docIds.size());
		Assert.assertTrue(docIds.contains(new Long(2)));
		Assert.assertTrue(docIds.contains(new Long(3))); 
	}
	
	@Test
	public void testSearchResultItemCount() throws IOException { 
		sut.setResultItemsCount(5);
		Assert.assertEquals(4, sut.search(new TextDocument("bella cosas vida querer")).size());
		sut.setResultItemsCount(2);
		Assert.assertEquals(2, sut.search(new TextDocument("bella cosas vida querer")).size());
	}
	
	@Test
	public void testSearchWithFrecuencyRestriction() throws IOException {
		sut.setMinTermFrecuency(2);
		List<Long> docIds = sut.search(new TextDocument("bella vida"));
		Assert.assertEquals(1, docIds.size());
		Assert.assertEquals(4L, docIds.get(0).longValue()); 
	}

}