package desktop.model.server_api;

/**
 * Created by Leszek Placzkiewicz on 2015-12-07.
 */
public interface Observer {
    void stateUpdated(/*GameState state*/);
    void gameEnded();
    void removePlayer(String nickname);
}
