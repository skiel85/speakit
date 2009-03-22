package speakit.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class SerializablePrimitiveType {

	public SerializablePrimitiveType() {
		super();
	}


	protected byte[] readBytes(InputStream in) throws IOException{
		return readBytes(in,this.getSerializationSize());
	}
	
	protected byte[] readBytes(InputStream in,int count) throws IOException{
		byte[] data=new byte[count];
		in.read(data );
		return data;
	}
	

	public long deserialize(InputStream in) throws IOException {
		this.actuallyDeserialize(in);
		return this.getSerializationSize();
	}
	
	public long serialize(OutputStream out) throws IOException {
		this.actuallySerialize( out);
		return this.getSerializationSize();
	}
	

	public abstract int getSerializationSize();
	
	public abstract void actuallyDeserialize(InputStream out) throws IOException ;

	public abstract void actuallySerialize(OutputStream in) throws IOException ;
}