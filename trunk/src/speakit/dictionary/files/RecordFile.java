package speakit.dictionary.files;

public interface RecordFile<RECTYPE extends Record> {
	public void insertRecord(RECTYPE record);
}
