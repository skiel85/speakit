package speakit.io.test;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import speakit.io.BlocksFile;
import speakit.io.SimpleBlocksFile;

public class BlocksFileTest {

	File file;
	 
	private SimpleBlocksFile	createdFile;

	private BlocksFile	sut;

	@Before
	public void setUp() throws Exception {
		this.file = File.createTempFile(this.getClass().getName(), ".dat");
		this.createdFile = new SimpleBlocksFile(this.file);
		this.createdFile.create(512);
		
		sut = new SimpleBlocksFile(this.file);
		sut.load();
	}
	
	@After
	public void tearDown() throws Exception {
		this.file.delete();
	}

	@Test
	public void testBlockSize() {
		Assert.assertEquals(512, this.sut.getBlockSize());
	}  
	
	//TODO: prueba pendiente. Que la clase lance excepcion si se quiere usar un método y todavia no fué creada o cargada.
	//TODO: prueba pendiente. Si bien hoy el header del archivo se guarda solo la primera vez, puede ser que mas adelante no. Entonces probar que luego de escribir algo en el archivo(ejemplo: apendear un bloque), que el header se guarde bien, es decir que haga un seek(0) y no se escriba en cualquier lado destruyendo el archivo.

}
