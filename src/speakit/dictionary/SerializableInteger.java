package speakit.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerializableInteger extends SerializablePrimitiveType {

	private int value=0;
	
	public SerializableInteger(int value){
		this.value=value;
	}
	
	public SerializableInteger(){
		this.value=0;
	}
	
	public int getInteger(){
		return this.value;
	}
	
	public void setInteger(int value){
		this.value = value;
	}

	@Override
	public int getSerializationSize() {
		return Integer.SIZE/BYTE_SIZE;
	}

	@Override
	public void actuallyDeserialize(InputStream in) throws IOException {
		this.value= ByteArrayConverter.toInt(readBytes(in));
	}

	@Override
	public void actuallySerialize(OutputStream out) throws IOException {
		out.write(ByteArrayConverter.toByta(this.value));
	}

	@Override
	protected int compareToSameClass(SerializablePrimitiveType o) {
		SerializableInteger other = (SerializableInteger)o;
		return (this.value<other.value)?-1:1;
	}


}
