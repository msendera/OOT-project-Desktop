package desktop.controller;

import desktop.Main;
import desktop.model.RoomList;
import desktop.model.primitives.Participant;
import desktop.model.primitives.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class SummaryController {

    @FXML
    public Text gameResult;

    @FXML
    public ListView playerList;

    private ObservableList<String> playerInfos = FXCollections.observableArrayList();

    Room room;

    public SummaryController() {
    }

    public SummaryController(Room room) {
        setRoom(room);
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        createList();
    }

    private void createList() {
        playerInfos.clear();
        for (Participant p : room.getParticipants().sorted()) {
            playerInfos.add(p.getNick()); // TODO: Result or something
        }
        playerList.setItems(playerInfos);
    }

    public void handleContinueButtonClicked(ActionEvent actionEvent) {
        Main.closeSummary();
    }
}
