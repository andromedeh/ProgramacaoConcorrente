/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 07/11/2023
* Ultima alteracao.: 14/11/2023
* Nome.............: Variables
* Funcao...........: Define as variaveis globais do problema do barbeiro 
dorminhoco e os metodos de atualizacao das mesmas
************************************************************************ */
package model;
import java.util.concurrent.Semaphore;

public abstract class Variables {
	public static final int CADEIRAS = 5; // n. de cadeiras na barbearia
  public static int esperando = 0; // numero de clientes que estao esperando
  public static Semaphore mutex = new Semaphore(1); 
  public static Semaphore clientes = new Semaphore(0); 
  public static Semaphore barbeiros  = new Semaphore(0);
  public static volatile boolean personagens[] = {false, false, false, false, false, false, false, false, false, false, false}; // flag p/ evitar repeticao de personagem na cena
  public static volatile boolean cadeirasOcupadas[] = {false, false, false, false, false}; // array flags de cadeiras de espera livres/ocupadas
  public static volatile boolean barbeiroTerminou = false; // 
  public static boolean resetado = false; // flag de reset

  // define personagem em cena ou nao, p/ evitar repeticao
  public static void atualizaPersonagem(int index, boolean estado){
    personagens[index] = estado;
  }
  
  // atualiza se a cadeira esta ocupada ou nao
  public static void atualizaCadeiraDeEspera(int index, boolean estado){
    cadeirasOcupadas[index] = estado;
  } 

  // define a cadeira de espera do personagem de acordo com a primeira que estiver livre
  public static int pegarPrimeiraCadeiraLivre(){
    int cadeira = 0;
    for (int i=0; i< cadeirasOcupadas.length; i++){
      if (cadeirasOcupadas[i] == false){ // procura pela primeira cadeira livre
        cadeirasOcupadas[i] = true; // marca como ocupada
        cadeira = i; 
        break;
      } // fim do if
    } // fim do for
    return cadeira;
  } // fim do primeiraCadeiraLivre
  
  public static void incrementaEsperando(){
    esperando++;
  }

  public static void decrementaEsperando(){
    esperando--;
  }
  
  // reinicia semaforos e define todos personagens como fora de cena e todas cadeiras de espera como livres
  public static void reset(){
    setResetado(true);
    esperando = 0;
    mutex = new Semaphore(1); 
    clientes = new Semaphore(0); 
    barbeiros  = new Semaphore(0);
    for (int i=0; i < personagens.length; i++){
      personagens[i] = false;
    } // fim do for p/ personagens
    for (int i=0; i < cadeirasOcupadas.length; i++){
      cadeirasOcupadas[i] = false;
    } // fim do for das cadeiras
  } // fim do reset

  // --------------------------------------------- GETTERS E SETTERS ------------------------------------------
  public static boolean getResetado() {
    return resetado;
  }

  public static void setResetado(boolean resetou) {
    Variables.resetado = resetou;
  }

  public static void setBarbeiroTerminou(boolean barbeiroTerminou) {
    Variables.barbeiroTerminou = barbeiroTerminou;
  }

  public static boolean getBarbeiroTerminou() {
    return barbeiroTerminou;
  }
} // fim do Client