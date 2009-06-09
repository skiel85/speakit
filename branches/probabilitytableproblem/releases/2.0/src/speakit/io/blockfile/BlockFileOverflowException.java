package speakit.io.blockfile;

public class BlockFileOverflowException extends BlockFileException {

	private static final long	serialVersionUID	= 1L;
	private int					overflowLenght		= 0;
	private final int			blockSize;
	private final int			actualDataLength;

	public BlockFileOverflowException(int blockSize, int actualDataLength) {
		this("",blockSize, actualDataLength);
	}

	public BlockFileOverflowException(String message, int blockSize, int actualDataLength) {
		super(message + " Se esperaban como máximo: " + blockSize + " pero se recibieron: " + actualDataLength + ". Overflow: " + (actualDataLength - blockSize));
		this.blockSize = blockSize;
		this.actualDataLength = actualDataLength;
		overflowLenght = actualDataLength - blockSize;
	}

	public int getOverflowLenght() {
		return overflowLenght;
	}
}
