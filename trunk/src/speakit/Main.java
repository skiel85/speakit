package speakit;

import datos.capturaaudio.exception.SimpleAudioRecorderException;

public class Main {
	public static void main(String[] args) throws SimpleAudioRecorderException {
		Menu menu = new Menu();
		menu.display();
	}
	

//	public static void main(String[] args) throws SimpleAudioRecorderException, InterruptedException, SimpleAudioPlayerException, IOException {
//		String soundFilePath = "prueba.wav";
//		FileOutputStream file = new FileOutputStream(soundFilePath); 
//		
//		SimpleAudioRecorder recorder = new SimpleAudioRecorder(AudioFileFormat.Type.AU, file  );
//		
//		recorder.init();
//		recorder.startRecording( );
//		Thread.sleep(3000);
//		recorder.stopRecording();
//		file.flush();
//		file.close(); 
//		
//		InputStream stream=new FileInputStream(soundFilePath); 
//		BufferedInputStream fstream = new BufferedInputStream(stream);
//		
//		SimpleAudioPlayer player = new SimpleAudioPlayer(fstream);
//		player.init();
//		player.startPlaying();
//		Thread.sleep(3000);
//		player.stopPlaying();
//	}
}
