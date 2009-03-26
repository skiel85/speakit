package speakit;

import java.io.IOException;

import datos.capturaaudio.exception.SimpleAudioRecorderException;

public class Main {
	/**
	 * Lanza el programa principal
	 * @param args
	 * @throws SimpleAudioRecorderException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws SimpleAudioRecorderException, IOException {
		Menu menu = new Menu();
		menu.start();
	}
	
}
