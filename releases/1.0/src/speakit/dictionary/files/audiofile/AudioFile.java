package speakit.dictionary.files.audiofile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import speakit.audio.Audio;
import speakit.dictionary.files.Record;
import speakit.dictionary.files.RecordFactory;
import speakit.dictionary.files.RecordFile;

/**
 * Representa un archivo de registros de audio.
 */
public class AudioFile implements RecordFactory {
	private RecordFile recordFile;
	private long currentOffset;

	/**
	 * Crea un archivo de registros de audio.
	 * 
	 * @param file
	 *            Archivo.
	 * @throws FileNotFoundException
	 */
	public AudioFile(File file) throws FileNotFoundException {
		this.recordFile = new RecordFile(file, this);
		this.currentOffset = file.length();
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
		AudioRecord record = (AudioRecord) this.recordFile.readRecord(offset);
		return new Audio(record.getAudio());
	}

	/**
	 * Agrega un audio al archivo de registros de audio.
	 * 
	 * @param audio
	 *            Audio que se agregará.
	 * @return Offset donde se agregó el audio.
	 * @throws IOException
	 */
	public long addAudio(Audio audio) throws IOException {
		long oldOffset = this.currentOffset;
		AudioRecord record = new AudioRecord(audio.getBytes());
		this.recordFile.writeRecord(record);
		this.currentOffset = this.recordFile.getCurrentWriteOffset();
		return oldOffset;
	}

	/**
	 * Crea un registro de audio, de acuerdo con la implementación de la
	 * interfaz RecordFactory.
	 */
	@Override
	public Record createRecord() {
		return new AudioRecord();
	}
}
