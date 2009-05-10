package speakit.io.bsharptree.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import speakit.TextDocument;
import speakit.ftrs.index.InvertedIndexIndexRecord;
import speakit.ftrs.index.InvertedIndexIndexRecordEncoder;
import speakit.io.bsharptree.RecordEncoder;
import speakit.io.bsharptree.Tree;
import speakit.io.bsharptree.TreeDuplicatedRecordException;
import speakit.io.record.RecordSerializationException;
import speakit.io.record.StringField;
import speakit.test.TestFileManager;
@Ignore
public class StressTreeTest {
	

	/**
	 * Simula un numero de bloque. Devuelve el tama�o de la palabra como numero
	 * de bloque
	 * 
	 * @param word
	 * @return
	 */
	public static int simulateBlockNumber(String word) {
		return word.length();
	}

	public static int simulateUpdatedBlockNumber(String word) {
		return word.length() / 2;
	}

	public static void testRetrieveAllRecords(Tree<InvertedIndexIndexRecord, StringField> sut, Iterable<String> words) throws RecordSerializationException, IOException {
		for (String word : words) {
			StringField key = new StringField(word);
			InvertedIndexIndexRecord record = sut.getRecord(key);			
			verifyCorrectRecord(record, word, key);
		}
	}

	public static void verifyCorrectRecord(InvertedIndexIndexRecord record, String word, StringField key) {
		// verifica que el record obtenido sea el correcto
		Assert.assertNotNull("El arbol no devolvi� ning�n registro cuando se le pidi� uno que hab�a sido insertado. Palabra buscada: " + key.toString(), record);
		Assert.assertEquals(0, record.getKey().compareTo(key));
		Assert.assertEquals(simulateBlockNumber(word), record.getBlockNumber());
	}

	private Tree<InvertedIndexIndexRecord, StringField>	sut;

	private TestFileManager								filemanager;

	private RecordEncoder								encoder;

	private TextDocument								article;

