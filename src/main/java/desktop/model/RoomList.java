package desktop.model;

import desktop.model.adapters.RoomInfoDisplay;
import desktop.model.primitives.GameType;
import desktop.model.primitives.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomList {

    private final static Logger logger = Logger.getLogger(RoomList.class.getName());

    private ObservableList<RoomInfoDisplay> roomsInfos;

    private ObservableList<Room> rooms;

    private GameType gameType;

    private boolean showFull = true;

    private boolean showEmpty = true;

    public RoomList(GameType gameType) {
        // For logger
        logger.setLevel(Level.WARNING);

        this.gameType = gameType;
        this.rooms = FXCollections.observableArrayList();
        this.roomsInfos = FXCollections.observableArrayList();
        // TODO: Logika do pobierania listy pokoi
//        IServerConnector connector = LocalServerConnector.getInstance();
        IServerConnector connector = ServerConnectorFactory.getServerConnector();
        connector.getRoomsList().forEach(this::checkAndAddRoom);
    }

    public void checkAndAddRoom(Room room) {
        if (!rooms.contains(room)) {
            rooms.add(room);
        }

        if (roomGood(room)) {
            roomsInfos.add(new RoomInfoDisplay(room));
        }
    }

    private void filterRooms() {
        roomsInfos.removeAll();
        rooms.forEach(this::checkAndAddRoom);
    }

    private boolean roomGood(Room room) {
        // This generally an implication
        // roomFull => shouldBeShowingFull
        // The implication changes into:
        // !roomFull || shouldBeShowingFull
        // Same for empty
        return  checkGameType(room) && (showFull || !room.full()) && (showEmpty || !room.empty());
    }

    private boolean checkGameType(Room room) {
        logger.info(String.format("Room %s is correct game type: %s", room.getName(), room.getGameType() == gameType));
        return (room.getGameType() == gameType);
    }

    public boolean isShowFull() {
        return showFull;
    }

    public void setShowFull(boolean showFull) {
        logger.info("Showing full: " + showFull);
        this.showFull = showFull;
        filterRooms();
    }

    public boolean isShowEmpty() {
        return showEmpty;
    }

    public void setShowEmpty(boolean showEmpty) {
        logger.info("Showing empty: " + showEmpty);
        this.showEmpty = showEmpty;
        filterRooms();
    }

    public ObservableList<RoomInfoDisplay> getRoomsInfos() {
        return roomsInfos;
    }

    public ObservableList<Room> getRooms() {
        return rooms;
    }

    public GameType getGameType() {
        return gameType;
    }
}
