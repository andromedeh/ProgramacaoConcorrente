/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 22/08/2023
* Ultima alteracao.: 31/08/2023
* Nome.............: PokeTrem
* Funcao...........: Simular o funcionamento de trens em um percurso usando GUI
************************************************************************ */
package controle;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PathTransition;
import javafx.util.Duration;
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
import javafx.scene.shape.Polyline;

public class TelaPrincipalController implements Initializable{
  // atributos FXML
  @FXML 
  private AnchorPane anchorPanePrincipal;
  @FXML 
  private AnchorPane anchorPaneInicial;
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
  private ToggleGroup toggleGroup = new ToggleGroup(); // agrupamento de todos os RadioButtons
  @FXML
  private Button btnConfirmarOp; // botao p/ confirmar opcao de direcao
  @FXML
  private Button btnResetar; // botao p/ resetar posicao e velocidade
  @FXML
  private Button btnDirecao; // botao p/ trocar a direcao
  @FXML
  private Button btnIniciar; // botao p/ iniciar tela de simulacao
  @FXML
  private Polyline descidaTrem1; // cada polyline define um tipo de caminho p cada trem
  @FXML
  private Polyline subidaTrem1;
  @FXML
  private Polyline descidaTrem2;
  @FXML
  private Polyline subidaTrem2;
  // atributos da classe controladora
  private Polyline caminhoDoTrem1; 
  private Polyline caminhoDoTrem2;
  private PathTransition transicaoTrem1 = new PathTransition();
  private PathTransition transicaoTrem2 = new PathTransition();
  
