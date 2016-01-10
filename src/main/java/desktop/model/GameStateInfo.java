package desktop.model;

import com.google.gson.annotations.SerializedName;
import desktop.model.primitives.Participant;

import java.util.List;

/**
 * Created by Leszek Placzkiewicz on 2015-12-14.
 */
public class GameStateInfo {
    private int pointsToWin;
    private String currentParticipant;
    private String roundWinner;
    private String gameWinner;
    private int currentRoundN;

    @SerializedName("playerInfos")
    private List<Participant> participants;
//    @SerializedName("") - need info from serwer team
//    private List<Spectator> spectators;

    public int getPointsToWin() {
        return pointsToWin;
    }

    public void setPointsToWin(int pointsToWin) {
        this.pointsToWin = pointsToWin;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public String getCurrentParticipant() {
        return currentParticipant;
    }

    public void setCurrentParticipant(String currentParticipant) {
        this.currentParticipant = currentParticipant;
    }

    public String getRoundWinner() {
        return roundWinner;
    }

    public void setRoundWinner(String roundWinner) {
        this.roundWinner = roundWinner;
    }

    public String getGameWinner() {
        return gameWinner;
    }

    public void setGameWinner(String gameWinner) {
        this.gameWinner = gameWinner;
    }

//    public List<Spectator> getSpectators() {
//        return spectators;
//    }
//
//    public void setSpectators(List<Spectator> spectators) {
//        this.spectators = spectators;
//    }

    public int getCurrentRoundN() {
        return currentRoundN;
    }

    public void setCurrentRoundN(int currentRoundN) {
        this.currentRoundN = currentRoundN;
    }
}
