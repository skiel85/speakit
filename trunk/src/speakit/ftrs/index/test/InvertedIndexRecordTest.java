package speakit.ftrs.index.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.ftrs.index.InvertedIndexRecord;
import speakit.ftrs.index.InvertedList;
import speakit.ftrs.index.InvertedListItem;

public class InvertedIndexRecordTest {
	private InvertedIndexRecord	record;

	@Before
	public void setUp() throws Exception { 
		record = new InvertedIndexRecord("vida", (new InvertedList()).add(new InvertedListItem(4, 2)).add(new InvertedListItem(1, 1)).add(new InvertedListItem(2, 1)));
	}
	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testInitialState() throws IOException {
		Assert.assertEquals(3, record.getDocumentsQty());
	}
	
	@Test
	public void testSettingInvertedListItemsUpdatesDocumentQty() throws IOException {
		record.setInvertedList(new InvertedList().add(new InvertedListItem(5, 8)).add(new InvertedListItem(4, 2)).add(new InvertedListItem(1, 1)).add(new InvertedListItem(2, 1)));
		Assert.assertEquals(4, record.getDocumentsQty());
	}
}
