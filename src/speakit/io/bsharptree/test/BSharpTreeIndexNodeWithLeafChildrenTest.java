package speakit.io.bsharptree.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.bsharptree.BSharpTreeIndexNode;
import speakit.io.bsharptree.BSharpTreeIndexNodeElement;
import speakit.io.bsharptree.BSharpTreeLeafNode;
import speakit.io.record.StringField;

public class BSharpTreeIndexNodeWithLeafChildrenTest {
	private BSharpTreeIndexNode sut;
	private TestIndexRecord[] records;
	private BSharpTreeLeafNode[] nodes;

	@Before
	public void setUp() throws Exception {
		this.sut = new BSharpTreeIndexNode(null, 2);
		this.sut.setNodeNumber(0);

		this.nodes = new BSharpTreeLeafNode[3];
		nodes[0] = new BSharpTreeLeafNode(null, 1);
		nodes[0].setNodeNumber(2);
		nodes[1] = new BSharpTreeLeafNode(null, 1);
		nodes[1].setNodeNumber(3);
		nodes[2] = new BSharpTreeLeafNode(null, 1);
		nodes[2].setNodeNumber(4);

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
	}

	@Test
	public void testIndexChildren() {
		{
			Assert.assertEquals(0, this.sut.getElements().size());
		}
		{
			this.sut.indexChild(nodes[0]);
			Assert.assertEquals(2, this.sut.getLeftChildNodeNumber());
			Assert.assertEquals(0, this.sut.getElements().size());
		}
		{
			this.sut.indexChild(nodes[1]);
			Assert.assertEquals(2, this.sut.getLeftChildNodeNumber());
			Assert.assertEquals(1, this.sut.getElements().size());
			BSharpTreeIndexNodeElement element0 = (BSharpTreeIndexNodeElement) this.sut.getElements().get(0);
			Assert.assertEquals("del", ((StringField) element0.getKey()).getString());
			Assert.assertEquals(3, element0.getRightChildNodeNumber());
		}
		{
			this.sut.indexChild(nodes[2]);
			Assert.assertEquals(2, this.sut.getLeftChildNodeNumber());
			Assert.assertEquals(2, this.sut.getElements().size());
			BSharpTreeIndexNodeElement element0 = (BSharpTreeIndexNodeElement) this.sut.getElements().get(0);
			Assert.assertEquals("del", ((StringField) element0.getKey()).getString());
			Assert.assertEquals(3, element0.getRightChildNodeNumber());
			BSharpTreeIndexNodeElement element1 = (BSharpTreeIndexNodeElement) this.sut.getElements().get(1);
			Assert.assertEquals("naci�n", ((StringField) element1.getKey()).getString());
			Assert.assertEquals(4, element1.getRightChildNodeNumber());
		}
	}

}
