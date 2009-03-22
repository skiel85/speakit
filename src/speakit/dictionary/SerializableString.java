package speakit.dictionary;


public class SerializableString extends SerializableByteArray {
 
	String value;
	public SerializableString(String value) {
		this.setValue(value);
	}
	
	public SerializableString( ) {
		this("");
	}
	
	public String getStringValue(){
		return this.value;
	}
	public void setValue(String value){
		this.value=value;
	}
	
	public byte[] getValue(){
		return this.value.getBytes();
	} 
	public void setValue(byte[] value){
		this.value= ByteArrayConverter.toString(value);
	}
}
