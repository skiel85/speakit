package speakit.compression;


/**
 * Clase para codificar una palabra
 * 
 */

public class FrontCodingWordEncoder {

    private String lastWord = null; 
    
    /**
	 * Metodo que comprime una palabra.
	 * 
	 * @param string
	 * @return FrontCodedWord
	 */
	public FrontCodedWord encode(String string) {
		FrontCodedWord encodedWord;
		if(lastWord == null){
			lastWord = string;
			encodedWord = new FrontCodedWord((short)0, string);
		}else{
			short matchingCharacters = compare (lastWord,string);
			lastWord = string;
			String endingCharacters = "";
			char[] charsOfString = new char[string.length()];
			charsOfString = string.toCharArray();
			short position = matchingCharacters;
			while(position < (short)string.length()){
				endingCharacters = endingCharacters + charsOfString[position];
				position++;
			}
			encodedWord = new FrontCodedWord(matchingCharacters,endingCharacters);
		}
		return encodedWord;
	}

	
	
	/**
	 * Metodo para comparar cantidad de caracteres iguales en 2 strings
	 * 
	 * @param string1
	 * @param string2
	 *            
	 */
	private short compare (String string1, String string2){
		short equalsCharacters = 0;
		int position = 0;
		char[] charactersOfString1 = new char[string1.length()];
		char[] charactersOfString2 = new char[string2.length()];
		charactersOfString1 = string1.toCharArray();
		charactersOfString2 = string2.toCharArray();
		if(string1.length()<string2.length()){
			while(position<string1.length()){
				if(charactersOfString1[position] == charactersOfString2[position]){
					equalsCharacters++;
					position++;
					
				}else{
					position++;
				}
			}
		}else{
			if(string2.length()<string1.length()){
				while(position<string2.length()){
					if(charactersOfString1[position] == charactersOfString2[position]){
						equalsCharacters++;
						position++;
						
					}else{
						position++;
					}
				}
			}else{
				while(position<string1.length()){
					if(charactersOfString1[position] == charactersOfString2[position]){
						equalsCharacters++;
						position++;
					
					}else{
						position++;
					}
				}
			}
			
		}
		
		return equalsCharacters;
	}
	 
}
