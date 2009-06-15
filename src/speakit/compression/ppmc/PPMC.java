package speakit.compression.ppmc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
import speakit.compression.lzp.TextDocumentInterpreter;


public class PPMC implements BitWriter{

	//private ProbabilityTableDefault ModelMinusOne;
	private ProbabilityTable ModelMinusOne;
	private HashMap<Context, ProbabilityTable> tables;
	private Integer contextSize;
	private String infoEntry;
	private int ENCODER_PRECISION = 32;
	private OutputStream outStream;
	private BitWriter writer;

	
	public PPMC(OutputStream outputStream, Integer contextSize) {
		this.outStream=outputStream;
		this.contextSize=contextSize;
		this.ModelMinusOne=new ProbabilityTable(); 
		this.ModelMinusOne.initAllSymbols();
		this.tables=new HashMap<Context, ProbabilityTable>();
		this.writer = new StreamBitWriter(outStream);
	}
	
	protected ProbabilityTable getTable(Context context){
		if (tables.containsKey(context)) {
			return tables.get(context);
		} else {
			ProbabilityTable table = new ProbabilityTable();

			//Agrego el ESC con probabilidad 1 a la tabla
			table.increment(Symbol.getEscape());


			tables.put(context, table);
			return table;
		}	
	}


	public void compress(TextDocument document) throws IOException {
		ProbabilityTable table = null;
		ProbabilityTable table2 = null;

		SpeakitLogger.deactivate();
		ArithmeticEncoder encoder = new ArithmeticEncoder(this, ENCODER_PRECISION);

		TextDocumentInterpreter interpreter = new TextDocumentInterpreter(document);

		while (interpreter.hasData()) {
			Symbol sym = interpreter.getActualSymbol();
			Context ctx = interpreter.getContext(this.getContextSize());
	
			SpeakitLogger.activate();
			prepareInfoEntry("Caracter Actual: '" + interpreter.getActualSymbol().toString() + "'");
			prepareInfoEntry("Contexto Actual: '" + ctx.toString() + "'\n");
			SpeakitLogger.deactivate();

			encodeSymbol(new EncoderEmitter(encoder), ctx, sym);
			
			SpeakitLogger.activate();

			Set<Context> contexts = this.tables.keySet();

			for (Context context2 : contexts) {
				prepareInfoEntry("La tabla de probabilidades del contexto '" + context2.toString() + "' quedó: \n" + this.getTable(context2).toString2() + "\n");
			}

			logInfoEntry();
			interpreter.advance();
			SpeakitLogger.deactivate();
		}

	}
	
	private void encodeSymbol(Emitter emitter, Context context, Symbol sym) throws IOException {
		// Obtengo la tabla del contexto actual
		ProbabilityTable table;
		if(context != null) {
			table = this.getTable(context);
		} else {
			table = this.ModelMinusOne;
		}

		// Codifico con la tabla actual.
		boolean foundInModels = emitter.emitSymbol(table, sym);

		// Si no encuentro el símbolo en esa tabla:
		if (!foundInModels) {
			// Emito un escape
			if (context != null) {
				if (table.getSymbolsQuantity() != 1) {
					table.increment(Symbol.getEscape());
				}
			}

			// Busco en el contexto siguiente
			if (context.size() > 0) {
				encodeSymbol(emitter, context.subContext(context.size() - 1), sym);
			} else {
				encodeSymbol(emitter, null, sym);
			}
		}
		
		// Incremento el símbolo
		if (context != null) {
			table.increment(sym);
		}

	}

	@Deprecated
	private boolean emitSymbol(ProbabilityTable table, ArithmeticEncoder encoder, Symbol sym) throws IOException {
		boolean foundInTable = false;
		if (table.contains(sym)) {
			// Emito el caracter y actualizo la probabilidad del
			// caracter en este contexto
			foundInTable = true;
		} else {
			// Emito un escape
			sym = Symbol.getEscape();
		}
		prepareInfoEntry(sym.toString() + "[" + table.getProbability(sym) + "]");
		SpeakitLogger.activate();
		encoder.encode(sym, table);
		SpeakitLogger.deactivate();
		return foundInTable;
	}

	private ProbabilityTable getTableWithExclusion(ProbabilityTable table,
			ProbabilityTable table2, Symbol actualSymbol) {
		//Utilizo el mecanismo de exclusión sobre la tabla del contexto anterior
		ProbabilityTable tableWithEscape = new ProbabilityTable();
		ProbabilityTable tableToExclude = new ProbabilityTable();
		tableWithEscape.increment(Symbol.getEscape());
		
		tableToExclude=table.exclude(tableWithEscape);
		
		// Incremento la probabilidad de los caracteres en el contexto actual
		if(!tableToExclude.contains(actualSymbol)) table.increment(actualSymbol);
		if (table.getSymbolsQuantity()!=2) table.increment(Symbol.getEscape());
		
		table=table2.exclude(tableToExclude);
		return table;
	}

	public ProbabilityTable getModelMinusOne() {
		return ModelMinusOne;
	}


	public void setModelMinusOne(ProbabilityTableDefault modelMinusOne) {
		ModelMinusOne = modelMinusOne;
	}


	public HashMap<Context, ProbabilityTable> getTables() {
		return tables;
	}


	public void setModels(HashMap<Context, ProbabilityTable> tables) {
		this.tables = tables;
	}


