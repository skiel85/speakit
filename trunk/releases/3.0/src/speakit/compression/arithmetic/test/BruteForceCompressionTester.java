package speakit.compression.arithmetic.test;

import java.io.IOException;

import org.junit.Assert;

import speakit.compression.arithmetic.AritmethicCompressionException;
import speakit.compression.arithmetic.Symbol;

public class BruteForceCompressionTester {
	
	private int	maxLength;
	private final SingleCompressionTester	tester;
	private final boolean	failOnIncorrectTest;

	public BruteForceCompressionTester(int maxLength, SingleCompressionTester tester,boolean failOnIncorrectTest){
		this.tester = tester;
		this.maxLength=maxLength;
		this.failOnIncorrectTest = failOnIncorrectTest;
	}
	
	public void runTests() throws IOException {
		maxLength = 5;
		for (int i = 0; i < maxLength; i++) {
			testCompressionRecursively("", i, 0);
		}
	}

	private String symbolFileToString(String file) {
		String result = "";
		for (int i = 0; i < file.length(); i++) {
			result += new Symbol(file.charAt(i));
		}
		return result;
	}
	
	private void testCompressionRecursively(String baseFile, int deepToTest, int currentDeep) throws IOException {
		Symbol first = new Symbol('a');
		Symbol last = new Symbol('c');
		for (Symbol symbol = first; symbol.compareTo(last) <= 0; symbol = symbol.next()) {
			Character current = new Character(symbol.getChar());
			String currentFile = baseFile + current;
			if (currentDeep < deepToTest) {
				testCompressionRecursively(currentFile, deepToTest, currentDeep + 1);
			} else {
				try {
					System.out.println("Comprimiendo: " + symbolFileToString(currentFile));
					tester.testCompress(currentFile);
				} catch (AritmethicCompressionException ex) {
					if(failOnIncorrectTest){
						Assert.fail("Error en la compresion de " + "\"" + symbolFileToString(currentFile) + "\": " + ex.getMessage());	
					}else{
						System.out.println("Error en la compresion de " + "\"" + symbolFileToString(currentFile) + "\": " + ex.getMessage());						
					}
				}
			}
		}
	}
}
