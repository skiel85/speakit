package speakit.dictionary.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class SerializablePrimitiveType implements Comparable<SerializablePrimitiveType> {

	/**
	 * Tamaño de un byte expresado en bits
	 */
	protected static final int BYTE_SIZE = 8;
	
	public SerializablePrimitiveType() {
		super();
	}

	/**
	 * Lee una tantos bytes como la cantidad de bytes necesarios para serializarse 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	protected byte[] readBytes(InputStream in) throws IOException{
		return readBytes(in,this.getSerializationSize());
	}

	/**
	 * Lee una cantidad específica de bytes de un inputStream
	 * @param in
	 * @param count
	 * @return
	 * @throws IOException
	 */
	protected byte[] readBytes(InputStream in,int lenght) throws IOException{
		byte[] data=new byte[lenght];
		in.read(data );
		return data;
	}

	/**
	 * Delega al metodo actuallyDeserialize
	 * @param in
	 * @return cantidad de bytes leidos desde in
	 * @throws IOException
	 */
	public long deserialize(InputStream in) throws IOException {
		this.actuallyDeserialize(in);
		return (long)this.getSerializationSize();
	}

	/**
	 * Delega al metodo actuallySerialize
	 * @param out
	 * @return cantidad de bytes escritos en out
	 * @throws IOException
	 */
	public long serialize(OutputStream out) throws IOException {
		this.actuallySerialize( out);
		return (long)this.getSerializationSize();
	}

	/**
	 * @return la cantidad de bytes que ocupa la serializacion de su valor
	 */
	public abstract int getSerializationSize();

	/**
	 * Hidrata el objeto 
	 * @param out
	 * @throws IOException
	 */
	public abstract void actuallyDeserialize(InputStream out) throws IOException ;

	/**
	 * Persiste el objeto como bytes
	 * @param in
	 * @throws IOException
	 */
	public abstract void actuallySerialize(OutputStream in) throws IOException ;
	
	@Override
	public int compareTo(SerializablePrimitiveType o) {
		if (this.getClass()== o.getClass()){ 	
			return compareToSameClass(o);
		}else{
			return this.getClass().toString().compareTo(o.getClass().toString());
		}
	}

	protected abstract int compareToSameClass(SerializablePrimitiveType o);
}