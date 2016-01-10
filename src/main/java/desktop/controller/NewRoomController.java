package desktop.controller;

import desktop.exception.InvalidRoomParameterException;
import desktop.exception.UnsupportedGameTypeException;
import desktop.model.primitives.GameType;
import desktop.model.primitives.Participant;
import desktop.model.primitives.Room;
import desktop.model.primitives.Spectator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import desktop.Main;
import desktop.model.*;

import java.util.Arrays;

public class NewRoomController {

    @FXML
    public TextField gameNameField;
    @FXML
    public TextField playerNumberField;
    @FXML
    public Button finalizeParticipantButton;
    @FXML
    public Button finalizeObserverButton;
    @FXML
    public ComboBox<String> easyBotCombo;
    @FXML
    public ComboBox<String> hardBotCombo;
    @FXML
    public ComboBox<String> gameTypeCombo;
    @FXML
    public TextField pointsToVictoryField;

    private int maxPlayers;

    private int remainingPlaces;

    private IServerConnector serverConnector = ServerConnectorFactory.getServerConnector();

    @FXML
    public void initialize() {
        setUpGameTypeComboBox();
    }

    public void handleFinalizeParticipantButton(ActionEvent actionEvent) {
        try {
            Room room = buildRoom();
            serverConnector.createRoom(room);
            Participant participant = new Participant(AppConfig.getUserNick());
            room.addParticipant(participant);
            AppConfig.setUserParticipant(participant);
            Main.createRoomWindow(room);
            Main.closeNewRoomWindow();
        } catch (InvalidRoomParameterException e) {
            e.printStackTrace();// TODO Jakies okienko czy cos :)
        }
    }

    public void handleFinalizeObserverButton(ActionEvent actionEvent) {
        try {
            Room room = buildRoom();
            serverConnector.createRoom(room);
            room.addSpectator(new Spectator(AppConfig.getUserNick()));
            Main.createRoomWindow(room);
            Main.closeNewRoomWindow();
        } catch (InvalidRoomParameterException e) {
                e.printStackTrace();// TODO Jakies okienko czy cos :)
        }
    }

    private Room buildRoom() throws InvalidRoomParameterException {
        RoomBuilder builder = new RoomBuilder();
        builder.name(gameNameField.getText());
        builder.maxPlayers(getNumberFieldValue(playerNumberField, "Invalid value in number of players field"));
        builder.victory(getNumberFieldValue(pointsToVictoryField, "Invalid value in points to victory field"));
        builder.botConfig(createBotConfig());
        try {
            builder.gameType(GameType.get(gameTypeCombo.getValue()));
        } catch (UnsupportedGameTypeException e) {
            e.printStackTrace();
        }
        return builder.buildRoom();
    }

    private BotConfig createBotConfig() {
        int easyBotCount = Integer.parseInt(easyBotCombo.getValue());
        int hardBotCount = Integer.parseInt(hardBotCombo.getValue());
        return new BotConfig(easyBotCount,hardBotCount);
    }

    private int getNumberFieldValue(TextField field, String message) throws InvalidRoomParameterException {
        try {
            return Integer.parseInt(field.getText());
        } catch (NumberFormatException e) {
            throw new InvalidRoomParameterException(message);
        }
    }

    public void handleBotCountChanged(ActionEvent actionEvent) {
        updateBotCountBoxes();
    }

    public void handleGameTypeChanged(ActionEvent actionEvent) {
    }

    private void setUpGameTypeComboBox() {
        for (GameType g: GameType.values()) {
            gameTypeCombo.getItems().add(g.getName());
            gameTypeCombo.getSelectionModel().select(0);
        }
    }

    public void handlePlayerNumberChanged(Event event) {
        if (!playerNumberField.getText().isEmpty()) {
            this.maxPlayers = Integer.parseInt(playerNumberField.getText());
            System.out.println("Max player number changed to: " + maxPlayers);
            updateBotCountBoxes();
        }
    }

    private void updateBotCountBoxes() {
        remainingPlaces = maxPlayers - placesTakenByAllBots() - 1;
        System.out.println("Remaining places: " + remainingPlaces);

        // String [] required

        manageBoxItems(easyBotCombo);
        manageBoxItems(hardBotCombo);
    }

    private String [] createBoxContent(ComboBox<String> box) {
        int currentPlaces;
        if (!(boxSelectionIsEmpty(box) || box.getValue().equals(""))) {
            currentPlaces = Integer.parseInt(box.getValue());
        } else {
            currentPlaces = 0;
        }
        System.out.println(String.format("Current places in box %s: %d ", box, currentPlaces));

        String[] numbers = new String [remainingPlaces+currentPlaces+1];
        for (int i = 0 ; i <= remainingPlaces + currentPlaces; i++) {
            numbers[i] = Integer.toString(i);
        }
        return numbers;
    }

    private void manageBoxItems(ComboBox<String> box) {
        String [] numbers = createBoxContent(box);
        setBoxItems(box,numbers);
        updateBotCountSelection(box);
    }

    private void setBoxItems(ComboBox<String> box, String [] items) {
        // Due to concurrency problem with JavaFX thread this is a proper way
        // https://stackoverflow.com/questions/26343495/indexoutofboundsexception-while-updating-a-listview-in-javafx
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                box.getItems().setAll(items);
            }
        });
    }

    private void updateBotCountSelection (ComboBox<String> box) {
        if (!boxSelectionIsEmpty(box)) {
            String selection = box.getValue();
            // Check if there is a number selected
            if (!Arrays.asList(createBoxContent(box)).contains(selection)) {
                box.getSelectionModel().clearSelection();
            }
        }
    }

    private int placesTakenByAllBots () {
        return placesTakenByBots(easyBotCombo) + placesTakenByBots(hardBotCombo);
    }

    private int placesTakenByBots (ComboBox<String> box) {
        if (!boxSelectionIsEmpty(box)) {
                if (box.getValue().equals("")) {
                    return 0;
                } else {
                    return Integer.parseInt(box.getValue());
                }
        }
        else return 0;
    }

    private boolean boxSelectionIsEmpty (ComboBox<String> box) {
        return box.getSelectionModel().isEmpty();
    }

}
