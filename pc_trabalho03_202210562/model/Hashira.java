/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 13/10/2023
* Ultima alteracao.: 27/10/2023
* Nome.............: Hashira
* Funcao...........: Define os metodos e atributos de cada Hashira de forma
interconectada com a classe controladora de tela.
************************************************************************ */
package model;
import java.util.concurrent.Semaphore;
import control.MainController;
import javafx.application.Platform;

public class Hashira extends Thread {
  private MainController controller = new MainController();
  private static final int N = 5; // qtd de Hashiras
  private final int id; // identificador do hashira
  private int left; // vizinho da esquerda
  private int right; // vizinho da direita
  private static final int THINKING = 0;
  private static final int STARVING = 1;
  private static final int EATING = 2;
  public static int[] state = new int[N]; // estado dos hashiras
  public static Semaphore mutex = new Semaphore(1); 
  public static Semaphore[] semaphores = new Semaphore[N]; // um semaforo por hashira
  private final static int TIME = 4000; // tempo geral das atividades de comer e pensar
  private long speedThink = 1; // velocidade pensar inicial
  private long speedEat = 1; // velocidade comer inicial
  private int contSpeedThink = TIME; // contador usado p/ acelerar a velocidade de comer
  private int contSpeedEat = TIME; // contador usado p/ acelerar a velocidar de pensar
  private int flag = 0;  // flag para interromper a thread
  private boolean isPaused = false; // flag necessaria em metodo de pausar a thread

  public Hashira(int id, MainController controller) {
    this.id = id; // numero do Hashira
    this.controller = controller;  
    this.left = (id + N -1) % N; // vizinho esquerda
    this.right = (id + 1) % N; // vizinho direita
    this.flag = 0; 
  }

  public void run() {
    while (flag >= 0) { // flag para conseguir interromper a thread
      synchronizedPause(); // metodo p sincronizar a pausa
      if (flag==0){
        think(); // hashira pensando
      }
      if (flag == 1){
        getForks(); // adquire  2 hashi ou fica bloqueado
      }
      if (flag == 2){
        eat(); // hashira comendo
      }
      if (flag == 3){
        flag = -1;
        leaveForks(); // coloca ambos os hashi na mesa
      }
      flag++;
    } // fim do while
    System.out.println("Hashira " + id + " devorado por um ONI (Thread Interrompida)!");
  } // fim do run

  /*********************************************************************
  * Metodo: think
  * Funcao: representacao do filosofo - Hashira pensando
  muda o estado do Hashira com identificador id para pensando, de forma a 
  chamar metodos p interface que mostram os elementos de pensamento
  durante um determinando tempo que pode ser modificado atraves do speedThink
  * Parametros: nenhum
  * Retorno: nenhum
  ******************************************************************* */
  private void think() {
    try {
      state[id] = THINKING;
      Platform.runLater(() -> {  // Platform p/ melhorar a atualizacao da interface
        showIsThinking(true); 
      });
      while (contSpeedThink > 0){
        synchronizedPause(); // para garantir que sincronize a pausa
        if (flag == -2){ // para garantir o reset quebrando o laco
          break;
        } // fim do if
        sleep(1);
        contSpeedThink -= speedThink;
      } // fim do while
      contSpeedThink = TIME;
      Platform.runLater(() -> {
        showIsThinking(false);
      });
    } catch (InterruptedException e) {} // fim do try-catch
  } // fim do metodo think

  /*********************************************************************
  * Metodo: eat
  * Funcao: representacao do filosfo - Hashira comendo
  muda o estado do Hashira com identificador id para comendo, de forma a 
  chamar metodos p interface que mostram os elementos do ato de comer
  durante um determinando tempo que pode ser modificado atraves do speedEat
  * Parametros: nenhum
  * Retorno: nenhum
  ******************************************************************* */
  private void eat() {
    try {
      Platform.runLater(() -> {
        showIsWaiting(false);
        showIsEating(true);
      });
      while (contSpeedEat > 0){
        synchronizedPause(); // para garantir que sincronize a pausa
        if (flag == -2){ // para garantir o reset quebrando o laco
          break;
        } // fim do if
        sleep(1);
        contSpeedEat -= speedEat;
      } // fim do while
      contSpeedEat = TIME;
      Platform.runLater(() -> {
        showIsEating(false);
      });
    } catch (InterruptedException e) {} // fim do try-catch
  } // fim do metodo eat

  /*********************************************************************
  * Metodo: getForks
  * Funcao: implementacao da solucao de semaforo que representa a tentativa
  do Hashira de pegar os dois garfos, por meio da analise de disponibilidade dos 
  garfos da esquerda e da direita e se nao consegue pegar os dois, fica esperando
  ou bloqueado
  * Parametros: nenhum
  * Retorno: nenhum
  ******************************************************************* */
  public void getForks(){
    try {
      synchronizedPause();
      mutex.acquire(); // entra na RC
      state[id] = STARVING; // hashira com fome
      tryForks(id); // tenta adquirir dois garfos
      mutex.release(); // sai da RC
      semaphores[id].acquire(); // bloqueia se nao pegar os dois
      synchronizedPause();
    } catch (InterruptedException e) {}
  } // fim do metodo getForks

