package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class CompositeField extends Field {
	protected abstract Field[] getFields();
	
	protected int getFieldCount() {
		return this.getFields().length;
	}

	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		for (Field field : this.getFields()) {
			field.deserialize(in);
		}
	}

	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		for (Field field : this.getFields()) {
			field.serialize(out);
		}
	}

	@Override
	public int getSerializationSize() throws RecordSerializationException, IOException {
		int accum = 0;
		for (Field field : this.getFields()) {
			accum += field.getSerializationSize();
		}
		return accum;
	}
	
	@Override
	protected String getStringRepresentation() {
		String result=this.getClass().getSimpleName() + "[" + getFields().length + "](";
		int i=0;
		for (Field field : getFields()) {
			result += (i!=0?",":"") + field.toString();
			i++;
		}
		result+=")";
		return result;		
	}

}