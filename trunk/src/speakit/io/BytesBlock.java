package speakit.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import speakit.dictionary.serialization.BooleanField;
import speakit.dictionary.serialization.ByteArrayField;

public class BytesBlock {
 
	private boolean	removed;
	private byte[]	content;
	private int	blockNumber;

	public BytesBlock(int	blockNumber ) {
		this.blockNumber = blockNumber; 
	}

	public void setIsRemoved(boolean removed) {
		this.removed = removed;
	}

	public void setBytes(byte[] content) {
		if (content==null){
			this.content = new byte[]{};
		}else{
			this.content = content;
		}
		  
	}
	
	public byte[] getBytes() {
		return this.content;
	}

	public boolean getIsRemoved() {
		return removed;
	}

	public void deserialize(byte[] blockSerialization) throws IOException {		
		ByteArrayInputStream in = new ByteArrayInputStream(blockSerialization);
		
		BooleanField isRemovedField = new BooleanField();
		ByteArrayField contentField = new ByteArrayField();
		
		isRemovedField.deserialize(in);
		contentField.deserialize(in);
		
		this.removed = isRemovedField.getBoolean();
		this.content = contentField.getBytes();		
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		BooleanField isRemovedField = new BooleanField(this.removed);		
		ByteArrayField contentField = new ByteArrayField(this.content);

		isRemovedField.serialize(out);
		contentField.serialize(out);
		
		return out.toByteArray();	
	}

	public int getBlockNumber() {
		return this.blockNumber;
	}
	
	public void prepareAsNew(){ 
		this.setIsRemoved(false);
		this.setBytes(null);
	}
}
