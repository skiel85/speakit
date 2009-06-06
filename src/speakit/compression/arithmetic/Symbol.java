package speakit.compression.arithmetic;

public class Symbol implements Comparable<Symbol> {

	private static final int ESC_CODE = -1;
	private static final int EOF_CODE = -2;

	/**
	 * @uml.property name="number"
	 */
	private int number;

	/**
	 * Getter of the property <tt>number</tt>
	 * 
	 * @return Returns the number.
	 * @uml.property name="number"
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Setter of the property <tt>number</tt>
	 * 
	 * @param number
	 *            The number to set.
	 * @uml.property name="number"
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 */
	public String toString() {
		if (this.number >= 0) {
			return Character.toString((char) this.number);
		} else if (this.number == Symbol.ESC_CODE) {
			return "ESC";
		} else if (this.number == Symbol.EOF_CODE) {
			return "EOF";
		} else {
			return "ERR";
		}
	}

	/**
	 */
	public static Symbol getEscape() {
		return new Symbol(Symbol.ESC_CODE);
	}

	/**
	 */
	public static Symbol getEof() {
		return new Symbol(EOF_CODE);
	}

	public Symbol(int number) {
		this.number = number;
	}

	public Symbol(char character) {
		this.number = (int) character;
	}

	@Override
	public int compareTo(Symbol o) {
		return new Integer(this.number).compareTo(new Integer(o.number));
	}

	@Override
	public boolean equals(Object o) {
		return (this.number == ((Symbol) o).number);
	}

	@Override
	public int hashCode() {
		return this.number;
	}

	public char getChar() {
		if (this.number < 0) {
			throw new RuntimeException("No es posible devolver un char porque el symbolo no es unicode es un ESC o un EOF");
		}
		return (char) this.number;
	}

	public Symbol next() {
		return new Symbol(this.number++);
	}

	public Symbol previous() {
		return new Symbol(this.number--);
	}
	
	public static Symbol first() {
		return Symbol.getEof();
	}
	
	public static Symbol last() {
		return new Symbol(65535);
	}

	public int distance(Symbol other) {
		return Math.abs(other.number - this.number);
	}

}