package speakit.compression.arithmetic;


public class Symbol {

	/**
	 * @uml.property  name="number"
	 */
	private int number;

	/**
	 * Getter of the property <tt>number</tt>
	 * @return  Returns the number.
	 * @uml.property  name="number"
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Setter of the property <tt>number</tt>
	 * @param number  The number to set.
	 * @uml.property  name="number"
	 */
	public void setNumber(int number) {
		this.number = number;
	}

		
	/**
	 */
	public String toString(){
	return ""; 
	}

			
	/**
	 */
	public static Symbol GetEscape(){
		return null;
	}

				
	/**
	 */
	public static Symbol GetEof(){
		return null;
	}

					
	/**
	 */
	public Symbol(char character){
	}

}
