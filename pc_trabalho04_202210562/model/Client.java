/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 07/11/2023
* Ultima alteracao.: 14/11/2023
* Nome.............: Client
* Funcao...........: Define os metodos e atributos dos clientes do problema do 
barbeiro dorminhoco de forma interconectada com a classe controladora de tela.
************************************************************************ */
package model;
import control.MainController;
import javafx.scene.image.ImageView;

public class Client extends Thread {
  private MainController controller = new MainController();
  private ImageView standing, sitting;
  private int index; // numero que identifica o personagem
  private String nome; // nome do personagem
  private int cadeiraDeEspera; // qual das 5 cadeiras que o personagem esta esperando
  private int positions[]; // vetor com os pares ordenados (x,y) p/ cada posicao no layout da cena
  private boolean newClient = false; // para o mesmo personagem retornar

  public Client (MainController controller, int index, ImageView standing, ImageView sitting){
    this.controller = controller;
    this.index = index; // identificador
    this.standing = standing; // icon do personagem em pe
    this.sitting = sitting; // icon do personagem sentado
    verifyCharacter(); // determinar o vetor de coordenadas a partir do personagem
  }

  // logica do cliente para o barbeiro dorminhoco atrelado a interface grafica
  public void run(){
    while (true){
      try {
        if (Variables.getResetado()){ // verifica flag de reset
          break;
        } // fim do if
        Variables.atualizaPersonagem(index, true); // flag p/ personagem ja ativo nao aparecer repetido
        controller.arrive(index, positions[0], positions[1]); // cliente chega na barbearia
        sleep(500);
        Variables.mutex.acquire(); // entra na RC
        if (Variables.esperando < Variables.CADEIRAS){ // verifica se pode esperar
          sitAndWait(); // escolhe a primeira cadeira vazia e senta
          Variables.incrementaEsperando(); // incrementa n. de clientes esperando
          Variables.clientes.release(); // acorda barbeiro se necessario
          Variables.mutex.release(); // libera acesso a variavel esperando
          if (Variables.getResetado()){ // verificando flag de reset
            break;
          } // fim do if reset
          Variables.barbeiros.acquire(); // dorme se nao tem barbeiros livres
          Variables.atualizaCadeiraDeEspera(cadeiraDeEspera, false); // flag p/ cadeira de espera
          sitOnTheChair(); // secao critica
          sleep(500);
          while(Variables.getBarbeiroTerminou() == false){ // fica sentado na cadeira enquanto barbeiro nao termina
            if (Variables.getResetado()){ // verifica flag de reset
              break;
            } // fim do if
            sleep(1);
          } // fim do while
        } // fim do if esperando
        else{ // vai embora se todas as cadeiras estao ocupadas
          Variables.mutex.release(); // libera semaforo
          System.out.print("Nao ha vaga! ");
        } // fim do if-else
        leaveBarberShop(); // cliente vai embora
        Variables.atualizaPersonagem(index, false); // personagem fora de cena
        while(true){ // espera enquanto o personagem nao for sorteado novamente
          if (Variables.getResetado()){ // verifica flag de reset
            break; 
          } // fim do if 
          if(newClient){ // verifica se foi sorteado dnv
            newClient = false;
            break;
          } // fim do if 
          sleep(1); // espera ser sorteado
        } // fim do while, comeca a executar novamente
      } catch (InterruptedException e) {} // fim do try-catch
    } // fim do while
  } // fim do run

  // chama metodo da controladora que representa o cliente na cadeira do barbeiro
  public void sitOnTheChair(){
    controller.sit(index, positions[12], positions[13]); // indice e coordenadas na cadeira do barbeiro
  } // fim do sitOnTheChair

  // chama metodo da controladora que representa o cliente indo embor
  public void leaveBarberShop(){
    controller.leave(index); 
    System.out.println(nome + " foi embora!");
  } // fim do leaveBarberShop

