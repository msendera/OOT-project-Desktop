package desktop.model;

/**
 * Created by Leszek Placzkiewicz on 2015-12-07.
 */
public class ServerConnectorFactory {

    private static IServerConnector serverConnector = null;
    private static boolean local = false;

    public static IServerConnector getServerConnector() {
        if(serverConnector == null) {
            if(local == true)
                serverConnector = LocalServerConnector.getInstance();
            else {
                serverConnector = RemoteServerConnector.getInstance();
            }
        }
        return serverConnector;
    }

    public static void setlocal(boolean l) {
        local = l;
    }

}
