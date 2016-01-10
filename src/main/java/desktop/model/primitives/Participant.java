package desktop.model.primitives;

import desktop.exception.BadPointsValueException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Participant extends Player {

    private Dice dice;

    private IntegerProperty points;

    private int rollsInRound;

    public Participant(String nick) {
        this(nick, new Dice());
    }

    public Participant(String nick, Dice dice) {
        super(nick);
        this.points = new SimpleIntegerProperty(0);
        this.dice = dice;
    }

    public Participant(Dice dice) {
        super();
        this.dice = dice;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public int getPoints() {
        return points.get();
    }

    public void setPoints(int points) throws BadPointsValueException {
        if (points >=0) {
            this.points.set(points);
        } else {
            throw new BadPointsValueException(points + " is a bad value. Should be positive");
        }
    }

    public int getRollsInRound() {
        return rollsInRound;
    }

    public void setRollsInRound(int rollsInRound) {
        this.rollsInRound = rollsInRound;
    }
}
