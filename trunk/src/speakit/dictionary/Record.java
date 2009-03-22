package speakit.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Record extends Comparable<Record> {
	public long serialize(OutputStream stream) throws IOException;
	public long deserialize(InputStream stream) throws IOException;
}
