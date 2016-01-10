package desktop.model.primitives;


import desktop.model.BotConfig;
import desktop.model.RoomList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.logging.Logger;

public class Room implements Comparable<Room> {

    private final static Logger logger = Logger.getLogger(RoomList.class.getName());

    private int id;

    private StringProperty name;

    private Participant currentParticipant;

    private ObservableList<Participant> participants;

    private ObservableList<Spectator> spectators;

    private GameType gameType;

    private int maxPlayers;

    private BotConfig botConfig;

    private int pointsToVictory;

    private boolean gameState;

    private String roundWinner;

    private String gameWinner;

    public Room() {
        name = new SimpleStringProperty();
        this.participants = FXCollections.observableArrayList();
        this.spectators = FXCollections.observableArrayList();
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
        // TODO: Wywalic
        currentParticipant = participant;
    }

    public void addSpectator(Spectator spectator) {
        spectators.add(spectator);
    }

    public Participant getCurrentParticipant() {
        return currentParticipant;
    }

    public void setCurrentParticipant(Participant currentParticipant) {
        this.currentParticipant = currentParticipant;
    }

    public ObservableList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ObservableList<Participant> participants) {
        this.participants = participants;
    }

    public ObservableList<Spectator> getSpectators() {
        return spectators;
    }

    public void setSpectators(ObservableList<Spectator> spectators) {
        this.spectators = spectators;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public BotConfig getBotConfig() {
        return botConfig;
    }

    public void setBotConfig(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    public int getPointsToVictory() {
        return pointsToVictory;
    }

    public void setPointsToVictory(int pointsToVictory) {
        this.pointsToVictory = pointsToVictory;
    }

    public int getParticipantCount() {
        return participants.size();
    }

    public boolean full() {
        logger.info(String.format("Room %s is full: %b", name, getParticipantCount() == maxPlayers));
        return (getParticipantCount() == maxPlayers);
    }

    public boolean empty() {
        logger.info(String.format("Room %s is empty: %b", name, participants.isEmpty()));
        return participants.isEmpty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Room o) {
        return this.id - o.getId();
    }

    public boolean getGameState() {
        return gameState;
    }

    public void setGameState(boolean gameState) {
        this.gameState = gameState;
    }

    public String getRoundWinner() {
        return roundWinner;
    }

    public void setRoundWinner(String roundWinner) {
        this.roundWinner = roundWinner;
    }

    public String getGameWinner() {
        return gameWinner;
    }

    public void setGameWinner(String gameWinner) {
        this.gameWinner = gameWinner;
    }
}
