package desktop.model.server_api;

/**
 * Created by Leszek Placzkiewicz on 2015-12-07.
 */
public interface Client {
    String getActiveGames();
    String listen();
    void requestCreate(String json);
    void requestMove(String json);
    void requestJoinAsPlayer(long gameId);
    void requestJoinAsObserver(long gameId);
    void requestQuitGame();
}
