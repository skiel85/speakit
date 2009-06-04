package speakit.compression.arithmetic.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.compression.arithmetic.ArithmeticDecoder;
import speakit.compression.arithmetic.ArithmeticEncoder;
import speakit.compression.arithmetic.ProbabilityTable;
import speakit.compression.arithmetic.Symbol;

public class ArithmeticCompressorTest {
	

//	private static final String	VUELO_447_DE_AIR_FRANCE	= "El vuelo 447 de Air France fue un vuelo internacional entre el Aeropuerto Internacional de Galeão y el Aeropuerto Internacional Charles de Gaulle de París. El 1 de junio de 2009, el avión, un Airbus A330-200, registrado F-GZCP (primer vuelo el 25 de febrero de 2005),1 desapareció sobre el Océano Atlántico con 216 pasajeros, entre ellos 61 franceses, 58 brasileños, 26 alemanes, 71 de otras 29 nacionalidades2 y 11 tripulantes a bordo, incluyendo tres pilotos.3 4 Las autoridades de Brasil confirmaron que la Fuerza Aérea Brasileña se encuentra realizando una búsqueda con el avión militar C-130 Hercules en la zona del archipiélago de Fernando de Noronha, donde se cree que pudo haber caído la aeronave.5 El estado del avión y sus pasajeros es actualmente desconocido, pero tanto las declaraciones oficiales de Air France como del Gobierno de Francia presumen que la aeronave ha sufrido un accidente y que todas las personas que iban a bordo han fallecido.6 Se han localizado los restos de un avión cerca de las costas de Senegal; se cree que pueda ser el vuelo 447. Un testigo afirma que vio restos en llamas caer al mar. Otro piloto de la Fuerza Aérea Brasileña informó haber visto luces naranjas en el mar cerca al archipielago de Fernando de Noronha.7";
	private static final String	VUELO_447_DE_AIR_FRANCE	= "El vuelo 447 de Air France France.";
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testCompress() throws IOException {
		ByteArrayInputStream source = new ByteArrayInputStream(VUELO_447_DE_AIR_FRANCE.getBytes());
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ArithmeticCompressor compressor = new ArithmeticCompressor(out,source);
		compressor.compress();
		System.out.println(out.toString());
	}


}
