package desktop.model.primitives;

import desktop.model.BotConfig;
import desktop.model.LocalServerConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Observable;

import static org.junit.Assert.*;

public class RoomTest {

    private static DataFactory df = new DataFactory();

    @Test
    public void testAddParticipant() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        Participant participant = new Participant("Gandalf");
        room.addParticipant(participant);
        assertTrue(room.getParticipants().contains(participant));
    }

    @Test
    public void testAddSpectator() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        Spectator spectator = new Spectator("Gandalf");
        room.addSpectator(spectator);
        assertTrue(room.getSpectators().contains(spectator));
    }

    @Test
    public void testGetCurrentParticipant() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        assertTrue(room.getCurrentParticipant() != null);
    }

    @Test
    public void testSetCurrentParticipant() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        Participant participant = new Participant("Gandalf");
        room.addParticipant(participant);
        room.setCurrentParticipant(participant);
        assertEquals(room.getCurrentParticipant(), participant);
    }

    @Test
    public void testSetAndGetParticipants() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        ObservableList<Participant> participants = FXCollections.observableArrayList();
        for (int i = 0; i < df.getNumberBetween(5,25); i++) {
            participants.add(new Participant(df.getName()));
        }
        room.setParticipants(participants);
        assertEquals(room.getParticipants(),participants);
    }

    @Test
    public void testSetAndGetSpectators() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        ObservableList<Spectator> spectators = FXCollections.observableArrayList();
        for (int i = 0; i < df.getNumberBetween(5,25); i++) {
            spectators.add(new Spectator(df.getName()));
        }
        room.setSpectators(spectators);
        assertEquals(room.getSpectators(),spectators);
    }

    @Test
    public void testSetAndGetGameType() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        GameType gameType = GameType.N_PLUS;
        room.setGameType(gameType);
        assertEquals(room.getGameType(),gameType);
    }


    @Test
    public void testSetAndGetName() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        String name  = df.getBusinessName();
        room.setName(name);
        assertEquals(room.getName(),name);
    }


    @Test
    public void testSetAndGetMaxPlayers() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        int maxPlayers = 15;
        room.setMaxPlayers(15);
        assertEquals(room.getMaxPlayers(), maxPlayers);

    }

    @Test
    public void testSetAndGetBotConfig() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        BotConfig botConfig = room.getBotConfig();
        room = LocalServerConnector.getInstance().getRoomsList().get(1);
        assertNotEquals(room.getBotConfig(),botConfig);
        room.setBotConfig(botConfig);
        assertEquals(room.getBotConfig(),botConfig);
    }

    @Test
    public void testSetAndGetPointsToVictory() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        int victory = 15;
        room.setPointsToVictory(15);
        assertEquals(room.getPointsToVictory(),victory);
    }

    @Test
    public void testGetParticipantCount() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        assertEquals(room.getParticipants().size(), room.getParticipantCount());
    }

    @Test
    public void testFull() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        while (room.getParticipants().size() < room.getMaxPlayers()) {
            room.addParticipant(new Participant(df.getName()));
        }
        assertEquals(room.getParticipants().size(),room.getMaxPlayers());
        assertTrue(room.full());
    }

    @Test
    public void testEmpty() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        room.getParticipants().clear();
        assertEquals(room.getParticipants().size(),0);
        assertTrue(room.empty());
    }

    @Test
    public void testSetAndGetId() throws Exception {
        Room room = LocalServerConnector.getInstance().getRoomsList().get(0);
        int id;
        do {
            id = df.getNumberBetween(0,Integer.MAX_VALUE);
        } while (id == room.getId());
        room.setId(id);
        assertEquals(room.getId(),id);
    }

    @Test
    public void testCompareTo() throws Exception {
        List<Room> rooms = LocalServerConnector.getInstance().getRoomsList();
        for (Room room1 : rooms) {
            for (Room room2: rooms) {
                if (room1.getId() > room2.getId()) {
                    assertTrue(room1.compareTo(room2) > 0);
                } else if (room1.getId() < room2.getId()) {
                    assertTrue(room1.compareTo(room2) < 0);
                } else if (room1.getId() < room2.getId()) {
                    assertTrue(room1.compareTo(room2) == 0);
                }
            }
        }
    }
}