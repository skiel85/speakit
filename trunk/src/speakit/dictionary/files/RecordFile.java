package speakit.dictionary.files;

import java.io.IOException;

import speakit.dictionary.serialization.Field;

public interface RecordFile<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> {
	public void insertRecord(RECTYPE record) throws IOException, RecordSerializationException;
	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException;
	public boolean contains(KEYTYPE key) throws IOException, RecordSerializationException;
}
