package speakit.io.bsharptree;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.io.record.Field;
import speakit.io.record.Record;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BSharpTreeLeafNodeElement extends Field implements BSharpTreeNodeElement {
	private Record record;

	public BSharpTreeLeafNodeElement(Record record) {
		this.record = record;
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
		return record.compareTo(((BSharpTreeLeafNodeElement) o).getRecord());
	}

	@Override
	public int getSerializationSize() {
		throw new NotImplementedException();
	}

	public Record getRecord() {
		return this.record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}
}