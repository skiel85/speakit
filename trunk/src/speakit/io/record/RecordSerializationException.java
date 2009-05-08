package speakit.io.record;

import java.io.IOException;

public class RecordSerializationException extends IOException {

	public RecordSerializationException(String string) {
		super(string);
	}
	
	public RecordSerializationException( ) { 
	}

	public RecordSerializationException(IOException e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
