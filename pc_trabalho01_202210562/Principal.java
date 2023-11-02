/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 22/08/2023
* Ultima alteracao.: 01/09/2023
* Nome.............: PokeTrem
* Funcao...........: Simular o funcionamento de trens em um percurso usando GUI
************************************************************************ */
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controle.TelaPrincipalController;

public class Principal extends Application {
  Image iconImage = new Image(getClass().getClassLoader().getResourceAsStream("img/logo.png")); // busca o icone do programa
  URL arquivoFXML = getClass().getResource("/visao/TelaPrincipal.fxml"); // busca a pagina fxml
  @Override
  public void start(Stage stage) throws IOException {
    try {
      Parent root = FXMLLoader.load(arquivoFXML); // define tela e controlador
      Scene scene = new Scene(root);  
      stage.getIcons().add(iconImage); // troca icone                    
      stage.setScene(scene); // define a cena
      stage.setTitle("PokeTrem");
      stage.setResizable(false); // nao redimensionar a tela
      stage.show(); // mostra a tela
    } catch (IOException e) {
      e.printStackTrace();
    } // fim do try-catch
  } // fim do start 

  public static void main(String[] args) {
    launch(args);
  } // fim do main

} // fim da classe Principal