package desktop.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import desktop.model.primitives.Dice;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiceDisplayController extends Observable {

    @FXML
    public Canvas canvas0;
    @FXML
    public Canvas canvas1;
    @FXML
    public Canvas canvas2;
    @FXML
    public Canvas canvas3;
    @FXML
    public Canvas canvas4;

    private Dice dice;

    private boolean visible;

    private Boolean selectionEnabled = true;

    public void initialize() {
        updateCanvasVisibility();
    }

    public DiceDisplayController () {
        this(new Dice());
    }

    public DiceDisplayController (Dice dice) {
        this.dice = dice;
        setUpSelection();
    }

    public void setUpSelection () {
        for (int i = 0; i < Dice.COUNT; i++) {
            dice.unmarkForRethrow(i);
        }
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
        draw();
    }

    private void drawSingleDice(int index, int value) {
        double size = canvas0.getHeight();
        try {
            getCanvasByNumber(index).getGraphicsContext2D().drawImage(getImageByValue(value),0, 0, size, size);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    private Canvas getCanvasByNumber(int index) throws ReflectiveOperationException {
        Field[] fields = this.getClass().getFields();
        for (Field field : fields) {
            if (field.getName().equals(String.format("canvas%s", index)))
                return (Canvas)field.get(this);
        }
        throw new ReflectiveOperationException ();
    }

    private Image getImageByValue(int value) {
        URL url  = getClass().getResource(String.format("/images/%s.png", Integer.toString(value)));
        return new Image(url.toString());
    }

    /**
     * 5 dices and 6 "holes" (4 between, 1 left side, 1 right side).
     * Let's assume 60% for the dices and 40 % for the holes
     */
    public void draw() {
        double width = canvas0.getWidth();
        double diceWidth = width*0.5;

        for (int i = 0; i < Dice.COUNT; i++) {
            drawSingleDice(i,dice.getValues()[i]);
        }
    }

    public void toggleSelection(int canvasNumber) throws ReflectiveOperationException {
        drawSingleDice(canvasNumber,dice.getValues()[canvasNumber]);

        if (dice.getMarkedForRethrow()[canvasNumber]) {
            dice.unmarkForRethrow(canvasNumber);
        } else {
            drawSelection(canvasNumber);
            dice.markForRethrow(canvasNumber);
        }
    }

    public void drawSelection(int canvasNumber) throws ReflectiveOperationException {
        double size = canvas0.getWidth();
        Canvas canvas = getCanvasByNumber(canvasNumber);
        Color myColor = new Color(Color.ORANGE.getRed(), Color.ORANGE.getGreen(), Color.ORANGE.getBlue(), 0.5);
        canvas.getGraphicsContext2D().setFill(myColor);
        canvas.getGraphicsContext2D().fillRect(0,0,size,size);
    }

    public void handleSelect(Event event) {
        if (selectionEnabled) {
            String name = event.getSource().toString();
            Pattern pattern = Pattern.compile("(\\d+)\\]");
            Matcher matcher = pattern.matcher(name);
            try {
                if (matcher.find()) {
                    int canvasNumber = Integer.parseInt(matcher.group(1));
                    toggleSelection(canvasNumber);
                    setChanged(); //TODO: Bug, concurrency perhaps
                }
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
        notifyObservers();
    }

    public Boolean getSelectionEnabled() {
        return selectionEnabled;
    }

    public void setSelectionEnabled(Boolean selectionEnabled) {
        this.selectionEnabled = selectionEnabled;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        updateCanvasVisibility();
    }

    public void deselectDice() {
        for (int i = 0; i < Dice.COUNT; i++) {
            dice.unmarkForRethrow(i);
        }
        draw();
    }

    private void updateCanvasVisibility() {
        for (int i = 0; i < Dice.COUNT; i++) {
            try {
                getCanvasByNumber(i).setVisible(visible);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }
}
