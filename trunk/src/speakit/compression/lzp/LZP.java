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
	private HashMap<Context, ProbabilityTable> tables;
	private BitWriter	writer	;
	private final OutputStream	out;
	
	public LZP() {
		tables = new HashMap<Context, ProbabilityTable>();
		this.out = System.out;
		this.writer = new StreamBitWriter(this.out);
	}
	
	/**
	 * @throws IOException 
	 */
	public void compress(TextDocument document) throws IOException{
		ProbabilityTable table = null;
		Integer match = null;
		ArithmeticEncoder encoder = new ArithmeticEncoder(this, ENCODER_PRECISION);
		LZPTable lzpTable = new LZPTable();
		TextDocumentInterpreter interpreter = new TextDocumentInterpreter(document);
		while (interpreter.hasData()) {
			Context context = interpreter.getContext(MATCH_CONTEXT_SIZE);
			match = lzpTable.getLastMatchPosition(context);
			table = getTable(context);
			encoder.encode(new Symbol(match), table);
			encoder.encode(interpreter.getActualSymbol(), getTable(interpreter.getContext(1)));
		}
	}

		
	/**
	 */
	protected ProbabilityTable getTable(Context context){
		if (tables.containsKey(context)) {
			return tables.get(context);
		} else {
			ProbabilityTable table = new ProbabilityTable();
			tables.put(context, table);
			return table;
		}	
	}
	
	
	@Override
	public void write(String bits) throws IOException {
		writer.write(bits);
		//System.out.println(bits);
	}

}
