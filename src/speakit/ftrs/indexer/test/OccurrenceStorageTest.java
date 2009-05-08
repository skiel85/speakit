package speakit.ftrs.indexer.test;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.ftrs.indexer.Occurrence;
import speakit.ftrs.indexer.OccurrenceStorage;
import speakit.ftrs.indexer.OccurrenceStorageImpl;

public class OccurrenceStorageTest {
	OccurrenceStorage storage;
	@Before
	public void setUp() throws Exception {
		deleteTempFiles();
		
		storage = new OccurrenceStorageImpl(4);
	}

	private void deleteTempFiles() {
		/* Parche hasta q pueda borrar los archivos desde la clase OccurrenceStorage */
		File f1 = new File("sortedPart0.part");
		f1.delete();
		f1 = new File("sortedPart1.part");
		f1.delete();
		f1 = new File("sortedPart2.part");
		f1.delete();
		f1 = new File("sortedPart3.part");
		f1.delete();
		f1 = new File("mergedList.merge");
		f1.delete();
	}

	@After
	public void tearDown() throws Exception {
		deleteTempFiles();
	}
	
	@Test
	public void testAddOccurrence() {
		//addOccurrences();
	}

	private int addOccurrences() {
		//Inserto ocurrencias desordenadas, los valors son termino; documento
		//El resultado deberia ser: (1,1) (1,2) (1,2) (1,3) (1,3) (1,4) (2,1) (2,1) (2,4) (3,1) (3,5) (3,5) (4,20) (4,4 (5,3)
			storage.addOccurrence(new Occurrence(1, 1));
			storage.addOccurrence(new Occurrence(2, 1));
			storage.addOccurrence(new Occurrence(3, 1));
			storage.addOccurrence(new Occurrence(2, 1));
			storage.addOccurrence(new Occurrence(1, 2));
			storage.addOccurrence(new Occurrence(4, 2));
			storage.addOccurrence(new Occurrence(1, 2));
			storage.addOccurrence(new Occurrence(1, 3));
			storage.addOccurrence(new Occurrence(1, 3));
			storage.addOccurrence(new Occurrence(5, 3));
			storage.addOccurrence(new Occurrence(2, 4));
			storage.addOccurrence(new Occurrence(4, 4));
			storage.addOccurrence(new Occurrence(1, 4));
			storage.addOccurrence(new Occurrence(3, 5));
			storage.addOccurrence(new Occurrence(3, 5));
			//cantidad de regs insertados
			return 15;
	}

	@Ignore
	public void testGetApearanceListFor() {
		fail("Not yet implemented"); // TODO
	}
	
	@Test
	public void testGetSortedAppearanceList() {
		int adds = addOccurrences();	
		ArrayList<Occurrence> originalList = storage.getSortedAppearanceList();
		Assert.assertEquals("Longitud de la lista", adds, originalList.size());
		ArrayList<Occurrence> list = new ArrayList<Occurrence>(originalList);
		Collections.sort(list);
		Assert.assertEquals(list.size(), originalList.size());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).compareTo(originalList.get(i)) != 0)
				fail("En la posicion " + i + " del listado original aparece " + originalList.get(i).toString() + " mientras q en el reordenado aparece: " + list.get(i).toString());
		}
//		System.out.println(list.toString());
	}

}
