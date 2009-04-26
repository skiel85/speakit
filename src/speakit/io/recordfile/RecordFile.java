package speakit.io.recordfile;

import java.io.IOException;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;


public interface RecordFile<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> {
	public void insertRecord(RECTYPE record) throws IOException, RecordSerializationException;

	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException;

	public boolean contains(KEYTYPE key) throws IOException, RecordSerializationException;
}
