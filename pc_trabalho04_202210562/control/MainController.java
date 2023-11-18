/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 07/11/2023
* Ultima alteracao.: 14/11/2023
* Nome.............: MainController
* Funcao...........: Controlar os elementos da interface e relaciona com 
metodos das threads 
************************************************************************ */
package control;
import model.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MainController implements Initializable{
  // atributos fxml
  @FXML
  private AnchorPane anchorPanePrincipal, anchorPaneInicial;
  @FXML
  private Button btnStart, btnMusic, btnOpen;
  @FXML
  private Button btnReset, btnPauseBarber, btnPauseClients;
  @FXML
  private ImageView iconMusic, iconSliders, playPauseB, playPauseC;
  @FXML 
  private ImageView pernalonga, pernalongaSleep, frajola1, frajola2, coiote1, coiote2, eufrazino1, eufrazino2, hector1, hector2, ligeirinho1, ligeirinho2;
  @FXML
  private ImageView marciano1, marciano2, papalegua1, papalegua2, patolino1, patolino2, pepe1, pepe2, piupiu1, piupiu2, taz1, taz2;
  @FXML
  private Slider speedBarber, speedClients;
  @FXML
  private ProgressBar progressBar;
  //atributos da classe
  private static final Client[] personagens = new Client[11]; // 9 personagens possiveis para clientes
  private static Barber pernalongaBarbeiro;
  private static ClientGenerator clientGenerator;
  // atributos relacionados a trilha sonora
  private String trilha = "./resources/opening.wav";
  private boolean isPlaying = false, isPausedB = false, isPausedC = false;

  /*********************************************************************
  * Metodo: initialize
  * Funcao: mostrar a anchorPane inicial e da play na musica tematica
  * Parametros: location e resources sao parametros predefinidos
  * Retorno: nenhum
  ******************************************************************* */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    anchorPaneInicial.setVisible(true);
    Music.play(trilha);
    isPlaying = true;
  } // fim do método initialize

  /*********************************************************************
  * Metodo: start
  * Funcao: mostrar a tela principal da barbearia; cria as instancias
  dos personagens clientes e do barbeiro; adiciona os personagens no vetor
  de clientes; omite ou mostra alguns elementos da interface
  * Parametros: e eh um evento sobre o btnStart
  * Retorno: nenhum
  ******************************************************************* */
  @FXML
  public void start(ActionEvent event){
    anchorPaneInicial.setVisible(false);
    anchorPanePrincipal.setVisible(true);
    btnOpen.setVisible(true);
    btnReset.setVisible(false);
    progressBar.setVisible(false);
    pernalongaBarbeiro = new Barber(this, pernalonga);
    clientGenerator = new ClientGenerator(this);
    showBarberSleeping(true);
  
    Client frajola = new Client(this, 0, frajola1, frajola2);
    Client coiote = new Client(this, 1, coiote1, coiote2);
    Client eufrazino = new Client(this, 2, eufrazino1, eufrazino2);
    Client ligeirinho = new Client (this, 3, ligeirinho1, ligeirinho2);
    Client papalegua = new Client(this, 4, papalegua1, papalegua2);
    Client patolino = new Client(this, 5, patolino1, patolino2);
    Client pepe = new Client (this, 6, pepe1, pepe2);
    Client piupiu = new Client(this, 7, piupiu1, piupiu2);
    Client taz = new Client(this, 8, taz1, taz2);
    Client marciano = new Client(this, 9, marciano1, marciano2);
    Client hector = new Client(this, 10, hector1, hector2);

    personagens[0] = frajola;
    personagens[1] = coiote;
    personagens[2] = eufrazino;
    personagens[3] = ligeirinho;
    personagens[4] = papalegua;
    personagens[5] = patolino;
    personagens[6] = pepe;
    personagens[7] = piupiu;
    personagens[8] = taz;
    personagens[9] = marciano;
    personagens[10] = hector;

    Variables.setResetado(false); // varivel de controle p/ reset
  } // fim do start

  /*********************************************************************
  * Metodo: openBarberShop
  * Funcao: da start nas threads do barbeiro e do gerador de personagem,
  omite ou mostra alguns elementos da interface
  * Parametros: e eh um evento sobre o btnOpen
  * Retorno: nenhum
  ******************************************************************* */
  @FXML
  void openBarberShop(ActionEvent event) {
    btnOpen.setVisible(false);
    btnReset.setVisible(true);
    btnPauseBarber.setVisible(true);
    btnPauseClients.setVisible(true);
    iconSliders.setVisible(true);
    speedBarber.setVisible(true);
    progressBar.setVisible(true);
    speedClients.setVisible(true);
    showBarberSleeping(false);
    pernalongaBarbeiro.start();
    clientGenerator.start();
  } // fim do openBarberShop

  /*********************************************************************
  * Metodo: music
  * Funcao: troca o icon do btnMusic e da pause ou play na musica
  * Parametros: e eh um evento sobre o btnMusic
  * Retorno: nenhum
  ******************************************************************* */
  @FXML
  void music(ActionEvent event) {
    Image icon;
    if (isPlaying){ // se estiver tocando e quiser pausar
      Music.pause();
      icon = new Image("resources/img/musicOff.png");
      isPlaying = false; // pausado
    } else{ // nao esta tocando
      Music.pause();
      icon = new Image("resources/img/musicOn.png");
      isPlaying = true; // som normal
    } // fim do if-else
    iconMusic.setImage(icon); // troca icon do botao
  } // fim do music

  /*********************************************************************
  * Metodo: changeSpeedBarber
  * Funcao: chama a funcao que varia a velocidade de trabalho do barbeiro
  com novo valor
  * Parametros: e eh um evento sobre o slider speedBarber
  * Retorno: nenhum
  ******************************************************************* */
  @FXML
  void changeSpeedBarber(MouseEvent event) {
    long newSpeed = (long) speedBarber.getValue(); // pega valor do slider
    pernalongaBarbeiro.setSpeedJob(newSpeed); // seta nova velocidade
  } // fim do changeSpeedBarber

  /*********************************************************************
  * Metodo: changeSpeedClients
  * Funcao: chama a funcao que varia a velocidade de chegada dos clientes
  com novo valor 
  * Parametros: e eh um evento sobre o slider speedClients
  * Retorno: nenhum
  ******************************************************************* */
  @FXML
  void changeSpeedClients(MouseEvent event) {
    long newSpeed = (long) speedClients.getValue(); // pega valor do slider
    clientGenerator.setSpeedArrive(newSpeed); // seta nova velocidade
  } // fim do changeSpeedClients

  /*********************************************************************
  * Metodo: changeProgressBar
  * Funcao: modificar a barra de progresso de acordo o avança do corte do barbeiro
  * Parametros: e eh um evento sobre a progressBar
  * Retorno: nenhum
  ******************************************************************* */
  public void changeProgressBar(double value){
    progressBar.setProgress(value);
  } // fim do changeProgressBar

  /*********************************************************************
  * Metodo: pauseBarber
  * Funcao: pausa barbeiro e alterna o icon do botao de pausa
  * Parametros: e eh um evento sobre o btnPauseBarber
  * Retorno: nenhum
  ******************************************************************* */
  @SuppressWarnings("deprecation")
  @FXML
  void pauseBarber(ActionEvent event) {
    Image icon;
    if (!isPausedB){ // se quer pausar
      icon = new Image("resources/img/iconPlay.png");
      isPausedB = true; // pausado
      try {
        pernalongaBarbeiro.suspend();
      } catch(SecurityException e) {}
    } else{ // dar play
      icon = new Image("resources/img/iconPause.png");
      isPausedB = false; // som normal
      try {
        pernalongaBarbeiro.resume();
      } catch(SecurityException e) {}
    } // fim do if-else
    playPauseB.setImage(icon); // troca icon do botao
  } // fim do pauseBarber

  /*********************************************************************
  * Metodo: pauseClientes
  * Funcao: pausa chegada de clientes e alterna o icon do botao de pausa
  * Parametros: event eh um evento sobre o btnPauseClients
  * Retorno: nenhum
  ******************************************************************* */
  @SuppressWarnings("deprecation")
  @FXML
  void pauseClients(ActionEvent event) {
    Image icon;
    if (!isPausedC){ // se quer pausar
      icon = new Image("resources/img/iconPlay.png");
      isPausedC = true; // pausado
      try {
        clientGenerator.suspend(); 
      } catch(SecurityException e) {}
    } else{ // se quer dar play
      icon = new Image("resources/img/iconPause.png");
      isPausedC = false; // som normal
      try {
        clientGenerator.resume();
      } catch(SecurityException e) {}
    } // fim do if-else
    playPauseC.setImage(icon); // troca icon do botao
  } // fim do pauseClients

  /*********************************************************************
  * Metodo: reset
  * Funcao: interrompe e reinicia as threads, reinicia a simulacao
  * Parametros: event eh um evento sobre o btnReset
  * Retorno: nenhum
  ******************************************************************* */
  @FXML
  void reset(ActionEvent event) {
    hideAll(); // esconde elementos
    Variables.reset(); // reseta semaforos
    // interrompe as threads
    pernalongaBarbeiro.interrupt();
    clientGenerator.interrupt();
    for (Client c: personagens){ // laco por todos personagens
      if (c.isAlive()) { // verifica se ja foi iniciada
        c.interrupt(); // interrompe se sim
      } // fim do if
    } //fim do for  
    start(event); // instancia threads de novo
  } // fim do reset

  /*********************************************************************
  * Metodo: hideAll
  * Funcao: esconde alguns elementos da tela e coloca nos padrões iniciais
  * Parametros: nenhum
  * Retorno: nenhum
  ******************************************************************* */
  public void hideAll(){
    pernalonga.setVisible(false);
     for (Client c: personagens){ // laco por todos personagens
      c.getStanding().setVisible(false);
      c.getSitting().setVisible(false);
    } //fim do for
    btnPauseBarber.setVisible(false);
    btnPauseClients.setVisible(false);
    btnReset.setVisible(false);
    iconSliders.setVisible(false);
    speedBarber.setVisible(false);
    speedClients.setVisible(false);
    speedBarber.setValue(1);
    speedClients.setValue(1);
    progressBar.setProgress(0);
  }

  /*********************************************************************
  * Metodo: arrive
  * Funcao: configura a interface para mostrar o personagem chegar
  * Parametros: index eh o id do personagem, x e y sao as coordenadas que
  eles devem aparecer na tela
  * Retorno: nenhum
  ******************************************************************* */
  public void arrive(int index, int x, int y){
    Platform.runLater(() -> {  
      personagens[index].getStanding().setLayoutX(x);
      personagens[index].getStanding().setLayoutY(y);
      personagens[index].getStanding().setVisible(true);
    });
  } // fim do arrive

  /*********************************************************************
  * Metodo: wait
  * Funcao: configura a interface para mostrar o personagem na cadeira de espera
  * Parametros: index eh o id do personagem, x e y sao as coordenadas que
  eles devem aparecer na tela
  * Retorno: nenhum
  ******************************************************************* */
  public void wait(int index, int x, int y){
    Platform.runLater(() -> {  
      personagens[index].getStanding().setVisible(false);
      personagens[index].getSitting().setLayoutX(x);
      personagens[index].getSitting().setLayoutY(y);
      personagens[index].getSitting().setVisible(true);
    });
  } // fim do wait

  /*********************************************************************
  * Metodo: sit
  * Funcao: configura a interface para mostrar o personagem na cadeira do barbeiro
  * Parametros: index eh o id do personagem, x e y sao as coordenadas que
  eles devem aparecer na tela
  * Retorno: nenhum
  ******************************************************************* */
  public void sit(int index, int x, int y){
    Platform.runLater(() -> {  
      personagens[index].getSitting().setLayoutX(x);
      personagens[index].getSitting().setLayoutY(y);
      personagens[index].getSitting().setVisible(true);
    });
  } // fim do sit

  /*********************************************************************
  * Metodo: leave
  * Funcao: omite o personagem da cena
  * Parametros: index eh o id do personagem
  * Retorno: nenhum
  ******************************************************************* */
  public void leave(int index){
    Platform.runLater(() -> {  
      personagens[index].getSitting().setVisible(false);
      personagens[index].getStanding().setVisible(false);
    });
  } // fim do sit
  
  // mostra barbeiro dormindo
  public void showBarberSleeping(boolean value){
    Platform.runLater(() -> {  
      pernalonga.setVisible(!value);
      pernalongaSleep.setVisible(value);
    });
  } // fim do showBarberSleeping
  
  // retorna array com todos os personagens
  public static Client[] getPersonagens() {
    return personagens;
  } // fim do getPersonagens

} // fim do MainController