  /*********************************************************************
  * Metodo: leaveForks
  * Funcao: implementacao da solucao de semaforo que representa a liberacao
  dos dois garfos para que o proximo Hashira possa utilizar
  * Parametros: nenhum
  * Retorno: nenhum
  ******************************************************************* */
  public void leaveForks(){
    try {
      synchronizedPause();
      mutex.acquire(); // entra na RC
      state[id] = THINKING; // hashira libera garfos
      tryForks(left); // olha se vizinho ESQUERDA pode comer
      tryForks(right); // olha se vizinho DIREITA pode comer
      mutex.release(); // sai da RC
      synchronizedPause();
    } catch (InterruptedException e) {}
  } // fim do metodo leaveForks

  /*********************************************************************
  * Metodo: tryForks
  * Funcao: implementacao da solucao de semaforo que representa a analise de 
  disponibilidade dos garfos da esquerda e da direita para decidir se o Hashira
  pega ou nao
  * Parametros: nenhum
  * Retorno: nenhum
  ******************************************************************* */
  public void tryForks(int i){
    int left = (i + 5 - 1) % 5; // nao ta funcionando corretamente sem essa verificacao
    int right = (i + 1) % 5;
    if (state[i] == STARVING && state[left] != EATING && state[right] != EATING){
      state[i] = EATING; // hashira pega 2 hashi
      semaphores[i].release();
    } else { // nao pega os 2 hashi
      if (i == id){ // se nao estiver deixando os garfos no leaveForks
        Platform.runLater(() -> { 
          showIsWaiting(true); 
        });
      } // fim do if
    } // fim do if-else
  } // fim do metodo tryForks

  // metodo relacionado com a pausa de thread no controlador, seta a variavel isPaused
  public void pauseThread() {
    isPaused = true;
    Platform.runLater(() -> {
      freezyState(true);
      controller.setLabel(id, "pausado");
    });
  } // fim do pauseThread()

  // metodo para retomar a thread
  public void resumeThread() {
    isPaused = false;
    Platform.runLater(() -> {
      freezyState(false);
    });
    synchronized (controller.lock) {
      controller.lock.notifyAll();
    } // fim do synchronized
  } // fim do resumeThread

  // metodo para resetar a thread
  public void reset() {
    flag = -2; // quebrando o laco
    speedEat = 1; // setando vel. de pensar inicial
    speedThink = 1; // setando vel. de pensar inicial
    Platform.runLater(() -> { // resetando animacoes na tela
      showIsThinking(true);
      showIsWaiting(false);
      showIsEating(false);
    });
    mutex.release(); // liberando semaforo
  }
  
  // chama metodos que controlam os elementos de interface relacionados com o estado de pensar
  public void showIsThinking(boolean value){
    controller.setBallonThinking(id, value);
    controller.setLabel(id, "pensando");
  } // fim do showIsThinking
  
  // chama metodos que controlam os elementos de interface relacionados com o estado de comer
  public void showIsEating(boolean value){
    controller.setHashi(id, value);
    controller.setPlate(id, value);
    controller.setLabel(id, "comendo");
  } // fim do showIsEating
  
  // chama metodos que controlam os elementos de interface relacionados com o estado de espera - bloqueado
  public void showIsWaiting(boolean value){
    controller.setWaiting(id, value);
    controller.setLabel(id, "bloqueado");
  } // fim do showIsWaiting

  // congela ou descongela na interface o estado do filosofo 
  public void freezyState(boolean value){
    if (state[id] == STARVING){
      showIsWaiting(value);
    } // fim do if 

    if (state[id] == THINKING){
      showIsThinking(value);
    } // fim do if

    if (state[id] == EATING){
      showIsEating(value);
    } // fim do if
  } // fim do freezyState

  // metodo para sincronizar a pausa do filosofo ao longo do run()
  public void synchronizedPause(){
    synchronized (controller.lock) {
      while (isPaused) {
        try {
          controller.lock.wait();
          sleep(1);
        } catch (InterruptedException e) {}
      } // fim do while
    } // fim do synchronized
  } // fim do synchronizedPause
  
  //------------------------------------ Getters e Setters --------------------------------------
  public boolean isPaused() {
    return isPaused;
  }

  public void setPaused (boolean isPaused) { 
    this.isPaused = isPaused; 
  } 

  public void setSpeedThink(long speedThink) {
    this.speedThink = speedThink;
  }

  public void setSpeedEat(long speedEat) {
    this.speedEat = speedEat;
  }
  
} // fim da classe Hashira
