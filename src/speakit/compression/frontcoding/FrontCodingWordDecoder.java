package speakit.compression.frontcoding;
/**
 * Clase para decodificar una palabra
 * 
 */
public class FrontCodingWordDecoder {
	private String lastWordDecoded = null;
	
	/**
	 * Metodo que descomprime una palabra.
	 * 
	 * @param codedWord
	 * @return String
	 */
	public String decode(FrontCodedWord codedWord) {

		String decodedWord = "";
		if(lastWordDecoded == null){
			decodedWord = codedWord.getEndingCharacters();
			lastWordDecoded = decodedWord;
		}else{
			String endingCharacters = codedWord.getEndingCharacters();
			short matchingCharacters = codedWord.getMatchingCharacters();
			char[] charsOfString = new char[lastWordDecoded.length()];
			charsOfString = lastWordDecoded.toCharArray();
			for(short i = 0; i < matchingCharacters; i++){
				decodedWord = decodedWord + charsOfString[i];
			}
			decodedWord = decodedWord + endingCharacters;
			lastWordDecoded = decodedWord;
		}
		
		return decodedWord;
	}
}
