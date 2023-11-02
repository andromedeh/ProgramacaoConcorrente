/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 27/09/2023
* Ultima alteracao.: 07/10/2023
* Nome.............: ThreadTrem2
* Funcao...........: Controla a thread do trem 2 e as solucoes para a nao-colisao, 
de forma interconectada com a classe controladora de tela
************************************************************************ */
package modelo;
import java.lang.Thread;
import controle.TelaPrincipalController;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

public class ThreadTrem2 extends Thread {
  private TelaPrincipalController controlador;
  private ImageView trem;
  private double velocidade; 
  private double posicaoInicial;
  private double posicaoY; 
  private int sentidoDoTrem; // subida ou descida escolhida pelo usuario na interface
  private int solucao; // escolhida pelo usuario na interface
  private int processo = 1; // variavel usada na solucao de peterson
  private boolean isPaused = false; // flag necessaria nos metodos de pausar thread
  
  public ThreadTrem2 (TelaPrincipalController controlador) {
    this.controlador = controlador;
    this.trem = controlador.getIconTrem2();
    this.posicaoY = trem.getLayoutY();
    this.posicaoInicial = posicaoY;
    this.solucao = controlador.solucaoEscolhida;
  }

  @Override
  public void run(){
    while(true){
      switch(solucao){
        case 1:{
          try {
            solucaoVariavelTravamento();
          } catch (InterruptedException e) {}
          break;
        } // fim do case 1
        case 2:{
          try {
            solucaoEstritaAlternancia();
          } catch (InterruptedException e) {}
          break;
        } // fim fo case 2
        case 3:{
          try {
            solucaoPeterson();
          } catch (InterruptedException e) {}
          break;
        } // fim do case 3
      } // fim do switch
    } // fim do while
  } // fim do run

  public void solucaoVariavelTravamento() throws InterruptedException{
    switch(sentidoDoTrem){
      case 0:{ // trem superior
        trem.setLayoutY(-18); // setar posicao inicial
        descer(26); // regiao nao critica
        verificaRC1(); // verifica VT1 e para o trem se preciso
        controlador.alterarVT1(); // avisa que vai entrar na RC1/tunel1
        descer(175); // movimenta no tunel 1
        controlador.alterarVT1(); // avisa que saiu de RC1
        descer(226); // regiao nao critica
        verificaRC2(); // verifica VT2 e para o trem se preciso
        controlador.alterarVT2();// avisa que vai entrar na RC2/tunel2
        descer(365); // movimenta no tunel 2
        controlador.alterarVT2(); // avisa que saiu da RC2
        descer(460); // regiao nao critica
        posicaoY = -18;
        Platform.runLater( () -> trem.setLayoutY(-18)); // volta pro começo
        break;
      } //fim do case 0

      case 1:{ // trem inferior
        trem.setLayoutY(410); // mesma logica do case 0
        subir(380);
        verificaRC2();
        controlador.alterarVT2(); 
        subir(228);
        controlador.alterarVT2(); 
        subir(190);
        verificaRC1();
        controlador.alterarVT1(); 
        subir(26);
        controlador.alterarVT1(); 
        subir(-70);
        posicaoY = 410;
        Platform.runLater( () -> trem.setLayoutY(410));
        break;
      } // fim do case 1
    } // fim do switch
  } // fim do metodo solucaoVariavelTravamento

