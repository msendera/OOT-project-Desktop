package desktop.model.primitives;

import desktop.exception.BadDieValueException;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiceTest {

    private static DataFactory df = new DataFactory();

    private Dice dice;

    private int[] buildValues() {
        int [] values = new int[Dice.COUNT];
        for (int i = 0; i < Dice.COUNT; i++) {
            values[i] = df.getNumberBetween(1,6);
        }
        return values;
    }

    private boolean[] buildMarked() {
        boolean [] values = new boolean[Dice.COUNT];
        for (int i = 0; i < Dice.COUNT; i++) {
            if (df.getNumberBetween(0,1) == 1)
                values[i] = true;
            else
                values[i] = false;
        }
        return values;
    }

    @Test
    public void randomConstructorTest() throws BadDieValueException {
        dice = new Dice();
        for (int i = 0; i < Dice.COUNT; i++) {
            assertTrue(dice.getValues()[i] <=6 && dice.getValues()[i] >= 1);
        }
    }

    @Test
    public void argumentConstructorTest() throws BadDieValueException {
        int [] values = buildValues();
        dice = new Dice(values);
        for (int i = 0; i < Dice.COUNT; i++) {
            assertEquals(dice.getValues()[i],values[i]);
        }
    }

    @Test(expected = BadDieValueException.class)
    public void AbuseNegativeArgumentConstructorTest() throws BadDieValueException {
        int [] values = buildValues();
        values[df.getNumberBetween(0,Dice.COUNT)] = df.getNumberBetween(-15,0);
        dice = new Dice(values);
    }

    @Test(expected = BadDieValueException.class)
    public void AbuseTooHighArgumentConstructorTest() throws BadDieValueException {
        int [] values = buildValues();
        values[df.getNumberBetween(0,Dice.COUNT)] = df.getNumberBetween(7,35);
        dice = new Dice(values);
    }

    @Test
    public void testSetValue() throws Exception {
        int [] values = buildValues();
        dice = new Dice();
        for (int i = 0; i < Dice.COUNT; i++) {
            dice.setValue(i,values[i]);
        }
        for (int i = 0; i < Dice.COUNT; i++) {
            assertEquals(dice.getValues()[i],values[i]);
        }
    }

    @Test
    public void testMarkForRethrow() throws Exception {
        dice = new Dice();
        for (int i = 0; i < Dice.COUNT; i++) {
            dice.markForRethrow(i);
            assertTrue(dice.getMarkedForRethrow()[i]);
        }
    }

    @Test
    public void testUnmarkForRethrow() throws Exception {
        dice = new Dice();
        for (int i = 0; i < Dice.COUNT; i++) {
            dice.markForRethrow(i);
        }
        for (int i = 0; i < Dice.COUNT; i++) {
            dice.unmarkForRethrow(i);
            assertFalse(dice.getMarkedForRethrow()[i]);
        }
    }

    @Test
    public void testSetAndGetValues() throws Exception {
        int [] values = buildValues();
        dice = new Dice(values);
        for (int i = 0; i < Dice.COUNT; i++) {
            assertEquals(dice.getValues()[i],values[i]);
        }
    }

    @Test
    public void testSetAndGetMarkedForRethrow() throws Exception {
        dice = new Dice();
        boolean [] marked = buildMarked();
        dice.setMarkedForRethrow(marked);
        for (int i = 0; i < Dice.COUNT; i++) {
            assertEquals(dice.getMarkedForRethrow()[i],marked[i]);
        }

    }

    @Test
    public void testSelectionNotEmpty() throws Exception {
        dice = new Dice();
        dice.markForRethrow(df.getNumberBetween(0,5));
        assertTrue(Dice.selectionNotEmpty(dice));
    }
}