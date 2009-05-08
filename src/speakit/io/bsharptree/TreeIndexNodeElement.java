package speakit.io.bsharptree;

import speakit.io.record.CompositeField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;

public class TreeIndexNodeElement extends CompositeField implements TreeNodeElement {

	private Field key;
	private IntegerField rightChild = new IntegerField();

	public TreeIndexNodeElement() {
		// Dejado intencionalmente en blanco.
	}

	public TreeIndexNodeElement(Field key, int rightChild) {
		this.key = key;
		this.rightChild.setInteger(rightChild);
	}

	@Override
	protected int compareToSameClass(Field o) {
		return this.key.compareTo(((TreeIndexNodeElement) o).key);
	}

	@Override
	protected Field[] getFields() {
		return new Field[] { this.key, this.rightChild };
	}

	@Override
	public Field getKey() {
		return this.key;
	}

	public int getRightChildNodeNumber() {
		return this.rightChild.getInteger();
	}

	@Override
	protected String getStringRepresentation() {
		return "(B#IE|key:" + this.key.toString() + ",r child:" + this.rightChild + ")";
	}

	/**
	 * Devuelve si el nodo es apuntado por este elemento
	 * 
	 * @param aNode
	 * @return
	 */
	public boolean pointsTo(TreeNode aNode) {
		return this.getRightChildNodeNumber() == aNode.getNodeNumber();
	}

	public void setKey(Field key) {
		this.key = key;
	}

	public void setRightChild(int rightChild) {
		this.rightChild.setInteger(rightChild);
	}

}
