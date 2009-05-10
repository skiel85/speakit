package speakit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

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
	private boolean DEBUG_MODE = false;
	private ArrayList<String> documentsAdded = new ArrayList<String>(); 
	
	public Menu(boolean debug) {
		this();
		DEBUG_MODE = debug;
	}
	
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
	 * Despliega el menu para procesar varios archivos de texto
	 * 
	 * @throws IOException
	 * @throws RecordSerializationException
	 * 
	 * @throws WordNotFoundException
	 */
	private void addSeveralDocuments()throws IOException, RecordSerializationException {
		System.out.println("Ingrese cada una de las rutas de los documentos que desea ingresar separadas por coma");
		
		String paths = this.userInput.readLine().trim();
		char[] charsOfPaths = new char[paths.length()];
		charsOfPaths = paths.toCharArray();
		
		TextDocumentList documents = new TextDocumentList();
		int position = 0;
		String path = "";
		ArrayList<String> pathToShow = new ArrayList<String>();
		boolean endOfString = false;
		
		while(endOfString == false){
			if((position != paths.length()) && (charsOfPaths[position] != ',')){ 
				path = path + charsOfPaths[position];
				position++;
			}else{
				try{
					if(position == paths.length())endOfString = true;
					if((!pathToShow.contains(path)) &&(!documentsAdded.contains(path)) ){
						pathToShow.add(path);
						documentsAdded.add(path);
						TextDocument document = this.speakit.getTextDocumentFromFile(path);
						documents.add(document);
						showFileFoundMessage(path);
						path = "";
						position++;	
					}
				}catch (FileNotFoundException fnf){
					showFileNotFoundMessage(path);
					path = "";
					position++;
				}	
			}	
		}
		Iterator<String> iterator = pathToShow.iterator();
		Iterable <TextDocument> documentIterable = this.speakit.addDocuments(documents);
		for(TextDocument document : documentIterable){
			System.out.println();
			String showedPath = iterator.next();
//			Iterator<String> wordIterator = document.iterator();
			for(String word : document){
//			while(wordIterator.hasNext()){
//	            String word = wordIterator.next();
	            if(word != ""){
	            	System.out.println("El documento" + " " + showedPath + " " + "contiene palabras desconocidas que deberá grabar a continuación");
	            	WordAudio audio = getAudio(word);
	            	if (audio != null && audio.getAudio() != null) {
	            		speakit.addWordAudio(audio);
	            	}
	            }
	        }
			System.out.println("El documento" + " " + showedPath + " " + "fue agregado con éxito.");
		}
		System.out.println();
	}
	
		
		

	
	private void showFileFoundMessage(String path) {
		System.out.println("Pudo encontrarse el archivo '" + path + "'.");
		
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
		System.out.println(textDocumentFromFile.getText());
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
				addSeveralDocuments();
				break;
			case 3:
				playTextDocument();
				break;
			case 4:
				doConsultation();
				break;
			case 0:
				System.out.println("Terminado.");
				finished = true;
				break;
			case 5:
				if (DEBUG_MODE) {
					printIndexFile();
					break;
				}
			default:
				System.out.println("Opción inválida.\n");
				break;
			}
		}
	}

	private void printIndexFile() {
		System.out.println(speakit.printIndexForDebug());
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
		if (!documentList.isEmpty()) {
			showResults(documentList);
			showOptions(documentList);
		}
		else
		{
			System.out.println("La consulta no arrojó ningun resultado.\n");
			System.out.println("Para realizar una nueva consulta presione 1");
			System.out.println("Para ir al menu principal presione 0");
			chooseFailConsultationOption();
		}
	}

	/**
	 * Muestra los resultados de una consulta
	 * 
	 * @param documentList
	 */
	private void showResults(TextDocumentList documentList) {
		System.out.println("Los documentos encontrados para la consulta" + " " + "realizada se muestran a continuacion:");
		int number = 1;
		for (TextDocument textDocument : documentList) {
			System.out.println(number + " : " + textDocument.getPreview());
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
	private void chooseFailConsultationOption() throws IOException {
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
		System.out.println("Elija el numero de documento que desea reproducir");
		int number = Integer.parseInt(userInput.readLine());
		TextDocument document;
		int counter = 1;
		Iterator<TextDocument> iterator = documentList.iterator();
		
		while ((iterator.hasNext()) && (counter != number)){
			iterator.next();
			counter++;
		}
		document = iterator.next();
		if(document != null){
			WordAudioDocument audioDocument = this.speakit.convertToAudioDocument(document);
			System.out.println("Se va a reproducir el siguiente documento");
			System.out.println(document.getText());
			while (audioDocument.hasNext()) {
				this.playSound(audioDocument.next());
			}
		}else{
			System.out.println("EL número de documento que eligió es incorrecto, elija de nuevo");
			chooseDocumentToPlay(documentList);
		}
	}

	private BufferedReader initializeUserInput() {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader input = new BufferedReader(isr);
		return input;
	}

	private void displayMainMenu() {
		System.out.println("Speak It!");
		System.out.println("Menu Principal\n" + "		1.- Procesar un archivo de Texto\n" + "		2.- Procesar varios archivos de Texto\n" + "		3.- Reproducir Archivo\n" + "		4.- Realizar una consulta\n");
		if (DEBUG_MODE) {
			System.out.println("Bienvenido al lado oscuro\n");
			System.out.println("\t\t5 - Imprimir archivo indice\n");
		}
		System.out.println("\n" + "	0.- Salir");
		
	}

}
