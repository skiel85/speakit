package speakit.io.bsharptree;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("unchecked")
public class TreeLeafNodeElement extends TreeNodeElement {
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
	protected Field[] getFields() {
		throw new NotImplementedException();
	}

	@Override
	public Field getKey() {
		return this.record.getKey();
	}

	public Record getRecord() {
		return this.record;
	}

	@Override
	public int getSerializationSize() throws RecordSerializationException, IOException {
		return this.record.serialize().length;
	}

	@Override
	protected String getStringRepresentation() {
		return this.record.toString();
	}

	public void setRecord(Record record) {
		this.record = record;
	}
}
