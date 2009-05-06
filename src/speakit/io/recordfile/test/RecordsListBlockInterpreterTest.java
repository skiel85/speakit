package speakit.io.recordfile.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import speakit.io.blockfile.Block;
import speakit.io.record.IntegerField;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.RecordsListBlockInterpreter;

public class RecordsListBlockInterpreterTest implements RecordFactory {
	private static final TestVariableRecord	RECORD_1	= new TestVariableRecord(60, "altura máxima de 80 cm aproximadamente");
	private static final TestVariableRecord	RECORD_2	= new TestVariableRecord(90, "alcanzando una longitud no mayor de 135 cm ");
	private static final TestVariableRecord	RECORD_3	= new TestVariableRecord(30, "subespecies más pequeñas");
	private static final TestVariableRecord	RECORD_4	= new TestVariableRecord(50, "subespecie genéticamente más distinta de lobo presente en Norteamérica");
	private static final TestVariableRecord	RECORD_5	= new TestVariableRecord(20, "Canis lupus baileyi");
	private static final TestVariableRecord	RECORD_6	= new TestVariableRecord(70, "lobo mexicano");
	private RecordsListBlockInterpreter<TestVariableRecord, IntegerField>	interpreter; 

	@Override
	public TestVariableRecord createRecord() {
		return new TestVariableRecord(0, "");
	}

	@Before
	public void setUp() throws Exception {
		Block bl = new Block(0);
		interpreter = new RecordsListBlockInterpreter<TestVariableRecord, IntegerField>(bl, this);
		interpreter.insertRecord(RECORD_1);
		interpreter.insertRecord(RECORD_2);
		interpreter.insertRecord(RECORD_3);
		interpreter.insertRecord(RECORD_4);
		interpreter.insertRecord(RECORD_5);
		interpreter.insertRecord(RECORD_6);
	}
	
	/**
	 * Prueba que la inserción es ordenada luego de guardar el interprete en el bloque y crear un nuevo interprete
	 */
	@Test
	public void testInsertsSortedFromDeserialized() throws IOException {
		Block savedBlock = interpreter.getBlock();
		RecordsListBlockInterpreter<TestVariableRecord, IntegerField> sut = new RecordsListBlockInterpreter<TestVariableRecord, IntegerField>(savedBlock, this);
		testInsertsSorted(sut);
	}
	
	/**
	 * Prueba que la inserción es ordenada
	 */
	@Test
	public void testInsertsSortedDirectly() throws IOException {
		testInsertsSorted(this.interpreter);
	}
	
	 
	@Test
	public void testContainsAllRecordsDirectly() throws IOException {
		testContainsAllRecords(this.interpreter);
	}
	
	 
	@Test
	public void testContainsAllRecordsFromDeserialized() throws IOException {
		Block savedBlock = interpreter.getBlock();
		RecordsListBlockInterpreter<TestVariableRecord, IntegerField> sut = new RecordsListBlockInterpreter<TestVariableRecord, IntegerField>(savedBlock, this);
		testContainsAllRecords(sut);
	}
	
	@Test
	public void testDeleteRecord() throws IOException {
		boolean deleteRecord = interpreter.deleteRecord(RECORD_4);
		Assert.assertEquals(true, deleteRecord);
		assertContainsRecord(interpreter,RECORD_1);
		assertContainsRecord(interpreter,RECORD_2);
		assertContainsRecord(interpreter,RECORD_3);
		assertContainsRecord(interpreter,RECORD_5);
		assertContainsRecord(interpreter,RECORD_6);
		int size = interpreter.getRecords().size();
		Assert.assertEquals(5, size); 
		Assert.assertEquals(false, interpreter.deleteRecord(RECORD_4));
		
	}
	

	/**
	 * Prueba que la inserción es ordenada
	 */ 
	public static void testInsertsSorted(RecordsListBlockInterpreter<TestVariableRecord, IntegerField> sut) throws IOException {
		TestVariableRecord newRecord = new TestVariableRecord(45, "Los pesos varían desde los 27 kg hasta los 45 kg.");
		sut.insertRecord(newRecord);
		TestVariableRecord retrievedRecord = sut.getRecord(new IntegerField(45));
		Assert.assertNotNull("Se esperaba no nulo, pero vino: " + retrievedRecord, retrievedRecord);
		Assert.assertTrue("Deberian ser iguales pero dió " + newRecord.compareTo(retrievedRecord), newRecord.compareTo(retrievedRecord) == 0);
	}
	
	public static void assertContainsRecord(RecordsListBlockInterpreter<TestVariableRecord, IntegerField> sut,TestVariableRecord record) throws RecordSerializationException, IOException{
		Assert.assertTrue("No obtiene el record correcto", record.compareTo(sut.getRecord(record.getKey())) == 0);
	}
	
	public static void testContainsAllRecords(RecordsListBlockInterpreter<TestVariableRecord, IntegerField> sut) throws RecordSerializationException, IOException{
		assertContainsRecord(sut,RECORD_1);
		assertContainsRecord(sut,RECORD_2);
		assertContainsRecord(sut,RECORD_3);
		assertContainsRecord(sut,RECORD_4);
		assertContainsRecord(sut,RECORD_5);
		assertContainsRecord(sut,RECORD_6);
		int size = sut.getRecords().size();
		Assert.assertEquals(6, size);
	}

}
