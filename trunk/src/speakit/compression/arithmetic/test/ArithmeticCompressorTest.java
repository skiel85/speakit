package speakit.compression.arithmetic.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.SpeakitLogger;
import speakit.TextDocument;
import speakit.compression.arithmetic.ArithmeticCompressor;
import speakit.io.ByteArrayConverter;

public class ArithmeticCompressorTest {

	private static final String	VUELO_447_DE_AIR_FRANCE			= ".E.l. vuelo 447 de Air France fue un vuelo internacional entre el Aeropuerto Internacional de Galeão y el Aeropuerto Internacional Charles de Gaulle de París. El 1 de junio de 2009, el avión, un Airbus A330-200, registrado F-GZCP (primer vuelo el 25 de febrero de 2005),1 desapareció sobre el Océano Atlántico con 216 pasajeros, entre ellos 61 franceses, 58 brasileños, 26 alemanes, 71 de otras 29 nacionalidades2 y 11 tripulantes a bordo, incluyendo tres pilotos.3 4 Las autoridades de Brasil confirmaron que la Fuerza Aérea Brasileña se encuentra realizando una búsqueda con el avión militar C-130 Hercules en la zona del archipiélago de Fernando de Noronha, donde se cree que pudo haber caído la aeronave.5 El estado del avión y sus pasajeros es actualmente desconocido, pero tanto las declaraciones oficiales de Air France como del Gobierno de Francia presumen que la aeronave ha sufrido un accidente y que todas las personas que iban a bordo han fallecido.6 Se han localizado los restos de un avión cerca de las costas de Senegal; se cree que pueda ser el vuelo 447. Un testigo afirma que vio restos en llamas caer al mar. Otro piloto de la Fuerza Aérea Brasileña informó haber visto luces naranjas en el mar cerca al archipielago de Fernando de Noronha.7";
	private static final String	VUELO_447_DE_AIR_FRANCE_CORTO	= ".E.l. vuélo 447 de Air France France.";
	private static final String	VUELO_447_DE_AIR_FRANCE_CORTO_SIN_PUNTO	= ".E.l. vuélo 447 de Air France France";
	private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam commodo molestie est eget ornare. Fusce pellentesque dolor sed risus sodales accumsan. Proin fringilla, turpis ac aliquam dapibus, eros eros pharetra lacus, rutrum volutpat purus sapien in purus. Phasellus quam diam, tincidunt vel ultrices vitae, tempus vel nisl. Fusce lacinia ornare massa ac commodo. Suspendisse malesuada nibh vitae lacus ultricies eget mattis arcu bibendum. Aenean felis erat, faucibus porttitor sagittis sed, ultrices in dolor. Sed malesuada tortor ut quam semper ut sodales augue ultricies. Nam sit amet mauris dolor. Cras nec ante sed leo rhoncus commodo. Aenean accumsan sapien eget elit consequat pulvinar.\n Integer non condimentum erat. Aliquam malesuada tellus sed felis ullamcorper semper. Morbi vitae eros nisi, et facilisis eros. Duis ultricies fermentum nunc id dictum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin id velit sem, sit amet sollicitudin ligula. Aliquam eget turpis urna, at tempus mauris. Suspendisse id velit vitae est consectetur consectetur. Cras nec est non augue tempor fermentum. Cras tellus nunc, ultrices vel pharetra vitae, egestas eu nisl. Nullam ultricies dolor ac neque tempor blandit. In sodales facilisis metus vitae venenatis. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Fusce faucibus augue eu lorem tincidunt ultrices. Aliquam lobortis convallis aliquet. Vestibulum eleifend quam id diam aliquet malesuada vel non turpis. Nam interdum metus vel sem tristique posuere. In hac habitasse platea dictumst. Fusce ac mi est.\n Aliquam convallis metus quis augue feugiat in venenatis urna mattis. Vivamus quis elit augue. Proin id gravida lorem. Duis eget purus et mauris scelerisque cursus. Ut congue augue eu augue viverra nec porttitor felis euismod. Curabitur vulputate, augue id tincidunt vehicula, magna tellus gravida arcu, ut malesuada orci purus in velit. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Duis pellentesque, massa in hendrerit vulputate, purus eros mollis arcu, sit amet luctus nisi felis in purus. Integer varius elit vitae magna lacinia mollis. Ut vitae elit est, eget pulvinar tellus. Phasellus sit amet lectus velit. Quisque tincidunt dictum turpis id aliquam. Nulla vestibulum sollicitudin metus, at venenatis augue rhoncus vestibulum. Fusce eget sem sapien.\n Quisque quis convallis odio. Proin accumsan placerat sapien, at blandit tellus gravida sit amet. Morbi tempor nulla nec nibh varius a convallis ligula vulputate. Nunc quis ante ut dui viverra accumsan. Aliquam nibh nisi, gravida sed iaculis sed, scelerisque vel enim. Proin vitae facilisis mi. Duis egestas convallis urna, eu malesuada felis cursus a. Nulla gravida suscipit quam. Nullam in mi sed massa adipiscing mattis. In luctus lobortis erat, sit amet pellentesque massa elementum non. Proin rutrum lacinia aliquet. Morbi lacinia pretium diam scelerisque sodales.\n Maecenas feugiat cursus rhoncus. Sed ipsum orci, elementum at tincidunt eget, luctus ut augue. Suspendisse potenti. Nam at sem nisi. Sed venenatis lobortis urna, et iaculis orci luctus interdum. Fusce convallis mauris eget nulla vestibulum rutrum id eget turpis. Nunc non vulputate purus. Phasellus interdum gravida nibh ut faucibus. Nulla tincidunt adipiscing accumsan. Sed porta leo justo, id vulputate diam. Maecenas pretium placerat malesuada. Donec dictum, sapien sit amet tempor interdum, ipsum elit hendrerit nisl, et mollis diam risus nec mi. Sed bibendum, turpis eu aliquam fermentum, augue ligula pharetra sem, interdum accumsan quam sem non nibh. Phasellus placerat turpis id felis semper facilisis. Nam lectus dolor, faucibus vitae tristique in, adipiscing id est. Sed tempor leo nec lorem rutrum non gravida nibh bibendum. Donec lectus purus, pulvinar et vulputate a, aliquet vel tellus. Maecenas vitae dolor nec tortor porta sollicitudin.";
	private static final String LOREM_IPSUM_CORTO = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam commodo molestie est eget ornare. Fusce pellentesque dolor sed risus sodales accumsan. Proin fringilla, turpis ac aliquam dapibus, eros eros pharetra lacus, rutrum volutpat purus sapien in purus. Phasellus quam diam, tincidunt vel ultrices vitae, tempus vel nisl. Fusce lacinia ornare massa ac commodo. Suspendisse malesuada nibh vitae lacus ultricies eget mattis arcu bibendum. Aenean felis erat, faucibus porttitor sagittis sed, ultrices in dolor. Sed malesuada tortor ut quam semper ut sodales augue ultricies. Nam sit amet mauris dolor. Cras nec ante sed leo rhoncus commodo. Aenean accumsan sapien eget elit consequat pulvinar.\n Integer non condimentum erat. Aliquam malesuada tellus sed felis ullamcorper semper. Morbi vitae eros nisi, et facilisis eros. Duis ultricies fermentum nunc id dictum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin id velit sem, sit amet sollicitudin ligula. Aliquam eget turpis urna, at tempus mauris. Suspendisse id velit vitae est consectetur consectetur. Cras nec est non augue tempor fermentum. Cras tellus nunc, ultrices vel pharetra vitae, egestas eu nisl. Nullam ultricies dolor ac neque tempor blandit. In sodales facilisis metus vitae venenatis. Cum sociis natoque penatibus et magnis ";

	@Before
	public void setUp() throws Exception {
		SpeakitLogger.deactivate();
//		SpeakitLogger.activate();
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
		// SpeakitLogger.Log(out.toString());
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
		
		SpeakitLogger.Log("***Comprimiendo: " + article);
		byte[] compressedbytes = compress(article);
		byte[] sourcebytes = article.getBytes();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SpeakitLogger.Log("***Descomprimiendo: " + article);
		ArithmeticCompressor compressor = new ArithmeticCompressor(out);
		compressor.setVerificationFile(article);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
	} 
	
	@Test
	public void testCompleteCompressionNeuquen() throws IOException {
//		testCompressString("xxxxxxxxxxxxxxxxxxxaxaxaxxxxxxxxxxxxxxxxxxxxxsxssxxxxxxxx");
		testCompressString("NEUQUEN");
	}
	
	@Test
	public void testCompleteCompressionLoremIpsumCorto() throws IOException {
		SpeakitLogger.activate();
		testCompressString(LOREM_IPSUM_CORTO);
	}
	
	@Ignore
	@Test
	public void testCompleteCompressionLoremIpsum() throws IOException {
		SpeakitLogger.activate();
		testCompressString(LOREM_IPSUM);
	}

}
