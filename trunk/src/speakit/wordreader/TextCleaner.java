package speakit.wordreader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		Pattern p = Pattern.compile("[^a-z0-9áéíóúàèìòùâêîôûñüç]");
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
}
