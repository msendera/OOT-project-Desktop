package desktop.model;

/**
 * Created by Leszek Placzkiewicz on 2015-12-15.
 */
public class MoveInfo {

    private int[] dicesToRoll;
    private String nickname;

    public MoveInfo(){}

    public MoveInfo(int[] dicesToRoll, String nickname) {
        this.dicesToRoll = dicesToRoll;
        this.nickname = nickname;
    }

    public int[] getDicesToRoll() {
        return dicesToRoll;
    }

    public void setDicesToRoll(int[] dicesToRoll) {
        this.dicesToRoll = dicesToRoll;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
