package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.bsharptree.BSharpTree;
import speakit.io.bsharptree.BSharpTreeLeafNode;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.test.TestFileManager;

public class BSharpTreeLeafNodeTest {

	private BSharpTreeLeafNode sut;
	private TestBSharpTree tree;

	@Before
	public void setUp() throws Exception {
		tree = new TestBSharpTree(new TestFileManager("").openFile("testTree.dat"));
		this.sut = new BSharpTreeLeafNode(tree, 1);
	}

	@After
	public void tearDown() throws Exception {
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
		TestIndexRecord retrievedRec = (TestIndexRecord) this.sut.getRecord(new StringField("mundo"));
		Assert.assertEquals("mundo", retrievedRec.getKey().getString());
		Assert.assertEquals(1, retrievedRec.getBlockNumber());
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
	public void testOverflow() throws RecordSerializationException, IOException {
		File file = File.createTempFile(this.getClass().getName(), ".dat");
		BSharpTree<TestIndexRecord, StringField> tree = new BSharpTree<TestIndexRecord, StringField>(file) {
			@SuppressWarnings("unchecked")
			@Override
			public Record createRecord() {
				return new TestIndexRecord("", 0);
			}
		};
		tree.create(25);
		this.sut = new BSharpTreeLeafNode(tree, 1);
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("hola", 2));
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("supercalifragilisticoespialidoso", 2));
		Assert.assertTrue(this.sut.isInOverflow());
		file.delete();
	}
}
