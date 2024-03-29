package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.bsharptree.TreeIndexNode;
import speakit.io.bsharptree.TreeIndexNodeElement;
import speakit.io.bsharptree.TreeLeafNode;
import speakit.io.bsharptree.TreeNode;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class TreeIndexNodeWithLeafChildrenTest {
	private TreeIndexNode sut;
	private TestIndexRecord[] records;
	private TreeNode[] nodes;
	private TreeMock tree;
	private File file;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.tree = new TreeMock(this.file);

		this.sut = new TreeIndexNode(this.tree,0, 2);
//		this.sut.setNodeNumber(0);

		this.nodes = new TreeNode[3];
		nodes[0] = new TreeLeafNode(this.tree,2);
//		nodes[0].setNodeNumber(2);
		nodes[1] = new TreeLeafNode(this.tree,3);
//		nodes[1].setNodeNumber(3);
		nodes[2] = new TreeLeafNode(this.tree,4);
//		nodes[2].setNodeNumber(4);

		this.tree.registerNodesInMock(nodes);

		this.records = new TestIndexRecord[9];
		records[0] = new TestIndexRecord("Nos", 12);
		records[1] = new TestIndexRecord("los", 23);
		records[2] = new TestIndexRecord("representantes", 34);
		records[3] = new TestIndexRecord("del", 45);
		records[4] = new TestIndexRecord("pueblo", 56);
		records[5] = new TestIndexRecord("de", 67);
		records[6] = new TestIndexRecord("la", 78);
		records[7] = new TestIndexRecord("naci�n", 89);
		records[8] = new TestIndexRecord("Argentina", 90);

		nodes[0].insertRecord(records[8]); // Argentina
		nodes[0].insertRecord(records[0]); // Nos
		nodes[0].insertRecord(records[5]); // de
		nodes[1].insertRecord(records[3]); // del
		nodes[1].insertRecord(records[6]); // la
		nodes[1].insertRecord(records[1]); // los
		nodes[2].insertRecord(records[7]); // naci�n
		nodes[2].insertRecord(records[4]); // pueblo
		nodes[2].insertRecord(records[2]); // representantes
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testIndexChildren() throws IOException {
		{
			Assert.assertEquals(0, this.sut.getElementCount());
		}
		{
			this.sut.indexChild(nodes[0]);
			Assert.assertEquals(2, this.sut.getLeftChildNodeNumber());
			Assert.assertEquals(0, this.sut.getElementCount());
		}
		{
			this.sut.indexChild(nodes[1]);
			Assert.assertEquals(2, this.sut.getLeftChildNodeNumber());
			Assert.assertEquals(1, this.sut.getElementCount());
			TreeIndexNodeElement element0 = (TreeIndexNodeElement) this.sut.getElement(0);
			Assert.assertEquals("del", ((StringField) element0.getKey()).getString());
			Assert.assertEquals(3, element0.getRightChildNodeNumber());
		}
		{
			this.sut.indexChild(nodes[2]);
			Assert.assertEquals(2, this.sut.getLeftChildNodeNumber());
			Assert.assertEquals(2, this.sut.getElementCount());
			TreeIndexNodeElement element0 = (TreeIndexNodeElement) this.sut.getElement(0);
			Assert.assertEquals("del", ((StringField) element0.getKey()).getString());
			Assert.assertEquals(3, element0.getRightChildNodeNumber());
			TreeIndexNodeElement element1 = (TreeIndexNodeElement) this.sut.getElement(1);
			Assert.assertEquals("naci�n", ((StringField) element1.getKey()).getString());
			Assert.assertEquals(4, element1.getRightChildNodeNumber());
		}
	}

	@Test
	public void testInsertAndRetrieve() throws RecordSerializationException, IOException {
		this.sut.indexChild(nodes[0]);
		this.sut.indexChild(nodes[1]);
		this.sut.indexChild(nodes[2]);
		this.sut.insertRecord(new TestIndexRecord("elefante", 1));
		TestIndexRecord retrievedRec = (TestIndexRecord) this.sut.getRecord(new StringField("elefante"));
		Assert.assertEquals("elefante", retrievedRec.getKey().getString());
		Assert.assertEquals(1, retrievedRec.getBlockNumber());
	}

	@Test
	public void testInsertInCorrectLeaf() throws RecordSerializationException, IOException {
		this.sut.indexChild(nodes[0]);
		this.sut.indexChild(nodes[1]);
		this.sut.indexChild(nodes[2]);
		this.sut.insertRecord(new TestIndexRecord("elefante", 99));
		TestIndexRecord retrievedRec = (TestIndexRecord) nodes[1].getRecord(new StringField("elefante"));
		Assert.assertEquals("elefante", retrievedRec.getKey().getString());
		Assert.assertEquals(99, retrievedRec.getBlockNumber());
	}

	@Test
	public void testInsertInCorrectLeafExtremeCase() throws RecordSerializationException, IOException {
		this.sut.indexChild(nodes[0]);
		this.sut.indexChild(nodes[1]);
		this.sut.indexChild(nodes[2]);
		this.sut.insertRecord(new TestIndexRecord("AAA", 88));
		TestIndexRecord retrievedRec = (TestIndexRecord) nodes[0].getRecord(new StringField("AAA"));
		Assert.assertEquals("AAA", retrievedRec.getKey().getString());
		Assert.assertEquals(88, retrievedRec.getBlockNumber());
	}

}
