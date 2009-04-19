package speakit.dictionary.files;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import speakit.io.RandomAccessFileInputStream;
import speakit.io.RandomAccessFileOutputStream;

/**
 * Archivo de registros.
 */
public class RecordFile {
	private RandomAccessFile randomAccessFile;
	private RandomAccessFileInputStream inputStream;
	private RandomAccessFileOutputStream outputStream;
	private RecordFactory recordFactory;

	/**
	 * Construye un archivo de registros a partir de un archivo y una fabrica de
	 * registros. Se utiliza la fábrica de registros, para crear los registros
	 * que se van a deserializar.
	 * 
	 * @throws IOException
	 */
	public RecordFile(File file, RecordFactory recordFactory) throws IOException {
		this.recordFactory = recordFactory;
		this.randomAccessFile = new RandomAccessFile(file, "rw");
		this.inputStream = new RandomAccessFileInputStream(this.randomAccessFile);
		this.outputStream = new RandomAccessFileOutputStream(this.randomAccessFile);
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
		try {
			record.deserialize(this.inputStream);
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
		try {
			record.serialize(this.outputStream);
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
		if (this.inputStream.markSupported()) {
			this.inputStream.reset();
		} else {
			throw new RuntimeException("Operación no soportada para " + this.inputStream.getClass().getName());
		}
	}

	public long getCurrentReadOffset() {
		return this.inputStream.getPosition();
	}

	public long getCurrentWriteOffset() {
		return this.outputStream.getPosition();
	}
}
