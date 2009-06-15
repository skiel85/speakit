package speakit.compression.arithmetic.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.TextDocument;
import speakit.compression.arithmetic.ArithmeticCompressor;
import speakit.io.ByteArrayConverter;

public class ArithmeticCompressorTest {

	private static final String	VUELO_447_DE_AIR_FRANCE			= ".E.l. vuelo 447 de Air France fue un vuelo internacional entre el Aeropuerto Internacional de Gale�o y el Aeropuerto Internacional Charles de Gaulle de Par�s. El 1 de junio de 2009, el avi�n, un Airbus A330-200, registrado F-GZCP (primer vuelo el 25 de febrero de 2005),1 desapareci� sobre el Oc�ano Atl�ntico con 216 pasajeros, entre ellos 61 franceses, 58 brasile�os, 26 alemanes, 71 de otras 29 nacionalidades2 y 11 tripulantes a bordo, incluyendo tres pilotos.3 4 Las autoridades de Brasil confirmaron que la Fuerza A�rea Brasile�a se encuentra realizando una b�squeda con el avi�n militar C-130 Hercules en la zona del archipi�lago de Fernando de Noronha, donde se cree que pudo haber ca�do la aeronave.5 El estado del avi�n y sus pasajeros es actualmente desconocido, pero tanto las declaraciones oficiales de Air France como del Gobierno de Francia presumen que la aeronave ha sufrido un accidente y que todas las personas que iban a bordo han fallecido.6 Se han localizado los restos de un avi�n cerca de las costas de Senegal; se cree que pueda ser el vuelo 447. Un testigo afirma que vio restos en llamas caer al mar. Otro piloto de la Fuerza A�rea Brasile�a inform� haber visto luces naranjas en el mar cerca al archipielago de Fernando de Noronha.7";
	private static final String	VUELO_447_DE_AIR_FRANCE_CORTO	= ".E.l. vu�lo 447 de Air France France.";
	private static final String	VUELO_447_DE_AIR_FRANCE_CORTO_SIN_PUNTO	= ".E.l. vu�lo 447 de Air France France";
	

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testCompleteCompressionCorto() throws IOException {
		testCompressString(VUELO_447_DE_AIR_FRANCE_CORTO);
	}

	private byte[] compress(String document) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ArithmeticCompressor compressor = new ArithmeticCompressor(out);
		compressor.compress(new TextDocument(document));
		// System.out.println(out.toString());
		return out.toByteArray();
	}
	
	private static final String	VUELO_447_DE_AIR_FRANCE_MICRO	= "gg";
	
	@Test
	public void testCompleteCompressionMicro() throws IOException {
		testCompressString(VUELO_447_DE_AIR_FRANCE_MICRO);
	}
	
	@Test
	public void testCompleteCompressionArticle() throws IOException {
		testCompressString(VUELO_447_DE_AIR_FRANCE);
	}
	
	@Test
	public void testCompleteCompressionCortoSinPunto() throws IOException {
		testCompressString(VUELO_447_DE_AIR_FRANCE_CORTO_SIN_PUNTO);
	}
	
	

	private void testCompressString(String article) throws IOException {
		// testea que el compresor genere un archivo que sea menos pesado que el
		// original
		
		System.out.println("***Comprimiendo: " + article);
		byte[] compressedbytes = compress(article);
		byte[] sourcebytes = article.getBytes();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.out.println("***Descomprimiendo: " + article);
		ArithmeticCompressor compressor = new ArithmeticCompressor(out);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
	} 
	
	@Test
	public void testCompleteCompressionNeuquen() throws IOException {
//		testCompressString("xxxxxxxxxxxxxxxxxxxaxaxaxxxxxxxxxxxxxxxxxxxxxsxssxxxxxxxx");
		testCompressString("NEUQUEN");
	}

}