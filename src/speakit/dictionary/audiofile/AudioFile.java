package speakit.dictionary.audiofile;

import java.io.File;
import java.io.IOException;

import speakit.audio.Audio;
import speakit.io.record.RecordFactory;
import speakit.io.record.RecordSerializationException;
import speakit.io.recordfile.OffsetRecordFile;

/**
 * Representa un archivo de registros de audio.
 */
public class AudioFile implements RecordFactory {
	private OffsetRecordFile<AudioRecord> recordFile;

	/**
	 * Crea un archivo de registros de audio.
	 * 
	 * @param file
	 *            Archivo.
	 * @throws IOException
	 */
	public AudioFile(File file) throws IOException {
		this.recordFile = new OffsetRecordFile(file, this);
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
