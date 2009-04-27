package speakit.ftrs.index.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.ftrs.index.InvertedList;
import speakit.ftrs.index.InvertedListItem;

public class InvertedListTest {
	InvertedList	sut;

	@Before
	public void setUp() throws Exception {
		sut = new InvertedList();
		sut.add(new InvertedListItem(2, 4));
		sut.add(new InvertedListItem(9, 2));
		sut.add(new InvertedListItem(1, 1));
		sut.add(new InvertedListItem(5, 6));
		sut.add(new InvertedListItem(7, 3));
		sut.add(new InvertedListItem(10, 1));
	}

	@After
	public void tearDown() throws Exception {

	}
	
	@Test
	public void testCreatedProperly() {
		testAllDocumentsAreInList(this.sut);
	}
 
	public void testAllDocumentsAreInList(InvertedList list) {
		Assert.assertEquals(false, list.containsDocument(0));
		Assert.assertEquals(false, list.containsDocument(11));
		Assert.assertEquals(false, list.containsDocument(8));
		Assert.assertEquals(false, list.containsDocument(-1));
		Assert.assertEquals(true, list.containsDocument(1));
		Assert.assertEquals(true, list.containsDocument(10));
		Assert.assertEquals(true, list.containsDocument(2));
		Assert.assertEquals(true, list.containsDocument(5));
		Assert.assertEquals(true, list.containsDocument(9));
		Assert.assertEquals(true, list.containsDocument(7));
	}

	@Test
	public void testTruncateByFrecuency() {
		InvertedList truncatedByFrecuency = sut.truncateByFrecuency(3);
		Assert.assertEquals(false, truncatedByFrecuency.containsDocument(1));
		Assert.assertEquals(false, truncatedByFrecuency.containsDocument(10));
		Assert.assertEquals(true, truncatedByFrecuency.containsDocument(2));
		Assert.assertEquals(true, truncatedByFrecuency.containsDocument(5));
		Assert.assertEquals(true, truncatedByFrecuency.containsDocument(7));
		Assert.assertEquals(false, truncatedByFrecuency.containsDocument(9));
	}
	
	@Test
	public void testTruncatingWithZeroDoesNotActuallyTruncates() {
		InvertedList truncatedByFrecuency = sut.truncateByFrecuency(0);
		testAllDocumentsAreInList(truncatedByFrecuency);
	} 

}