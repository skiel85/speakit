package speakit.compression;



/**
 * Clase para codificar una palabra
 * 
 */
public class FrontCodingWordEncoder {

    private boolean isFirstWord = false;
    
    /**
	 * Metodo que comprime una palabra de un array de palabras.
	 * 
	 * @param arrayOfStrings
	 * @param string
	 * 
	 */
	public FrontCodedWord encode(String [] arrayOfStrings, String string) {
		FrontCodedWord returnWord;
		String beforeString = "";
		String actualString = "";
		int counter = 0;
		
		while (actualString != string){
			actualString = arrayOfStrings[counter];
			counter++;
		}	
		if((actualString == arrayOfStrings[0]) && (isFirstWord == false)){
			returnWord = new FrontCodedWord((short)0,string);
			isFirstWord = true;
		}else{
			counter = counter - 2;
			if(counter >= 0){
				beforeString = arrayOfStrings[counter];
			}else{
				beforeString = actualString;
			}
			short equalsCharacters = compare(beforeString,actualString);
			String endingCharacters = "";
			char[] charsOfString = new char[string.length()];
			charsOfString = string.toCharArray();
			short position = equalsCharacters;
		
			while(position <string.length()){
				endingCharacters = endingCharacters + charsOfString[position];
				position++;
			}
			returnWord = new FrontCodedWord(equalsCharacters,endingCharacters);
		
		}
		return returnWord;
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
