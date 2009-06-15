package speakit.compression.ppmc.test;

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
import speakit.compression.arithmetic.test.BruteForceCompressionTester;
import speakit.compression.arithmetic.test.SingleCompressionTester;
import speakit.compression.ppmc.PPMC;
import speakit.io.ByteArrayConverter;

public class PPMCCompressorTest {
	
	private PPMC ppmc;
	ByteArrayOutputStream out;

	@Before
	public void setUp() throws Exception {
		out = new ByteArrayOutputStream();
		ppmc = new PPMC(out,2);
		
	}

	@After
	public void tearDown() throws Exception {

	}
	
	
	@Test
	public void testCompress1() throws IOException {
		SpeakitLogger.activate();
		
		byte[] compressedbytes = this.compress("ABAAAB");
		byte[] sourcebytes = "ABAAAB".getBytes();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PPMC compressor = new PPMC(out,2);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
		
		
	}
	
	
	@Test
	public void testCompress2() throws IOException {
		SpeakitLogger.activate();
		
		byte[] compressedbytes = this.compress("ABAAAC");
		byte[] sourcebytes = "ABAAAC".getBytes();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		PPMC compressor = new PPMC(out,2);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
		
		
	}
	
	
	@Test
	public void testCompress3() throws IOException {
		SpeakitLogger.activate();
		
		byte[] compressedbytes = this.compress("ABAAAD");
		byte[] sourcebytes = "ABAAAD".getBytes();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		PPMC compressor = new PPMC(out,2);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
		
		
	}
	
	@Test
	public void testCompress4() throws IOException {
		SpeakitLogger.activate();
		
		byte[] compressedbytes = this.compress("TATATAAAAALO");
		byte[] sourcebytes = "TATATAAAAALO".getBytes();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		PPMC compressor = new PPMC(out,2);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
		
		
	}
	
	@Test
	public void testCompress5() throws IOException {
		SpeakitLogger.activate();
		
		byte[] compressedbytes = this.compress(".E.l. vuélo 447 de Air France France.");
		byte[] sourcebytes = ".E.l. vuélo 447 de Air France France.".getBytes();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		PPMC compressor = new PPMC(out,2);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
		
		
	}
	
