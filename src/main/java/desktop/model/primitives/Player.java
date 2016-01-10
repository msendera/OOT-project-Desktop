package desktop.model.primitives;

public abstract class Player {

    private String nick;

    public Player() {
        this("Player");
    }

    public Player(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
