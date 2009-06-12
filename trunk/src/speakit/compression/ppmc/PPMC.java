package speakit.compression.ppmc;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;

import speakit.SpeakitLogger;
import speakit.TextDocument;
import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.BitWriter;
import speakit.compression.arithmetic.Context;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;
import speakit.compression.lzp.TextDocumentInterpreter;


public class PPMC implements BitWriter{

	//private ProbabilityTableDefault ModelMinusOne;
	private ProbabilityTable ModelMinusOne;
	private HashMap<Context, ProbabilityTable> tables;
	private Integer contextSize = 2;
	private String infoEntry;
	private int ENCODER_PRECISION = 32;
	private OutputStream outStream;
	private BitWriter writer;

	
	public PPMC(ByteArrayOutputStream out) {
		this.outStream=out;
		this.ModelMinusOne=new ProbabilityTable(); 
		this.ModelMinusOne.initAllSymbols();
		this.tables=new HashMap<Context, ProbabilityTable>();
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


	public void compress(TextDocument document) throws IOException{
		ProbabilityTable table = null;
		ProbabilityTable table2 = null;
		
		SpeakitLogger.deactivate();
		ArithmeticEncoder encoder = new ArithmeticEncoder(this, ENCODER_PRECISION);
		

		TextDocumentInterpreter interpreter = new TextDocumentInterpreter(document);
		try {
			String emision="";

			Context context = null;

			while (interpreter.hasData()) {

				// Contexto para el modelo 0
				if (interpreter.getCurrentPosition()==0) {
					context=interpreter.getContext(0);

				} else {
					//Contexto para el modelo 1
					if (interpreter.getCurrentPosition()==1){
						context = interpreter.getContext(1);
					}
					//Contexto para el resto de los modelos
					else{
						context=interpreter.getContext(this.getContextSize());
					}
				}
				SpeakitLogger.activate();
				prepareInfoEntry("Caracter Actual: '" + interpreter.getActualSymbol().toString() + "'");
				prepareInfoEntry("Contexto Actual: '" + context.toString() + "'\n");
				SpeakitLogger.deactivate();

				table=this.getTable(context);

				boolean foundInModels=false;

				while (!foundInModels && context.size()>0){
					if (table.contains(interpreter.getActualSymbol())){
						//Emito el caracter y actualizo la probabilidad del caracter en este contexto
						
						emision+=interpreter.getActualSymbol().toString()+"["+table.getProbability(interpreter.getActualSymbol())+"]";
						encoder.encode(interpreter.getActualSymbol(), table);

						//table.increment(interpreter.getActualSymbol());
						foundInModels=true;
					}else{
						//Emito un escape
						
						emision+=Symbol.getEscape().toString()+"["+table.getProbability(Symbol.getEscape())+"]";
						encoder.encode(Symbol.getEscape(), table);
						
						table=getTable(context);

						
					}
					
					//Obtengo el subcontexto para chequear en el modelo anterior
					context=context.subContext(context.size()-1);
					table2=this.getTable(context);
					
					if(!foundInModels){
						//Utilizo el mecanismo de exclusión sobre la tabla del contexto anterior
						ProbabilityTable tableWithEscape = new ProbabilityTable();
						ProbabilityTable tableToExclude = new ProbabilityTable();
						tableWithEscape.increment(Symbol.getEscape());
						
						tableToExclude=table.exclude(tableWithEscape);
						
						// Incremento la probabilidad de los caracteres en el contexto actual
						if(!tableToExclude.contains(interpreter.getActualSymbol())) table.increment(interpreter.getActualSymbol());
						if (table.getSymbolsQuantity()!=2) table.increment(Symbol.getEscape());
						
						table=table2.exclude(tableToExclude);	
						
					} else {
						table.increment(interpreter.getActualSymbol());
						table=table2;
					}

				}

				if (!foundInModels){

					if (table.contains(interpreter.getActualSymbol())){
						//Emito el caracter en el modelo 0 y actualizo su probabilidad
						emision+=interpreter.getActualSymbol().toString()+"["+table.getProbability(interpreter.getActualSymbol())+"]";
						
						encoder.encode(interpreter.getActualSymbol(), table);

						table.increment(interpreter.getActualSymbol());

					}else{
						//Emito un escape en el modelo 0 y emito el caracter en el modelo -1 
						
						table.getProbability(Symbol.getEscape());
						emision+=Symbol.getEscape().toString()+"["+table.getProbability(Symbol.getEscape())+"]";

						encoder.encode(Symbol.getEscape(), table);

						//Excluyo el Modelo 0 del modelo -1, antes de emitir
						table=getTable(context);
						
						this.ModelMinusOne=this.ModelMinusOne.exclude(table);
						
						//Incremento las probabilidades del caracter en el modelo 0
						
						table.increment(interpreter.getActualSymbol());

						if (table.getSymbolsQuantity()!=2)table.increment(Symbol.getEscape());

						//Emito el caracter en el modelo -1
						
						emision+=interpreter.getActualSymbol().toString()+"["+this.ModelMinusOne.getProbability(interpreter.getActualSymbol())+"]";

						encoder.encode(interpreter.getActualSymbol(), this.ModelMinusOne);

						//encoder.encode(interpreter.getActualSymbol(), ((ProbabilityTableDefault)this.ModelMinusOne.exclude(table)));

					}
				}
				SpeakitLogger.activate();
				prepareInfoEntry("Emito: '"+emision+"' \n");

				Set<Context> contexts = this.tables.keySet();

				for (Context context2 : contexts) {
					prepareInfoEntry("La tabla de probabilidades del contexto '"+context2.toString()+"' quedó: \n" + this.getTable(context2).toString2()+"\n");
				}
				
				emision="";
				logInfoEntry();
				interpreter.advance();
				SpeakitLogger.deactivate();
			}

			//Emito el EOF
			//encoder.encode(Symbol.getEof(), this.getTable(context));

		} catch (Exception e){
			e.printStackTrace();
		}

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
		//writer.write(bits); 
	}

}
