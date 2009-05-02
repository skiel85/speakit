package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class CompositeField extends Field {
	protected abstract Field[] getFields();
	
	protected int getFieldCount() {
		return this.getFields().length;
	}

	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		for (Field field : this.getFields()) {
			field.deserialize(in);
		}
	}

	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		for (Field field : this.getFields()) {
			field.serialize(out);
		}
	}

	@Override
	protected int compareToSameClass(Field o) {
		for (Field field : this.getFields()) {
			int comparationResult = field.compareTo(o);
			if (comparationResult != 0)
				return comparationResult;
		}
		return 0;
	}

	@Override
	public int getSerializationSize() {
		int accum = 0;
		for (Field field : this.getFields()) {
			accum += field.getSerializationSize();
		}
		return accum;
	}

}