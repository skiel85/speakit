package speakit.compression.lzp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import speakit.TextDocument;
import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.BitWriter;
import speakit.compression.arithmetic.Context;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.StreamBitWriter;
import speakit.compression.arithmetic.Symbol;


public class LZP implements BitWriter {
	private static final int ENCODER_PRECISION = 32;
	private Integer MATCH_CONTEXT_SIZE = 2;
	private HashMap<Context, ProbabilityTable> contextTables;
	private ProbabilityTable matchsTable;
	private BitWriter	writer	;
	private final OutputStream	out;
	
	public LZP() {
		contextTables = new HashMap<Context, ProbabilityTable>();
		this.out = System.out;
		this.writer = new StreamBitWriter(this.out);
	}
	
	/**
	 * @throws IOException 
	 */
	public void compress(TextDocument document) throws IOException{
		Integer matchPos = null;
		Integer matchLength = null;
		ProbabilityTable table = null;
		ArithmeticEncoder encoder = new ArithmeticEncoder(this, ENCODER_PRECISION);
		LZPTable lzpTable = new LZPTable();
		TextDocumentInterpreter interpreter = new TextDocumentInterpreter(document);
		while (interpreter.hasData()) {
			Context matchContext = interpreter.getContext(MATCH_CONTEXT_SIZE);
			matchPos = lzpTable.getLastMatchPosition(matchContext);
			matchLength = interpreter.getMatchLength(matchPos);
			Symbol lengthSymbol = new Symbol(matchLength); 
			//encoder.encode(lengthSymbol, getMatchsTable());
			Context releasedContext = interpreter.getContext(1);
			table = getContextTable(releasedContext);
			Symbol actualSymbol = interpreter.getActualSymbol();
			//encoder.encode(actualSymbol, table);
		//actualizo las tablas q use en esta iteracion
			lzpTable.update(matchContext, interpreter.getCurrentPosition());
			getMatchsTable().increment(lengthSymbol);
			table.increment(actualSymbol);
			updateTable(releasedContext, table);
		} 
	}

		
	private void updateTable(Context releasedContext, ProbabilityTable table) {
		// TODO Auto-generated method stub
		
	}

	private ProbabilityTable getMatchsTable() {
		return this.matchsTable;
	}

	/**
	 */
	protected ProbabilityTable getContextTable(Context context){
		if (contextTables.containsKey(context)) {
			return contextTables.get(context);
		} else {
			ProbabilityTable table = new ProbabilityTable();
			contextTables.put(context, table);
			return table;
		}	
	}
	
	
	@Override
	public void write(String bits) throws IOException {
		writer.write(bits);
		//System.out.println(bits);
	}

}
