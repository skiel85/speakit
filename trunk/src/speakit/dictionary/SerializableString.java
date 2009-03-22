package speakit.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.Assert;

public class SerializableString extends SerializablePrimitiveType {

	SerializableInteger valueSize=null;
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
		this.valueSize=new SerializableInteger(this.getStringBytesLength());
	} 
	
	private int getStringBytesLength(){
		return this.value.getBytes().length;
	}
 
	
	@Override
	public int getSerializationSize() {
		return this.valueSize.getSerializationSize() + this.getStringBytesLength();
	}
	
	@Override
	public void actuallyDeserialize(InputStream in) throws IOException {
		SerializableInteger size=new SerializableInteger();
		size.actuallyDeserialize(in);		
		this.value=ByteArrayConverter.toString(this.readBytes(in,size.getValue()));
		Assert.assertEquals(size.getValue(), this.getSerializationSize());
	} 
	
	@Override
	public void actuallySerialize(OutputStream out) throws IOException {
		this.valueSize.actuallySerialize(out);
		out.write(ByteArrayConverter.toByta(this.value.getBytes()));
	}
}
