package speakit.bsharptree;

import java.io.IOException;

import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;

public class BSharpTreeIndexNode<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> extends BSharpTreeNode<RECTYPE, KEYTYPE> {

	@Override
	public boolean contains(KEYTYPE key) throws IOException, RecordSerializationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RECTYPE getRecord(KEYTYPE key) throws IOException, RecordSerializationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertRecord(RECTYPE record) throws IOException, RecordSerializationException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInOverflow() {
		// TODO Auto-generated method stub
		return false;
	}
}