package speakit.io.bsharptree;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;

public class BSharpTreeIndexNodeRecord extends Record<IntegerField> {
	private IntegerField nodeNumber = new IntegerField();
	private ArrayField<BSharpTreeIndexNodeElement> element = new ArrayField<BSharpTreeIndexNodeElement>();
	private IntegerField nextSequenceNodeNumber = new IntegerField();

	@Override
	protected Field[] getFields() {
		return new Field[] { this.element, this.nextSequenceNodeNumber };
	}

	@Override
	protected IntegerField getKey() {
		return this.nodeNumber;
	}
}
