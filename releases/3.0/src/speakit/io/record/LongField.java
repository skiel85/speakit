package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.io.ByteArrayConverter;

public class LongField extends Field {

	private long value = 0;

	public LongField(long value) {
		this.value = value;
	}

	public LongField() {
		this.value = 0;
	}

	public long getLong() {
		return this.value;
	}

	public void setLong(long value) {
		this.value = value;
	}

	@Override
	public int getSerializationSize() {
		return Long.SIZE / BYTE_SIZE;
	}

	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		this.value = ByteArrayConverter.toLong(readBytes(in));
	}

	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		out.write(ByteArrayConverter.toByteArray(this.value));
	}

	@Override
	protected int compareToSameClass(Field o) {
		LongField other = (LongField) o;
		if (this.value == other.value) {
			return 0;
		} else {
			return (this.value < other.value) ? -1 : 1;
		}
	}
	
	@Override
	protected String getStringRepresentation() {
		return "L"+this.value;
	}
}