  public void solucaoEstritaAlternancia() throws InterruptedException{
    switch(sentidoDoTrem){
      case 0:{ // trem descendo
        trem.setLayoutY(-18); // seta posicao inicial
        descer(26); // desce ate perto do tunel1
        esperaVEZ1(); // espera sua vez no tunel 1
        descer(175); // movimenta no tunel1
        controlador.passarVEZ1(0); // passa a vez quando sai
        descer(228); // desce ate perto do tunel2
        esperaVEZ2(); // espera sua vez no tunel 1
        descer(365); // movimenta no tunel2
        controlador.passarVEZ2(0); // passa a vez quando sai
        descer(460); // movimenta ate fim do percurso
        posicaoY = -18;
        Platform.runLater( () -> trem.setLayoutY(-18)); // volta para o inicio
        break;
      } // fim do case 0

      case 1:{ // trem subindo
        trem.setLayoutY(410); // seta posicao inicial
        subir(380); // sobe ate perto do tunel 2
        esperaVEZ2(); // espera sua vez no tunel 1 
        subir(228); // movimenta no tunel 2
        controlador.passarVEZ2(0); // passa a vez quando sai 
        subir(190); // sobe ate perto do tunel 1
        esperaVEZ1(); // espera sua vez no tunel 1
        subir(26); // movimenta no tunel 1
        controlador.passarVEZ1(0); // passa a vez quando sai
        subir(-70); // movimenta ate fim do percurso
        posicaoY = 410;
        Platform.runLater( () -> trem.setLayoutY(410)); // volta para o inicio
        break;
      }// fim do case 1
    } // fim do switch
  } // fim do metodo solucaoEstritaAlternancia

  public void solucaoPeterson() throws InterruptedException{
    switch(sentidoDoTrem){
      case 0:{ // trem descendo
        trem.setLayoutY(-18); // seta posicao inicial
        descer(26); // desce ate perto do tunel 1
        entrarTunel1();
        descer(175); // movimenta no tunel 1
        sairTunel1();
        descer(226); // desce ate perto do tunel 2
        entrarTunel2();
        descer(365); // movimenta no tunel2
        sairTunel2();
        descer(460); // movimenta ate fim do percurso
        posicaoY = -18;
        Platform.runLater( () -> trem.setLayoutY(-18)); // volta para o inicio
        break;
      } // fim do case 0

      case 1:{ // trem subindo
        trem.setLayoutY(410); // seta posicao inicial
        subir(380); // sobe ate perto do tunel 2
        entrarTunel2();
        subir(228); // movimenta no tunel 2
        sairTunel2();
        subir(190); // sobe ate perto do tunel 1
        entrarTunel1();
        subir(26); // movimenta no tunel 1
        sairTunel1();
        subir(-70); // movimenta ate fim do percurso
        posicaoY = 410;
        Platform.runLater( () -> trem.setLayoutY(410)); // volta para o inicio
        break;
      }// fim do case 1
    } // fim do switch
  } // fim do metodo solucaoPeterson

  //---------------------------- metodos p/ variavel de travam. ---------------------------------
  public void verificaRC1(){
    while(controlador.getVT1() == 1){
      try {
        if (controlador.isReseted()){ // garantir que ele resete a posicao quando estiver em sleep
          controlador.setReseted(false);
          break;
        }
        sleep(1);
      } catch (InterruptedException e) {}
    } // fim do while
  } // fim do metodo verificarRC1

  public void verificaRC2(){
    while(controlador.getVT2() == 1){
      try {
        if (controlador.isReseted()){ // garantir que ele resete a posicao quando estiver em sleep
          controlador.setReseted(false);
          break;
        }
        sleep(1);
      } catch (InterruptedException e) {}
    } // fim do while
  } // fim do metodo verificarRC2
  
    //---------------------------- metodos p/ estrita alternancia ---------------------------------
  public void esperaVEZ1(){
    while(controlador.getVEZ1()!=1){ 
      try {
        if (controlador.isReseted()){ // garantir que ele resete a posicao quando estiver em sleep
          controlador.setReseted(false);
          break;
        }
        sleep(1);
      } catch (InterruptedException e) {}
    } // fim do while
  } // fim do metodo esperarVEZ1

  public void esperaVEZ2(){
    while(controlador.getVEZ2()!=1){ 
      try {
        if (controlador.isReseted()){ // garantir que ele resete a posicao quando estiver em sleep
          controlador.setReseted(false);
          break;
        }
        sleep(1);
      } catch (InterruptedException e) {}
    } // fim do while
  } // fim do metodo esperarVEZ2

