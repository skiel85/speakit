package speakit.compression.arithmetic;

import java.io.IOException;
import java.math.BigDecimal;

import speakit.SpeakitLogger;
import speakit.compression.arithmetic.test.Emitter;

public class Range { 

	public long		rangeSize;
	private Emitter data;
	private final int	precision;
	private Binary		floor			= null;
	private Binary		roof			= null;
	
	
	public Range(int precision) throws IOException {
		this.precision = precision;
		this.data=new Emitter();
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
			int underflowCount=this.data.getUndeflowCount();
			for (int i = 1; i < floorbits.length() && !exit; i++) {
				if (floorbits.charAt(i) != floorbits.charAt(0) && floorbits.charAt(i) != roofbits.charAt(i)) {
					underflowCount++;
				} else {
					exit = true;
				}
			}
			this.data.setUnderflowCount(underflowCount);
			this.setBounds(floor.shiftLeft(this.data.getUndeflowCount(), 1, new ConstantBitReader(true)).getBits(), roof.shiftLeft(this.data.getUndeflowCount(), 1, new ConstantBitReader(false))
					.getBits(), false);
		}
		SpeakitLogger.Log("UF= " + this.data.getUndeflowCount());
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
		data.emitOverflow(overflow);
	}

	/**
	 * @throws IOException
	 */
	public void zoomIn(Double accumulatedProbability, Double probability) throws IOException {
		SpeakitLogger.Log("ZoomIn: acumProba:" + accumulatedProbability + ",probability" + probability);
		if (probability == 0) {
			throw new RuntimeException("La probabilidad del símbolo no puede ser cero");
		}
		long floor = roundDouble(this.floor.getNumber() + this.rangeSize * accumulatedProbability);
		long roof = roundDouble(floor - 1 + this.rangeSize * probability);
		this.setBounds(Binary.numberToBinary(floor), Binary.numberToBinary(roof));
	}
	
	public long roundDouble(double decimal) { 
		BigDecimal bd = new BigDecimal(decimal);
		bd = bd.setScale(2, BigDecimal.ROUND_UP);
		return bd.longValue();
	} 
	/**
	 * Devuelve el buffer de emision actual y lo limpia
	 * 
	 * @return
	 */
	public String flush() {
		return data.flush();
	}
	 

	/**
	 * Emite el piso del rango
	 */
	public void emitEnding() { 
		data.emitOverflow(this.floor.getBits());
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
		return data.getUndeflowCount();
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
