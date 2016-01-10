package desktop.controller;

import desktop.model.AppConfig;
import desktop.model.IServerConnector;
import desktop.model.MoveInfo;
import desktop.model.ServerConnectorFactory;
import desktop.model.primitives.Dice;
import desktop.model.primitives.Participant;
import desktop.model.primitives.Room;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class RoomController implements Observer {

    private IServerConnector serverConnector = ServerConnectorFactory.getServerConnector();

    private final static Logger logger = Logger.getLogger(RoomController.class.getName());

    @FXML
    public Text leaderInfo;
    @FXML
    public ListView<String> playerList;
    @FXML
    public Button mainActionButton;
    @FXML
    public Text gameInfo;
    @FXML
    public Text currentPlayerInfo;
    @FXML
    public Text opponentDiceOverheadText;
    @FXML
    public Text ownDiceOverheadText;

    @FXML
    private DiceDisplayController opponentDiceDisplayController;
    @FXML
    private DiceDisplayController ownDiceDisplayController;

    private Room room;

    ObservableList<String> playerInfos = FXCollections.observableArrayList();

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {

        this.room = room;
        setUpDice();
        setUpParticipantList();
        update(null, null);
    }

    private void setUpDice() {
        opponentDiceDisplayController.draw();
        opponentDiceDisplayController.setSelectionEnabled(false);

        if (AppConfig.getUserParticipant() != null) {
            ownDiceDisplayController.setDice(AppConfig.getUserParticipant().getDice());
            ownDiceDisplayController.draw();
        }

    }

    private void updateGameInfo() {
        gameInfo.setText(String.format("%s - %s: %s", room.getName(), room.getGameType().getName(), "Gra sie gra"));
        // TODO: Obviously...
    }

    private void updateCurrentParticipant() {
        if (room.getCurrentParticipant() != null) {
            currentPlayerInfo.setText(String.format("Aktualny gracz: %s",room.getCurrentParticipant().getNick()));
        } else {
            currentPlayerInfo.setText("Oczekiwanie na graczy");
        }
    }

    private void updateCurrentLeader() {
        Participant leader = room.getParticipants().get(0);
        leaderInfo.setText(leader.getNick());
        // TODO: Change
    }

    private void updateMainActionButton() {
        if (room.getCurrentParticipant() == AppConfig.getUserParticipant()) {
            mainActionButton.setDisable(false);
            mainActionButton.setDefaultButton(true);
            if (AppConfig.getUserParticipant() != null && Dice.selectionNotEmpty(AppConfig.getUserParticipant().getDice())) {
                mainActionButton.setText("Przerzuć i zakończ ruch");
            } else {
                mainActionButton.setText("Zakończ ruch");
            }
        } else {
            mainActionButton.setDisable(true);
            mainActionButton.setDefaultButton(false);
            mainActionButton.setText("Czekaj na koniec tury przeciwnika");
        }
    }

    private void updateDice() {
        updateOwnDiceSelectivity();
        updateOpponentDiceVisibility();
        updateOwnDiceVisibility();
    }

    private void updateOwnDiceSelectivity() {
        ownDiceDisplayController.setSelectionEnabled(room.getCurrentParticipant() == AppConfig.getUserParticipant());
    }

    private void updateOpponentDiceVisibility() {
        if (room.getCurrentParticipant() != AppConfig.getUserParticipant()) {
            setOpponentDiceVisibility(true);
            if (getSelectedParticipant() != null) {
                opponentDiceDisplayController.setDice(getSelectedParticipant().getDice());
                opponentDiceOverheadText.setText("Kości gracza: " + getSelectedParticipant().getNick());
            }
        } else {
            setOpponentDiceVisibility(false);
        }
    }

    private void setOpponentDiceVisibility(boolean value) {
        opponentDiceDisplayController.setVisible(value);
        opponentDiceOverheadText.setVisible(value);
    }

    private void updateOwnDiceVisibility() {
        if (AppConfig.getUserParticipant() != null) {
            setOwnDiceVisibility(true);
        } else {
            setOwnDiceVisibility(false);
        }
    }

    private void setOwnDiceVisibility(boolean value) {
        ownDiceDisplayController.setVisible(value);
        ownDiceOverheadText.setVisible(value);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (!room.empty()) {
            updateCurrentLeader();
            updateMainActionButton();
        }
        updateGameInfo();
        updateCurrentParticipant();
        updateParticipantList();
        updateDice();
    }

    public void setUpParticipantList() {
        room.getParticipants().addListener(new ListChangeListener<Participant>() {
            @Override
                public void onChanged(Change<? extends Participant> c) {
                    updateParticipantList();
                }
        });
    }

    private void updateParticipantList() {
        playerInfos.clear();
        for (Participant p : room.getParticipants().sorted()) {
            playerInfos.add(p.getNick()); // TODO: Result or something
        }
        playerList.setItems(playerInfos);
    }

    public void handlePlayerListClick(Event event) {
        if (!playerList.getSelectionModel().isEmpty()) {
            updateOpponentDiceVisibility();
        }
    }

    private Participant getSelectedParticipant() {
        for (Participant p : room.getParticipants()) {
            if (p.getNick().equals(playerList.getSelectionModel().getSelectedItem())) {
                return p;
            }
        }
        return null;
    }

    public void handleMainActionButtonClicked(ActionEvent actionEvent) throws IOException, InterruptedException {
        if (room.getCurrentParticipant() == AppConfig.getUserParticipant()) {
            logger.info("Player clicked on the move button");
            serverConnector.move(prepareMoveInfo());
            ownDiceDisplayController.deselectDice();
        }
    }

    private MoveInfo prepareMoveInfo() {
        MoveInfo moveInfo = new MoveInfo();
        moveInfo.setNickname(room.getCurrentParticipant().getNick());
        moveInfo.setDicesToRoll(convertToIndices(room.getCurrentParticipant().getDice().getMarkedForRethrow()));
        return moveInfo;
    }

    private int[] convertToIndices(boolean[] markedForRethrow) {

        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < markedForRethrow.length; i++) {
            if (markedForRethrow[i]) {
                indices.add(i);
            }
        }

        return indices.stream().mapToInt(x -> x).toArray();
    }
}
