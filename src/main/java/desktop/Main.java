package desktop;

import desktop.controller.RoomController;
import desktop.controller.SummaryController;
import desktop.model.primitives.Room;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;

    private static Stage mainWindowStage;

    private static Stage newRoomStage;

    private static Stage roomStage;

    private static FXMLLoader roomLoader;

    private static Stage summaryStage;

    private static FXMLLoader summaryLoader;


    private static FXMLLoader mainMenuLoader;

    private static FXMLLoader newRoomLoader;

    @Override
    public void start(Stage startStage) throws Exception{
        Main.primaryStage = startStage;
        Parent chooseNickScene = FXMLLoader.load(getClass().getResource("/view/chooseNickPane.fxml"));
        startStage.setTitle("Gra w kości");
        Scene chooseNickWindow = new Scene(chooseNickScene, 500, 150);
        startStage.setScene(chooseNickWindow);
        startStage.show();
    }

    public static void closeShowNickWindow() {
        if (primaryStage != null)
            primaryStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /** Methods for creating new windows
     * Prevents code duplication
     * **/

    public static void createMainWindow() {
        try {
            mainMenuLoader = new FXMLLoader();
            mainMenuLoader.setLocation(Main.class.getResource("/view/mainMenu.fxml"));
            TabPane page = mainMenuLoader.load();

            mainWindowStage = new Stage();
            mainWindowStage.setTitle("Gra w kosci");
            mainWindowStage.initModality(Modality.WINDOW_MODAL);
            Scene mainWindow = new Scene(page, 1000, 600);
            mainWindowStage.setScene(mainWindow);

            mainWindowStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewRoomWindow() {
        try {
            newRoomLoader = new FXMLLoader();
            newRoomLoader.setLocation(Main.class.getResource("/view/newRoom.fxml"));
            GridPane page = newRoomLoader.load();

            newRoomStage = new Stage();
            newRoomStage.setTitle("Stworz nowy pokoj");
            newRoomStage.initModality(Modality.WINDOW_MODAL);
            newRoomStage.initOwner(mainWindowStage);
            Scene newRoomWindow = new Scene(page);
            newRoomStage.setScene(newRoomWindow);

            newRoomStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeNewRoomWindow() {
        if (newRoomStage != null) {
            newRoomStage.close();
        }
    }

    public static void createRoomWindow(Room room) {
        try {
            roomLoader = new FXMLLoader();
            roomLoader.setLocation(Main.class.getResource("/view/room.fxml"));
            BorderPane page = roomLoader.load();

            roomStage = new Stage();
            roomStage.setTitle("Rozgrywka");
            roomStage.initModality(Modality.WINDOW_MODAL);
            roomStage.initOwner(mainWindowStage);
            Scene roomWindow = new Scene(page);
            roomStage.setScene(roomWindow);
            // Setting desktop.model for the the controller
            ((RoomController)roomLoader.getController()).setRoom(room);

            createSummaryWindow(room);

            roomStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createSummaryWindow(Room room) {
        try {
            summaryLoader = new FXMLLoader();
            summaryLoader.setLocation(Main.class.getResource("/view/summary.fxml"));
            BorderPane page = summaryLoader.load();

            summaryStage = new Stage();
            summaryStage.setTitle("Wyniki");
            summaryStage.initModality(Modality.WINDOW_MODAL);
            summaryStage.initOwner(mainWindowStage);
            Scene roomWindow = new Scene(page);
            summaryStage.setScene(roomWindow);

            ((SummaryController)summaryLoader.getController()).setRoom(room);

            summaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeSummary() {
        if (summaryStage != null) {
            summaryStage.close();
        }
    }

    public static FXMLLoader getRoomLoader() {
        return roomLoader;
    }

    public static FXMLLoader getMainMenuLoader() {
        return mainMenuLoader;
    }

    public static FXMLLoader getNewRoomLoader() {
        return newRoomLoader;
    }

    public static FXMLLoader getSummaryLoader() {
        return summaryLoader;
    }


}
