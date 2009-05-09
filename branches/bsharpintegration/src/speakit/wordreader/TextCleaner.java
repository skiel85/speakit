package speakit.wordreader;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import speakit.TextDocument;
import speakit.ftrs.StopWords;

/**
 * Realiza una limpieza del texto, eliminando puntuación y espacios, para luego
 * poder obtener las palabras.
 */
public class TextCleaner {
	/**
	 * Elimina los caracteres no latinos y convierte a minúsculas.
	 * 
	 * @param text
	 *            Texto original.
	 * @return Texto con los caracteres no latinos eliminados.
	 */
	public String replaceStrangeCharacters(String text) {
		text = text.replace('á', 'a');
		text = text.replace('é', 'e');
		text = text.replace('í', 'i');	
		text = text.replace('ó', 'o');
		text = text.replace('ú', 'u');
		text = text.replace('ü', 'u');
		Pattern p = Pattern.compile("[^a-z0-9]");
		Matcher m = p.matcher(text.toLowerCase());
		return m.replaceAll(" ");
	}

	/**
	 * Colapsa los espacios contiguos en un único espacio.
	 * 
	 * @param text
	 *            Texto original.
	 * @return Texto con los espacios colapsados.
	 */
	public String collapseSpaces(String text) {
		Pattern p = Pattern.compile(" +");
		Matcher m = p.matcher(text.toLowerCase());
		return m.replaceAll(" ").trim();
	}

	/**
	 * Realiza todas las operaciones de limpieza.
	 * 
	 * @param text
	 *            Texto original.
	 * @return Texto limpio.
	 */
	public String cleanText(String text) {
		return this.collapseSpaces(this.replaceStrangeCharacters(text));
	}

	/**
	 * Obtiene las palabras del texto realizando una limpieza previa.
	 * 
	 * @param text
	 *            Texto original.
	 * @return Arreglo de palabras.
	 */
	public String[] getWords(String text) {
		return this.cleanText(text).split(" ");
	}

	/**
	 * Obtiene las palabras relevantes del documento de texto, es decir, eliminia los stop words
	 * 
	 * @param textDocument
	 *            Texto original.
	 * @return TextDocument.
	 */
	public TextDocument getRelevantWords (TextDocument textDocument){
		String filteredWords = "";
		ArrayList<String> wordIterable = new ArrayList<String>();
		
		for(String word : textDocument){
			wordIterable.add(word.toLowerCase());
		}
		StopWords stopWords = new StopWords();
		ArrayList<String> stopWordIterable = stopWords.getStopWords();
		
		for(String word : wordIterable){
			if(!stopWordIterable.contains((String)word)){
				if(filteredWords == ""){
					filteredWords = word;
				}else{
					filteredWords = filteredWords + " " + word;
				}
			}
		}
		
		return new TextDocument(textDocument.getId(), filteredWords);
	}
	
	/**
	 * Devuelve un documento con todas las palabras limpias y con los stop words eliminados
	 * 
	 * @param document
	 *            Texto original.
	 * @return TextDocument
	 */
	public TextDocument cleanDocument(TextDocument  document){
		TextDocument relevantDocument = getRelevantWords(document);
		String cleanWords = cleanText(relevantDocument.getText());
		
		return new TextDocument(document.getId(), cleanWords);
	}
	
}
