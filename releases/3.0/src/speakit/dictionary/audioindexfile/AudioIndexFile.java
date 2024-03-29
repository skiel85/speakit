package speakit.dictionary.audioindexfile;

import java.io.File;
import java.io.IOException;

import speakit.dictionary.audiofile.WordNotFoundException;
import speakit.io.record.Record;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.io.recordfile.SecuentialRecordFile;

/**
 * Representa un archivo de registros de �ndice del archivo de registros de
 * audio.
 */
public class AudioIndexFile implements RecordFactory {
	private SecuentialRecordFile<AudioIndexRecord, StringField> recordFile;

	/**
	 * Construye un nuevo archivo de registros de �ndice.
	 * 
	 * @param file
	 *            Archivo.
	 * @throws IOException
	 */
	public AudioIndexFile(File file) throws IOException {
		this.recordFile = new SecuentialRecordFile<AudioIndexRecord, StringField>(file, this);
	}

	/**
	 * Agrega una entrada al �ndice.
	 * 
	 * @param word
	 *            Palabra (clave del �ndice).
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
	 * Verifica si una palabra est� contenida en el �ndice.
	 * 
	 * @param word
	 *            Palabra a buscar.
	 * @return Verdadero si la palabra se encuentra en el �ndice, falso en caso
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
		AudioIndexRecord record = this.recordFile.getRecord(new StringField(word));
		if (record == null) {
			throw new WordNotFoundException(word);
		}
		return record.getOffset();
	}

	/**
	 * Crea un registro de �ndice, seg�n lo especificado en la interfaz
	 * RecordFactory.
	 */
	@Override
	public Record createRecord() {
		return new AudioIndexRecord();
	}
}
