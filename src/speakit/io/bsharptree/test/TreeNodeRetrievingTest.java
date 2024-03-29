package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.io.blockfile.BasicBlockFile;
import speakit.io.bsharptree.Tree;
import speakit.io.bsharptree.TreeLeafNodeRecord;
import speakit.io.bsharptree.TreeNode;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class TreeNodeRetrievingTest {
	private File file;
	private Tree sut;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.sut = new Tree(file, TestIndexRecord.createFactory());
		this.sut.create(15);
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	@Ignore
	public void testGetNode() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("prueba", 123));
		TreeNode node = this.sut.getNode(0, null);
		Assert.assertNotNull("Es nulo", node);
		Assert.assertNotNull("Es nulo", node.getRecord(new StringField("prueba")));
	}

	@Test
	public void testInstallation() throws RecordSerializationException, IOException {
		BasicBlockFile blocksFile = this.sut.getBlockFile();

		Assert.assertEquals(2, blocksFile.getBlockCount());
		Assert.assertEquals(2, sut.getRootNoteBlocksQty());

		// Se hace s�lo para probar que no arroja excepci�n
		ArrayList<byte[]> rootSerializationParts = new ArrayList<byte[]>();
		rootSerializationParts.add(blocksFile.read(0));
		rootSerializationParts.add(blocksFile.read(1));
		TreeLeafNodeRecord record = new TreeLeafNodeRecord(2, this.sut);
		record.deserializeFromParts(rootSerializationParts);
	}

	@Test
	public void testSaveNode() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("prueba", 123));
		TreeNode node = this.sut.getNode(0, this.sut.getRoot());
		this.sut.updateNode(node);
	}
}
