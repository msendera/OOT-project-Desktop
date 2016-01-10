package desktop.model.GsonAdapters;

import desktop.exception.InvalidRoomParameterException;
import desktop.model.*;
import desktop.model.primitives.GameType;
import desktop.model.primitives.Room;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by Leszek Placzkiewicz on 2015-12-18.
 */
public class JsonUtilsTest {

    @Test
    public void moveTest() {
        int[] dicesToRoll = new int[4];
        dicesToRoll[0] = 4;
        dicesToRoll[1] = 1;
        dicesToRoll[2] = 2;
        dicesToRoll[3] = 3;

        String nickname = "player1";

        MoveInfo moveInfo = new MoveInfo(dicesToRoll, nickname);

        String result = JsonUtils.prepareMove(moveInfo);

        String expected = "{\"dicesToRoll\":[4,1,2,3],\"nickname\":\"player1\"}";
        assertEquals(expected, result);
    }

    @Test
    public void newRoomTest() throws InvalidRoomParameterException {
        BotConfig botConfig = new BotConfig(4, 2);

        RoomBuilder roomBuilder = new RoomBuilder();

        roomBuilder.name("pokoj1")
                .gameType(GameType.POKER)
                .maxPlayers(7)
                .botConfig(botConfig)
                .victory(1);

        Room room = roomBuilder.buildRoom();

        String result = JsonUtils.newRoom(room);

        String expected =
                "{\"name\":\"pokoj1\",\"gameType\":\"POKER\",\"totalPlayers\":7," +
                        "\"botConfiguration\":{" +
                        "\"DIFFICULT\":2,\"EASY\":4},\"pointsToWin\":1}";

        assertEquals(expected, result);
    }

    @Test
    public void extractRoomsTest() {
        String incomingJson =
                "{" +
                        "\"type\": \"GameList\"," +
                        "\"activeGames\": [" +
                            "{" +
                            "\"gameId\":1, " +
                            "\"gameInfo\":" +
                                "{" +
                                "\"pointsToWin\":14, " +
                                "\"name\":\"pokoj1\"," +
                                "\"gameType\": \"N_PLUS\"," +
                                "\"numberOfBots\":1, " +
                                "\"players\": " +
                                    "[" +
                                        "\"player1\",\"bot1\"" +
                                    "]," +
                                "\"spectators\": [\"spec1\"]," +//TODO moze observers - zalezy jak serwer zdefiniuje
                            "\"expectedNumberOfPlayers\":7," +
                            "\"started\":false" +
                                "}" +
                            "}" +
                        "]" +
                "}";

        List<Room> rooms = JsonUtils.extractRooms(incomingJson);

        assertEquals(rooms.size(), 1);

        Room onlyRoom = rooms.get(0);

        assertEquals(onlyRoom.getId(), 1);
        assertEquals(onlyRoom.getPointsToVictory(), 14);
        assertEquals(onlyRoom.getName(), "pokoj1");
        assertEquals(onlyRoom.getGameType(), GameType.N_PLUS);
        assertEquals(onlyRoom.getParticipants().size(), 2);
        assertEquals(onlyRoom.getParticipants().get(0).getNick(), "player1");
        assertEquals(onlyRoom.getParticipants().get(1).getNick(), "bot1");
        assertEquals(onlyRoom.getSpectators().size(), 1);
        assertEquals(onlyRoom.getSpectators().get(0).getNick(), "spec1");
        assertEquals(onlyRoom.getMaxPlayers(), 7);
        assertEquals(onlyRoom.getGameState(), false);
    }

