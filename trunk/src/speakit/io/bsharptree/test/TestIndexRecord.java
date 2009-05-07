package speakit.io.bsharptree.test;

import speakit.io.record.IndexRecord;
import speakit.io.record.StringField;

public class TestIndexRecord extends IndexRecord<StringField>  {
	private StringField key = new StringField();

	@Override
	public StringField getKey() {
		return this.key;
	}

	public TestIndexRecord(String key, int block) {
		this.key.setString(key);
		this.setBlockNumber(block);
	}
	
	public TestIndexRecord() {
		this("",0);
	}

	@Override
	protected String getStringRepresentation() {
		return "TestIndexRecord{key:"+this.key.toString()+"}";
	}
 
//	public long deserialize(InputStream in, FrontCodingWordDecoder decoder) throws IOException {
//		return key.deserialize(in, decoder)+this.block.deserialize(in);
//	}
// 
//	public long serialize(OutputStream out, FrontCodingWordEncoder encoder) throws IOException {
//		return key.serialize(out, encoder)+this.block.serialize(out);
//	}
}
