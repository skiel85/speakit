package speakit.wordreader;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import speakit.TextDocument;

/**
 * Realiza una limpieza del texto, eliminando puntuaci�n y espacios, para luego
 * poder obtener las palabras.
 */
public class TextCleaner {
	/**
	 * Elimina los caracteres no latinos y convierte a min�sculas.
	 * 
	 * @param text
	 *            Texto original.
	 * @return Texto con los caracteres no latinos eliminados.
	 */
	public String replaceStrangeCharacters(String text) {
		
		Pattern p = Pattern.compile("[^a-z0-9�������]");
		Matcher m = p.matcher(text.toLowerCase());
		return m.replaceAll(" ");
	}

	/**
	 * Colapsa los espacios contiguos en un �nico espacio.
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
	 * Realiza todas las operaciones de limpieza, adem�s quita los acentos,
	 * la dieresis y la �
	 * 
	 * @param text
	 *            Texto original.
	 * @return Texto limpio.
	 */
	
	public String normalizeText(String text){
		try{
			text = text.replace('�', 'a');
			text = text.replace('�', 'e');
			text = text.replace('�', 'i');	
			text = text.replace('�', 'o');
			text = text.replace('�', 'u');
			text = text.replace('�', 'u');
			text = text.replace('�', 'n');
		}catch(NullPointerException ioe){
			/*No hace nada porque lo unico que se intenta
			 en el try es tratar de reemplazar esos caracteres
			 si es que los hay, sino, no pasa nada.
			 */ 
		}
		return cleanText(text);
	}
	
	/**
	 * Devuelve un documento con todas las palabras limpias y con los stop words eliminados
	 * 
	 * @param document
	 *            Texto original.
	 * @return TextDocument
	 */
	public TextDocument cleanDocument(TextDocument  document){
		//TextDocument relevantDocument = getRelevantWords(document);
		String cleanWords = normalizeText(document.getText());
		return new TextDocument(document.getId(), cleanWords);
	}
	
}
