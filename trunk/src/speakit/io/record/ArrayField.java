package speakit.io.record;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayField<FIELDTYPE extends Field> extends CompositeField implements Iterable<FIELDTYPE> {
	private IntegerField size = new IntegerField();

	public ArrayField() {
		this.addField(this.size);
	}

	private void incrementSize() {
		this.size.setInteger(this.size.getInteger() + 1);
	}

	private void decrementSize() {
		this.size.setInteger(this.size.getInteger() - 1);
	}

	public void addItem(FIELDTYPE item) {
		this.incrementSize();
		this.addField(item);
	}

	public void removeItem(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		this.decrementSize();
		this.removeField(index + 1);
	}

	public void removeItem(FIELDTYPE field) {
		this.decrementSize();
		this.removeField(field);
	}

	@SuppressWarnings("unchecked")
	public FIELDTYPE getItem(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return (FIELDTYPE) this.getField(index + 1);
	}

	public int getSize() {
		return this.size.getInteger();
	}

	@Override
	public Iterator<FIELDTYPE> iterator() {
		return new ArrayFieldIterator<FIELDTYPE>(this);
	}
	
	public void setArray(List<FIELDTYPE> array){
		for (FIELDTYPE item : array) {
			this.addItem(item);
		} 
	}
	
	@SuppressWarnings("unchecked")
	public List<FIELDTYPE> getArray(){
		List<FIELDTYPE> result=new ArrayList<FIELDTYPE>();
		for (int i = 0; i < this.getFieldCount(); i++) {
			result.add((FIELDTYPE)this.getField(i));
		}
		return result; 
	}
}
