package desktop.model.primitives;

import desktop.exception.BadDieValueException;
import desktop.exception.BadPointsValueException;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParticipantTest {

    private static DataFactory df = new DataFactory();

    private static Participant participant;

    private static Dice dice;

    public static final String NICK = df.getName();

    private static int [] diceValues;

    private static int points;

    @Before
    public void setUp() {
        buildNewDice();
        participant = new Participant(NICK,dice);
    }

    @Test
    public void testNick() {
        assertEquals(participant.getNick(),NICK);
    }

    @Test
    public void testGetDice() {
        assertEquals(dice,participant.getDice());
    }

    @Test
    public void testSetDice() {
        buildNewDice();
        participant.setDice(dice);
        assertEquals(dice,participant.getDice());
    }

    @Test
    public void testGetPoints() throws BadPointsValueException {
        points = df.getNumberBetween(0,100);
        participant.setPoints(points);
        assertEquals(participant.getPoints(),points);
    }

    @Test
    public void testSetPoints() throws BadPointsValueException {
        points = df.getNumberBetween(0,100);
        participant.setPoints(points);
        assertEquals(participant.getPoints(),points);
    }

    @Test(expected = BadPointsValueException.class)
    public void testAbusiveSetPoints() throws BadPointsValueException {
        points = df.getNumberBetween(-100,-1);
        participant.setPoints(points);
    }

    private void buildNewDice() {
        diceValues = new int[Dice.COUNT];
        for (int i = 0; i < Dice.COUNT; i++) {
            diceValues[i] = df.getNumberBetween(1,6);
        }
        try {
            dice = new Dice(diceValues);
        } catch (BadDieValueException e) {
            e.printStackTrace();
        }
    }
}