package speakit.compression.arithmetic;

import java.awt.datatransfer.StringSelection;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class Range {
	 
	private final byte	precision;

	public Range(byte precision){
		this.precision = precision;
		roof=createRangeBound(false);
		floor=createRangeBound(true);
	}
	
	private String createRangeBound(boolean isFloor){
		String res="";
		for (int i = 0; i < precision; i++) {
			res+=(isFloor)?"0":"1";
		}
		return res;
	}

	/**
	 * @uml.property  name="floor"
	 */
	private String floor="";

	/**
	 * Getter of the property <tt>floor</tt>
	 * @return  Returns the floor.
	 * @uml.property  name="floor"
	 */
	public String getFloor() {
		return floor;
	}

	/**
	 * Setter of the property <tt>floor</tt>
	 * @param floor  The floor to set.
	 * @uml.property  name="floor"
	 */
	public void setFloor(String floor) {
		this.floor = floor;
	}  

	/**
	 * @uml.property  name="roof"
	 */
	private String roof="";
	private byte	underflowCount=0;

	/**
	 * Getter of the property <tt>roof</tt>
	 * @return  Returns the roof.
	 * @uml.property  name="roof"
	 */
	public String getRoof() {
		return roof;
	}
	
	/**
	 * Simplifica el rango resolviendo underflow y overflow
	 */
	public void simplify(){
		this.emissionBuffer+=solveOverflow();
		this.underflowCount+=solveUnderflow();
	}

	private byte solveUnderflow() {
		byte underflow = 0;
		boolean exit=false;
		if(floor.charAt(0)!=roof.charAt(0)){
			//puede haber underflow
		}else{
			return 0;
		}
		for (int i = 1; i < this.floor.length() && !exit; i++) {
			if(floor.charAt(i)!=floor.charAt(0) && floor.charAt(i)!=roof.charAt(i)){
				underflow++;
			}else{
				exit=true;
			}
		}
		roof=shiftLeft(roof,underflow,1,"1");
		floor=shiftLeft(floor,underflow,1,"0");
		return underflow;
	}

	/**
	 * resuelve el overflow en el rango, modifica roof y floor
	 * @return devuelve los bits en overflow
	 */
	private String solveOverflow() {
		String overflow = "";
		boolean exit=false;
		for (int i = 0; i < this.floor.length() && !exit; i++) {
			if(floor.charAt(i)==roof.charAt(i)){
				overflow+=floor.charAt(i);
			}else{
				exit=true;
			}
		}
		roof=shiftLeft(roof,overflow.length(),0,"1");
		floor=shiftLeft(floor,overflow.length(),0,"0");
		return overflow;
	}

	/**
	 * mueve todos los bits a la izquierda y agrega al final los bits deseados  
	 * @param array
	 * @param shiftSize
	 * @param completionBit
	 * @return
	 */
	private String shiftLeft(String array, int shiftSize,int startPos,String completionBit) {
		String shifted="";
		for (int i = 0; i < array.length(); i++) {
			if(i>=startPos){
				if(i+shiftSize<array.length()){
					shifted+=array.charAt(i+shiftSize);
				}else{
					shifted+=completionBit;
				}	
			}else{
				shifted+=array.charAt(i);
			}
			
		}
		return shifted;
	}

	/**
	 * Setter of the property <tt>roof</tt>
	 * @param roof  The roof to set.
	 * @uml.property  name="roof"
	 */
	public void setRoof(String roof) {
		this.roof = roof;
	}

		
	/**
	 */
	public Range zoomIn(Float accumulatedProbability, Float probability){
		throw new NotImplementedException();
		//Ajustar piso y techo
		//return this;
	}
 
	String emissionBuffer="";
	/**
	 * Devuelve el buffer de emision actual y lo limpia
	 * @return
	 */
	public String flush(){
		StringBuffer buffer=new StringBuffer();
		for (int i = 0; i < emissionBuffer.length(); i++) {
			buffer.append(emissionBuffer.charAt(i));				
			if(underflowCount>0 && i==0){
				buffer.append(repeat(not(emissionBuffer.charAt(0)),underflowCount));	
			}
		}
		underflowCount=0;
		emissionBuffer="";
		return buffer.toString();
	}

	private String repeat(char charToRepeat,int count) {
		StringBuffer buffer=new StringBuffer();
		for(int j=0;j<count;j++){
			buffer.append(charToRepeat);						
		}
		return buffer.toString();
	}

	private char not(char bit) {
		return (bit=='1')?'0':'1';
	}
	
	/**
	 * Emite el piso del rango
	 */
	public void emitEnding(){
		emissionBuffer+=this.floor;
	}

}
