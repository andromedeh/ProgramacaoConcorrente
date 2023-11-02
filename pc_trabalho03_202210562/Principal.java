/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 13/10/2023
* Ultima alteracao.: 27/10/2023
* Nome.............: Demon Slayer: Arco do Jantar dos Hashiras
* Funcao...........: O programa implementa o problema do Jantar dos Filosofos
                     com solucao por semaforos
************************************************************************ */
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import control.MainController;

public class Principal extends Application {
  MainController controller = new MainController();
  Image iconImage = new Image(getClass().getClassLoader().getResourceAsStream("img/icon.png")); // busca o icone do programa
  URL arquivoFXML = getClass().getResource("/view/MainScreen.fxml"); // busca a pagina fxml
  @Override
  public void start(Stage stage) throws IOException {
    try {
      Parent root = FXMLLoader.load(arquivoFXML); // define tela e controlador
      Scene scene = new Scene(root);  
      stage.getIcons().add(iconImage); // troca icone                    
      stage.setScene(scene); // define a cena
      stage.setTitle("Demon Slayer: Arco do Jantar dos Hashiras"); // define titulo
      stage.setResizable(false); // nao redimensionar a tela
      scene.getStylesheets().add("view/Style.css"); // adicionando folhas de estilo css
      Font.loadFont(getClass().getResourceAsStream("/view/Korean_Calligraphy.ttf"), 14); // adicionando fonte
      stage.setOnCloseRequest(e -> { // encerrar corretamente
        Platform.exit();
        System.exit(0);
      });
      stage.show(); // mostra a tela
    } catch (IOException e) {
      e.printStackTrace();
    } // fim do try-catch
  } // fim do start 

  public static void main(String[] args) {
    launch(args);
  } // fim do main

} // fim da classe Principal