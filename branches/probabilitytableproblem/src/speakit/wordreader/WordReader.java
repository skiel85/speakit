package speakit.wordreader;

/**
 * Lector de palabras. Utilizado para leer las palabras de un documento de
 * texto, salteando signos de puntuaci�n, espacios y caracteres de nueva l�nea.
 */
public interface WordReader {
	/**
	 * Indica si quedan palabras por leer.
	 * 
	 * @return Verdadero si hay una palabra para leer, falso si no hay m�s
	 *         palabras.
	 */
	public boolean hasNext();

	/**
	 * Obtiene la siguiente palabra del documento de texto.
	 * 
	 * @return La siguiente palabra.
	 */
	public String next();
}
