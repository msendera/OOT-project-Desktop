package desktop.model.server_api;

/**
 * Created by Leszek Placzkiewicz on 2015-12-07.
 */
public interface GameControllerFactory {
    /*Game*/void  makeGame(String description, Observer observer);
}
