package speakit.dictionary.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BooleanField extends Field {
	private boolean value = false;

	public BooleanField(boolean value) {
		this.value = value;
	}

	public BooleanField() {
		this.value = false;
	}

	public boolean getBoolean() {
		return this.value;
	}

	public void setBoolean(boolean value) {
		this.value = value;
	}

	@Override
	public int getSerializationSize() {
		return 1;
	}

	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		this.value = ByteArrayConverter.toBoolean(readBytes(in));
	}

	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		out.write(ByteArrayConverter.toByteArray(this.value));
	}

	@Override
	protected int compareToSameClass(Field o) {
		BooleanField other = (BooleanField) o;

		if (this.value == other.value) {
			return 0;
		} else {
			return (this.value = true) ? -1 : 1;
		}
	}

}