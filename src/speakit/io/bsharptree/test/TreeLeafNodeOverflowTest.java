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
import speakit.io.bsharptree.DefaultRecordEncoder;
import speakit.io.bsharptree.RecordEncoder;
import speakit.io.bsharptree.Tree;
import speakit.io.bsharptree.TreeLeafNode;
import speakit.io.bsharptree.TreeNodeElement;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.test.TestFileManager;

public class TreeLeafNodeOverflowTest {

	private TreeLeafNode sut;
	private Tree<TestIndexRecord, StringField> tree;
	private RecordEncoder encoder;
	private Iterator<String> testStrings;
	private TestFileManager testFileManager;
	private File file;

	@Before
	public void setUp() throws Exception {
		this.testFileManager = new TestFileManager("");
		this.file = this.testFileManager.openFile("testTree.dat");
		encoder = new DefaultRecordEncoder(TestIndexRecord.createFactory());
		this.tree = new Tree<TestIndexRecord, StringField>(this.file, TestIndexRecord.createFactory(), encoder);
		this.sut = new TreeLeafNode(this.tree);
		this.testStrings = new TextDocument(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur lacinia eleifend ante ut suscipit. Pellentesque porta urna sit amet leo egestas eu rhoncus dui faucibus. In hac habitasse platea dictumst. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris ut massa ante. Suspendisse potenti. Curabitur a nisi non mi viverra elementum id et magna. Mauris eu ipsum eu nulla posuere bibendum. Suspendisse et elit magna. Sed malesuada, turpis eget dapibus vestibulum, augue arcu hendrerit mi, sit amet scelerisque ipsum nulla ullamcorper ipsum. Integer aliquet, leo ac commodo malesuada, augue justo auctor elit, vel auctor nulla mi ac nulla. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum ac justo sit amet massa varius tempus eu at ipsum. Etiam semper nisl ac nulla molestie vestibulum. Nunc nec ante at nisl tempus placerat.")
				.iterator();
	}

	@After
	public void tearDown() throws Exception {
		testFileManager.destroyFiles();
	}

	@Test
	public void testOverflows() throws RecordSerializationException, IOException {
		tree.create(30);
		this.sut = (TreeLeafNode) tree.getRoot();
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("hola", 2));
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("supercalifragilisticoespialidoso", 2));
		this.sut.insertRecord(new TestIndexRecord("aaa_supercalifragilisticoespialidoso", 3));
		Assert.assertTrue(this.sut.isInOverflow());
	}

	@Test
	public void testNotOverflows() throws RecordSerializationException, IOException {
		tree.create(1024);
		this.sut = (TreeLeafNode) tree.getRoot();
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("hola", 2));
		Assert.assertFalse(this.sut.isInOverflow());
		this.sut.insertRecord(new TestIndexRecord("supercalifragilisticoespialidoso", 2));
		this.sut.insertRecord(new TestIndexRecord("aaa_supercalifragilisticoespialidoso", 3));
		Assert.assertFalse(this.sut.isInOverflow());
	}

	@Test
	public void testExtractMinimumCapacityExcedent() throws RecordSerializationException, IOException {
		tree.create(255);
		this.sut = (TreeLeafNode) tree.getRoot();
		Assert.assertFalse("El nodo no debe estar en overflow al principio pero lo está.", this.sut.isInOverflow());
		Assert.assertTrue("El nodo debe estar en underflow al principio pero no lo está.", this.sut.isInUnderflow());

		while (this.sut.isInUnderflow()) {
			this.sut.insertRecord(new TestIndexRecord(testStrings.next(), 2));
		}

		// El caracter ~ es para que se inserte al final, ya que se inserta
		// ordenado.
		this.sut.insertRecord(new TestIndexRecord("~parte", 8));
		this.sut.insertRecord(new TestIndexRecord("~512", 5));
		this.sut.insertRecord(new TestIndexRecord("~ciclo", 3));

		List<TreeNodeElement> excedent = this.sut.extractMinimumCapacityExcedent();

		Assert.assertFalse("Luego de extraer el excedente el nodo no debería estar en underflow.", this.sut.isInUnderflow());

		Assert.assertFalse("El registro no debería estar en el nodo.", this.sut.contains(new StringField("~parte")));
		Assert.assertFalse("El registro no debería estar en el nodo.", this.sut.contains(new StringField("~512")));
		Assert.assertFalse("El registro no debería estar en el nodo.", this.sut.contains(new StringField("~ciclo")));

		// Recordar que el excedente sale ordenado.
		Assert.assertEquals(new StringField("~512").getString(), ((StringField) excedent.get(excedent.size() - 3).getKey()).getString());
		Assert.assertEquals(new StringField("~ciclo").getString(), ((StringField) excedent.get(excedent.size() - 2).getKey()).getString());
		Assert.assertEquals(new StringField("~parte").getString(), ((StringField) excedent.get(excedent.size() - 1).getKey()).getString());
	}

	@Test
	public void testExtractMaximumCapacityExcedent() throws RecordSerializationException, IOException {
		tree.create(255);
		this.sut = (TreeLeafNode) tree.getRoot();
		Assert.assertFalse("El nodo no debe estar en overflow al principio pero lo está.", this.sut.isInOverflow());
		Assert.assertTrue("El nodo debe estar en underflow al principio pero no lo está.", this.sut.isInUnderflow());

		while (!this.sut.isInOverflow()) {
			this.sut.insertRecord(new TestIndexRecord(testStrings.next(), 2));
		}

		// El caracter ~ es para que se inserte al final, ya que se inserta
		// ordenado.
		this.sut.insertRecord(new TestIndexRecord("~parte", 8));
		this.sut.insertRecord(new TestIndexRecord("~512", 5));
		this.sut.insertRecord(new TestIndexRecord("~ciclo", 3));

		List<TreeNodeElement> excedent = this.sut.extractUpperExcedent();

		Assert.assertFalse("Luego de extraer el excedente el nodo no debería estar en overflow.", this.sut.isInOverflow());

		Assert.assertFalse("El registro no debería estar en el nodo.", this.sut.contains(new StringField("~parte")));
		Assert.assertFalse("El registro no debería estar en el nodo.", this.sut.contains(new StringField("~512")));
		Assert.assertFalse("El registro no debería estar en el nodo.", this.sut.contains(new StringField("~ciclo")));

		// Recordar que el excedente sale ordenado.
		Assert.assertEquals(new StringField("~512").getString(), ((StringField) excedent.get(excedent.size() - 3).getKey()).getString());
		Assert.assertEquals(new StringField("~ciclo").getString(), ((StringField) excedent.get(excedent.size() - 2).getKey()).getString());
		Assert.assertEquals(new StringField("~parte").getString(), ((StringField) excedent.get(excedent.size() - 1).getKey()).getString());
	}

}
