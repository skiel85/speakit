package speakit.io;

public class BlockFileOverflowException extends BlockFileException {

	private static final long serialVersionUID = 1L;
	private int overflowLenght = 0;
	private final int blockSize;
	private final int actualDataLength;

	public BlockFileOverflowException(int blockSize, int actualDataLength) {
		super("");
		this.blockSize = blockSize;
		this.actualDataLength = actualDataLength;
		overflowLenght = actualDataLength - blockSize;
	}

	public int getOverflowLenght() {
		return overflowLenght;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + "Se esperaban como máximo: " + blockSize + " pero se recibieron: " + actualDataLength + ". Bytes en overflow: " + this.overflowLenght;
	}
}
