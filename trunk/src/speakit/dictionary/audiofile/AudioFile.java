package speakit.dictionary.audiofile;

import java.io.File;
import java.io.IOException;

import speakit.audio.Audio;
import speakit.dictionary.serialization.LongField;
import speakit.io.RecordFactory;
import speakit.io.RecordSerializationException;
import speakit.io.SecuentialRecordFile;

/**
 * Representa un archivo de registros de audio.
 */
public class AudioFile implements RecordFactory<AudioRecord> {
	private SecuentialRecordFile<AudioRecord, LongField> recordFile;

	/**
	 * Crea un archivo de registros de audio.
	 * 
	 * @param file
	 *            Archivo.
	 * @throws IOException
	 */
	public AudioFile(File file) throws IOException {
		this.recordFile = new SecuentialRecordFile<AudioRecord, LongField>(file, this);
	}

	/**
	 * Obtiene un audio a partir de un offset.
	 * 
	 * @param offset
	 *            Offset donde comienza el audio.
	 * @return Audio encontrado.
	 * @throws IOException
	 */
	public Audio getAudio(long offset) throws IOException {
		AudioRecord record = this.recordFile.readRecord(offset);
		return new Audio(record.getAudio());
	}

	/**
	 * Agrega un audio al archivo de registros de audio.
	 * 
	 * @param audio
	 *            Audio que se agregará.
	 * @return Offset donde se agregó el audio.
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public long addAudio(Audio audio) throws IOException, RecordSerializationException {
		AudioRecord record = new AudioRecord(audio.getBytes());
		this.recordFile.insertRecord(record);
		return record.getOffset();
	}

	/**
	 * Crea un registro de audio, de acuerdo con la implementación de la
	 * interfaz RecordFactory.
	 */
	@Override
	public AudioRecord createRecord() {
		return new AudioRecord();
	}
}
