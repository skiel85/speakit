package speakit.io.bsharptree;

import speakit.io.record.CompositeField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;

public class BSharpTreeIndexNodeElement extends CompositeField implements BSharpTreeNodeElement {

	private Field key;
	private IntegerField rightChild = new IntegerField();

	public BSharpTreeIndexNodeElement(Field key, int rightChild) {
		this.key = key;
		this.rightChild.setInteger(rightChild);
	}

	public BSharpTreeIndexNodeElement() {
		// Dejado intencionalmente en blanco.
	}

	@Override
	protected Field[] getFields() {
		return new Field[] { this.key, this.rightChild };
	}

	@Override
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

	@Override
	protected int compareToSameClass(Field o) {
		return this.key.compareTo(((BSharpTreeIndexNodeElement)o).key);
	}
	
	@Override
	protected String getStringRepresentation() {
		return "(B#IE|key:"+ this.key.toString()+",r child:"+this.rightChild+")";
	}
	
}
