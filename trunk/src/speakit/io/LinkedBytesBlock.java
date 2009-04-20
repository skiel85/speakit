/**
 * 
 */
package speakit.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import speakit.dictionary.serialization.ByteArrayField;
import speakit.dictionary.serialization.IntegerField;

/**
 * @author Nahuel
 * 
 */
public class LinkedBytesBlock extends BytesBlock {

	private int		nextBlockNumber;
	private byte[]	content;

	public LinkedBytesBlock(int blockNumber) {
		super(blockNumber);
		nextBlockNumber = -1;
		content = new byte[]{};
	}

	public void setNextBlockNumber(int nextBlockNumber) {
		this.nextBlockNumber = nextBlockNumber;
	}

	public int getNextBlockNumber() {
		return nextBlockNumber;
	}

	public void prepareAsNew() {
		super.prepareAsNew();
		this.nextBlockNumber = -1;
	}

	@Override
	public byte[] getBytes() {
		return this.content;
	}

	@Override
	public void setBytes(byte[] content) {
		this.content = content;
		if (this.content == null) {
			this.content = new byte[]{};
		}
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		IntegerField nextBlockNumberField = new IntegerField(this.nextBlockNumber);
		nextBlockNumberField.serialize(out);

		ByteArrayField contentField = new ByteArrayField(this.content);
		contentField.serialize(out);

		super.setBytes(out.toByteArray());

		return super.serialize();
	}

	@Override
	public void deserialize(byte[] blockSerialization) throws IOException {
		super.deserialize(blockSerialization);

		ByteArrayInputStream in = new ByteArrayInputStream(super.getBytes());

		IntegerField nextBlockNumberField = new IntegerField();
		nextBlockNumberField.deserialize(in);
		this.nextBlockNumber = nextBlockNumberField.getInteger();

		ByteArrayField contentField = new ByteArrayField();
		contentField.deserialize(in);
		this.content = contentField.getBytes();
	}

	/**
	 * copia todo el contenido desde otro bloque
	 * @param other
	 * @throws IOException
	 */
	public void copyFrom(LinkedBytesBlock other) throws IOException{
		deserialize(other.serialize());
	}

	/**
	 * elimina de content los bytes que indique en overflowLength y copia los bytes eliminados en un bloque pasado como parametro
	 * @param overflowLength
	 * @param writeOn
	 * @return
	 */
	public LinkedBytesBlock truncateOverflow(int overflowLength,LinkedBytesBlock writeOn) {
		this.nextBlockNumber=writeOn.getBlockNumber();
		writeOn.content = Arrays.copyOfRange(this.content, this.content.length - overflowLength, this.content.length);
		this.content = Arrays.copyOf(this.content, this.content.length - overflowLength);
		return writeOn;
	}

	/**
	 * Concatena al content el contenido del bloque siguiente
	 * @param nextBlock
	 * @throws IOException
	 */
	public void appendNextBlockContent(LinkedBytesBlock nextBlock) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(this.content);
		out.write(nextBlock.content);
		this.content=out.toByteArray();
	}
}

