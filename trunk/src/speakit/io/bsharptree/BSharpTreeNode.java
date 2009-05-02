package speakit.io.bsharptree;

import java.io.IOException;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

public abstract class BSharpTreeNode<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> {
	public abstract boolean contains(KEYTYPE key) throws IOException, RecordSerializationException;

	public abstract RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException;

	public abstract void insertRecord(RECTYPE record) throws IOException, RecordSerializationException;

	public abstract boolean isInOverflow() throws RecordSerializationException, IOException;
	
	public abstract int getLevel();
	
	public abstract void balance(BSharpTreeNode<RECTYPE, KEYTYPE>[] nodes);
}
