package speakit.compression.arithmetic;


public class Range {

	private final int	precision;
	private String		floor			= "";
	private String		roof			= "";
	private int		underflowCount	= 0;
	private int			numericFloor	= 0;
	private int			numericRoof		= 0;
	private int			rangeSize		= 0;

	public Range(int precision) {
		this.precision = precision;
		this.setBounds(createRangeBound(true), createRangeBound(false));
	}

	private String createRangeBound(boolean isFloor) {
		String res = "";
		for (int i = 0; i < precision; i++) {
			res += (isFloor) ? "0" : "1";
		}
		return res;
	}

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
	 * Elimina los bits en underflow, rearma el rango shifteando a izquierda
	 * para ocupar el lugar de los bits de underflow. Incrementa el contador de
	 * underflow si hiciera falta.
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
			this.setBounds(shiftLeft(floor, this.underflowCount, 1, "0"), shiftLeft(roof, this.underflowCount, 1, "1"),false);
		}
	}

	/**
	 * resuelve el overflow, modifica el rango, emite los bits de overflow y
	 * underflow en el emissionBuffer, reinicia el contador de underflow si
	 * hiciera falta.
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
		this.setBounds(shiftLeft(floor, overflow.length(), 0, "0"), shiftLeft(roof, overflow.length(), 0, "1"),false);
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
	public void zoomIn(Double accumulatedProbability, Double probability) {
		int floor = (int) Math.round(this.numericFloor + this.rangeSize * accumulatedProbability);
		int roof = (int) Math.round(floor - 1 + this.rangeSize * probability);
		this.setBounds(alignRight(Binary.integerToBinary(floor)), alignRight(Binary.integerToBinary(roof)));
	}

	private String alignRight(String num) {
		if (num.length() < this.precision) {
			return this.repeat('0', precision - num.length()) + num;
		} else {
			if (num.length() > this.precision) {
				throw new IllegalArgumentException("El " + num + " es mas grande que lo soportado por la precicion que es de " + this.precision + " ints");
			} else {
				return num;
			}
		}

	}

	
	StringBuffer	emissionBuffer	= new StringBuffer();
	/**
	 * Devuelve el buffer de emision actual y lo limpia
	 * 
	 * @return
	 */
	public String flush() {
		String flow = emissionBuffer.toString();
		emissionBuffer = new StringBuffer();
		return flow;
	}

	/**
	 * Emite los bits enviados, emite el underflow si hiciera falta reiniciando
	 * el contador.
	 * 
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
		if (overflowBuffer.length() > 0) {
			this.emissionBuffer.append(overflowBuffer);
			this.underflowCount = 0;
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

	/**
	 * Setea los limites del rago (piso y techo), resuelve underflows/overflows si corresponde
	 * @param floor
	 * @param roof
	 */
	public void setBounds(String floor, String roof) {
		setBounds(this.alignRight(floor),  this.alignRight(roof), true);
	}
	
	/**
	 * Setea los limites del rago (piso y techo), resuelve underflows/overflows si simplify == true y corresponde
	 * @param floor
	 * @param roof
	 */
	private void setBounds(String floor, String roof,boolean simplify) {
		this.floor = floor;
		this.roof = roof;
		if(simplify){
			this.simplify();	
		}
		numericFloor = Binary.bitStringToInt(this.floor);
		numericRoof = Binary.bitStringToInt(this.roof);
		rangeSize = numericRoof - numericFloor + 1;
	}

	public int getUnderflowCount() {
		return underflowCount;
	}

	public int getNumericFloor() {
		return this.numericFloor;
	}

	public int getNumericRoof() {
		return this.numericRoof;
	}

}
