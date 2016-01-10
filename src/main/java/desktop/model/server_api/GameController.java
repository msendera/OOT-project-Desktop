package desktop.model.server_api;

/**
 * Created by Leszek Placzkiewicz on 2015-12-07.
 */
public interface GameController {
    void addPlayer(String nickname);
    void makeMove(String moveInfo);
    void removePlayer(String nickname);
    String getGameInfo();
}
