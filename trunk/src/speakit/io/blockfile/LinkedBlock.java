package speakit.io.blockfile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import speakit.io.record.IntegerField;
import speakit.io.record.RecordSerializationException;

public class LinkedBlock extends RemovableBlock {

	private IntegerField nextBlockNumber = new IntegerField(-1);

	public LinkedBlock(int blockNumber) {
		super(blockNumber);
		this.addField(nextBlockNumber);
		this.clear();
	}

	public void setNextBlockNumber(int nextBlockNumber) {
		this.nextBlockNumber.setInteger(nextBlockNumber);
	}

	public int getNextBlockNumber() {
		return nextBlockNumber.getInteger();
	}

	@Override
	public void clear() {
		super.clear();
		this.nextBlockNumber.setInteger(-1);
	}

	/**
	 * copia todo el contenido desde otro bloque
	 * 
	 * @param other
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public void copyFrom(LinkedBlock other) throws IOException, RecordSerializationException {
		deserialize(other.serialize());
	}

	/**
	 * elimina de content los bytes que indique en overflowLength y copia los
	 * bytes eliminados en un bloque pasado como parametro
	 * 
	 * @param overflowLength
	 * @param excedentBlock
	 * @return
	 */
	public LinkedBlock truncateOverflow(int overflowLength, LinkedBlock excedentBlock) {
		this.nextBlockNumber.setInteger(excedentBlock.getBlockNumber());
		byte[] originalContent = this.getContent();
		int originalLength = originalContent.length;
		int part1Lenght = originalLength - overflowLength;

		this.setContent(Arrays.copyOf(originalContent, part1Lenght));
		excedentBlock.setContent(Arrays.copyOfRange(originalContent, part1Lenght, originalLength));
		return excedentBlock;
	}

	/**
	 * Concatena el contenido del bloque siguiente al content
	 * 
	 * @param nextBlock
	 * @throws IOException
	 */
	public void appendNextBlockContent(LinkedBlock nextBlock) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(this.getContent());
		out.write(nextBlock.getContent());
		this.setContent(out.toByteArray());
	}

}
