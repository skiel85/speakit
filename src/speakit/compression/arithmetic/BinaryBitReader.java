package speakit.compression.arithmetic;

import java.io.IOException;
import java.io.Reader;

import sun.misc.Queue;

public class BinaryBitReader extends BitReader {

	private final Reader	in;

	public BinaryBitReader(Reader in) {
		this.in = in;
	}

	public Bit readBit() {
		try {
			return getBit();
		} catch (IOException e) {
			return null;
		}
	}

	private Queue	queue	= new Queue();

	private Integer	next	= null;

	private Bit getBit() throws IOException {
		if (queue.isEmpty()) {
			setNext();
			if (next!= -1) {
				for (char bit : new Binary(next.byteValue(), 8).getBits().toCharArray()) {
					queue.enqueue(new Character(bit));
				}
			} else {
				return null;
			}
			next = null;
		}
		try {
			return new Bit((Character) queue.dequeue());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void reset() throws IOException {
		in.reset();
	}

	private void setNext() throws IOException {
		if (next == null) {
			next = new Integer(in.read());
		}
	}

	@Override
	public boolean hashNext() throws IOException {
		setNext();
		return next != 0;
	}

}
