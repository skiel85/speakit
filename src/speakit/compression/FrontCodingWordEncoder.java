package speakit.compression;

/**
 * Clase para codificar una palabra
 * 
 */

public class FrontCodingWordEncoder {

	private String	lastWord	= null;

	/**
	 * Metodo que comprime una palabra.
	 * 
	 * @param string
	 * @return FrontCodedWord
	 */
	public FrontCodedWord encode(String string) {
		FrontCodedWord encodedWord;
		if (lastWord == null) {
			lastWord = string;
			return new FrontCodedWord((short) 0, string);
		} else {
			short frontMatchingCharacters = calculateFrontMatchingCharacters(lastWord, string);
			lastWord = string;
			// String endingCharacters = "";
			// char[] charsOfString = new char[string.length()];
			// charsOfString = string.toCharArray();
			// short position = frontMatchingCharacters;
			// while(position < (short)string.length()){
			// endingCharacters = endingCharacters + charsOfString[position];
			// position++;
			// }
			// encodedWord = new
			// FrontCodedWord(frontMatchingCharacters,endingCharacters);
			String ending = "";
			if(frontMatchingCharacters<string.length()){
				ending=string.substring(frontMatchingCharacters);	
			}else{
				//Caso de que matchee toda la palabra dentro de la anterior
				ending="";
			}
			return new FrontCodedWord(frontMatchingCharacters, ending);
			// for (short i = frontMatchingCharacters-1; i <
			// frontMatchingCharacters; i++) {
			// if(string1.charAt(i)!=string2.charAt(i)){
			// return i;
			// }
			// }
		}
		// throw new IllegalStateException("No se pudo frontcodear");
	}

	/**
	 * Compara la cantidad de caracteres iniciales que coinciden
	 * 
	 * @param string1
	 * @param string2
	 * 
	 */
	private short calculateFrontMatchingCharacters(String string1, String string2) {
		short shorterArrayLenght = min((short) string1.length(), (short) string2.length());
		for (short i = 0; i < shorterArrayLenght; i++) {
			if (string1.charAt(i) != string2.charAt(i)) {
				return i;
			}
		}
		return shorterArrayLenght;
	}

	private short min(short x, short y) {
		if (x < y) {
			return x;
		} else {
			return y;
		}
	}

}
