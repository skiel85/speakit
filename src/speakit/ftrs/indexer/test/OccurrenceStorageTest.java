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
		
		storage = new OccurrenceStorageImpl(150);
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
		//deleteTempFiles();
	}
	
	@Test
	public void testAddOccurrence() {
		//addOccurrences();
	}

	private void addRandomOccurrence() {
		//Inserto ocurrencias desordenadas generando un pseudo random, los valors son termino; documento
			storage.addOccurrence(new Occurrence(Math.round(Math.round(Math.random())) + 1, Math.round(Math.round(Math.random())) + 1));
	}
	private int addOccurrences() {
		//Inserto ocurrencias desordenadas, los valors son termino; documento
		//El resultado deberia ser: (1,1) (1,2) (1,2) (1,3) (1,3) (1,4) (2,1) (2,1) (2,4) (3,1) (3,5) (3,5) (4,20) (4,4) (5,3)
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

	@Test
	public void testGetApearanceListFor() {
		addOccurrences();
		ArrayList<Occurrence> list = storage.getApearanceListFor(2);
		Assert.assertEquals(3, list.size());
		list.clear();
		list = storage.getApearanceListFor(1);
		Assert.assertEquals(6, list.size());
	}
	
	@Test
	public void testGetApearanceListForBiggerSet() {
		for (int i = 0; i < 1387; i++) {
			addRandomOccurrence();
		}
		int termId = 2;
		ArrayList<Occurrence> list = storage.getApearanceListFor(termId);
		for (Occurrence occurrence : list) {
			if (occurrence.getTermId() != termId)
				fail("En el listado aparece " + occurrence.toString() + " en vez de un registro con id = " + termId);
		}
		list.clear();
		termId = 3;
		list = storage.getApearanceListFor(termId);
		for (Occurrence occurrence : list) {
			if (occurrence.getTermId() != termId)
				fail("En el listado aparece " + occurrence.toString() + " en vez de un registro con id = " + termId);
		}
		list.clear();
		termId = 175;
		list = storage.getApearanceListFor(termId);
		for (Occurrence occurrence : list) {
			if (occurrence.getTermId() != termId)
				fail("En el listado aparece " + occurrence.toString() + " en vez de un registro con id = " + termId);
		}
	}
	
	
	@Ignore
	public void testStress() {
		//int size = 1;
		int size = 10000;
		for (int i = 0; i < size; i++) {
			addRandomOccurrence();
		}
		testOrderedList(size);
	}
	@Test
	public void testBugMenorCantidadProcesada() {
		//int size = 1;
		int size = 151;
		for (int i = 0; i < size; i++) {
			addRandomOccurrence();
		}
		testOrderedList(size);
	}
	
	@Test
	public void testGetSortedAppearanceList() {
		int adds = addOccurrences();			
		testOrderedList(adds);
	}

	private void testOrderedList(int adds) {
		ArrayList<Occurrence> originalList = storage.getSortedAppearanceList();
		Assert.assertEquals("Longitud de la lista", adds, originalList.size());
		ArrayList<Occurrence> list = new ArrayList<Occurrence>(originalList);
		Collections.sort(list);
		Assert.assertEquals(list.size(), originalList.size());
		//System.out.println(list.toString());
		//System.out.println("\n");
		//System.out.println(originalList.toString());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).compareTo(originalList.get(i)) != 0)
				fail("En la posicion " + i + " del listado original aparece " + originalList.get(i).toString() + " mientras q en el reordenado aparece: " + list.get(i).toString());
		}
		//System.out.println(list.toString());
	}

}
