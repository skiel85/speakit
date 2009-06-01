package speakit.compression.arithmetic;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Range {

	private final byte	precision;

	public Range(byte precision) {
		this.precision = precision;
		roof = createRangeBound(false);
		floor = createRangeBound(true);
	}

	private String createRangeBound(boolean isFloor) {
		String res = "";
		for (int i = 0; i < precision; i++) {
			res += (isFloor) ? "0" : "1";
		}
		return res;
	}

	/**
	 * @uml.property name="floor"
	 */
	private String	floor	= "";

	/**
	 * Getter of the property <tt>floor</tt>
	 * 
	 * @return Returns the floor.
	 * @uml.property name="floor"
	 */
	public String getFloor() {
		return floor;
	}

	/**
	 * @uml.property name="roof"
	 */
	private String	roof			= "";
	private byte	underflowCount	= 0;

	/**
	 * Getter of the property <tt>roof</tt>
	 * 
	 * @return Returns the roof.
	 * @uml.property name="roof"
	 */
	public String getRoof() {
		return roof;
	}

	/**
	 * Simplifica el rango resolviendo underflow y overflow
	 */
	private void simplify() {
		solveOverflow();
		solveUnderflow();
	}

	/**
	 * Elimina los bits en underflow, rearma el rango shifteando a izquierda para ocupar el lugar de los bits de underflow. Incrementa el contador de underflow si hiciera falta.
	 */
	private void solveUnderflow() { 
		boolean exit = false;
		if (floor.charAt(0) != roof.charAt(0)) {
			// puede haber underflow
			for (int i = 1; i < this.floor.length() && !exit; i++) {
				if (floor.charAt(i) != floor.charAt(0) && floor.charAt(i) != roof.charAt(i)) {
					this.underflowCount++;
				} else {
					exit = true;
				}
			}
			roof = shiftLeft(roof, this.underflowCount, 1, "1");
			floor = shiftLeft(floor, this.underflowCount, 1, "0"); 
		}
	}

	/**
	 * resuelve el overflow, modifica el rango, emite los bits de overflow y underflow en el emissionBuffer, reinicia el contador de underflow si hiciera falta.
	 */
	private void solveOverflow() {
		String overflow = "";
		boolean exit = false;
		for (int i = 0; i < this.floor.length() && !exit; i++) {
			if (floor.charAt(i) == roof.charAt(i)) {
				overflow += floor.charAt(i);
			} else {
				exit = true;
			}
		}
		roof = shiftLeft(roof, overflow.length(), 0, "1");
		floor = shiftLeft(floor, overflow.length(), 0, "0");
		emitOverflow(overflow);
	}

	/**
	 * mueve todos los bits a la izquierda y agrega al final los bits deseados
	 * 
	 * @param array
	 * @param shiftSize
	 * @param completionBit
	 * @return
	 */
	private String shiftLeft(String array, int shiftSize, int startPos, String completionBit) {
		String shifted = "";
		for (int i = 0; i < array.length(); i++) {
			if (i >= startPos) {
				if (i + shiftSize < array.length()) {
					shifted += array.charAt(i + shiftSize);
				} else {
					shifted += completionBit;
				}
			} else {
				shifted += array.charAt(i);
			}

		}
		return shifted;
	}

	/**
	 */
	public Range zoomIn(Float accumulatedProbability, Float probability) {
		throw new NotImplementedException();
		// Ajustar piso y techo
		// return this;
	}

	StringBuffer	emissionBuffer	= new StringBuffer();
	/**
	 * Devuelve el buffer de emision actual y lo limpia
	 * 
	 * @return
	 */
	public String flush() {
		String flow = emissionBuffer.toString();
		emissionBuffer=new StringBuffer();
		return flow;
	}

	/**
	 * Emite los bits enviados, emite el underflow si hiciera falta reiniciando el contador.
	 * @param overflow
	 */
	private void emitOverflow(String overflow) {
		StringBuffer overflowBuffer = new StringBuffer();
		for (int i = 0; i < overflow.length(); i++) {
			overflowBuffer.append(overflow.charAt(i));
			if (this.underflowCount > 0 && i == 0) {
				overflowBuffer.append(repeat(not(overflow.charAt(0)), this.underflowCount));
			}
		}
		if(overflowBuffer.length()>0){
			this.emissionBuffer.append(overflowBuffer);
			this.underflowCount=0;
		}
	}

	private String repeat(char charToRepeat, int count) {
		StringBuffer buffer = new StringBuffer();
		for (int j = 0; j < count; j++) {
			buffer.append(charToRepeat);
		}
		return buffer.toString();
	}

	private char not(char bit) {
		return (bit == '1') ? '0' : '1';
	}

	/**
	 * Emite el piso del rango
	 */
	public void emitEnding() {
		emitOverflow(this.floor);
	}

	public void setBounds(String floor, String roof) {
		this.floor = floor;
		this.roof = roof;
		this.simplify();
	}

	public byte getUnderflowCount() {
		return underflowCount;
	}

}
