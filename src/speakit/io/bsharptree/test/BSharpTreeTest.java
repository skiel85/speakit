package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.io.bsharptree.BSharpTree;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class BSharpTreeTest {
	private File file;
	private BSharpTree<TestIndexRecord, StringField> sut;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.sut = new BSharpTree<TestIndexRecord, StringField>(file){
			@Override
			public Record createRecord() { 
				return new TestIndexRecord("", 0);
			}
			
		};
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	@Ignore
	public void testInsertAndRetrieve() throws RecordSerializationException, IOException {
		TestIndexRecord rec1 = new TestIndexRecord("hola", 1);
		TestIndexRecord rec2 = new TestIndexRecord("mundo", 3);
		this.sut.insertRecord(rec1);
		this.sut.insertRecord(rec2);
		TestIndexRecord retrievedRec = this.sut.getRecord(new StringField("mundo"));
		Assert.assertEquals("mundo", retrievedRec.getKey().getString());
		Assert.assertEquals(3, retrievedRec.getBlockNumber());
	}
}
