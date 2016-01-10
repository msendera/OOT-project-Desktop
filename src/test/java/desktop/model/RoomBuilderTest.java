package desktop.model;

import desktop.model.RoomBuilder;
import org.junit.Test;
import desktop.exception.InvalidRoomParameterException;

public class RoomBuilderTest {

    @Test(expected = InvalidRoomParameterException.class)
    public void nameUnspecifiedTest() throws InvalidRoomParameterException {
        RoomBuilder roomBuilder = new RoomBuilder();
        roomBuilder.getName();
    }

    @Test(expected = InvalidRoomParameterException.class)
    public void botConfigUnspecifiedTest() throws InvalidRoomParameterException {
        RoomBuilder roomBuilder = new RoomBuilder();
        roomBuilder.getBotConfig();
    }


    @Test(expected = InvalidRoomParameterException.class)
    public void gameTypeUnspecifiedTest() throws InvalidRoomParameterException {
        RoomBuilder roomBuilder = new RoomBuilder();
        roomBuilder.getGameType();
    }

    @Test(expected = InvalidRoomParameterException.class)
    public void victoryUnspecifiedTest() throws InvalidRoomParameterException {
        RoomBuilder roomBuilder = new RoomBuilder();
        roomBuilder.getPointsToVictory();
    }

    @Test(expected = InvalidRoomParameterException.class)
    public void maxPlayersUnspecifiedTest() throws InvalidRoomParameterException {
        RoomBuilder roomBuilder = new RoomBuilder();
        roomBuilder.getMaxPlayers();
    }

}