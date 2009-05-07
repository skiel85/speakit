package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class ArrayField<FIELDTYPE extends Field> extends Field implements Iterable<FIELDTYPE> {
	private IntegerField size = new IntegerField();
	private ArrayList<FIELDTYPE> values = new ArrayList<FIELDTYPE>();

//	@Override
//	protected Field[] getFields() {
//		return Field.JoinFields(new Field[] { this.size }, this.values.toArray(new Field[this.values.size()]));
//	}

 
	
	private void updateSize(){
		this.size.setInteger(this.values.size());
	}

	public void addItem(FIELDTYPE item) {
		this.values.add(item);
		this.updateSize();
	}

	public void removeItem(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		this.values.remove(index);
		this.updateSize();
	}

	public void removeItem(FIELDTYPE field) {
		this.values.remove(field);
		this.updateSize();
	}

	public FIELDTYPE get(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return (FIELDTYPE) this.values.get(index);
	}

	public int size() {
		return this.values.size();
	}

	@Override
	public Iterator<FIELDTYPE> iterator() {
		return new ArrayFieldIterator<FIELDTYPE>(this);
	}

	public void setArray(List<FIELDTYPE> array) {
		for (FIELDTYPE item : array) {
			this.addItem(item);
		}
	}

	public List<FIELDTYPE> getArray() {
		List<FIELDTYPE> result = new ArrayList<FIELDTYPE>();
		if (this.values.size() > 0) {
			for (int i = 0; i < this.values.size(); i++) {
				result.add((FIELDTYPE) this.values.get(i));
			}
		}
		return result;
	}

	public void clear() {
		this.values.clear();
		this.updateSize();
	}
	
	public void sort() {
		Collections.sort(this.values);
	}

	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		this.size.deserialize(in);
		for (int i = 0; i < size.getInteger(); i++) {
			FIELDTYPE createdField = this.createField();
			deserializeField(in, createdField);
			this.values.add(createdField);
		}
	} 

	private void deserializeField(InputStream in, FIELDTYPE createdField) throws IOException {
		createdField.deserialize(in);
	}

	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		this.updateSize();
		this.size.serialize(out);
		for (int i = 0; i < this.values.size(); i++) {
			serializeField(out, i);
		}
	}

	private long serializeField(OutputStream out, int i) throws IOException {
		return this.values.get(i).serialize(out);
	}

	@Override
	protected int compareToSameClass(Field o) {
		for (Field field : this.values) {
			int comparationResult = field.compareTo(o);
			if (comparationResult != 0)
				return comparationResult;
		}
		return 0;
	}

	@Override
	public int getSerializationSize() throws RecordSerializationException, IOException {
		int accum = 0;
		for (Field field : this.values) {
			accum += field.getSerializationSize();
		}
		return accum;
	}
	
	protected abstract FIELDTYPE createField(); 
	
	@Override
	protected String getStringRepresentation() {
		String result="A[" + this.size.toString() + "]{";
		int i=0;
		for (Field field : this.values) {
			result += (i!=0?",":"") + field.toString();
			i++;
		}
		result+="}";
		return result;		
	}
}
