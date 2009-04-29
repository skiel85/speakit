package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.io.ByteArrayConverter;

public class IntegerField extends Field {

	private int value = 0;

	public IntegerField(int value) {
		this.value = value;
	}

	public IntegerField() {
		this.value = 0;
	}

	public int getInteger() {
		return this.value;
	}

	public void setInteger(int value) {
		this.value = value;
	}

	@Override
	public int getSerializationSize() {
		return Integer.SIZE / BYTE_SIZE;
	}

	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		this.value = ByteArrayConverter.toInt(readBytes(in));
	}

	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		out.write(ByteArrayConverter.toByteArray(this.value));
	}

	@Override
	protected int compareToSameClass(Field o) {
		IntegerField other = (IntegerField) o;
		if (this.value == other.value) {
			return 0;
		} else {
			return (this.value < other.value) ? -1 : 1;
		}
	}

}