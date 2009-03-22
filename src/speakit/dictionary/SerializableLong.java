package speakit.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SerializableLong extends SerializablePrimitiveType {

	private long value = 0;
	
	public SerializableLong(long value){
		this.value = value;
	}
	
	public SerializableLong(){
		this.value = 0;
	}
	
	public long getLong(){
		return this.value;
	}

	@Override
	public int getSerializationSize() {
		return Long.SIZE/BYTE_SIZE;
	}

	@Override
	public void actuallyDeserialize(InputStream in) throws IOException {
		this.value = ByteArrayConverter.toLong(readBytes(in));
	}

	@Override
	public void actuallySerialize(OutputStream out) throws IOException {
		out.write(ByteArrayConverter.toByta(this.value));
	} 
	
	@Override
	protected int compareToSameClass(SerializablePrimitiveType o) {
		SerializableLong other = (SerializableLong)o;
		return (this.value<other.value)?-1:1;
	}
}
