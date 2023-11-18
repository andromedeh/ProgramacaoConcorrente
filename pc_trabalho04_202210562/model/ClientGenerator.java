/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 07/11/2023
* Ultima alteracao.: 14/11/2023
* Nome.............: ClientGenerator
* Funcao...........: Gerar aleatoriamente os clientes da barbearia e controlar
a velocidade com que eles sao sorteados e aparecem 
************************************************************************ */
package model;
import control.MainController;
import java.util.Random;

public class ClientGenerator extends Thread {
  private MainController controller = new MainController();
  private Random rand = new Random();
  private int upperbound = 11; // numero de personagens
  private final static int TIME = 4500; // tempo geral de chegada
  private long speedArrive = 1; // velocidade com que os clientes chegam
  private int contSpeedArrive = TIME; // contador usado p/ acelerar a velocidade dos clientes

  public ClientGenerator (MainController controller) {
    this.controller = controller;
    resetCont();
  }

  public void run(){
    while (true){ // sorteia novos personagens para entrar na cena - chegar na barbearia
      try {
        if (Variables.getResetado()){ // verificando flag de reset
          break;
        } // fim do if
        int n = rand.nextInt(upperbound); // sorteia um id de personagem de 0 a 10
        System.out.println("Personagem sorteado: " + controller.getPersonagens()[n].getNome()); 
        if (Variables.personagens[n] != true){ // da start se o personagem nao estiver na cena ainda
          Client c = controller.getPersonagens()[n];
          if (c.isAlive()){ // se a thread do personagem ja estiver viva, permite que o cliente volte a cena
            c.setNewClient(true);
          } else{
            c.start(); // da start
          } // fim do if-else
        } // fim do if 
        while (contSpeedArrive > 0){  // inicio da logica de controle de velocidade
          if (Variables.getResetado()){ // verificando flag de reset
            break;
          } // fim do if
          sleep(1);
          contSpeedArrive-= speedArrive; 
        } // fim do while
        contSpeedArrive = TIME; // fim da logica de controle de velocidade
      } catch (InterruptedException e) {} // fim do try-catch
    } //fim do while  
  } // fim do run

  // seta nova velocidade de surgimento dos personagens
  public void setSpeedArrive(long speedArrive) {
    this.speedArrive = speedArrive;
  }

  //reset contador da logica de velocidade
  public void resetCont(){
    this.contSpeedArrive = TIME;
  }

} // fim da classe ClientGenerator