/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 13/10/2023
* Ultima alteracao.: 27/10/2023
* Nome.............: MainController
* Funcao...........: Controlar os elementos da interface e relacionar com 
metodos das threads
************************************************************************ */
package control;
import model.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MainController implements Initializable{
  // atributos FXML
  @FXML
  private AnchorPane mainAnchorPane;
  @FXML
  private AnchorPane inicialAnchorPane;
  @FXML
  private Button btnPlay;
  @FXML
  private Button btnResetar;
  @FXML
  private Button btnPause0, btnPause1, btnPause2, btnPause3, btnPause4;
  @FXML
  private ImageView hashi0, hashi1, hashi2, hashi3, hashi4;
  @FXML
  private ImageView ballon0, ballon1, ballon2, ballon3, ballon4;
  @FXML
  private ImageView plate0, plate1, plate2, plate3, plate4;
  @FXML
  private ImageView speech0, speech1, speech2, speech3, speech4;
  @FXML
  private ImageView waiting0, waiting1, waiting2, waiting3, waiting4;
  @FXML
  private ImageView imgPlayPause0, imgPlayPause1, imgPlayPause2, imgPlayPause3, imgPlayPause4;
  @FXML
  private Slider velComer0, velComer1, velComer2, velComer3, velComer4;
  @FXML
  private Slider velPensar0, velPensar1, velPensar2, velPensar3, velPensar4;
  @FXML
  private Label labelRengoku, labelMitsuri, labelShinobu, labelTokito, labelIguro;
  // atributos da classe
  Image playImage = new Image("img/iconPlay.png");
  Image pauseImage = new Image("img/iconPause.png");
  private static final int NUM_HASHIRAS = 5;
  private static final Hashira[] hashiras = new Hashira[NUM_HASHIRAS];
  public volatile Object lock = new Object();

  /*********************************************************************
  * Metodo: initialize
  * Funcao: 
  * Parametros: location e resources sao parametros predefinidos
  * Retorno: nenhum
  ******************************************************************* */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    inicialAnchorPane.setVisible(true);
    for (int i = 0; i < NUM_HASHIRAS; i++) {
      hashiras[i] = new Hashira(i, this);
      hashiras[i].semaphores[i] = new Semaphore(0);
    } // fim do for
  } // fim do método initialize

  /*********************************************************************
  * Metodo: showMainScreen
  * Funcao: omite a tela inicial e mostra a principal
  * Parametros: event eh um evento sobre o btnPlay
  * Retorno: nenhum
  ******************************************************************* */
  @FXML
  void showMainScreen(ActionEvent event) {
    inicialAnchorPane.setVisible(false);
    mainAnchorPane.setVisible(true);
    for (Hashira h : hashiras) {
      h.start();
    } // fim do for
  } // fim do metodo showMainScreen

  /*********************************************************************
  * Metodo: changeEatSpeed
  * Funcao: identifica qual slider de velocidade de comer foi modificado e 
  muda a velocidade em que o respectivo hashira come
  * Parametros: event eh um evento sobre os sliders de velComer 
  * Retorno: nenhum
  ******************************************************************* */
  @FXML
  void changeSpeedEat(MouseEvent event) {
    long newSpeed;
    if (event.getSource().equals(velComer0)){ // slider comer do hashira 0
      newSpeed = (long) velComer0.getValue(); // pegando valor do slider
      hashiras[0].setSpeedEat(newSpeed); // setando a nova velocidade
    } // fim do if
    if (event.getSource().equals(velComer1)){ // slider comer do hashira 1
      newSpeed = (long) velComer1.getValue();
      hashiras[1].setSpeedEat(newSpeed);
    } // fim do if
    if (event.getSource().equals(velComer2)){ // slider comer do hashira 2
      newSpeed = (long) velComer2.getValue();
      hashiras[2].setSpeedEat(newSpeed);
    } // fim do if
    if (event.getSource().equals(velComer3)){ // slider comer do hashira 3
      newSpeed = (long) velComer3.getValue(); 
      hashiras[3].setSpeedEat(newSpeed);
    } // fim do if
    if (event.getSource().equals(velComer3)){ // slider comer do hashira 4
      newSpeed = (long) velComer4.getValue();
      hashiras[4].setSpeedEat(newSpeed);
    } // fim do if
  } // fim do metodo changeEatSpeed

  /*********************************************************************
  * Metodo: changeThinkSpeed
  * Funcao: identifica qual slider de velocidade de pensar foi modificado 
  e muda a velocidade em que o respectivo hashira pensa
  * Parametros: event eh um evento sobre os sliders de velPensar
  * Retorno: nenhum
  ******************************************************************* */
  @FXML
  void changeSpeedThink(MouseEvent event) {
    long newSpeed;
    if (event.getSource().equals(velPensar0)){ // slider pensar no hashira 0
      newSpeed = (long) velPensar0.getValue(); // pegando valor do slider
      hashiras[0].setSpeedThink(newSpeed); // setando nova velocidade
    } // fim do if
    if (event.getSource().equals(velPensar1)){ // slider pensar no hashira 1
      newSpeed = (long) velPensar1.getValue();
      hashiras[1].setSpeedThink(newSpeed);
    } // fim do if
    if (event.getSource().equals(velPensar2)){ // slider pensar no hashira 2
      newSpeed = (long) velPensar2.getValue();
      hashiras[2].setSpeedThink(newSpeed);
    } // fim do if
    if (event.getSource().equals(velPensar3)){ // slider pensar no hashira 3
      newSpeed = (long) velPensar3.getValue();
      hashiras[3].setSpeedThink(newSpeed);
    } // fim do if
    if (event.getSource().equals(velPensar4)){ // slider pensar no hashira 4
      newSpeed = (long) velPensar4.getValue();
      hashiras[4].setSpeedThink(newSpeed);
    } // fim do if
  } // fim do metodo changeThinkSpeed

  /*********************************************************************
  * Metodo: reset
  * Funcao: chamar metodo para interromper todas threads e chamar metodos
  para dar restart nas threads e nos sliders de velocidade
  * Parametros: event eh um evento sobre o btnResetar
  * Retorno: nenhum
  ******************************************************************* */
  @FXML
  void reset(ActionEvent event) {
    for (Hashira h : hashiras) {
      h.reset();
    } // fim do for
    restartThreads();
    restartSliders();
  } // fim do metodo reset
 
  /*********************************************************************
  * Metodo: restartThreads
  * Funcao: instancia novamente os semaforos e as threads e da start nas threads
  * Parametros: nenhum
  * Retorno: nenhum
  ******************************************************************* */
  public void restartThreads(){
    // instanciando as threads e os semaforos
    for (int i = 0; i < NUM_HASHIRAS; i++) {
      hashiras[i] = new Hashira(i, this);
      hashiras[i].semaphores[i] = new Semaphore(0);
    } // fim do for
   // reiniciando as threads
    for (Hashira h : hashiras) {
      h.start();
    } // fim do for 
  }// fim do restartThreads

  /*********************************************************************
  * Metodo: restartSliders
  * Funcao: seta todos os sliders para o valor inicial 
  * Parametros: nenhum
  * Retorno: nenhum
  ******************************************************************* */
  public void restartSliders(){ // seta valor dos sliders para 1
    // sliders da velocidade de comer
    velComer0.setValue(1);
    velComer1.setValue(1);
    velComer2.setValue(1);
    velComer3.setValue(1);
    velComer4.setValue(1);
    // sliders da velocidade de pensar
    velPensar0.setValue(1);
    velPensar1.setValue(1);
    velPensar2.setValue(1);
    velPensar3.setValue(1);
    velPensar4.setValue(1);
  } // fim do restartSliders

  /*********************************************************************
  * Metodo: pause
  * Funcao: pausa ou continua os hashiras dependendo de qual botao de
  pausa foi pressionado e alterna a imagem dos botoes play e pause
  * Parametros: event eh um evento sobre os botoes individuais de pausa
  * Retorno: nenhum
  ******************************************************************* */
  @FXML
  void pause(ActionEvent event) {
    // definir de qual hashira foi o botao play-pause apertado
    if (event.getSource().equals(btnPause0)){ // se foi o hashira 0
      if (!hashiras[0].isPaused()){  // se vai pausar
        imgPlayPause0.setImage(playImage); // troca imagem do botao de pausa 0 
        hashiras[0].pauseThread(); // chama metodo de pausa
      } else{ // se vai dar play
        imgPlayPause0.setImage(pauseImage); // trocando imagem do botao de pausa 0
        hashiras[0].resumeThread(); // chama metodo de retorno 
      } // fim do if else
    } // fim do if p/ hashira 0
    if (event.getSource().equals(btnPause1)){ // se foi o hashira 1
      if (!hashiras[1].isPaused()){ 
        imgPlayPause1.setImage(playImage);
        hashiras[1].pauseThread();
      } else{
        imgPlayPause1.setImage(pauseImage);
        hashiras[1].resumeThread();
      } // fim do if else
    } // fim do if p/ hashira 1
    if (event.getSource().equals(btnPause2)){ // se foi o hashira 2
      if (!hashiras[2].isPaused()){
        imgPlayPause2.setImage(playImage);
        hashiras[2].pauseThread();
      } else{
        imgPlayPause2.setImage(pauseImage);
        hashiras[2].resumeThread();
      } // fim do if else
    } // fim do if p/ hashira 2
    if (event.getSource().equals(btnPause3)){ // se foi o hashira 3
      if (!hashiras[3].isPaused()){
        imgPlayPause3.setImage(playImage);
        hashiras[3].pauseThread();
      } else{
        imgPlayPause3.setImage(pauseImage);
        hashiras[3].resumeThread();
      } // fim do if else
    } // fim do if p/ hashira 3
    if (event.getSource().equals(btnPause4)){ // se foi o hashira 4
      if (!hashiras[4].isPaused()){
        imgPlayPause4.setImage(playImage);
        hashiras[4].pauseThread();
      } else{
        imgPlayPause4.setImage(pauseImage);
        hashiras[4].resumeThread();
      } // fim do if else
    } // fim do if p/ hashira 4
  } // fim do metodo pause

  /*********************************************************************
  * Metodo: setBallonThinking
  * Funcao: mostrar ou omitir balao de pensamento de casa Hashira
  * Parametros: i eh o numero do hashira e value eh um boleano de estado de 
  visibilidade do balao de pensamento
  * Retorno: void
  ******************************************************************* */
  public void setBallonThinking(int i, boolean value){
    switch(i){
      case 0:{
        ballon0.setVisible(value);
        break;
      } // fim do case 0
      case 1:{
        ballon1.setVisible(value);
        break;
      } // fim do case 1
      case 2:{
        ballon2.setVisible(value);
        break;
      } // fim do case 2
      case 3:{
        ballon3.setVisible(value);
        break;
      } // fim do case 3
      case 4:{
        ballon4.setVisible(value);
        break;
      } // fim do case 4
    } // fim do switch
  } // fim do setBallonThinking

  /*********************************************************************
  * Metodo: setPlate
  * Funcao: mostrar ou omitir o prato com comida do Hashira
  * Parametros: i eh o numero do hashira e value eh um boleano de estado de 
  visibilidade do prato
  * Retorno: void
  ******************************************************************* */
  public void setPlate(int i, boolean value){
    switch(i){
      case 0:{
        plate0.setVisible(value);
        speech0.setVisible(value);
        break;
      } // fim do case 0
      case 1:{
        plate1.setVisible(value);
        speech1.setVisible(value);
        break;
      } // fim do case 1
      case 2:{
        plate2.setVisible(value);
        speech2.setVisible(value);
        break;
      } // fim do case 2
      case 3:{
        plate3.setVisible(value);
        speech3.setVisible(value);
        break;
      } // fim do case 3
      case 4:{
        plate4.setVisible(value);
        speech4.setVisible(value);
        break;
      } // fim do case 4
    } // fim de switch
  } // fim do setPlate

  /*********************************************************************
  * Metodo: setWaiting
  * Funcao: mostrar ou omitir o desenho da flag de bloqueado quando o hashira 
  estiver esperando para poder comer
  * Parametros: i eh o numero do hashira e value eh um boleano de estado de 
  visibilidade da flag
  * Retorno: void
  ******************************************************************* */
  public void setWaiting(int i, boolean value){
    switch(i){
      case 0:{
        waiting0.setVisible(value);
        break;
      } // fim do case o
      case 1:{
        waiting1.setVisible(value);
        break;
      } // fim do case 1
      case 2:{
        waiting2.setVisible(value);
        break;
      } // fim do case 2
      case 3:{
        waiting3.setVisible(value);
        break;
      } // fim do case 3
      case 4:{
        waiting4.setVisible(value);
        break;
      } // fim do case 4
    } // fim do switch
  } // fim do setWaiting

  /*********************************************************************
  * Metodo: setHashi
  * Funcao: destaca os hashi de determinado Hashira, quando este ja esta
  podendo comer, ou seja, destaca os dois hashi ja em posse do Hashira
  * Parametros: i eh o numero do hashira e value eh um boleano de estado de 
  destaque dos hashi
  * Retorno: void
  ******************************************************************* */
  public void setHashi(int i, boolean value){
    switch(i){
      case 0:{
        hashi0.setVisible(value);
        hashi1.setVisible(value);
        break;
      } // fim do case 0
      case 1:{
        hashi1.setVisible(value);
        hashi2.setVisible(value);
        break;
      } // fim do case 1
      case 2:{
        hashi2.setVisible(value);
        hashi3.setVisible(value);
        break;
      } // fim do case 2
      case 3:{
        hashi3.setVisible(value);
        hashi4.setVisible(value);
        break;
      } // fim do case 3
      case 4:{
        hashi4.setVisible(value);
        hashi0.setVisible(value);
        break;
      } // fim do case 4
    } // fim do switch
  } // fim do setHashi
  
  /*********************************************************************
  * Metodo: setLabel
  * Funcao: atualiza labels de estado de cada hashira no pergaminho da interface
  * Parametros: i eh o numero do hashira e estado eh uma string com o estado
  atual do hashira
  * Retorno: void
  ******************************************************************* */
  public void setLabel(int i, String estado){
    switch(i){
      case 0:{
        labelRengoku.setText("Rengoku: " + estado);
        break;
      } // fim do case 0
      case 1:{
        labelMitsuri.setText("Mitsuri: " + estado);
        break;
      } // fim do case 1
      case 2:{
        labelShinobu.setText("Shinobu: " + estado);
        break;
      } // fim do case 2
      case 3:{
        labelTokito.setText("Tokito: " + estado);
        break;
      } // fim do case 3
      case 4:{
        labelIguro.setText("Iguro: " + estado);
        break;
      } // fim do case 4
    } // fim do switch
  } // fim do setBallonThinking
  
  public static int getNumHashiras() {
    return NUM_HASHIRAS;
  } // fim do getNumHashiras

} // fim da class MainController