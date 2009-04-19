package speakit.io;

public class BlocksFileOverflowException extends Exception {

	private static final long	serialVersionUID	= 1L;

	public BlocksFileOverflowException( int blockSize, int actualDataLength) {
		super("Se esperaban como m�ximo: " + blockSize + " pero se recibieron: " + actualDataLength );
	}
}