	@Ignore
	@Test
	public void testCompress6() throws IOException {
		SpeakitLogger.activate();
		
		byte[] compressedbytes = this.compress("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam commodo molestie est eget ornare. Fusce pellentesque dolor sed risus sodales accumsan. Proin fringilla, turpis ac aliquam dapibus, eros eros pharetra lacus, rutrum volutpat purus sapien in purus. Phasellus quam diam, tincidunt vel ultrices vitae, tempus vel nisl. Fusce lacinia ornare massa ac commodo. Suspendisse malesuada nibh vitae lacus ultricies eget mattis arcu bibendum. Aenean felis erat, faucibus porttitor sagittis sed, ultrices in dolor. Sed malesuada tortor ut quam semper ut sodales augue ultricies. Nam sit amet mauris dolor. Cras nec ante sed leo rhoncus commodo. Aenean accumsan sapien eget elit consequat pulvinar.\n Integer non condimentum erat. Aliquam malesuada tellus sed felis ullamcorper semper. Morbi vitae eros nisi, et facilisis eros. Duis ultricies fermentum nunc id dictum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin id velit sem, sit amet sollicitudin ligula. Aliquam eget turpis urna, at tempus mauris. Suspendisse id velit vitae est consectetur consectetur. Cras nec est non augue tempor fermentum. Cras tellus nunc, ultrices vel pharetra vitae, egestas eu nisl. Nullam ultricies dolor ac neque tempor blandit. In sodales facilisis metus vitae venenatis. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Fusce faucibus augue eu lorem tincidunt ultrices. Aliquam lobortis convallis aliquet. Vestibulum eleifend quam id diam aliquet malesuada vel non turpis. Nam interdum metus vel sem tristique posuere. In hac habitasse platea dictumst. Fusce ac mi est.\n Aliquam convallis metus quis augue feugiat in venenatis urna mattis. Vivamus quis elit augue. Proin id gravida lorem. Duis eget purus et mauris scelerisque cursus. Ut congue augue eu augue viverra nec porttitor felis euismod. Curabitur vulputate, augue id tincidunt vehicula, magna tellus gravida arcu, ut malesuada orci purus in velit. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Duis pellentesque, massa in hendrerit vulputate, purus eros mollis arcu, sit amet luctus nisi felis in purus. Integer varius elit vitae magna lacinia mollis. Ut vitae elit est, eget pulvinar tellus. Phasellus sit amet lectus velit. Quisque tincidunt dictum turpis id aliquam. Nulla vestibulum sollicitudin metus, at venenatis augue rhoncus vestibulum. Fusce eget sem sapien.\n Quisque quis convallis odio. Proin accumsan placerat sapien, at blandit tellus gravida sit amet. Morbi tempor nulla nec nibh varius a convallis ligula vulputate. Nunc quis ante ut dui viverra accumsan. Aliquam nibh nisi, gravida sed iaculis sed, scelerisque vel enim. Proin vitae facilisis mi. Duis egestas convallis urna, eu malesuada felis cursus a. Nulla gravida suscipit quam. Nullam in mi sed massa adipiscing mattis. In luctus lobortis erat, sit amet pellentesque massa elementum non. Proin rutrum lacinia aliquet. Morbi lacinia pretium diam scelerisque sodales.\n Maecenas feugiat cursus rhoncus. Sed ipsum orci, elementum at tincidunt eget, luctus ut augue. Suspendisse potenti. Nam at sem nisi. Sed venenatis lobortis urna, et iaculis orci luctus interdum. Fusce convallis mauris eget nulla vestibulum rutrum id eget turpis. Nunc non vulputate purus. Phasellus interdum gravida nibh ut faucibus. Nulla tincidunt adipiscing accumsan. Sed porta leo justo, id vulputate diam. Maecenas pretium placerat malesuada. Donec dictum, sapien sit amet tempor interdum, ipsum elit hendrerit nisl, et mollis diam risus nec mi. Sed bibendum, turpis eu aliquam fermentum, augue ligula pharetra sem, interdum accumsan quam sem non nibh. Phasellus placerat turpis id felis semper facilisis. Nam lectus dolor, faucibus vitae tristique in, adipiscing id est. Sed tempor leo nec lorem rutrum non gravida nibh bibendum. Donec lectus purus, pulvinar et vulputate a, aliquet vel tellus. Maecenas vitae dolor nec tortor porta sollicitudin.");
		byte[] sourcebytes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam commodo molestie est eget ornare. Fusce pellentesque dolor sed risus sodales accumsan. Proin fringilla, turpis ac aliquam dapibus, eros eros pharetra lacus, rutrum volutpat purus sapien in purus. Phasellus quam diam, tincidunt vel ultrices vitae, tempus vel nisl. Fusce lacinia ornare massa ac commodo. Suspendisse malesuada nibh vitae lacus ultricies eget mattis arcu bibendum. Aenean felis erat, faucibus porttitor sagittis sed, ultrices in dolor. Sed malesuada tortor ut quam semper ut sodales augue ultricies. Nam sit amet mauris dolor. Cras nec ante sed leo rhoncus commodo. Aenean accumsan sapien eget elit consequat pulvinar.\n Integer non condimentum erat. Aliquam malesuada tellus sed felis ullamcorper semper. Morbi vitae eros nisi, et facilisis eros. Duis ultricies fermentum nunc id dictum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin id velit sem, sit amet sollicitudin ligula. Aliquam eget turpis urna, at tempus mauris. Suspendisse id velit vitae est consectetur consectetur. Cras nec est non augue tempor fermentum. Cras tellus nunc, ultrices vel pharetra vitae, egestas eu nisl. Nullam ultricies dolor ac neque tempor blandit. In sodales facilisis metus vitae venenatis. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Fusce faucibus augue eu lorem tincidunt ultrices. Aliquam lobortis convallis aliquet. Vestibulum eleifend quam id diam aliquet malesuada vel non turpis. Nam interdum metus vel sem tristique posuere. In hac habitasse platea dictumst. Fusce ac mi est.\n Aliquam convallis metus quis augue feugiat in venenatis urna mattis. Vivamus quis elit augue. Proin id gravida lorem. Duis eget purus et mauris scelerisque cursus. Ut congue augue eu augue viverra nec porttitor felis euismod. Curabitur vulputate, augue id tincidunt vehicula, magna tellus gravida arcu, ut malesuada orci purus in velit. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Duis pellentesque, massa in hendrerit vulputate, purus eros mollis arcu, sit amet luctus nisi felis in purus. Integer varius elit vitae magna lacinia mollis. Ut vitae elit est, eget pulvinar tellus. Phasellus sit amet lectus velit. Quisque tincidunt dictum turpis id aliquam. Nulla vestibulum sollicitudin metus, at venenatis augue rhoncus vestibulum. Fusce eget sem sapien.\n Quisque quis convallis odio. Proin accumsan placerat sapien, at blandit tellus gravida sit amet. Morbi tempor nulla nec nibh varius a convallis ligula vulputate. Nunc quis ante ut dui viverra accumsan. Aliquam nibh nisi, gravida sed iaculis sed, scelerisque vel enim. Proin vitae facilisis mi. Duis egestas convallis urna, eu malesuada felis cursus a. Nulla gravida suscipit quam. Nullam in mi sed massa adipiscing mattis. In luctus lobortis erat, sit amet pellentesque massa elementum non. Proin rutrum lacinia aliquet. Morbi lacinia pretium diam scelerisque sodales.\n Maecenas feugiat cursus rhoncus. Sed ipsum orci, elementum at tincidunt eget, luctus ut augue. Suspendisse potenti. Nam at sem nisi. Sed venenatis lobortis urna, et iaculis orci luctus interdum. Fusce convallis mauris eget nulla vestibulum rutrum id eget turpis. Nunc non vulputate purus. Phasellus interdum gravida nibh ut faucibus. Nulla tincidunt adipiscing accumsan. Sed porta leo justo, id vulputate diam. Maecenas pretium placerat malesuada. Donec dictum, sapien sit amet tempor interdum, ipsum elit hendrerit nisl, et mollis diam risus nec mi. Sed bibendum, turpis eu aliquam fermentum, augue ligula pharetra sem, interdum accumsan quam sem non nibh. Phasellus placerat turpis id felis semper facilisis. Nam lectus dolor, faucibus vitae tristique in, adipiscing id est. Sed tempor leo nec lorem rutrum non gravida nibh bibendum. Donec lectus purus, pulvinar et vulputate a, aliquet vel tellus. Maecenas vitae dolor nec tortor porta sollicitudin.".getBytes();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		PPMC compressor = new PPMC(out,2);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(ByteArrayConverter.toString(sourcebytes), ByteArrayConverter.toString(out.toByteArray()));
		
		
	}
	
