package desktop.model;

import desktop.Main;
import desktop.controller.RoomController;
import desktop.exception.BadDieValueException;
import desktop.exception.InvalidRoomParameterException;
import desktop.model.primitives.Dice;
import desktop.model.primitives.GameType;
import desktop.model.primitives.Participant;
import desktop.model.primitives.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Leszek Placzkiewicz on 2015-12-07.
 */
/** Singleton **/
public class LocalServerConnector implements IServerConnector {

    private final static Logger logger = Logger.getLogger(IServerConnector.class.getName());

    private static List<Room> rooms = new ArrayList<>();
    private static IServerConnector instance = null;

    private static DataFactory df = new DataFactory();

    private LocalServerConnector() {}

    public static IServerConnector getInstance() {
        if(instance == null) {
            instance = new LocalServerConnector();
            populateRoomsList();
        }
        return instance;
    }

    private static void populateRoomsList()  {

        for (int i=0; i<df.getNumberBetween(20,40);i++) {
            try {
                Room room = new RoomBuilder().name(df.getRandomWord(4, 10)).victory(df.getNumberBetween(1, 10)).
                        gameType(generateRandomGameType()).maxPlayers(df.getNumberBetween(6, 12)).
                        botConfig(generateRandomBotConfig()).buildRoom();
                room.setParticipants(generateRandomParticipants(df.getNumberBetween(0,room.getMaxPlayers())));
                if (!room.getParticipants().isEmpty())
                    room.setCurrentParticipant(room.getParticipants().get(0));
                room.setId(i);
                rooms.add(room);
            } catch (InvalidRoomParameterException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static BotConfig generateRandomBotConfig() {
        return new BotConfig(df.getNumberBetween(0,3),df.getNumberBetween(0,3));
    }

    private static GameType generateRandomGameType() {
        int num = df.getNumberBetween(0,4);
        if (num == 1) {
            return GameType.N_PLUS;
        } else if (num == 2) {
            return GameType.N_PRODUCT;
        }
        return GameType.POKER;
    }

    private static ObservableList<Participant> generateRandomParticipants(int count) {
        ObservableList<Participant> participants = FXCollections.observableArrayList();

        for (int i=0; i<count;i++) {
                participants.add(generateRandomParticipant());
        }

        return participants;
    }

    private static Participant generateRandomParticipant() {
        Participant participant = new Participant(df.getName());
        participant.setDice(new Dice());
        return participant;
    }


    @Override
    public void registerClient(String nickname) {
        AppConfig.setUserNick(nickname);
    }

    @Override
    public void unregisterClient() {}

    @Override
    public void createRoom(Room room) {
        int count = room.getBotConfig().getEasyBotCount() + room.getBotConfig().getHardBotCount();
        logger.info(String.format("Creating room %s with %d bots",room.getName(), count));
        for (int i = 0; i < count;i++ ) {
            Participant participant = generateRandomParticipant();
            participant.setNick(df.getRandomText(8,16));
            room.addParticipant(participant);
        }
        rooms.add(room);
    }

    @Override
    public List<Room> getRoomsList() {
        return rooms;
    }

    @Override
    public void joinAsParticipant(long gameId) {
        FXMLLoader loader = Main.getRoomLoader();
        RoomController controller = loader.getController();
        controller.getRoom().addParticipant(AppConfig.getUserParticipant());
    }

    @Override
    public void joinAsSpectator(long gameId) {

    }

    @Override
    public void move(MoveInfo moveInfo) {
        logger.info("Move order sent to server");
        FXMLLoader loader = Main.getRoomLoader();
        RoomController controller = loader.getController();
        Room room = controller.getRoom();
        Participant playerParticipant = AppConfig.getUserParticipant();
        if (room.getCurrentParticipant() == playerParticipant) {
            logger.info("Correct user requested the move");
            boolean [] rethrow = playerParticipant.getDice().getMarkedForRethrow();
            for (int i = 0; i < Dice.COUNT; i++) {
                if (rethrow[i]) {
                    logger.info("Rethrowing dice " + i);
                    try {
                        playerParticipant.getDice().setValue(i, df.getNumberBetween(1,Dice.COUNT));
                    } catch (BadDieValueException e) {
                        e.printStackTrace();
                    }
                }
            }
            room.setCurrentParticipant(room.getParticipants().get(df.getNumberBetween(0, room.getParticipantCount())));
        } else {
            logger.warning("Incorrect user requested the move");
        }
    }

    @Override
    public void quitGame() {
    }
}
