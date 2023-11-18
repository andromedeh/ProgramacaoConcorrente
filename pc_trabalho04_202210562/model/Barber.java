/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 07/11/2023
* Ultima alteracao.: 14/11/2023
* Nome.............: Barber
* Funcao...........: Define os metodos e atributos do barbeiro dorminhoco 
de forma interconectada com a classe controladora de tela.
************************************************************************ */
package model;
import control.MainController;
import javafx.scene.image.ImageView;

public class Barber extends Thread {
  private MainController controller = new MainController();
  private final static int TIME = 6000; // tempo geral de cortar o cabelo
  private long speedJob = 1; // velocidade trabalhar inicial
  private int contSpeedJob = TIME; // contador usado p/ acelerar a velocidade de trabalhar

  public Barber (MainController controller, ImageView icon){
    this.controller = controller;
    resetCont();
  }
  // codigo do barbeiro dorminhoco atrelado a interface grafica
  public void run(){
    while(true){
      try {
        if (Variables.getResetado()){
          break;
        }
        sleep(500);
        controller.showBarberSleeping(true);
        Variables.clientes.acquire(); // dorme se nao tem clientes 
        Variables.mutex.acquire(); // adquire acesso a variavel esperando
        Variables.decrementaEsperando(); // decrementa n. de clientes esperando
        controller.showBarberSleeping(false);
        Variables.barbeiros.release(); // barbeiro pronto pra cortar cabelo
        Variables.mutex.release(); // libera acesso a variavel esperando
        cutHair(); // secao nao critica
        sleep(500);
      } catch (InterruptedException e) {} // fim do try-catch
    } // fim do while
  } // fim do run

  // representacao do barbeiro cortando o cabelo do cliente
  public void cutHair(){ 
    try { 
      Variables.setBarbeiroTerminou(false);
      while (contSpeedJob > 0){ // controle da velocidade de trabalho do barbeiro
        if (Variables.getResetado()){ // quebrar laco se for resetado
          break;
        } // fim do if
        sleep(1);
        contSpeedJob-= speedJob;
        controller.changeProgressBar((double) (TIME - contSpeedJob)/TIME); // aumentando o progresso
      } // fim do while
      contSpeedJob = TIME;
      Variables.setBarbeiroTerminou(true); 
      controller.changeProgressBar(0);
    } catch (InterruptedException e) {}
  } // fim do cutHair

  // troca valor da velocidade do barbeiro
  public void setSpeedJob(long speedJob) {
    this.speedJob = speedJob;
  } // fim do setSpeedJob

  //reset contador da logica de velocidade
  public void resetCont(){
    this.contSpeedJob = TIME;
  }

} // fim da classe Barber