  // chama metodo de escolha de uma cadeira de espera, e dependendo de qual for,
  // chama metodo da controladora que representa o cliente nessa cadeira de espera
  public void sitAndWait(){
    cadeiraDeEspera = Variables.pegarPrimeiraCadeiraLivre();
    switch(cadeiraDeEspera){ 
      case 0:{ // cadeira 0
        controller.wait(index, positions[2], positions[3]); // indice e coordenadas nessa cadeira de espera
        break;
      } // fim do case cadeira 0
      case 1:{ // cadeira 1
        controller.wait(index, positions[4], positions[5]);
        break;
      } // fim do case cadeira 1
      case 2:{ // cadeira 2
        controller.wait(index, positions[6], positions[7]);
        break;
      } // fim do case cadeira 2
      case 3:{ // cadeira 3
        controller.wait(index, positions[8], positions[9]);
        break;
      } // fim do case cadeira 3
      case 4:{ // cadeira 4
        controller.wait(index, positions[10], positions[11]);
        break;
      } // fim do case cadeira 4
    } // fim do switch
  } // fim do sit and wait

  // a depender do indice, define nome e coordenadas (x, y) do personagem na interface grafica
  // sao 7 pares ordenados: 1 p/ porta, 5 p/ cadeiras de espera e 1 p/ cadeira do barbeiro
  public void verifyCharacter(){
    switch (index){
      case 0: { // frajola
        int vetor[] = {300, 95, 429, 176, 500, 176, 590, 176, 485, 308, 590, 308, 90, 195};
        positions = vetor;
        nome = "Frajola";
        break;
      } // fim do case frajola

      case 1: { // coiote
        int vetor[] = {323, 97, 445, 197, 520, 197, 610, 197, 520, 330, 610, 330, 125, 211};
        positions = vetor;
        nome = "Coiote";
        break;
      } // fim do case coiote

      case 2: { // eufrazino
        int vetor[] = {321, 132, 427, 163, 510, 163, 600, 163, 498, 290, 600, 290, 106, 176};
        positions = vetor;
        nome = "Eufrazino";
        break;
      } // fim do case eufrazino

      case 3: { // ligeirinho
        int vetor[] = {321, 185, 456, 203, 540, 203, 620, 203, 525, 337, 610, 337, 135, 217};
        positions = vetor;
        nome = "Ligeirinho";
        break;
      } // fim do case ligeirinho

      case 4: { // papalegua
        int vetor[] = {305, 116, 419, 194, 505, 194, 600, 194, 492, 322, 600, 322, 102, 203};
        positions = vetor;
        nome = "Papalegua";
        break;
      } // fim do case papalegua

      case 5: { // patolino
        int vetor[] = {305, 97, 411, 175, 500, 175, 600, 175, 488, 303, 590, 303, 94, 194};
        positions = vetor;
        nome = "Patolino";
        break;
      } // fim do case patolino

      case 6: { // pepe
        int vetor[] = {297, 159, 432, 191, 510, 191, 600, 191, 505, 322, 600, 322, 112, 205};
        positions = vetor;
        nome = "Pepe";
        break;
      } // fim do case pepe

      case 7: { // piupiu
        int vetor[] = {327, 165, 449, 200, 530, 200, 620, 200, 521, 331, 610, 331, 125, 203};
        positions = vetor;
        nome = "Piupiu";
        break;
      } // fim do case piupiu

      case 8: { // taz
        int vetor[] = {295, 135, 430, 195, 510, 195, 600, 195, 500, 322, 600, 322, 112, 211};
        positions = vetor;
        nome = "Taz";
        break;
      } // fim do case taz

      case 9:{ // marciano
        int vetor[] = {318, 118, 448, 185, 525, 185, 615, 185, 515, 311, 610, 311, 120, 193};
         positions = vetor;
         nome = "Marciano";
         break;
      } // fim do case marciano

      case 10:{ // hector
        int vetor[] = {297, 102, 432, 117, 520, 117, 605, 117, 508, 117, 600, 250, 110, 122};
         positions = vetor;
         nome = "Hector";
         break;
      } // fim do case hector

    } // fim do switch
  } // fim do verificaPersonagem

  // --------------------------------------------- GETTERS E SETTERS ------------------------------------------
  public String getNome(){
    return this.nome;
  }

  public ImageView getStanding() {
    return standing;
  }

  public ImageView getSitting() {
    return sitting;
  }

  public boolean isNewClient() {
    return newClient;
  }

  public void setNewClient(boolean newClient) {
    this.newClient = newClient;
  }

} // fim da classe Client