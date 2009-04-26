package speakit.io.record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ByteArrayField extends Field {

	byte[] value;

	public ByteArrayField(byte[] value) {
		this.setBytes(value);
	}

	public ByteArrayField() {
		this(new byte[] {});
	}

	public byte[] getBytes() {
		return this.value;
	}

	public void setBytes(byte[] value) {
		if (value == null) {
			this.value = new byte[] {};
		} else {
			this.value = value;
		}
	}

	public int getValueLenght() {
		return this.getBytes().length;
	}

	@Override
	public int getSerializationSize() {
		return new IntegerField(getValueLenght()).getSerializationSize() + getValueLenght();
	}

	/**
	 * lee primero el campo que indica la longitud y luego lee esa cantidad de
	 * bytes para hidratar el string
	 */
	@Override
	protected void actuallyDeserialize(InputStream in) throws IOException {
		IntegerField size = new IntegerField();
		size.actuallyDeserialize(in);
		this.setBytes(this.readBytes(in, size.getInteger()));
	}

	/**
	 * guarda primero un campo que indica la longitud y a continuacion los bytes
	 * del string
	 */
	@Override
	protected void actuallySerialize(OutputStream out) throws IOException {
		IntegerField size = new IntegerField(getValueLenght());
		size.serialize(out);
		out.write(this.getBytes());
	}

	protected int compareToSameClass(Field o) {
		ByteArrayField other = (ByteArrayField) o;

		byte[] thisBytes = this.getBytes();
		byte[] otherBytes = other.getBytes();

		// se recorren los arrays desde 0 hasta el indice del array mas chico,
		// si algun elemento es difenente se devuelve la comparacion entre esos
		// elementos
		// si son iguales hasta el último elemento se resuelve mas abajo
		for (int i = 0; i < min(thisBytes.length, otherBytes.length) - 1; i++) {
			if (thisBytes[i] != otherBytes[i]) {
				return (thisBytes[i] < otherBytes[i]) ? -1 : +1;
			}
		}
		// Se comparan los largos de los arrays
		// Si los tamaños son iguales ==> arrays iguales
		if (thisBytes.length == otherBytes.length) {
			return 0;
		} else {
			// Si el de tamaño mínimo es el array de este objeto ==> este objeto
			// es MENOR que el otro
			if (min(thisBytes.length, otherBytes.length) == thisBytes.length) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	/**
	 * Calcula el mínimo de dos números.
	 * 
	 * @param length
	 * @param length2
	 * @return
	 */
	private int min(int num1, int num2) {
		return (num1 < num2) ? num1 : num2;
	}

}
