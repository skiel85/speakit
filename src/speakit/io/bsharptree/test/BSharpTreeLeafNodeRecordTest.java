package speakit.io.bsharptree.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.ftrs.index.InvertedIndexIndexRecord;
import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.bsharptree.BSharpTreeLeafNodeElement;
import speakit.io.bsharptree.BSharpTreeLeafNodeRecord;
import speakit.io.bsharptree.RecordEncoder;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;

public class BSharpTreeLeafNodeRecordTest {
	private RecordFactory				recordFactory;
	private RecordEncoder				encoder;

	private BSharpTreeLeafNodeRecord	sut;

	@Before
	public void setUp() throws Exception { 
		encoder = new InvertedIndexIndexRecordEncoder();
		recordFactory = new RecordFactory() {
			@Override
			public Record createRecord() {
				return new InvertedIndexIndexRecord();
			}

		};
		this.sut = new BSharpTreeLeafNodeRecord(recordFactory, encoder);
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
		BSharpTreeLeafNodeRecord deserialized = new BSharpTreeLeafNodeRecord(recordFactory, encoder);
		deserialized.deserialize(serialization);
//		System.out.println("sut: " + sut.toString());
//		System.out.println("des: " + deserialized.toString());
		Assert.assertEquals(sut.toString(), deserialized.toString());
	}
}
