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
	
	protected int compareToSameClass(SerializablePrimitiveType o){	
		SerializableByteArray other=(SerializableByteArray)o;

		byte[] thisBytes=this.getBytes();
		byte[] otherBytes=other.getBytes();

		//se recorren los arrays desde 0 hasta el indice del array mas chico, si algun elemento es difenente se devuelve la comparacion entre esos elementos
		//si son iguales hasta el último elemento se resuelve mas abajo
		for (int i = 0; i < min(thisBytes.length,otherBytes.length)-1; i++) {
			if (thisBytes[i]!=otherBytes[i]){
				return (thisBytes[i]<otherBytes[i])?-1:+1;
			}
		}
		//Se comparan los largos de los arrays
		//Si los tamaños son iguales ==> arrays iguales
		if (thisBytes.length==otherBytes.length){
			return 0;
		}else{
			//Si el de tamaño mínimo es el array de este objeto ==> este objeto es MENOR que el otro
			if(min(thisBytes.length,otherBytes.length)==thisBytes.length){
				return -1;
			}else{
				return 1;
			}
		} 
	}


	private int min(int length, int length2) {
		return (length<length2)?length:length2;
	} 

}
