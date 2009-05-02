package speakit.io.bsharptree;

import java.io.IOException;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.RecordFile;

public class BSharpTree<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> implements RecordFile<RECTYPE, KEYTYPE> {
	private BSharpTreeNode<RECTYPE, KEYTYPE> root;

	@Override
	public boolean contains(KEYTYPE key) throws IOException, RecordSerializationException {
		return this.root.contains(key);
	}

	@Override
	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException {
		return this.root.getRecord(key);
	}

	@Override
	public void insertRecord(RECTYPE record) throws IOException, RecordSerializationException {
		this.root.insertRecord(record);
		if (this.root.isInOverflow()) {

		}
	}

}
