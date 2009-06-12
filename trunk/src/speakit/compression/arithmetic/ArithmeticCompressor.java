package speakit.compression.arithmetic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;

import speakit.TextDocument;

public class ArithmeticCompressor implements BitWriter {

	private int				precision;
	private OutputStream	outStream;

	public ArithmeticCompressor(OutputStream outputStream) {
		this.outStream = outputStream;
		this.writer = new StreamBitWriter(outputStream);

		precision = 32;
	}

	public void decompress(InputStream compressedFile) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(outStream);

		ArithmeticDecoder decoder = new ArithmeticDecoder(new StreamBitReader(compressedFile), precision);
		ProbabilityTable table = createInitialTable();

		Symbol decodedSymbol = null;
		int verificationFileIndex = 0;
		do {
			decodedSymbol = decoder.decode(table);
			if (verificationFile != null && decodedSymbol.getNumber()>=0) {
				if (decodedSymbol.getChar() != this.verificationFile.charAt(verificationFileIndex)) {
					throw new RuntimeException("Se esperaba " + this.verificationFile.charAt(verificationFileIndex) + " pero vino " + decodedSymbol);
				}
			}
			verificationFileIndex++;
			table.increment(decodedSymbol);
			if (!decodedSymbol.equals(Symbol.getEof())) {
				writer.write(decodedSymbol.getChar());
			}
		} while (!decodedSymbol.equals(Symbol.getEof()));
		writer.flush();
	}

	/**
	 * Lee los datos de in y lo escribe comprimido en out
	 * 
	 * @throws IOException
	 */
	public void compress(TextDocument document) throws IOException {
		ArithmeticEncoder encoder = new ArithmeticEncoder(this, precision);
		ProbabilityTable table = createInitialTable();
		Reader in = new BufferedReader(new StringReader(document.getText()));
		int ch;
		while ((ch = in.read()) > -1) {
			Symbol symbol = new Symbol(ch);
			encoder.encode(symbol, table);
			table.increment(symbol);
		}
		encoder.encode(Symbol.getEof(), table);
		table.increment(Symbol.getEof());
		in.close();
	}

	private ProbabilityTable createInitialTable() {
		ProbabilityTable table;
		table = new ProbabilityTable();
		table.initAllSymbols();
		return table;
	}

	private BitWriter	writer;
	private String		verificationFile;

	@Override
	public void write(String bits) throws IOException {
		writer.write(bits);
	}

	public void setVerificationFile(String verificationFile) {
		this.verificationFile = verificationFile;

	}
}
