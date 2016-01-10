package desktop.model;

import desktop.model.adapters.RoomInfoDisplay;
import desktop.model.primitives.GameType;
import desktop.model.primitives.Room;
import javafx.collections.ObservableList;
import org.junit.Test;

import java.util.*;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RoomListTest {

    private final static Logger logger = Logger.getLogger(RoomListTest.class.getName());

    @Test
    public void roomContentTest() {
        for (GameType g: GameType.values()) {
            logger.info("Starting testing game type " + g.toString());

            RoomList list = new RoomList(g);
            List<Room> input = LocalServerConnector.getInstance().getRoomsList();

            logger.info("Received list from server. It contains " + input.size() + " rooms.");

            for (Room r : input) {
                list.checkAndAddRoom(r);
            }
            ObservableList<RoomInfoDisplay> result = list.getRoomsInfos();

            logger.info("Added list to RoomList instance. Resulting list contains " + result.size() + " rooms.");

            for (RoomInfoDisplay r : result) {
                assertEquals(r.getRoom().getGameType(),g);
                logger.info("Room " + r.getName() + " has correct game type");
            }

            logger.info("All rooms in result have correct game type ");


            Set<Room> expectedRoomsSet = new TreeSet<Room>();
            Set<Room> resultSet = new TreeSet<Room>();
            for (RoomInfoDisplay r : result) {
                resultSet.add(r.getRoom());
            }

            for (Room r : input) {
                if (r.getGameType() == g) {
                    expectedRoomsSet.add(r);
                }
            }

            logger.info("Manually filtered the input rooms. Result contains " + expectedRoomsSet.size() + " rooms");

            logger.info("Checking the result vs manual checking ");

            assertTrue(expectedRoomsSet.equals(resultSet));

            logger.info("The result is tested. ");

            logger.info("Game type " + g.toString() + " passed");
        }
    }


}