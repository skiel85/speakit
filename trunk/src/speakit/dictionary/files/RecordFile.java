package speakit.dictionary.files;

import java.io.IOException;

public interface RecordFile<RECTYPE extends Record> {
	public void insertRecord(RECTYPE record) throws IOException;
}
