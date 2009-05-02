package speakit.bsharptree;

import java.io.IOException;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

public abstract class BSharpTreeNode<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> {
	public abstract boolean contains(KEYTYPE key) throws IOException, RecordSerializationException;

	public abstract RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException;

	public abstract void insertRecord(RECTYPE record) throws IOException, RecordSerializationException;

	public abstract boolean isInOverflow();
}
