package speakit.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.Assert;

public class SerializableString extends SerializablePrimitiveType {
 
	String value;
	public SerializableString(String value) {
		this.setValue(value);
		
	}
	
	public SerializableString( ) {
		this("");
	}
	
	public String getValue(){
		return this.value;
	}
	
	public void setValue(String value){
		this.value=value;
	} 
	
	private int getStringBytesLength(){
		return this.value.getBytes().length;
	} 
 
	
	@Override
	public int getSerializationSize() {
		return new SerializableInteger(this.getStringBytesLength()).getSerializationSize() + this.getStringBytesLength();
	}

	/**
	 * lee primero el campo que indica la longitud y luego lee esa cantidad de bytes para hidratar el string 
	 */
	@Override
	public void actuallyDeserialize(InputStream in) throws IOException {
		SerializableInteger size=new SerializableInteger();
		size.actuallyDeserialize(in);		
		this.value=ByteArrayConverter.toString(this.readBytes(in,size.getValue()));
	}	

	/**
	 * guarda primero un campo que indica la longitud y a continuacion los bytes del string 
	 */
	@Override	
	public void actuallySerialize(OutputStream out) throws IOException {
		SerializableInteger size = new SerializableInteger(this.getStringBytesLength());
		size.serialize(out);
		out.write(ByteArrayConverter.toByta(this.value.getBytes()));
	}
}
