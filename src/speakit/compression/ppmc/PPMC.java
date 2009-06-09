package speakit.compression.ppmc;

import java.io.FileInputStream;
import java.io.FileOutputStream; 
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import speakit.SpeakitLogger;
import speakit.TextDocument;
import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.Context;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;
import speakit.compression.lzp.LZPTable;
import speakit.compression.lzp.TextDocumentInterpreter;


public class PPMC {
	
	private ProbabilityTableDefault ModelMinusOne;
	private HashMap<Context, ProbabilityTable> tables;
	private Integer contextSize = 2;
	private String infoEntry;

		
	public ProbabilityTableDefault getModelMinusOne() {
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


		/**
		 */
		public void compress(FileInputStream input){
		}

			
		/**
		 */
		public PPMC(FileOutputStream outputFile){
		}


			
		public PPMC() {
			this.ModelMinusOne=new ProbabilityTableDefault(65535);
			this.tables=new HashMap<Context, ProbabilityTable>();
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


		public void compress(TextDocument document) throws IOException{
			ProbabilityTable table = null;
			ProbabilityTable table2 = null;
			
			TextDocumentInterpreter interpreter = new TextDocumentInterpreter(document);
			try {
				String emision="";
				
				while (interpreter.hasData()) {
					
					Context context;
					
					// Contexto para el modelo 0
					if (interpreter.getCurrentPosition()==0) {
						//context = interpreter.getContext(0);
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
					
					prepareInfoEntry("Caracter Actual: '" + interpreter.getActualSymbol().toString() + "'");
					prepareInfoEntry("Contexto Actual: '" + context.toString() + "'");
					
					table=this.getTable(context);
					
					boolean foundInModels=false;
					
					while (!foundInModels && context.size()>0){
						if (table.contains(interpreter.getActualSymbol())){
							//Emito el caracter y actualizo la probabilidad del caracter en este contexto
							emision+=interpreter.getActualSymbol().toString()+"("+table.getProbability(interpreter.getActualSymbol())+")";
							table.getProbability(interpreter.getActualSymbol());
							table.increment(interpreter.getActualSymbol());
							foundInModels=true;
						}else{
							//Emito un escape
							if (table.getSymbolsQuantity()==0) table.increment(Symbol.getEscape());
							emision+=Symbol.getEscape().toString()+"("+table.getProbability(Symbol.getEscape())+")";
							table.getProbability(Symbol.getEscape());
							
							table.increment(interpreter.getActualSymbol());
						}
						//Obtengo el subcontexto para chequear en el modelo anterior
						context=context.subContext(context.size()-1);
						table2=this.getTable(context);
							
						//Utilizo el mecanismo de exclusión sobre la tabla del contexto anterior
						//table2.exclude(table);
						table=table2;
						
						
					}
					
					if (!foundInModels){
						
						if (table.contains(interpreter.getActualSymbol())){
							//Emito el caracter en el modelo 0 y actualizo su probabilidad
							emision+=interpreter.getActualSymbol().toString()+"("+table.getProbability(interpreter.getActualSymbol())+")";
							table.getProbability(interpreter.getActualSymbol());
							table.increment(interpreter.getActualSymbol());
							
						}else{
							//Emito un escape en el modelo 0 y emito el caracter en el modelo -1 
							if (table.getSymbolsQuantity()==0) table.increment(Symbol.getEscape());
							table.getProbability(Symbol.getEscape());
							emision+=Symbol.getEscape().toString()+"("+table.getProbability(Symbol.getEscape())+")";
							table.increment(interpreter.getActualSymbol());
							
							//Emito el caracter en el modelo -1, excluyendo los del modelo 0
							emision+=interpreter.getActualSymbol().toString()+"("+((ProbabilityTableDefault)this.ModelMinusOne.exclude(table)).getProbabilityOf(interpreter.getActualSymbol())+")";
							((ProbabilityTableDefault)this.ModelMinusOne.exclude(table)).getProbabilityOf(interpreter.getActualSymbol());
							
						}
					}
					
					prepareInfoEntry("Emito: '"+emision+"' \n");
					
					Set<Context> lalala = this.tables.keySet();
					
					for (Context context2 : lalala) {
						prepareInfoEntry("La tabla de probabilidades del contexto '"+context2.toString()+"' quedó: \n" + this.getTable(context2).toString());
					}
					emision="";
					logInfoEntry();
					interpreter.advance();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			
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

}
