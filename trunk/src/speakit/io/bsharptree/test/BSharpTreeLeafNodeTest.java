package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.io.bsharptree.BSharpTree;
import speakit.io.bsharptree.BSharpTreeLeafNode;
import speakit.io.bsharptree.BSharpTreeLeafNodeElement;
import speakit.io.bsharptree.BSharpTreeNodeElement;
import speakit.io.bsharptree.RecordEncoder;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.test.TestFileManager;

public class BSharpTreeLeafNodeTest {

	private BSharpTreeLeafNode sut;
	private TestBSharpTree tree;
	private RecordEncoder encoder;
	private Iterator<String> testStrings;
	private TestFileManager testFileManager;
	private File file;

	@Before
	public void setUp() throws Exception {
		this.testFileManager = new TestFileManager("");
		this.file = this.testFileManager.openFile("testTree.dat");
		this.tree = new TestBSharpTree(this.file);
		this.encoder = new TestRecordEncoder();
		this.sut = new BSharpTreeLeafNode(this.tree, 1, this.encoder);
		this.testStrings = new TextDocument(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur lacinia eleifend ante ut suscipit. Pellentesque porta urna sit amet leo egestas eu rhoncus dui faucibus. In hac habitasse platea dictumst. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris ut massa ante. Suspendisse potenti. Curabitur a nisi non mi viverra elementum id et magna. Mauris eu ipsum eu nulla posuere bibendum. Suspendisse et elit magna. Sed malesuada, turpis eget dapibus vestibulum, augue arcu hendrerit mi, sit amet scelerisque ipsum nulla ullamcorper ipsum. Integer aliquet, leo ac commodo malesuada, augue justo auctor elit, vel auctor nulla mi ac nulla. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum ac justo sit amet massa varius tempus eu at ipsum. Etiam semper nisl ac nulla molestie vestibulum. Nunc nec ante at nisl tempus placerat.")
				.iterator();
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

	@Test
	public void testOverflows() throws RecordSerializationException, IOException {
		File file = File.createTempFile(this.getClass().getName(), ".dat");
		BSharpTree<TestIndexRecord, StringField> tree = new BSharpTree<TestIndexRecord, StringField>(file, new TestRecordEncoder()) {
			@SuppressWarnings("unchecked")
			@Override
			public Record createRecord() {
				return new TestIndexRecord("", 0);
			}
		};
		tree.create(25);
		this.sut = new BSharpTreeLeafNode(tree, 1, encoder);
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("hola", 2));
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("supercalifragilisticoespialidoso", 2));
		this.sut.insertRecord(new TestIndexRecord("aaa_supercalifragilisticoespialidoso", 3));
		Assert.assertTrue(this.sut.isInOverflow());
	}

	@Test
	public void testNotOverflows() throws RecordSerializationException, IOException {
		BSharpTree<TestIndexRecord, StringField> tree = new BSharpTree<TestIndexRecord, StringField>(this.file, new TestRecordEncoder()) {
			@SuppressWarnings("unchecked")
			@Override
			public Record createRecord() {
				return new TestIndexRecord("", 0);
			}
		};
		tree.create(1024);
		this.sut = (BSharpTreeLeafNode) tree.getRoot();
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("hola", 2));
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("supercalifragilisticoespialidoso", 2));
		this.sut.insertRecord(new TestIndexRecord("aaa_supercalifragilisticoespialidoso", 3));
		Assert.assertFalse(this.sut.isInOverflow());
	}

	@Test
	public void testExtractMinimumCapacityExcedent() throws RecordSerializationException, IOException {
		BSharpTree<TestIndexRecord, StringField> tree = new BSharpTree<TestIndexRecord, StringField>(this.file, new TestRecordEncoder()) {
			@SuppressWarnings("unchecked")
			@Override
			public Record createRecord() {
				return new TestIndexRecord("", 0);
			}
		};
		tree.create(50);
		this.sut = (BSharpTreeLeafNode) tree.getRoot();
		Assert.assertFalse("El nodo no debe estar en overflow al principio pero lo está.", this.sut.isInOverflow());
		Assert.assertTrue("El nodo debe estar en underflow al principio pero no lo está.", this.sut.isInUnderflow());

		while (this.sut.isInUnderflow()) {
			this.sut.insertRecord(new TestIndexRecord(testStrings.next(), 2));
		}

		//El caracter ~ es para que se inserte al final, ya que se inserta ordenado.
		this.sut.insertRecord(new TestIndexRecord("~parte", 8));
		this.sut.insertRecord(new TestIndexRecord("~512", 5));
		this.sut.insertRecord(new TestIndexRecord("~ciclo", 3));

		List<BSharpTreeNodeElement> excedent = this.sut.extractMinimumCapacityExcedent();
		
		Assert.assertFalse("El registro no debería estar en el nodo.", this.sut.contains(new StringField("~parte")));
		Assert.assertFalse("El registro no debería estar en el nodo.", this.sut.contains(new StringField("~512")));
		Assert.assertFalse("El registro no debería estar en el nodo.", this.sut.contains(new StringField("~ciclo")));

		//Recordar que el excedente sale ordenado.
		Assert.assertEquals(new StringField("~512").getString(), ((StringField) excedent.get(0).getKey()).getString());
		Assert.assertEquals(new StringField("~ciclo").getString(), ((StringField) excedent.get(1).getKey()).getString());
		Assert.assertEquals(new StringField("~parte").getString(), ((StringField) excedent.get(2).getKey()).getString());
	}
}
