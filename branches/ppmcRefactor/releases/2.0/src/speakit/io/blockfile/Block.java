package speakit.io.blockfile;

import speakit.io.record.ByteArrayField;
import speakit.io.record.Field;
import speakit.io.record.IntegerField;
import speakit.io.record.Record;

public class Block extends Record<IntegerField> {
	private IntegerField blockNumber = new IntegerField();
	private ByteArrayField content = new ByteArrayField();

	public Block(int blockNumber) {
		this.blockNumber.setInteger(blockNumber);
	}

	public void setContent(byte[] content) {
		this.content.setBytes(content);
	}

	public byte[] getContent() {
		return content.getBytes();
	}

	public void appendContent(byte[] appendedContent) {
		byte[] content = this.getContent();
		byte[] newContent = new byte[content.length + appendedContent.length];
		for (int i = 0; i < content.length; i++) {
			newContent[i] = content[i];
		}
		for (int i = content.length; i < content.length + appendedContent.length; i++) {
			newContent[i] = appendedContent[i - content.length];
		}
		this.setContent(newContent);
	}

	public int getBlockNumber() {
		return this.blockNumber.getInteger();
	}

	public void clear() {
		this.content.setBytes(new byte[] {});
	}

	@Override
	protected Field[] getFields() {
		return new Field[] {this.content};
	}

	@Override
	public IntegerField getKey() {
		return this.blockNumber;
	}

	
	@Override
	protected String getStringRepresentation() {
		return "Block{blockNumber:"+this.blockNumber.toString()+",content:"+this.content.toString()+"}";
	}
}
