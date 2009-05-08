package speakit.io.bsharptree;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;

@SuppressWarnings("unchecked")
public class BSharpTreeLeafNodeRecord extends BSharpTreeNodeRecord {
	public class ArrayFieldExtension extends ArrayField<BSharpTreeLeafNodeElement> {
		RecordFactory recordFactory;
		public ArrayFieldExtension(RecordFactory recordFactory) {
			this.recordFactory = recordFactory; 
		}
		
		@Override
		protected BSharpTreeLeafNodeElement createField() {
			return new BSharpTreeLeafNodeElement(this.recordFactory.createRecord());
		}
	}
	
	public class FrontCodedElementArrayField extends ArrayField<BSharpTreeLeafNodeElement> {
		RecordFactory recordFactory;
		public FrontCodedElementArrayField(RecordFactory recordFactory) {
			this.recordFactory = recordFactory; 
		}
		
		@Override
		protected BSharpTreeLeafNodeElement createField() {
//			BSharpTreeLeafNodeRecord.this;
			return new BSharpTreeLeafNodeElement(this.recordFactory.createRecord());
		}
	}

	private ArrayField<BSharpTreeLeafNodeElement> elements ;
	private ArrayField<BSharpTreeLeafNodeElement> frontCodedElements ;
	private IntegerField nextSecuenceNodeNumber = new IntegerField();
	
	private BSharpTree tree;

	public BSharpTreeLeafNodeRecord(BSharpTree tree) {
		this(tree, 1);
	}
	
	public BSharpTreeLeafNodeRecord(BSharpTree tree, int size){
		if(tree==null){
			throw new IllegalArgumentException("El argumento árbol es nulo.");
		}
		this.tree = tree;
		elements= new ArrayFieldExtension(tree);
		frontCodedElements = new FrontCodedElementArrayField(tree.getEncoder());		
	}
	
	@Override
	protected Field[] getFields() { 
		return new Field[] { this.frontCodedElements, this.nextSecuenceNodeNumber };
	}

	public List<BSharpTreeNodeElement> getElements() {
		ArrayList<BSharpTreeNodeElement> result = new ArrayList<BSharpTreeNodeElement>(this.elements.getArray());
		return result;
	}

	public BSharpTreeNodeElement extractLastElement() {
		BSharpTreeNodeElement element = this.elements.get(this.elements.size() - 1);
		this.elements.removeItem(this.elements.size() - 1);
		return element;
	}
	
	@Override
	public long serialize(OutputStream stream) throws RecordSerializationException {
		this.frontCodedElements.clear();
		this.tree.getEncoder().clear();
		for (BSharpTreeLeafNodeElement element : this.elements) {
			Record encodedRecord = this.tree.getEncoder().encode(element.getRecord());
			BSharpTreeLeafNodeElement encodedElement = new BSharpTreeLeafNodeElement(encodedRecord);
			this.frontCodedElements.addItem(encodedElement);
		}
		long serializationResult = super.serialize(stream);		
//		System.out.println("Serialización: " + this.toString());
		return serializationResult;
	}
	
	@Override
	public long deserialize(InputStream stream) throws RecordSerializationException {
		this.frontCodedElements.clear();
		long deserializationResult = super.deserialize(stream);
		this.tree.getEncoder().clear();
		this.elements.clear();
		for (BSharpTreeLeafNodeElement element : this.frontCodedElements) {
			Record decodedRecord = this.tree.getEncoder().decode(element.getRecord());
			BSharpTreeLeafNodeElement decodedElement = new BSharpTreeLeafNodeElement(decodedRecord);
			this.elements.addItem(decodedElement);
		}
//		System.out.println("Deserialización: " + this.toString());
		return deserializationResult;
	} 
	
	public BSharpTreeNodeElement extractFirstElement() {
		BSharpTreeNodeElement element = this.elements.get(0);
		this.elements.removeItem(0);
		return element;
	}
	
	public void insertElement(BSharpTreeNodeElement element) {
		this.elements.addItem((BSharpTreeLeafNodeElement) element);
		this.elements.sort();
	}
	
	@Override
	protected String getStringRepresentation() {
		return "B#LN{num:"+this.getNodeNumber()+",frontCodedElements:"+this.frontCodedElements.toString()+",secuenceNext:"+this.nextSecuenceNodeNumber.toString()+",elements:"+this.elements.toString()+"}";
	}

	public List<BSharpTreeNodeElement> extractAllElements() {
		ArrayList<BSharpTreeNodeElement> elementList = new ArrayList<BSharpTreeNodeElement>();
		for (BSharpTreeNodeElement element : this.elements) {
			elementList.add(element);
		}
		this.elements.clear();
		return elementList;
	}
}
