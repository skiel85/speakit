package speakit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import speakit.audio.Audio;
import speakit.audio.AudioManager;
import speakit.audio.AudioManagerException;
import speakit.dictionary.files.audiofile.WordNotFoundException;
import datos.capturaaudio.exception.SimpleAudioRecorderException;

/**
 * Clase Encargada de manejar la interaccion con el usuario. Es la vista de la
 * clase Speakit Conoce las librerias de audio
 */
public class Menu {
	protected AudioManager audioManager;
	private SpeakitInterface speakit;

	public Menu() {
		audioManager = new AudioManager();
		speakit = new Speakit();
	}

	/**
	 * Despliega el menu para procesar los archivos de texto
	 * 
	 * @throws IOException
	 * 
	 * @throws WordNotFoundException
	 */
	private void addDocument() throws IOException {
		System.out.println("Leer archivo de Texto\n");
		String path;
		path = displayReadFilePath();
		Iterable<String> wordIterable;
		try {
			wordIterable = this.speakit.addDocument(speakit.getTextDocumentFromFile(path));
		} catch (FileNotFoundException fnf) {
			showFileNotFoundMessage(path);
			return;
		}

		if (wordIterable.iterator().hasNext()) {
			System.out.println("El documento contiene palabras desconocidas, que deber� grabar a continuaci�n.");
		}
		for (String unknownWord : wordIterable) {
			WordAudio audio = getAudio(unknownWord);
			if (audio != null && audio.getAudio() != null) {
				speakit.addWordAudio(audio);
			}
		}
		System.out.println("El documento fu� agregado con �xito.");
	}

	private void showFileNotFoundMessage(String path) {
		System.out.println("No pudo encontrarse el archivo '" + path + "'.");
	}

	String pathCache = "1.txt";
	private BufferedReader userInput;

	/**
	 * Pide una ruta de archivo al usuario.
	 * 
	 * @throws IOException
	 * @throws SimpleAudioRecorderException
	 */
	private String displayReadFilePath() throws IOException {
		String path = "";

		System.out.println("Ingrese la ruta a continuaci�n:");
		if (isPathCacheAvaliable()) {
			System.out.println("(Si su archivo es '" + this.pathCache + "' s�lo presione ENTER)");
		}
		path = this.userInput.readLine();

		if (isPathCacheAvaliable() && path.length() == 0) {
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
	 * @param this.userInput
	 * @throws IOException
	 */
	private void playTextDocument() throws IOException {
		String path = displayReadFilePath();
		TextDocument textDocumentFromFile;
		try {
			textDocumentFromFile = speakit.getTextDocumentFromFile(path);
		} catch (FileNotFoundException fnf) {
			showFileNotFoundMessage(path);
			return;
		}

		WordAudioDocument audioDocument = this.speakit.convertToAudioDocument(textDocumentFromFile);
		while(audioDocument.hasNext()){
			this.playSound(audioDocument.next());
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
	 * @param this.userInput La consola para escribir
	 * @return La nueva palabra en audio o la anterior de confirmarse
	 * @throws IOException
	 * @throws SimpleAudioRecorderException
	 */
	private boolean confirmateAudioWord(WordAudio wordAudio) throws IOException, SimpleAudioRecorderException {

		System.out.print("Reproduciendo...");
		// Reproduzco el audio grabado

		playSound(wordAudio);

		System.out.println("(ENTER para confirmar. N para volver a grabar)");
		while (true) {
			String line = this.userInput.readLine();
			if (line.length() == 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void playSound(WordAudio wordAudio) {
		if (wordAudio.getAudio() != null) {
			System.out.println("Reproduciendo: " + wordAudio.getWord());
			// TODO hacer algo con la duracion
			audioManager.play(wordAudio.getAudio().getBytes());
		}
	}

	/**
	 * Inicia la obtencion de una palabra en formato audio y muestra la opcion
	 * de detener
	 * 
	 * @param this.userInput la consola donde escribir
	 * @return la palabra en formato audio
	 * @throws IOException
	 * @throws SimpleAudioRecorderException
	 */
	private Audio recordAudio() throws IOException, SimpleAudioRecorderException {
		Audio audio = null;
		try {
			audioManager.startRecording();
			System.out.println("Grabando... " + "(ENTER para detener).");
			this.userInput.readLine();
			byte[] bytes = audioManager.stopRecording();
			audio = new Audio(bytes);
		} catch (AudioManagerException e) {
			System.out.println("No se puede grabar el audio");
		}
		return audio;
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
	public WordAudio getAudio(String word) {
		try {
			WordAudio newAudioWord = null;
			do {
				Audio audio = null;
				System.out.println("Palabra '" + word + "'. (ENTER para grabar).");
				this.userInput.readLine();
				audio = recordAudio();
				newAudioWord = new WordAudio(word, audio);
			} while (!confirmateAudioWord(newAudioWord));

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
	 * @throws IOException
	 * 
	 * @throws SimpleAudioRecorderException
	 */
	public void start() throws IOException {
		speakit.load();

		userInput = initializeUserInput();

		boolean finished = false;
		while (!finished) {
			displayMainMenu();

			int opt = 0;
			try {
				opt = Integer.parseInt(userInput.readLine());
			} catch (IOException e) {
				System.out.println("Error de E/S al leer la consola.");
				continue;
			} catch (NumberFormatException e) {
				System.out.println("Opci�n inv�lida.");
				continue;
			}

			switch (opt) {
			case 1:
				addDocument();
				break;
			case 2:
				playTextDocument();
				break;
			case 0:
				System.out.println("Terminado.");
				finished = true;
				break;
			default:
				System.out.println("Opci�n inv�lida.\n");
				break;
			}
		}
	}

	private BufferedReader initializeUserInput() {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader input = new BufferedReader(isr);
		return input;
	}

	private void displayMainMenu() {
		System.out.println("Speak It!");
		System.out.println("Menu Principal\n" + "	1.- Procesar archivo de Texto\n" + "	2.- Reproducir Archivo\n" + "\n" + "	0.- Salir");
	}
}
