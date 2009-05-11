package speakit.io.bsharptree;

import java.io.InputStream;
import java.io.OutputStream;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;

@SuppressWarnings("unchecked")
public class TreeLeafNodeRecord extends TreeNodeRecord {
	public class ArrayFieldExtension extends ArrayField<TreeLeafNodeElement> {
		RecordFactory recordFactory;

		public ArrayFieldExtension(RecordFactory recordFactory) {
			this.recordFactory = recordFactory;
		}

		@Override
		protected TreeLeafNodeElement createField() {
			return new TreeLeafNodeElement(this.recordFactory.createRecord());
		}
	}

	public class FrontCodedElementArrayField extends ArrayField<TreeLeafNodeElement> {
		RecordFactory recordFactory;

		public FrontCodedElementArrayField(RecordFactory recordFactory) {
			this.recordFactory = recordFactory;
		}

		@Override
		protected TreeLeafNodeElement createField() {
			// BSharpTreeLeafNodeRecord.this;
			return new TreeLeafNodeElement(this.recordFactory.createRecord());
		}
	}

	private ArrayField<TreeLeafNodeElement> elements;
	private ArrayField<TreeLeafNodeElement> frontCodedElements;
	private IntegerField nextSecuenceNodeNumber = new IntegerField();

	private Tree tree;

	public TreeLeafNodeRecord(int nodeNumber, Tree tree) {
		super(nodeNumber);
		
		if (tree == null) {
			throw new IllegalArgumentException("El argumento árbol es nulo.");
		}
		this.tree = tree;
		elements = new ArrayFieldExtension(tree);
		frontCodedElements = new FrontCodedElementArrayField(tree.getEncoder());
	}

	@Override
	public void addElement(TreeNodeElement element) {
		this.elements.addItem((TreeLeafNodeElement) element);
		sort();
	}

	@Override
	public long deserialize(InputStream stream) throws RecordSerializationException {
		this.frontCodedElements.clear();
		long deserializationResult = super.deserialize(stream);
		this.tree.getEncoder().clear();
		this.elements.clear();
		for (TreeLeafNodeElement element : this.frontCodedElements) {
			Record decodedRecord = this.tree.getEncoder().decode(element.getRecord());
			TreeLeafNodeElement decodedElement = new TreeLeafNodeElement(decodedRecord);
			this.elements.addItem(decodedElement);
		}
		sort();
		// System.out.println("Deserialización: " + this.toString());
		return deserializationResult;
	}
	
	private void sort() {
		this.elements.sort(); 
	}

	public ArrayField<TreeLeafNodeElement> getElements() {
		return this.elements;
	}

	@Override
	protected Field[] getFields() {
		return new Field[] { this.frontCodedElements, this.nextSecuenceNodeNumber };
	}

	@Override
	public int getLevel() {
		return 0;
	}

	public int getNextSecuenceNodeNumber() {
		return this.nextSecuenceNodeNumber.getInteger();
	}

	@Override
	protected String getStringRepresentation() {
		return "LN " + this.getNodeNumber() + ",next:" + this.nextSecuenceNodeNumber.toString() + "," + this.elements.toString() + ",FCElements:" + this.frontCodedElements.toString();

	}

	@Override
	public long serialize(OutputStream stream) throws RecordSerializationException {
		this.frontCodedElements.clear();
		this.tree.getEncoder().clear();
		for (TreeLeafNodeElement element : this.elements) {
			Record encodedRecord = this.tree.getEncoder().encode(element.getRecord());
			TreeLeafNodeElement encodedElement = new TreeLeafNodeElement(encodedRecord);
			this.frontCodedElements.addItem(encodedElement);
		}
		long serializationResult = super.serialize(stream);
		// System.out.println("Serialización: " + this.toString());
		return serializationResult;
	}

	public void setNextSecuenceNodeNumber(int nextSecuenceNodeNumber) {
		this.nextSecuenceNodeNumber.setInteger(nextSecuenceNodeNumber);
	}
}
