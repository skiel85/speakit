package speakit.dictionary;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class SerializableString extends SerializableByteArray {
 
	private String value;
	public SerializableString(String value) {
		this.setString(value);
	}
	
	public SerializableString( ) {
		this("");
	}
	
	public String getString(){
		return this.value;
	}
	public void setString(String value){
		this.value=value;
	}
	
	public byte[] getBytes(){
		return this.value.getBytes();
	} 
	public void setBytes(byte[] value){
		this.value= ByteArrayConverter.toString(value);
	}
	
	@Override
	protected int compareToSameClass(SerializablePrimitiveType o) {
		SerializableString other = (SerializableString)o;
		return this.getString().compareTo(other.getString());
	}
}