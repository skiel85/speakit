package speakit.dictionary;

import java.io.InputStream;
import java.io.OutputStream;

public interface Record extends Comparable<Record> {
	public void serialize(OutputStream stream);
	public void deserialize(InputStream stream);
}
