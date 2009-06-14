package speakit.compression.arithmetic;

import speakit.SpeakitLogger;
import speakit.compression.arithmetic.Binary;

public class Emitter {
	private int		underflowCount;		
	private String	emissionBuffer;
	
	public Emitter() {
		this.underflowCount = 0;
		this.emissionBuffer = "";
	}
	 
	public int getUndeflowCount(){
		return this.underflowCount;
	}
	
	/**
	 * Emite los bits enviados, emite el underflow si hiciera falta reiniciando
	 * el contador.
	 * 
	 * @param overflow
	 */
	public void emitOverflow(String overflow) {
		SpeakitLogger.Log("Emitiendo overflow: ");
		String emision = "";
		StringBuffer overflowBuffer = new StringBuffer();
		for (int i = 0; i < overflow.length(); i++) {
			overflowBuffer.append(overflow.charAt(i));
			emision+=overflow.charAt(i);
			if (this.underflowCount > 0 && i == 0) {
				String underflowBits = Binary.repeat(not(overflow.charAt(0)), this.underflowCount);
				overflowBuffer.append(underflowBits);
				emision+="(" + underflowBits + ")";
			}
		}
		SpeakitLogger.Log(emision);
		if (overflowBuffer.length() > 0) {
			this.emissionBuffer += overflowBuffer;
			this.underflowCount = 0;
		}
	}
	
	public String flush() {
		String flow = emissionBuffer;
		emissionBuffer = "";
		return flow;
	}

	private char not(char bit) {
		return (bit == '1') ? '0' : '1';
	}

	public void setUnderflowCount(int underflowCount) {
		this.underflowCount=underflowCount;
	}
}
