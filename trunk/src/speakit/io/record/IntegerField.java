package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import speakit.io.ByteArrayConverter;

public class IntegerField extends Field {

	private static final int	INTSIZE	= Integer.SIZE / BYTE_SIZE;
	private int value = 0;
	private boolean	hashed=true;

	public IntegerField(int value) {
		this.value = value;
	}
	
	public IntegerField(int value,boolean hashed) {
		this.hashed = hashed;
		this.value = value;
	}

	public IntegerField() {
		this.value = 0;
	}
	
	public IntegerField(boolean hashed) {
		this.hashed = hashed;
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
		return (hashed?1:0)  + INTSIZE;
	}

	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		byte[] intBytes = readBytes(in,INTSIZE);
		this.value = ByteArrayConverter.toInt(intBytes);
		if(hashed){
			byte[] hash = readBytes(in,1);
			byte calculateHash = Record.calculateHash(value);
			if(hash[0] != calculateHash){
				throw new RecordSerializationCorruptDataException( hash[0],calculateHash);
			}
		}
	} 
	
	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		out.write(ByteArrayConverter.toByteArray(this.value));
		if(hashed){
			out.write(new byte[]{ Record.calculateHash(this.value)});
		}
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
	
	@Override
	protected String getStringRepresentation() {
		return "I"+this.value+(hashed?Record.calculateHash(value):"");
	}

	public static FieldFactory createFactory() {
		return new FieldFactory(){@Override public Field createField() {return new IntegerField();}};
	}

}
