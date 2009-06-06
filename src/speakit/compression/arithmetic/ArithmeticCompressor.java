package speakit.compression.arithmetic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;

import speakit.TextDocument;

public class ArithmeticCompressor implements BitWriter {

	private int					precision;
	private OutputStream outStream;
	
	public ArithmeticCompressor(OutputStream outputStream) {
		this.outStream = outputStream;
		this.writer = new StreamBitWriter(outputStream);

		precision = 32;
		charSize = 16;
	}
	
	public void decompress(Reader compressedFileReader) throws IOException {
		OutputStreamWriter writer= new OutputStreamWriter(outStream, "UTF-8");
		
		ArithmeticDecoder decoder = new ArithmeticDecoder(new BinaryBitReader(compressedFileReader), precision);
		ProbabilityTable table = createInitialTable();
		
		Symbol decodedSymbol=null; 
		do{
			System.out.println("Decode");
			decodedSymbol=decoder.decode(table);
			table.increment(decodedSymbol);
			if(!decodedSymbol.equals(Symbol.getEof())){
				writer.write( decodedSymbol.getChar());	
			}
        }while(!decodedSymbol.equals(Symbol.getEof()));
		writer.flush();
	}

	/**
	 * Lee los datos de in y lo escribe comprimido en out 
	 * @throws IOException
	 */
	public void compress(TextDocument document) throws IOException {
		ArithmeticEncoder encoder = new ArithmeticEncoder(this, precision);
		ProbabilityTable table = createInitialTable();		
        Reader in = new BufferedReader( new StringReader(document.getText()));
        int ch;
        while ((ch = in.read()) > -1) {
        	Symbol symbol = new Symbol(ch);
        	encoder.encode(symbol, table);
        	table.increment(symbol);
        }
        encoder.encode(Symbol.getEof(), table);
        in.close();
	}

	private ProbabilityTable createInitialTable() {
		ProbabilityTable table;
		table = new ProbabilityTable();
		table.add(Symbol.getEof(), 1);
		for (int i = 0; i < (Math.pow(2,charSize)); i++) {
			table.add(new Symbol(i), 1);
		}
		return table;
	}

	private BitWriter writer; 
	private int	charSize;
	
	@Override
	public void write(String bits) throws IOException {
		writer.write(bits);
//		System.out.println(bits);
	} 
}
