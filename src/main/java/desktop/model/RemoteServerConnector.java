package desktop.model;

/**
 * Created by Leszek Placzkiewicz on 2015-12-07.
 */


import desktop.Main;
import desktop.controller.RoomController;
import desktop.model.primitives.Participant;
import desktop.model.primitives.Room;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import to2.ds.api.Client;
import to2.ds.api.Connector;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.URISyntaxException;
import java.util.List;

/** Singleton **/
public class RemoteServerConnector implements IServerConnector {

    private static int portNumber = 49000;
    private Connector connector = null;
    private Client client = null;

    private static IServerConnector instance = null;

    private RemoteServerConnector(Connector c) {
        connector = c;
    }

    public static IServerConnector getInstance(){
        if (instance == null) {
            try {
                String ipAddress = Inet4Address.getLocalHost().getHostAddress();

                instance = new RemoteServerConnector(Connector.connect(ipAddress, portNumber));
            } catch (IOException | URISyntaxException | DeploymentException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }


    @Override
    public void registerClient(String nickname) {
        try {
            client = connector.addClient(nickname);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unregisterClient() {
        try {
            connector.removeClient(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createRoom(Room room) {
        String newRoom = JsonUtils.newRoom(room);
        try {
            client.requestCreate(newRoom);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Room> getRoomsList() {
        String roomsJson = null;
        try {
            roomsJson = client.getActiveGames();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        return JsonUtils.extractRooms(roomsJson);
    }

    @Override
    public void joinAsParticipant(long gameId) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                client.requestJoinAsPlayer((int) gameId);

                boolean shouldListen = true;

                while (shouldListen) {
                    if (isCancelled()) {
                        break;
                    }

                    String gameStateInfoJson = client.listen();
                    GameStateInfo gameStateInfo = JsonUtils.extractGameStateInfo(gameStateInfoJson);
                    shouldListen = checkShouldListen(gameStateInfo);
                    Platform.runLater(() -> updateGame(gameStateInfo));
                }
                return null;
            }
        };

        runTask(task);
    }

    private boolean checkShouldListen(GameStateInfo gameStateInfo) {
        String currentParticipantNick = gameStateInfo.getCurrentParticipant();

        String myNick = AppConfig.getUserParticipant().getNick();

        return myNick.equals(currentParticipantNick);
    }

    @Override
    public void joinAsSpectator(long gameId) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                client.requestJoinAsObserver((int)gameId);

                while (true) {
                    if(isCancelled()) {
                        break;
                    }

                    String gameStateInfoJson = client.listen();
                    GameStateInfo gameStateInfo = JsonUtils.extractGameStateInfo(gameStateInfoJson);
                    Platform.runLater(() -> updateGame(gameStateInfo));
                }
                return null;
            }
        };

        runTask(task);
    }

    private void updateGame(GameStateInfo gameStateInfo) {
        FXMLLoader loader = Main.getRoomLoader();
        RoomController controller = loader.getController();
        Room room = controller.getRoom();

        List<Participant> participants = gameStateInfo.getParticipants();
        room.setParticipants(FXCollections.observableArrayList(participants));

        String currentParticipant = gameStateInfo.getCurrentParticipant();
        if (currentParticipant != null) {
            Participant participant = room.getParticipants()
                    .stream()
                    .filter(p -> p.getNick().equals(currentParticipant))
                    .findAny()
                    .get();
            room.setCurrentParticipant(participant);
        }

        String roundWinner = gameStateInfo.getRoundWinner();
        if(roundWinner != null) {
            room.setRoundWinner(roundWinner);
        }

        String gameWinner = gameStateInfo.getGameWinner();
        if(gameWinner != null) {
            room.setGameWinner(gameWinner);
        }

        int pointsToWin = gameStateInfo.getPointsToWin();
        room.setPointsToVictory(pointsToWin);

//        gameStateInfo.getCurrentRoundN();
    }

    @Override
    public void move(MoveInfo moveInfo) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String moveJson = JsonUtils.prepareMove(moveInfo);
                client.requestMove(moveJson);

                boolean shouldListen = true;

                while (shouldListen) {
                    if (isCancelled()) {
                        break;
                    }

                    String gameStateInfoJson = client.listen();
                    GameStateInfo gameStateInfo = JsonUtils.extractGameStateInfo(gameStateInfoJson);
                    shouldListen = checkShouldListen(gameStateInfo);
                    Platform.runLater(() -> updateGame(gameStateInfo));
                }
                return null;
            }
        };

        runTask(task);
    }

    @Override
    public void quitGame() {
        try {
            client.requestQuiteGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <E> void runTask(Task<E> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