  /*********************************************************************
  * Metodo: initialize
  * Funcao: implementacao de metodo da interface Initializable, sao omitidos 
  * incialmente alguns elementos da tela
  * Parametros: location e resources sao parametros predefinidos
  * Retorno: void
  ******************************************************************* */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    anchorPaneInicial.setVisible(true);
    velocidadeTrem1.setVisible(false); 
    velocidadeTrem2.setVisible(false);
    btnResetar.setVisible(false);
    iconTrem1.setVisible(false);
    iconTrem2.setVisible(false);
  } // fim do método initialize
  
  /*********************************************************************
  * Metodo: iniciar
  * Funcao: omite a anchorPaneInicial para que seja possivel visualizar
  * a anchorPanePrincipal em que ocorrera a simulacao 
  * Parametros: event eh um evento sobre o botao btnIniciar
  * Retorno: void
  ******************************************************************* */
  public void iniciar(ActionEvent event){
    anchorPaneInicial.setVisible(false);
  } // fim do metodo iniciar

  /*********************************************************************
  * Metodo: posicionarTrens
  * Funcao: mostra preview da posicao dos trens de acordo a opcao selecionada 
  * pelo usuario e define qual sera o caminho de cada trem  
  * Parametros: event eh um evento sobre todos os RadioButton
  * Retorno: void
  ******************************************************************* */
  public void posicionarTrens(ActionEvent event){ 
      iconTrem1.setLayoutX(234); 
      iconTrem2.setLayoutX(302); 

      if(event.getSource().equals(op1)){ // opcao inferior
        iconTrem1.setLayoutY(420);
        iconTrem2.setLayoutY(420);
        iconTrem1.setRotate(-90);
        iconTrem2.setRotate(-90);
        caminhoDoTrem1 = subidaTrem1;
        caminhoDoTrem2 = subidaTrem2;
      } // fim do if op1

      if(event.getSource().equals(op2)){ // opcao superior
        iconTrem2.setLayoutY(12);
        iconTrem1.setLayoutY(10);
        iconTrem1.setRotate(90);
        iconTrem2.setRotate(90);
        caminhoDoTrem1 = descidaTrem1;
        caminhoDoTrem2 = descidaTrem2;
      } // fim do if op2

      if(event.getSource().equals(op3)){ // opcao alternado 1
        iconTrem1.setLayoutY(10);
        iconTrem2.setLayoutY(420);
        iconTrem1.setRotate(90);
        iconTrem2.setRotate(-90);
        caminhoDoTrem1 = descidaTrem1;
        caminhoDoTrem2 = subidaTrem2;
      } // fim do if op3

      if(event.getSource().equals(op4)){ // opcao alternado 2
        iconTrem1.setLayoutY(420);
        iconTrem2.setLayoutY(10);
        iconTrem1.setRotate(-90);
        iconTrem2.setRotate(90);
        caminhoDoTrem1 = subidaTrem1;
        caminhoDoTrem2 = descidaTrem2;
      } // fim do if op4
      iconTrem1.setVisible(true);
      iconTrem2.setVisible(true);
  } // fim do método posicionarTrens

  /*********************************************************************
  * Metodo: confirmarOpcao
  * Funcao: confirma escolha do usuario, omite o menu de direcao, mostra 
  * o menu de velocidade e chama funcao que cria as transicoes usadas para 
  * o efeito de animacao dos trens
  * Parametros: event eh um evento sobre o btnConfirmarOp
  * Retorno: void
  ******************************************************************* */
  public void confirmarOpcao(ActionEvent event){ 
      op1.setVisible(false);
      op2.setVisible(false);
      op3.setVisible(false);
      op4.setVisible(false);
      btnConfirmarOp.setVisible(false);
      tituloPosicao.setVisible(false);
      tituloVelocidade.setVisible(true);
      velocidadeTrem1.setVisible(true); 
      velocidadeTrem2.setVisible(true);
      btnResetar.setVisible(true);
      btnDirecao.setVisible(true);
      criarAnimacoes();
  } // fim do metodo ConfirmarOpcao

  /*********************************************************************
  * Metodo: criarAnimacoes
  * Funcao: define os parametros das transicoes dos trens
  * Parametros: nenhum
  * Retorno: void
  ******************************************************************* */
  public void criarAnimacoes(){
    // transicao do trem1
    transicaoTrem1.setDuration(Duration.seconds(10));
    transicaoTrem1.setNode(iconTrem1);
    transicaoTrem1.setPath(caminhoDoTrem1); // o caminho oficial do trem eh sobre a polyline determinada
    transicaoTrem1.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT); //  trem se desloca de acordo a Polyline
    transicaoTrem1.setCycleCount(PathTransition.INDEFINITE); // transicao ciclica
    // transicao do trem 2
    transicaoTrem2.setDuration(Duration.seconds(10)); 
    transicaoTrem2.setNode(iconTrem2);
    transicaoTrem2.setPath(caminhoDoTrem2);
    transicaoTrem2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
    transicaoTrem2.setCycleCount(PathTransition.INDEFINITE);
  } // fim do metodo criarAnimacoes

  /*********************************************************************
  * Metodo: movimentarTrem1
  * Funcao: move o iconTrem1 e varia a velocidade de acordo com o slider
  * Parametros: event eh um evento sobre o slider velocidadeTrem1
  * Retorno: void
  ******************************************************************* */
  public void movimentarTrem1(MouseEvent event){ 
    double velocidade = velocidadeTrem1.getValue();
    if (velocidade == 0){
      transicaoTrem1.pause();
    } else{
      transicaoTrem1.pause();
      transicaoTrem1.setRate(velocidade/25);
      transicaoTrem1.play();
    } //fim do if-else
  } // fim do metodo movimentarTrem1

  /*********************************************************************
  * Metodo: movimentarTrem2
  * Funcao: move o iconTrem2 e varia a velocidade de acordo com o slider
  * Parametros: event eh um evento sobre o slider velocidadeTrem2
  * Retorno: void
  ******************************************************************* */
  public void movimentarTrem2(MouseEvent event){
    double velocidade = velocidadeTrem2.getValue();
    if (velocidade == 0){
      transicaoTrem2.pause();
    } else{
      transicaoTrem2.pause();
      transicaoTrem2.setRate(velocidade/25);
      transicaoTrem2.play();
    }  //fim do if-else
  } // fim do metodo movimentarTrem2
  
  /*********************************************************************
  * Metodo: resetar
  * Funcao: reseta a posicao e velocidade dos dois trens e reseta os sliders
  * Parametros: event eh um evento sobre btnResetar
  * Retorno: void
  ******************************************************************* */
  public void resetar(ActionEvent event){
    transicaoTrem1.playFrom(Duration.seconds(0));
    transicaoTrem1.stop();
    transicaoTrem2.playFrom(Duration.seconds(0));
    transicaoTrem2.stop();
    velocidadeTrem1.setValue(0);
    velocidadeTrem2.setValue(0);
  }  // fim do metodo resetar

  /*********************************************************************
  * Metodo: mudarDirecao
  * Funcao: mostra os elementos de escolha de direcoes e omite os 
  * elementos de mudanca de velocidade
  * Parametros: event eh um evento sobre o botao btnDirecao
  * Retorno: void
  ******************************************************************* */
  public void mudarDirecao(ActionEvent event){
    resetar(event);
    velocidadeTrem1.setVisible(false);
    velocidadeTrem2.setVisible(false);
    tituloVelocidade.setVisible(false);
    btnResetar.setVisible(false);
    btnDirecao.setVisible(false);
    op1.setVisible(true);
    op2.setVisible(true);
    op3.setVisible(true);
    op4.setVisible(true);
    btnConfirmarOp.setVisible(true);
    tituloPosicao.setVisible(true);
  } 
  /*********************************************************************
  * Metodo: mouseOn
  * Funcao: altera estilo dos botoes na tela quando o mouse esta em cima
  * Parametros: event eh um evento de entrada do mouse sobre os botoes 
  * btnconfirmarOp, bntResetar e btnDirecao
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
    if (event.getSource().equals(btnDirecao)){
      btnDirecao.setStyle("-fx-background-radius: 10; -fx-background-radius: 10; -fx-background-color: #38702F;");
    } // fim do if
  } // fim do metodo mouseOn

  /*********************************************************************
  * Metodo: MouseOff
  * Funcao: restaura o estilo dos botoes na tela quando o mouse sai
  * Parametros: event eh um evento de saida do mouse sobre os botoes 
  * btnconfirmarOp, bntResetar e btnDirecao
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
    if (event.getSource().equals(btnDirecao)){
      btnDirecao.setStyle("-fx-background-radius: 10; -fx-background-radius: 10; -fx-background-color: #4E9B68;");
    } // fim do if
  }  // fim do metodo mouseOff
} // fim da classe TelaPrincipalController