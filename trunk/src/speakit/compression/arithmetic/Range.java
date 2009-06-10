package speakit.compression.arithmetic;

import java.io.IOException;

import speakit.SpeakitLogger;

public class Range {

	private final int	precision;
	private Binary		floor			= null;
	private Binary		roof			= null;
	private int			underflowCount	= 0;
	private long		rangeSize		= 0;

	public Range(int precision) throws IOException {
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
	 */
	public String getFloor() {
		return floor.getBits();
	}

	/**
	 * Getter of the property <tt>roof</tt>
	 * 
	 * @return Returns the roof.
	 * @uml.property name="roof"
	 */
	public String getRoof() {
		return roof.getBits();
	}

	/**
	 * Simplifica el rango resolviendo underflow y overflow
	 * 
	 * @throws IOException
	 */
	private void simplify() throws IOException {
		solveOverflow();
		solveUnderflow();
	}

	/**
	 * Elimina los bits en underflow, rearma el rango shifteando a izquierda
	 * para ocupar el lugar de los bits de underflow. Incrementa el contador de
	 * underflow si hiciera falta.
	 * 
	 * @throws IOException
	 */
	private void solveUnderflow() throws IOException {
		boolean exit = false;
		String floorbits = floor.getBits();
		String roofbits = roof.getBits();
		if (floorbits.charAt(0) != roofbits.charAt(0)) {
			// puede haber underflow
			for (int i = 1; i < floorbits.length() && !exit; i++) {
				if (floorbits.charAt(i) != floorbits.charAt(0) && floorbits.charAt(i) != roofbits.charAt(i)) {
					this.underflowCount++;
				} else {
					exit = true;
				}
			}
			this.setBounds(floor.shiftLeft(this.underflowCount, 1, new ConstantBitReader(true)).getBits(), roof.shiftLeft(this.underflowCount, 1, new ConstantBitReader(false))
					.getBits(), false);
		}
		SpeakitLogger.Log("UF= " + this.underflowCount);
	}

	/**
	 * resuelve el overflow, modifica el rango, emite los bits de overflow y
	 * underflow en el emissionBuffer, reinicia el contador de underflow si
	 * hiciera falta.
	 * 
	 * @throws IOException
	 */
	private void solveOverflow() throws IOException {
		String overflow = "";
		boolean exit = false;
		String floorbits = floor.getBits();
		String roofbits = roof.getBits();
		for (int i = 0; i < floorbits.length() && !exit; i++) {
			if (floorbits.charAt(i) == roofbits.charAt(i)) {
				overflow += floorbits.charAt(i);
			} else {
				exit = true;
			}
		}
		this.setBounds(floor.shiftLeft(overflow.length(), 0, new ConstantBitReader(true)).getBits(), roof.shiftLeft(overflow.length(), 0, new ConstantBitReader(false)).getBits(),
				false);
		emitOverflow(overflow);
	}

	/**
	 * @throws IOException
	 */
	public void zoomIn(Double accumulatedProbability, Double probability) throws IOException {
		SpeakitLogger.Log("ZoomIn: acumProba:" + accumulatedProbability + ",probability" + probability);
		if (probability == 0) {
			throw new RuntimeException("La probabilidad del símbolo no puede ser cero");
		}
		long floor = (long) Math.round(this.floor.getNumber() + this.rangeSize * accumulatedProbability);
		long roof = (long) Math.round(floor - 1 + this.rangeSize * probability);
		this.setBounds(Binary.numberToBinary(floor), Binary.numberToBinary(roof));
	}

	String	emissionBuffer	= "";
	/**
	 * Devuelve el buffer de emision actual y lo limpia
	 * 
	 * @return
	 */
	public String flush() {
		String flow = emissionBuffer;
		emissionBuffer = "";
		return flow;
	}

	/**
	 * Emite los bits enviados, emite el underflow si hiciera falta reiniciando
	 * el contador.
	 * 
	 * @param overflow
	 */
	private void emitOverflow(String overflow) {
		SpeakitLogger.Log("Emitiendo overflow: ");
		StringBuffer overflowBuffer = new StringBuffer();
		for (int i = 0; i < overflow.length(); i++) {
			overflowBuffer.append(overflow.charAt(i));
			SpeakitLogger.Log(overflow.charAt(i));
			if (this.underflowCount > 0 && i == 0) {
				String underflowBits = Binary.repeat(not(overflow.charAt(0)), this.underflowCount);
				overflowBuffer.append(underflowBits);
				SpeakitLogger.Log("(" + underflowBits + ")");
			}
		}
		SpeakitLogger.Log("\n");
		if (overflowBuffer.length() > 0) {
			this.emissionBuffer += overflowBuffer;
			this.underflowCount = 0;
		}
	}

	private char not(char bit) {
		return (bit == '1') ? '0' : '1';
	}

	/**
	 * Emite el piso del rango
	 */
	public void emitEnding() {

		// emitOverflow(new Binary(
		// this.floor.getNumber()+1,this.precision).getBits());

		emitOverflow(this.floor.getBits());
	}

	/**
	 * Setea los limites del rago (piso y techo), resuelve underflows/overflows
	 * si corresponde
	 * 
	 * @param floor
	 * @param roof
	 * @throws IOException
	 */
	public void setBounds(String floor, String roof) throws IOException {
		setBounds(floor, roof, true);
	}

	/**
	 * Setea los limites del rago (piso y techo), resuelve underflows/overflows
	 * si simplify == true y corresponde
	 * 
	 * @param floor
	 * @param roof
	 * @throws IOException
	 */
	private void setBounds(String floor, String roof, boolean simplify) throws IOException {
		this.floor = new Binary(floor, this.precision);
		this.roof = new Binary(roof, this.precision);
		rangeSize = calculateRangeSize();
		if (simplify) {
			this.simplify();
		}
		SpeakitLogger.Log("*Pos Rango:\n" + this.toString());
	}

	private long calculateRangeSize() {
		return (long) this.roof.getNumber() - (long) this.floor.getNumber() + 1L;
	}

	public int getUnderflowCount() {
		return underflowCount;
	}

	public long getNumericFloor() {
		return this.floor.getNumber();
	}

	public long getNumericRoof() {
		return this.roof.getNumber();
	}
	
	public long getRangeSize() {
		return this.rangeSize;
	}

	@Deprecated
	public double getProbabilityFor(long number) {
		return (long) (number - this.getNumericFloor()) / (double) this.rangeSize;
	}

	@Override
	public String toString() {
		return "Floor= " + this.floor.toString() + "\nRoof=  " + this.roof.toString();
	}
}
