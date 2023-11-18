/* *********************************************************************
* Autor............: Andreina Novaes Silva Melo
* Matricula........: 202210562
* Inicio...........: 07/11/2023
* Ultima alteracao.: 14/11/2023
* Nome.............: Barbeiro Dorminhoco
* Funcao...........: O programa implementa o problema do Barbeiro Dorminhoco
                     com solucao por semaforos
************************************************************************ */
import java.io.File;
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
  Image iconImage = new Image(getClass().getClassLoader().getResourceAsStream("resources/img/icon.png")); // busca o icone do programa
  URL arquivoFXML = getClass().getResource("/view/MainScreen.fxml"); // busca a pagina fxml
  @Override
  public void start(Stage stage) throws IOException {
    try {
      Parent root = FXMLLoader.load(arquivoFXML); // define tela e controlador
      Scene scene = new Scene(root);  
      stage.getIcons().add(iconImage); // troca icone                    
      stage.setScene(scene); // define a cena
      stage.setTitle("Barbeiro Dorminhoco"); // define titulo
      stage.setResizable(false); // nao redimensionar a tela
      Font.loadFont(getClass().getResourceAsStream( "resources/Super Milk.ttf"), 12); // adcionando fonte
      scene.getStylesheets().add("resources/Style.css"); // adicionando folhas de estilo css
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