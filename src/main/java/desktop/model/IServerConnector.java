package desktop.model;

import desktop.model.primitives.Room;

import java.util.List;

/**
 * Created by Leszek Placzkiewicz on 2015-12-07.
 */
public interface IServerConnector {
    void registerClient(String nickname);
    void unregisterClient();
    void createRoom(Room room);
    List<Room> getRoomsList();
    void joinAsParticipant(long gameId);
    void joinAsSpectator(long gameId);
    void move(MoveInfo moveInfo);
    void quitGame();
}
