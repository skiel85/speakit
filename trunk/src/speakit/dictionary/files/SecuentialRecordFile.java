package speakit.dictionary.files;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Enumeration;

import speakit.dictionary.serialization.Field;
import speakit.io.RandomAccessFileInputStream;
import speakit.io.RandomAccessFileOutputStream;

/**
 * Archivo de registros.
 */
public class SecuentialRecordFile<RECTYPE extends Record<KEYTYPE>, KEYTYPE extends Field> implements RecordFile<RECTYPE, KEYTYPE>, Enumeration<RECTYPE> {
	private RandomAccessFile randomAccessFile;
	private RandomAccessFileInputStream inputStream;
	private RandomAccessFileOutputStream outputStream;
	private RecordFactory<RECTYPE> recordFactory;

	/**
	 * Construye un archivo de registros a partir de un archivo y una fabrica de
	 * registros. Se utiliza la fábrica de registros, para crear los registros
	 * que se van a deserializar.
	 * 
	 * @throws IOException
	 */
	public SecuentialRecordFile(File file, RecordFactory<RECTYPE> recordFactory) throws IOException {
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
	public RECTYPE readRecord() throws IOException {
		RECTYPE record = recordFactory.createRecord();
		try {
			record.notifyOffsetChanged(this.inputStream.getPosition());
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
	public RECTYPE readRecord(long offset) throws IOException {
		this.resetReadOffset();
		this.inputStream.skip(offset);
		RECTYPE record = this.readRecord();
		record.notifyOffsetChanged(offset);
		return record;
	}

	/** Obtiene un registro a partir de su clave. 
	 * 
	 * @param key Clave a buscar.
	 * @return Registro con la clave buscada.
	 * @throws IOException
	 */
	@Override
	public RECTYPE getRecord(KEYTYPE key) throws IOException {
		this.resetReadOffset();
		long offset = 0;
		while (!this.eof()) {
			offset = this.inputStream.getPosition();
			RECTYPE record = this.readRecord();
			record.notifyOffsetChanged(offset);
			if (record.compareToKey(key) == 0) {
				return record;
			}
		}
		return null;
	}
	
	@Override
	public boolean contains(KEYTYPE key) throws IOException {
		RECTYPE record = this.getRecord(key);
		return (record != null);
	}

	/**
	 * Escribe un registro al final del archivo de registros.
	 * 
	 * @param record
	 *            Registro a escribir.
	 * @throws IOException
	 */
	public void insertRecord(RECTYPE record) throws IOException {
		try {
			record.notifyOffsetChanged(this.outputStream.getPosition());
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

	@Override
	public boolean hasMoreElements() {
		try {
			return this.eof();
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public RECTYPE nextElement() {
		try {
			return this.readRecord();
		} catch (IOException e) {
			return null;
		}
	}

}
