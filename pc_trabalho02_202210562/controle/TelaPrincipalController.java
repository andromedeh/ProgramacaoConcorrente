/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 27/09/2023
* Ultima alteracao.: 07/10/2023
* Nome.............: TelaPrincipalController
* Funcao...........: Relaciona e controla os elementos da interface e os
conecta com a animacao nas threads dos trens
************************************************************************ */
package controle;
import java.net.URL;
import java.util.ResourceBundle;
import modelo.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class TelaPrincipalController implements Initializable{
  // atributos FXML
  @FXML 
  private AnchorPane anchorPanePrincipal;
  @FXML 
  private AnchorPane anchorPaneInicial;
  @FXML 
  private AnchorPane menuPosicao;
  @FXML 
  private AnchorPane menuSolucao;
  @FXML
  private Slider velocidadeTrem1; 
  @FXML
  private Slider velocidadeTrem2;
  @FXML
  private ImageView iconTrem1;
  @FXML
  private ImageView iconTrem2;
  @FXML
  private ImageView tituloVelocidade; 
  @FXML
  private ImageView tituloPosicao; 
  @FXML
  private RadioButton op1; // trens posicao inferior
  @FXML
  private RadioButton op2; // trens posicao superior
  @FXML
  private RadioButton op3; // trens posicao alternada I
  @FXML
  private RadioButton op4; // trens posicao alternada II
  @FXML
  private ToggleGroup toggleGroup = new ToggleGroup(); // agrupamento de todos os RadioButtons de direcao
  @FXML
  private RadioButton sol1; // solucao variavel de travamento
  @FXML
  private RadioButton sol2; // solucao estrita alternancia
  @FXML
  private RadioButton sol3; // solucao de peterson
  @FXML
  private ToggleGroup toggleGroup2 = new ToggleGroup(); // agrupamento de todos os RadioButtons de solucao
  @FXML
  private Button btnConfirmarOp; // botao p/ confirmar opcao de direcao
  @FXML
  private Button btnConfirmarSol; // botao p/ confirmar opcao de direcao
  @FXML
  private Button btnResetar; // botao p/ resetar posicao e velocidade
  @FXML
  private Button btnSolucao; // botao p/ trocar a solucao
  @FXML
  private Button btnIniciar; // botao p/ iniciar tela de simulacao
  // atributos do controlador
  private ThreadTrem1 trem1;
  private ThreadTrem2 trem2;
  private boolean reseted = false; // flag p/ garantir o reset nas threads se estiverem em sleep()
  // atributos relacionados as soluçoes para colisao
  public static volatile Object pauseLock = new Object(); // varivel para pausar threads
  public int solucaoEscolhida;
  public static int VT1; // variavel de travamento 1
  public static int VT2; // variavel de travamento 2
  public static int VEZ1 = 0; // vez no tunel 1, 0 para trem1 e 1 para trem2
  public static int VEZ2 = 0; // vez no tunel 2, 0 para trem1 e 1 para trem2
  private int turno1; // turno p/ tunel 1
  private int turno2; // turno p/ tunel 2
  private boolean interessado1[] = {false, false}; // interesse tunel1
  private boolean interessado2[] = {false, false}; // interesse tunel2

  /*********************************************************************
  * Metodo: initialize
  * Funcao: implementacao de metodo da interface Initializable, threads
  instanciadas, omite inicialmente alguns elementos da tela
  * Parametros: location e resources sao parametros predefinidos
  * Retorno: nenhum
  ******************************************************************* */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    anchorPaneInicial.setVisible(true);
    iconTrem1.setVisible(false);
    iconTrem2.setVisible(false);
    trem1 = new ThreadTrem1(this);
    trem2 = new ThreadTrem2(this);
  } // fim do método initialize
  
  /*********************************************************************
  * Metodo: iniciar
  * Funcao: omite a anchorPaneInicial, omite e/ou mostra alguns elementos 
  * Parametros: event eh um evento sobre o botao btnIniciar
  * Retorno: nenhum
  ******************************************************************* */
  public void iniciar(ActionEvent event){
    anchorPaneInicial.setVisible(false);
    menuPosicao.setVisible(true);
    tituloVelocidade.setVisible(false);
    velocidadeTrem1.setVisible(false); 
    velocidadeTrem2.setVisible(false);
    btnResetar.setVisible(false);
    btnSolucao.setVisible(false);
  } // fim do metodo iniciar

  /*********************************************************************
  * Metodo: posicionarTrens
  * Funcao: mostra preview da posicao dos trens de acordo a opcao selecionada 
  * pelo usuario e define qual sera o sentido de cada trem  
  * Parametros: event eh um evento sobre o toggleGroup
  * Retorno: nenhum
  ******************************************************************* */
  public void posicionarTrens(ActionEvent event){
    iconTrem1.setLayoutX(257); 
    iconTrem2.setLayoutX(324); 
    
    if(event.getSource().equals(op1)){ // opcao inferior
      iconTrem1.setLayoutY(410);
      iconTrem2.setLayoutY(410);
      iconTrem1.setRotate(180);
      iconTrem2.setRotate(180);
      trem1.setSentidoDoTrem(1);
      trem2.setSentidoDoTrem(1);
    } // fim do if op1

    if(event.getSource().equals(op2)){ // opcao superior
      iconTrem2.setLayoutY(-18);
      iconTrem1.setLayoutY(-18);
      iconTrem1.setRotate(0);
      iconTrem2.setRotate(0);
      trem1.setSentidoDoTrem(0);
      trem2.setSentidoDoTrem(0);
    } // fim do if op2

    if(event.getSource().equals(op3)){ // opcao alternado 1
      iconTrem1.setLayoutY(-18);
      iconTrem2.setLayoutY(410);
      iconTrem1.setRotate(0);
      iconTrem2.setRotate(180);
      trem1.setSentidoDoTrem(0);
      trem2.setSentidoDoTrem(1); 
      passarVEZ2(1); // para o trem2 nao precisar esperar o trem1 descer todo o caminho
    } // fim do if op3

    if(event.getSource().equals(op4)){ // opcao alternado 2
      iconTrem1.setLayoutY(410);
      iconTrem2.setLayoutY(-18);
      iconTrem1.setRotate(180);
      iconTrem2.setRotate(0);
      trem1.setSentidoDoTrem(1);
      trem2.setSentidoDoTrem(0);
      passarVEZ1(1);  // p/ o trem2 nao precisar esperar o trem1 subir todo o caminho
    } // fim do if op4
    iconTrem1.setVisible(true);
    iconTrem2.setVisible(true);
  }// fim do método posicionarTrens

  /*********************************************************************
  * Metodo: confirmarOpcao
  * Funcao: omite o menu de direcao e mostra o menu de solucao
  * Parametros: event eh um evento sobre o btnConfirmarOp
  * Retorno: nenhum
  ******************************************************************* */
  public void confirmarOpcao(ActionEvent event){ 
    menuPosicao.setVisible(false);
    menuSolucao.setVisible(true);
  } // fim do metodo ConfirmarOpcao

  /*********************************************************************
  * Metodo: selecionarSolucao
  * Funcao: define um valor para a variavel solucaoEscolhida
  * Parametros: event eh um evento sobre o toggleGroup2
  * Retorno: nenhum
  ******************************************************************* */
  public void selecionarSolucao(ActionEvent event){
    if(event.getSource().equals(sol1)){ // solucao 1 - VT
      solucaoEscolhida = 1;
    } // fim do if sol1

    if(event.getSource().equals(sol2)){ // solucao 2 - estrita alternancia
      solucaoEscolhida = 2;
    } // fim do if sol2

    if(event.getSource().equals(sol3)){ // solucao 3 - peterson
      solucaoEscolhida = 3;
    } // fim do if sol3
  } // fim do método selecionarSolucao

  /*********************************************************************
  * Metodo: confirmarSolucao
  * Funcao: define a solucaoEscolhida para os trens, omite o menu de direcao 
  e de solucao, mostra elementos relacionados com vel., reset e troca de solucao
  * Parametros: event eh um evento sobre o btnSolucao
  * Retorno: void
  ******************************************************************* */
  public void confirmarSolucao(MouseEvent event){ 
    menuPosicao.setVisible(false);
    menuSolucao.setVisible(false);
    tituloVelocidade.setVisible(true);
    velocidadeTrem1.setVisible(true); 
    velocidadeTrem2.setVisible(true);
    btnResetar.setVisible(true);
    btnSolucao.setVisible(true);
    trem1.setSolucao(solucaoEscolhida);
    trem2.setSolucao(solucaoEscolhida);
    if (trem1.isAlive() && trem2.isAlive()) { // caso esteja trocando a solucao
      resetar(event);
    }// fim do if
  } // fim do metodo confirmarSolucao

  /*********************************************************************
  * Metodo: mudarSolucao
  * Funcao: mostra menu solucao e omite os elementos de mudanca de velocidade
  * Parametros: event eh um evento sobre o botao btnSolucao
  * Retorno: void
  ******************************************************************* */
  public void mudarSolucao(MouseEvent event){
    tituloVelocidade.setVisible(false);
    velocidadeTrem1.setVisible(false); 
    velocidadeTrem2.setVisible(false);
    btnResetar.setVisible(false);
    btnSolucao.setVisible(false);
    menuSolucao.setVisible(true);
  } // fim do metodoMudarSolucao

  /*********************************************************************
  * Metodo: mudarVelocidadeTrem1
  * Funcao: varia a velocidade do trem1 de acordo com o slider
  * Parametros: event eh um evento sobre o slider velocidadeTrem1
  * Retorno: void
  ******************************************************************* */
  public void mudarVelocidadeTrem1(MouseEvent event){ 
    double velocidade = velocidadeTrem1.getValue();
    if (!trem1.isAlive()) { // iniciar a thread caso ainda nao esteja iniciada
      trem1.start();
    } else{
      trem1.resumeThread();
    } // fim do if-else
    trem1.setVelocidade(velocidade); 
  } // fim do metodo mudarVelocidadeTrem1

  /*********************************************************************
  * Metodo: mudarVelocidadeTrem2
  * Funcao: varia a velocidade do trem2 de acordo com o slider
  * Parametros: event eh um evento sobre o slider velocidadeTrem2
  * Retorno: void
  ******************************************************************* */
  public void mudarVelocidadeTrem2(MouseEvent event){
    double velocidade = velocidadeTrem2.getValue();
    if (!trem2.isAlive()) { // iniciar a thread caso ainda nao esteja iniciada
      trem2.start();
    } else{
      trem2.resumeThread();
    } // fim do if-else
    trem2.setVelocidade(velocidade); 
  } // fim do metodo mudarVelocidadeTrem2
  
  /*********************************************************************
  * Metodo: resetar
  * Funcao: reseta as variaveis relacionadas com as solucoes e interrompe
  as threads
  * Parametros: event eh um evento sobre btnResetar
  * Retorno: void
  ******************************************************************* */
  public void resetar(MouseEvent event){
    VT1 = VT2 = 0;
    VEZ1 = VEZ2 = 0;
    interessado1[0] = interessado1[1] = false;
    interessado2[0] = interessado2[1] = false;
    setReseted(true);
    try {
      trem1.resetar();
      trem2.resetar();
    } catch (InterruptedException e) {} // fim de try-catch
  }// fim do metodo resetar

  /*********************************************************************
  * Metodo: alterarVT1
  * Funcao: alterna os valores da variavel de travamento 1 de acordo a
  a movimentacao ou nao no tunel 1
  * Parametros: nenhum
  * Retorno: nenhum
  ******************************************************************* */
  public void alterarVT1() {
    if (VT1==1){
      VT1 = 0;
    } else{
      VT1 = 1;
    }// fim do if-else
  } // fim do metodo alteraVT1

  /*********************************************************************
  * Metodo: alterarVT2
  * Funcao: alterna os valores da variavel de travamento 2 de acordo 
  a movimentacao ou nao no tunel 2
  * Parametros: nenhum
  * Retorno: nenhum
  ******************************************************************* */
  public void alterarVT2() {
    if (VT2==1){
      VT2 = 0;
    } else{
      VT2 = 1;
    } // fim do if-else
  } // fim do metodo alteraVT2

  /*********************************************************************
  * Metodos: passarVEZ1 e passarVEZ2
  * Funcao: altera VEZ1 do tunel 1 e VEZ2 do tunel 2 respectivamente
  * Parametros: valor
  * Retorno: nenhum
  ******************************************************************* */
  public void passarVEZ1(int valor){ // tunel 1
    VEZ1 = valor;
  } // fim do passarVEZ1

  public void passarVEZ2(int valor){ // tunel 2
    VEZ2 = valor;
  } // fim do passarVEZ2

  /*********************************************************************
  * Metodo: mouseOn
  * Funcao: altera estilo dos botoes na tela quando o mouse esta em cima
  * Parametros: event eh um evento de entrada do mouse sobre os botoes 
  * btnconfirmarOp, btnConfirmarSol, bntResetar e btnSolucao
  * Retorno: void
  ******************************************************************* */
  @FXML
  public void mouseOn(MouseEvent event){
    if (event.getSource().equals(btnResetar)){
      btnResetar.setStyle("-fx-background-radius: 10; -fx-background-radius: 10; -fx-background-color: #38702F;");
    } // fim do if
    if (event.getSource().equals(btnConfirmarOp)){
      btnConfirmarOp.setStyle("-fx-background-radius: 10; -fx-background-radius: 10; -fx-background-color: #38702F;");
    } // fim do if
    if (event.getSource().equals(btnConfirmarSol)){
      btnConfirmarSol.setStyle("-fx-background-radius: 10; -fx-background-radius: 10; -fx-background-color: #38702F;");
    } // fim do if
    if (event.getSource().equals(btnSolucao)){
      btnSolucao.setStyle("-fx-background-radius: 10; -fx-background-radius: 10; -fx-background-color: #38702F;");
    } // fim do if
  } // fim do metodo mouseOn

  /*********************************************************************
  * Metodo: MouseOff
  * Funcao: restaura o estilo dos botoes na tela quando o mouse sai
  * Parametros: event eh um evento de saida do mouse sobre os botoes 
  * btnconfirmarOp, btnConfirmarSol, bntResetar e btnSolucao
  * Retorno: void
  ******************************************************************* */
  @FXML
  public void mouseOff(MouseEvent event){
    if (event.getSource().equals(btnResetar)){
      btnResetar.setStyle("-fx-background-radius: 10; -fx-background-radius: 10; -fx-background-color: #4E9B68;");
    } //fim do if
    if (event.getSource().equals(btnConfirmarOp)){
      btnConfirmarOp.setStyle("-fx-background-radius: 10; -fx-background-radius: 10; -fx-background-color: #4E9B68;");
    } // fim do if
    if (event.getSource().equals(btnConfirmarSol)){
      btnConfirmarSol.setStyle("-fx-background-radius: 10; -fx-background-radius: 10; -fx-background-color: #4E9B68;");
    } // fim do if
    if (event.getSource().equals(btnSolucao)){
      btnSolucao.setStyle("-fx-background-radius: 10; -fx-background-radius: 10; -fx-background-color: #4E9B68;");
    } // fim do if
  }  // fim do metodo mouseOff

  // ---------------------------- getters e setters --------------------------------
  public ImageView getIconTrem1() {
    return iconTrem1;
  } 

  public ImageView getIconTrem2() {
    return iconTrem2;
  } 
 
  public static int getVT1() {
    return VT1;
  } 

  public static int getVT2() {
    return VT2;
  } 

  public static int getVEZ1() {
    return VEZ1;
  } 

  public static int getVEZ2() {
    return VEZ2;
  } 

  public boolean getInteresed1(int n){
    return this.interessado1[n];
  }
  
  public boolean getInteresed2(int n){
    return this.interessado2[n];
  }

  public int getTurno1(){
    return this.turno1;
  }

  public int getTurno2(){
    return this.turno2;
  }

  public void setTurno1(int turno){
    this.turno1 = turno;
  }

  public void setTurno2(int turno){
    this.turno2 = turno;
  }
  
  public void setInteresed1(int n, boolean estado){
    this.interessado1[n] = estado;
  }
  
  public void setInteresed2(int n, boolean estado){
    this.interessado2[n] = estado;
  }

  public boolean isReseted() {
    return reseted;
  }

  public void setReseted(boolean reseted) {
    this.reseted = reseted;
  }

  // metodo para encerrar no terminal corretamente
  public void fechar(){
    System.exit(0);
  } // fim do metodo fechar

} // fim da classe TelaPrincipalController