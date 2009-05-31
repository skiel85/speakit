package speakit.compression.ppmc;

import java.util.Map;


public class ProbabilityTable {

	/**
	 * @uml.property  name="symbolOccurrence"
	 */
	private Map<Symbol, Integer> symbolOccurrence;

	/**
	 * Getter of the property <tt>symbolOccurrence</tt>
	 * @return  Returns the symbolOccurrence.
	 * @uml.property  name="symbolOccurrence"
	 */
	public Map<Symbol, Integer> getSymbolOccurrence() {
		return symbolOccurrence;
	}

	/**
	 * Setter of the property <tt>symbolOccurrence</tt>
	 * @param symbolOccurrence  The symbolOccurrence to set.
	 * @uml.property  name="symbolOccurrence"
	 */
	public void setSymbolOccurrence(Map<Symbol, Integer> symbolOccurrence) {
		this.symbolOccurrence = symbolOccurrence;
	}

		
		/**
		 */
		public void getProbability(Symbol symbol){
		}

			
		/**
		 */
		public ProbabilityTable exclude(ProbabilityTable table){
			return null;
		}

				
		/**
		 */
		public void add(Symbol symbol){
		}

					
		/**
		 */
		public Float getProbabilityOf(Symbol symbol){
			return null;
		}

		/**
		 */
		public Float getProbabilityUntil(Symbol symbol){
			return null;
		}
						
		/**
		 */
		public Symbol getSymbolFor(Float probabity){
			return null;
		}

							
		/**
		 */
		public boolean has(Symbol symbol){
			return false;	
		}

	
}
