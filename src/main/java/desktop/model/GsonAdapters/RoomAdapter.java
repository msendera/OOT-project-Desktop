package desktop.model.GsonAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import desktop.exception.InvalidRoomParameterException;
import desktop.model.RoomBuilder;
import desktop.model.primitives.GameType;
import desktop.model.primitives.Participant;
import desktop.model.primitives.Room;
import desktop.model.primitives.Spectator;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leszek Placzkiewicz on 2015-12-12.
 */
public class RoomAdapter extends TypeAdapter<Room>{

    @Override
    public Room read(JsonReader jsonReader) throws IOException {
        RoomBuilder roomBuilder = new RoomBuilder();
        List<Participant> participants = new ArrayList<>();
        List<Spectator> spectators = new ArrayList<>();


        jsonReader.beginObject();
        while(jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals("gameId")) {
                roomBuilder.id(jsonReader.nextInt());
            } else if (name.equals("gameInfo")) {

                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    name = jsonReader.nextName();
                    if (name.equals("pointsToWin")) {
                        roomBuilder.victory(jsonReader.nextInt());
                    } else if (name.equals("name")) {
                        roomBuilder.name(jsonReader.nextString());
                    } else if (name.equals("gameType")) {
                        roomBuilder.gameType(extractGameTypeFromJson(jsonReader.nextString()));
                    } else if (name.equals("expectedNumberOfPlayers")) {
                        roomBuilder.maxPlayers(jsonReader.nextInt());
                    } else if (name.equals("started")) {
                        roomBuilder.gameStarted(jsonReader.nextBoolean());
                    } else if (name.equals("players")) {
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            participants.add(new Participant(jsonReader.nextString()));
                        }
                        jsonReader.endArray();
                    } else if (name.equals("spectators")) {//TODO list of spectators should be in gameState not gameInfo - can be removed from here
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            spectators.add(new Spectator(jsonReader.nextString()));
                        }
                        jsonReader.endArray();
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
            } else {
                jsonReader.skipValue();
            }
        }

        jsonReader.endObject();

        Room r = null;
        try {
            r = roomBuilder.buildRoom();
        } catch (InvalidRoomParameterException e) {
            throw new RuntimeException(e);
        }

        r.setParticipants(FXCollections.observableArrayList(participants));
        r.setSpectators(FXCollections.observableArrayList(spectators));

        return r;
    }

    private GameType extractGameTypeFromJson(String gameType) {
        GameType result = null;
        if (gameType.equals("POKER")) {
            result = GameType.POKER;
        } else if (gameType.equals("N_PLUS")) {
            result = GameType.N_PLUS;
        } else if (gameType.equals("N_STAR")) {
            result = GameType.N_PRODUCT;
        }

        return result;
    }

    @Override
    public void write(JsonWriter jsonWriter, Room room) throws IOException {
        jsonWriter.beginObject();

        jsonWriter.name("name")
                .value(room.getName());

        jsonWriter.name("gameType")
                .value(convertGameNameForJson(room.getGameType()));

        jsonWriter.name("totalPlayers")
                .value(room.getMaxPlayers());

        jsonWriter.name("botConfiguration")
                .beginObject()
                .name("DIFFICULT")
                .value(room.getBotConfig().getHardBotCount())
                .name("EASY")
                .value(room.getBotConfig().getEasyBotCount())
                .endObject();

        jsonWriter.name("pointsToWin")
                .value(room.getPointsToVictory());

        jsonWriter.endObject();
    }

    private String convertGameNameForJson(GameType gameType) {
        String result = null;
        if (gameType == GameType.POKER) {
            result = "POKER";
        } else if(gameType == GameType.N_PLUS) {
            result = "N_PLUS";
        } else if(gameType == GameType.N_PRODUCT) {
            result = "N_STAR";
        }

        return result;
    }
}
