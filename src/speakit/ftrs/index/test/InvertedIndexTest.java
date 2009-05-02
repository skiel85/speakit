package speakit.ftrs.index.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import speakit.ftrs.index.InvertedIndex;
import speakit.ftrs.index.InvertedIndexRecord;
import speakit.ftrs.index.InvertedList;
import speakit.ftrs.index.InvertedListItem;

public class InvertedIndexTest {

	private InvertedIndex	index;

	@Before
	public void setUp() throws Exception {
		index = new InvertedIndex();

		// bella|0,6|1 (2, 1)
		// cosas|0,3|1 (1, 1), (3, 1)
		// querer|0,6|1 (3, 1)
		// vida|0,125|2 (4, 2), (1,1), (2, 1)
		// Notar q las listas se arman en el siguiente orden, frecuencia, luego
		// nro de documento.
		index.updateRecord(new InvertedIndexRecord("bella", (new InvertedList()).add(new InvertedListItem(2, 1))));
		index.updateRecord(new InvertedIndexRecord("cosas", (new InvertedList()).add(new InvertedListItem(1, 1)).add(new InvertedListItem(3, 1))));
		index.updateRecord(new InvertedIndexRecord("querer", (new InvertedList()).add(new InvertedListItem(3, 1))));
		index.updateRecord(new InvertedIndexRecord("vida", (new InvertedList()).add(new InvertedListItem(4, 2)).add(new InvertedListItem(1, 1)).add(new InvertedListItem(2, 1))));
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testUpdatingRecordMergesLists() throws IOException {
		index.updateRecord(new InvertedIndexRecord("querer", (new InvertedList()).add(new InvertedListItem(7, 2))));
		InvertedIndexRecord indexRecord = index.getDocumentsFor("querer");
		InvertedList actualList = indexRecord.getInvertedList();

		InvertedList expectedList = new InvertedList();
		expectedList.add(new InvertedListItem(7, 2)).add(new InvertedListItem(3, 1));
		
		Assert.assertEquals(true,expectedList.equals(actualList));
	}
	
	@Test
	public void testCompare(){
		InvertedList list1 = new InvertedList();
		list1.add(new InvertedListItem(7, 2)).add(new InvertedListItem(4, 2)).add(new InvertedListItem(1, 1)).add(new InvertedListItem(2, 1));
		
		InvertedList list2 = new InvertedList();
		list2.add(new InvertedListItem(7, 2)).add(new InvertedListItem(2, 2)).add(new InvertedListItem(1, 1)).add(new InvertedListItem(2, 1));
		
		InvertedList list3 = new InvertedList();
		list3.add(new InvertedListItem(7, 2)).add(new InvertedListItem(4, 2)).add(new InvertedListItem(1, 1));
		
		InvertedList listEquals1 = new InvertedList();
		listEquals1.add(new InvertedListItem(7, 2)).add(new InvertedListItem(4, 2)).add(new InvertedListItem(1, 1)).add(new InvertedListItem(2, 1));
		
		Assert.assertEquals(true,list1.equals(listEquals1));
		Assert.assertEquals(false,list1.equals(list2));
		Assert.assertEquals(false,list1.equals(list3)); 
	}

}