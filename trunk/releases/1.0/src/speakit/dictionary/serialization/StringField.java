package speakit.dictionary.serialization;

public class StringField extends ByteArrayField {

	private String value;

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
}