	private byte[] compress(String document) throws IOException {
		ppmc.compress(new TextDocument(document));
		return out.toByteArray();
	}
	
	private static byte[] compress(String document,PPMC ppmc,ByteArrayOutputStream out) throws IOException {
		ppmc.compress(new TextDocument(document));
		// SpeakitLogger.Log(out.toString());
		return out.toByteArray();
	}
	
	public static void testCompress(String file) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PPMC ppmc = new PPMC(out,2);
		//Este no funciona
		ppmc.compress(new TextDocument(file));
		byte[] compressedbytes = out.toByteArray(); 
		
		ByteArrayOutputStream decompressionOutput = new ByteArrayOutputStream(); 
		PPMC compressor = new PPMC(decompressionOutput,2);
		compressor.decompress(new ByteArrayInputStream(compressedbytes));
		Assert.assertEquals(file, new String(decompressionOutput.toByteArray()));
		
	}
	

	@Ignore
	@Test
	public void testCompressAAB() throws IOException{
		SpeakitLogger.activate();
		testCompress("aab");
	}
	
	@Ignore
	@Test
	public void testProgressiveTextFiles() throws IOException {
		SpeakitLogger.deactivate();
		BruteForceCompressionTester tester = new BruteForceCompressionTester(5,new SingleCompressionTester(){

			@Override
			public void testCompress(String file) throws IOException {
				PPMCCompressorTest.testCompress(file);
			}
			
		},true);
		tester.runTests();
	}

}
