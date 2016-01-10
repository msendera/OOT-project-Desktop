package desktop.model.server_api;

/**
 * Created by Leszek Placzkiewicz on 2015-12-07.
 */
public interface Connector {
    void connect(long port, String ip);
    void removeClient(Client client);
    Client addClient(String nick);
}
