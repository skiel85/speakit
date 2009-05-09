package speakit;

import java.io.IOException;

import speakit.io.record.RecordSerializationException;
import datos.capturaaudio.exception.SimpleAudioRecorderException;

public class Main {
	/**
	 * Lanza el programa principal
	 * 
	 * @param args
	 * @throws SimpleAudioRecorderException
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	public static void main(String[] args) throws SimpleAudioRecorderException, IOException, RecordSerializationException {
		Menu menu = null;
		if (args.length != 0)
			if (args[0].compareToIgnoreCase("GOD") == 0)
				menu = new Menu(true);
		if (menu == null)
			menu = new Menu();
		menu.start();
	}

}
