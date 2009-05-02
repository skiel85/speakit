package speakit.bsharptree.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.bsharptree.BSharpTree;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class BSharpTreeTest {
	private BSharpTree<TestIndexRecord, StringField> sut = new BSharpTree<TestIndexRecord, StringField>();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test @Ignore
	public void testInsertAndRetrieve() throws RecordSerializationException, IOException {
		TestIndexRecord rec1 = new TestIndexRecord("hola", 1);
		TestIndexRecord rec2 = new TestIndexRecord("mundo", 3);
		this.sut.insertRecord(rec1);
		this.sut.insertRecord(rec2);
		TestIndexRecord retrievedRec= this.sut.getRecord(new StringField("mundo"));
		Assert.assertEquals("mundo", retrievedRec.getKey().getString());
		Assert.assertEquals(3, retrievedRec.getBlockNumber());
	}
}
