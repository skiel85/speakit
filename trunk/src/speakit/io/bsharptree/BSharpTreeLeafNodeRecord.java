package speakit.io.bsharptree;

import speakit.io.record.ArrayField;
import speakit.io.record.ByteArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;

public class BSharpTreeLeafNodeRecord extends Record<IntegerField> {
	private IntegerField nodeNumber = new IntegerField();
	private ArrayField<ByteArrayField> records = new ArrayField<ByteArrayField>();
	private IntegerField nextSecuenceNodeNumber = new IntegerField();

	@Override
	protected Field[] getFields() {
		return new Field[] { this.records, this.nextSecuenceNodeNumber };
	}

	@Override
	protected IntegerField getKey() {
		return this.nodeNumber;
	}
}
