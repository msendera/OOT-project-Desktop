package desktop.model.primitives;

import desktop.exception.BadDieValueException;

import java.util.Random;

public class Dice {

    public static final int COUNT = 5;

    private int [] values = new int[COUNT];

    private boolean [] markedForRethrow = new boolean[COUNT];

    private String configurationDescription;

    private int n;//sum or product of dice values

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    /**
     * Randomizes all values
     */
    public Dice () {
        for (int i = 0 ; i < COUNT; i++) {
            try {
                setValue(i,randomValue());
            } catch (BadDieValueException e) {
                e.printStackTrace();
            }
        }
    }

    public Dice (int [] values) throws BadDieValueException {
        for (int i = 0 ; i < COUNT; i++) {
            setValue(i, values[i]);
        }
    }

    public void setValue (int index, int value) throws BadDieValueException {
        if (value >= 1 && value <=6) {
            this.values[index] = value;
        } else {
            throw new BadDieValueException("Value " + value + " is incorrect for dice " + index);
        }
    }

    private static int randomValue () {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    public void markForRethrow(int index){
        markedForRethrow[index] = true;
    }

    public void unmarkForRethrow(int index) {
        markedForRethrow[index] = false;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) throws BadDieValueException {
        for (int i = 0 ; i < COUNT; i++) {
            setValue(i,values[i]);
        }
    }

    public boolean[] getMarkedForRethrow() {
        return markedForRethrow;
    }

    public void setMarkedForRethrow(boolean[] markedForRethrow) {
        this.markedForRethrow = markedForRethrow;
    }

    public static boolean selectionNotEmpty(Dice dice) {
        boolean notEmpty = false;
        for (int i =0; i < Dice.COUNT; i++) {
            notEmpty |= dice.getMarkedForRethrow()[i];
        }
        return notEmpty;
    }

    public String getConfigurationDescription() {
        return configurationDescription;
    }

    public void setConfigurationDescription(String configurationDescription) {
        this.configurationDescription = configurationDescription;
    }
}
