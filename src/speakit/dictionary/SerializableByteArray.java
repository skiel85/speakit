package speakit.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerializableByteArray extends SerializablePrimitiveType {

	 
	byte[] value;
	public SerializableByteArray(byte[] value) {
		this.setBytes(value);
	}
	public SerializableByteArray() {
		this(new byte[]{});
	}
	
	public byte[] getBytes(){
		return this.value;
	}
	
	public void setBytes(byte[] value){
		this.value=value;
	}   
	
	public int getValueLenght(){
		return this.getBytes().length;
	}
	
	@Override
	public int getSerializationSize() {
		return new SerializableInteger(getValueLenght()).getSerializationSize() + getValueLenght();
	}

	/**
	 * lee primero el campo que indica la longitud y luego lee esa cantidad de bytes para hidratar el string 
	 */
	@Override
	public void actuallyDeserialize(InputStream in) throws IOException {
		SerializableInteger size=new SerializableInteger();
		size.actuallyDeserialize(in);		
		this.setBytes(this.readBytes(in,size.getInteger()));
	}	

	/**
	 * guarda primero un campo que indica la longitud y a continuacion los bytes del string 
	 */
	@Override	
	public void actuallySerialize(OutputStream out) throws IOException {
		SerializableInteger size = new SerializableInteger(getValueLenght());
		size.serialize(out);
		out.write(this.getBytes());
	}

}
