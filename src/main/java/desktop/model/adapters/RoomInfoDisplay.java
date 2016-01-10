package desktop.model.adapters;

/**
 * Intended for use with JavaFX table. This format is required for proper display of table rows.
 * The fields are not necessarily supposed to be strings however, we mitigate a chance of mistake if
 * we make the fields the same.
 */

import desktop.model.primitives.Participant;
import desktop.model.primitives.Room;
import javafx.collections.ObservableList;

public class RoomInfoDisplay {

    private Room room;

    private String name;

    private String participants;

    private String participantCountStatus;

    private String pointsToVictory;

    private String gameStatus;

    public RoomInfoDisplay (Room room) {
        setRoom(room);
    }

    public void setRoom(Room room) {
        this.room = room;
        this.name = room.getName();
        this.participants = createParticipantString(room);
        this.gameStatus = ""; //TODO: Tutaj dac status
        this.participantCountStatus = String.format("%d/%d",room.getParticipantCount(),room.getMaxPlayers());
        this.pointsToVictory = Integer.toString(room.getPointsToVictory());
    }

    private String createParticipantString(Room room) {
        String participantString = new String();
        ObservableList<Participant> participantList = room.getParticipants();
        for (Participant p : participantList) {
            participantString  += p.getNick();
            if (participantList.indexOf(p) != participantList.size()) {
                participantString  += ", ";
            }
        }
        return participantString;
    }

    public Room getRoom() {
        return room;
    }

    public String getName() {
        return name;
    }

    public String getParticipants() {
        return participants;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public String getParticipantCountStatus() {
        return participantCountStatus;
    }

    public String getPointsToVictory() {
        return pointsToVictory;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s", name, gameStatus, participantCountStatus, pointsToVictory, participants);
    }
}
