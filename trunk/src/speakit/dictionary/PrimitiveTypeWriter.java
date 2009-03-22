package speakit.dictionary;

import java.io.IOException;
import java.io.OutputStream;


public class PrimitiveTypeWriter {

	OutputStream out=null;
	
	public PrimitiveTypeWriter(OutputStream out){
		this.out=out;
	}
	
	public void writeInt(int number) throws IOException{
		out.write(ByteArrayConverter.toByta(number));
	}
	
	public void writeLong(long number) throws IOException{
		out.write(ByteArrayConverter.toByta(number));
	}
	
	public void writeDouble(double number) throws IOException{
		out.write(ByteArrayConverter.toByta(number));
	}
	
	public void writeFloat(float number) throws IOException{
		out.write(ByteArrayConverter.toByta(number));
	}
	
	public void writeByte(byte number) throws IOException{
		out.write(ByteArrayConverter.toByta(number));
	}
	
//	public void writeString(String str) throws IOException{
//		out.write(Converter.toByta(str));
//	}

}
