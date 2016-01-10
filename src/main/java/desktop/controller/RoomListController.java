package desktop.controller;

import desktop.Main;
import desktop.model.*;
import desktop.model.adapters.RoomInfoDisplay;
import desktop.model.primitives.Participant;
import desktop.model.primitives.Room;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class RoomListController {

    private final static Logger logger = Logger.getLogger(RoomListController.class.getName());

    @FXML
    Text nickTextField;
    @FXML
    TableView mainTable;
    @FXML
    TableColumn gameNameCol;
    @FXML
    TableColumn gameStateCol;
    @FXML
    TableColumn numberOfPlayersCol;
    @FXML
    TableColumn pointsToVictoryCol;
    @FXML
    TableColumn playersCol;
    @FXML
    Button createButton;
    @FXML
    Button joinObserverButton;
    @FXML
    Button joinParticipantButton;
    @FXML
    CheckBox showEmptyCheckBox;
    @FXML
    CheckBox showFullCheckBox;

    private IServerConnector serverConnector = ServerConnectorFactory.getServerConnector();

    private RoomList roomList;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        nickTextField.setText("Twój nick to: " + AppConfig.getUserNick());

        List<Room> roomsList = serverConnector.getRoomsList();
        roomsList.stream().map(Room::getName).forEach(System.out::println);
        //TODO set column values
    }

    public RoomListController() {
        System.out.println(String.format("%s created", this.toString()));
    }

    public void handleJoinParticipantButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        if (!mainTable.getSelectionModel().isEmpty()) {
            Room selectedRoom = getSelectedRoom();
            if (!selectedRoom.full()) {
                logger.info("Entering room " + getSelectedRoom().getId() + " as a participant");
                AppConfig.setUserParticipant(new Participant(AppConfig.getUserNick()));
                Main.createRoomWindow(getSelectedRoom());
                LocalServerConnector.getInstance().joinAsParticipant(selectedRoom.getId());
            }
        }
    }

    public void handleJoinSpectatorButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        if (!mainTable.getSelectionModel().isEmpty()) {
            logger.info("Entering room " + getSelectedRoom().getId() + " as a spectator");
            LocalServerConnector.getInstance().joinAsSpectator(getSelectedRoom().getId());
            Main.createRoomWindow(getSelectedRoom());
        }
    }

    private Room getSelectedRoom() {
        SelectionModel<RoomInfoDisplay> selectionModel = mainTable.getSelectionModel();
        for (Room r : roomList.getRooms()) {
            if (selectionModel.getSelectedItem().getRoom() == r) {
                    logger.info("Selected room's ID is " + r.getId());
                    return r;
            }
        }
        logger.info("Selection model empty - no room selected");
        return null;
    }

    public void handleCreateRoomButton(ActionEvent actionEvent) {
        Main.createNewRoomWindow();
    }

    public void handleShowEmptyCheckBoxAction(ActionEvent actionEvent) {
        mainTable.getItems().clear(); // It will change
        roomList.setShowEmpty(showEmptyCheckBox.isSelected());
    }

    public void handleShowFullCheckBox(ActionEvent actionEvent) {
        mainTable.getItems().clear(); // It will change
        roomList.setShowFull(showFullCheckBox.isSelected());
    }

    public RoomList getRoomList() {
        return roomList;
    }

    public void setRoomList(RoomList roomList) {
        this.roomList = roomList;
        if (roomList != null) {
            mainTable.setItems(roomList.getRoomsInfos());
            ObservableList columns = mainTable.getColumns();
            gameNameCol.setCellValueFactory(new PropertyValueFactory<RoomInfoDisplay,String>("name"));
            gameStateCol.setCellValueFactory(new PropertyValueFactory<RoomInfoDisplay,String>("gameStatus"));
            numberOfPlayersCol.setCellValueFactory(new PropertyValueFactory<RoomInfoDisplay,String>("participantCountStatus"));
            pointsToVictoryCol.setCellValueFactory(new PropertyValueFactory<RoomInfoDisplay,String>("pointsToVictory"));
            playersCol.setCellValueFactory(new PropertyValueFactory<RoomInfoDisplay,String>("participants"));
        }
    }


    public void handleTableAction(Event event) {
        logger.info("Clicked on table");
        if (!mainTable.getSelectionModel().isEmpty()) {
            logger.info("Selection model is not empty");
            joinObserverButton.setDisable(false);
            if (!getSelectedRoom().full()) {
                joinParticipantButton.setDisable(false);
                joinParticipantButton.setDefaultButton(true);
            } else {
                joinObserverButton.setDefaultButton(true);
            }
        } else {
            logger.info("Selection model is empty");
            joinObserverButton.setDisable(true);
            joinObserverButton.setDefaultButton(false);
            joinParticipantButton.setDisable(true);
            joinParticipantButton.setDefaultButton(false);
        }
    }
}
