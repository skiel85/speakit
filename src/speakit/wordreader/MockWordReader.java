package speakit.wordreader;

/**
 * Clase Dummy q implementa WordREader
 * @author 
 *
 */
public class MockWordReader implements WordReader {
	int count;
	public MockWordReader() {
		count = 4;
	}
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		count--;
		return count != 0;
	}

	@Override
	public String next() {
		// TODO Auto-generated method stub
		return "unaPalabra";
	}

}
