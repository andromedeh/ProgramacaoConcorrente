/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 07/11/2023
* Ultima alteracao.: 16/11/2023
* Nome.............: Music
* Funcao...........: Instanciar atributos  e controlar metodos relacionados 
a reproducao de musicas
************************************************************************ */
package model;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public abstract class Music {

	private static Clip clip; 
	
	public static void play (String fileName) {
		try {
			File audio = new File(Music.class.getClassLoader().getResource(fileName).getFile());
			// -- No if sera verificado se o arquivo existe -- //
			if (audio.exists()) {
				AudioInputStream inptStream = AudioSystem.getAudioInputStream(audio);
				clip = AudioSystem.getClip();
				clip.open(inptStream); // abre arquivo de audio
				clip.loop(Clip.LOOP_CONTINUOUSLY); // fica repetindo
				clip.start(); // da start
			} 
		} catch (Exception e) {} // fim do try-catch
	} // fim do metodo play

	public static void pause() {
		// -- O primeiro if verifica se a variavel 'clip' eh diferente de nulo (caso sim executa o segundo if) -- //
		if (clip != null) {		
			if(clip.isRunning()) { // se ja esta executando, para
				clip.stop();
			} else { // se nao esta, comeca
				clip.start();
			} // fim do else
		} // fim do primeiro if
	} // fim do metodo pause

} // fim da classe
