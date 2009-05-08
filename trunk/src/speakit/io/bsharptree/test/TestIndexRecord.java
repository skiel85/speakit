package speakit.io.bsharptree.test;

import speakit.ftrs.index.InvertedIndexIndexRecord;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;

public class TestIndexRecord extends InvertedIndexIndexRecord {

	public static RecordFactory createFactory() {
		return new RecordFactory() {

			@Override
			public Record createRecord() {
				return new TestIndexRecord("", 0);
			}

		};
	}
	// private StringField key = new StringField();
	//
	// @Override
	// public StringField getKey() {
	// return this.key;
	// }
	//
	// public TestIndexRecord(String key, int block) {
	// this.key.setString(key);
	// this.setBlockNumber(block);
	// }
	//	
	// public TestIndexRecord() {
	// this("",0);
	// }
	//
	// @Override
	// protected String getStringRepresentation() {
	// return "TestIndexRecord{key:"+this.key.toString()+"}";
	// }

	public TestIndexRecord() {
		// Dejado indentencionalmente en blanco.
	}

	public TestIndexRecord(String key, int blockNumber) {
		super(key, blockNumber);
	}

	// public long deserialize(InputStream in, FrontCodingWordDecoder decoder)
	// throws IOException {
	// return key.deserialize(in, decoder)+this.block.deserialize(in);
	// }
	// 
	// public long serialize(OutputStream out, FrontCodingWordEncoder encoder)
	// throws IOException {
	// return key.serialize(out, encoder)+this.block.serialize(out);
	// }
}
