package speakit.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerializableInteger extends SerializablePrimitiveType {

	int value=0;
	
	public SerializableInteger(int value){
		this.value=value;
	}
	
	public SerializableInteger(){
		this.value=0;
	}
	
	public int getValue(){
		return this.value;
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
}
