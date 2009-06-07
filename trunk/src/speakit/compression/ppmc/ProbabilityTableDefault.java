package speakit.compression.ppmc;

import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;




public class ProbabilityTableDefault extends ProbabilityTable {

	int maxCharNumber;
	/**
	 */
	public ProbabilityTable exclude(ProbabilityTable table){
		
		return new ProbabilityTableDefault(maxCharNumber-table.getSymbolsQuantity());
	}


	/**
	 */
	public ProbabilityTableDefault(int maxCharNumber){
		this.maxCharNumber=maxCharNumber;
	}



	/**
	 */
	public Float getProbabilityOf(Symbol symbol){
		return new Float(1/maxCharNumber);
	}

}
