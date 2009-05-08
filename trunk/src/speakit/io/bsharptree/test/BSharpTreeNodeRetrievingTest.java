package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.blockfile.BasicBlockFile;
import speakit.io.bsharptree.BSharpTree;
import speakit.io.bsharptree.BSharpTreeLeafNodeRecord;
import speakit.io.bsharptree.BSharpTreeNode;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;

public class BSharpTreeNodeRetrievingTest { 
	private File				file;
	private  BSharpTree	sut;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.sut = new  BSharpTree(file,TestIndexRecord.createFactory());
		this.sut.create(15);
	}

	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testInstallation() throws RecordSerializationException, IOException {
		BasicBlockFile blocksFile = this.sut.getBlockFile();

		Assert.assertEquals(2, blocksFile.getBlockCount());
		Assert.assertEquals(2, sut.getRootNoteBlocksQty());

		// Se hace sólo para probar que no arroja excepción
		ArrayList<byte[]> rootSerializationParts = new ArrayList<byte[]>();
		rootSerializationParts.add(blocksFile.read(0));
		rootSerializationParts.add(blocksFile.read(1));
		BSharpTreeLeafNodeRecord record = new BSharpTreeLeafNodeRecord(this.sut);
		record.deserializeFromParts(rootSerializationParts);
	}

	@Test @Ignore
	public void testGetNode() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("prueba", 123));
		BSharpTreeNode node = this.sut.getNode(0, null);
		Assert.assertNotNull("Es nulo", node);
		Assert.assertNotNull("Es nulo", node.getRecord(new StringField("prueba")));
	}

	@Test
	public void testSaveNode() throws RecordSerializationException, IOException {
		this.sut.insertRecord(new TestIndexRecord("prueba", 123));
		BSharpTreeNode node = this.sut.getNode(0, this.sut.getRoot());
		this.sut.saveNode(node);
	}
}
