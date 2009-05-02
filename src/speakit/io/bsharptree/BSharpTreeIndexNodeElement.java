package speakit.io.bsharptree;

import speakit.io.record.CompositeField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.StringField;

public class BSharpTreeIndexNodeElement extends CompositeField {

	private StringField key = new StringField();
	private IntegerField rightChild = new IntegerField();

	@Override
	protected Field[] getFields() {
		return new Field[] { this.key, this.rightChild };
	}

}
