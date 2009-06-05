package speakit.compression.arithmetic;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.BitPacker;
import speakit.compression.arithmetic.BitWriter;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class ArithmeticCompressor implements BitWriter {

	private int					precision;
	private final OutputStream	out;
	private final InputStream	in;

	public ArithmeticCompressor(OutputStream out, InputStream in) {
		this.out = out;
		this.in = in;

		precision = 32;
		charSize = 16;
	}

	/**
	 * Lee los datos de in y lo escribe comprimido en out 
	 * @throws IOException
	 */
	public void compress() throws IOException {
		
		
		ArithmeticEncoder encoder = new ArithmeticEncoder(this, precision);
		ProbabilityTable table = new ProbabilityTable();
		table.add(Symbol.getEof(), 1);
		for (int i = 0; i < (Math.pow(2,charSize)); i++) {
			table.add(new Symbol(i), 1);
		}
		
		InputStreamReader isr = new InputStreamReader(in, "UTF8");
        Reader in = new BufferedReader(isr);
        int ch;
        while ((ch = in.read()) > -1) {
        	Symbol symbol = new Symbol(ch);
        	encoder.encode(symbol, table);
        	table.increment(symbol);
        }
        encoder.encode(Symbol.getEof(), table);
        in.close();
	}

	private BitPacker	packer	= new BitPacker();
	private int	charSize;
	
	@Override
	public void write(String bits) throws IOException {
		packer.pack(bits);
		for (Byte eachByte : packer.flush()) {
			out.write(eachByte);
		}
//		System.out.println(bits);
	} 
}
