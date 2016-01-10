package desktop.model;

import com.google.gson.*;
import desktop.model.GsonAdapters.ParticipantAdapter;
import desktop.model.GsonAdapters.RoomAdapter;
import desktop.model.primitives.Participant;
import desktop.model.primitives.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leszek Placzkiewicz on 2015-12-07.
 */
public enum JsonUtils {;

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Room.class, new RoomAdapter())
            .registerTypeAdapter(Participant.class, new ParticipantAdapter())
            .create();

    public static String newRoom(Room room) {
        return gson.toJson(room);
    }

    public static List<Room> extractRooms(String roomsJson) {
        JsonParser parser = new JsonParser();

        JsonObject gameListObject = parser.parse(roomsJson).getAsJsonObject();
        JsonArray jArray = gameListObject.getAsJsonArray("activeGames");

        ArrayList<Room> roomList = new ArrayList<Room>();

        for(JsonElement el : jArray) {
            Room room = gson.fromJson(el, Room.class);
            roomList.add(room);
        }
        return roomList;
    }

    public static String prepareMove(MoveInfo moveInfo) {
        return gson.toJson(moveInfo);
    }

    public static GameStateInfo extractGameStateInfo(String gameStateJson) {
        return gson.fromJson(gameStateJson, GameStateInfo.class);
    }
}
