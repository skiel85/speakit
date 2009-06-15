package speakit.io.recordfile.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import speakit.io.blockfile.Block;
import speakit.io.record.IntegerField;
import speakit.io.record.RecordFactory;
import speakit.io.recordfile.RecordsListBlockInterpreter;

public class RecordListBlockInterpreterSimpleTest  implements RecordFactory  {
	private static final TestVariableRecord	RECORD_1	= new TestVariableRecord(60, "altura máxima de 80 cm aproximadamente");
	private static final TestVariableRecord	RECORD_2	= new TestVariableRecord(111, "azureus");

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
		bl=interpreter.getBlock();
		interpreter = new RecordsListBlockInterpreter<TestVariableRecord, IntegerField>(bl, this);
		interpreter.insertRecord(RECORD_2);
	}
	
	/**
	 * Prueba que la inserción es ordenada luego de guardar el interprete en el bloque y crear un nuevo interprete
	 */
	@Test
	public void testDoNotSavesMoreRecorsThatItHas() throws IOException {
		Block savedBlock = interpreter.getBlock();
		RecordsListBlockInterpreter<TestVariableRecord, IntegerField> sut = new RecordsListBlockInterpreter<TestVariableRecord, IntegerField>(savedBlock, this);
		int size = sut.getRecords().size();
		Assert.assertEquals(2, size);
	}
}
