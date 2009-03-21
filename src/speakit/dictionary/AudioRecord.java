package speakit.dictionary;

import java.io.InputStream;
import java.io.OutputStream;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class AudioRecord implements Record {

	@Override
	public void deserialize(InputStream stream) {
		throw new NotImplementedException();
	}

	@Override
	public void serialize(OutputStream stream) {
		throw new NotImplementedException();
	}

	@Override
	public int compareTo(Record o) {
		throw new NotImplementedException();
	}

}
