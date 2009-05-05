package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.bsharptree.BSharpTreeIndexNode;
import speakit.io.bsharptree.BSharpTreeIndexNodeElement;
import speakit.io.bsharptree.BSharpTreeLeafNode;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class BSharpTreeIndexNodeWithIndexChildrenTest {
	private BSharpTreeIndexNode sut;
	private TestIndexRecord[] records;
	private BSharpTreeLeafNode[] leafNodes;
	private BSharpTreeIndexNode[] indexNodes;
	private BSharpTreeMock tree;
	private File file;

	@Before
	public void setUp() throws Exception {
		// 00: ... 02(040)03(400)04
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
		this.tree = new BSharpTreeMock(this.file);

		this.sut = new BSharpTreeIndexNode(this.tree, 2);
		this.sut.setNodeNumber(0);

		this.leafNodes = new BSharpTreeLeafNode[9];
		leafNodes[0] = new BSharpTreeLeafNode(this.tree, 1);
		leafNodes[0].setNodeNumber(5);
		leafNodes[1] = new BSharpTreeLeafNode(this.tree, 1);
		leafNodes[1].setNodeNumber(6);
		leafNodes[2] = new BSharpTreeLeafNode(this.tree, 1);
		leafNodes[2].setNodeNumber(7);
		leafNodes[3] = new BSharpTreeLeafNode(this.tree, 1);
		leafNodes[3].setNodeNumber(8);
		leafNodes[4] = new BSharpTreeLeafNode(this.tree, 1);
		leafNodes[4].setNodeNumber(9);
		leafNodes[5] = new BSharpTreeLeafNode(this.tree, 1);
		leafNodes[5].setNodeNumber(10);
		leafNodes[6] = new BSharpTreeLeafNode(this.tree, 1);
		leafNodes[6].setNodeNumber(11);
		leafNodes[7] = new BSharpTreeLeafNode(this.tree, 1);
		leafNodes[7].setNodeNumber(12);
		leafNodes[8] = new BSharpTreeLeafNode(this.tree, 1);
		leafNodes[8].setNodeNumber(13);

		this.tree.registerNodesInMock(leafNodes);

		this.records = new TestIndexRecord[27];
		records[0] = new TestIndexRecord("001", 1);
		records[1] = new TestIndexRecord("002", 2);
		records[2] = new TestIndexRecord("003", 3);
		records[3] = new TestIndexRecord("004", 4);
		records[4] = new TestIndexRecord("005", 5);
		records[5] = new TestIndexRecord("006", 6);
		records[6] = new TestIndexRecord("007", 7);
		records[7] = new TestIndexRecord("008", 8);
		records[8] = new TestIndexRecord("009", 9);
		records[9] = new TestIndexRecord("010", 10);
		records[10] = new TestIndexRecord("020", 20);
		records[11] = new TestIndexRecord("030", 30);
		records[12] = new TestIndexRecord("040", 40);
		records[13] = new TestIndexRecord("050", 50);
		records[14] = new TestIndexRecord("060", 60);
		records[15] = new TestIndexRecord("070", 70);
		records[16] = new TestIndexRecord("080", 80);
		records[17] = new TestIndexRecord("090", 90);
		records[18] = new TestIndexRecord("100", 100);
		records[19] = new TestIndexRecord("200", 200);
		records[20] = new TestIndexRecord("300", 300);
		records[21] = new TestIndexRecord("400", 400);
		records[22] = new TestIndexRecord("500", 500);
		records[23] = new TestIndexRecord("600", 600);
		records[24] = new TestIndexRecord("700", 700);
		records[25] = new TestIndexRecord("800", 800);
		records[26] = new TestIndexRecord("900", 900);

		leafNodes[0].insertRecord(records[0]);
		leafNodes[0].insertRecord(records[1]);
		leafNodes[0].insertRecord(records[2]);
		leafNodes[1].insertRecord(records[3]);
		leafNodes[1].insertRecord(records[4]);
		leafNodes[1].insertRecord(records[5]);
		leafNodes[2].insertRecord(records[6]);
		leafNodes[2].insertRecord(records[7]);
		leafNodes[2].insertRecord(records[8]);
		leafNodes[3].insertRecord(records[9]);
		leafNodes[3].insertRecord(records[10]);
		leafNodes[3].insertRecord(records[11]);
		leafNodes[4].insertRecord(records[12]);
		leafNodes[4].insertRecord(records[13]);
		leafNodes[4].insertRecord(records[14]);
		leafNodes[5].insertRecord(records[15]);
		leafNodes[5].insertRecord(records[16]);
		leafNodes[5].insertRecord(records[17]);
		leafNodes[6].insertRecord(records[18]);
		leafNodes[6].insertRecord(records[19]);
		leafNodes[6].insertRecord(records[20]);
		leafNodes[7].insertRecord(records[21]);
		leafNodes[7].insertRecord(records[22]);
		leafNodes[7].insertRecord(records[23]);
		leafNodes[8].insertRecord(records[24]);
		leafNodes[8].insertRecord(records[25]);
		leafNodes[8].insertRecord(records[26]);
		
		this.indexNodes = new BSharpTreeIndexNode[3];
		this.indexNodes[0] = new BSharpTreeIndexNode(this.tree, 1);
		this.indexNodes[0].setNodeNumber(2);
		this.indexNodes[1] = new BSharpTreeIndexNode(this.tree, 1);
		this.indexNodes[1].setNodeNumber(3);
		this.indexNodes[2] = new BSharpTreeIndexNode(this.tree, 1);
		this.indexNodes[2].setNodeNumber(4);
		
		this.indexNodes[0].indexChild(leafNodes[0]);
		this.indexNodes[0].indexChild(leafNodes[1]);
		this.indexNodes[0].indexChild(leafNodes[2]);
		this.indexNodes[1].indexChild(leafNodes[3]);
		this.indexNodes[1].indexChild(leafNodes[4]);
		this.indexNodes[1].indexChild(leafNodes[5]);
		this.indexNodes[2].indexChild(leafNodes[6]);
		this.indexNodes[2].indexChild(leafNodes[7]);
		this.indexNodes[2].indexChild(leafNodes[8]);
		
		this.sut.indexChild(this.indexNodes[0]);
		this.sut.indexChild(this.indexNodes[1]);
		this.sut.indexChild(this.indexNodes[2]);
		
		this.tree.registerNodeInMock(this.sut);
		this.tree.registerNodesInMock(this.indexNodes);
		this.tree.registerNodesInMock(this.leafNodes);
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testIndexChildren() {
		Assert.assertEquals(2, this.sut.getLeftChildNodeNumber());
		BSharpTreeIndexNodeElement element0 = (BSharpTreeIndexNodeElement) this.sut.getElements().get(0);
		Assert.assertEquals("040", ((StringField)element0.getKey()).getString());
		Assert.assertEquals(3, element0.getRightChildNodeNumber());
		BSharpTreeIndexNodeElement element1 = (BSharpTreeIndexNodeElement) this.sut.getElements().get(1);
		Assert.assertEquals("400", ((StringField)element1.getKey()).getString());
		Assert.assertEquals(4, element1.getRightChildNodeNumber());
	}

	@Test
	public void testInsertInCorrectLeaf1() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("733", 88));
		TestIndexRecord retrievedRec = (TestIndexRecord) leafNodes[8].getRecord(new StringField("733"));
		Assert.assertEquals("733", retrievedRec.getKey().getString());
		Assert.assertEquals(88, retrievedRec.getBlockNumber());
	}
	
	@Test
	public void testInsertInCorrectLeaf2() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("055", 68));
		TestIndexRecord retrievedRec = (TestIndexRecord) leafNodes[4].getRecord(new StringField("055"));
		Assert.assertEquals("055", retrievedRec.getKey().getString());
		Assert.assertEquals(68, retrievedRec.getBlockNumber());
	}
//
//	@Test
//	public void testInsertInCorrectLeafExtremeCase() throws RecordSerializationException, IOException {
//		this.sut.indexChild(leafNodes[0]);
//		this.sut.indexChild(leafNodes[1]);
//		this.sut.indexChild(leafNodes[2]);
//		this.sut.insertRecord(new TestIndexRecord("AAA", 88));
//		TestIndexRecord retrievedRec = (TestIndexRecord) leafNodes[0].getRecord(new StringField("AAA"));
//		Assert.assertEquals("AAA", retrievedRec.getKey().getString());
//		Assert.assertEquals(88, retrievedRec.getBlockNumber());
//	}

}
