package speakit.io.record;

import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ArrayFieldIterator<FIELDTYPE extends Field> implements Iterator<FIELDTYPE> {
	private ArrayField<FIELDTYPE> arrayField;
	private int position;

	public ArrayFieldIterator(ArrayField<FIELDTYPE> arrayField) {
		this.arrayField = arrayField;
	}

	@Override
	public boolean hasNext() {
		return (this.position < this.arrayField.size());
	}

	@Override
	public FIELDTYPE next() {
		return this.arrayField.get(this.position++);
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}

}
