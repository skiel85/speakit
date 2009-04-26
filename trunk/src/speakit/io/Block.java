package speakit.io;

import speakit.dictionary.serialization.ByteArrayField;
import speakit.dictionary.serialization.IntegerField;

public class Block extends Record<IntegerField> {
	private IntegerField blockNumber = new IntegerField();
	private ByteArrayField content = new ByteArrayField();

	public Block(int blockNumber) {
		this.setKey(this.blockNumber);
		this.addField(this.content);
		this.blockNumber.setInteger(blockNumber);
	}

	public void setContent(byte[] content) {
		this.content.setBytes(content);
	}

	public byte[] getContent() {
		return content.getBytes();
	}

	public int getBlockNumber() {
		return this.blockNumber.getInteger();
	}

	public void clear() {
		this.content.setBytes(new byte[] {});
	}

}
