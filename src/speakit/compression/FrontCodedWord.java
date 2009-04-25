package speakit.compression;

public class FrontCodedWord {

	private final short matchingCharacters;
	private final String endingCharacters;

	public FrontCodedWord(short matchingCharacters, String endingCharacters) {
		this.matchingCharacters = matchingCharacters;
		this.endingCharacters = endingCharacters;
	}

	public String getEndingCharacters() {
		return endingCharacters;
	}

	public short getMatchingCharacters() {
		return matchingCharacters;
	}

}
