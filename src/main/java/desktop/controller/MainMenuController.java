package desktop.controller;

import desktop.model.primitives.GameType;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import desktop.model.RoomList;

public class MainMenuController {

    @FXML
    public BorderPane nPlus;
    @FXML
    public RoomListController nPlusController;
    @FXML
    public BorderPane poker;
    @FXML
    public RoomListController pokerController;
    @FXML
    public BorderPane nProduct;
    @FXML
    public RoomListController nProductController;

    public void initialize() {
        setUpRoomLists();
    }

    public void setUpRoomLists() {
        nPlusController.setRoomList(new RoomList(GameType.N_PLUS));
        nProductController.setRoomList(new RoomList(GameType.N_PRODUCT));
        pokerController.setRoomList(new RoomList(GameType.POKER));
    }
}
