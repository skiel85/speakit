package speakit.io.bsharptree.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.ftrs.index.InvertedIndexIndexRecord;
import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.bsharptree.BSharpTree;
import speakit.io.bsharptree.BSharpTreeLeafNodeElement;
import speakit.io.bsharptree.BSharpTreeLeafNodeRecord;
import speakit.io.record.RecordSerializationException;

@SuppressWarnings("unchecked")
public class BSharpTreeLeafNodeRecordTest {
	private BSharpTreeLeafNodeRecord	sut;
	private BSharpTree tree;
	private File file;

	@Before
	public void setUp() throws Exception { 
		this.tree = new BSharpTree(this.file, InvertedIndexIndexRecord.createRecordFactory() , new InvertedIndexIndexRecordEncoder());
		this.sut = new BSharpTreeLeafNodeRecord(this.tree);
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("cuadrado", 1)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("cuadratura", 2)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("cuaderno", 2)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("cereza", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("cereza", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("las", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("las", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("las", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("maniobra", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("política", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("ocultaba", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("las", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("auténticas", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("intenciones", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("independentistas", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("de", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("los", 3)));
		this.sut.insertElement(new BSharpTreeLeafNodeElement(new InvertedIndexIndexRecord("revolucionarios", 3)));

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFrontCoding() throws RecordSerializationException, IOException {
		byte[] serialization = this.sut.serialize();
		BSharpTreeLeafNodeRecord deserialized = new BSharpTreeLeafNodeRecord(this.tree);
		deserialized.deserialize(serialization);
//		System.out.println("sut: " + sut.toString());
//		System.out.println("des: " + deserialized.toString());
		Assert.assertEquals(sut.toString(), deserialized.toString());
	}
}
