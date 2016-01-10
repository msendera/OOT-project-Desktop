package desktop;

import desktop.controller.DiceDisplayController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DiceDisplayControllerTest extends Application {

    @org.junit.Test
    public void SimpleTest() {
        launch(null);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Canvas test!");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/dieDisplay.fxml"));
        GridPane root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root, 800, 450));
        primaryStage.show();

        DiceDisplayController diceDisplayController = fxmlLoader.<DiceDisplayController>getController();
        diceDisplayController.draw();
    }
}

