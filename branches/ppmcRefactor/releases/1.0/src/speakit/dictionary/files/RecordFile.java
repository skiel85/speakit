package speakit.dictionary.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Archivo de registros.
 */
public class RecordFile {
	private InputStream inputStream;
	private OutputStream outputStream;
	private RecordFactory recordFactory;
	private long currentWriteOffset;
	private long currentReadOffset;

	/*
	 * public RecordFile(InputStream inputStream, OutputStream outputStream,
	 * RecordFactory recordFactory) { this.inputStream = inputStream;
	 * this.outputStream = outputStream; this.recordFactory = recordFactory; }
	 */

	/**
	 * Construye un archivo de registros a partir de un archivo y una fabrica de
	 * registros. Se utiliza la fábrica de registros, para crear los registros
	 * que se van a deserializar.
	 */
	public RecordFile(File file, RecordFactory recordFactory) throws FileNotFoundException {
		this.inputStream = new FileInputStream(file);
		this.outputStream = new FileOutputStream(file, true);
		this.recordFactory = recordFactory;
		this.currentReadOffset = 0;
		this.currentWriteOffset = file.length();
	}

	/**
	 * Lee el registro siguiente y lo devuelve. El archivo de registros queda
	 * posicionado en el registro siguiente.
	 * 
	 * @return El registro siguiente.
	 * @throws IOException
	 */
	public Record readRecord() throws IOException {
		Record record = recordFactory.createRecord();
		long bytesRead;
		try {
			bytesRead = record.deserialize(this.inputStream);
			this.currentReadOffset += bytesRead;
			return record;
		} catch (RecordSerializationException e) {
			// TODO Resolver que hacer cuando una excepcion es lanzada en
			// RecordFile.readRecord
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Lee el registro ubicado en el offset indicado. El archivo de registros
	 * queda posicionado en el registro siguiente.
	 * 
	 * @return El registro siguiente.
	 * @throws IOException
	 */
	public Record readRecord(long offset) throws IOException {
		this.resetReadOffset();
		this.inputStream.skip(offset);
		return this.readRecord();
	}

	/**
	 * Escribe un registro al final del archivo de registros.
	 * 
	 * @param record
	 *            Registro a escribir.
	 * @throws IOException
	 */
	public void writeRecord(Record record) throws IOException {
		long bytesWritten;
		try {
			bytesWritten = record.serialize(this.outputStream);
			this.currentWriteOffset += bytesWritten;
		} catch (RecordSerializationException e) {
			// TODO Resolver que hacer cuando una excepcion es lanzada en
			// RecordFile.writeRecord
			e.printStackTrace();
		}
	}

	/**
	 * Indica si ya no hay más registros que leer.
	 * 
	 * @return Verdadero si no hay mas registros. Falso en caso contrario.
	 * @throws IOException
	 */
	public boolean eof() throws IOException {
		return (this.inputStream.available() == 0);
	}

	public void resetReadOffset() throws IOException {
		this.currentReadOffset = 0;
		if (this.inputStream.markSupported()) {
			this.inputStream.reset();
		} else if (this.inputStream instanceof FileInputStream) {
			((FileInputStream) this.inputStream).getChannel().position(0);
		} else {
			throw new RuntimeException("Operación no soportada para " + this.inputStream.getClass().getName());
		}
	}

	public long getCurrentReadOffset() {
		return this.currentReadOffset;
	}

	public long getCurrentWriteOffset() {
		return this.currentWriteOffset;
	}
}
