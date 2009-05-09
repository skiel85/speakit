package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.ftrs.index.InvertedIndexIndexRecord;
import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.bsharptree.Tree;
import speakit.io.bsharptree.TreeLeafNodeElement;
import speakit.io.bsharptree.TreeLeafNodeRecord;
import speakit.io.record.RecordSerializationException;

@SuppressWarnings("unchecked")
public class TreeLeafNodeRecordTest {
	private TreeLeafNodeRecord sut;
	private Tree tree;
	private File file;

	@Before
	public void setUp() throws Exception {
		this.tree = new Tree(this.file, InvertedIndexIndexRecord.createRecordFactory(), new InvertedIndexIndexRecordEncoder());
		this.sut = new TreeLeafNodeRecord(this.tree);
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("cuadrado", 1)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("cuadratura", 2)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("cuaderno", 2)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("cereza", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("cereza", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("las", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("las", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("las", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("maniobra", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("política", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("ocultaba", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("las", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("auténticas", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("intenciones", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("independentistas", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("de", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("los", 3)));
		this.sut.addElement(new TreeLeafNodeElement(new InvertedIndexIndexRecord("revolucionarios", 3)));

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFrontCoding() throws RecordSerializationException, IOException {
		byte[] serialization = this.sut.serialize();
		TreeLeafNodeRecord deserialized = new TreeLeafNodeRecord(this.tree);
		deserialized.deserialize(serialization);
		// System.out.println("sut: " + sut.toString());
		// System.out.println("des: " + deserialized.toString());
		Assert.assertEquals(sut.toString(), deserialized.toString());
	}
}
