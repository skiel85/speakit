package speakit.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Permite que una sección de un RandomAccessFile sea accedido como un
 * InputStream
 */
public class RandomAccessFileInputStream extends InputStream {
	private RandomAccessFile file;
	private long startPosition;
	private long currentPosition;
	private long markPosition;

	/**
	 * Constructor.
	 * 
	 * @param randomAccessFile
	 *            El archivo de donde leer los datos.
	 * @throws IOException
	 */
	public RandomAccessFileInputStream(RandomAccessFile randomAccessFile) throws IOException {
		this.file = randomAccessFile;
		this.startPosition = 0;
		this.currentPosition = 0;
		this.markPosition = 0;
	}

	/**
	 * Constructor.
	 * 
	 * @param randomAccessFile
	 *            El archivo de donde leer los datos.
	 * @param startPosition
	 *            La posición donde comienza este stream.
	 * @param length
	 *            El largo de este InputStream.
	 */
	public RandomAccessFileInputStream(RandomAccessFile randomAccessFile, long startPosition) {
		this.file = randomAccessFile;
		this.startPosition = startPosition;
		this.currentPosition = startPosition;
		this.markPosition = startPosition;
	}

	/**
	 * Obtiene la posición actual en el archivo.
	 * 
	 * @return Posición actual en el archivo.
	 */
	public long getPosition() {
		return currentPosition;
	}

	/**
	 * Cantidad de bytes realmente escritos.
	 * 
	 * @return La cantidad de bytes realmente escritos.
	 */
	public long getLengthRead() {
		return currentPosition - startPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	public int available() {
		try {
			return (int) (file.length() - currentPosition);
		} catch (IOException e) {
			return 0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() {
		// Dejado intencionalmente en blanco.
		// No queremos hacer nada porque queremos dejar el archivo abierto.
	}

	/**
	 * {@inheritDoc}
	 */
	public int read() throws IOException {
		synchronized (file) {
			int retval = -1;
			if (this.available() > 0) {
				file.seek(currentPosition);
				currentPosition++;
				retval = file.read();
			}
			return retval;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int read(byte[] b, int offset, int length) throws IOException {
		// solo permitir leer la cantidad de bytes disponible.
		if (length > available()) {
			length = available();
		}
		int amountRead = -1;

		// solo leer si hay bytes disponibles, de otra forma devolver -1 si se
		// ha alcanzado el fin del archivo.
		if (available() > 0) {
			synchronized (file) {
				file.seek(currentPosition);
				amountRead = file.read(b, offset, length);
			}
		}

		// actualizar la posición actual del cursor.
		if (amountRead > 0) {
			currentPosition += amountRead;
		}
		return amountRead;
	}

	/**
	 * {@inheritDoc}
	 */
	public long skip(long amountToSkip) {
		long amountSkipped = Math.min(amountToSkip, available());
		currentPosition += amountSkipped;
		return amountSkipped;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean markSupported() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mark(int readLimit) {
		this.markPosition = this.currentPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() {
		this.currentPosition = this.markPosition;
	}

}
