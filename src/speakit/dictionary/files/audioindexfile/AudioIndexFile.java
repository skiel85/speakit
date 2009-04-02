package speakit.dictionary.files.audioindexfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import speakit.dictionary.files.Record;
import speakit.dictionary.files.RecordFactory;
import speakit.dictionary.files.RecordFile;

/**
 * Representa un archivo de registros de índice del archivo de registros de
 * audio.
 */
public class AudioIndexFile implements RecordFactory {
	private RecordFile recordFile;

	/**
	 * Construye un nuevo archivo de registros de índice.
	 * 
	 * @param file
	 *            Archivo.
	 * @throws FileNotFoundException
	 */
	public AudioIndexFile(File file) throws FileNotFoundException {
		this.recordFile = new RecordFile(file, this);
	}

	/**
	 * Agrega una entrada al índice.
	 * 
	 * @param word
	 *            Palabra (clave del índice).
	 * @param offset
	 *            Offset del archivo de registros de audio donde se encuentra el
	 *            audio de la palabra.
	 * @throws IOException
	 */
	public void addEntry(String word, long offset) throws IOException {
		AudioIndexRecord record = new AudioIndexRecord(word, offset);
		this.recordFile.writeRecord(record);
	}

	/**
	 * Verifica si una palabra está contenida en el índice.
	 * 
	 * @param word
	 *            Palabra a buscar.
	 * @return Verdadero si la palabra se encuentra en el índice, falso en caso
	 *         contrario.
	 * @throws IOException
	 */
	public boolean contains(String word) throws IOException {
		this.recordFile.resetReadOffset();
		while (!this.recordFile.eof()) {
			AudioIndexRecord record = (AudioIndexRecord) this.recordFile.readRecord();
			if (record.getWord().compareTo(word) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Obtiene el offset donde se encuentra el audio de una palabra en el
	 * archivo de registros de audio.
	 * 
	 * @param word
	 *            Palabra buscada.
	 * @return Offset del audio de la palabra en el archivo de registros de
	 *         audio.
	 * @throws IOException
	 */
	public long getOffset(String word) throws IOException {
		recordFile.resetReadOffset();// TODO: hacer una prueba de esto
		while (!this.recordFile.eof()) {
			AudioIndexRecord record = (AudioIndexRecord) this.recordFile.readRecord();
			if (record.getWord().compareTo(word) == 0) {
				return record.getOffset();
			}
		}
		throw new RuntimeException("No se encontró la palabra en el índice.");
	}

	/**
	 * Crea un registro de índice, según lo especificado en la interfaz
	 * RecordFactory.
	 */
	@Override
	public Record createRecord() {
		return new AudioIndexRecord();
	}
}
