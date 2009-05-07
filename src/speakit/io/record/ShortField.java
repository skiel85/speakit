package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.io.ByteArrayConverter;

public class ShortField extends Field {

	private short value = 0;

	public ShortField(short value) {
		this.value = value;
	}

	public ShortField() {
		this.value = 0;
	}

	public short getShort() {
		return this.value;
	}

	public void setShort(short value) {
		this.value = value;
	}

	@Override
	public int getSerializationSize() {
		return Short.SIZE / BYTE_SIZE;
	}

	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		this.value = ByteArrayConverter.toShort(readBytes(in));
	}

	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		out.write(ByteArrayConverter.toByteArray(this.value));
	}

	@Override
	protected int compareToSameClass(Field o) {
		ShortField other = (ShortField) o;
		if (this.value == other.value) {
			return 0;
		} else {
			return (this.value < other.value) ? -1 : 1;
		}
	}
	
	@Override
	protected String getStringRepresentation() {
		return "S"+this.value;
	}

}