    @Test
    public void testPokerGameState() {
        String incomingJson =
                "{\"pointsToWin\":4," +
                        "\"playerInfos\": [" +
                        "{ " +
                        "\"nick\": \"player1\"," +
                        "\"rollsInRound\":1," +
                        "\"hand\": {" +
                        "\"dices\": [5, 5, 5, 5, 5]," +
                        "\"description\": \"Five of a Kind (5)\"" +
                        "}," +
                        "\"points\":4" +
                        "}," +
                        "{" +
                        "\"nick\":\"bot1\"," +
                        "\"rollsInRound\": 2," +
                        "\"hand\": {" +
                        "\"dices\": [2, 2, 4, 4, 1]," +
                        "\"description\": \"Two Pair (4, 2)\"" +
                        "}," +
                        "\"points\": 2" +
                        "}]," +//TODO - add spectators
                        "\"currentPlayer\": null," +
                        "\"roundWinner\": \"player1\"," +
                        "\"gameWinner\": null" +
                        "}";

        GameStateInfo gameStateInfo = JsonUtils.extractGameStateInfo(incomingJson);

        int[] player1Dice = new int[5];
        IntStream.range(0, 5).forEach(i -> player1Dice[i] = 5);

        int[] bot1Dice = new int[5];
        bot1Dice[0] = 2;
        bot1Dice[1] = 2;
        bot1Dice[2] = 4;
        bot1Dice[3] = 4;
        bot1Dice[4] = 1;

        assertEquals(gameStateInfo.getPointsToWin(), 4);
        assertEquals(gameStateInfo.getParticipants().size(), 2);
        assertEquals(gameStateInfo.getParticipants().get(0).getRollsInRound(), 1);
        assertArrayEquals(gameStateInfo.getParticipants().get(0).getDice().getValues(), player1Dice);
        assertEquals(gameStateInfo.getParticipants().get(0).getDice().getConfigurationDescription(), "Five of a Kind (5)");
        assertEquals(gameStateInfo.getParticipants().get(0).getPoints(), 4);

        assertEquals(gameStateInfo.getParticipants().get(1).getRollsInRound(), 2);
        assertArrayEquals(gameStateInfo.getParticipants().get(1).getDice().getValues(), bot1Dice);
        assertEquals(gameStateInfo.getParticipants().get(1).getDice().getConfigurationDescription(), "Two Pair (4, 2)");
        assertEquals(gameStateInfo.getParticipants().get(1).getPoints(), 2);

        assertNull(gameStateInfo.getCurrentParticipant());
        assertEquals(gameStateInfo.getRoundWinner(), "player1");
        assertNull(gameStateInfo.getGameWinner());
    }

    @Test
    public void testNGameState() {
        String incomingJson =
                "{\"pointsToWin\":7," +
                        "\"playerInfos\": [" +
                        "{ " +
                        "\"nick\": \"player1\"," +
                        "\"hand\": {" +
                        "\"dices\": [5, 5, 5, 5, 5]," +
                        "\"n\":25" +
                        "}," +
                        "\"points\":4" +
                        "}," +
                        "{" +
                        "\"nick\":\"bot1\"," +
                        "\"hand\": {" +
                        "\"dices\": [2, 2, 4, 4, 1]," +
                        "\"n\":13" +
                        "}," +
                        "\"points\": 2" +
                        "}]," +
                        "\"currentPlayer\": null," +
                        "\"roundWinner\": \"player1\"," +
                        "\"gameWinner\": player1," +
                        "\"currentRoundN\":23" +
                        "}";

        GameStateInfo gameStateInfo = JsonUtils.extractGameStateInfo(incomingJson);

        int[] player1Dice = new int[5];
        IntStream.range(0, 5).forEach(i -> player1Dice[i] = 5);

        int[] bot1Dice = new int[5];
        bot1Dice[0] = 2;
        bot1Dice[1] = 2;
        bot1Dice[2] = 4;
        bot1Dice[3] = 4;
        bot1Dice[4] = 1;

        assertEquals(gameStateInfo.getPointsToWin(), 7);
        assertEquals(gameStateInfo.getParticipants().size(), 2);
        assertArrayEquals(gameStateInfo.getParticipants().get(0).getDice().getValues(), player1Dice);
        assertEquals(gameStateInfo.getParticipants().get(0).getDice().getN(), 25);
        assertEquals(gameStateInfo.getParticipants().get(0).getPoints(), 4);

        assertArrayEquals(gameStateInfo.getParticipants().get(1).getDice().getValues(), bot1Dice);
        assertEquals(gameStateInfo.getParticipants().get(1).getDice().getN(), 13);
        assertEquals(gameStateInfo.getParticipants().get(1).getPoints(), 2);

        assertNull(gameStateInfo.getCurrentParticipant());
        assertEquals(gameStateInfo.getRoundWinner(), "player1");
        assertEquals(gameStateInfo.getGameWinner(), "player1");
        assertEquals(gameStateInfo.getCurrentRoundN(), 23);
    }
}
