package speakit.compression.arithmetic;


public class Range {

	/**
	 * @uml.property  name="floor"
	 */
	private int floor;

	/**
	 * Getter of the property <tt>floor</tt>
	 * @return  Returns the floor.
	 * @uml.property  name="floor"
	 */
	public int getFloor() {
		return floor;
	}

	/**
	 * Setter of the property <tt>floor</tt>
	 * @param floor  The floor to set.
	 * @uml.property  name="floor"
	 */
	public void setFloor(int floor) {
		this.floor = floor;
	}

	/**
	 * @uml.property  name="roof"
	 */
	private int roof;

	/**
	 * Getter of the property <tt>roof</tt>
	 * @return  Returns the roof.
	 * @uml.property  name="roof"
	 */
	public int getRoof() {
		return roof;
	}

	/**
	 * Setter of the property <tt>roof</tt>
	 * @param roof  The roof to set.
	 * @uml.property  name="roof"
	 */
	public void setRoof(int roof) {
		this.roof = roof;
	}

		
	/**
	 */
	public Range zoomIn(Float accumulatedProbability, Float probability){
		//Ajustar piso y techo
		return this;
	}

			
	/**
	 */
	public int getOverflow(){
		return 0;
	}

				
	/**
	 */
	public int getFinal(){
		return 0;
	}

}
