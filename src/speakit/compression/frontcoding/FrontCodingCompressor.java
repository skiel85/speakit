package speakit.compression.frontcoding;

/**
 * Clase para poder codificar y decodificar palabras.
 * 
 */
public class FrontCodingCompressor {

	/**
	 * Metodo que permite codificar un array de palabras
	 * 
	 * @param strings
	 * @return FrontCodedWord[]           
	 */
	public FrontCodedWord[] compress(String[] strings) {

		FrontCodedWord[] compressedWords = new FrontCodedWord[strings.length];
		FrontCodingWordEncoder encoder = new FrontCodingWordEncoder();
		for(int i=0;i<strings.length;i++){
			compressedWords[i] = encoder.encode(strings[i]);
		}
		return compressedWords;
	}
	
	/**
	 * Metodo que permite decodificar un array de palabras comprimidas
	 * 
	 * @param frontCodedWords
	 * @return String[]
	 */
	public String[] decompress(FrontCodedWord[] frontCodedWords) {

	    String[] decompressedWords = new String[frontCodedWords.length];
	    FrontCodingWordDecoder decoder = new FrontCodingWordDecoder(); 
	    for(int position=0;position < frontCodedWords.length;position++){
	    	decompressedWords[position] = decoder.decode(frontCodedWords[position]);
	    }
		return decompressedWords;

	}

}
