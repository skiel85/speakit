package speakit.io.bsharptree.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.bsharptree.BSharpTreeLeafNode;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class BSharpTreeLeafNodeTest {

	private BSharpTreeLeafNode sut;

	@Before
	public void setUp() throws Exception {
		this.sut = new BSharpTreeLeafNode(null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInsertAndRetrieve() throws RecordSerializationException, IOException {
		TestIndexRecord rec1 = new TestIndexRecord("hola", 1);
		TestIndexRecord rec2 = new TestIndexRecord("mundo", 3);
		this.sut.insertRecord(rec1);
		this.sut.insertRecord(rec2);
		TestIndexRecord retrievedRec = (TestIndexRecord) this.sut.getRecord(new StringField("mundo"));
		Assert.assertEquals("mundo", retrievedRec.getKey().getString());
		Assert.assertEquals(3, retrievedRec.getBlockNumber());
	}

}
