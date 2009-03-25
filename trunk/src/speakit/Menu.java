package speakit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import speakit.audio.AudioManager;
import datos.capturaaudio.exception.SimpleAudioRecorderException;

/**
 * Clase Encargada de manejar la interaccion con el usuario. Es la vista de la
 * clase Speakit Conoce las librerias de audio
 */
public class Menu implements SpeakitObserver {
	protected AudioManager audioManager;
	private Speakit speakit;

	public Menu() {
		audioManager = new AudioManager();
	}

	/**
	 * Despliega el menu para procesar los archivos de texto
	 * 
	 * @param entrada
	 */
	private void displayReadTextSubMenu(BufferedReader entrada) {
		System.out.println("Leer archivo de Texto\n");
		String path;
		try {
			path = displayReadFilePath(entrada);
			try {
				this.speakit.processFile(path);
			} catch (CannotProccessFileSpeakitException e) {
				System.out.println("No es posible procesar el archivo " + path);
			}
		} catch (IOException e1) {
			System.out.println("Error en la lectura del archivo.");
		}
	}

	String pathCache = "1.txt";

	/**
	 * Despliega el menu para pedir un path
	 * 
	 * @param entrada
	 * @throws IOException
	 * @throws SimpleAudioRecorderException
	 */
	private String displayReadFilePath(BufferedReader entrada)
			throws IOException {
		String path = "";

		System.out.println("Ingrese el path a continuación:");
		if (isPathCacheAvaliable()) {
			System.out.println("( Si su archivo es '" + this.pathCache
					+ "' solo presione ENTER)");
		}
		path = entrada.readLine();

		if (isPathCacheAvaliable() &&  path.length()==0) {
			path = pathCache;
		} else {
			pathCache = path;
		}
		return path;
	}

	private boolean isPathCacheAvaliable() {
		return (!"".equals(pathCache) && pathCache != null);
	}

	/**
	 * Despliega el menu para la reproduccion de los archivos.
	 * 
	 * @param entrada
	 * @throws IOException
	 */
	private void displayPlayFileSubMenu(BufferedReader entrada)
			throws IOException {
		int opt;
		System.out.println("Reproducir Archivo\n" + "	1.- Reproducir Archivo\n"
				+ "\n" + "	0.- Volver");
		opt = Integer.parseInt(entrada.readLine());
		switch (opt) {
		case 1:
			String path = displayReadFilePath(entrada);
			try {
				this.speakit.readFile(path);
			} catch (CannotProccessFileSpeakitException e) {
				System.out.println("No es posible reproducir el archivo "
						+ path);
			}
			break;
		case 0:
			break;
		}

	}

	/**
	 * confirma que el contenido del audio sea correcto, si no lo es, vuelve a
	 * grabarlo
	 * 
	 * @param word
	 *            la palabra en formato texto
	 * @param oldAudioWord
	 *            la grabacion a confirmar
	 * @param entrada
	 *            La consola para escribir
	 * @return La nueva palabra en audio o la anterior de confirmarse
	 * @throws IOException
	 * @throws SimpleAudioRecorderException
	 */
	private byte[] confirmateAudioWord(String word, byte[] oldAudioWord,
			BufferedReader entrada) throws IOException,
			SimpleAudioRecorderException {
		char option;
		System.out.println("Verifique la palabra '" + word + "'.");
		System.out.println("Reproduciendo...");
		// Reproduzco el audio grabado

		playSound(oldAudioWord);

		System.out.println("Si está conforme, presione ENTER.\n"
				+ "De lo contrario presione 'n' e ingresela nuevamente.");
		while (true) {
			String line = entrada.readLine();
			if (line.length() == 1) {
				option = entrada.readLine().charAt(0);
			} else {
				option = 's';
			}

			switch (option) {
			case 's':
			case 'S':
				return oldAudioWord;
			case 'n':
			case 'N':
				// Inicio nuevamente la captura
				return getAudio(word);
			}
		}
	}

	public void playSound(byte[] sound) {
		audioManager.play(sound);
	}

	/**
	 * Inicia la obtencion de una palabra en formato audio y muestra la opcion
	 * de detener
	 * 
	 * @param entrada
	 *            la consola donde escribir
	 * @return la palabra en formato audio
	 * @throws IOException
	 * @throws SimpleAudioRecorderException
	 */
	private byte[] startRecording(BufferedReader entrada) throws IOException,
			SimpleAudioRecorderException {
		audioManager.startRecording();
		// Inicio grabacion
		while (true) {
			entrada.readLine();
			// recorder.stopRecording();
			byte[] audioWord = audioManager.stopRecording();
			System.out.println("Grabación finalizada.");
			return audioWord;
		}

	}

	/**
	 * Dada la representacion en texto de la palabra, se procede a obtener el
	 * audio
	 * 
	 * @param word
	 * @return
	 * @throws IOException
	 * @throws IOException
	 * @throws SimpleAudioRecorderException
	 */
	@Override
	public byte[] getAudio(String word) {
		try {
			boolean recording = true;
			byte[] newAudioWord = null;
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader entrada = new BufferedReader(isr);
			System.out
					.println("Se ha detectado una nueva palabra.\n"
							+ "	La palabra '"
							+ word
							+ "'"
							+ " no se encuentra registrada.\n"
							+ "	Presione ENTER para iniciar la captura del audio y ENTER para finalizarla.");
			while (recording) {
				entrada.readLine();

				System.out.println("Grabando nueva palabra. "
						+ "Presione ENTER para finalizar.");
				newAudioWord = startRecording(entrada);
				recording = false;

			}
			newAudioWord = confirmateAudioWord(word, newAudioWord, entrada);
			return newAudioWord;
		} catch (IOException e) {
			return null;
		} catch (SimpleAudioRecorderException e) {
			return null;
		}
	}

	/**
	 * Despliega el menu principal
	 * 
	 * @throws SimpleAudioRecorderException
	 */
	@Override
	public void start() {
		// try {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader entrada = new BufferedReader(isr);
		while (true) {

			System.out.println("Speak It!");
			int opt;
			System.out.println("Menu Principal\n"
					+ "	1.- Procesar archivo de Texto\n"
					+ "	2.- Reproducir Archivo\n" + "\n" + "	0.- Salir");
			try {
				opt = Integer.parseInt(entrada.readLine());
				switch (opt) {
				case 1:
					displayReadTextSubMenu(entrada);
					break;

				case 2:
					displayPlayFileSubMenu(entrada);
					break;

				case 0:
					System.out.println("Terminado");
					return;
				}
			} catch (IOException e) {
				System.out.println("Error en el menú\n");
			}
		}
	}

	public void setSpeakit(Speakit speakit) {
		this.speakit = speakit;
		this.speakit.setObserver(this);
	}

	@Override
	public void notifyAlreadyHaveIt(String word) {
		System.out.println("Palabra ya indexada: " + word);
	}

}
