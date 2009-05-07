package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.bsharptree.BSharpTreeLeafNode;
import speakit.io.bsharptree.BSharpTreeLeafNodeElement;
import speakit.io.bsharptree.RecordEncoder;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.test.TestFileManager;

public class BSharpTreeLeafNodeTest {

	private BSharpTreeLeafNode sut;
	private TestBSharpTree tree;
	private RecordEncoder encoder;
	private TestFileManager testFileManager;
	private File file;

	@Before
	public void setUp() throws Exception {
		this.testFileManager = new TestFileManager("");
		this.file = this.testFileManager.openFile("testTree.dat");
		this.tree = new TestBSharpTree(this.file);
		this.encoder = new TestRecordEncoder();
		this.sut = new BSharpTreeLeafNode(this.tree, 1, this.encoder);
	}

	@After
	public void tearDown() throws Exception {
		testFileManager.destroyFiles();
	}

	@Test
	public void testInsertAndRetrieve() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("hola", 1));
		this.sut.insertRecord(new TestIndexRecord("mundo", 3));
		TestIndexRecord retrievedRec = (TestIndexRecord) this.sut.getRecord(new StringField("mundo"));
		Assert.assertEquals("mundo", retrievedRec.getKey().getString());
		Assert.assertEquals(3, retrievedRec.getBlockNumber());
	}

	@Test
	public void testInsertsOrdered() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("adios", 3));
		this.sut.insertRecord(new TestIndexRecord("mundo", 1));
		this.sut.insertRecord(new TestIndexRecord("cruel", 2));
		BSharpTreeLeafNodeElement firstElement = (BSharpTreeLeafNodeElement) this.sut.getElements().get(0);
		Assert.assertEquals("adios", ((StringField) firstElement.getKey()).getString());
		Assert.assertEquals(3, ((TestIndexRecord) firstElement.getRecord()).getBlockNumber());
		BSharpTreeLeafNodeElement secondElement = (BSharpTreeLeafNodeElement) this.sut.getElements().get(1);
		Assert.assertEquals("cruel", ((StringField) secondElement.getKey()).getString());
		Assert.assertEquals(2, ((TestIndexRecord) secondElement.getRecord()).getBlockNumber());
		BSharpTreeLeafNodeElement thirdElement = (BSharpTreeLeafNodeElement) this.sut.getElements().get(2);
		Assert.assertEquals("mundo", ((StringField) thirdElement.getKey()).getString());
		Assert.assertEquals(1, ((TestIndexRecord) thirdElement.getRecord()).getBlockNumber());
	}

	@Test
	public void testGetNodeKey() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("adios", 3));
		this.sut.insertRecord(new TestIndexRecord("mundo", 1));
		this.sut.insertRecord(new TestIndexRecord("cruel", 2));
		StringField retrievedKey = (StringField) this.sut.getNodeKey();
		Assert.assertEquals("adios", retrievedKey.getString());
	}
}
