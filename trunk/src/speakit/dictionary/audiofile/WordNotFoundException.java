package speakit.dictionary.audiofile;

@SuppressWarnings("serial")
public class WordNotFoundException extends Exception {

	private final String notFoundWord;

	public WordNotFoundException(String notFoundWord) {
		super("No se encuentra la palabra:" + notFoundWord);
		this.notFoundWord = notFoundWord;

	}

	public WordNotFoundException(String notFoundWord, Throwable arg1) {
		super(notFoundWord, arg1);
		this.notFoundWord = notFoundWord;
	}

	@Override
	public String toString() {
		return "No se encuentra la palabra: " + this.notFoundWord + ".\n" + super.toString();
	}

}
