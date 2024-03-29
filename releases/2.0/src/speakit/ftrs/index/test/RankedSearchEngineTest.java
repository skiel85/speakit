package speakit.ftrs.index.test;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.Configuration;
import speakit.FileManager;
import speakit.TextDocument;
import speakit.ftrs.RankedSearchEngine;
import speakit.ftrs.index.InvertedIndex;
import speakit.ftrs.index.InvertedIndexRecord;
import speakit.ftrs.index.InvertedList;
import speakit.ftrs.index.TermOcurrence;
import speakit.test.TestFileManager;

public class RankedSearchEngineTest {

	RankedSearchEngine		sut;
	private InvertedIndex	index;
	private FileManager		filemanager;
	private Configuration	conf;

	@Before
	public void setUp() throws Exception {
		index = new InvertedIndex();
		filemanager = new TestFileManager(this.getClass().getName());
		conf = new Configuration();
		conf.setBlockSize(512);//TODO OJO con el 512, tira errores que no tira usando un n�mero mas alto. Verificar si el linked records file funciona bien.
		index.install(filemanager, conf);
		// TextDocument doc1 = new TextDocument("cosas vida");
		// TextDocument doc2 = new TextDocument("vida bella");
		// TextDocument doc3 = new TextDocument("cosas querer");
		// TextDocument doc4 = new TextDocument("vida despues vida");

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

		int resultQty = 10;
		sut = new RankedSearchEngine(index, resultQty, 1);
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

	// Ejemplo de la diapositiva
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
		// "querer" tiene mayor peso que "vida", pues "querer" es mas raro
		// Se espera que primero vengan los documentos de "querer" (3) y luego
		// de "vida" (4,1,2)
		Assert.assertEquals(3L, docIds.get(0).longValue());
		Assert.assertEquals(4L, docIds.get(1).longValue());
		Assert.assertEquals(1L, docIds.get(2).longValue());
		Assert.assertEquals(2L, docIds.get(3).longValue());
	}

	@Test
	public void testSearchWithOrder3() throws IOException {
		List<Long> docIds = sut.search(new TextDocument("cosas querer"));
		Assert.assertEquals(2, docIds.size());
		// Se espera que primero vengan los documentos de "querer" (3) y luego
		// de "cosas" (1,[el 3 ya apareci�])
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

	@Test
	public void testSearchAfterIndexingNewDocuments() throws IOException {
		index.updateRecord(new InvertedIndexRecord("vida", (new InvertedList()).add(new TermOcurrence(10, 10))));

		List<Long> docIds = sut.search(new TextDocument("vida"));
		Assert.assertEquals(4, docIds.size());
		Assert.assertEquals(10L, docIds.get(0).longValue());
		Assert.assertEquals(4L, docIds.get(1).longValue());
		Assert.assertEquals(1L, docIds.get(2).longValue());
		Assert.assertEquals(2L, docIds.get(3).longValue());
	}

}