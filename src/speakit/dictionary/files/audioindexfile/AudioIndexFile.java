package speakit.dictionary.files.audioindexfile;

import java.io.File;
import java.io.IOException;

import speakit.dictionary.files.RecordFactory;
import speakit.dictionary.files.RecordSerializationException;
import speakit.dictionary.files.SecuentialRecordFile;
import speakit.dictionary.files.audiofile.WordNotFoundException;
import speakit.dictionary.serialization.StringField;

/**
 * Representa un archivo de registros de índice del archivo de registros de
 * audio.
 */
public class AudioIndexFile implements RecordFactory<AudioIndexRecord> {
	private SecuentialRecordFile<AudioIndexRecord, StringField> recordFile;

	/**
	 * Construye un nuevo archivo de registros de índice.
	 * 
	 * @param file
	 *            Archivo.
	 * @throws IOException
	 */
	public AudioIndexFile(File file) throws IOException {
		this.recordFile = new SecuentialRecordFile<AudioIndexRecord, StringField>(file, this);
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
	 * @throws RecordSerializationException 
	 */
	public void addEntry(String word, long offset) throws IOException, RecordSerializationException {
		AudioIndexRecord record = new AudioIndexRecord(word, offset);
		this.recordFile.insertRecord(record);
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
		return this.recordFile.contains(new StringField(word));
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
	 * @throws WordNotFoundException
	 */
	public long getOffset(String word) throws IOException, WordNotFoundException {
		return this.recordFile.getRecord(new StringField(word)).getOffset();
	}

	/**
	 * Crea un registro de índice, según lo especificado en la interfaz
	 * RecordFactory.
	 */
	@Override
	public AudioIndexRecord createRecord() {
		return new AudioIndexRecord();
	}
}
