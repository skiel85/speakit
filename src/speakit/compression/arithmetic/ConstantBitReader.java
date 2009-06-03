package speakit.compression.arithmetic;

import java.io.IOException;
import java.io.Reader;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
 

public class ConstantBitReader extends Reader {

	private final boolean	alwaysZero;

	public ConstantBitReader(boolean alwaysZero) { 
		this.alwaysZero = alwaysZero;
	}

	@Override
	public int read() throws IOException {
		if (alwaysZero) {
			return (int) '0';
		} else {
			return (int) '1';
		}
	}

	@Override
	public void close() throws IOException {
		 //nada
	}

	@Override
	public int read(char[] arg0, int arg1, int arg2) throws IOException {
		throw new NotImplementedException();
	}

}
