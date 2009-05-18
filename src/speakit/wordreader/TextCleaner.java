package speakit.wordreader;


import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import speakit.TextDocument;

import speakit.ftrs.StopWordsFilter;

/**
 * Realiza una limpieza del texto, eliminando puntuación y espacios, para luego
 * poder obtener las palabras.
 */
public class TextCleaner {
	/**
	 * Elimina los caracteres no latinos, convierte a minúsculas y reemplaza los 
	 * caracteres con acento por los respectivos sin acento para la indexación.
	 * 
	 * @param text
	 *            Texto original.
	 * @return Texto con los caracteres no latinos eliminados.
	 */
	public String replaceStrangeCharactersForIndex(String text) {
		try{
			text = text.replace('á', 'a');
			text = text.replace('é', 'e');
			text = text.replace('í', 'i');	
			text = text.replace('ó', 'o');
			text = text.replace('ú', 'u');
			text = text.replace('ü', 'u');
//			text = text.replace('ñ', 'n');
		}catch(NullPointerException ioe){
			/*No hace nada porque lo unico que se intenta
			 en el try es tratar de reemplazar esos caracteres
			 si es que los hay, sino, no pasa nada.
			 */ 
		}
		Pattern p = Pattern.compile("[^a-z0-9ñ]");
		Matcher m = p.matcher(text.toLowerCase());
		return m.replaceAll(" ");
	}
	
	/**
	 * Elimina los caracteres no latinos y convierte a minúsculas.
	 * 
	 * @param text
	 *            Texto original.
	 * @return Texto con los caracteres no latinos eliminados.
	 */
	public String replaceStrangeCharactersToShow(String text){
		Pattern p = Pattern.compile("[^A-Za-z0-9ñÑáéíóúü]");
		Matcher m = p.matcher(text);
		return collapseSpaces(m.replaceAll(" "));
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
		Matcher m = p.matcher(text);
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
		return this.collapseSpaces(this.replaceStrangeCharactersForIndex(text));
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
	 * Devuelve un documento con todas las palabras limpias y con los stop words eliminados
	 * 
	 * @param document
	 *            Texto original.
	 * @return TextDocument
	 * @throws IOException 
	 */
	public TextDocument cleanDocument(TextDocument  document) throws IOException{

		String cleanWords = cleanText(document.getText());
		StopWordsFilter stopWordsFilter = new StopWordsFilter();
		TextDocument relevantsWords = stopWordsFilter.getRelevantWords(new TextDocument(cleanWords));
		
		return relevantsWords;
	}
	
}
