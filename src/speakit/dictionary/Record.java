package speakit.dictionary;

import java.io.InputStream;
import java.io.OutputStream;

public interface Record extends Comparable<Record> {
	public long serialize(OutputStream stream) throws RecordSerializationException;
	public long deserialize(InputStream stream) throws RecordSerializationException;
}
