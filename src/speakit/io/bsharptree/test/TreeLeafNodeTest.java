package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.bsharptree.RecordEncoder;
import speakit.io.bsharptree.Tree;
import speakit.io.bsharptree.TreeLeafNode;
import speakit.io.bsharptree.TreeLeafNodeElement;
import speakit.io.bsharptree.TreeNode;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.test.TestFileManager;

public class TreeLeafNodeTest {

	private TreeNode sut;
	private TestBSharpTree tree;
	private RecordEncoder encoder;
	private TestFileManager testFileManager;
	private File file;

	@Before
	public void setUp() throws Exception {
		encoder = new InvertedIndexIndexRecordEncoder();
		this.testFileManager = new TestFileManager("");
		this.file = this.testFileManager.openFile("testTree.dat");
		this.tree = new TestBSharpTree(this.file);
		this.sut = new TreeLeafNode(this.tree,-1);
	}

	@After
	public void tearDown() throws Exception {
		testFileManager.destroyFiles();
	}

	@Test
	public void testGetNodeKey() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("adios", 3));
		this.sut.insertRecord(new TestIndexRecord("mundo", 1));
		this.sut.insertRecord(new TestIndexRecord("cruel", 2));
		StringField retrievedKey = (StringField) this.sut.getNodeKey();
		Assert.assertEquals("adios", retrievedKey.getString());
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
		TreeLeafNodeElement firstElement = (TreeLeafNodeElement) this.sut.getElement(0);
		Assert.assertEquals("adios", ((StringField) firstElement.getKey()).getString());
		Assert.assertEquals(3, ((TestIndexRecord) firstElement.getRecord()).getBlockNumber());
		TreeLeafNodeElement secondElement = (TreeLeafNodeElement) this.sut.getElement(1);
		Assert.assertEquals("cruel", ((StringField) secondElement.getKey()).getString());
		Assert.assertEquals(2, ((TestIndexRecord) secondElement.getRecord()).getBlockNumber());
		TreeLeafNodeElement thirdElement = (TreeLeafNodeElement) this.sut.getElement(2);
		Assert.assertEquals("mundo", ((StringField) thirdElement.getKey()).getString());
		Assert.assertEquals(1, ((TestIndexRecord) thirdElement.getRecord()).getBlockNumber());
	}

	@Test
	public void testOverflow() throws RecordSerializationException, IOException {
		File file = File.createTempFile(this.getClass().getName(), ".dat");
		Tree<TestIndexRecord, StringField> tree = new Tree<TestIndexRecord, StringField>(file, TestIndexRecord.createFactory(), new InvertedIndexIndexRecordEncoder());
		tree.create(30);
		this.sut = new TreeLeafNode(tree,-1);
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("hola", 2));
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("supercalifragilisticoespialidoso", 2));
		this.sut.insertRecord(new TestIndexRecord("aaa_supercalifragilisticoespialidoso", 3));
		Assert.assertTrue(this.sut.isInOverflow());
		file.delete();
	}

}