  //--------------------------- metodos da solucao de peterson --------------------------------
  public void entrarTunel1(){ 
    int other;
    other = 1 - processo;
    controlador.setInteresed1(processo, true);
    controlador.setTurno1(processo);
    while(controlador.getTurno1() == processo && controlador.getInteresed1(other) == true){
      try {
        if (controlador.isReseted()){ // garantir que ele resete a posicao quando estiver em sleep
          controlador.setReseted(false);
          break;
        }
        sleep(1);
      } catch (InterruptedException e) {}
    } // fim do while
  } // fim do metodo entrarTunel1 

  public void entrarTunel2(){ 
    int other;
    other = 1 - processo;
    controlador.setInteresed2(processo, true);
    controlador.setTurno2(processo);
    while(controlador.getTurno2() == processo && controlador.getInteresed2(other) == true){
      try {
        if (controlador.isReseted()){ // garantir que ele resete a posicao quando estiver em sleep
          controlador.setReseted(false);
          break;
        }
        sleep(1);
      } catch (InterruptedException e) {}
    } // fim do while
  } // fim do metodo entrarTunel2

  public void sairTunel1(){
    controlador.setInteresed1(processo, false);
  } // fim do metodo sairTunel1

  public void sairTunel2(){
    controlador.setInteresed2(processo, false);
  } // fim do metodo sairTunel2

  //-------------------------- metodos de movimentacao/animacao ------------------------------
  // movimenta o trem para baixo, incrementando o valor da posicao Y
  public void descer(double posicaoParada) throws InterruptedException{
    posicaoY = trem.getLayoutY();
    while (posicaoY != posicaoParada) {
      if (velocidade == 0) {
        synchronized (controlador.pauseLock) { // metodo proprio de thread p/ sincronizar
          isPaused = true;
          controlador.pauseLock.wait(); // pausa a thread quando a velocidade for == 0
        } // fim do synchronized
      } // fim do if
      sleep(1000 / (long) velocidade);
      Platform.runLater(() -> {trem.setLayoutY(posicaoY);});
      posicaoY++;
    } // fim do while
  } // fim do metodo descer

  // movimenta o trem para cima, decrementando o valor da posicao Y
  public void subir(double posicaoParada) throws InterruptedException {
    posicaoY = trem.getLayoutY();
    while (posicaoY != posicaoParada) {
      if (velocidade == 0) {
        synchronized (controlador.pauseLock) { // metodo proprio de thread p/ sincronizar
          isPaused = true;
          controlador.pauseLock.wait();
        } // fim do synchronized
      } // fim do if
      sleep(1000 / (long) velocidade);
      Platform.runLater(() -> {trem.setLayoutY(posicaoY);});
      posicaoY--;
    } // fim do while
  } // fim do metodo subir

  // metodo relacionado com a pausa de thread no controlador, seta a variavel isPaused
  public void pauseThread() {
    isPaused = true;
  } // fim do pauseThread()

  // metodo para retomar a thread
  public void resumeThread() {
    synchronized (controlador.pauseLock) {
      isPaused = false;
      controlador.pauseLock.notifyAll();
    } // fim do synchronized
  } // fim do resumeThread

  //metodo para resetar a thread
  public void resetar() throws InterruptedException {
    Platform.runLater(() -> {trem.setLayoutY(posicaoInicial);});
    this.interrupt(); // interrompe
    sleep(100);
  } // fim do resetar
  
  // ---------------------------- getters e setters --------------------------------
  public boolean isPaused() {
    return isPaused;
  }

  public void setPaused(boolean isPaused) {
    this.isPaused = isPaused;
  }

  public void setVelocidade(double velocidade){
    this.velocidade = velocidade;
  }

  public void setSentidoDoTrem(int sentidoDoTrem) {
    this.sentidoDoTrem = sentidoDoTrem;
  }

  public void setSolucao(int solucao) {
    this.solucao = solucao;
  }
} // fim da classe ThreadTrem2
