package speakit.io.bsharptree;

import speakit.io.record.CompositeField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.StringField;

public class BSharpTreeIndexNodeElement extends CompositeField implements BSharpTreeNodeElement {

	private Field key;
	private IntegerField rightChild = new IntegerField();

	@Override
	protected Field[] getFields() {
		return new Field[] { this.key, this.rightChild };
	}

	public Field getKey() {
		return this.key;
	}
	
	public void setKey(Field key) {
		this.key = key;
	}
	
	public int getRightChildNodeNumber() {
		return this.rightChild.getInteger();
	}
	
	public void setRightChild(int rightChild) {
		this.rightChild.setInteger(rightChild);
	}
	
}
