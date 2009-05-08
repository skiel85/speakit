package speakit.io.bsharptree;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;

public class TreeLeafNodeElement extends Field implements TreeNodeElement {
	
	private Record record;

	public TreeLeafNodeElement(Record record) {
		this.record = record;
	}
	
	public TreeLeafNodeElement(RecordFactory factory) {
		this.record = factory.createRecord();
	}

	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		record.deserialize(in);
	}

	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		record.serialize(out);
	}

	@Override
	protected int compareToSameClass(Field o) {
		return record.compareTo(((TreeLeafNodeElement) o).getRecord());
	}

	@Override
	public int getSerializationSize() throws RecordSerializationException, IOException {
			return this.record.serialize().length;
	}

	public Record getRecord() {
		return this.record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	@Override
	public Field getKey() {
		return this.record.getKey();
	}
	
	@Override
	protected String getStringRepresentation() {
		return this.record.toString();
	} 
}
