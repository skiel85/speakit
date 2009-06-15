package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;

import speakit.io.ByteArrayConverter;

public class StringField extends ByteArrayField {

	private String	value;

	public StringField(String value) {
		this.setString(value);
	}

	public StringField() {
		this("");
	}

	public String getString() {
		return this.value;
	}

	public void setString(String value) {
		this.value = value;
	}

	public byte[] getBytes() {
		return this.value.getBytes();
	}

	public void setBytes(byte[] value) {
		this.value = ByteArrayConverter.toString(value);
	}

	@Override
	protected int compareToSameClass(Field o) {
		StringField other = (StringField) o;
		return this.getString().compareTo(other.getString());
	}

	@Override
	public long deserialize(InputStream in) throws IOException {
		long deserialize = super.deserialize(in);
		return deserialize;
	}

	@Override
	protected String getStringRepresentation() {
		return "\"" +  this.value + "\"";
	}
}