	/**
	 * definimos un tama�o de bloque real, por ejemplo 512 e insertamos una gran cantidad de palabras"
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.filemanager = new TestFileManager("");
		encoder = new InvertedIndexIndexRecordEncoder();
		this.sut = new Tree<InvertedIndexIndexRecord, StringField>(filemanager.openFile("FullBSTree.dat"), InvertedIndexIndexRecord.createRecordFactory(), encoder);
		//this.sut.create(256);
		this.sut.create(128);
		String longText = "Libro no, gui�n cinematogr�fico no, obra de tratro no, todo no, todo no, todo no. Billones y billones de usuarios me escriben explic�ndome que han intentado toda la gama de los g�neros literarios siguiendo mis consejos y han fracasado miserablemente, exigi�ndome por tanto la devoluci�n del importe de los talleres m�s un �plus� por �lucro cesante�, que es como que te tengo que pagar por ser un inoperante." +
"Lamentable la pol�tica de la empresa incluye, grabada a fuego, una �cl�usula de no devoluci�n de dinero y en general de ninguna otra cosa�, por lo que no va a poder ser, querido. En vez, les ofrezco �a mitad de precio- una clase especial develando los secretos del g�nero literario m�s menospreciado de todos: el arte de la conversaci�n. Si ud. domina este g�nero, no va a poder lucrar con �l pero le facilitar� hacer contactos. Tender los hilos, hacer negocios, relaciones p�blicas, todas esas cositas que luego lo habilitar�n para su primer libro o puesto en presidencia de la Naci�n. Si alcanza un nivel de conversaci�n aceptable, tal vez alguien incluso se atreva a publicar un libro de �Conversaciones con (inserte aqu� su nombre)� y ah� capaz le puede pedir plata. Por fin, dominar el arte de la Conversaci�n tal vez lo acredite para ser invitado a un programa o a un panel de algo, aunque mis magras experiencias en el ramo no me han reportado ganancia alguna. Pero de golpe ah� ten�s a Winograd o a Jorge As�s que nadie sabe qu� hacen pero, c�mo hablan, mamita querida, y me refiero sobre todo a la cantidad." +
"Empecemos por el hecho de que la gente no sabe hablar. El arte de la conversaci�n ha muerto o est� agonizando b�sicamente por ausencia de capacidad de hilaci�n de dos frases seguidas en el humano promedio. El locuaz polemista cibern�tico, que puede deshacerse en largas parrafadas en el espacio virtual, no es capaz de sobrepasar el nivel del borborigmo inarticulado en la vida real." +
"El conversador promedio actual se pierde en medio de las oraciones, hace ruidos con saliva, confunde los tiempos verbales (con otros tiempos verbales pero incluso con sustantivos o cosas), es incapaz de describir algo, repite el mismo adjetivo para definir todo, interrumpe, no es capaz de relacionar un hecho �a� con un hecho �b�, carece de an�cdotas personales o ajenas, cree que el idioma es una herramienta y no un juego de mesa, dice cosas que escuch� en la tele, se detiene en medio de una historia dieciocho veces para autocorregirse insignificantes detalles de la misma y por fin todos sus esfuerzos de participaci�n en la charla se dirigen a mechar el hecho de que se compr� tal o cual aparatito. En resumen, un bodrio." +
"Esto es obviamente culpa de la tele, lo internet y los jueguitos, pero tambi�n del desconocimiento b�sico de algunas herramientas b�sicas que los grandes conversadores de anta�o dominaban a la perfecci�n. Repasemos algunas:" +
"LOS VERSITOS: Cualquiera que haya conversado con un padre, un abuelo o un suegro sabe que esta clase de gente no puede pasar dos minutos de charla sin que algo le recuerde tal o cual versito. Puede ser el fragmento de una canci�n, de un poema escrito por uno de los grandes Maestros de la Poes�a o sencillamente de un versito que ley� alguna vez en un cartelito de un almac�n. Este elemento le aporta a la conversaci�n algo m�s que el intercambio de an�cdotas aburridas u opiniones bodrias. Le aporta musicalidad y zucund�n, zucund�n. Una conversaci�n sin versitos es como un plato de arroz sin queso rallado. No necesariamente deben ser piezas en verso: un refr�n desconocido o una frase c�lebre (acompa�ada del nombre de su autor) tambi�n pueden servir de adorno."+
"Por supuesto que lo m�s complicado es encontrar el versito justo para meterlo en el momento pertinente de la conversaci�n. Por eso, el aspirante a conversador debe aprenderse todos los d�as entre diez y veinte versitos y memorizarlos, esperando que llegue la ocasi�n adecuada. Si este no llega, el versito hay que meterlo igual. La gente se sentir� desconcertada pero lo agradecer�."+
"Ejemplo:"+
"-Qu� b�rbaro, ya estamos en mayo."+
"-Como en el �Romance del Prisionero�: �Que por mayo era por mayo, cuando hace la calor, cuando canta la calandria, y est�n los campos en flor�."+
"De ah� se puede derivar a temas como la situaci�n de los presos en la Argentina, la ornitolog�a, poes�a espa�ola y por supuesto a lo de qu� loco, mayo y todav�a hace calor."+
"LA �POCA DE PER�N: Un elemento a tener en cuenta en el Arte de la Conversaci�n es c�mo iniciar una an�cdota, relato o ejemplo hist�rico. Encuentro que, por alg�n motivo, la �poca de Per�n es un buen �leit-motiv� que captura inmediatamente la atenci�n del contrincante. Las frases para iniciar la historia pueden ser, por ejemplo �Cuando subi� Per�n en el 45�� o �Me acuerdo all� por el 55�� o �Cuando volvi� Per�n en el 73��, etc. El resto de la historia va por cuenta del conversador. Puede estar o no relacionado vagamente con Per�n. No importa. Lo importante es que ha empezado la frase y se ha vencido el �temor a la hoja en blanco�. Otras posibles frases son �Cuando estuve en la Acr�polis�� o �La vez que por poco no cuento el cuento fue cuando��"+
"Frases no recomendadas para iniciar una frase: �El otro d�a vi en la tele que��, �Mi chica/o dice que��, �Me lleg� un mail que dice que��, ��Sab�s cu�nto me sali� el�?�, �El otro d�a me sali� un grano en el��, �Cuando me operaron de la��, �Una vez me hice pop� y eso que ten�a 40 a�os, resulta que��, �Si hablamos de asquerosidades, en Youtube pusieron un coso donde�� y �A ver�."+
"Ejemplo:"+
"-Qu� calor que hace, �eh?"+
"-En la �poca de Per�n, ah� en el 17 de octubre, parece que tambi�n hac�a bastante calor, te acord�s de c�mo los tipos se mojaban las patas en la fuente. Bueno, un poroto al lado de esto."+
"LOS N�MEROS DE LA QUINIELA: Otro elemento muy enriquecedor que suelen utilizar nuestros mayores es la lista de n�meros de la quiniela y sus sue�os correspondientes. Es muy �til como �parante� o �apoyo� del parlamento del contrincante cuando este es muy bodrio y de paso sirve para hacerle recordar de nuestra presencia (y de paso que agilice un poco)."+
"Para poder aplicar esta herramienta, claro, hay que memorizar los n�meros de la quiniela (en tres meses un ser humano normal puede lograrlo) y tenerlos en la punta de la lengua, como para utilizarlos en cuanto el otro conversador mencione un n�mero. De no ser as�, pueden inventarse n�meros de la quiniela imaginarios y confiar que el otro tipo no los conozca."+
"Ejemplo:"+
"-Y me dijeron que tardaban en reconect�rtelo como 36 horas."+
"-36, el Vampiro. �Qu� barbaridad!"+
"Tengo que decir que no saber los n�meros de la quineila de memoria es una de mis grandes frustraciones. Si pudiera, estar�a haciendo esto todo el tiempo."+
"LOS OJOS: El buen conversador utiliza tanto las palabras, los versitos, la �poca de Per�n y los n�meros de la Quiniela como los ojos. Para empezar, debemos recordar que cuando el conversador se convierte en oyente debe mirar al conversador �b�. Desgraciadamente la vida moderna hace que muchas de nuestras conversaciones se produzcan entre tipos que est�n mirando una pantallita como est�pidos. Claro que as� les salen. Mal�simas. Sin embargo, de no mediar computadora se puede considerar una groser�a mirar para otro lado cuando alguien nos habla, actitud que puede impulsar el completo fracaso de la conversaci�n. El oyente debe �salpimentar� el relato ajeno con aperturas de p�rpados, abri�ndolos en diferentes tama�os para indicar los diferentes niveles de asombro o cerr�ndolos (con un leve asentimiento en �c�mara lenta� de la cabeza) al final de la historia, como diciendo �Claro, qu� b�rbaro, no, ten�s raz�n�. Los ojos juegan un rol a�n m�s importante en el conversador activo, acompa�ando el �acting� de los diversos personajes de la an�cdota, mirando al oyente sin penetrarlo con los ojos, sino focalizando la vista en una zona indefinida de su rostro, posando la vista alternativamente en los diversos oyentes si son m�s de uno y cerr�ndolos en la conclusi�n final, como bajando el tel�n."+
"Ejemplo: ...La cosa es que al final llegamos y el perro se lo hab�a comido (cierra los ojos)"+
"EL SILENCIO: La conversaci�n es un arte en colaboraci�n. Si una persona es conversadora y la otra no, entonces estamos ante el caso de un �Mon�logo� y eso ya es m�s jodido porque parece que hay que verlo en un teatro y pagarlo. Por eso el conversador debe apelar a todos los medos posibles para que la participaci�n del enemigo tambi�n sea significativa."+
"Por eso el manejo del silencio es important�simo, ya que quieren las reglas de la cponversaci�n y hasta dir�a de la f�sica del o�do y el cerebro humanos que mientras uno habla, el otro se calle. Esto, que parece tan sencillo porque simplemente no hay que hacer algo, es menospreciado en la mayor�a de las conversaciones, que suelen parecerse m�s a un duelo a los cachetazos que a un baile sobre patines de hielo."+
"Por otro lado, el silencio sirve tambi�n para ir preparando uno la contrarr�plica o contraan�cdota, redactarla mentalmente y agregarle versitos y n�meros de quiniela. Por supuesto, no olvide abrir y cerrar los ojos, para que el tipo siga con la ilusi�n de que ud. lo escucha. Por fin, cuando el hombre ha terminado de hablar, recuerde que es necesario dejar una peque�a pausa, como un �colchoncito� para que no parezca que ud. est� completamente desesperado por intervenir. Este ejercicio de paciencia tiene un doble premio: por un lado, el aire embellecer� un poco su obra, es decir la conversaci�n ( y no parecer� unn guiso de lentejas), y por otro lado el otro tipo se sentir� obligado a hacer silencio. Por supuesto, lo cort�s no quita lo valiente, y si a pesar de su esfuerzo el contrario lo interrumpe m�s de dos veces (la Momia), ud. puede recurrir a la violencia para acallarlo."+
"Ejemplo:"+
"-�"+
"LOS TEMAS: Evite a toda costa los siguientes temas, capaces de embodriar las conversaciones m�s prometedoras:"+
"-Cosas de computaci�n y cosas para hacer que las m�quinas anden mejor, o cosas que hay que comprarse para la cosa de la computaci�n y los precios de las diferentes cosas esas."+
"-Remedios para diferentes cosas y cu�nto salen."+
"-Programas de cable y de qu� se tratan y cu�ndo los dan."+
"-El clima, especialmente el calor."+
"-Lo del campo y lo del gobierno."+
"-Modelos de autos modernos y cu�nto salen y por qu� te conviene comprarte un auto as� o un auto as�."+
"-Tr�mites de t�tulos y cu�ndo te dijeron que te los iban adar."+
"-Enfermedades de ni�os."+
"-La Educaci�n argentina y lo que hay que hacer para arreglar la Educaci�n argentina y por qu� anda as� la Educaci�n argentina."+
"-Modelos de tel�fonos celulares y cosas para ponerles para que anden mejor y cu�ntos alen las cosas esas y los celulares propiamente dicho y cu�nto hay que pagar para que te den el servicio ese de que anda el celular."+
"-Refacciones de casas y cu�nto tardaron en hacerte la refacci�n de la casa y cu�nto te sali� y las pr�ximas refacciones que quer�s hacer en la casa y cu�nto sale y c�mo pens�s pagarla."+
"-Dietas especiales."+
"El resto de los temas son v�lidos: La aeron�utica, los recuerdos del Colegio, los animales, el Renacimiento, el canibalismo, recetas de cocina, Popeye, las colonias jesu�ticas, la invisibilidad, lo que dijo Mario Mactas, an�cdotas de vecinos medio loquibambis, borracheras, los ni�os, historias de ca�das al piso, expresiones idiom�ticas, etc. Un buen conversador debe intentar en lo posible desviar los temas bodrios a los temas no bodrios."+
"Ejemplo:"+
"-Lo que vos ten�s que hacer es pedirle a tu t�cnico que te traiga un disco de rebuteo y entonces que te lo ponga y as� esos archivos se te van a hacer visibles."+
"-Ah, la visibilidad, qu� tema, aunque no tan controvertido como la invisibilidad. A prop�sito, �qu� te parece la invisibilidad? Yo preferir�a volar. O ser can�bal, no s�."+
"EL VINO: Por fin, si los conversadores son especialmente ineptos se recomienda fervientemente que haya vino. Si son competentes, tambi�n. Se recomienda a los educandos que graben la conversaci�n resultante para poder estudiarla a posteriori. Bueno, y tambi�n para revisar que uno no haya dicho algo indebido o que haya denunciado a alguien al FBI a manera de broma, o que haya prometido pasear al perro completamente desnudo";
		
		
String longText2 = " Dengos: �Nueva tendencia o moda pasajera?"
+"�Mmmmmbbbb, mmmbbb, mbbmbmmmbbmmm�, dice Jaqueline (27), detr�s de su m�scara de ox�geno, instalada sobre su capucha de l�tex a su vez enfundada sobre el triple vendaje untado en repelente. Y es que, como todos los miembros de la movida �Dengo�, debe luchar contra la incomprensi�n de la sociedad tanto como contra la incomprensi�n meramente auditiva."
+"Mientras las autoridades metropolitanas hacen todo lo que est� en sus manos para detener el avance del dengue �que consiste b�sicamente, confiar en que el Aedes Aegypty llegue hasta la General Paz, se sienta intimidado por la arrolladora personalidad de los porte�os y ah� pegue la media vuelta-, la poblaci�n de Buenos Aires ha realizado creativamente como siempre lo hace en momentos de crisis: inventando una nueva �tribu urbana�."
+"�Para m�, ser un �dengo� es m�s que una moda: es una filosof�a de vida�, dice Mart�n (21), un joven �dengo� que ha aprendido a modular exageradamente y gritar �tipo Gustavo Cerati- para que sus interlocutores lo entiendan, incluso bajo la triple capa protectiva de materiales elastizados que recubren su persona. �Cuando empez� esta movida, parec�a que se trataba solo de gente medio aterrorizada que insist�a en que hab�a que usar manga larga, dar vuelta recipientes al aire libre y embadurnarse con repelente. Pero un mont�n de nosotros pensamos que eso pod�a dar lugar a algo m�s copado�. Mart�n se interrumpe para untar la totalidad de la superficie de su mameluco de l�tex con una capa de 3 cm. de �Off�, ceremonia que los �dengos� realizan cada cinco minutos. Cuando termina, me ofrece una pastillita de Fuyi Vape, y ante mi negativa se abre una puertita del casco que cubre su cabeza y se la manduca, sin agua ni nada. �Y no le hace mal? �Un poco. Por ejemplo, ya perd� la capacidad de distinguir los contornos de las cosas. Y un poco la parte de adentro tambi�n. �Pero siempre va a ser mejor que tomar aspirinas!�, grita, con furia (Los �dengos� odian la aspirina y estiman que quienes la consumen son unos �negros cabeza�)."
+"�La guerra de Vietnam impuls� el movimiento hippie, y el desempleo brit�nico dio origen al punk�, dice Amadeo Rosenwasser (56), soci�logo. �A nosotros, la aparici�n del dengue nos ha dejado toda una subcultura. Lo m�s sorprendente es la velocidad con que se ha extendido: a las tres horas de conocerse los primeros casos en Charata, en Internet ya hab�a m�s de 456.78e7.334.9896.000.000.90 p�ginas dedicadas al �Movimiento Dengo�. La verdad es que la gente no es m�s pelotuda porque no tiene tiempo�, agrega, en un tono muy acad�mico."
+"Por si la masividad del Movimiento �Dengo� es puesta en duda, las ventas de vestuario y m�scaras de l�tex en Sex Shops fetichistas, tiendas de disfraces y convenciones de c�mic �que ya se revel� que es todo lo mismo- se han cuadriplicado, casi exclusivamente en art�culos que a�slen la piel del ataque del mosquito. �Me piden de todo, desde bolsas marineras tipo 'Audition' a disfraces de Batman�, dice Mario (49), due�o de local �Bebota, esta carne es para vos� sito en una galer�a de Florida. �Incluso se agotaron unas caretas de De la R�a que me llegaron por error.�"
+"Pero, �qu� es realmente ser un �dengo�? �Cu�l es su ideal �ltimo, si es que este corpus de rituales sin sentido para el lego tiene un fin �ltimo? �La total y absoluta aislaci�n (sic) corporal y mental de cualquier tipo de contacto con el exterior, para conseguir un estado superior de conciencia, una sanidad f�sica total y una pureza de cuerpo y esp�ritu y aparte no tener dengue�, dice con voz apasionada Princess Nefertiti (17), gur� del Movimiento �Dengo� y autora del blog �No me moleste mosquito�, que recibe aproximadamente 234.5678.90.0.876.321.000 visitas por d�a, aunque diez de ellas por parte de surfeadores accidentales (hay que decir que dice �sexo gratis� en el header)."
+"Princess Nefertiti se ufana de ser una de las �dengos� con m�s capas de cosas entre su epidermis y el exterior. �Tengo unas 17 capas, entre enteritos de l�tex, goma y ropa de distintos talles. Entre capa y capa me embadurno toda con Off y pastillitas Fuy� Vape picadas. Tardo unas tres horas en vestirme, y otras dos horas en ponerme las capas para dormir bajo mi mosquitero hecho con bolsas de residuos.� A pesar de su aspecto exterior (lleva una m�scara de Domingo Cavallo y la superposici�n de ropajes le da el aspecto de una pi�ata humana), comentarios como ��Diosa!� o ��Te como toda!� son frecuentes en su blog."
+"�Me puse Princess Nefertiti, que es un nombre egipcio, como el mosquito�, nos aclara la Reina de los �dengos� mientras nos gu�a hasta un estacionamiento subterr�neo abandonado (los miembros de esta tribu odian el aire libre, por ser caldo de cultivo de �esos bichos de mierda que te pican y te mor�s�, seg�n Princess Nefertiti) donde se realiza el �XVIII Encuentro Alternativo Dengo�, que se realiza cada seis horas, y donde los �dengos� comparten secretos, escuchan su m�sica, celebran org�as de embadurnamiento con Off, se ponen hasta las cejas de tabletas Fuy� y comercian art�culos que reflejan su subcultura. �Me compr� esto por 45 mangos�, nos dice Laura (16), una joven �dengo� envuelta en un mameluco anaranjado, mientras nos muestra un par de aros confeccionados con espirales (que, como todos saben, se hacen con bosta de vaca). Luego de encenderlos, aclara �Igual para dormir me los saco, obvio�. Desde el interior de un antiguo arc�n de madera cubierto de cadenas y cerrojos, Demi�n (20) nos dice �Para m� al Movimiento �Dengo� se lo est� fagocitando el sistema. Ya no es lo que era cuando naci�, la semana pasada. Por eso yo curto el �Denguismo Extremo� desde adentro de este ba�l. Lo �nico que estoy empezando a tener un poco de hambre.�"
+"�Moda? �Movimiento? �Locura? �Invento de los medios? Mientras Princess Nefertiti sube al estrado y es ovacionada por sus admiradores, la verdadera �fiesta dengo� arranca, y los asistentes empiezan a patear recipientes �baldes, ollas, neum�ticos- colocados estrat�gicamente a lo alrgo del predio, con el objeto de darlos vuelta. Entonces, una mariposa desorientada ingresa al estacionamiento, alguien la se�ala al grito de ��Dengue!� y la multitud se dispersa entre patadas, atropellamientos y uno o dos muertos aplastados."
+"Moda pasajera o no, los �dengos� est�n aqu� para quedarse.";

String longText3 = " �Qu� me mir�s sin esa cara?"
+"Durante d�cadas, los pesimistas gur�es de la ciencia ficci�n han alertado a la Humanidad sobre los peligros del confort: Desde �Los Supers�nicos�, y a la pel�cula �Wall-E�, pasando por la historieta de Kurtzman y Wood �Blobs!� (en los inicios de la revista �Mad�) nos han mostrado un futuro donde el uso de la tecnolog�a ha convertido al Hombre en un beb� absolutamente dependiente y con el cuerpo completamente fofo y atrofiado, culpa de las veredas m�viles y mini-transportes individuales."
+"Sin embargo, ninguno de estos Nostradamus de pacotilla fue capaz de predecir la peor consecuencia de la tecnolog�a, y que �es hora de revelarlo- est� ocurriendo en este momento: Se nos est� atrofiando la cara."
+"Anta�o, cuando la forma de comunicaci�n humana m�s extendida era cara a cara, el lenguaje oral estaba acompa�ado por miles y miles de peque�os gestos, visajes, gui�os, inflexiones en la voz, miradas y muecas que complementaban el limitad�simo sistema de comunicaci�n verbal. Entonces, gracias a este conjunto de herramientas perif�ricas de la palabra, fue el reino del sarcasmo. Uno pod�a decir �ah, s�, los comentaristas de blogs, esos s� que son re grosos�, y gracias a una mirada, un gesto realizado con los dientes superiores o cierta anomal�a autoinflingida en la voz, el interlocutor pod�a dilucidar el verdadero significado. Este arte fue tomando ramas cada vez m�s sutiles, una sencilla levantada de ceja pod�a eliminar de un plumazo todo lo que el sarc�stico de turno acababa de decir."
+"Hoy en d�a, cuando las j�venes generaciones (y cuando digo joven, digo �joven� en serio, tipo 17 a�os para abajo, no lo que los medios period�sticos consideran un �escritor joven� o un �director joven�, juventud muy el�stica que puede extenderse hasta los sesenta a�os) quieren borrar con el codo lo que dicen con la mano, dicen �arre�. Por ejemplo, agarran y dicen �Los comentaristas de blogs, esos s� que son grosos. Arre�. Esta palabrita, �arre�, funciona de reemplazo a esa catarata de informaci�n gestual y vocal de la que hablaba al principio. De sostenerse en el tiempo, har� completamente innecesaria la realizaci�n de muecas, y por fin, seremos tan expresivos como un escandinavo."
+"No seamos muy duros con los j�venes: la palabra �arre� es un idiotismo derivado del �lenguaje flogger� (seg�n una breve investigaci�n, su etimolog�a viene, viene de �ahh, re que no�), donde justamente se utizaba originalmente como �warning de sarcasmo�, para evitar malentendidos."
+"�No les recuerda (y tal vez les despierte cierta envidia por la practicidad del admin�culo) a las constantes ofensas inintencionadas, bodrios intercambios de aclaraciones, disculpas, �yo no quise decir lo que dije� y �yo no dije que vos hayas querido decir lo que vos dijiste sino todo lo contrario� que solemos leer y experimentar en blogs y foros de gente mayor? Desde luego, los adultos no estamos a salvo de la inc�moda inexpresividad de nuestro medio. Pocas cosas hay tan escalofriantes como nuestros �jajaja� escritos con un rostro absolutamente p�treo e inexpresivo; no necesitamos expresar simpat�a, odio, celos, alegr�a, ternura, malicia ni amarga indiferencia hacia el interlocutor que est� detr�s de la pantalla del monitor. Y quiere la Naturaleza Humana que cuando no necesitamos hacer algo, no lo hacemos. As� que expresamos nuestros peores instintos y mejores deseos a trav�s de la escritura, pero con una cara de leer avisos clasificados que no le hace el m�s m�nimo honor a estos humanos sentimientos."
+"El problema es que, as� como nuestra carencia de herramientas idiom�ticas floggers nos mantiene toscos en la conversaci�n cibern�tica, nuestros j�venes sim�tricamente han empezado a utilizar esta suerte de emoticones en el habla cotidiana; �en cu�ntas generaciones el �habla emoticona� dejar� sin capacidad de expresi�n gestual a la raza humana? �Llegaremos a vivir en un mundo liso y rosa, donde los seres humanos no pueden poner �cara de Robert de Niro�, abrir las aletas de la nariz en se�al de ira contenida o elevar una ceja demsotrando desconfianza? �Desaparecer�n las patas de gallo y �l�neas de expresi�n�, d�ndonos aspecto de beb�s monstruosos y encanecidos?"
+"En ese mundo, los hombres hablar�n de nosotros como de esos Superhombres capaces de haza�as incre�bles, como fruncir la nariz o ponernos bizcos. Jim Carrey ser� para ellos una suerte de Dios (incluso creer�n que la pel�cula �Todopoderoso� es un documental) e Ismael Echeverr�a, su profeta. Desde luego, el gesto facial como demostraci�n de afecto desaparecer� por completo, perdiendo la actividad er�tica un 76 % de atractivo y reduciendo la tasa de natalidad, y el amor volver� a expresarse por la espalda como en la era de las cavernas. Desaparecida la risa (reemplazada por un �he encontrado eso muy gracioso� expresado verbalmente), tambi�n desaparecer�n los c�micos stand-up, ya que los chistes sin un ruido que los acompa�e colapsan sobre s� mismos. Por �ltimo, sin el ejercicio de los m�sculos faciales la tonicidad muscular de las caras de nuestros bisnietos desaparecer� a los 25 a�os de edad, con el consiguiente peligro de desprendimiento de nariz, etc."
+"La �nica forma de evitar esto es exigir que la escuela p�blica incorpore �Expresividad Facial� como materia O-BLI-GA-TO-RIA, donde se les ense�e a los educandos a bajar el extremo central de las cejas para denotar enojo, hacer una boquita tipo Francella para expresar temor, y otros. Eso, o que se empiecen a fabricar caretas con distintas expresiones, que todo el mundo tenga a mano para las diferentes ocasiones. No s�, esto tambi�n estar�a bueno, aparte si quer�s enojarte mucho te pod�s poner una cara de Mr. T.";

String longText4 = " �Proponen �ellos� unos programas metaf�sicos de TV!"
+"Entendemos que las nuevas tecnolog�as (microc�maras capaces de meterse dentro de las arterias humanas, edici�n digital, c�maras infrarrojas, c�maras que tiran vision cal�rica, etc.) no est�n aprovechadas a full, al m�ximo, al 100 %. Por eso �Yo contra el Mundo� propone los siguientes �programas de televisi�n metaf�sicos�:"
+"360 �: Se trata de una serie de ficci�n al estilo de �24 horas�, pero en lugar de seguir las peripecias del protagonista segundo a segundo, se toma un segundo en la vida del protagonista y se ve todo lo que est� pasando a su alrededor. Nos referimos a todo su alrededor, es decir, al mundo. Por ejemplo: El protagonista est� en una terraza, apuntando con un rifle al Presidente de los Estados Unidos. La imagen se congela y empezamos a alejarnos del tipo en leve movimiento espiralado: Se ve al propio Presidente, a sus ministros, a la multitud que lo ovaciona o abuchea, luego al verdulero que est� en la otra uadra, tambi�n al carnicero, y al peluquero y al ferretero y al boticario la se�ora que tiene una diet�tica, y a la gente que vive en el edificio donde est�n instalados esos comercios, visitando departamento por departamento a todos y cada uno de los inquilinos, a la Sra. Tincasetti, al matrimonio Ochmoneck, a los Brady y a los O�Malley. Luego vamos al edificio de enfrente y vemos al Sr. Gonz�lez, a la Sra. Rachmaninoff, a la familia Mulligan y a los Corleone. Luego pasamos a los barrios vecinos, a visitar cada uno de los departamentos de los edificios de las calles de esos barrios. Vemos el departamento del Sr. Muchnick, de la familia P�rez y de los ortega y los Rabufetti y los Ostertag. S�, s�, a los Frumdiltong tambi�n. Y tambi�n a los O� Franganngangan y a los Schortknijyyytcknik y a los Wilamssumburgomgurgurgurg tambi�n, y a los Bopprenheimerrerr y los Tyonghiu y los Prrrohumghui y los Blogrodbtopp. A todos. Luego vamso a las ciudades vecinas. Bueno, y as�."
+"Como el factor que estamos cubriendo es el espacio y no el tiempo, el programa deber�a tener una duraci�n temporal de cero. Pero sabemos que esto es imposible, debido a los c�digos de la televisi�n. As� que proponemos que dure un segundo. �C�mo? Gracias a la magia de la c�mara r�pida (la tecnolog�a digital moderna ha hecho posible gracias a unas cpamras r�pidas muy muy r�pidas). La ventaja de esta duraci�n es que permite que el programa tenga mucha publicidad, aumentando las ganancias un 78.64.995649 %%%%% por ciento."
+"��QU� GRACIOSO ES ESTO!�: Se trata de uno de esos programas humor�sticos con p�blico en vivo, donde cada tanto enfocan a la gente riendo o sonri�ndose o aplaudiendo. La diferencia es que en este programa, s�lo se muestra eso. A la gente ri�ndose. Mediante un sofisticado sistema de audio que incluye un botocito de �off�, no se nos permite escuchar lo que dicen los protagonsitas del programa, sino solamente las risas. El resultado es un �viaje� tragic�mico y angustiante, en el que, como los habitantes de la caverna de Plat�n, no vemos la luz sino las sombras que �sta produce, o algo as� (no recuerdo c�mo era exactamente. Tampoco recuerdo si era Plat�n. Esto es televisi�n, amigos). No vemos lo que produce la risa, sino sus resultados, y durante una horita minutos m�s minutos menos, nos sentimos angustiad�simos, pensando en lo que nos estaremos perdiendo. Porque adem�s, uno de los requisitos es que que el programa �el que no vemos- sea buen�simo, como para que la gente se haga encima de la risa sin parar. De cualquier modo, lo m�s probable es que, por un fen�meno de simpat�a o tal vez histeria, nos terminemos riendo igual (tambi�n hay grandes probabilidades de que �ste se convierta en uno de esos programas recomendados por drogones, esos de que �uh, ayer me clav� un faso y me vi ��Qu� gracioso es esto!�, no sab�s�). El programa se financia, justamente, con la venta de droga."
+"�MY TRUE LIFE�: Se trata de uno de esos realities donde vemos 24 horas en la vida de alguien a trav�s, con una camarita pegada con la Gotita en la frente, o cosida en las c�rneas, o con unostacos fischer y unos tornillos de media pulgada en el tabique de la nariz. O alg�n otro m�todo menos cruento, si es que alg�n d�a existe esa tecnolog�a. La cosa es que en este caso, la vida de la persona elegida es la de usted. Entonces pasa todo el d�a con el aparatito ese en los ojos, y se graba c�mo desayuna, c�mo se ducha (se le pide que se envuelva la camarita con una bolsa de Coto), c�mo se toma el colectivo, c�mo pasa ocho horitas fente a la computadora chateando y escribiendo pelotudeces y viendo cosas en Youtube, c�mo vuelve a su casa, c�mo cena, c�mo se lava los dientes y c�mo se queda dormido viendo �Constantine�, y luego despierta en mitad de la noche confuso y sudado. Una vez terminada de editar esta grabaci�n (se le arreglan un par de cositas de audio), al d�a siguiente se le entrega el programa (en un DVD, o un cassette) para que lo vea exclusivamente usted. Entero. Por segunda vez. O sea, pasa las 24 horas del d�a siguiente, viendo exactamente todo lo que vio a trav�s de sus propios ojos el d�a anterior. Hay que aclarar que cada programa viene acompa�ado de unas pildoritas de cianuro. ";
		
String longText5 = " �Denuncian plan yanqui de �obamizaci�n� de la Argentina!"
+"D�A 1 DEL �COMPROMISO PODETI�"
+"Por razones de fuerza mayor, el �Compromiso Podeti� �un larguito todos los d�as- se pospone por el d�a de la fecha. La raz�n es que me ha llegado un mail bastante largo pero que me ha dejado un poco preocupado por las cponsecuencias que puede tener para el destino de nuestro pa�s. Lo transcribo �se me rompi� la tecla de �copy paste�, as� que lo tipeo de vuelta- para que juzguen por ustedes mismos:"
+"Por favor reenv�en este mail a todas sus direcciones. Est� en juego el futuro de todos nosotros y del lugar que le queremos dejar a nuestros hijos y del mundo en general. No s� si esto es una broma o no pero por las dudas lo paso total si es una broma despu�s nos re�mos todos pero no es una broma, m�ndenlo porque los medios lo est�n ocultando y esto SE TIENE QUE SABER."
+"Una periodista muy conocida entre sus allegados cercana auna importante fuente relacionada con una embajada de un pa�s aliado a una agencia de noticias muy cercana a un conocido funcionario del Gobierno ha comentado entre bambalinas ciertas noticias que tienen que ver con el tema de que es mayo y hacen 35 grados a la sombra."
+"La temperatura tendr�a que ver con la estrategia que estar�a siguiendo el Gobierno para enfrentar la crisis del Dengue en nuestro pa�s (ahora que se descubri� que lo de la gripe porcina, creada para que se coma al virus del dengue era un bluff), y que consistir�a en que los miembros del statu quo kirchnerista estar�an esperando �pero esperando con mucho mucho fervor y entusiasmo- que venga el oto�o de una buena vez por todas y arrase con el Aedes Aegpty. Este plan �el de esperar- habr�a calado hondo en la estrategia comunicacional del Gobierno, que se sentir�a identificado con la Rusia Sovi�tica de la II Guerra Mundial por lo de contar como �carta fuerte� con el �General Invierno� en sus intentos por detener a Hitler."
+"Gracias a esta inspiraci�n el Kirchenrismo relanzar�a el Gobierno de la Presidente Cristina Fern�ndez como una Dictadura �Neo Estalinista� (Bajo el lema de �tanto tanto rompeiron las pelotas de que somos autoritarios, bueno, ac� est�, ahora somos estaliniatas, �qu� tul?�), claro que sin genocidios que quedan medio feos y son caros de implementar, ni dictadura del proletariado ni reforma agraria ni comisarios del pueblo que es todo medio dif�cil, sino Estalinistas desde el hecho de decir que son Estalinistas y hacer p�sters con est�tica constructivista. Como contrapartida, se alzar�a contra el Gobierno un sector trostskista del kirchenrismo aduciendo que �mejor que siga haciendo calor, as� la gente gasta menos guita en gas y est� de buen humor�, estrategia que ser�a discutida en el Politbur� con el argumento dial�ctico de que �pero el calor tambi�n te pone de vuen humor�, rebatido a su vez por algunos te�ricos del materialismo hist�rico con que �y, pero no s�, a lo que seguir�an las tesis marxistas de que �yo te prefiero el fr�o al calor toda la vida�, �y, pero y la gente que no tiene casa, preguntale a esa si quiere que venga el invierno�, �ah, bueeeeeeno�, �no s�, no s�, �qu� se yo�, �y bu�h�, �puede ser�, �y, es todo seg�n� e �igual es todo una mierda�."
+"Instalado el Estalinismo en nuestro pa�s se revelar�a la cara oculta del plan, lanzada por la agenda secreta de los republicanos de la Casa Blanca de Bush de Obama para reinstalar la �Guerra Fr�a� �lo que coincidir�a con el tema del invierno-, s�lo que en lugar de promoverla de oeste a este que es medio bodrio porque te agarra el �jet lag� se realizar�a de norte a sur �que aparte los monitos estos del Sur son menos jodidos que los rusos�, como habr�a argumentado la Secretaria de Estado Hillary Clinton. Reinstalada la Guerra Fr�a, se revitalizar�a la fabricaci�n ya no de misiles �lo que ir�a en contra del paulatino proceso de desarme nuclear programado para el a�o 23.678- sino de pinzas pico de loro gigantes para hacer girar las plataformas de misiles y que apunten para este lado, con la consecuente revitalizaci�n de la indistria sider�rgica."
+"Con este fin, los testaferros de la CIA Benetton, Robert Duvall y Douglas Thompkins habr�a comprado 20.000 kil�metros de territorio argentino pegaditos a la cordillera, a donde se habr�an instalado enormes estufas �colocados sobre la Cordillera justamente para que sean de tiro balanceado- gracias a las cuales estar�an extendiendo este Verano Eterno hasta los l�mites que hoy sufren miles de compatriotas."
+"Como plus, esta estrategia de �Calefacci�n Regional� apuntar�a a terminar de romper el Glaciar Perito Moreno, que no se rompe m�s y ya mjedio que cans�, y de paso eliminar de un plumazo el turismo del principal �Feudo K.�, lo que llevar�a la cadena de Hoteles Kirchner a la ruina. De este modo la agenda del Pent�gono de los Halcones Dem�cratas de George W. Bysh de Barak Obama buscar�an crear un a �glasznost� y una �perestroika� dentro de la estalinizaci�n kircherista, buscando el ascenso del empresario Francisco De Narv�ez, que ser�a presentado como el �Obama Sudamericano� en su rol de representante de una etnia discriminada (los pelirrojos) y crear el contexto para una obamizaci�n de la pol�tica argentina, conocida como denarvaezisaci�n. Se vivir� una euforia de reivindicaci�n �colorada� y veremos spots publicitarios de pelirrojos famosos (por ejemplo, el jugador McAllister) llorando y diciendo �nunca cre� que llegar�a este momento�. Se aprovechar� toda esta confusi�n para que el nuevo Gobierno haga espectaculares anuncios de corte progresista como el retiro de tropas argentinas de las Malvinas y el cierre del Penal de la Isla Mart�n Garc�a."
+"Entonces llegar� el momento de la estocada final del plan elaborado por el �ala dura� del Obamismo norteamericano dela CIA de la casa B�anca de Bush de Obama de la CIA de la Casa Blanca de los republicanos de George W. Bush de Obama: la fabricaci�n de picos de loro gigantes para zurdos, que cumplir�n la funci�n de volver a girar los misiles nuevamente para el otro lado y as� revitalizar la Industria sider�rgica."
+"Entonces se revelar� el verdadero objetivo de la Agenda estadounidense nortamericana de USA de EEUU de los US de los EU de Bush de Obama de Reagan rep�blicanos de los halcones de las palomas del FBI de la derecha de la NRA de la agenda del �ala dura� de Obama de George W. Bush: la invasi�n de la argentina, a trav�s de la excusa de salvar al pueblo norteamericano de la locura del denarvaizmo (ya que Francisco De narv�ez se volver� loco a los dos d�as de asumir, como se desprende observando sus ojillos de psic�pata) que pretende instalartle un �mapa de la inseguridad� en el recto a los ciudadanos argentinos. �El objetivo oculto? Llevarse los meteoritos que abundan en Chaco, cuyo territorio habr� sido desmalezado y despoblado a trav�s de la epidemia de dengue (que en realidad ser�a gripe porcina disfrazada, por lo que el uso de repelente ser�a totalmente in�til), con el objetivo de extraer su hierro para usos sider�rgicos con el fin de fabricar picos de loro gigantes para girar platarofrmas de misiles."
+"�El fin? Vender el remanente de estas herramientas gigantes a los plomeros gigantes que ser�n requeridos en un par de meses para erreglar la termocupla y la electrov�lvula de las estufas instaladas en la Cordillera y utilizar ese dinero para palirar la Crisis Financiera. ";

String longText6 = " �Lanzan Instructivo para encuentros en la calle!"
+"Cosas para decir cuando se encuentran dos personas que llevan un beb� cada una:"
+"-��Qu� hermoso!�"
+"-��Qu� grande que est�!�"
+"-�S�, es una bestia.�"
+"-�Este come como un energ�meno.�"
+"-�Es igual a vos, eh.�"
+"-�Los ojos son de la madre.�"
+"-�La nariz es de la madre.�"
+"-�El pelo es de la madre.�"
+"-�Las orejas son de la madre.�"
+"-��Ya habla?�"
+"-�S�, dice �aua�, �com�ee�, �guau� y �queo baj�.�"
+"-�El m�o dice �plota�, �tita�, �a�al��, �al�� y �om�.�"
+"-��Y ya camina?�"
+"-�S�, s�, no para, te agota�."
+"-�El m�o est� ahi, est� ah�.�"
+"-�Y te deja dormir?"
+"-�Y, a veces, ja, ja, ja.�"
+"-�El m�o duerme toda la noche, como un angelito.�"
+"-�No, el m�o tambi�n, el m�o tambi�n, eh.�"
+"-��Y ya dej� el pa�al?�"
+"-�Le falta, le falta, est� ah�, est� ah�, es cosa de un par de d�as.�"
+"-�El m�o tambi�n, est� ah�, est� ah�, yo creo que es cosa de menos de un d�a. Igual no lo quiero apurar porque estuvo mal de la panza y le est� saliendo como una caca verde.�"
+"-�Ah, al m�o, tambi�n, la otra vez le sali� como una caca verde. No te preocupes, no es nada.�"
+"-�Chau, saludos, eh, est� herrrrmoso, eh.�"
+"-�El tuyo tambi�n, eh, herrrrrmoso, muy lindo, eh.�"
+"Cosas que no decir: �Epa, fulerita la criatura. Igual lo importante es que sea sanito.�, ��NO CAMINA todav�a? �Lo llevaste a un especialista? Te digo porque capaz que est�s a tiempo�, �Yo no me quiero meter en c�mo lo cuid�s al pibe pero yo lo pondr�a a r�gimen�, etc."
+"Cosas para decir cuando se encuentran dos personas que llevan un perro cada una:"
+"-��Qu� hermoso!�"
+"-��Qu� grande que est�!�"
+"-�S�, es una bestia.�"
+"-�Este come como un energ�meno.�"
+"-��Ya aprendi� a hacer caca afuera?�"
+"-�No, me deja cada regalito.�"
+"-�Ah, el m�o el otro d�a me dej� un tereso adentro del zapato. Lo-que-r�-a-ma-tar, ja, ja, ja.�"
+"-��Ya ladra?�"
+"-��Ya camina en cuatro patas?�"
+"-��Y qu� tipo de perro es?�"
+"-�Es un Golden Retriever Daschund Airedale Pitbull terrier Shetland. Son re buenos con los chicos.�"
+"-�Ah, igual hay que tener cuidado. El perro tiene que aprender su lugar en la casa.�"
+"-�S�, el perro tiene que aprender su lugar en la casa. Por ejemplo, no lo dejamos m�s que se suba a la mesa a comer de nuestro plato.�"
+"-�S�.�"
+"-�Y si te chumba, �cazote!�"
+"-�S�, yo le pego con un diario en la cola.�"
+"-�Yo uso una barreta de hierro. Lo que pasa es que este tipo de perro no siente el dolor.�"
+"-�El m�o es un callejero, dicen que SON M�S INTELIGENTES QUE LOS DE RAZA (mirando para otro lado, haci�ndose el boludo).�"
+"-�S�, bueno, cada perro tiene su personalidad. Lo bueno es que el perro de raza ya sab�s c�mo te viene. Lo otro es una loter�a. Capaz que TE SALE UN PERRO ASESINO Y ES MEDIO UNA IRRESPONSABILIDAD TENERLO (mirando para otro lado, haci�ndose el boludo).�"
+"-�S�, en fin, cada perro tiene su personalidad. Depende c�mo lo eduques.�"
+"-�Chau, saludos, eh, est� herrrrmoso, eh.�"
+"-�El tuyo tambi�n, eh, herrrrrmoso, muy lindo, eh.�"
+"Cosas que no decir: �Es igual a vos, eh�, ��Vos PAGASTE por ese perro? �Con la cantidad de perritos abandonados que hay? Y bu�h�, �Yo con mi plata hago lo que quiero, hijo de puta�."
+"Cosas para decir cuando se encuentra una persona con un perro y una persona con un beb�:"
+"-��Qu� hermoso!�"
+"-��Qu� grande que est�!�"
+"-�S�, es una bestia.�"
+"-�Este come como un energ�meno.�"
+"-��Y ya habla? Lo digo por el beb�.�"
+"-�S�, dice� aua�, �com�ee�, �guau� y �queo baj�.�"
+"-��Y el tuyo, ya ladra? Lo digo por el perro.�"
+"-�S�, dice �guau, guau, guau��"
+". -��Y hace caca afuera? Lo digo por el perro.�"
+"-�No, me hace cada enchastre, ja, ja, ja.�"
+"-��Y todav�a usa pa�al? Lo digo por el beb�.�"
+"-�S� hace cada enchastre. Por suerte, adentro del pa�al.�"
+"-��Y ya camina? Lo digo por el beb�.�"
+"-�El m�o �que es un perro- ya camina. Y eso que tiene dos meses nada m�s.�"
+"-�Claro, porque es un perro.�"
+"-�Claro, claro, es otra cosa.�"
+"-��Y te deja dormir?�"
+"-�Y, a veces lo tengo que pasar a la cama. Lo digo por el beb�.�"
+"-�Y, a veces lo tengo que entrar. Lo digo por el perro. A la cama no, porque el perro tiene que saber su lugar, sino despu�s te pasan por encima.�"
+"-�S�, s�, porque es un perro. El m�o es un beb�.�"
+"-�S�, s�.�"
+"-�No es lo mismo.�"
+"-�No, claro, claro.�"
+"-��Y tiene todas las vacunas?�"
+"-�Le falta la del moquillo. Me refiero, claro, al perro.�"
+"-�A �l le falta la Sabin. Me refiero, claro, al beb�.�"
+"-�Se te re parece, eh. Me refiero, claro, al beb�.�"
+"-�S�. �l tambi�n se te re parece. Me refiero al perro. Porque dicen que los perros se parecen a sus due�os.�"
+"-�Puede ser, puede ser, ja, ja. Es un Golden Retriever Daschund Airedale Pitbull Terrier Shetland. Son re buenos con los chicos.�"
+"-��l, directamente, es un chico.�"
+"-�S�.�"
+"-�Lo importante es que no sea agresivo. Me refiero al perro.�"
+"-�S�, depende c�mo lo eduques. Me refiero al perro y tambi�n al beb�.�"
+"-�S�, pero no es lo mismo. Uno es un perro y el otro es un ser humano.�"
+"-�S�.�"
+"-�Chau, saludos, eh, est� herrrrmoso, eh.�"
+"-�El tuyo tambi�n, eh, herrrrrmoso, muy lindo, eh.�"
+"Cosas que no decir: �Lo que yo no entiendo es c�mo alguien puede traer un chico a este mundo lleno de Guerras y Desesperanza�, �Y a ver cu�ndo nos dejamos de joder con el perrito y ten�s un hijo, mir� que se te va el tren, eh�, etc."
+"Cosas para decir cuando se encuentra una persona con un beb� y una persona con un anciano:"
+"-��Qu� hermoso!�"
+"-��Qu� grande que est�! Lo digo por el beb�"
+"-��Qu� delgado, empeque�ecido y enjuto que est�! Lo digo por el anciano.�"
+"-��Y ya camina? Me refiero, claro, al beb�.�"
+"-��Y todav�a camina? Me refiero, claro, al anciano.�"
+"-��Y todav�a usa pa�al? Me refiero, claro, al beb�.�"
+"-�Y ya usa pa�al? Me refiero, claro, al anciano.�"
+"-�Le falta, le falta, es cosa de un par de d�as. Me refiero al beb� y tambi�n, desgraciadamente, al anciano�"
+"-��Y te deja dormir?�"
+"-�Y, a veces lo tengo que pasar a la cama. Estoy hablando del beb�.�"
+"-�Y, aveces le tengo que dar medio Lexotanil. Estoy hablando del anciano.�"
+"-��Come papilla?"
+"-�S�.�"
+"-��Y ya le sali� alg�n diente? Lo digo por el beb�.�"
+"-��Y le queda alg�n diente? Lo digo por el anciano.�"
+"-�Es re parecido a vos, eh.�"
+"-�Los ojos son de la madre, es decir, mi esposa. Me refiero al beb�.�"
+"-�Los ojos son de la madre, es decir, mi bisabuela. Me refiero al anciano.�"
+"-��Y cu�nto tiempo tiene?�"
+"-�Un a�o y medio. El beb�.�"
+"-�Ciento tres a�os. El anciano.�"
+"-�Lo importante es que no sea agresivo. Me refiero al beb� y, por qu� no, tambi�n al anciano.�"
+"-�Depende c�mo lo eduques. Me refiero al beb�.�"
+"-�Depende de c�mo est� de la cocorota. Me refiero al anciano.�"
+"-��Y de la pr�stata c�mo anda?�"
+"-��El beb� o el anciano?�"
+"-�No s�, cu�l es el tuyo, el anciano?"
+"-�S�.�"
+"-�Entonces, el anciano.�"
+"-�Diez puntos.�"
+"-�El beb� tambi�n. Bah, el pediatra no dijo nada. Cuando no dice nada es porque est� bien.�"
+"-�S�, cuando el geront�logo no dice nada es porque tambi�n est� bien.�"
+"-�Chau, saludos, eh, est� herrrrmoso, eh.�"
+"-�El tuyo tambi�n, eh, herrrrrmoso, muy lindo, eh.�"
+"Cosas que no decir: ��Y cu�nto le quedar�? El anciano, digo�, �Y vos ahora hasta dentro de cuarenta a�itos ni un minuto de tranquilidad, �no?�, etc."
+"Cosas para decir cuando se encuentra una persona con un perro, una persona con un beb� y una persona con un Golem:"
+"-��Qu� hermoso! Lo digo por el beb� y por el perro, no as� por el Golem�"
+"-��Qu� grande que est�! Lo digo por el beb�, el perro y el Golem.�"
+"-�S�, es una bestia (los tres).�"
+"-��Y ya camina?�"
+"-�Est� ah�, est� ah�. Lo digo por el beb�.�"
+"-�S�, ya camina, y eso que s�lo tiene dos meses. Lo digo por el perro.�"
+"-�S�, ya camina, y eso que s�lo tiene unas Horas de hab�rsele dado el Don de la Animaci�n. Lo digo por el Golem.�"
+"-��Ya habla? Lo digo por el beb� y el Golem.�"
+"-�S�, dice �aua�, �com�ee�, �guau� y �queo baj�. Me refiero, claro, al beb�.�"
+"-�No, este es del tipo de Golems que no hablan. Me refiero, claro, al Golem.�"
+"-��Y hace caca afuera?"
+"-��Lo dec�s por el beb�, el perro o el Golem?�"
+"-�Lo digo por los tres.�"
+"-�El perro a�n no, el beb� no tiene por qu� hacerlo y el Golem no hace caca de ning�n tipo.�"
+"-�Ah, OK.�"
+"-��Y el tuyo obedece ciegamente todas y cada una de tus �rdenes?�"
+"-��Lo dec�s por el beb�, el perro o el Golem?�"
+"-�Los tres.�"
+"-�El perro no porque es cachorrito, el Golem s� y el beb� es un ser humano y debe ser educado en el uso responsable de la Libertad, no en la Obediencia Ciega.�"
+"-�Ah, te cre�s que el beb� es mejor, �eh?"
+"-�S�,es un ser humano. Es mejor.�"
+"-(El del perro masculla con rabia)"
+"-��Y el tuyo mata ante la orden de ��Mata!�? Me refiero al beb� y al perro.�"
+"-�No, es cachorrito. Me refiero al perro. Cuando crezca, veremos.�"
+"-�No. Me refiero al beb�.�"
+"-�El m�o s�. Me refiero al Golem.�"
+"-�S�.�"
+"-�En este punto en particular, ponele, el Golem es superior al beb� y en menor medida al perro.�"
+"-�No es as�, un ser humano es un ser humano.�"
+"-�Depende c�mo lo eduques.�"
+"-�No, no, no, yo privilegio al Humano. No vas a comparar con un animal y con un monigote horrible de barro.�"
+"-��MATA! Me refiero, claro, al Golem.�"
+"Cosas que no decir: ��Y qui�n lo hizo al Golem este? �Un ciego?�, etc."
+"Cosas para decir cuando se encuentra una persona con un perro, una persona con un beb�, una persona con una Estatua Viviente, una persona con una Avispa, una persona con Pap� Noel y una persona con una persona."
+"-��Qu� hermoso! Lo digo por el beb�, por el perro, la Estatua Viviente, la persona y en menor medida por la Avispa, porque los bichos me impresionan�"
+"-��Qu� grande que est�! �dem.�"
+"-��Ya habla? Me refiero al beb�, no as� a la Avispa, que no habla, ni a Pap� Noel y a la persona, que ya deber�an hablar desde hace tiempo, ni a la Estatua Viviente, que no deber�a hablar bajo ning�n concepto.�"
+"-�S�, dice �aua�, �com�ee�, �guau� y �queo baj�."
+"-�Bueno, mi Estatua Viviente a veces habla, pero por error.�"
+"-��Y ya camina? Me refiero al beb�, no as� a la Avispa, que no camina, ni a Pap� Noel y a la persona, que ya deber�an caminar desde hace tiempo, ni a la Estatua Viviente, que no deber�a caminar bajo ning�n concepto.�"
+"-�S�, no para, es una m�quina (el beb�).�"
+"-�Una cosa, mi Estatua Viviente caminaba, cuando laburaba de Mimo. Pero no hablaba.�"
+"-�Y la Avispa camina, �claro que camina! Tiene patas.�"
+"-��Y ya vuela? Me refiero a la Avispa, y en menor medida a Pap� Noel, que no vuela per se.�"
+"-�S�, vuela. Lo digo por la Avispa.�"
+"-�S�, bueno, se puede decri que Pap� Noel tambi�n vuela. No s� si per se, pero vuela.�"
+"-��Y ya se recibi� de abogada? Lo digo por la persona.�"
+"-�No, pero es que no estudia para abogada.�"
+"-��Y qu� estudia?�"
+"-��El perro, el beb�, la Estatua Viviente, la Avispa, pap� Noel o la persona?� -�La persona, la persona.�"
+"-�Ah, ah.�"
+"-�(Resopla) Ven�amos hablando de la persona, �no? �De qui�n voy a hablar?�"
+"-�Bueno, tranquilizate.�"
+"-��Entonces al final qu� estudia?�"
+"-�No s�. Myriam, �qu� estudi�s?�"
+"-�Fonoaudiolog�a.�"
+"-��Ah, HABLA la persona!�"
+"-�Claro que habla, si dijimos que hablaba! Qued� claro que hablaba.�"
+"-�Claro que hablo, hablo mejor que todos ustedes juntos, estudio fonoaudiolog�a.�"
+"-�Yo tambi�n hablo. Lo digo por m�, es decir, Pap� Noel.�"
+"-��Ya s�! �Ya sabemos que Pap� Noel habla!�"
+"-�Par�, �ahora van a hablar las cosas que vienen con nosotros? Me refiero al beb�, el perro, la Avispa, la Estatua Viviente, Pap� Noel y la persona. Porque si es as� yo agarro y me voy. Lo digo yo, la persona que viene con el beb�.�"
+"-�Aaaah, claro, el pibe se cree mejor que todos nosotros porque est� con un beb�.�"
+"-�Y s�, flaquito, es un ser humano, yo pongo al humano por encima de todo.�"
+"-��Par�! �Y la persona (Myriam)? Tambi�n es una persona.�"
+"-��MATA!�"
+"-�Por qu� lo dec�s? �Por el Golem? �S� el Golem no estaba!!!!�"
+"-�No, lo digo por Pap� Noel. �MATA!�"
+"-�Par�, loco. Y lo digo yo, que soy Pap� Noel. Mir� que, antes que la obediencia ciega, yo he sido educado en la libertad con responsabilidad.�"
+"-�Chau, saludos, eh, est� herrrrmoso, eh.�"
+"-�El tuyo tambi�n, eh, herrrrrmoso, muy lindo, eh.�"
+"-(Esplota todo)"
+"Cosas que no decir: �Qu� beb� m�s feo�, �Qu� perro inmundo�, �Las estatuas vivientes apestan�, �Qu� bicho de mierda la Avispa�, �Pap� Noel no existe�, �La persona es boluda�, etc.";
article = new TextDocument(
				0,longText + longText2 + longText3 + longText4 + longText5 + longText6,
				true);

		insertAllWords(this.sut,article);
	}
	/**
	 * Cierra el archivo y realiza la prueba de obtener todos los registros
	 * 
	 * @throws IOException
	 */
	@Test
	public void testRetrieveAllFromRecentlyOpenBTree() throws IOException {
		Tree<InvertedIndexIndexRecord, StringField> newTree = new Tree<InvertedIndexIndexRecord, StringField>(filemanager.openFile("FullBSTree.dat"), InvertedIndexIndexRecord
				.createRecordFactory(), encoder);
		newTree.load();
		testRetrieveAllRecords(newTree, article);
	}

	/**
	 * Prueba obtener todos los registros
	 * 
	 * @throws IOException
	 * @throws RecordSerializationException
	 */
	@Test
	public void testRetrieveAllRecords() throws RecordSerializationException, IOException {
		testRetrieveAllRecords(this.sut, article);
	}

	public static void insertAllWords(Tree<InvertedIndexIndexRecord, StringField> tree, Iterable<String> words) throws RecordSerializationException, IOException {
		for (String word : words) {
			try{
				if (word.compareToIgnoreCase("texas") == 0)
					System.out.println("insertando: " + word + "\n");
				 tree.insertRecord(new InvertedIndexIndexRecord(word, simulateBlockNumber(word)));
				 System.out.println(tree.toString());
			}catch (TreeDuplicatedRecordException e) {
				//words tiene palabras duplicadas, no interfiere con la prueba
			}
		}
	}

}
