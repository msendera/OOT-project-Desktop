package desktop.model;

import desktop.model.primitives.Participant;

/** Singleton */
public class AppConfig {

    private static String userNick;

    private static Participant userParticipant;

    private static AppConfig instance = null;

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public static String getUserNick() {
        return userNick;
    }

    public static void setUserNick(String userNick) {
        AppConfig.userNick = userNick;
    }

    public static Participant getUserParticipant() {
        return userParticipant;
    }

    public static void setUserParticipant(Participant userParticipant) {
        AppConfig.userParticipant = userParticipant;
    }
}
