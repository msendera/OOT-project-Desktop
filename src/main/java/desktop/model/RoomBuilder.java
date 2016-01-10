package desktop.model;

import desktop.exception.InvalidRoomParameterException;
import desktop.model.primitives.GameType;
import desktop.model.primitives.Room;

public class RoomBuilder {
    private int id;

    private String name;

    private int maxPlayers;

    private int pointsToVictory ;

    private BotConfig botConfig;

    private GameType gameType;

    private boolean gameStarted;

    public RoomBuilder(){}


    public int getId() {
        return id;
    }

    public RoomBuilder id(int id) {
        this.id = id;
        return this;
    }

    public RoomBuilder name(String name) {
        this.name = name;
        return this;
    }

    public RoomBuilder maxPlayers(int count) {
        this.maxPlayers = count;
        return this;
    }

    public RoomBuilder botConfig(BotConfig botConfig) {
        this.botConfig = botConfig;
        return this;
    }

    public RoomBuilder gameType(GameType gameType) {
        this.gameType = gameType;
        return this;
    }

    public RoomBuilder victory(int pointsToVictory) {
        this.pointsToVictory = pointsToVictory;
        return this;
    }

    public Room buildRoom() throws InvalidRoomParameterException {
        Room room = new Room();
        room.setId(getId());
        room.setName(getName());
        room.setGameType(getGameType());
        room.setMaxPlayers(maxPlayers);
        room.setPointsToVictory(pointsToVictory);
        room.setBotConfig(getBotConfig());
        room.setGameState(getGameStarted());
        return room;
    }

    public String getName() throws InvalidRoomParameterException {
        if (name == null) {
            throw new InvalidRoomParameterException("Name not specified");
        } else {
            return name;
        }
    }

    public int getMaxPlayers() throws InvalidRoomParameterException {
        if (maxPlayers <= 0) {
            throw new InvalidRoomParameterException("Number of players must be bigger than 0");
        } else {
            return maxPlayers;
        }
    }

    public int getPointsToVictory() throws InvalidRoomParameterException {
        if (pointsToVictory <= 0) {
            throw new InvalidRoomParameterException("Number of points required for vicory must be bigger than 0");
        } else {
            return pointsToVictory;
        }
    }

    public BotConfig getBotConfig() throws InvalidRoomParameterException {
        //TODO json z lista gier nie ma info o botach wiec nie mozna ustawic botConfig w RoomBuilderze
        //Rozwiazanie:
        // 1.Nie rzucanie wyjatku - obecnie zastosowane
        // 2. Stworzenie klasy ktora posiadala by tylko info otrzymywane w jsonie z lista gier

//        if (botConfig == null) {
//            throw new InvalidRoomParameterException("Bot config not specified");
//        } else {
//            return botConfig;
//        }
        return botConfig;
    }

    public GameType getGameType() throws InvalidRoomParameterException {
        if (gameType == null) {
            throw new InvalidRoomParameterException("Game type not specified");
        } else {
            return gameType;
        }
    }

    public boolean getGameStarted() {
        return gameStarted;
    }

    public RoomBuilder gameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
        return this;
    }
}
