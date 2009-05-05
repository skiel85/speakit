package speakit.io.bsharptree;

import java.util.ArrayList;
import java.util.List;

import speakit.io.record.ArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;

public class BSharpTreeLeafNodeRecord extends Record<IntegerField> {
	private final class ArrayFieldExtension extends ArrayField<BSharpTreeLeafNodeElement> {
		RecordFactory recordFactory;
		public ArrayFieldExtension(RecordFactory recordFactory) {
			this.recordFactory = recordFactory; 
		}
		
		@Override
		protected BSharpTreeLeafNodeElement createField() {
			return new BSharpTreeLeafNodeElement(this.recordFactory.createRecord());
		}
	}

	private IntegerField nodeNumber = new IntegerField();
	private ArrayField<BSharpTreeLeafNodeElement> elements ;
	private IntegerField nextSecuenceNodeNumber = new IntegerField();

	public BSharpTreeLeafNodeRecord(RecordFactory recordFactory){
		if(recordFactory==null){
			throw new IllegalArgumentException("La fabrica de registros es nula. Se debe suministrar una fabrica, o alguna clase que implemente RecordFactory.");
		}
		elements= new ArrayFieldExtension(recordFactory);
	}
	
	public int getNodeNumber() {
		return this.nodeNumber.getInteger();
	}
	
	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber.setInteger(nodeNumber);
	}
	
	@Override
	protected Field[] getFields() {
		return new Field[] { this.elements, this.nextSecuenceNodeNumber };
	}

	@Override
	public IntegerField getKey() {
		return this.nodeNumber;
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
	
	public void insertElement(BSharpTreeNodeElement element) {
		this.elements.addItem((BSharpTreeLeafNodeElement) element);
		this.elements.sort();
	}
}
