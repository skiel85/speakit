package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.bsharptree.TreeIndexNode;
import speakit.io.bsharptree.TreeIndexNodeElement;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class TreeIndexNodeWithIndexChildrenTest {
	private TreeIndexNode sut;
	private TestBSharpTree tree;
	private File file;

	@Before
	public void setUp() throws Exception {
		// 00: ... 02(010)03(100)04
		// . 02: . 05(004)06(007)07
		// ... 05: (001)(002)(003)
		// ... 06: (004)(005)(006)
		// ... 07: (007)(008)(009)
		// . 03: . 08(040)09(070)10
		// ... 08: (010)(020)(030)
		// ... 09: (040)(050)(060)
		// ... 10: (070)(080)(090)
		// . 04: . 11(400)12(700)13
		// ... 11: (100)(200)(300)
		// ... 12: (400)(500)(600)
		// ... 13: (700)(800)(900)

		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.tree = new TestBSharpTree(this.file);

		this.sut = (TreeIndexNode) this.tree.getRoot();
		this.sut.setNodeNumber(0);
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testSearch() throws RecordSerializationException, IOException {
		TestIndexRecord record = (TestIndexRecord) this.sut.getRecord(new StringField("100"));
		Assert.assertNotNull(record);
		Assert.assertEquals("100", ((StringField) record.getKey()).getString());
		Assert.assertEquals(100, record.getBlockNumber());
	}
	
	@Test
	public void testIndexChildren() {
		Assert.assertEquals(2, this.sut.getLeftChildNodeNumber());
		TreeIndexNodeElement element0 = (TreeIndexNodeElement) this.sut.getElement(0);
		Assert.assertEquals("010", ((StringField) element0.getKey()).getString());
		Assert.assertEquals(3, element0.getRightChildNodeNumber());
		TreeIndexNodeElement element1 = (TreeIndexNodeElement) this.sut.getElement(1);
		Assert.assertEquals("100", ((StringField) element1.getKey()).getString());
		Assert.assertEquals(4, element1.getRightChildNodeNumber());
	}

	@Test
	public void testInsertInCorrectLeaf1() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("733", 88));
		TestIndexRecord retrievedRec = (TestIndexRecord) this.tree.getNode(13, null).getRecord(new StringField("733"));
		Assert.assertEquals("733", retrievedRec.getKey().getString());
		Assert.assertEquals(88, retrievedRec.getBlockNumber());
	}

	@Test
	public void testInsertInCorrectLeaf2() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("055", 68));
		TestIndexRecord retrievedRec = (TestIndexRecord) this.tree.getNode(9, null).getRecord(new StringField("055"));
		Assert.assertEquals("055", retrievedRec.getKey().getString());
		Assert.assertEquals(68, retrievedRec.getBlockNumber());
	}

	@Test
	public void testInsertInCorrectLeafExtremeCase() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("000", 56));
		TestIndexRecord retrievedRec = (TestIndexRecord) this.tree.getNode(5, null).getRecord(new StringField("000"));
		Assert.assertEquals("000", retrievedRec.getKey().getString());
		Assert.assertEquals(56, retrievedRec.getBlockNumber());
	}

}
