package speakit.compression;



/**
 * Clase para poder codificar y decodificar palabras.
 * 
 */
public class FrontCodingCompressor {
	
	/**
	 * Metodo que permite codificar un array de palabras
	 * 
	 * @param strings
	 *            
	 */
	public FrontCodedWord[] compress(String[] strings) {
		FrontCodedWord[] compressedWords = new FrontCodedWord[strings.length];
		FrontCodingWordEncoder encoder = new FrontCodingWordEncoder();
		for(int i=0;i<strings.length;i++){
			compressedWords[i] = encoder.encode(strings, strings[i]);
		}
		
		return compressedWords;
	}

	
	/**
	 * Metodo que permite decodificar un array de palabras comprimidas
	 * 
	 * @param frontCodedWords
	 */
	public String[] decompress(FrontCodedWord[] frontCodedWords) {
	    String[] decompressedWords = new String[frontCodedWords.length];
	    FrontCodingWordDecoder decoder = new FrontCodingWordDecoder(); 
	    int position = 0;
	    while(position < frontCodedWords.length){
	    	if(position == 0){
	    		String word = decoder.decode(frontCodedWords[position]);
	    		decompressedWords[position] = word;
	    	}else{
	    	    String beforeWord = decompressedWords[position--];
	    		FrontCodedWord actualWord = frontCodedWords[position];
	    		short matchingCharacters = actualWord.getMatchingCharacters(); 
	    		char[] array = new char[beforeWord.length()];
	    		array = beforeWord.toCharArray();
	    		String decompressedWord = "";
	    		for(int i=0; i<matchingCharacters;i++){
	    			decompressedWord = decompressedWord + array[i];
	    		}
	    		decompressedWord = decompressedWord + actualWord.getEndingCharacters();
	    		decompressedWords[position] = decompressedWord;
	    	} 
	    }
		
		return decompressedWords;
	}

}
