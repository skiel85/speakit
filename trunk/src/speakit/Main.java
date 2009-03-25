package speakit;

import datos.capturaaudio.exception.SimpleAudioRecorderException;

public class Main {
	/**
	 * Lanza el programa principal
	 * @param args
	 * @throws SimpleAudioRecorderException
	 */
	public static void main(String[] args) throws SimpleAudioRecorderException {
		Speakit speakit = new Speakit();
		Menu menu = new Menu();
		menu.setSpeakit(speakit);
		speakit.launch();
	}
	
}
