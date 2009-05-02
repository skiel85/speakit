package speakit.io.bsharptree;

import java.io.IOException;

import speakit.io.blockfile.BasicBlockFile;
import speakit.io.record.Field;
import speakit.io.record.Record;
import speakit.io.record.RecordSerializationException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BSharpTreeLeafNode<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> extends BSharpTreeNode<RECTYPE, KEYTYPE> {

	private BasicBlockFile blockFile;
	private BSharpTreeIndexNodeRecord record;
	
	public BSharpTreeLeafNode(BasicBlockFile blockFile) {
		this.blockFile = blockFile;
	}
	
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
	public boolean isInOverflow() throws RecordSerializationException, IOException {
		return (this.record.serialize().length > this.blockFile.getBlockSize() * 2);
	}
	
	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public void balance(BSharpTreeNode<RECTYPE, KEYTYPE>[] nodes) {
		throw new NotImplementedException();
	}
}