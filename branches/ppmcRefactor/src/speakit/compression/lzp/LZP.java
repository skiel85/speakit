package speakit.compression.lzp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import speakit.SpeakitLogger;
import speakit.TextDocument;
import speakit.compression.arithmetic.ArithmeticDecoder;
import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.BitWriter;
import speakit.compression.arithmetic.Context;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.StreamBitReader;
import speakit.compression.arithmetic.StreamBitWriter;
import speakit.compression.arithmetic.Symbol;
import speakit.compression.lzp.test.TextDocumentBuilder;


public class LZP implements BitWriter {
	private static final int ENCODER_PRECISION = 32;
	private Integer MATCH_CONTEXT_SIZE = 2;
	private HashMap<String, LzpProbabilityTable> contextTables;
	private ProbabilityTable matchsTable;
	private BitWriter	writer	;
	private OutputStream	outStream;
	private String infoEntry;
	

	
	
	public LZP(OutputStream outputStream) {
		contextTables = new HashMap<String, LzpProbabilityTable>();
		this.outStream = outputStream;
		this.writer = new StreamBitWriter(outputStream);
		initTables();
	}
	
	private void initTables() {
		this.matchsTable = new ProbabilityTable();
		this.matchsTable.initAllSymbols();
	}

	/**
	 * @throws IOException 
	 */
	public void compress(TextDocument document) throws IOException{
		Integer matchPos = null;
		Integer matchLength = null;
		LzpProbabilityTable table = null;
		ArithmeticEncoder encoder = new ArithmeticEncoder(this, ENCODER_PRECISION);
		LZPTable lzpTable = new LZPTable();
		TextDocumentInterpreter interpreter = new TextDocumentInterpreter(document);
		while (interpreter.hasData()) {
			Integer currPosition = interpreter.getCurrentPosition();
			prepareInfoEntry("Pos: '" + currPosition.toString() + "'");
			Context matchContext = interpreter.getContext(MATCH_CONTEXT_SIZE);
			prepareInfoEntry("Ctxt Busqueda = '" + matchContext.toString() + "'");
			matchPos = lzpTable.getLastMatchPosition(matchContext);
			prepareInfoEntry("Ult Match = '" + matchPos.toString() + "'");
			matchLength = interpreter.getMatchLength(matchPos);
			prepareInfoEntry("Longitud = '" + matchLength.toString() + "'");
			Symbol lengthSymbol = new Symbol(matchLength); 
			encoder.encode(lengthSymbol, getMatchsTable());
			Context releasedContext = interpreter.getContext(1);
			prepareInfoEntry("Ctxt Compresion = '" + releasedContext.toString() + "'");
			table = getContextTable(releasedContext);
			Symbol actualSymbol = interpreter.getActualSymbol();
			prepareInfoEntry("Char = '" + actualSymbol.toString() + "'");
			encoder.encode(actualSymbol, table.getProbabilityTable());
		//actualizo las tablas q use en esta iteracion
			lzpTable.update(matchContext, currPosition);
			prepareInfoEntry("Actualizo = '" + matchContext.toString() + " pos: " + currPosition + "'");
			updateMatchsTable(lengthSymbol);
			table.increment(actualSymbol);
			updateTable(releasedContext, table);
		//Avanzo en la lectura del archivo
			interpreter.advance();
		//Logueo la info
			logInfoEntry();
		} 
	}
	
	public TextDocument decompress(InputStream compressedFile) throws IOException {
		ArithmeticDecoder decoder = new ArithmeticDecoder(new StreamBitReader(compressedFile), ENCODER_PRECISION);
		TextDocumentBuilder builder = new TextDocumentBuilder();
		Symbol decodedSymbol = null;
		LzpProbabilityTable compressTable = null;
		Context matchContext = null;
		Context compressContext = null;
		LZPTable lzpTable = new LZPTable();
		Integer matchLength = null;
		Integer matchPos = null;
		Integer currentPos = null;
		do {
			currentPos = builder.getPosition() + 1;
			prepareInfoEntry("Pos: '" + currentPos + "'");
			matchContext = builder.getContext(MATCH_CONTEXT_SIZE);
			prepareInfoEntry("Ctxt: '" + matchContext + "'");
			decodedSymbol = decoder.decode(getMatchsTable());
			matchLength = decodedSymbol.getNumber();
			updateMatchsTable(decodedSymbol);
			
			if (decodedSymbol.getNumber() > 0) {
				matchPos = lzpTable.getLastMatchPosition(matchContext);
				prepareInfoEntry("Ult match: '" + matchPos + "'");
				String match = builder.getMatch(matchPos, matchLength);
				builder.add(match);
				prepareInfoEntry("Match: '" + match + "'");
				compressContext = builder.getContext(1);
			} else {
				compressContext = builder.getContext(1);
				prepareInfoEntry("Ult match: '-'");
			}
			prepareInfoEntry("Compr ctx: '" + compressContext + "'");
			compressTable = getContextTable(compressContext);
			decodedSymbol = decoder.decode(compressTable.getProbabilityTable());
			prepareInfoEntry("Char: '" + decodedSymbol + "'");
			builder.add(decodedSymbol);
			
			lzpTable.update(matchContext, currentPos);
			prepareInfoEntry("Actualizo: Ctxt= '" + matchContext + "' pos: '" + currentPos + "'");
			compressTable.increment(decodedSymbol);
			updateTable(compressContext, compressTable);
			prepareInfoEntry("Actual: '" + builder.getDocument().getText());
			logInfoEntry();
		} while (!decodedSymbol.equals(Symbol.getEof()));
		return builder.getDocument();
	}
	
	private void prepareInfoEntry(String info) {
		infoEntry += "\t" + info;
	}
	private void logInfoEntry() {
		SpeakitLogger.Log(infoEntry);
		infoEntry = "";
	}
		
	private void updateTable(Context releasedContext, LzpProbabilityTable table) {
		contextTables.put(releasedContext.toString(), table);
	}

	public ProbabilityTable getMatchsTable() {
		return this.matchsTable;
	}
	
	public void updateMatchsTable(Symbol symbol) {
		this.matchsTable.increment(symbol);
	}

	/**
	 */
	protected LzpProbabilityTable getContextTable(Context context){
		if (contextTables.containsKey(context.toString())) {
			return contextTables.get(context.toString());
		} else {
			LzpProbabilityTable table = new LzpProbabilityTable();
			//table.initAllSymbols();
			contextTables.put(context.toString(), table);
			return table;
		}	
	}
	
	public HashMap<String, LzpProbabilityTable> getTables() {
		return contextTables;
	}
	
	@Override
	public void write(String bits) throws IOException {
		writer.write(bits);
		//System.out.println(bits);
	}

}
