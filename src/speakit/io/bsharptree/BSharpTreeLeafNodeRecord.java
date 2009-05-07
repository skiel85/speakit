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
	private ArrayField<BSharpTreeLeafNodeElement> frontCodedRecords ;
	private IntegerField nextSecuenceNodeNumber = new IntegerField();
	private final RecordEncoder	encoder;
	

	public BSharpTreeLeafNodeRecord(RecordFactory recordFactory,RecordEncoder encoder){
		this.encoder = encoder;
		if(recordFactory==null){
			throw new IllegalArgumentException("La fabrica de registros es nula. Se debe suministrar una fabrica, o alguna clase que implemente RecordFactory.");
		}
		elements= new ArrayFieldExtension(recordFactory);
		frontCodedRecords = new FrontCodedElementArrayField(encoder);		
	}
	
	@Override
	protected Field[] getFields() { 
		return new Field[] { this.frontCodedRecords, this.nextSecuenceNodeNumber };
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
		this.frontCodedRecords.clear();
		this.encoder.clear();
		for (BSharpTreeLeafNodeElement element : this.elements) {
			Record encodedRecord = this.encoder.encode(element.getRecord());
			BSharpTreeLeafNodeElement encodedElement = new BSharpTreeLeafNodeElement(encodedRecord);
			this.frontCodedRecords.addItem(encodedElement);
		} 
		return super.serialize(stream);
	}
	
	@Override
	public long deserialize(InputStream stream) throws RecordSerializationException {
		this.frontCodedRecords.clear();
		long deserializationResult = super.deserialize(stream);
		this.encoder.clear();
		this.elements.clear();
		for (BSharpTreeLeafNodeElement element : this.frontCodedRecords) {
			Record decodedRecord = this.encoder.decode(element.getRecord());
			BSharpTreeLeafNodeElement decodedElement = new BSharpTreeLeafNodeElement(decodedRecord);
			this.elements.addItem(decodedElement);
		} 
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
		return "B#LN{num:"+this.getNodeNumber()+",secuenceNext:"+this.nextSecuenceNodeNumber.toString()+",elements:"+this.elements.toString()+"}";
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
