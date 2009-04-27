package speakit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


import speakit.audio.Audio;
import speakit.audio.AudioManager;
import speakit.audio.AudioManagerException;
import speakit.dictionary.audiofile.WordNotFoundException;
import speakit.documentstorage.TextDocumentList;

import speakit.io.record.RecordSerializationException;
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
	 * @throws RecordSerializationException
	 * 
	 * @throws WordNotFoundException
	 */
	private void addDocument() throws IOException, RecordSerializationException {
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
			System.out.println("El documento contiene palabras desconocidas, que deberá grabar a continuación.");
		}
		for (String unknownWord : wordIterable) {
			WordAudio audio = getAudio(unknownWord);
			if (audio != null && audio.getAudio() != null) {
				speakit.addWordAudio(audio);
			}
		}
		System.out.println("El documento fué agregado con éxito.");
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

		System.out.println("Ingrese la ruta a continuación:");
		if (isPathCacheAvaliable()) {
			System.out.println("(Si su archivo es '" + this.pathCache + "' sólo presione ENTER)");
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
		System.out.println("Se va a reproducir el siguiente documento");
		while (audioDocument.hasNext()) {
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
			System.out.print(wordAudio.getWord() + " ");
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
	 * @throws RecordSerializationException
	 * @throws NotFoundDocumentListException
	 * 
	 * @throws SimpleAudioRecorderException
	 */
	public void start() throws IOException, RecordSerializationException {
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
				System.out.println("Opción inválida.");
				continue;
			}

			switch (opt) {
			case 1:
				addDocument();
				break;
			case 2:
				playTextDocument();
				break;
			case 3:
				doConsultation();
				break;
			case 0:
				System.out.println("Terminado.");
				finished = true;
				break;
			default:
				System.out.println("Opción inválida.\n");
				break;
			}
		}
	}
	
	/**
	 * Despliega el menu para realizar una consulta
	 * 
	 * @throws IOException
	 */
	private void doConsultation() throws IOException {
		String consultation = "";
		TextDocumentList documentList;
        
		System.out.println("Ingrese la consulta");
		consultation = this.userInput.readLine();
		TextDocument searchText = new TextDocument(consultation);
		documentList = speakit.search(searchText);
		if (documentList!= null){
			showResults(documentList);
		    showOptions(documentList);
		}
	}

	/**
	 * Muestra los resultados de una consulta
	 * 
	 * @param documentList
	 */
	private void showResults(TextDocumentList documentList) {
		System.out.println("Los documentos encontrados para la consulta" + " " +
				"realizada se muestran a continuacion:");
		int number = 1;
		while(documentList.iterator().hasNext()){
			System.out.println(number + " : " + documentList.iterator().next().getPreview());
			System.out.println();
			number++;
		}
		System.out.println();
	}

	/**
	 * Muestra las opciones luego de realizar una consulta
	 * 
	 * @param documentList
	 */
	private void showOptions(TextDocumentList documentList) throws IOException {
		System.out.println("Si desea reproducir algun documento presione 1");
		System.out.println("Para realizar una nueva consulta presione 2");
		System.out.println("Para ir al menu principal presione 0");
		chooseOption(documentList);
	}

	/**
	 * Permite elegir una opcion
	 * 
	 * @param documentList
	 */
	private void chooseOption(TextDocumentList documentList) throws IOException {
		int opt = 0;
		boolean option = false;

		while (!option) {
			try {
				opt = Integer.parseInt(userInput.readLine());
			} catch (IOException e) {
				System.out.println("Error de E/S al leer la consola.");
				continue;
			} catch (NumberFormatException e) {
				System.out.println("Opción inválida.");
				continue;
			}
			option = true;
		}
		switch (opt) {

		case 1:
			chooseDocumentToPlay(documentList);
			break;
		case 2:
			doConsultation();
			break;
		case 0:
			displayMainMenu();
			break;
		default:
			System.out.println("Opción inválida.\n");
			break;
		}

	}

	/**
	 * Luego de una consulta, permite elegir un documento para reproducir.
	 * 
	 * @param documentList
	 */
	private void chooseDocumentToPlay(TextDocumentList documentList) throws IOException {
		String number = "";
		System.out.println("Elija el numero de documento que desea reproducir");
		number = this.userInput.readLine();
		Integer integer = new Integer(number);
		TextDocument document;
		int counter = 1;
		while((documentList.iterator().hasNext()) && (counter!=integer.intValue())){
			documentList.iterator().next();
			counter++;
		}
		document=documentList.iterator().next();
		WordAudioDocument audioDocument = this.speakit.convertToAudioDocument(document);
		System.out.println("Se va a reproducir el siguiente documento");
		while (audioDocument.hasNext()) {
			this.playSound(audioDocument.next());
		}
	}

	private BufferedReader initializeUserInput() {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader input = new BufferedReader(isr);
		return input;
	}

	private void displayMainMenu() {
		System.out.println("Speak It!");
		System.out.println("Menu Principal\n" + "	1.- Procesar archivo de Texto\n" + "	2.- Reproducir Archivo\n" + "\n" + "	3.- Realizar una consulta\n" + "\n" + "	0.- Salir");
	}

	
}
