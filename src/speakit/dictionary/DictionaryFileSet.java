package speakit.dictionary;

import java.io.File;

/**
 * Conjunto de archivos de datos del diccionario.
 */
public interface DictionaryFileSet {

	/**
	 * Archivo de datos de audio.
	 */
	public File getAudioFile();

	/**
	 * Archivo de índice del archivo de datos de audio.
	 */
	public File getAudioIndexFile();

}
