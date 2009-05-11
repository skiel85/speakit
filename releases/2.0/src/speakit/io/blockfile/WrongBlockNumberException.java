package speakit.io.blockfile;

public class WrongBlockNumberException extends BlockFileException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int blockNumber;

	public WrongBlockNumberException(int blockNumber) {
		super();
		this.blockNumber = blockNumber;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + "Bloque " + this.blockNumber + " no existe.";
	}

}
