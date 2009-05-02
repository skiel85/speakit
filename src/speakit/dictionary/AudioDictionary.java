package speakit.dictionary;

import java.io.IOException;

import speakit.Configuration;
import speakit.FileManager;
import speakit.audio.Audio;
import speakit.dictionary.audiofile.WordNotFoundException;
import speakit.io.File;
import speakit.io.record.RecordSerializationException;

/**
 * Diccionario de audio. A partir de una palabra obtiene su representación
 * sonora.
 */
public interface AudioDictionary extends File {

	/**
	 * Carga el diccionario con los archivos especificados. Se debe llamar a
	 * este método antes de cualquier otro.
	 * 
	 * @throws IOException
	 */
	public void load(FileManager fileManager, Configuration conf) throws IOException;

	/**
	 * Agrega una nueva entrada al diccionario, es decir, agrega al diccionario
	 * una nueva palabra junto con su audio.
	 * 
	 * @param word
	 *            Palabra a insertar.
	 * @param audio
	 *            Bytes del audio a insertar.
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public void addEntry(String word, Audio audio) throws IOException, RecordSerializationException;

	/**
	 * Verifica si una palabra está contenida en el diccionario.
	 * 
	 * @param word
	 *            Palabra a buscar.
	 * @return Verdadero si la palabra existe en el diccionario, falso en caso
	 *         contrario.
	 * @throws IOException
	 */
	public boolean contains(String word) throws IOException;

	/**
	 * Obtiene los bytes del audio de una palabra del diccionario.
	 * 
	 * @param word
	 *            Palabra buscada.
	 * @return Bytes del audio de la palabra.
	 * @throws IOException
	 */
	public Audio getAudio(String word) throws IOException, WordNotFoundException;
}
