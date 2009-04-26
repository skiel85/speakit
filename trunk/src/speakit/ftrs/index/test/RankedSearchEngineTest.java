package speakit.ftrs.index.test;

import java.io.IOException;
import java.util.ArrayList;

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
		int resultQty = 3;
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
		index.updateRecord(new IndexRecord("vida", (new InvertedList()).add(new InvertedListItem(4, 1)).add(new InvertedListItem(1, 1)).add(new InvertedListItem(2, 1))));
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testSearch() throws IOException {
		ArrayList<Long> docIds = sut.search(new TextDocument("cosas vida"));
		Assert.assertEquals(3, docIds.size());
		Assert.assertEquals(1L, docIds.get(0).longValue());
		Assert.assertEquals(3L, docIds.get(1).longValue());
		Assert.assertEquals(4L, docIds.get(2).longValue());
	}

}