	public void setContextSize(Integer contextSize) {
		this.contextSize = contextSize;
	}


	public Integer getContextSize() {
		return contextSize;
	}

	private void prepareInfoEntry(String info) {
		infoEntry += "\t" + info;
	}
	private void logInfoEntry() {
		SpeakitLogger.Log(infoEntry);
		infoEntry = "";
	}


	@Override
	public void write(String bits) throws IOException {
		writer.write(bits); 
	}

	public void decompress(InputStream compressedFile) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(outStream);

		ArithmeticDecoder decoder = new ArithmeticDecoder(new StreamBitReader(compressedFile), ENCODER_PRECISION);
		StringBuilder originalDocument = new StringBuilder("");
		int positionOnDocument = 0;

		Context context = new Context(this.contextSize);
		// SpeakitLogger.activate();

		Symbol decodedSymbol = null;

		do {
			ProbabilityTable table = null;
			ProbabilityTable table2 = null;
			boolean foundInModels = false;

			table = this.getTable(context);

			ArrayList<Context> contextsToUpdate = new ArrayList<Context>();
			//////
			while (!foundInModels && context.size() > 0) {
				SpeakitLogger.activate();
				decodedSymbol = decoder.decode(table);
				SpeakitLogger.deactivate();
				if (!decodedSymbol.equals(Symbol.getEscape())) {
					// Si el caracter no es un ESC, escribo el caracter,
					// actualizo la tabla de probabilidades y rearmo el contexto
					writer.write(decodedSymbol.getChar());
					originalDocument.append(decodedSymbol.getChar());

					updateContexts(contextsToUpdate, decodedSymbol);

					table.increment(decodedSymbol);
					foundInModels = true;
				} else {
					// Si el caracter es un ESC, acorto el contexto y actualizo
					// la probabilidad del escape, si corresponde

					table = this.getTable(context);

					contextsToUpdate.add(context);
					context = context.subContext(context.size() - 1);

				}

				// Exclusion!
				/*
				 * table2=this.getTable(context);
				 * 
				 * 
				 * if(!foundInModels){ ProbabilityTable tableWithEscape = new
				 * ProbabilityTable(); ProbabilityTable tableToExclude = new
				 * ProbabilityTable();
				 * tableWithEscape.increment(Symbol.getEscape());
				 * 
				 * tableToExclude=table.exclude(tableWithEscape);
				 * 
				 * table=table2.exclude(tableToExclude);
				 * 
				 * } else {
				 * 
				 * table=table2; }
				 */
				table = this.getTable(context);

			}

			/* COMIENZO Manejo de modelo 0 y modelo -1 */
			if (!foundInModels) {

				// Decodifico el caracter en el modelo 0
				SpeakitLogger.activate();
				decodedSymbol = decoder.decode(table);
				SpeakitLogger.deactivate();

				// Si es un escape, paso al modelo -1 y emito el caracter
				if (decodedSymbol.equals(Symbol.getEscape())) {
					// this.ModelMinusOne=this.ModelMinusOne.exclude(table);
					SpeakitLogger.activate();
					decodedSymbol = decoder.decode(this.ModelMinusOne);
					SpeakitLogger.deactivate();
				}

				if (!decodedSymbol.equals(Symbol.getEof())) {
					writer.write(decodedSymbol.getChar());
					originalDocument.append(decodedSymbol.getChar());
				}

				/*
				 * if(!table.contains(decodedSymbol) &&
				 * table.getSymbolsQuantity()!=1) {
				 * table.increment(Symbol.getEscape());
				 * 
				 * } table.increment(decodedSymbol);
				 */
				contextsToUpdate.add(context);
				updateContexts(contextsToUpdate, decodedSymbol);
			}
			//////
			/* FIN Manejo de modelo 0 y modelo -1 */
			context = new Context(this.contextSize);

			// String
			// contextString=originalDocument.substring(originalDocument.length()-this.contextSize-1);

			for (int i = 0; i < originalDocument.length(); i++) {
				context.add(new Symbol(originalDocument.charAt(i)));
			}

			SpeakitLogger.activate();
			prepareInfoEntry("Documento: '" + originalDocument + "'\n");
			Set<Context> lalala = this.getTables().keySet();
			for (Context context2 : lalala) {
				ProbabilityTable tablita = getTable(context2);
				prepareInfoEntry("Probabilidades para el contexto: '" + context2.toString() + "'\n" + tablita.toString2());

				/*
				 * List<Symbol> symbolList=tablita.getSymbols(); for (Symbol
				 * symbol : symbolList) {
				 * prepareInfoEntry(symbol.toString()+":"+"'" +
				 * tablita.getProbability(symbol) + "'\n"); }
				 */

			}

			logInfoEntry();
			SpeakitLogger.deactivate();

		} while (!decodedSymbol.equals(Symbol.getEof()));
		writer.flush();
	}
	
	private void updateContexts(ArrayList<Context> contextsToUpdate, Symbol decodedSymbol){
		
		for (Context context : contextsToUpdate) {
			ProbabilityTable table=this.getTable(context);
			if(!table.contains(decodedSymbol) && table.getSymbolsQuantity()!=1) {
				table.increment(Symbol.getEscape());

			}
			table.increment(decodedSymbol);
		}
		
	}
	
	

}
