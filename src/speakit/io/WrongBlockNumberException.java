package speakit.io;

public class WrongBlockNumberException extends BlocksFileException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private final int	blockNumber;
	
	public WrongBlockNumberException(int blockNumber){
		super();
		this.blockNumber = blockNumber;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + "Bloque " + this.blockNumber + " no existe.";
	}

}
