package desktop.model;

import desktop.exception.InvalidRoomParameterException;
import desktop.model.adapters.RoomInfoDisplay;
import desktop.model.primitives.GameType;
import desktop.model.primitives.Room;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoomInfoDisplayTest {

    public static final String NAME = "Pokoj";
    public static final GameType GAME_TYPE = GameType.N_PLUS;
    public static final String GAME_STATUS = ""; // TODO: change to real state
    public static final BotConfig BC = new BotConfig(0 , 0);
    public static final int MAX_PLAYERS = 5;
    public static final int VICTORY = 3;
    public static final String participants = "";

    @Test
    public void creationTest() {
        Room room = buildExampleRoom();
        RoomInfoDisplay info = new RoomInfoDisplay(room);
        assertEquals(info.getName(),NAME);
        assertEquals(info.getGameStatus(),GAME_STATUS);
        assertEquals(info.getParticipantCountStatus(),0+"/"+MAX_PLAYERS);
        assertEquals(info.getPointsToVictory(),Integer.toString(VICTORY));
        assertEquals(info.getParticipants(),"");
    }

    public Room buildExampleRoom() {
        try {
            return (new RoomBuilder().name(NAME).gameType(GAME_TYPE).maxPlayers(MAX_PLAYERS).botConfig(BC).victory(VICTORY).buildRoom());
        } catch (InvalidRoomParameterException e) {
            throw new RuntimeException(e);
        }
    }

}