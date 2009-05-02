package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public abstract class CompositeField extends Field {
	private ArrayList<Field> fields = new ArrayList<Field>();

	protected void addField(Field field) {
		this.fields.add(field);
	}

	protected void removeField(int index) {
		this.fields.remove(index);
	}

	protected void removeField(Field field) {
		this.fields.remove(field);
	}

	protected Field getField(int index) {
		return this.fields.get(index);
	}

	protected int getFieldCount() {
		return this.fields.size();
	}

	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		for (Field field : this.fields) {
			field.deserialize(in);
		}
	}

	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		for (Field field : this.fields) {
			field.serialize(out);
		}
	}

	@Override
	protected int compareToSameClass(Field o) {
		for (Field field : this.fields) {
			int comparationResult = field.compareTo(o);
			if (comparationResult != 0)
				return comparationResult;
		}
		return 0;
	}

	@Override
	public int getSerializationSize() {
		int accum = 0;
		for (Field field : this.fields) {
			accum += field.getSerializationSize();
		}
		return accum;
	}

}