package speakit.audio;

@SuppressWarnings("serial")
public class AudioManagerException extends Exception {

	public AudioManagerException(String string) {
		super(string);
	}

	public AudioManagerException(String string, Exception ex) {
		super(string, ex);
	}

}
