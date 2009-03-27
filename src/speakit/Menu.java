package speakit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import speakit.audio.Audio;
import speakit.audio.AudioManager;
import datos.capturaaudio.exception.SimpleAudioRecorderException;

/**
 * Clase Encargada de manejar la interaccion con el usuario. Es la vista de la
 * clase Speakit Conoce las librerias de audio
 */
public class Menu  {
	protected AudioManager audioManager;
	private Speakit speakit;

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
		TextDocument textDocumentFromFile = speakit
				.getTextDocumentFromFile(path);

		for (String unknownWord:this.speakit.addDocument(textDocumentFromFile)) {
			WordAudio audio = getAudio(unknownWord);
			speakit.addWordAudio(audio);
		}
		System.out.println("El documento fu� agregado con �xito.");
	}

	String pathCache = "1.txt";
	private BufferedReader userInput;

	/**
	 * Despliega el menu para pedir un path
	 * 
	 * @param this.userInput
	 * @throws IOException
	 * @throws SimpleAudioRecorderException
	 */
	private String displayReadFilePath() throws IOException {
		String path = "";

		System.out.println("Ingrese el path a continuaci�n:");
		if (isPathCacheAvaliable()) {
			System.out.println("( Si su archivo es '" + this.pathCache
					+ "' solo presione ENTER)");
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
		TextDocument textDocumentFromFile = speakit
				.getTextDocumentFromFile(path);
		
		WordAudioDocument audioDocument = this.speakit
		.convertToAudioDocument(textDocumentFromFile);
		for (WordAudio word : audioDocument) {
			System.out.println("Reproduciendo:" + word.getWord());
			this.playSound(word.getAudio());
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
	private boolean confirmateAudioWord(WordAudio wordAudio)
			throws IOException, SimpleAudioRecorderException {
		
		System.out.println("Verifique la palabra '" + wordAudio.getWord()
				+ "'.");
		System.out.println("Reproduciendo...");
		// Reproduzco el audio grabado

		playSound(wordAudio.getAudio());

		System.out.println("Si est� conforme, presione ENTER.\n"
				+ "De lo contrario presione 'n' e ingresela nuevamente.");
		while (true) {
			String line = this.userInput.readLine();
			if(line.length() ==0){
				return true;
			}else{
				return false;
			}
		}
	}

	public void playSound(Audio audio) {
		// TODO hacer algo con la duracion
		audioManager.play(audio.getBytes());
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
	private Audio recordAudio() throws IOException,
			SimpleAudioRecorderException {
		System.out.println("	Presione ENTER para iniciar la captura del audio y ENTER para finalizarla.");
		this.userInput.readLine();
		audioManager.startRecording(); 
		System.out.println("Grabando nueva palabra. "
				+ "Presione ENTER para finalizar.");
		this.userInput.readLine();
		byte[] bytes = audioManager.stopRecording();
		Audio audio = new Audio(bytes, 0L);
		System.out.println("Grabaci�n finalizada.");
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
				System.out.println("Se ha detectado una nueva palabra.\n"
						+ "	La palabra '" + word + "'"
						+ " no se encuentra registrada.\n");
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
		while (true) {
			displayMainMenu();
			try {
				int opt = Integer.parseInt(userInput.readLine());
				switch (opt) {
				case 1:
					addDocument();
					break;

				case 2:
					playTextDocument();
					break;

				case 0:
					System.out.println("Terminado");
					return;
				}
			} catch (IOException e) {
				System.out.println("Error en el men�\n");
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
		System.out.println("Menu Principal\n"
				+ "	1.- Procesar archivo de Texto\n"
				+ "	2.- Reproducir Archivo\n" + "\n" + "	0.- Salir");
	}

//	@Deprecated
//	public void setSpeakit(Speakit speakit) {
//		this.speakit = speakit;
//		this.speakit.setObserver(this);
//	}

//	@Override
//	public void notifyAlreadyHaveIt(String word) {
//		System.out.println("Palabra ya indexada: " + word);
//	}

}