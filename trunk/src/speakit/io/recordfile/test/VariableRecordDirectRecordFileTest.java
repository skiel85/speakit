package speakit.io.recordfile.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.FileManager;
import speakit.io.record.IntegerField;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.DirectRecordFile;
import speakit.test.TestFileManager;

public class VariableRecordDirectRecordFileTest {   
		private DirectRecordFile<TestVariableRecord, IntegerField> sut;
		private FileManager filemanager;

		@Before
		public void setUp() throws Exception {
			filemanager=new TestFileManager(this.getClass().getName()); 
			this.sut = new DirectRecordFile<TestVariableRecord, IntegerField>(filemanager.openFile("DirectRecordFile.dat"), new RecordFactory<TestVariableRecord>() {
				@Override
				public TestVariableRecord createRecord() {
					return new TestVariableRecord(0,"");
				}
			});
			this.sut.create(30);
			
			blockNumber = this.sut.createBlock();
			TestVariableRecord record1 = new TestVariableRecord(1,"w");
			TestVariableRecord record2 = new TestVariableRecord(2,"qwerty");
			TestVariableRecord record3 = new TestVariableRecord(3,"qqqqq");
			TestVariableRecord record4 = new TestVariableRecord(4,"zxcvb");
			this.sut.insertRecord(record1, blockNumber);
			this.sut.insertRecord(record2, blockNumber);
			this.sut.insertRecord(record3, blockNumber);
			this.sut.insertRecord(record4, blockNumber);
			
		}

		@After
		public void tearDown() throws Exception {
			this.filemanager.destroyFiles();
		}
		
		private static final String	TEXTO_DEL_REGISTRO_ACTUALIADO	= "texto del registro actualiado";
		private int	blockNumber;
 
		@Test
		public void testUpdateRecord() throws IOException, RecordSerializationException {
			TestVariableRecord updatingRecord = new TestVariableRecord(3,TEXTO_DEL_REGISTRO_ACTUALIADO);
			this.sut.updateRecord(updatingRecord);
			TestVariableRecord retrievedRecord = this.sut.getRecord(new IntegerField(3), blockNumber);
			Assert.assertNotNull(retrievedRecord);
			Assert.assertEquals(TEXTO_DEL_REGISTRO_ACTUALIADO, retrievedRecord.text.getString());
		}
		
		@Test
		public void testUpdateRecordKnowingBlockNumber() throws IOException, RecordSerializationException {
			TestVariableRecord updatingRecord = new TestVariableRecord(3,TEXTO_DEL_REGISTRO_ACTUALIADO);
			this.sut.updateRecord(updatingRecord,this.blockNumber);
			TestVariableRecord retrievedRecord = this.sut.getRecord(new IntegerField(3), blockNumber);
			Assert.assertNotNull(retrievedRecord);
			Assert.assertEquals(TEXTO_DEL_REGISTRO_ACTUALIADO, retrievedRecord.text.getString());
		}
		
		
		//TODO probar updateRecord con numero de bloque
		//TODO probar updateRecord sin numero de bloque

	}

